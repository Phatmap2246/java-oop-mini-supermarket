import hoadon.CTHoaDon;
import hoadon.HoaDon;
import hoadon.HoaDonBanHang;
import hoadon.HoaDonNhapHang;
import hoadon.QuanLyHoaDon;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;

public class HoaDonPersistenceTest {
    public static void main(String[] args) throws Exception {
        Path thuMuc = Files.createTempDirectory("hoa-don-persistence-");
        Path fileBan = thuMuc.resolve("hoadon_ban.txt");
        Path fileNhap = thuMuc.resolve("hoadon_nhap.txt");
        QuanLyHoaDon danhSach = new QuanLyHoaDon();

        HoaDonBanHang hoaDonBan = new HoaDonBanHang("HD001", "Khách | lẻ");
        hoaDonBan.setNgayLap(LocalDate.of(2026, 11, 7));
        hoaDonBan.setTienKhuyenMai(100);
        hoaDonBan.setDaThanhToan(true);
        hoaDonBan.setChietKhauHoaDonPhanTram(10);
        hoaDonBan.themChiTiet(new CTHoaDon(
                "SP001", "Gạo | thơm", 2, 1_000, 0, false, 1_800));
        danhSach.themHoaDon(hoaDonBan);

        HoaDonNhapHang hoaDonNhap = new HoaDonNhapHang(
                "HDN001", "Nhà cung cấp | A", "NCC01", 25_000, 8);
        hoaDonNhap.setNgayLap(LocalDate.of(2026, 11, 6));
        hoaDonNhap.setThongTinVanChuyen(
                "DVVC01", "XE01", "Xe tải | nhỏ", HoaDonNhapHang.TRANG_THAI_CHO_QUAN_LY_DUYET);
        hoaDonNhap.themChiTiet(new CTHoaDon("SP002", "Sữa", 3, 5_000, 0));
        danhSach.themHoaDon(hoaDonNhap);

        danhSach.ghiHoaDonBanText(fileBan.toString());
        danhSach.ghiHoaDonNhapText(fileNhap.toString());
        check(Files.readAllLines(fileBan, StandardCharsets.UTF_8).stream()
                .anyMatch(line -> line.startsWith("BAN4|")), "sale invoices use the new format");
        check(Files.readAllLines(fileNhap, StandardCharsets.UTF_8).stream()
                .anyMatch(line -> line.startsWith("NHAP2|")), "purchase invoices use the new format");

        QuanLyHoaDon loaded = new QuanLyHoaDon();
        loaded.docHoaDonBanText(fileBan.toString());
        loaded.docHoaDonNhapText(fileNhap.toString());
        HoaDonBanHang loadedSale = (HoaDonBanHang) loaded.timKiemTheoMa("HD001");
        HoaDonNhapHang loadedPurchase = (HoaDonNhapHang) loaded.timKiemTheoMa("HDN001");
        equal(2, loaded.getDanhSachHoaDon().size(), "both invoice types are loaded");
        equal("Khách | lẻ", loadedSale.getTenDoiTac(), "sale partner name round-trips");
        equal(LocalDate.of(2026, 11, 7), loadedSale.getNgayLap(), "sale date round-trips");
        equal(1_620L, loadedSale.tinhTongTien(), "sale discounts and line total round-trip");
        equal("Gạo | thơm", loadedSale.getDanhSachChiTiet().get(0).getTenSanPham(),
                "sale item name round-trips");
        equal("Nhà cung cấp | A", loadedPurchase.getTenDoiTac(),
                "supplier name round-trips");
        equal(LocalDate.of(2026, 11, 6), loadedPurchase.getNgayLap(),
                "purchase date round-trips");
        equal(HoaDonNhapHang.TRANG_THAI_CHO_QUAN_LY_DUYET, loadedPurchase.getTrangThai(),
                "purchase status round-trips");
        equal("DVVC01", loadedPurchase.getMaDonViVanChuyen(),
                "shipping provider round-trips");
        equal("Xe tải | nhỏ", loadedPurchase.getLoaiXeVanChuyen(),
                "vehicle details round-trip");

        Path badFile = thuMuc.resolve("hoa-don-loi.txt");
        Files.write(badFile, java.util.Collections.singletonList("BAN4|broken"),
                StandardCharsets.UTF_8);
        boolean malformedRejected = false;
        try {
            loaded.docHoaDonBanText(badFile.toString());
        } catch (java.io.IOException expected) {
            malformedRejected = true;
        }
        check(malformedRejected, "malformed invoice file reports an error");
        check(loaded.timKiemTheoMa("HD001") != null,
                "failed load leaves current invoice list unchanged");

        Path legacyPurchaseFile = thuMuc.resolve("hoa-don-cu.txt");
        ArrayList<String> legacyLines = new ArrayList<>();
        legacyLines.add("NHAP|HDN002|NCC cu|NCC02|10.0|0");
        legacyLines.add("CT|SP003|San pham cu|1|2500|0.0");
        Files.write(legacyPurchaseFile, legacyLines, StandardCharsets.UTF_8);
        loaded.docHoaDonNhapText(legacyPurchaseFile.toString());
        check(loaded.timKiemTheoMa("HDN001") == null,
                "loading replaces invoices of the selected type");
        equal(2_750L, loaded.timKiemTheoMa("HDN002").tinhTongTien(),
                "legacy purchase format remains readable");
        check(loaded.timKiemTheoMa("HD001") != null,
                "loading one invoice type preserves the other type");
        System.out.println("PASS: invoice persistence round-trip checks.");
    }

    private static void check(boolean condition, String label) {
        if (!condition) {
            throw new AssertionError(label);
        }
    }

    private static void equal(Object expected, Object actual, String label) {
        if (expected == null ? actual != null : !expected.equals(actual)) {
            throw new AssertionError(label + ": expected " + expected + " but was " + actual);
        }
    }
}
