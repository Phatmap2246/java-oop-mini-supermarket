package person;

import hoadon.CTHoaDonBan;
import hoadon.HoaDon;
import hoadon.HoaDonBanHang;
import utils.DataBase;
import utils.YeuCau;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Scanner;

public class PersonPackageTest {
    private static int assertions;

    public static void main(String[] args) throws Exception {
        testDiaChi();
        testPersonAndNhanVien();
        testDSNhanVien();
        testKhachHang();
        testDSKhachHang();
        testKeToan();
        testQuanKho();
        testYeuCauLifecycle();
        testThuNganShift();
        System.out.println("PASS: " + assertions + " assertions across all person Java classes.");
    }

    private static void testDiaChi() {
        DiaChi address = new DiaChi("1", "Le Loi", "Ben Nghe", "Quan 1", "TP HCM");
        DiaChi copy = new DiaChi(address);
        equal("1, Le Loi, Ben Nghe, Quan 1, TP HCM", copy.toString(), "DiaChi copy and formatting");
        copy.setDuong("Nguyen Hue");
        equal("Le Loi", address.getDuong(), "DiaChi copy is independent");
        equal("Nguyen Hue", copy.getDuong(), "DiaChi setter");
        DiaChi entered = new DiaChi();
        entered.nhapDiaChi(new Scanner("2\nDong Khoi\nBen Nghe\nQuan 1\nTP HCM\n"));
        equal("2, Dong Khoi, Ben Nghe, Quan 1, TP HCM", entered.toString(), "DiaChi scanner input");
    }

    private static void testPersonAndNhanVien() {
        NhanVien employee = employee("NV001", "ThuNgan");
        employee.setEmail("staff@example.com");
        employee.setHoten("Nguyen Van A");
        employee.setSdt("0912345678");
        equal("staff@example.com", employee.getEmail(), "Person email setter");
        equal("Nguyen Van A", employee.getHoten(), "Person name setter");
        equal("0912345678", employee.getSdt(), "Person phone setter");
        employee.setEmail("invalid");
        equal("staff@example.com", employee.getEmail(), "Person rejects invalid email");
        employee.setHoten("123");
        equal("Nguyen Van A", employee.getHoten(), "Person rejects invalid name");
        employee.setSdt("123");
        equal("0912345678", employee.getSdt(), "Person rejects invalid phone");
        employee.setNgsinh("31/02/2000");
        equal(LocalDate.of(1990, 1, 1), employee.getNgsinh(), "Person rejects invalid date");
        employee.setNgsinh("02/01/2000");
        equal(LocalDate.of(2000, 1, 2), employee.getNgsinh(), "Person accepts valid date");
        employee.setLuong(12_000);
        employee.setPhuCapTrachNhiem(3_000);
        equal(15_000L, employee.getLuong(), "NhanVien total salary getter");
        equal(15_000L, employee.tinhLuong(), "NhanVien salary calculation");
        equal("ThuNgan", employee.getVaiTro(), "NhanVien role");
        check(!employee.isSaThai(), "NhanVien defaults to active");
        employee.setSaThai(true);
        check(employee.isSaThai(), "NhanVien dismissal state setter");
        NhanVien copy = new NhanVien(employee);
        check(copy.isSaThai(), "NhanVien copy preserves dismissal state");
        check(copy.toString().contains("Đã sa thải"), "NhanVien display includes dismissal state");
        NhanVien restored = DataBase.taoObject(
                NhanVien.class,
                "maNV,vaitro,luong,phuCapTrachNhiem,isSaThai",
                "NV001,ThuNgan,0,0,true");
        check(restored.isSaThai(), "NhanVien persistence reads dismissal state");
        NhanVien legacyRecord = DataBase.taoObject(
                NhanVien.class,
                "maNV,vaitro,luong,phuCapTrachNhiem",
                "NV001,ThuNgan,0,0");
        check(!legacyRecord.isSaThai(), "NhanVien legacy records default to active");
    }

    private static void testDSNhanVien() {
        NhanVien employee = employee("NV002", "KeToan");
        ArrayList<NhanVien> employees = new ArrayList<>();
        employees.add(employee);
        DSNhanVien list = new DSNhanVien(employees);
        equal(0, list.timNV(" NV002 "), "DSNhanVien finds trimmed code");
        check(list.suaNV("NV002", new Scanner("2\nTran Thi B\n0\n")), "DSNhanVien edits existing employee");
        equal("Tran Thi B", employee.getHoten(), "DSNhanVien edit persists");
        check(!list.suaNV("NV999", new Scanner("0\n")), "DSNhanVien reports missing employee");
        check(list.saThai("NV002"), "DSNhanVien dismisses existing employee");
        equal(0, list.timNV("NV002"), "DSNhanVien retains dismissed employee");
        check(employee.isSaThai(), "DSNhanVien marks employee as dismissed");
        check(!list.saThai("NV002"), "DSNhanVien does not dismiss an employee twice");
        check(new DSNhanVien(list).list == list.list, "DSNhanVien copy shares backing list");
    }

    private static void testKhachHang() {
        KhachHang customer = new KhachHang();
        equal("Khách hàng", customer.getVaiTro(), "KhachHang role");
        check(KhachHang.isValidMaKhachHang("KH001"), "KhachHang accepts valid code");
        check(!KhachHang.isValidMaKhachHang("K001"), "KhachHang rejects invalid code");
        customer.setMaKhachHang("KH001");
        customer.tichLuyChiTieu(1_000_000);
        equal("Bạc", customer.getHangThanhVien(), "KhachHang membership threshold");
        equal(10, customer.getDiemTichLuy(), "KhachHang points");
        equal(0.03, customer.tinhChietKhau(), "KhachHang discount");
        customer.tichLuyChiTieu(4_000_000);
        equal("Vàng", customer.getHangThanhVien(), "KhachHang next membership threshold");

        // Giỏ hàng chưa tồn tại trong KhachHang của repo này; chỉ kiểm tra tích luỹ chi tiêu.
        check(customer.getTongChiTieu() > 0, "KhachHang tracks total spending");
    }

    private static void testDSKhachHang() {
        KhachHang customer = new KhachHang();
        customer.setMaKhachHang("KH123");
        customer.setHoten("Le Van C");
        DSKhachHang list = new DSKhachHang();
        list.add(customer);
        equal(0, list.timKH(" kh123 "), "DSKhachHang finds case-insensitively");
        check(list.timKiemKH("KH123") == customer, "DSKhachHang returns matching object");
        equal(1, list.timKiemTheoTen("van").length, "DSKhachHang searches partial name");
        check(!list.suaKH("KH999", new Scanner("0\n")), "DSKhachHang reports missing customer");
        list.xoaKH("KH123");
        KhachHang removed = list.timKiemKH("KH123");
        check(removed == null || !removed.isTonTai(),
                "DSKhachHang soft-deletes customer (isTonTai = false)");
    }

    private static void testKeToan() {
        KeToan accountant = new KeToan();
        HoaDonBanHang salesInvoice = new HoaDonBanHang("HD001", "KH001");
        salesInvoice.themChiTiet(new CTHoaDonBan("SP001", "Rice", 3, 50_000));
        ArrayList<HoaDon> invoices = new ArrayList<>();
        invoices.add(salesInvoice);

        equal(150_000L, accountant.tinhTongDoanhThu(invoices, 7), "KeToan total revenue");
        equal(150_000L, accountant.tinhTongDoanhThu(invoices, 7), "KeToan repeated revenue calculation");
        equal(0L, accountant.tinhTongChiPhi(invoices, 7), "KeToan has no expense source after hoadon rewrite");
        equal(150_000L, accountant.tinhLoiNhuan(invoices, 7), "KeToan profit");
        equal(120_000L, accountant.tinhLoiNhuanRong(invoices, 7), "KeToan net profit after corporate income tax");
        equal(0L, accountant.tinhLoiNhuanRong(new ArrayList<>(), 7), "KeToan handles empty invoice list");
    }

    private static void testQuanKho() {
        QuanKho warehouse = new QuanKho();
        equal("QuanKho", warehouse.getVaiTro(), "QuanKho role");
        check(warehouse.timSanPham(null) == null, "QuanKho null product lookup");
        check(warehouse.timSanPham("SP-NOT-FOUND") == null, "QuanKho missing product lookup");
        check(warehouse.danhSachSapHetHan(7).isEmpty(), "QuanKho empty expiry query");
    }

    private static void testYeuCauLifecycle() throws IOException {
        Path requestFile = Paths.get("YeuCau.txt");
        check(!Files.exists(requestFile), "Request test starts in isolated working directory");

        NhanVien employee = employee("NV003", "KeToan");
        employee.taoYeuCau(new Scanner("Đề nghị duyệt thanh toán, nội dung | cần xét\n"));
        ArrayList<YeuCau.BanGhiDataBase> requests = YeuCau.docDanhSach(requestFile);
        equal(1, requests.size(), "NhanVien writes request in DataBase format");
        equal("Đề nghị duyệt thanh toán, nội dung | cần xét",
                requests.get(0).getNoiDung(), "Request content supports CSV separators");
        equal("ChoDuyet", requests.get(0).getTrangThai(), "New request starts pending");

        ThuNgan cashier = new ThuNgan(
                "cashier@example.com", "Tran Thi D", "0912345678", new DiaChi(),
                LocalDate.of(1990, 1, 1), "NV004", "ThuNgan", 10_000, 0, 0);
        cashier.taoYeuCau(new Scanner("Đề nghị hỗ trợ đối soát\n"));
        KeToan accountant = new KeToan();
        accountant.setMaNV("NV005");
        accountant.setVaiTro("KeToan");
        accountant.taoYeuCau(new Scanner("Đề nghị duyệt công nợ\n"));

        QuanLy manager = new QuanLy();
        equal("QuanLy", manager.getVaiTro(), "QuanLy role");
        manager.duyetYeuCau(new Scanner("1\n0\n1\n"));
        requests = YeuCau.docDanhSach(requestFile);
        equal("DaDuyet", requests.get(0).getTrangThai(), "QuanLy approves request");
        equal("KhongDuyet", requests.get(1).getTrangThai(), "QuanLy rejects request");
        equal("DaDuyet", requests.get(2).getTrangThai(), "QuanLy approves accountant request");

        ByteArrayOutputStream historyOutput = new ByteArrayOutputStream();
        PrintStream originalOutput = System.out;
        PrintStream capturedOutput = new PrintStream(historyOutput, true, StandardCharsets.UTF_8);
        try {
            System.setOut(capturedOutput);
            manager.xemLichSuDuyet(requestFile.toString());
        } finally {
            System.setOut(originalOutput);
            capturedOutput.close();
        }
        String history = historyOutput.toString(StandardCharsets.UTF_8);
        check(history.contains("NV003") && history.contains("DaDuyet"),
                "Manager displays approved request in review history");
        check(history.contains("NV004") && history.contains("KhongDuyet"),
                "Manager displays rejected request in review history");

        Files.write(requestFile, java.util.Arrays.asList(
                "NV001|KeToan|bad|ChoDuyet|extra",
                "NV002|KeToan|already done|DaDuyet",
                "NV003|KeToan|pending|ChoDuyet"), StandardCharsets.UTF_8);
        historyOutput.reset();
        capturedOutput = new PrintStream(historyOutput, true, StandardCharsets.UTF_8);
        try {
            System.setOut(capturedOutput);
            manager.xemLichSuDuyet(requestFile.toString());
        } finally {
            System.setOut(originalOutput);
            capturedOutput.close();
        }
        check(historyOutput.toString(StandardCharsets.UTF_8).contains(
                "NV001|KeToan|bad|ChoDuyet|extra"),
                "Manager reports malformed rows in review history");

        manager.duyetYeuCau(new Scanner("1\n"));
        requests = YeuCau.docDanhSach(requestFile);
        equal("NV001|KeToan|bad|ChoDuyet|extra",
                requests.get(0).getDongLoi(), "Manager preserves malformed request");
        equal("DaDuyet", requests.get(2).getTrangThai(),
                "Manager processes valid pending request");
    }

    private static void testThuNganShift() throws Exception {
        ThuNgan cashier = new ThuNgan(
                "cashier@example.com", "Tran Thi E", "0912345678", new DiaChi(),
                LocalDate.of(1990, 1, 1), "NV006", "ThuNgan", 10_000, 0, 100);
        ThuNgan nextCashier = new ThuNgan(
                "next@example.com", "Nguyen Van F", "0912345679", new DiaChi(),
                LocalDate.of(1991, 1, 1), "NV007", "ThuNgan", 10_000, 0, 0);
        HoaDonBanHang invoice = new HoaDonBanHang("HD001", "KH001");
        invoice.setNgayLap(LocalDateTime.of(2026, 11, 6, 0, 0));
        invoice.themChiTiet(new CTHoaDonBan("SP001", "Rice", 2, 50));
        invoice.setDaThanhToan(true);
        Field invoicesField = ThuNgan.class.getDeclaredField("dsHoaDonBanHang");
        invoicesField.setAccessible(true);
        invoicesField.set(cashier, new HoaDonBanHang[] {invoice});

        Path history = Paths.get("hoadon.LichSuHoaDon");
        Files.write(history, new byte[0]);
        cashier.thayCa(nextCashier);
        equal(200L, nextCashier.getTienDauCa(), "ThuNgan transfers final cash at shift change");
        check(Files.readString(history, StandardCharsets.UTF_8).contains(
                "NV006;HD001;KH001;2026-11-06"),
                "ThuNgan writes invoice history with expected fields");
    }

    private static NhanVien employee(String code, String role) {
        return new NhanVien(
                null, null, null, new DiaChi(), LocalDate.of(1990, 1, 1),
                code, role, 0, 0);
    }

    private static void check(boolean condition, String label) {
        assertions++;
        if (!condition) {
            throw new AssertionError(label);
        }
    }

    private static void equal(Object expected, Object actual, String label) {
        assertions++;
        if (expected == null ? actual != null : !expected.equals(actual)) {
            throw new AssertionError(label + ": expected <" + expected + "> but was <" + actual + ">");
        }
    }
}
