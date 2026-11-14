import hoadon.CTHoaDonBan;
import hoadon.DanhSachHoaDonBan;
import hoadon.HoaDonBanHang;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class HoaDonPersistenceTest {
    public static void main(String[] args) throws Exception {
        Path thuMuc = Files.createTempDirectory("hoa-don-persistence-");
        Path fileBan = thuMuc.resolve("hoadon_ban.txt");

        HoaDonBanHang hoaDon = new HoaDonBanHang("HD001", "0901234567");
        hoaDon.setNgayLap(LocalDateTime.of(2026, 11, 7, 9, 30));
        hoaDon.setDaThanhToan(true);
        hoaDon.themChiTiet(new CTHoaDonBan("SP001", "Gạo thơm", 2, 1_000));

        ArrayList<HoaDonBanHang> danhSachBan = new ArrayList<>();
        danhSachBan.add(hoaDon);
        DanhSachHoaDonBan danhSach = new DanhSachHoaDonBan(danhSachBan);
        danhSach.ghiFileText(fileBan.toString());

        check(Files.readAllLines(fileBan, StandardCharsets.UTF_8).stream()
                .anyMatch(line -> line.startsWith("BAN|HD001|0901234567|true")),
                "hóa đơn bán ghi đúng dòng BAN");
        check(Files.readAllLines(fileBan, StandardCharsets.UTF_8).stream()
                .anyMatch(line -> line.startsWith("CT|SP001|")),
                "chi tiết hóa đơn ghi đúng dòng CT");

        DanhSachHoaDonBan loaded = new DanhSachHoaDonBan();
        loaded.docFileText(fileBan.toString());
        HoaDonBanHang loadedHd = loaded.timMa("HD001");
        check(loadedHd != null, "đọc lại được hóa đơn HD001");
        equal("0901234567", loadedHd.getSdtDoiTac(), "số điện thoại khách round-trip");
        equal(1, loadedHd.getDanhSachChiTiet().size(), "số dòng chi tiết round-trip");
        equal("Gạo thơm", loadedHd.getDanhSachChiTiet().get(0).getTenSanPham(),
                "tên sản phẩm round-trip");
        equal(2_000L, loadedHd.tinhTongTien(), "thành tiền round-trip");
        check(loadedHd.isDaThanhToan(), "trạng thái thanh toán round-trip");
        check(loaded.timKiemHoaDonBanHang_SDT("0901234567") != null,
                "tìm theo số điện thoại sau khi đọc file");

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
