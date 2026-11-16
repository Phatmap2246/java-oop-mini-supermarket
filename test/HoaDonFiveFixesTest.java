import dichvu.*;
import hoadon.*;
import person.*;
import sanpham.*;
import java.util.*;
import java.time.*;
import java.io.*;

public class HoaDonFiveFixesTest {
    static int checks;
    static void check(boolean ok, String name) {
        checks++;
        if (!ok) throw new AssertionError(name);
    }
    static void rejects(Runnable action) {
        try { action.run(); } catch (IllegalArgumentException | IllegalStateException e) { checks++; return; }
        throw new AssertionError("Expected rejection");
    }
    static KhuyenMaiMuaTang gift() {
        KhuyenMaiMuaTang km = new KhuyenMaiMuaTang(); km.setMaKhuyenMai("KM001");
        km.setNgayBatDau(LocalDate.now().minusDays(1)); km.setNgayKetThuc(LocalDate.now().plusDays(1));
        km.setSoLuongMuaToiThieu(2); km.setSoLuongTang(1); return km;
    }
    static void sale(String input, int stock, int expectedQty, boolean paid, Khuyenmai km) {
        QuanLySanPham products = new QuanLySanPham(); DienTu sp = new DienTu();
        sp.setMaSanPham("SP001"); sp.setTenSanPham("Test"); sp.setGiaBan(10000);
        sp.setSoLuongTonKho(stock); products.getDanhSachSanPham().add(sp);
        KhachHang kh = new KhachHang(); kh.setMaKhachHang("KH001");
        DSKhachHang customers = new DSKhachHang(); customers.setList(new KhachHang[]{kh}, 1);
        Danhsachkhuyenmai kms = new Danhsachkhuyenmai(); DanhSachSPKM links = new DanhSachSPKM();
        if (km != null) {
            kms.setKhuyenmais(new Khuyenmai[]{km}); SanPhamKhuyenMai link = new SanPhamKhuyenMai();
            link.setSanPhamKhuyenMai(new Scanner("SP001\nKM001\n"), products, kms); links.them(link);
        }
        HoaDonBanHang hd = new HoaDonBanHang("NV001");
        check(hd.nhapHoaDon(new Scanner(input), products, links, kms, customers) == paid, "payment result");
        check(hd.getCtHoaDonBans().size() == 1 && hd.getCtHoaDonBans().get(0).getSoLuong() == expectedQty, "merge quantity");
        check(sp.getSoLuongTonKho() == stock - (paid ? expectedQty : 0), "stock");
        check(kh.getTongChiTieu() == (paid ? hd.tinhTongTien() : 0), "customer accumulation");
        if (km instanceof KhuyenMaiMuaTang)
            check(hd.getCtHoaDonBans().get(0).getSoLuongTangKem() == expectedQty / 3, "merged gifts");
    }
    public static void main(String[] args) {
        PrintStream original = System.out; System.setOut(new PrintStream(OutputStream.nullOutputStream()));
        try {
            sale("KH001\nSP001\n1\n1\nSP001\n2\n0\n1\n100000\n", 10, 3, true, gift());
            sale("KH001\nSP001\n2\n1\nSP001\n2\n0\n0\n1\n0\n1\n100000\n", 3, 3, true, null);
            sale("KH001\nSP001\n1\n1\nSP001\n2\n0\n1\n0\n", 10, 3, false, gift());
            KhuyenMaiTrucTiep free = new KhuyenMaiTrucTiep(); free.setMaKhuyenMai("KM001");
            free.setNgayBatDau(LocalDate.now().minusDays(1)); free.setNgayKetThuc(LocalDate.now().plusDays(1));
            free.setSoTienGiam(100000);
            sale("KH001\nSP001\n1\n0\n1\n1\n", 10, 1, true, free);
            sale("KH001\nSP001\n1\n0\n1\n0\n", 10, 1, false, free);
            CTHoaDonBan ct = new CTHoaDonBan("SP001", "Test", 3, 10000, "None");
            ct.apDungKhuyenMai(gift()); ct.setSoLuong(6);
            check(ct.tinhThanhTien() == 40000 && ct.getSoLuongTangKem() == 2, "increase gift");
            ct.setSoLuong(2); check(ct.tinhThanhTien() == 20000 && ct.getSoLuongTangKem() == 0, "decrease gift");
            ct.setSoLuong(3); ct.setDonGia(20000); check(ct.tinhThanhTien() == 40000, "price update");
            KhuyenMaiPhanTram percent = new KhuyenMaiPhanTram();
            percent.setNgayBatDau(LocalDate.now().minusDays(1)); percent.setNgayKetThuc(LocalDate.now().plusDays(1));
            percent.setPhanTramGiamGia(0.1); percent.setMucGiamToiDa(1000000); percent.setMucChiToiThieu(50000);
            ct.apDungKhuyenMai(percent); ct.setSoLuong(2); check(ct.tinhThanhTien() == 40000, "below threshold");
            ct.setSoLuong(3); check(ct.tinhThanhTien() == 54000, "cross threshold");
            String saved = ct.toDongFile(); percent.setTonTai(false);
            check(CTHoaDonBan.tuDongFile(saved).tinhThanhTien() == 54000, "historical discount preserved");
            CTHoaDonBan max = new CTHoaDonBan("SP001", "Test", 1, Long.MAX_VALUE, "None");
            rejects(() -> max.setSoLuong(2)); check(max.getSoLuong() == 1 && max.tinhThanhTien() == Long.MAX_VALUE, "failed quantity unchanged");
            CTHoaDonBan two = new CTHoaDonBan("SP002", "Test", 2, 1, "None");
            rejects(() -> two.setDonGia(Long.MAX_VALUE)); check(two.getDonGia() == 1, "failed price unchanged");
            HoaDonBanHang hd = new HoaDonBanHang(); hd.getCtHoaDonBans().add(max); hd.setChietKhau(0);
            check(hd.tinhTongTien() == Long.MAX_VALUE, "maximum exact total");
            hd.getCtHoaDonBans().add(two); rejects(hd::tinhTongTien);
            rejects(() -> new ThanhToanTienMat().thucHienThanhToan(new Scanner(""), -1));
            ThanhToanTienMat cash = new ThanhToanTienMat(100, 50);
            check(cash.thucHienThanhToan(new Scanner("bad\nco\n"), 0) && cash.getTienKhachDua() == 0 && cash.getTienThua() == 0, "free reset and retry");
            for (double vat : new double[]{0, 0.08, 8, 15}) {
                HoaDonNhapHang h = new HoaDonNhapHang();
                h.nhapHoaDon(new Scanner(vat + "\n0\n"), new ArrayList<>(), "NCC001");
                check(h.getThueVAT() == vat, "VAT percent value " + vat);
            }
            HoaDonNhapHang h = new HoaDonNhapHang();
            h.nhapHoaDon(new Scanner("NaN\n16\n-1\nabc\n8\n0\n"), new ArrayList<>(), "NCC001");
            check(h.getThueVAT() == 8, "invalid VAT retry");
        } finally { System.setOut(original); }
        System.out.println("PASS: " + checks + " regression checks for fixes 1-5.");
    }
}
