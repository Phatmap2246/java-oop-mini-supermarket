import dichvu.*;
import hoadon.*;
import person.*;
import sanpham.*;
import utils.DataBase;
import utils.YeuCau;
import utils.LuuTruNhieuFile;

import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.*;
import java.time.*;
import java.util.*;

/** Chạy write và read ở hai JVM khác nhau để kiểm tra việc mở lại chương trình. */
public class LuuTruChuongTrinhTest {
    private static final String TEN = "Sữa, \"đặc biệt\" | Tiếng Việt\nxuống dòng";
    private static final Class<?>[] CAC_LOP = {SanPham.class, NhanVien.class, KhachHang.class,
            HoaDon.class, Khuyenmai.class, Nhacungcap.class};

    public static void main(String[] args) throws Exception {
        String file = args[1];
        if (args[0].equals("write")) {
            taoDuLieu(file);
        } else {
            kiemTraDuLieu(file);
        }
        System.out.println("PASS: " + args[0]);
    }

    private static void taoDuLieu(String file) throws Exception {
        check(Files.notExists(Paths.get(file, "hien-tai")), "Chưa có bộ dữ liệu");
        GiaoDien gd = new GiaoDien();
        QuanLySanPham sanPhams = lay(gd, "dsSanPham");
        ThucPham thucPham = new ThucPham();
        thucPham.setTenSanPham(TEN);
        thucPham.setSoLuongTonKho(12);
        thucPham.setNgaySanXuat(LocalDate.of(2026, 1, 2));
        thucPham.setHanSuDung(LocalDate.of(2027, 1, 2));
        sanPhams.getDanhSachSanPham().add(thucPham);
        sanPhams.getDanhSachSanPham().add(new DienTu());
        sanPhams.getDanhSachSanPham().add(new TieuDung());

        Nhacungcap ncc = new Nhacungcap();
        ncc.getSanPhamCungCap().getDanhSachSanPham().add(thucPham);
        ((Danhsachdoitac) lay(gd, "dsDoiTac")).getDanhSachDoiTac().add(ncc);

        KhachHang kh = new KhachHang();
        kh.setDiachi(new DiaChi("12/3", "Đường A, B", "Phường 1", "Quận 2", "TP Hồ Chí Minh"));
        kh.setTonTai(false);
        ((DSKhachHang) lay(gd, "dsKhachHang")).setList(new KhachHang[]{kh}, 1);
        DSNhanVien nhanViens = lay(gd, "dsNhanVien");
        ArrayList<NhanVien> list = lay(nhanViens, "list");
        list.add(new KeToan());
        list.add(new ThuNgan());
        list.add(new QuanKho(sanPhams, lay(gd, "dsHoaDonNhap")));
        list.add(new QuanLy());

        ((Danhsachkhuyenmai) lay(gd, "dsKhuyenMai")).setKhuyenmais(new Khuyenmai[]{
                new KhuyenMaiPhanTram(), new KhuyenMaiMuaTang(), new KhuyenMaiTrucTiep()});
        ((DanhSachSPKM) lay(gd, "dsSanPhamKhuyenMai")).setSanPhamKhuyenMais(
                new SanPhamKhuyenMai[]{new SanPhamKhuyenMai(thucPham.getMaSanPham(), "KM001")});

        HoaDonBanHang ban = new HoaDonBanHang("NV001");
        ban.setNgayLap(LocalDateTime.of(2026, 1, 2, 3, 4, 5));
        ban.setMaKhachHang(kh.getMaKhachHang());
        ban.setDaThanhToan(true);
        ban.setPhuongThucThanhToan(new ThanhToanTienMat());
        ban.getCtHoaDonBans().add(new CTHoaDonBan(thucPham.getMaSanPham(), TEN, 2, 5000, "Không áp dụng"));
        ((DanhSachHoaDonBan) lay(gd, "dsHoaDonBan")).getDsHoaDonBanHang().add(ban);
        HoaDonBanHang chuyenKhoan = new HoaDonBanHang("NV002");
        chuyenKhoan.setPhuongThucThanhToan(new ThanhToanChuyenKhoan("Ngân hàng", "123456"));
        ((DanhSachHoaDonBan) lay(gd, "dsHoaDonBan")).getDsHoaDonBanHang().add(chuyenKhoan);

        HoaDonNhapHang nhap = new HoaDonNhapHang("NV003");
        nhap.getCtHoaDonNhaps().add(new CTHoaDonNhap());
        ((DanhSachHoaDonNhap) lay(gd, "dsHoaDonNhap")).getDsHoaDonNhapHang().add(nhap);
        DanhSachCongNo congNo = lay(gd, "dsCongNo");
        them(congNo, "maHoaDonCongNo", nhap.getMaHoaDon());
        them(congNo, "maPhieuNhapCongNo", "PNK001");
        them(congNo, "maNhaCungCapCongNo", ncc.getMaDoiTac());
        them(congNo, "tenNhaCungCapCongNo", TEN);
        them(congNo, "soTienCongNo", 10000L);
        them(congNo, "ngayDenHanCongNo", LocalDate.of(2026, 12, 1));
        them(congNo, "ngayThanhToanCongNo", null);
        them(congNo, "nganHangThanhToanCongNo", null);
        them(congNo, "daThanhToanCongNo", false);
        QuanLy quanLy = lay(gd, "quanLy");
        ((DSYeuCau) lay(quanLy, "dsYeuCau")).getList().add(new YeuCau("NV001", "QuanKho", TEN, "ChoDuyet"));

        for (Class<?> lop : CAC_LOP) {
            Field f = lop.getDeclaredField(lop == Nhacungcap.class ? "soThuTuNCC" : "soThuTu");
            f.setAccessible(true);
            f.setInt(null, 71);
        }
        gd.ghiDuLieu(file);
        String vanBan = Files.readString(Paths.get(file, "hien-tai/SanPham.txt"));
        check(vanBan.startsWith("<?xml") && vanBan.contains("tenSanPham")
                && vanBan.contains("Sữa,"), "TXT chứa tên thuộc tính và tiếng Việt đọc được");
        for (String ten : LuuTruNhieuFile.TEN_FILE) {
            check(Files.isRegularFile(Paths.get(file, "hien-tai", ten)), "Có file riêng " + ten);
        }
    }

    private static void kiemTraDuLieu(String file) throws Exception {
        GiaoDien gd = GiaoDien.docDuLieu(file);
        for (Class<?> lop : CAC_LOP) {
            Field f = lop.getDeclaredField(lop == Nhacungcap.class ? "soThuTuNCC" : "soThuTu");
            f.setAccessible(true);
            check(f.getInt(null) == 71, "Khôi phục bộ đếm " + lop.getSimpleName());
        }
        QuanLySanPham sanPhams = lay(gd, "dsSanPham");
        ThucPham sp = (ThucPham) sanPhams.getDanhSachSanPham().get(0);
        check(sp.getTenSanPham().equals(TEN) && sp.getSoLuongTonKho() == 12, "Unicode, dấu phân cách và tồn kho");
        check(sp.getHanSuDung().equals(LocalDate.of(2027, 1, 2)), "Ngày hạn sử dụng");
        check(sanPhams.getDanhSachSanPham().get(1) instanceof DienTu, "Lớp con sản phẩm");
        Nhacungcap ncc = (Nhacungcap) ((Danhsachdoitac) lay(gd, "dsDoiTac")).getDanhSachDoiTac().get(0);
        check(ncc.getSanPhamCungCap().getDanhSachSanPham().get(0) == sp, "Tham chiếu SP dùng chung với NCC");
        KhachHang kh = ((DSKhachHang) lay(gd, "dsKhachHang")).getList()[0];
        check(!kh.isTonTai() && kh.getDiaChi().getDuong().equals("Đường A, B"), "Xóa mềm và địa chỉ lồng nhau");
        ArrayList<NhanVien> nhanViens = lay(lay(gd, "dsNhanVien"), "list");
        check(nhanViens.size() == 4 && nhanViens.get(0) instanceof KeToan, "Danh sách nhân viên đa hình");
        check(((Danhsachkhuyenmai) lay(gd, "dsKhuyenMai")).getSoLuongKhuyenMai() == 3, "Khuyến mãi");
        check(((DanhSachSPKM) lay(gd, "dsSanPhamKhuyenMai")).getSoLuongSanPham() == 1, "Liên kết sản phẩm khuyến mãi");
        DanhSachHoaDonBan bans = lay(gd, "dsHoaDonBan");
        HoaDonBanHang ban = bans.getDsHoaDonBanHang().get(0);
        check(ban.getNgayLap().equals(LocalDateTime.of(2026, 1, 2, 3, 4, 5)), "Giữ nguyên ngày lập hóa đơn");
        check(ban.isDaThanhToan() && ban.getPhuongThucThanhToan() instanceof ThanhToanTienMat, "Trạng thái và phương thức thanh toán");
        check(bans.getDsHoaDonBanHang().get(1).getPhuongThucThanhToan() instanceof ThanhToanChuyenKhoan, "Chuyển khoản");
        check(ban.getCtHoaDonBans().get(0).getTenSanPham().equals(TEN), "Chi tiết hóa đơn");
        check(((DanhSachHoaDonNhap) lay(gd, "dsHoaDonNhap")).getDsHoaDonNhapHang().get(0).getCtHoaDonNhaps().size() == 1, "Chi tiết hóa đơn nhập");
        check(((ArrayList<?>) lay(lay(gd, "dsCongNo"), "soTienCongNo")).get(0).equals(10000L), "Công nợ");
        check(lay(lay(gd, "quanKho"), "qlsp") == sanPhams, "Kho dùng cùng danh mục");
        check(lay(lay(gd, "thuNgan"), "danhSachHoaDonBan") == bans, "Thu ngân dùng cùng hóa đơn");
        check(lay(lay(gd, "quanLy"), "dsnv") == lay(gd, "dsNhanVien"), "Quản lý dùng cùng nhân viên");
        check(new ThucPham().getMaSanPham().equals("SP071"), "Mã mới không trùng sau mở lại");

        Path fileSanPham = Paths.get(file, "hien-tai", "SanPham.txt");
        byte[] truoc = Files.readAllBytes(fileSanPham);
        try {
            DataBase.ghiDoiTuong(new DuLieuLoi(), fileSanPham.toString());
            throw new AssertionError("Phải báo lỗi khi không ghi được");
        } catch (IOException expected) {
            check(Arrays.equals(truoc, Files.readAllBytes(fileSanPham)), "Ghi thất bại không làm hỏng file cũ");
        }
        Map<String, Object> banLuuLoi = new LinkedHashMap<>();
        for (String ten : LuuTruNhieuFile.TEN_FILE) {
            banLuuLoi.put(ten, DataBase.docDoiTuong(Object.class,
                    Paths.get(file, "hien-tai", ten).toString()));
        }
        banLuuLoi.put("CongNo.txt", new DuLieuLoi());
        try {
            LuuTruNhieuFile.ghiDuLieu(file, banLuuLoi);
            throw new AssertionError("Phải báo lỗi khi một file trong bộ không ghi được");
        } catch (IOException expected) {
            check(Arrays.equals(truoc, Files.readAllBytes(fileSanPham)), "Ghi cả bộ thất bại giữ nguyên phiên cũ");
            check(GiaoDien.docDuLieu(file) != null, "Bộ file cũ vẫn đọc được");
        }
        Path thuMucLoi = Paths.get(file + ".loi");
        Files.createDirectories(thuMucLoi.resolve("hien-tai"));
        Path fileLoi = thuMucLoi.resolve("hien-tai/SanPham.txt");
        Files.writeString(fileLoi, "file sai định dạng");
        try {
            GiaoDien.docDuLieu(thuMucLoi.toString());
            throw new AssertionError("Phải báo lỗi file hỏng");
        } catch (IOException expected) {
            check(Files.readString(fileLoi).equals("file sai định dạng"), "Giữ file lỗi để kiểm tra");
        }
        sanPhams.getDanhSachSanPham().clear();
        gd.ghiDuLieu(file + ".rong");
        check(((QuanLySanPham) lay(GiaoDien.docDuLieu(file + ".rong"), "dsSanPham")).getDanhSachSanPham().isEmpty(), "Danh sách rỗng vẫn lưu được");
        check(!DataBase.getCSVHeader(kh).contains("soThuTu"), "CSV không ghi thuộc tính static");

        Path phucHoi = Paths.get(file + ".phuc-hoi");
        gd.ghiDuLieu(phucHoi.toString());
        Files.move(phucHoi.resolve("hien-tai"), phucHoi.resolve("ban-truoc"));
        check(GiaoDien.docDuLieu(phucHoi.toString()) != null
                && Files.exists(phucHoi.resolve("hien-tai/CongNo.txt")), "Khôi phục nếu dừng giữa bước đổi thư mục");

        Path thieuFile = Paths.get(file + ".thieu");
        gd.ghiDuLieu(thieuFile.toString());
        Files.delete(thieuFile.resolve("hien-tai/HoaDonBan.txt"));
        try {
            GiaoDien.docDuLieu(thieuFile.toString());
            throw new AssertionError("Phải báo rõ bộ dữ liệu thiếu file");
        } catch (IOException expected) {
            check(expected.getMessage().contains("HoaDonBan.txt"), "Thông báo đúng file bị thiếu");
        }
    }

    private static class DuLieuLoi {
        private final Object khongGhiDuoc = new Object();
    }

    @SuppressWarnings("unchecked")
    private static <T> T lay(Object obj, String ten) throws Exception {
        Field f = obj.getClass().getDeclaredField(ten);
        f.setAccessible(true);
        return (T) f.get(obj);
    }

    private static void them(Object obj, String ten, Object giaTri) throws Exception {
        ArrayList<Object> list = lay(obj, ten);
        list.add(giaTri);
    }

    private static void check(boolean dung, String moTa) {
        if (!dung) throw new AssertionError(moTa);
    }
}
