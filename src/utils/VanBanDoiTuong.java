package utils;

import org.w3c.dom.*;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.lang.reflect.*;
import java.time.*;
import java.util.*;

/** Văn bản XML UTF-8: giữ tên thuộc tính, lớp con, mảng và tham chiếu chung. */
public class VanBanDoiTuong {
    public static void ghi(Object obj, OutputStream stream) throws IOException {
        try {
            Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
            Element root = doc.createElement("du-lieu");
            root.setAttribute("phien-ban", "1");
            doc.appendChild(root);
            root.appendChild(ghiGiaTri(doc, obj, new IdentityHashMap<>()));
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            transformer.transform(new DOMSource(doc), new StreamResult(stream));
        } catch (Exception e) {
            throw new IOException("Không thể ghi dữ liệu văn bản: " + e.getMessage(), e);
        }
    }

    public static Object doc(InputStream stream) throws IOException {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
            factory.setXIncludeAware(false);
            factory.setExpandEntityReferences(false);
            var builder = factory.newDocumentBuilder();
            builder.setErrorHandler(new org.xml.sax.helpers.DefaultHandler() {
                @Override public void error(org.xml.sax.SAXParseException e) throws org.xml.sax.SAXException { throw e; }
                @Override public void fatalError(org.xml.sax.SAXParseException e) throws org.xml.sax.SAXException { throw e; }
            });
            Element root = builder.parse(stream).getDocumentElement();
            if (!root.getTagName().equals("du-lieu") || !root.getAttribute("phien-ban").equals("1")) {
                throw new IOException("File văn bản không đúng định dạng.");
            }
            return docGiaTri(con(root).get(0), new HashMap<>());
        } catch (Exception e) {
            throw new IOException("Không thể đọc dữ liệu văn bản: " + e.getMessage(), e);
        }
    }

    private static Element ghiGiaTri(Document doc, Object obj, IdentityHashMap<Object, String> daGhi)
            throws ReflectiveOperationException, IOException {
        if (obj == null) return doc.createElement("rong");
        Class<?> lop = obj.getClass();
        if (obj instanceof String || obj instanceof Number || obj instanceof Boolean
                || obj instanceof Character || obj instanceof LocalDate || obj instanceof LocalDateTime) {
            Element e = doc.createElement("gia-tri");
            e.setAttribute("kieu", lop.getName());
            e.setTextContent(obj.toString());
            return e;
        }
        if (daGhi.containsKey(obj)) {
            Element e = doc.createElement("tham-chieu");
            e.setAttribute("ma", daGhi.get(obj));
            return e;
        }
        String ma = Integer.toString(daGhi.size() + 1);
        daGhi.put(obj, ma);
        Element e = doc.createElement(lop.isArray() ? "mang" : obj instanceof List ? "danh-sach"
                : obj instanceof Map ? "anh-xa" : "doi-tuong");
        e.setAttribute("ma", ma);
        e.setAttribute("lop", lop.isArray() ? lop.getComponentType().getName() : lop.getName());
        if (lop.isArray()) {
            for (int i = 0; i < Array.getLength(obj); i++) e.appendChild(ghiGiaTri(doc, Array.get(obj, i), daGhi));
        } else if (obj instanceof List) {
            for (Object val : (List<?>) obj) e.appendChild(ghiGiaTri(doc, val, daGhi));
        } else if (obj instanceof Map) {
            for (var entry : ((Map<?, ?>) obj).entrySet()) {
                Element cap = doc.createElement("cap");
                cap.appendChild(ghiGiaTri(doc, entry.getKey(), daGhi));
                cap.appendChild(ghiGiaTri(doc, entry.getValue(), daGhi));
                e.appendChild(cap);
            }
        } else {
            kiemTraLop(lop.getName());
            for (Field field : DataBase.getALlField(lop)) {
                field.setAccessible(true);
                Element thuocTinh = doc.createElement("thuoc-tinh");
                thuocTinh.setAttribute("ten", field.getName());
                thuocTinh.setAttribute("khai-bao", field.getDeclaringClass().getName());
                thuocTinh.appendChild(ghiGiaTri(doc, field.get(obj), daGhi));
                e.appendChild(thuocTinh);
            }
        }
        return e;
    }

    private static Object docGiaTri(Element e, Map<String, Object> daDoc) throws ReflectiveOperationException, IOException {
        String tag = e.getTagName();
        if (tag.equals("rong")) return null;
        if (tag.equals("gia-tri")) {
            String val = e.getTextContent();
            switch (e.getAttribute("kieu")) {
                case "java.lang.String": return val;
                case "java.lang.Integer": return Integer.valueOf(val);
                case "java.lang.Long": return Long.valueOf(val);
                case "java.lang.Double": return Double.valueOf(val);
                case "java.lang.Float": return Float.valueOf(val);
                case "java.lang.Short": return Short.valueOf(val);
                case "java.lang.Byte": return Byte.valueOf(val);
                case "java.lang.Boolean":
                    if (!val.equals("true") && !val.equals("false")) throw new IOException("Boolean không hợp lệ.");
                    return Boolean.valueOf(val);
                case "java.lang.Character":
                    if (val.length() != 1) throw new IOException("Ký tự không hợp lệ.");
                    return val.charAt(0);
                case "java.time.LocalDate": return LocalDate.parse(val);
                case "java.time.LocalDateTime": return LocalDateTime.parse(val);
                default: throw new IOException("Kiểu giá trị không được hỗ trợ.");
            }
        }
        if (tag.equals("tham-chieu")) {
            if (!daDoc.containsKey(e.getAttribute("ma"))) throw new IOException("Tham chiếu không tồn tại.");
            return daDoc.get(e.getAttribute("ma"));
        }
        ArrayList<Element> cacCon = con(e);
        String ma = e.getAttribute("ma");
        if (ma.isEmpty() || daDoc.containsKey(ma)) throw new IOException("Mã đối tượng không hợp lệ.");
        Object obj;
        if (tag.equals("mang")) {
            obj = Array.newInstance(lop(e.getAttribute("lop")), cacCon.size());
        } else if (tag.equals("danh-sach")) {
            obj = new ArrayList<>();
        } else if (tag.equals("anh-xa")) {
            obj = new LinkedHashMap<>();
        } else if (tag.equals("doi-tuong")) {
            Constructor<?> constructor = lop(e.getAttribute("lop")).getDeclaredConstructor();
            constructor.setAccessible(true);
            obj = constructor.newInstance();
        } else throw new IOException("Nút dữ liệu không hợp lệ: " + tag);
        if (tag.equals("doi-tuong") && cacCon.size() != DataBase.getALlField(obj.getClass()).size()) {
            throw new IOException("Đối tượng thiếu hoặc thừa thuộc tính: " + obj.getClass().getSimpleName());
        }
        daDoc.put(ma, obj);
        Set<String> thuocTinhDaDoc = new HashSet<>();
        for (int i = 0; i < cacCon.size(); i++) {
            Element c = cacCon.get(i);
            if (tag.equals("mang")) Array.set(obj, i, docGiaTri(c, daDoc));
            else if (tag.equals("danh-sach")) themDanhSach(obj, docGiaTri(c, daDoc));
            else if (tag.equals("anh-xa")) {
                ArrayList<Element> cap = con(c);
                themAnhXa(obj, docGiaTri(cap.get(0), daDoc), docGiaTri(cap.get(1), daDoc));
            } else {
                if (!c.getTagName().equals("thuoc-tinh") || !thuocTinhDaDoc.add(
                        c.getAttribute("khai-bao") + "#" + c.getAttribute("ten"))) {
                    throw new IOException("Thuộc tính bị trùng hoặc sai định dạng.");
                }
                Field field = lop(c.getAttribute("khai-bao")).getDeclaredField(c.getAttribute("ten"));
                if (!field.getDeclaringClass().isInstance(obj) || Modifier.isStatic(field.getModifiers())
                        || Modifier.isTransient(field.getModifiers())) throw new IOException("Thuộc tính không hợp lệ.");
                field.setAccessible(true);
                Element duLieu = con(c).get(0);
                Object val = docGiaTri(duLieu, daDoc);
                if (Modifier.isFinal(field.getModifiers())) {
                    // Giữ danh sách final có sẵn, chỉ nạp lại các phần tử công nợ.
                    Object hienTai = field.get(obj);
                    if (!(hienTai instanceof List) || !(val instanceof List)) {
                        throw new IOException("Không thể khôi phục thuộc tính final: " + field.getName());
                    }
                    thayDanhSach(hienTai, val);
                    daDoc.put(duLieu.getAttribute("ma"), hienTai);
                } else field.set(obj, val);
            }
        }
        return obj;
    }

    @SuppressWarnings("unchecked")
    private static void themDanhSach(Object list, Object val) { ((List<Object>) list).add(val); }
    @SuppressWarnings("unchecked")
    private static void themAnhXa(Object map, Object key, Object val) { ((Map<Object, Object>) map).put(key, val); }
    @SuppressWarnings("unchecked")
    private static void thayDanhSach(Object list, Object val) {
        ((List<Object>) list).clear();
        ((List<Object>) list).addAll((List<Object>) val);
    }

    private static Class<?> lop(String ten) throws ClassNotFoundException, IOException {
        switch (ten) {
            case "int": return int.class;
            case "long": return long.class;
            case "double": return double.class;
            case "boolean": return boolean.class;
            default: kiemTraLop(ten); return Class.forName(ten);
        }
    }

    private static void kiemTraLop(String ten) throws IOException {
        if (!(ten.equals("GiaoDien") || ten.equals("utils.DataBase$BanLuu") || ten.equals("utils.YeuCau")
                || ten.startsWith("person.") || ten.startsWith("sanpham.") || ten.startsWith("dichvu.")
                || ten.startsWith("hoadon."))) throw new IOException("Lớp không được hỗ trợ: " + ten);
    }

    private static ArrayList<Element> con(Element e) {
        ArrayList<Element> list = new ArrayList<>();
        for (Node n = e.getFirstChild(); n != null; n = n.getNextSibling()) {
            if (n instanceof Element) list.add((Element) n);
        }
        return list;
    }
}
