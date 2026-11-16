package utils;

import interfaces.TrongFile;
import sanpham.SanPham;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.io.*;
import java.nio.file.*;
import java.util.Scanner;

public class DataBase {
    /** Lớp danh sách ghi từng bản ghi TXT theo thuộc tính. */
    public static synchronized void ghiDoiTuong(Object duLieu, String tenFile) throws IOException {
        KhoDuLieuTxt.ghi(duLieu, tenFile);
    }

    /** Chỉ đọc TXT dạng dòng; file chưa có trả null, file lỗi báo IOException. */
    public static synchronized <T> T docDoiTuong(Class<T> lop, String tenFile) throws IOException {
        if (Files.notExists(Paths.get(tenFile))) return null;
        int[] truoc = BoDemMa.chup();
        try {
            return KhoDuLieuTxt.doc(lop, tenFile);
        } catch (RuntimeException | IOException e) {
            BoDemMa.khoiPhuc(truoc);
            throw new IOException("Không thể đọc " + tenFile + ": " + e.getMessage(), e);
        }
    }

    public static ArrayList<Field> getALlField(Class<?> clazz){
       ArrayList<Field> fields = new ArrayList<>();
       while(clazz != null && clazz != Object.class){
           Field[] declareds = clazz.getDeclaredFields();
           for (Field field : declareds) {
               if (!Modifier.isStatic(field.getModifiers()) && !Modifier.isTransient(field.getModifiers())) {
                   fields.add(field);
               }
           }
           clazz =clazz.getSuperclass();
       }
       return fields;
    }
    public static String getCSVHeader(Object obj){
        Class<?> clazz = obj.getClass();
        ArrayList<Field> fields = getALlField(clazz);
        String header = "";
        for(Field field :fields){
           header += field.getName();
           header += ",";
        }
        if (!header.isEmpty()) header = header.substring(0,header.length()-1);
        return header;
    }
    public static String getCSVLine(Object obj) {
        Class<?> clazz = obj.getClass();
        ArrayList<Field> fields = getALlField(clazz);
        String line = "";
        for (Field field : fields) {
            field.setAccessible(true);
            Object val = null;
            try {
                val = field.get(obj);
            } catch (IllegalAccessException e) {
                System.err.println(e.getStackTrace());
            }
            String valString = (val != null) ? val.toString() : "";
            line += valString;
            line += ',';
        }
        if (!line.isEmpty()) line = line.substring(0,line.length() -1);
        return line;
    }
    public static Object getDataType(String value,Class<?> targetType){
        if (targetType == String.class) return value;
        if (value == null || value.strip().isEmpty() || value.equalsIgnoreCase("null")){
            return null;
        }
        value = value.strip();
        if(targetType == int.class){
            return Integer.parseInt(value);
        }
        else if(targetType == boolean.class){
            if (value.equalsIgnoreCase("true")) return true;
            if (value.equalsIgnoreCase("false")) return false;
            throw new IllegalArgumentException("Giá trị boolean không hợp lệ: " + value);
        }
        else if(targetType == double.class){
            return Double.parseDouble(value);
        }
        else if(targetType == long.class){
            return Long.parseLong(value);
        }
        else if(targetType == LocalDate.class){
            return LocalDate.parse(value);
        }
        System.err.println("co loi");
        return null;
    }
    public static String getFilePath(Class<?> clazz) {
        // Không tạo đối tượng chỉ để hỏi đường dẫn, tránh tăng bộ đếm mã.
        if (clazz == person.DiaChi.class) return DuongDanLuuTru.file("DiaChi.txt");
        if (clazz == dichvu.KhuyenMaiPhanTram.class) return DuongDanLuuTru.file("KhuyenMaiPhanTram.txt");
        if (clazz == dichvu.KhuyenMaiTrucTiep.class) return DuongDanLuuTru.file("KhuyenMaiTrucTiep.txt");
        if (clazz == dichvu.KhuyenMaiMuaTang.class) return DuongDanLuuTru.file("KhuyenMaiMuaTang.txt");
        if (clazz == hoadon.ThanhToanTienMat.class) return DuongDanLuuTru.file("ThanhToanTienMat.txt");
        if (clazz == hoadon.ThanhToanChuyenKhoan.class) return DuongDanLuuTru.file("ThanhToanChuyenKhoan.txt");
        throw new IllegalArgumentException("Lớp chưa khai báo đường dẫn TXT: " + clazz.getName());
    }
    //Class của object cần constuctor rỗng VD public SanPham(){};
    public static <T> T taoObject (Class<T> clazz,String CSVHeader, String CSVLine){
       if (CSVHeader == null || CSVLine == null) {
           System.err.println("Thiếu dữ liệu hoặc tiêu đề bản ghi.");
           return null;
       }
       String[] headers = CSVHeader.split(",", -1);
       String[] values = CSVLine.split("," ,-1);
       if (headers.length != values.length) {
           System.err.println("Số trường dữ liệu không khớp tiêu đề.");
           return null;
       }
       try{
           T inst = clazz.getDeclaredConstructor().newInstance();
           ArrayList<Field> fields = getALlField(clazz);
           for(int i = 0; i < headers.length; i++){
               String header = headers[i].trim();
               String val = values[i].trim();
              for (Field field : fields){
                if(field.getName().equals(header)){
                    field.setAccessible(true);
                    Object value = getDataType(val, field.getType());
                    field.set(inst,value);
                    break;
                }
              }
           }
           return inst;
       } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException | IllegalArgumentException | java.time.DateTimeException e) {
           System.err.println(e.getMessage());
           return null;
       }
    }
    public static <T> T taoObjectTuTerminal(Class<T> clazz, Scanner sc){
        System.out.println("nhap cac gia tri cho" + clazz);
        ArrayList<Field> fields = DataBase.getALlField(clazz);
        try{
            T inst = clazz.getDeclaredConstructor().newInstance();
            for(Field field : fields){
                field.setAccessible(true);
                System.out.println("nhap gia tri: " + field.getName());
                String inputString = sc.nextLine().trim();
                Object value = DataBase.getDataType(inputString,field.getType());
                field.set(inst,value);
            }
            System.out.println("Tao "+ clazz + "thanh cong");
            return inst;
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            return null;
        }
    }
    /** API tương thích: ghi bản ghi tường minh, không lấy thuộc tính bằng reflection. */
    public static void ghiVaoFile(Object obj, String filePath, boolean ghiDe) {
        ArrayList<Object> list = new ArrayList<>();
        list.add(obj);
        ghiVaoFile(list, filePath, ghiDe);
    }
    public static void ghiVaoFile(ArrayList<Object> objs, String filePath, boolean ghiDe) {
        try {
            ArrayList<String> cacDong = new ArrayList<>();
            if (!ghiDe && Files.exists(Paths.get(filePath))) cacDong.addAll(TepVanBan.doc(filePath));
            for (Object obj : objs) {
                if (!(obj instanceof interfaces.BanGhiFile)) throw new IOException("Đối tượng chưa có định dạng dòng.");
                if (obj instanceof hoadon.HoaDon || obj instanceof person.QuanLy || obj instanceof doitac.Nhacungcap)
                    throw new IOException("Hãy ghi đối tượng có danh sách con qua lớp quản lý để giữ đủ dữ liệu.");
                cacDong.add(((interfaces.BanGhiFile) obj).toDongFile());
            }
            TepVanBan.ghi(filePath, "DanhSach", cacDong);
        } catch (IOException e) { throw new UncheckedIOException(e); }
    }
    /** Chỉ hỗ trợ API đọc kiểu cũ; từng họ đối tượng tự nhận diện loại của mình. */
    public static <T> T tuDongFile(Class<T> lop, String dong) {
        Object obj;
        if (sanpham.SanPham.class.isAssignableFrom(lop)) obj = sanpham.SanPham.tuDongFile(dong);
        else if (person.Person.class.isAssignableFrom(lop)) obj = person.Person.tuDongFile(dong);
        else if (dichvu.Khuyenmai.class.isAssignableFrom(lop)) obj = dichvu.Khuyenmai.tuDongFile(dong);
        else if (doitac.Doitac.class.isAssignableFrom(lop)) obj = doitac.Doitac.tuDongFile(dong);
        else if (hoadon.HoaDon.class.isAssignableFrom(lop)) obj = hoadon.HoaDon.tuDongFile(dong);
        else if (hoadon.CTHoaDon.class.isAssignableFrom(lop)) obj = hoadon.CTHoaDon.tuDongFile(dong);
        else if (lop == person.DiaChi.class) obj = person.DiaChi.tuDongFile(dong);
        else if (lop == dichvu.SanPhamKhuyenMai.class) obj = dichvu.SanPhamKhuyenMai.tuDongFile(dong);
        else if (lop == hoadon.ThanhToanTienMat.class) obj = hoadon.ThanhToanTienMat.tuDongFile(dong);
        else if (lop == hoadon.ThanhToanChuyenKhoan.class) obj = hoadon.ThanhToanChuyenKhoan.tuDongFile(dong);
        else if (lop == YeuCau.class) obj = YeuCau.tuDongFile(dong);
        else throw new IllegalArgumentException("Hãy đọc đối tượng qua lớp quản lý danh sách tương ứng.");
        if (!lop.isInstance(obj)) throw new IllegalArgumentException("Dòng chứa sai loại dữ liệu.");
        return lop.cast(obj);
    }
    public static void ghiVanBan(String filePath, String content, boolean ghiDe) {
        Path file = Paths.get(filePath);
        try {
            Path parent = file.toAbsolutePath().getParent();
            Files.createDirectories(parent);
            try (BufferedWriter writer = Files.newBufferedWriter(file, java.nio.charset.StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE, ghiDe ? StandardOpenOption.TRUNCATE_EXISTING : StandardOpenOption.APPEND)) {
                writer.write(content);
            }
        } catch (IOException e) { throw new UncheckedIOException(e); }
    }
    public static <T> ArrayList<T> docfile(Class<T> clazz, String filePath) {
        ArrayList<T> values = new ArrayList<>();
        try {
            for (String dong : TepVanBan.doc(filePath)) {
                values.add(tuDongFile(clazz, dong));
            }
            return values;
        } catch (IOException e) { throw new UncheckedIOException(e); }
    }
    public static <T> ArrayList<T> docfile(Class<T> clazz) {
        String file = getFilePath(clazz);
        if (file == null) throw new IllegalArgumentException("Lớp chưa khai báo đường dẫn file.");
        return docfile(clazz, file);
    }
    public static <T> void suaObjectTrongArrayList(ArrayList<T> objs, int index){
        if (objs == null || index < 0 || index >= objs.size() || objs.get(index) == null) {
            System.out.println("Không tìm thấy đối tượng tại vị trí cần sửa!");
            return;
        }
        T obj = objs.get(index);
        ArrayList<Field> fields = DataBase.getALlField(obj.getClass());
        System.out.println("Cac gia tri co the sua");
        for(Field field : fields){
            field.setAccessible(true);
            System.out.println("STT:" +fields.indexOf(field) + " , " +field.getName());
        }
        //lay input stt tu user
        int stt =1;

        if (stt >= fields.size()) {
            System.out.println("Không có thuộc tính tại vị trí cần sửa!");
            return;
        }
        Field toBeEdit = fields.get(stt);
        //lay input gia tri moi tu user
        try {
            toBeEdit.set(obj,null);
        } catch (IllegalAccessException | IllegalArgumentException e) {
            System.err.println(e.getMessage());
        }
    }
}
