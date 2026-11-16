import interfaces.BanGhiFile;
import utils.*;
import sanpham.*;
import dichvu.*;
import doitac.*;
import hoadon.*;
import person.*;
import java.io.IOException;
import java.nio.file.*;
import java.time.*;
import java.util.*;

/** Kiểm tra định dạng đơn giản và các giới hạn được chủ động chấp nhận. */
public class DinhDangDongTest {
    private static int soCa;
    private interface ThaoTac { void chay() throws Exception; }
    public static void main(String[] args) throws Exception {
        Path dir = Files.createTempDirectory(Paths.get(args[0]), "kiem-tra-dong-");
        check(DongDuLieu.ghep("Tiếng Việt |,", null, "", "\\N").equals("Tiếng Việt |,;;;\\N"), "Null thành ô rỗng, không dùng escape");
        check("A;".split(";", -1).length == 2, "Giữ ô rỗng cuối");
        loi(() -> DongDuLieu.ghep("Có;dấu phân cách"));
        loi(() -> DongDuLieu.ghep("Có\nxuống dòng"));

        BanGhiFile[] models = {new ThucPham(), new DienTu(), new TieuDung(), new TaiKhoanNhanVien(),
                new QuanLy(), new QuanKho(), new ThuNgan(), new KeToan(), new KhachHang(), new DiaChi(),
                new Nhacungcap(), new KhuyenMaiPhanTram(), new KhuyenMaiTrucTiep(), new KhuyenMaiMuaTang(),
                new SanPhamKhuyenMai(), new HoaDonBanHang(), new HoaDonNhapHang(), new CTHoaDonBan(),
                new CTHoaDonNhap(), new ThanhToanTienMat(), new ThanhToanChuyenKhoan(), new YeuCau()};
        for (BanGhiFile obj : models) {
            String line = obj.toDongFile();
            int[] before = BoDemMa.chup();
            BanGhiFile copy = (BanGhiFile) DataBase.tuDongFile(obj.getClass(), line);
            check(line.equals(copy.toDongFile()), "Đọc ghi cột: " + obj.getClass().getSimpleName());
            check(Arrays.equals(before, BoDemMa.chup()), "Đọc không cấp mã mới");
        }
        ThucPham sp = new ThucPham();
        sp.setTenSanPham("Bánh mì, Tiếng Việt | \\N");
        sp.setNgaySanXuat(LocalDate.of(2019, 1, 1));
        sp.setHanSuDung(LocalDate.of(2020, 2, 29));
        sp.setNhietDoBaoQuan(-18.123456789012345);
        check(sp.toDongFile().equals(ThucPham.tuDongFile(sp.toDongFile()).toDongFile()), "Hạn sử dụng lịch sử");
        String[] p = sp.toDongFile().split(";", -1);
        p[11] = "31/02/2020"; loi(() -> ThucPham.tuDongFile(String.join(";", p)));
        p[11] = "29/02/2020"; p[7] = "-1"; loi(() -> ThucPham.tuDongFile(String.join(";", p)));
        p[7] = "1"; p[8] = "NaN"; loi(() -> ThucPham.tuDongFile(String.join(";", p)));
        p[8] = "Infinity"; loi(() -> ThucPham.tuDongFile(String.join(";", p)));
        p[8] = "1.1"; loi(() -> ThucPham.tuDongFile(String.join(";", p)));
        p[8] = "0.08"; p[9] = "yes"; loi(() -> ThucPham.tuDongFile(String.join(";", p)));
        loi(() -> ThucPham.tuDongFile(sp.toDongFile() + ";thừa"));
        loi(() -> ThucPham.tuDongFile("THUCPHAM;SP001"));

        Locale truoc = Locale.getDefault();
        try {
            Locale.setDefault(Locale.GERMANY);
            KhuyenMaiPhanTram km = new KhuyenMaiPhanTram();
            km.setPhanTramGiamGia(0.123456789012345);
            km.setMucGiamToiDa(Long.MAX_VALUE);
            check(km.toDongFile().equals(KhuyenMaiPhanTram.tuDongFile(km.toDongFile()).toDongFile()), "Số không làm tròn, độc lập locale");
        } finally { Locale.setDefault(truoc); }

        Danhsachkhuyenmai ds = new Danhsachkhuyenmai();
        KhuyenMaiTrucTiep km = new KhuyenMaiTrucTiep(); ds.them(km);
        Path file = dir.resolve("KhuyenMai.txt"); ds.ghiFile(file.toString());
        byte[] saved = Files.readAllBytes(file);
        loi(() -> DataBase.ghiDoiTuong(new QuanLy(), file.toString()));
        loi(() -> DataBase.ghiDoiTuong(new Nhacungcap(), file.toString()));
        check(Arrays.equals(saved, Files.readAllBytes(file)), "Đối tượng có danh sách con phải lưu qua manager");
        km.setLoaiKhuyenMai("Nội dung;không hợp lệ");
        loi(() -> ds.ghiFile(file.toString()));
        check(Arrays.equals(saved, Files.readAllBytes(file)), "Ghi chuỗi không hỗ trợ giữ file cũ");
        km.setLoaiKhuyenMai("Giảm giá trực tiếp");
        int[] dem = BoDemMa.chup();
        TepVanBan.ghi(file.toString(), "Danhsachkhuyenmai", Arrays.asList(km.toDongFile(), km.toDongFile()));
        loi(() -> ds.docFile(file.toString()));
        check(ds.getSoLuongKhuyenMai() == 1 && ds.getKhuyenmais()[0] == km, "Đọc lỗi giữ danh sách cũ");
        check(Arrays.equals(dem, BoDemMa.chup()), "Đọc lỗi giữ bộ đếm");
        ds.ghiFile(file.toString());
        check(!Files.readString(file).contains("#KETTHUC"), "Đã bỏ footer");

        Path hoaDonFile = dir.resolve("HoaDonNhap.txt");
        HoaDonNhapHang hd = new HoaDonNhapHang("NV001");
        CTHoaDonNhap ct = new CTHoaDonNhap();
        ct.setMaSanpham(sp.getMaSanPham()); ct.setTenSanPham(sp.getTenSanPham()); ct.setSoLuong(2); ct.setDonGia(15001);
        hd.getCtHoaDonNhaps().add(ct);
        DanhSachHoaDonNhap nhaps = new DanhSachHoaDonNhap(); nhaps.getDsHoaDonNhapHang().add(hd);
        nhaps.ghiFile(hoaDonFile.toString());
        DanhSachHoaDonNhap loaded = new DanhSachHoaDonNhap(); loaded.docFile(hoaDonFile.toString());
        check(loaded.getDsHoaDonNhapHang().get(0).getCtHoaDonNhaps().get(0).toDongFile().equals(ct.toDongFile()), "Ghép chi tiết theo mã");
        String[] detail = ct.toDongFile().split(";", 2);
        String dongCT = detail[0] + ";" + hd.getMaHoaDon() + ";" + detail[1];
        TepVanBan.ghi(hoaDonFile.toString(), "DanhSachHoaDonNhap", Arrays.asList(dongCT, hd.toDongFile()));
        loi(() -> loaded.docFile(hoaDonFile.toString()));

        Path request = dir.resolve("YeuCau.txt");
        YeuCau yc = new YeuCau("NV001", "QuanKho", "Nhập thêm sữa, Tiếng Việt", "ChoDuyet");
        YeuCau.themVaoFile(yc, request);
        check(YeuCau.docDanhSach(request).get(0).getNoiDung().equals(yc.getNoiDung()), "Yêu cầu UTF-8");
        byte[] cu = Files.readAllBytes(request);
        loi(() -> YeuCau.themVaoFile(new YeuCau("NV001", "QuanKho", "Không;hỗ trợ", "ChoDuyet"), request));
        check(Arrays.equals(cu, Files.readAllBytes(request)), "Yêu cầu không hợp lệ giữ file cũ");
        System.out.println("PASS: " + soCa + " kiểm tra TXT đơn giản");
    }
    private static void check(boolean dung, String ten) {
        if (!dung) throw new AssertionError(ten); soCa++;
    }
    private static void loi(ThaoTac action) throws Exception {
        try { action.chay(); }
        catch (IOException | IllegalArgumentException | java.time.DateTimeException expected) { soCa++; return; }
        throw new AssertionError("Phải báo lỗi dữ liệu");
    }
}
