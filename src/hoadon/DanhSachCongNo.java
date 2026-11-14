package hoadon;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;
import dichvu.Danhsachdoitac;
import dichvu.Doitac;
import utils.DateUtil;
import utils.Validation;

public class DanhSachCongNo implements interfaces.IQuanLy<DanhSachCongNo.KhoanCongNo> {
    private transient DanhSachHoaDonNhap nguonHoaDonNhap;
    private transient Danhsachdoitac nguonDoiTac;

    /** Kết quả tra cứu một khoản nợ từ các danh sách song song đang có. */
    public static class KhoanCongNo {
        private final String maHoaDon, maPhieuNhap, maNhaCungCap, tenNhaCungCap;
        private final long soTien;
        private final LocalDate ngayDenHan, ngayThanhToan;
        private final String nganHang;
        private final boolean daThanhToan;

        private KhoanCongNo(DanhSachCongNo ds, int i) {
            maHoaDon = ds.maHoaDonCongNo.get(i); maPhieuNhap = ds.maPhieuNhapCongNo.get(i);
            maNhaCungCap = ds.maNhaCungCapCongNo.get(i); tenNhaCungCap = ds.tenNhaCungCapCongNo.get(i);
            soTien = ds.soTienCongNo.get(i); ngayDenHan = ds.ngayDenHanCongNo.get(i);
            ngayThanhToan = ds.ngayThanhToanCongNo.get(i); nganHang = ds.nganHangThanhToanCongNo.get(i);
            daThanhToan = ds.daThanhToanCongNo.get(i);
        }
        public String getMaHoaDon() { return maHoaDon; }
        public String getMaPhieuNhap() { return maPhieuNhap; }
        public String getMaNhaCungCap() { return maNhaCungCap; }
        public String getTenNhaCungCap() { return tenNhaCungCap; }
        public long getSoTien() { return soTien; }
        public LocalDate getNgayDenHan() { return ngayDenHan; }
        public LocalDate getNgayThanhToan() { return ngayThanhToan; }
        public String getNganHang() { return nganHang; }
        public boolean isDaThanhToan() { return daThanhToan; }
        @Override public String toString() {
            return maHoaDon + " | " + maPhieuNhap + " | " + maNhaCungCap + " | " + tenNhaCungCap
                    + " | " + soTien + " | " + ngayDenHan + " | " + ngayThanhToan + " | " + nganHang
                    + " | " + (daThanhToan ? "Đã thanh toán" : "Chưa thanh toán");
        }
    }

    public void cauHinh(DanhSachHoaDonNhap hoaDons, Danhsachdoitac doiTacs) {
        nguonHoaDonNhap = hoaDons; nguonDoiTac = doiTacs;
    }
    @Override public void taoDanhSach(Scanner sc) {
        int n = Validation.inputInt(sc, "Nhập số khoản công nợ cần đối chiếu: ");
        for (int i = 0; i < n; i++) them(sc);
    }
    @Override public void them(Scanner sc) {
        if (nguonHoaDonNhap == null || nguonDoiTac == null) {
            throw new IllegalStateException("Cần gọi cauHinh để nối hóa đơn nhập và nhà cung cấp.");
        }
        doiChieuVaGhiNhanCongNo(sc, nguonHoaDonNhap, nguonDoiTac);
    }
    @Override public boolean sua(String ma, Scanner sc) {
        int i = timViTri(ma);
        if (i == -1 || daThanhToanCongNo.get(i)) return false;
        ngayDenHanCongNo.set(i, Validation.inputLocalDateRegex(sc, "Nhập hạn trả mới (dd/MM/yyyy): "));
        return true;
    }
    @Override public boolean xoa(String ma) {
        int i = timViTri(ma);
        if (i == -1 || daThanhToanCongNo.get(i)) return false;
        maHoaDonCongNo.remove(i); maPhieuNhapCongNo.remove(i); maNhaCungCapCongNo.remove(i);
        tenNhaCungCapCongNo.remove(i); soTienCongNo.remove(i); ngayDenHanCongNo.remove(i);
        ngayThanhToanCongNo.remove(i); nganHangThanhToanCongNo.remove(i); daThanhToanCongNo.remove(i);
        return true;
    }
    @Override public int timViTri(String ma) {
        if (ma == null || ma.isBlank()) return -1;
        for (int i = 0; i < maHoaDonCongNo.size(); i++) {
            if (ma.trim().equalsIgnoreCase(maHoaDonCongNo.get(i))) return i;
        }
        return -1;
    }
    @Override public KhoanCongNo timMa(String ma) {
        int i = timViTri(ma);
        return i == -1 ? null : new KhoanCongNo(this, i);
    }
    @Override public KhoanCongNo timTen(String ten) {
        if (ten == null || ten.isBlank()) return null;
        for (int i = 0; i < tenNhaCungCapCongNo.size(); i++) {
            String tenNCC = tenNhaCungCapCongNo.get(i);
            if (tenNCC != null && tenNCC.toLowerCase(java.util.Locale.ROOT)
                    .contains(ten.trim().toLowerCase(java.util.Locale.ROOT))) return new KhoanCongNo(this, i);
        }
        return null;
    }
    @Override public KhoanCongNo timKhoa(String khoa) {
        if (khoa == null || khoa.isBlank()) return null;
        for (int i = 0; i < maHoaDonCongNo.size(); i++) {
            KhoanCongNo no = new KhoanCongNo(this, i);
            if (no.toString().toLowerCase(java.util.Locale.ROOT)
                    .contains(khoa.trim().toLowerCase(java.util.Locale.ROOT))) return no;
        }
        return null;
    }
    private final ArrayList<String> maHoaDonCongNo = new ArrayList<>();
    private final ArrayList<String> maPhieuNhapCongNo = new ArrayList<>();
    private final ArrayList<String> maNhaCungCapCongNo = new ArrayList<>();
    private final ArrayList<String> tenNhaCungCapCongNo = new ArrayList<>();
    private final ArrayList<Long> soTienCongNo = new ArrayList<>();
    private final ArrayList<LocalDate> ngayDenHanCongNo = new ArrayList<>();
    private final ArrayList<LocalDate> ngayThanhToanCongNo = new ArrayList<>();
    private final ArrayList<String> nganHangThanhToanCongNo = new ArrayList<>();
    private final ArrayList<Boolean> daThanhToanCongNo = new ArrayList<>();

    public void doiChieuVaGhiNhanCongNo(Scanner sc, DanhSachHoaDonNhap danhSachHoaDonNhap, Danhsachdoitac danhSachDoiTac) {
        String maHD = Validation.inputStringRegex(sc, "Nhập mã hóa đơn NCC: ",
                HoaDonNhapHang.MA_HD_NHAP_REGEX, "Mã hóa đơn nhập không hợp lệ!");

        HoaDon hd = danhSachHoaDonNhap.timMa(maHD);
        if (!(hd instanceof HoaDonNhapHang)) {
            System.out.println(">> Không tìm thấy hóa đơn nhập hàng mã: " + maHD);
            return;
        }

        boolean biTrungmaHD = false;
        for (String ma : maHoaDonCongNo) {
            if (ma.equalsIgnoreCase(maHD)) biTrungmaHD = true;
        }
        if (biTrungmaHD) {
            System.out.println(">> Hóa đơn này đã được ghi nhận công nợ trước đó!");
            return;
        }

        HoaDonNhapHang hdNhap = (HoaDonNhapHang) hd;
        ArrayList<CTHoaDonNhap> dsChiTiet = hdNhap.getCtHoaDonNhaps();
        if (dsChiTiet.isEmpty()) {
            System.out.println(">> Hóa đơn trống, không có mặt hàng để đối chiếu.");
            return;
        }

        Doitac nhaCungCap = danhSachDoiTac.timMa(hdNhap.getMaNhaCungCap());
        if (nhaCungCap == null) return;
        System.out.println("\nNhà cung cấp: " + nhaCungCap.getTenDoiTac()
                + " (" + hdNhap.getMaNhaCungCap() + ")");
        System.out.println("=== ĐỐI CHIẾU HÀNG NHẬN ===");

        for (CTHoaDonNhap ct : dsChiTiet) {
            System.out.println("* Mặt hàng mã: " + ct.getMaSanpham());
            int slThucTe = Validation.inputInt(sc, "  - Nhập số lượng thực nhận: ");
            long giaThucTe = Validation.inputLong(sc, "  - Nhập đơn giá thực nhận: ");

            if (slThucTe != ct.getSoLuong() || Math.abs(giaThucTe - ct.getDonGia()) > 0.005) {
                System.out.printf("[!] Không khớp mã %s. Hóa đơn ghi: SL %d, giá %,.0f VND.%n",
                        ct.getMaSanpham(), ct.getSoLuong(), (double) ct.getDonGia());
                return;
            }
        }

        String maPNK = Validation.inputStringRegex(sc, "Nhập mã phiếu nhập kho (VD: PNK01): ",
                "^PNK\\d+$", "Mã phiếu phải có dạng PNK kèm chữ số!");
        boolean biTrungmaPNK = false;
        for (String ma : maPhieuNhapCongNo) {
            if (ma.equalsIgnoreCase(maPNK)) biTrungmaPNK = true;
        }
        if (biTrungmaPNK) {
            System.out.println(">> Mã phiếu nhập kho này đã tồn tại!");
            return;
        }

        LocalDate ngayNhan = nhapNgayNhanHang(sc);
        System.out.println("Chọn kỳ hạn: 1. (7 ngày) | 2. (15 ngày) | 3. (30 ngày)");
        int kyHan = Validation.luaChon(sc, "Chọn (1-3): ", 1, 3);
        LocalDate ngayDenHan = ngayNhan.plusDays(kyHan == 1 ? 7 : (kyHan == 2 ? 15 : 30));

        long tongTien = hdNhap.tinhTongTien();
        maHoaDonCongNo.add(maHD);
        maPhieuNhapCongNo.add(maPNK);
        maNhaCungCapCongNo.add(hdNhap.getMaNhaCungCap());
        tenNhaCungCapCongNo.add(nhaCungCap.getTenDoiTac());
        soTienCongNo.add(tongTien);
        ngayDenHanCongNo.add(ngayDenHan);
        ngayThanhToanCongNo.add(null);
        nganHangThanhToanCongNo.add(null);
        daThanhToanCongNo.add(false);

        System.out.printf("[+] Đối chiếu thành công. Ghi nhận nợ %,d VND, hạn trả %s.%n",
                tongTien, DateUtil.formatDate(ngayDenHan));
    }

    public void hienThiVaThanhToanCongNo(Scanner sc) {
        if (maHoaDonCongNo.isEmpty()) {
            System.out.println(">> Danh mục công nợ trống.");
            return;
        }

        ArrayList<Integer> dsChuaTra = new ArrayList<>();
        System.out.println("\n=== CÔNG NỢ NHÀ CUNG CẤP ===");
        System.out.println("Đang tải danh sách...");
        Validation.danghoatdong(1);
        for (int i = 0; i < maHoaDonCongNo.size(); i++) {
            boolean daTra = daThanhToanCongNo.get(i);
            LocalDate han = ngayDenHanCongNo.get(i);
            String trangThai = daTra ? "Đã thanh toán"
                    : han.isBefore(LocalDate.now()) ? "QUÁ HẠN" : "Chưa thanh toán";
            if (!daTra) {
                dsChuaTra.add(i);
            }

            System.out.printf("%d. HĐ: %s | NCC: %s | PNK: %s | Tiền: %,d VND | Hạn: %s | [%s]%n",
                    i + 1, maHoaDonCongNo.get(i), tenNhaCungCapCongNo.get(i),
                    maPhieuNhapCongNo.get(i), soTienCongNo.get(i),
                    DateUtil.formatDate(han), trangThai);
        }

        if (dsChuaTra.isEmpty()) {
            System.out.println(">> Tất cả công nợ đều đã được thanh toán.");
            return;
        }

        System.out.println("\n=== THANH TOÁN CÔNG NỢ ===");
        for (int i = 0; i < dsChuaTra.size(); i++) {
            int idx = dsChuaTra.get(i);
            System.out.printf("%d. HĐ: %s - %s (%,d VND - Hạn: %s)%n",
                    i + 1, maHoaDonCongNo.get(idx), tenNhaCungCapCongNo.get(idx),
                    soTienCongNo.get(idx), DateUtil.formatDate(ngayDenHanCongNo.get(idx)));
        }
        System.out.println("0. Quay lại");

        int chon = Validation.luaChon(sc, "Chọn khoản nợ cần trả: ", 0, dsChuaTra.size());
        if (chon == 0) {
            return;
        }

        int viTri = dsChuaTra.get(chon - 1);
        String nganHang = Validation.inputStringRegex(
                sc, "Nhập ngân hàng thụ hưởng: ", ".+", "Không được để trống!");
        String stk = Validation.inputStringRegex(
                sc, "Nhập số tài khoản: ", "\\S+", "Không được để trống!");

        System.out.printf("[+] Đã lập ủy nhiệm chi %,d VND tới %s (STK: %s).%n",
                soTienCongNo.get(viTri), nganHang, stk);
        daThanhToanCongNo.set(viTri, true);
        ngayThanhToanCongNo.set(viTri, LocalDate.now());
        nganHangThanhToanCongNo.set(viTri, nganHang);
        System.out.println(">> Cập nhật trạng thái: Đã thanh toán thành công!");
    }

    private LocalDate nhapNgayNhanHang(Scanner sc) {
        while (true) {
            System.out.print("Nhập ngày nhận hàng (dd/MM/yyyy): ");
            LocalDate date = DateUtil.parseDate(sc.nextLine());
            if (date != null) {
                return date;
            }
            System.out.println(">> Sai định dạng ngày!");
        }
    }

    @Override
    public void xuatDanhSach()
    {
        ArrayList<Integer> dsChuaTra = new ArrayList<>();
        System.out.println("\n=== CÔNG NỢ NHÀ CUNG CẤP ===");
        System.out.println("Đang thống kê...");
        Validation.danghoatdong(1);
        for (int i = 0; i < maHoaDonCongNo.size(); i++) {
            boolean daTra = daThanhToanCongNo.get(i);
            LocalDate han = ngayDenHanCongNo.get(i);
            String trangThai = daTra ? "Đã thanh toán"
                    : han.isBefore(LocalDate.now()) ? "QUÁ HẠN" : "Chưa thanh toán";
            if (!daTra) {
                dsChuaTra.add(i);
            }

            System.out.printf("%d. HĐ: %s | NCC: %s | PNK: %s | Tiền: %,d VND | Hạn: %s | [%s]%n",
                    i + 1, maHoaDonCongNo.get(i), tenNhaCungCapCongNo.get(i),
                    maPhieuNhapCongNo.get(i), soTienCongNo.get(i),
                    DateUtil.formatDate(han), trangThai);
        }

    }
}
