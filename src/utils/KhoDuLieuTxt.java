package utils;

import dichvu.*;
import hoadon.*;
import person.*;
import sanpham.*;
import interfaces.BanGhiFile;
import java.io.IOException;
import java.util.*;

/** Điều phối tới lớp danh sách; không tuần tự hóa đồ thị đối tượng. */
public final class KhoDuLieuTxt {
    private KhoDuLieuTxt() { }
    public static void ghi(Object obj, String file) throws IOException {
        try {
            if (obj instanceof QuanLySanPham) ((QuanLySanPham) obj).ghiFile(file);
            else if (obj instanceof DSNhanVien) ((DSNhanVien) obj).ghiFile(file);
            else if (obj instanceof DSKhachHang) ((DSKhachHang) obj).ghiFile(file);
            else if (obj instanceof Danhsachdoitac) ((Danhsachdoitac) obj).ghiFile(file);
            else if (obj instanceof Danhsachkhuyenmai) ((Danhsachkhuyenmai) obj).ghiFile(file);
            else if (obj instanceof DanhSachSPKM) ((DanhSachSPKM) obj).ghiFile(file);
            else if (obj instanceof DanhSachHoaDonBan) ((DanhSachHoaDonBan) obj).ghiFile(file);
            else if (obj instanceof DanhSachHoaDonNhap) ((DanhSachHoaDonNhap) obj).ghiFile(file);
            else if (obj instanceof DanhSachCongNo) ((DanhSachCongNo) obj).ghiFile(file);
            else if (obj instanceof DSYeuCau) ((DSYeuCau) obj).ghiFile(file);
            else if (obj instanceof NhanVien[]) ghiVaiTro((NhanVien[]) obj, file);
            else if (obj instanceof String) TepVanBan.ghi(file, "Chuoi", Collections.singletonList(DongDuLieu.ghep("CHUOI", obj)));
            else if (obj instanceof BanGhiFile && !(obj instanceof HoaDon) && !(obj instanceof QuanLy) && !(obj instanceof Nhacungcap)) {
                TepVanBan.ghi(file, "BanGhi", Collections.singletonList(((BanGhiFile) obj).toDongFile()));
            } else throw new IOException("Loại dữ liệu chưa có hàm ghi TXT.");
        } catch (RuntimeException e) { throw new IOException("Không ghi được " + file + ": " + e.getMessage(), e); }
    }
    public static <T> T doc(Class<T> type, String file) throws IOException {
        Object obj;
        switch (TepVanBan.nhom(file)) {
            case "QuanLySanPham": { QuanLySanPham ds = new QuanLySanPham(); ds.docFile(file); obj = ds; break; }
            case "DSNhanVien": { DSNhanVien ds = new DSNhanVien(); ds.docFile(file); obj = ds; break; }
            case "DSKhachHang": { DSKhachHang ds = new DSKhachHang(); ds.docFile(file); obj = ds; break; }
            case "Danhsachdoitac": { Danhsachdoitac ds = new Danhsachdoitac(); ds.docFile(file); obj = ds; break; }
            case "Danhsachkhuyenmai": { Danhsachkhuyenmai ds = new Danhsachkhuyenmai(); ds.docFile(file); obj = ds; break; }
            case "DanhSachSPKM": { DanhSachSPKM ds = new DanhSachSPKM(); ds.docFile(file); obj = ds; break; }
            case "DanhSachHoaDonBan": { DanhSachHoaDonBan ds = new DanhSachHoaDonBan(); ds.docFile(file); obj = ds; break; }
            case "DanhSachHoaDonNhap": { DanhSachHoaDonNhap ds = new DanhSachHoaDonNhap(); ds.docFile(file); obj = ds; break; }
            case "DanhSachCongNo": { DanhSachCongNo ds = new DanhSachCongNo(); ds.docFile(file); obj = ds; break; }
            case "YeuCau": { DSYeuCau ds = new DSYeuCau(); ds.docFile(file); obj = ds; break; }
            case "VaiTro": obj = docVaiTro(file); break;
            case "Chuoi": {
                List<String> dong = TepVanBan.doc(file);
                if (dong.size() != 1) throw new IOException("Cần một dòng CHUOI.");
                String[] p = dong.get(0).split(";", -1);
                if (p.length != 2 || !p[0].equals("CHUOI")) throw new IOException("Dòng CHUOI không hợp lệ.");
                obj = p[1]; break;
            }
            case "BanGhi": {
                List<String> dong = TepVanBan.doc(file);
                if (dong.size() != 1) throw new IOException("Cần một dòng đối tượng.");
                obj = DataBase.tuDongFile(type, dong.get(0)); break;
            }
            default: throw new IOException("Nhóm dữ liệu không được hỗ trợ.");
        }
        if (!type.isInstance(obj)) throw new IOException("File chứa sai loại dữ liệu.");
        return type.cast(obj);
    }
    public static void ghiVaiTro(NhanVien[] vaiTro, String file) throws IOException {
        kiemTraVaiTro(vaiTro);
        List<String> cacDong = new ArrayList<>();
        for (NhanVien nv : vaiTro) {
            cacDong.add(nv.toDongFile());
            if (nv instanceof QuanLy) {
                for (YeuCau yc : ((QuanLy) nv).getDsYeuCauFile().getList()) {
                    cacDong.add("YCQUANLY;" + nv.getMaNV() + ";" + yc.toDongFile());
                }
            }
        }
        TepVanBan.ghi(file, "VaiTro", cacDong);
    }
    public static NhanVien[] docVaiTro(String file) throws IOException {
        int[] truoc = BoDemMa.chup();
        try {
            DSNhanVien ds = new DSNhanVien();
            ds.docFile(file);
            NhanVien[] result = ds.getDanhSachNhanVien().toArray(new NhanVien[0]);
            kiemTraVaiTro(result);
            return result;
        } catch (IOException | RuntimeException e) {
            BoDemMa.khoiPhuc(truoc);
            throw new IOException("Không thể đọc nhân viên đăng nhập: " + file, e);
        }
    }
    private static void kiemTraVaiTro(NhanVien[] v) throws IOException {
        if (v.length != 4 || !(v[0] instanceof QuanLy) || !(v[1] instanceof QuanKho)
                || !(v[2] instanceof ThuNgan) || !(v[3] instanceof KeToan)) {
            throw new IOException("Cần bốn vai trò: quản lý, quản kho, thu ngân, kế toán.");
        }
        Set<String> ma = new HashSet<>();
        for (NhanVien nv : v) {
            if (nv.getMaNV() == null || !ma.add(nv.getMaNV().toUpperCase(Locale.ROOT))) {
                throw new IOException("Trùng hoặc thiếu mã nhân viên đăng nhập.");
            }
        }
    }
}
