import hoadon.*;
import java.io.*;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.*;

public class TrangThaiDuyetTest {
    private static int checks;
    private static void check(boolean ok) {
        checks++;
        if (!ok) throw new AssertionError("Check " + checks);
    }
    private static HoaDonNhapHang hd(int id) {
        return new HoaDonNhapHang("HDN" + id, LocalDateTime.of(2026, 11, 15, 9, 0),
                "NV001", "Nhap", "NCC001", 0, 0, false, false);
    }
    private static DanhSachHoaDonNhap ds(HoaDonNhapHang... hds) {
        return new DanhSachHoaDonNhap(new ArrayList<>(Arrays.asList(hds)));
    }
    private static void fails(Runnable run) {
        try { run.run(); } catch (IllegalArgumentException | IllegalStateException ex) { check(true); return; }
        throw new AssertionError("Expected invalid state to be rejected");
    }
    public static void main(String[] args) throws Exception {
        PrintStream original = System.out;
        System.setOut(new PrintStream(OutputStream.nullOutputStream()));
        try {
            for (String input : new String[]{"HDN1\n1\n", "bad\n\nhdn1\nabc\n-1\n3\n1\n"}) {
                HoaDonNhapHang h = hd(1);
                ds(h).duyetHoaDonNhap(new Scanner(input));
                check(h.isDuyet());
            }
            for (String input : new String[]{"HDN1\n2\n", "HDN1\n0\n", "0\n", "HDN999\n", ""}) {
                HoaDonNhapHang h = hd(1);
                ds(h).duyetHoaDonNhap(new Scanner(input));
                check(h.getTrangThaiDuyet() == (input.contains("\n2\n") ? TrangThaiDuyet.TU_CHOI : TrangThaiDuyet.CHO_DUYET));
            }
            ds().duyetHoaDonNhap(new Scanner(""));
            ds().xoaHoaDonBiTuChoi(new Scanner(""));
            for (TrangThaiDuyet state : TrangThaiDuyet.values()) {
                HoaDonNhapHang h = hd(1); h.setTrangThaiDuyet(state);
                check(h.toString().contains(state.toString()));
                check(HoaDonNhapHang.tuDongFile(h.toDongFile()).getTrangThaiDuyet() == state);
                check(ds(h).xoa("HDN1") == (state == TrangThaiDuyet.TU_CHOI));
                if (state != TrangThaiDuyet.DA_DUYET) fails(() -> h.setDaNhapSP(true));
                else {
                    h.setDaNhapSP(true);
                    fails(() -> h.setTrangThaiDuyet(TrangThaiDuyet.TU_CHOI));
                }
            }
            for (String input : new String[]{"bad\nHDN1\nwrong\nco\n", "HDN1\nkhong\n", "0\n", "HDN999\n", "HDN2\n"}) {
                HoaDonNhapHang h = hd(1); h.setTrangThaiDuyet(TrangThaiDuyet.TU_CHOI);
                HoaDonNhapHang pending = hd(2);
                DanhSachHoaDonNhap list = ds(h, pending);
                list.xoaHoaDonBiTuChoi(new Scanner(input));
                check((list.timMa("HDN1") == null) == input.endsWith("\nco\n"));
                check(list.timMa("HDN2") == pending);
            }
            HoaDonNhapHang h = hd(1);
            for (boolean approved : new boolean[]{false, true}) {
                String old = h.toDongFile().replace("CHO_DUYET", String.valueOf(approved));
                check(HoaDonNhapHang.tuDongFile(old).isDuyet() == approved);
            }
            fails(() -> h.setTrangThaiDuyet(null));
            fails(() -> HoaDonNhapHang.tuDongFile(h.toDongFile().replace("CHO_DUYET", "INVALID")));
            fails(() -> HoaDonNhapHang.tuDongFile(h.toDongFile().replace("CHO_DUYET;false", "TU_CHOI;true")));
            Path file = Files.createTempDirectory("duyet-test-").resolve("nhap.txt");
            HoaDonNhapHang rejected = hd(2); rejected.setTrangThaiDuyet(TrangThaiDuyet.TU_CHOI);
            DanhSachHoaDonNhap list = ds(h, rejected);
            list.ghiFile(file.toString());
            DanhSachHoaDonNhap loaded = ds(); loaded.docFile(file.toString());
            check(loaded.timMa("HDN2").getTrangThaiDuyet() == TrangThaiDuyet.TU_CHOI);
            loaded.xoaHoaDonBiTuChoi(new Scanner("HDN2\nco\n"));
            loaded.ghiFile(file.toString()); list.docFile(file.toString());
            check(list.timMa("HDN2") == null && list.timMa("HDN1") != null);
            Files.writeString(file, "HDNHAP;broken");
            try { list.docFile(file.toString()); throw new AssertionError(); }
            catch (IOException expected) { check(list.timMa("HDN1") != null); }
            HoaDonNhapHang approved = hd(3); approved.setDuyet(true);
            ds(h, rejected).capNhatKho(new sanpham.QuanLySanPham());
            check(!h.isDaNhapSP() && !rejected.isDaNhapSP());
            fails(() -> new HoaDonNhapHang("HDN4", LocalDateTime.now(), "NV001", "Nhap", "NCC001", 0, 0, false, true));
            list = ds(h, rejected, approved);
            list.duyetHoaDonNhap(new Scanner("HDN2\n")); check(!rejected.isDuyet());
            list.duyetHoaDonNhap(new Scanner("HDN3\n")); check(approved.isDuyet());
            check(!list.sua("HDN2", new Scanner("")));
        } finally { System.setOut(original); }
        System.out.println("PASS: " + checks + " approval, deletion, input and persistence checks.");
    }
}
