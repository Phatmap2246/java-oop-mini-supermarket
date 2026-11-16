import dichvu.*;
import doitac.*;
import hoadon.*;
import person.*;
import sanpham.*;
import utils.Validation;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Scanner;

/** Kiểm tra logic tích hợp từ PR 43, chỉ dùng dữ liệu trong bộ nhớ. */
public class Pr43LogicIntegrationTest {
    private static int soCa;
    private interface ThaoTac { void chay() throws Exception; }

    public static void main(String[] args) throws Exception {
        Locale cu = Locale.getDefault();
        try {
            Locale.setDefault(Locale.ROOT);
            imLang(() -> {
                nhapKho();
                khuyenMai();
                thanhToan();
                baoCao();
                nhapLieu();
                giuKiemTraCu();
                boDemMa();
                dangNhap();
            });
        } finally {
            Locale.setDefault(cu);
        }
        System.out.println("PASS: PR43 logic integration (" + soCa + " checks)");
    }

    private static DienTu sanPham(QuanLySanPham ds, String ma, int ton) {
        DienTu sp = new DienTu();
        sp.setMaSanPham(ma);
        sp.setTenSanPham("Sản phẩm thử");
        sp.setGiaBan(10000);
        sp.setGiaNhap(5000);
        sp.setSoLuongTonKho(ton);
        ds.getDanhSachSanPham().add(sp);
        return sp;
    }

    private static CTHoaDonNhap chiTietNhap(String ma, int soLuong) {
        CTHoaDonNhap ct = new CTHoaDonNhap();
        ct.setMaSanpham(ma);
        ct.setTenSanPham("Sản phẩm thử");
        ct.setSoLuong(soLuong);
        ct.setDonGia(5000);
        return ct;
    }

    private static HoaDonNhapHang hoaDonNhap(boolean duyet) {
        HoaDonNhapHang hd = new HoaDonNhapHang("NV001");
        hd.setDuyet(duyet);
        hd.getCtHoaDonNhaps().add(chiTietNhap("SP001", 5));
        return hd;
    }

    private static void nhapKho() throws Exception {
        QuanLySanPham ds = new QuanLySanPham();
        DienTu sp = sanPham(ds, "SP001", 10);
        DanhSachHoaDonNhap nhaps = new DanhSachHoaDonNhap();
        HoaDonNhapHang hd = hoaDonNhap(false);
        nhaps.getDsHoaDonNhapHang().add(null);
        nhaps.getDsHoaDonNhapHang().add(hd);
        nhaps.capNhatKho(ds);
        check(sp.getSoLuongTonKho() == 10 && !hd.isDaNhapSP(), "Chưa duyệt không tăng tồn");
        hd.setDuyet(true);
        nhaps.capNhatKho(ds);
        check(sp.getSoLuongTonKho() == 15 && hd.isDaNhapSP(), "Duyệt rồi mới nhập kho");
        nhaps.capNhatKho(ds);
        check(sp.getSoLuongTonKho() == 15, "Không nhập trùng cùng phiếu");

        HoaDonNhapHang loi = hoaDonNhap(true);
        loi.getCtHoaDonNhaps().add(chiTietNhap("SP999", 3));
        nhaps.getDsHoaDonNhapHang().add(loi);
        String thongBao = imLang(() -> nhaps.capNhatKho(ds));
        check(sp.getSoLuongTonKho() == 15 && !loi.isDaNhapSP(), "Chi tiết lỗi không nhập một phần");
        check(!thongBao.contains("Thêm hàng vào kho thành công!"), "Không báo thành công chung khi nhập thất bại");
        loi.getCtHoaDonNhaps().clear();
        loi.getCtHoaDonNhaps().add(chiTietNhap("SP001", Integer.MAX_VALUE));
        nhaps.capNhatKho(ds);
        check(sp.getSoLuongTonKho() == 15 && !loi.isDaNhapSP(), "Chặn tràn số tồn kho");
    }

    private static void kichHoat(Khuyenmai km) {
        km.setNgayBatDau(LocalDate.now().minusDays(1));
        km.setNgayKetThuc(LocalDate.now().plusDays(1));
        km.setMaKhuyenMai("KM001");
    }

    private static void nhapChiTiet(CTHoaDonBan ct, Khuyenmai km, int sl, QuanLySanPham ds) {
        Danhsachkhuyenmai kms = new Danhsachkhuyenmai();
        DanhSachSPKM liens = new DanhSachSPKM();
        if (km != null) {
            kms.setKhuyenmais(new Khuyenmai[]{km});
            liens.setSanPhamKhuyenMais(new SanPhamKhuyenMai[]{new SanPhamKhuyenMai("SP001", km.getMaKhuyenMai())});
        }
        ct.nhapThongTinBan(new Scanner("SP001\n" + sl + "\n"), ds, kms, liens);
    }

    private static void khuyenMai() throws Exception {
        QuanLySanPham ds = new QuanLySanPham();
        sanPham(ds, "SP001", 30);
        KhuyenMaiPhanTram km = new KhuyenMaiPhanTram();
        kichHoat(km);
        km.setPhanTramGiamGia(0.1);
        km.setMucGiamToiDa(50000);
        km.setMucChiToiThieu(10000);
        CTHoaDonBan ct = new CTHoaDonBan();
        nhapChiTiet(ct, km, 1, ds);
        check(ct.getSoTienGiam() == 1000 && ct.tinhThanhTien() == 9000, "Bằng mức tối thiểu vẫn giảm");
        km.setMucGiamToiDa(500);
        check(km.tinhTienGiam(10000, 1) == 500, "Giữ mức giảm tối đa");
        KhuyenMaiPhanTram gioiHanKhong = new KhuyenMaiPhanTram();
        kichHoat(gioiHanKhong);
        gioiHanKhong.setPhanTramGiamGia(0.1);
        check(gioiHanKhong.tinhTienGiam(10000, 1) == 0, "Giới hạn 0 không thành giảm không giới hạn");
        km.setMucGiamToiDa(50000);
        km.setMucChiToiThieu(10001);
        nhapChiTiet(ct, km, 1, ds);
        check(ct.getSoTienGiam() == 0 && ct.tinhThanhTien() == 10000, "Chưa đủ ngưỡng không giảm");
        km.setMucChiToiThieu(0);
        km.setTonTai(false);
        nhapChiTiet(ct, km, 1, ds);
        check(ct.getSoTienGiam() == 0, "Khuyến mãi xóa mềm không áp dụng");
        km.setTonTai(true);
        km.setNgayBatDau(LocalDate.now().minusDays(10));
        km.setNgayKetThuc(LocalDate.now().minusDays(1));
        nhapChiTiet(ct, km, 1, ds);
        check(ct.getSoTienGiam() == 0, "Khuyến mãi hết hạn không áp dụng");

        KhuyenMaiTrucTiep trucTiep = new KhuyenMaiTrucTiep();
        kichHoat(trucTiep);
        trucTiep.setSoTienGiam(50000);
        nhapChiTiet(ct, trucTiep, 1, ds);
        check(ct.tinhThanhTien() == 0 && ct.getSoTienGiam() == 10000, "Không giảm quá giá trị hàng");
        nhapChiTiet(ct, null, 2, ds);
        check(ct.tinhThanhTien() == 20000 && ct.getSoTienGiam() == 0
                && ct.getSoLuongTangKem() == 0 && ct.getSoLuongPhaiTra() == 2, "Nhập lại chi tiết không giữ ưu đãi cũ");
        ct.setDonGia(20000);
        check(ct.getThanhTienSauKhuyenMai() == 40000, "Đổi đơn giá cập nhật thành tiền");
        ct.setSoLuong(3);
        check(ct.tinhThanhTien() == 60000 && ct.getSoLuongPhaiTra() == 3, "Đổi số lượng cập nhật thành tiền");

        KhuyenMaiMuaTang tang = new KhuyenMaiMuaTang();
        kichHoat(tang);
        tang.setSoLuongMuaToiThieu(2);
        tang.setSoLuongTang(1);
        nhapChiTiet(ct, tang, 3, ds);
        check(ct.getSoLuongTangKem() == 1 && ct.getSoLuongPhaiTra() == 2
                && ct.tinhThanhTien() == 20000, "Lấy tổng 3, mua 2 tặng 1");
        nhapChiTiet(ct, tang, 2, ds);
        check(ct.getSoLuongTangKem() == 0 && ct.tinhThanhTien() == 20000, "Chưa đủ một cụm mua tặng");
        tang.setSoLuongTang(2);
        nhapChiTiet(ct, tang, 6, ds);
        check(ct.getSoLuongTangKem() == 2 && ct.getSoLuongPhaiTra() == 4
                && ct.tinhThanhTien() == 40000, "Mua 2 tặng 2 và hai hàng lẻ");
        tang.setSoLuongMuaToiThieu(Integer.MAX_VALUE);
        tang.setSoLuongTang(Integer.MAX_VALUE);
        check(tang.tinhSoLuongTang(Integer.MAX_VALUE) == 0, "Cụm mua tặng không tràn int");

        Danhsachkhuyenmai kms = new Danhsachkhuyenmai();
        kms.setKhuyenmais(new Khuyenmai[]{trucTiep});
        SanPhamKhuyenMai lien = new SanPhamKhuyenMai();
        lien.setSanPhamKhuyenMai(new Scanner("SP999\nSP001\nKM999\nKM001\n"), ds, kms);
        check(lien.getMaSanPham().equals("SP001") && lien.getMaKhuyenMai().equals("KM001"), "Liên kết kiểm tra đối tượng thực tồn tại");
        check(imLang(() -> kms.thongKeCacloaiKhuyenMai(null)).contains("Vui lòng"), "Loại thống kê null không gây lỗi");
    }

    private static void thanhToan() {
        QuanLySanPham ds = new QuanLySanPham();
        DienTu sp = sanPham(ds, "SP001", 10);
        KhachHang kh = new KhachHang();
        kh.setMaKhachHang("KH001");
        kh.setTongChiTieu(1500000);
        kh.capNhatThuHang();
        DSKhachHang khs = new DSKhachHang();
        khs.setList(new KhachHang[]{kh}, 1);
        HoaDonBanHang hd = new HoaDonBanHang("NV001");
        boolean tra = hd.nhapHoaDon(new Scanner("KH001\nSP001\n1\n0\n1\n9700\n"), ds,
                new DanhSachSPKM(), new Danhsachkhuyenmai(), khs);
        check(tra && hd.tinhTongTien() == 9700 && hd.getChietKhau() == 0.03, "Nhận đúng tiền sau chiết khấu Bạc");
        check(sp.getSoLuongTonKho() == 9 && kh.getTongChiTieu() == 1509700, "Trả thành công mới trừ kho và tích lũy");
        HoaDonBanHang thatBai = new HoaDonBanHang("NV001");
        tra = thatBai.nhapHoaDon(new Scanner("KH001\nSP001\n1\n0\n1\n9600\n0\n"), ds,
                new DanhSachSPKM(), new Danhsachkhuyenmai(), khs);
        check(!tra && sp.getSoLuongTonKho() == 9 && kh.getTongChiTieu() == 1509700, "Thiếu tiền không đổi kho và tích lũy");
        kh.setTongChiTieu(4995000);
        kh.capNhatThuHang();
        HoaDonBanHang lenHang = new HoaDonBanHang("NV001");
        lenHang.nhapHoaDon(new Scanner("KH001\nSP001\n1\n0\n1\n9700\n"), ds,
                new DanhSachSPKM(), new Danhsachkhuyenmai(), khs);
        check(kh.getHangThanhVien().equals("Vàng") && lenHang.tinhTongTien() == 9700
                && lenHang.getChietKhau() == 0.03, "Lên hạng sau thanh toán không đổi số tiền hóa đơn cũ");
    }

    private static void baoCao() throws Exception {
        DienTu dt = new DienTu(); dt.setGiaBan(100000); dt.setThue(0.05);
        TieuDung td = new TieuDung(); td.setGiaBan(100000); td.setThue(0.05);
        check(dt.tinhThue() == 5000 && td.tinhThue() == 5000, "Thuế 5% điện tử và tiêu dùng");
        LocalDate ngay = LocalDate.of(LocalDate.now().getYear(), 1, 1);
        HoaDonBanHang ban = new HoaDonBanHang("NV001");
        ban.setNgayLap(ngay.atStartOfDay());
        ban.setDaThanhToan(true);
        ban.getCtHoaDonBans().add(new CTHoaDonBan("SP001", "Hàng", 1, 10000, "Không áp dụng"));
        HoaDonBanHang nhap = new HoaDonBanHang("NV001");
        nhap.setNgayLap(ngay.atTime(12, 0));
        nhap.getCtHoaDonBans().add(new CTHoaDonBan("SP001", "Hàng", 1, 99999, "Không áp dụng"));
        DanhSachHoaDonBan bans = new DanhSachHoaDonBan();
        bans.getDsHoaDonBanHang().add(ban); bans.getDsHoaDonBanHang().add(nhap);
        check(bans.thongKeDoanhThuTheoKhoang(ngay, ngay) == 10000, "Doanh thu cùng ngày, chỉ hóa đơn đã trả");
        check(imLang(() -> bans.traCuuHoaDonTrenKhoangThoiGian(ngay, ngay)).contains(ban.getMaHoaDon()), "Tra cứu gồm cả ngày đầu và cuối");
        check(imLang(() -> bans.traCuuHoaDonTrenKhoangThoiGian(ngay.atStartOfDay(), ngay.atStartOfDay())).contains(ban.getMaHoaDon()), "Tra cứu cả thời điểm biên");

        HoaDonNhapHang duyet = hoaDonNhap(true); duyet.setNgayLap(ngay.atTime(23, 59));
        HoaDonNhapHang chuaDuyet = hoaDonNhap(false); chuaDuyet.setNgayLap(ngay.atTime(12, 0));
        ArrayList<HoaDon> hoaDons = new ArrayList<>();
        hoaDons.add(ban); hoaDons.add(nhap); hoaDons.add(duyet); hoaDons.add(chuaDuyet); hoaDons.add(null);
        KeToan kt = new KeToan();
        check(kt.tinhTongDoanhThu(hoaDons, ngay, ngay) == 10000, "Kế toán bỏ hóa đơn bán chưa trả");
        check(kt.tinhTongChiPhi(hoaDons, ngay, ngay) == duyet.tinhTongTien(), "Kế toán chỉ cộng phiếu nhập đã duyệt");
        DanhSachHoaDonNhap nhaps = new DanhSachHoaDonNhap(); nhaps.getDsHoaDonNhapHang().add(duyet);
        String xuat = imLang(() -> kt.thucHienNghiepVu(new Scanner("5\n1\n" + ngay.getYear()
                + "\n6\n" + ngay.getYear() + "\n4\n0\n"), bans, nhaps, new Danhsachdoitac(), new DanhSachCongNo()));
        check(xuat.contains("Lợi nhuận ròng") && xuat.contains("CÔNG NỢ"), "Báo cáo quý/năm và menu công nợ cũ cùng chạy");
    }

    private static void nhapLieu() throws Exception {
        Scanner sc = new Scanner("abc\n-1\n 12 \nTên hàng\n");
        check(Validation.inputInt(sc, "") == 12 && sc.nextLine().equals("Tên hàng"), "Nhập số theo dòng không nuốt chuỗi kế tiếp");
        check(Validation.inputLong(new Scanner("3000000000\n"), "") == 3000000000L, "Giá trị long lớn hơn int");
        check(Validation.inputDouble(new Scanner("NaN\nInfinity\n-1\n1.25\n"), "") == 1.25, "Chặn NaN, vô cực và số âm");
        check(Validation.inputFloat(new Scanner("-18.5\n"), "") == -18.5f, "Nhiệt độ âm vẫn hợp lệ");
        check(Validation.inputCoKhong(new Scanner("1\n"), "") && !Validation.inputCoKhong(new Scanner("0\n"), ""), "Có/Không chấp nhận 1/0");
        loi(NoSuchElementException.class, () -> Validation.inputInt(new Scanner(""), ""));
        loi(NoSuchElementException.class, () -> Validation.inputLong(new Scanner(""), ""));
        loi(NoSuchElementException.class, () -> Validation.luaChon(new Scanner(""), "", 0, 1));
        loi(NoSuchElementException.class, () -> new KhuyenMaiMuaTang().setKM(new Scanner("01/01/2026\n31/12/2026\n0\n")));
        check("HD001".matches(Validation.MaHDB_REGEX) && "HDB001".matches(Validation.MaHDB_REGEX)
                && "HDN001".matches(Validation.MaHDN_REGEX), "Mã HD cũ và tiền tố HDB/HDN đều hợp lệ");
    }

    private static void giuKiemTraCu() throws Exception {
        DienTu dt = new DienTu();
        loi(IllegalArgumentException.class, () -> dt.setThoiGianBaoHanh(-6));
        TaiKhoanNhanVien nv = new TaiKhoanNhanVien();
        loi(IllegalArgumentException.class, () -> nv.setEmail("sai-email"));
        nv.setDiachi(new DiaChi("1", "Đường A", "Phường B", "Quận C", "Tỉnh D"));
        nv.setNgsinh(LocalDate.of(2000, 1, 1));
        check(!nv.getDiachi().isBlank(), "Giữ overload địa chỉ và ngày sinh cũ");
        TaiKhoanNhanVien diaChiNull = new TaiKhoanNhanVien(null, null, null, null, null, "NV008", "KeToan", 0, 0);
        check(diaChiNull.getDiachi().equals(""), "Địa chỉ null không gây NullPointerException");
        new HoaDonNhapHang("HD050", LocalDateTime.now(), "NV001", "Hoá đơn nhập", "NCC001", 0, 0, true, false);
        check(true, "Giữ constructor hóa đơn nhập 9 tham số");
        loi(IllegalArgumentException.class, () -> new HoaDonNhapHang("HD051", LocalDateTime.now(), "NV001", "Hoá đơn nhập", "NCC001", -1, 0, true, false));
        loi(IllegalArgumentException.class, () -> new HoaDonBanHang().setChietKhau(Double.NaN));
    }

    private static void boDemMa() {
        DienTu sp = new DienTu(); sp.setMaSanPham("SP800");
        check(new DienTu().getMaSanPham().equals("SP801"), "Setter mã sản phẩm tăng bộ đếm");
        KhachHang kh = new KhachHang(); kh.setMaKhachHang("KH800");
        check(new KhachHang().getMaKhachHang().equals("KH801"), "Setter mã khách hàng tăng bộ đếm");
        TaiKhoanNhanVien nv = new TaiKhoanNhanVien(); nv.setMaNV("NV800");
        check(new TaiKhoanNhanVien().getMaNV().equals("NV801"), "Setter mã nhân viên tăng bộ đếm");
        HoaDonBanHang hd = new HoaDonBanHang(); hd.setMaHoaDon("HD800");
        check(new HoaDonNhapHang().getMaHoaDon().equals("HD801"), "Hóa đơn bán/nhập chia sẻ bộ đếm");
        KhuyenMaiTrucTiep km = new KhuyenMaiTrucTiep(); km.setMaKhuyenMai("KM800");
        check(new KhuyenMaiPhanTram().getMaKhuyenMai().equals("KM801"), "Setter mã khuyến mãi tăng bộ đếm");
        new KhuyenMaiTrucTiep("KM500", "Giảm giá trực tiếp", null, null, 0, new ArrayList<>(), 1000, 1);
        check(new KhuyenMaiPhanTram().getMaKhuyenMai().equals("KM802"), "Constructor không lùi bộ đếm");
    }

    private static void dangNhap() throws Exception {
        GiaoDien gd = new GiaoDien();
        String sai = "KeToanSieuThi\nKETOAN@123\n".repeat(5);
        String xuat = imLang(() -> gd.TrangChu(new Scanner(sai + "0\n")));
        check(xuat.contains("Số lần đăng nhập sai: 5") && !xuat.contains("NGHIỆP VỤ KẾ TOÁN"), "Mật khẩu phân biệt chữ hoa/thường");
        xuat = imLang(() -> gd.TrangChu(new Scanner("KeToanSieuThi\nketoan@123\n0\n0\n")));
        check(xuat.contains("NGHIỆP VỤ KẾ TOÁN"), "Mật khẩu đúng vẫn vào được menu");
    }

    private static String imLang(ThaoTac thaoTac) throws Exception {
        PrintStream cu = System.out;
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        try (PrintStream ps = new PrintStream(bytes, true, StandardCharsets.UTF_8)) {
            System.setOut(ps);
            thaoTac.chay();
        } finally {
            System.setOut(cu);
        }
        return bytes.toString(StandardCharsets.UTF_8);
    }

    private static void loi(Class<? extends Exception> loai, ThaoTac thaoTac) throws Exception {
        try {
            thaoTac.chay();
        } catch (Exception e) {
            if (!loai.isInstance(e)) throw e;
            soCa++;
            return;
        }
        throw new AssertionError("Cần báo lỗi " + loai.getSimpleName());
    }

    private static void check(boolean dung, String moTa) {
        if (!dung) throw new AssertionError(moTa);
        soCa++;
    }
}
