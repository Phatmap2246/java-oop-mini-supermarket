import interfaces.IQuanLy;
import person.*;
import sanpham.*;
import dichvu.*;
import hoadon.*;
import utils.YeuCau;

import java.io.*;
import java.lang.reflect.Field;
import java.nio.file.*;
import java.time.LocalDate;
import java.util.*;

/** Chạy trên bản sao dữ liệu mẫu, với working directory trong thư mục thử. */
public class QuanLyInterfaceTest {
    private static int soKiemTra;

    public static void main(String[] args) throws Exception {
        PrintStream out = System.out;
        ByteArrayOutputStream log = new ByteArrayOutputStream();
        try {
            Files.createDirectories(utils.DuongDanLuuTru.thuMuc());
            System.setOut(new PrintStream(log, true, java.nio.charset.StandardCharsets.UTF_8));
            GiaoDien gd = GiaoDien.docDuLieu(args[0]);
            IQuanLy<SanPham> sp = lay(gd, "dsSanPham");
            IQuanLy<NhanVien> nv = lay(gd, "dsNhanVien");
            IQuanLy<KhachHang> kh = lay(gd, "dsKhachHang");
            IQuanLy<Doitac> dt = lay(gd, "dsDoiTac");
            IQuanLy<Khuyenmai> km = lay(gd, "dsKhuyenMai");
            IQuanLy<SanPhamKhuyenMai> spkm = lay(gd, "dsSanPhamKhuyenMai");
            IQuanLy<HoaDonBanHang> ban = lay(gd, "dsHoaDonBan");
            IQuanLy<HoaDonNhapHang> nhap = lay(gd, "dsHoaDonNhap");
            IQuanLy<DanhSachCongNo.KhoanCongNo> no = lay(gd, "dsCongNo");
            DSYeuCau yc = new DSYeuCau();
            Path fileYC = Paths.get(args[0], "yeu-cau-thu.txt");
            yc.cauHinhFile(fileYC.toString());
            List<IQuanLy<?>> cacDanhSach = Arrays.asList(sp, nv, kh, dt, km, spkm, ban, nhap, no, yc);
            for (IQuanLy<?> ds : cacDanhSach) {
                check(ds.timMa(null) == null && ds.timTen(null) == null && ds.timKhoa(null) == null, "Tìm null");
                check(ds.timViTri("khong-ton-tai") == -1, "Không tìm thấy vị trí");
                check(!ds.xoa("khong-ton-tai"), "Không xóa mã thiếu");
                check(!ds.sua("khong-ton-tai", new Scanner("")), "Không sửa mã thiếu");
            }
            check(sp.timMa(" sp001 ") != null && sp.timTen("SỮA") != null && sp.timKhoa("SP001") != null, "Tìm sản phẩm");
            check(nv.timMa("NV001") != null && nv.timTen("Minh An") != null && nv.timKhoa("QuanLy") != null, "Tìm nhân viên");
            check(kh.timMa("KH001") != null && kh.timTen("Thảo Linh") != null && kh.timKhoa("KH001") != null, "Tìm khách hàng");
            check(dt.timMa("NCC001") != null && dt.timTen("An Nhiên") != null && dt.timKhoa("NCC001") != null, "Tìm đối tác");
            check(km.timMa("KM001") != null && km.timTen("phần trăm") != null && km.timKhoa("KM001") != null, "Tìm khuyến mãi");
            check(spkm.timMa("SP001") != null && spkm.timTen("sữa") != null && spkm.timKhoa("KM001") != null, "Tìm liên kết theo tên sản phẩm");
            check(ban.timMa("HD001") != null && ban.timTen("Thảo Linh") != null && ban.timKhoa("sữa") != null, "Tìm hóa đơn bán theo khách hàng/sản phẩm");
            check(nhap.timMa("HD004") != null && nhap.timTen("Bình Minh") != null && nhap.timKhoa("nước giặt") != null, "Tìm hóa đơn nhập theo NCC/sản phẩm");
            check(no.timMa("HD004") != null && no.timTen("Bình Minh") != null && no.timKhoa("PNK001") != null, "Tìm công nợ");

            check(!km.sua("KM001", new Scanner("99\n")), "Lựa chọn sửa khuyến mãi sai trả false");
            check(!sp.sua("SP001", new Scanner("99\n")), "Lựa chọn sửa sản phẩm sai trả false");
            check(spkm.sua("SP001", new Scanner("1\nSP001\n")), "Giữ mã liên kết hiện tại không bị nhận nhầm là trùng");
            spkm.them(new Scanner("SP008\nKM003\n"));
            check(spkm.timMa("SP008") != null, "Thêm qua interface");
            check(!ban.xoa("HD001") && !ban.sua("HD001", new Scanner("")), "Giữ hóa đơn đã thanh toán");
            check(!nhap.xoa("HD004") && !nhap.sua("HD004", new Scanner("")), "Giữ hóa đơn nhập đã duyệt");
            check(nhap.sua("HD006", new Scanner("4\n15000\n")), "Sửa hóa đơn nhập chưa duyệt qua interface");
            check(nhap.timMa("HD006").getChiPhiVanChuyen() == 15000, "Phí vận chuyển đã sửa");
            check(ban.sua("HD003", new Scanner("1\nKH002\n")), "Sửa hóa đơn bán qua interface");
            check(ban.xoa("HD003") && nhap.xoa("HD006"), "Xóa hóa đơn nháp");
            check(!no.xoa("HD005"), "Giữ công nợ đã thanh toán");
            LocalDate han = LocalDate.now().plusDays(20);
            check(no.sua("HD004", new Scanner(han.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")) + "\n")), "Sửa hạn nợ");
            check(no.timMa("HD004").getNgayDenHan().equals(han), "Hạn nợ được cập nhật");
            check(no.xoa("HD004") && no.timMa("HD005").getSoTien() == 4500000, "Xóa đồng bộ các danh sách công nợ");

            Files.writeString(fileYC, "dong-cu-sai-dinh-dang\n");
            yc.them(new Scanner("NV010\nQuanKho\nCần nhập sữa\n"));
            check(yc.timMa("NV010") != null && yc.timTen("sữa") != null && yc.timKhoa("QuanKho") != null, "Thêm/tìm yêu cầu");
            check(yc.sua("NV010", new Scanner("Bổ sung sữa\n2\n")), "Sửa yêu cầu");
            check(yc.timMa("NV010").getTrangThai().equals("DaDuyet"), "Trạng thái yêu cầu");
            check(yc.xoa("NV010") && yc.getList().isEmpty(), "Xóa yêu cầu cuối cùng");
            check(YeuCau.docDanhSach(fileYC).stream().anyMatch(YeuCau.BanGhiDataBase::laDongLoi), "Giữ dòng lỗi lịch sử yêu cầu");

            check(sp.xoa("SP001") && sp.timMa("SP001") == null && sp.timTen("hộp 1 lít") == null, "Xóa mềm sản phẩm");
            check(nv.xoa("NV004") && nv.timMa("NV004") == null, "Xóa mềm nhân viên");
            check(kh.xoa("KH001") && kh.timMa("KH001") == null, "Xóa mềm khách hàng");
            check(dt.xoa("NCC001") && dt.timMa("NCC001") == null, "Xóa mềm đối tác");
            check(km.xoa("KM001") && km.timMa("KM001") == null, "Xóa mềm khuyến mãi");
            check(spkm.xoa("SP004") && spkm.timMa("SP004") == null, "Xóa mềm liên kết");
            String luuLai = args[0] + "-sau-sua";
            gd.ghiDuLieu(luuLai);
            GiaoDien moLai = GiaoDien.docDuLieu(luuLai);
            IQuanLy<SanPham> spMoLai = lay(moLai, "dsSanPham");
            IQuanLy<SanPhamKhuyenMai> kmMoLai = lay(moLai, "dsSanPhamKhuyenMai");
            check(spMoLai.timMa("SP001") == null, "Giữ xóa mềm sau đọc TXT");
            check(kmMoLai.timTen("Ấm đun") != null, "Nối lại nguồn tìm tên sau đọc TXT");
        } catch (Throwable e) {
            out.println(log.toString(java.nio.charset.StandardCharsets.UTF_8));
            throw e;
        } finally {
            System.setOut(out);
        }
        System.out.println("PASS: " + soKiemTra + " interface checks across 10 list classes.");
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
