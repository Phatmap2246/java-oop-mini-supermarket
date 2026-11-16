import dichvu.*;
import doitac.*;
import hoadon.*;
import person.*;
import sanpham.*;
import utils.Validation;
import java.io.*;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/** Chạy với thư mục dữ liệu mẫu riêng, không thay đổi dữ liệu của dự án. */
public class MaDaCapTest {
    interface ThaoTac { void chay() throws Exception; }
    private static int soKiemTra;

    public static void main(String[] args) throws Exception {
        check(new HoaDonBanHang("NV001").getMaHoaDon().equals("HD001"), "Hóa đơn đầu tiên dùng mã dương");
        GiaoDien gd = GiaoDien.docDuLieu(args[0]);
        DSTaiKhoan nv = lay(gd, "dsTaiKhoan");
        QuanLySanPham sp = lay(gd, "dsSanPham");
        Danhsachdoitac dt = lay(gd, "dsDoiTac");
        Danhsachkhuyenmai km = lay(gd, "dsKhuyenMai");
        DanhSachSPKM spkm = lay(gd, "dsSanPhamKhuyenMai");
        DanhSachHoaDonBan ban = lay(gd, "dsHoaDonBan");
        DanhSachHoaDonNhap nhap = lay(gd, "dsHoaDonNhap");
        DanhSachCongNo no = lay(gd, "dsCongNo");

        thuNhapLai(() -> nv.timKhoa(new Scanner("1\nNV999\nNV001\n0\n")));
        thuNhapLai(() -> check(nv.nhapTimKiem(new Scanner("NV999\nNV001\n")) != null, "Tra cứu nhân viên sau nhập lại"));
        thuNhapLai(() -> sp.timKhoa(new Scanner("1\nSP999\nSP008\n0\n")));
        thuNhapLai(() -> sp.timKhoa(new Scanner("4\nNCC999\nNCC001\n0\n")));
        thuNhapLai(() -> dt.timKhoa(new Scanner("1\nNCC999\nNCC001\n0\n")));
        thuNhapLai(() -> km.timKhoa(new Scanner("1\nKM999\nKM001\n0\n")));
        thuNhapLai(() -> spkm.timKhoa(new Scanner("1\nSP999\nSP007\n0\n")));
        thuNhapLai(() -> spkm.timKhoa(new Scanner("2\nKM999\nKM003\n0\n")));
        thuNhapLai(() -> ban.timKhoa(new Scanner("1\nHD999\nHD001\n0\n")));
        thuNhapLai(() -> ban.timKhoa(new Scanner("2\nKH999\nKH001\n0\n")));
        thuNhapLai(() -> ban.timKhoa(new Scanner("4\nNV999\nNV003\n0\n")));
        thuNhapLai(() -> nhap.timKhoa(new Scanner("1\nHD999\nHD004\n0\n")));
        thuNhapLai(() -> nhap.timKhoa(new Scanner("2\nNCC999\nNCC001\n0\n")));
        thuNhapLai(() -> nhap.timKhoa(new Scanner("4\nNV999\nNV002\n0\n")));
        thuNhapLai(() -> no.doiChieuVaGhiNhanCongNo(new Scanner("HD999\nHD004\n"), nhap, dt));

        HoaDonNhapHang hd = new HoaDonNhapHang("NV002");
        thuNhapLai(() -> hd.nhapHoaDon(new Scanner("NCC999\nNCC001\n0\n0\nSP001\n1\nkhong\n"), sp, dt));
        check(hd.getMaNhaCungCap().equals("NCC001") && hd.getCtHoaDonNhaps().size() == 1, "Regex NCC hợp lệ và đã được cấp");
        thuNhapLai(() -> {
            String khongHopLe = "NV" + String.format("%03d", TaiKhoanNhanVien.getSoThuTu());
            String ma = Validation.inputStringRegex(new Scanner(khongHopLe + "\nNV001\n"), "", Validation.MaNV_REGEX, "", TaiKhoanNhanVien.getSoThuTu());
            check(ma.equals("NV001"), "Bộ đếm tiếp theo chưa phải mã đã cấp");
        });
        try {
            Validation.inputStringRegex(new Scanner(""), "", "KM\\d+", "", 1);
            throw new AssertionError("Phải báo chưa có mã được cấp");
        } catch (IllegalStateException expected) { soKiemTra++; }
        System.out.println("PASS: " + soKiemTra + " issued-code checks.");
    }

    private static void thuNhapLai(ThaoTac thaoTac) throws Exception {
        PrintStream cu = System.out;
        ByteArrayOutputStream log = new ByteArrayOutputStream();
        try {
            System.setOut(new PrintStream(log, true, StandardCharsets.UTF_8));
            thaoTac.chay();
        } finally { System.setOut(cu); }
        check(log.toString(StandardCharsets.UTF_8).contains("Ma chua duoc cap"), "Mã chưa cấp bị từ chối trước khi truy vấn");
    }

    @SuppressWarnings("unchecked")
    private static <T> T lay(Object obj, String ten) throws Exception {
        Field f = obj.getClass().getDeclaredField(ten); f.setAccessible(true); return (T) f.get(obj);
    }
    private static void check(boolean dung, String moTa) {
        soKiemTra++;
        if (!dung) throw new AssertionError(moTa);
    }
}
