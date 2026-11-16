import dichvu.*;
import hoadon.*;
import person.*;
import sanpham.*;
import utils.*;
import java.io.*;
import java.time.*;
import java.util.*;

/** Audit only: reports counterexamples without changing application data. Seed is reproducible. */
public class ProjectAuditTest {
    static int checks, failures;
    static final PrintStream report = System.out;
    interface Probe { void run() throws Exception; }
    static void check(boolean ok, String detail) {
        checks++;
        if (!ok) { failures++; report.println("FAIL: " + detail); }
    }
    static void probe(String name, Probe run) {
        try { run.run(); } catch (Exception e) {
            failures++; report.println("FAIL: " + name + " threw " + e);
        }
    }
    static DienTu product(QuanLySanPham ds) {
        DienTu sp = new DienTu(); sp.setMaSanPham("SP001");
        sp.setTenSanPham("Test"); sp.setGiaBan(10000); sp.setSoLuongTonKho(10);
        ds.getDanhSachSanPham().add(sp); return sp;
    }
    static DSKhachHang customers() {
        KhachHang kh = new KhachHang(); kh.setMaKhachHang("KH001");
        DSKhachHang ds = new DSKhachHang(); ds.setList(new KhachHang[]{kh}, 1); return ds;
    }
    public static void main(String[] args) throws Exception {
        System.setOut(new PrintStream(OutputStream.nullOutputStream()));
        try {
            Random random = new Random(20261116);
            for (int i = 0; i < 10000; i++) {
                int buy = random.nextInt(100) + 1, gift = random.nextInt(100) + 1;
                int qty = random.nextInt(100000); long price = random.nextInt(1000000);
                KhuyenMaiMuaTang km = new KhuyenMaiMuaTang();
                km.setNgayBatDau(LocalDate.now().minusDays(1));
                km.setNgayKetThuc(LocalDate.now().plusDays(1));
                km.setSoLuongMuaToiThieu(buy); km.setSoLuongTang(gift);
                check(km.tinhSoLuongTang(qty) == qty / (buy + gift) * gift, "gift quantity iteration=" + i);
                check(km.tinhTienGiam(price, qty) == price * (qty / (buy + gift) * gift), "gift amount iteration=" + i);
                ThanhToanTienMat cash = new ThanhToanTienMat();
                long total = 1 + random.nextInt(1000000000), extra = random.nextInt(1000000);
                check(cash.thucHienThanhToan(new Scanner((total + extra) + "\n"), total)
                        && cash.getTienThua() == extra, "cash iteration=" + i);
                CTHoaDonBan ct = new CTHoaDonBan("SP001", "Test", qty, price, "None");
                check(ct.tinhThanhTien() == qty * price, "line amount iteration=" + i);
            }
            check(Validation.inputDouble(new Scanner("NaN\nInfinity\n-1\nabc\n0.1\n"), "") == 0.1, "invalid doubles");
            check(Validation.inputLong(new Scanner("999999999999999999999999\n-1\nx\n25\n"), "") == 25, "invalid longs");
            probe("duplicate item", () -> {
                QuanLySanPham products = new QuanLySanPham(); DienTu sp = product(products);
                HoaDonBanHang hd = new HoaDonBanHang("NV001");
                hd.nhapHoaDon(new Scanner("KH001\nSP001\n1\n1\nSP001\n2\n0\n1\n100000\n"),
                        products, new DanhSachSPKM(), new Danhsachkhuyenmai(), customers());
                int actual = hd.getCtHoaDonBans().stream().mapToInt(CTHoaDonBan::getSoLuong).sum();
                check(actual == 3 && sp.getSoLuongTonKho() == 7,
                        "duplicate item 1+2: invoiceQty=" + actual + ", stock=" + sp.getSoLuongTonKho() + ", expected 3/7");
            });
            probe("VAT percentage entry", () -> {
                HoaDonNhapHang h = new HoaDonNhapHang();
                h.nhapHoaDon(new Scanner("8\n0.08\n0\n"), new ArrayList<>(), "NCC001");
                check(h.getThueVAT() == 8, "VAT 8 percent rejected; accepted=" + h.getThueVAT());
            });
            probe("free invoice", () -> {
                ThanhToanTienMat cash = new ThanhToanTienMat();
                check(cash.thucHienThanhToan(new Scanner("1\n"), 0)
                        && cash.getTienKhachDua() == 0 && cash.getTienThua() == 0,
                        "zero-total invoice confirmation");
            });
            probe("overflow line", () -> {
                boolean rejected = false;
                try { new CTHoaDonBan("SP001", "Test", 2, Long.MAX_VALUE, "None"); }
                catch (IllegalArgumentException e) { rejected = true; }
                check(rejected, "overflow price x quantity must be rejected");
            });
            probe("promotion after quantity edit", () -> {
                QuanLySanPham ds = new QuanLySanPham(); product(ds);
                KhuyenMaiMuaTang km = new KhuyenMaiMuaTang();
                km.setMaKhuyenMai("KM001"); km.setNgayBatDau(LocalDate.now().minusDays(1));
                km.setNgayKetThuc(LocalDate.now().plusDays(1));
                km.setSoLuongMuaToiThieu(2); km.setSoLuongTang(1);
                Danhsachkhuyenmai kms = new Danhsachkhuyenmai(); kms.setKhuyenmais(new Khuyenmai[]{km});
                SanPhamKhuyenMai link = new SanPhamKhuyenMai();
                link.setSanPhamKhuyenMai(new Scanner("SP001\nKM001\n"), ds, kms);
                DanhSachSPKM links = new DanhSachSPKM(); links.them(link);
                CTHoaDonBan ct = new CTHoaDonBan(); ct.nhapThongTinBan(new Scanner("SP001\n3\n"), ds, kms, links);
                ct.setSoLuong(6);
                check(ct.getSoLuongTangKem() == 2 && ct.tinhThanhTien() == 40000,
                        "quantity 3->6 retains old gift=" + ct.getSoLuongTangKem() + ", total=" + ct.tinhThanhTien());
            });
            if (!Arrays.asList(args).contains("--skip-request")) probe("empty request fields", () -> {
                boolean rejected = false;
                try { new YeuCau("", "", "", "INVALID").toDongFile(); }
                catch (IllegalArgumentException e) { rejected = true; }
                check(rejected, "empty employee/role/content and invalid request state can be persisted");
            });
        } finally { System.setOut(report); }
        report.println("AUDIT: " + checks + " checks, " + failures + " counterexamples.");
        if (failures != 0) System.exit(1);
    }
}
