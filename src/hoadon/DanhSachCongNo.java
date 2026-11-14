package hoadon;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;
import dichvu.Danhsachdoitac;
import dichvu.Doitac;
import utils.DateUtil;
import utils.Validation;

public class DanhSachCongNo {
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

        HoaDon hd = danhSachHoaDonNhap.timKiemHoaDonNhapHang(maHD);
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

        Doitac nhaCungCap = danhSachDoiTac.traCuuDoiTac(hdNhap.getMaNhaCungCap());
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

    public void thongKeCongNo()
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
