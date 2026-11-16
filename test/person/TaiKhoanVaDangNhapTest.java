package person;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TaiKhoanVaDangNhapTest {
    private DSTaiKhoan dsTaiKhoan;
    private DSNhanVien dsNhanVien;

    @BeforeEach
    public void setUp() {
        dsTaiKhoan = new DSTaiKhoan();
        dsTaiKhoan.them(new TaiKhoan("NV001", "quanly@123", "QuanLy"));
        dsTaiKhoan.them(new TaiKhoan("NV002", "quankho@123", "QuanKho"));
        dsTaiKhoan.them(new TaiKhoan("NV003", "thungan@123", "ThuNgan"));
        dsTaiKhoan.them(new TaiKhoan("NV004", "ketoan@123", "KeToan"));
        dsNhanVien = new DSNhanVien();

        // Tạo sẵn các nhân viên tương ứng 4 vai trò
        QuanLy ql = new QuanLy();
        ql.setMaNV("NV001");
        ql.setHoten("Nguyễn Quản Lý");

        QuanKho qk = new QuanKho();
        qk.setMaNV("NV002");
        qk.setHoten("Trần Quản Kho");

        ThuNgan tn = new ThuNgan();
        tn.setMaNV("NV003");
        tn.setHoten("Lê Thu Ngân");

        KeToan kt = new KeToan();
        kt.setMaNV("NV004");
        kt.setHoten("Phạm Kế Toán");

        dsNhanVien.getDanhSachNhanVien().add(ql);
        dsNhanVien.getDanhSachNhanVien().add(qk);
        dsNhanVien.getDanhSachNhanVien().add(tn);
        dsNhanVien.getDanhSachNhanVien().add(kt);

        dsTaiKhoan.cauHinh(dsNhanVien);
    }

    @Test
    public void testVaiTroTrongPersonVaCacLopCon() {
        // Kiểm tra biến vai trò trong Person thông qua lớp con NhanVien
        NhanVien nv = new NhanVien();
        nv.setVaiTro("QuanLy");
        assertEquals("QuanLy", nv.getVaiTro(), "Person/NhanVien getVaiTro phải trả về đúng vai trò đã set");

        // Kiểm tra KhachHang kế thừa Person và có vai trò Khách hàng
        KhachHang kh = new KhachHang();
        assertEquals("Khách hàng", kh.getVaiTro(), "KhachHang phải có vai trò là 'Khách hàng'");

        // Kiểm tra các vai trò chuyên biệt kế thừa Person/NhanVien
        QuanLy ql = new QuanLy();
        assertEquals("QuanLy", ql.getVaiTro(), "QuanLy phải có vai trò QuanLy");

        QuanKho qk = new QuanKho();
        assertEquals("QuanKho", qk.getVaiTro(), "QuanKho phải có vai trò QuanKho");

        ThuNgan tn = new ThuNgan();
        assertEquals("ThuNgan", tn.getVaiTro(), "ThuNgan phải có vai trò ThuNgan");

        KeToan kt = new KeToan();
        assertEquals("KeToan", kt.getVaiTro(), "KeToan phải có vai trò KeToan");
    }

    @Test
    public void testTaiKhoanDungTrucTiepMaNVLamTenDangNhap() {
        // Khởi tạo tài khoản dùng trực tiếp mã NV làm định danh/tên đăng nhập
        TaiKhoan tk = new TaiKhoan("NV001", "quanly@123", "QuanLy");
        assertEquals("NV001", tk.getMaNV());
        // getTenDangNhap() trả về maNV
        assertEquals("NV001", tk.getTenDangNhap());
        assertEquals("quanly@123", tk.getMatKhau());
        assertEquals("QuanLy", tk.getVaiTro());
        assertTrue(tk.isTrangThai());

        assertTrue(tk.kiemTraMatKhau("quanly@123"));
        assertFalse(tk.kiemTraMatKhau("sai_mat_khau"));

        // Khi đổi mã NV
        tk.setMaNV("NV099");
        assertEquals("NV099", tk.getMaNV());
        assertEquals("NV099", tk.getTenDangNhap());

        // Test đọc ghi dòng file
        String dong = tk.toDongFile();
        TaiKhoan parsed = TaiKhoan.tuDongFile(dong);
        assertEquals(tk.getMaNV(), parsed.getMaNV());
        assertEquals(tk.getTenDangNhap(), parsed.getTenDangNhap());
        assertEquals(tk.getMatKhau(), parsed.getMatKhau());
        assertEquals(tk.getVaiTro(), parsed.getVaiTro());
        assertEquals(tk.isTrangThai(), parsed.isTrangThai());
    }

    @Test
    public void testChucNangThemXoaSuaDSTaiKhoan() {
        // Thêm tài khoản mới
        TaiKhoan tkMoi = new TaiKhoan("NV005", "pass123", "ThuNgan");
        assertTrue(dsTaiKhoan.them(tkMoi), "Phải thêm được tài khoản mới");
        assertNotNull(dsTaiKhoan.timTheoMaNV("NV005"));

        // Thêm trùng mã NV phải bị từ chối
        TaiKhoan tkTrungMa = new TaiKhoan("NV005", "pass", "ThuNgan");
        assertFalse(dsTaiKhoan.them(tkTrungMa), "Không được thêm trùng mã NV");

        // Sửa tài khoản (đổi mật khẩu, vai trò, trạng thái)
        boolean suaThanhCong = dsTaiKhoan.sua("NV005", "pass_moi", "KeToan", true);
        assertTrue(suaThanhCong, "Phải sửa được tài khoản");
        TaiKhoan tkDaSua = dsTaiKhoan.timTheoMaNV("NV005");
        assertEquals("NV005", tkDaSua.getMaNV());
        assertEquals("pass_moi", tkDaSua.getMatKhau());
        assertEquals("KeToan", tkDaSua.getVaiTro());

        // Xóa mềm (khóa tài khoản)
        assertTrue(dsTaiKhoan.xoa("NV005"), "Xóa mềm thành công");
        assertFalse(dsTaiKhoan.timTheoMaNV("NV005").isTrangThai(), "Tài khoản sau khi xóa mềm phải chuyển sang trạng thái khóa");

        // Xóa hoàn toàn
        assertTrue(dsTaiKhoan.xoaHoanToan("NV005"), "Xóa hoàn toàn khỏi danh sách");
        assertNull(dsTaiKhoan.timTheoMaNV("NV005"), "Không còn tìm thấy sau khi xóa hoàn toàn");
    }

    @Test
    public void testTimKiemDSTaiKhoan() {
        // Tìm trực tiếp bằng mã nhân viên
        assertNotNull(dsTaiKhoan.timKiem("NV001"), "Tìm theo mã NV001");
        assertNotNull(dsTaiKhoan.timTheoMaNV("NV001"), "Tìm theo mã NV001");
        assertEquals("NV001", dsTaiKhoan.timKiem("NV001").getMaNV());

        // Hỗ trợ alias tên cũ
        assertNotNull(dsTaiKhoan.timKiem("QuanLySieuThi"), "Tìm theo alias QuanLySieuThi");
        assertEquals(dsTaiKhoan.timKiem("NV001"), dsTaiKhoan.timKiem("QuanLySieuThi"));

        assertNotNull(dsTaiKhoan.timKhoa("ThuNgan"));
        assertNotNull(dsTaiKhoan.timKhoa("KeToan"));
        assertNull(dsTaiKhoan.timKiem("KHONG_TON_TAI"));
    }

    @Test
    public void testDongBoVoiNhanVien() {
        NhanVien nvMoi = new ThuNgan();
        nvMoi.setMaNV("NV010");
        nvMoi.setHoten("Nguyễn Văn Mới");
        dsNhanVien.getDanhSachNhanVien().add(nvMoi);

        dsTaiKhoan.dongBoVoiNhanVien();

        TaiKhoan tkAuto = dsTaiKhoan.timTheoMaNV("NV010");
        assertNotNull(tkAuto, "Tự động đồng bộ tài khoản cho nhân viên mới");
        assertEquals("NV010", tkAuto.getMaNV(), "Tên đăng nhập trực tiếp là mã nhân viên NV010");
        assertEquals("ThuNgan", tkAuto.getVaiTro());
        assertTrue(tkAuto.kiemTraMatKhau("nv010@123"));
    }

    @Test
    public void testKiemTraLogicXetVaiTroTruocRoiMoiToiMatKhau() {
        // Giả lập logic kiểm tra khi đăng nhập:
        // Đăng nhập vào menu ThuNgan ("ThuNgan"):
        String vaiTroYeuCau = "ThuNgan";

        // Trường hợp 1: Nhập mã NV của Quản Kho ("NV002")
        TaiKhoan tkQuanKho = dsTaiKhoan.timKiem("NV002");
        assertNotNull(tkQuanKho);

        NhanVien nv2 = dsNhanVien.timMa(tkQuanKho.getMaNV());
        assertNotNull(nv2);

        // Lấy vai trò của người đó theo mã NV từ Person/NhanVien
        String vaiTroCuaNguoiDo = nv2.getVaiTro();
        assertEquals("QuanKho", vaiTroCuaNguoiDo);

        // BƯỚC 1: XÉT VAI TRÒ
        boolean dungVaiTro = vaiTroYeuCau.equalsIgnoreCase(vaiTroCuaNguoiDo);
        assertFalse(dungVaiTro, "Nhân viên NV002 (QuanKho) KHÔNG ĐƯỢC PHÉP đăng nhập vào menu ThuNgan!");
        // Do sai vai trò -> DỪNG NGAY, không xét mật khẩu!

        // Trường hợp 2: Nhập đúng mã NV của Thu Ngân ("NV003")
        TaiKhoan tkThuNgan = dsTaiKhoan.timKiem("NV003");
        assertNotNull(tkThuNgan);

        NhanVien nv3 = dsNhanVien.timMa(tkThuNgan.getMaNV());
        assertEquals("ThuNgan", nv3.getVaiTro());

        // BƯỚC 1: XÉT VAI TRÒ -> ĐÚNG!
        assertTrue(vaiTroYeuCau.equalsIgnoreCase(nv3.getVaiTro()), "Nhân viên NV003 đúng vai trò ThuNgan");

        // BƯỚC 2: RỒI MỚI TỚI MẬT KHẨU
        assertFalse(tkThuNgan.kiemTraMatKhau("mat_khau_sai"), "Mật khẩu sai phải báo lỗi");
        assertTrue(tkThuNgan.kiemTraMatKhau("thungan@123"), "Mật khẩu đúng phải xác thực thành công");
    }

    @Test
    public void testNhanVienBiSaThaiKhongTheDangNhap() {
        NhanVien nv = dsNhanVien.timMa("NV003");
        nv.setSaThai(true);
        assertTrue(nv.isSaThai(), "Nhân viên đã bị sa thải");

        TaiKhoan tk = dsTaiKhoan.timTheoMaNV("NV003");
        assertNotNull(tk);

        NhanVien checked = dsNhanVien.timMa(tk.getMaNV());
        assertNull(checked, "Nhân viên bị sa thải không còn trong danh sách hoạt động");

        boolean biSaThai = false;
        for (NhanVien e : dsNhanVien.getDanhSachNhanVien()) {
            if (e != null && tk.getMaNV().equalsIgnoreCase(e.getMaNV()) && e.isSaThai()) {
                biSaThai = true;
                break;
            }
        }
        assertTrue(biSaThai, "Phát hiện nhân viên bị sa thải để từ chối đăng nhập");
    }
}
