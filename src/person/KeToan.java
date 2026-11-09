package person;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

import hoadon.CTHoaDon;
import hoadon.HoaDon;
import hoadon.HoaDonBanHang;
import hoadon.HoaDonNhapHang;
import hoadon.QuanLyHoaDon;
import utils.DateUtil;
import utils.Validation;

public class KeToan extends NhanVien {
    ArrayList<Long> doanhThu = new ArrayList<>();
    ArrayList<Long> chiPhi = new ArrayList<>();
    private final ArrayList<String> maHoaDonCongNo = new ArrayList<>();
    private final ArrayList<String> maPhieuNhapCongNo = new ArrayList<>();
    private final ArrayList<String> maNhaCungCapCongNo = new ArrayList<>();
    private final ArrayList<String> tenNhaCungCapCongNo = new ArrayList<>();
    private final ArrayList<Long> soTienCongNo = new ArrayList<>();
    private final ArrayList<LocalDate> ngayDenHanCongNo = new ArrayList<>();
    private final ArrayList<Boolean> daThanhToanCongNo = new ArrayList<>();

    public long tinhTongDoanhThu(ArrayList<HoaDon> dshd) {
        long tongDoanhThu = 0;
        for (HoaDon hoaDon : dshd) {
            if (hoaDon instanceof HoaDonBanHang) {
                tongDoanhThu += hoaDon.tinhTongTien();
            }
        }
        return tongDoanhThu;
    }

    public long tinhTongChiPhi(ArrayList<HoaDon> dshd) {
        long tongChiPhi = 0;
        for (int i = 0; i < dshd.size(); i++) {
            if (dshd.get(i) instanceof HoaDonNhapHang) {
                tongChiPhi += dshd.get(i).tinhTongTien();
            }
        }
        return tongChiPhi;
    }

    public long tinhLoiNhuan(ArrayList<HoaDon> dshd) {
        return tinhTongDoanhThu(dshd) - tinhTongChiPhi(dshd);
    }

    public long tinhLoiNhuanRong(ArrayList<HoaDon> dshd) {
        double VAT = 0.08;
        double TNDN = 0.2;
        return (long) (tinhLoiNhuan(dshd) * VAT * TNDN);
    }

    private void doiChieuVaGhiNhanCongNo(Scanner sc, QuanLyHoaDon quanLyHoaDon) {
        String maHD = Validation.inputStringRegex(sc, "Nhập mã hóa đơn NCC: ",
                HoaDonNhapHang.MA_HD_NHAP_REGEX, "Mã hóa đơn nhập không hợp lệ!");

        HoaDon hd = quanLyHoaDon.timKiemTheoMa(maHD);
        if (!(hd instanceof HoaDonNhapHang)) {
            System.out.println(">> Không tìm thấy hóa đơn nhập hàng mã: " + maHD);
            return;
        }

        if (maHoaDonCongNo.stream().anyMatch(ma -> ma.equalsIgnoreCase(maHD))) {
            System.out.println(">> Hóa đơn này đã được ghi nhận công nợ trước đó!");
            return;
        }

        HoaDonNhapHang hdNhap = (HoaDonNhapHang) hd;
        ArrayList<CTHoaDon> dsChiTiet = hdNhap.getDanhSachChiTiet();
        if (dsChiTiet.isEmpty()) {
            System.out.println(">> Hóa đơn trống, không có mặt hàng để đối chiếu.");
            return;
        }

        System.out.println("\nNhà cung cấp: " + hdNhap.getTenDoiTac()
                + " (" + hdNhap.getMaNhaCungCap() + ")");
        System.out.println("=== ĐỐI CHIẾU HÀNG NHẬN ===");

        for (CTHoaDon ct : dsChiTiet) {
            System.out.println("* Mặt hàng mã: " + ct.getMaSanPham());
            int slThucTe = Validation.inputInt(sc, "  - Nhập số lượng thực nhận: ");
            long giaThucTe = Validation.inputLong(sc, "  - Nhập đơn giá thực nhận: ");

            if (slThucTe != ct.getSoLuong() || Math.abs(giaThucTe - ct.getDonGia()) > 0.005) {
                System.out.printf("[!] Không khớp mã %s. Hóa đơn ghi: SL %d, giá %,.0f VND.%n",
                        ct.getMaSanPham(), ct.getSoLuong(), (double) ct.getDonGia());
                return;
            }
        }

        String maPNK = Validation.inputStringRegex(sc, "Nhập mã phiếu nhập kho (VD: PNK01): ",
                "^PNK\\d+$", "Mã phiếu phải có dạng PNK kèm chữ số!");
        if (maPhieuNhapCongNo.stream().anyMatch(ma -> ma.equalsIgnoreCase(maPNK))) {
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
        tenNhaCungCapCongNo.add(hdNhap.getTenDoiTac());
        soTienCongNo.add(tongTien);
        ngayDenHanCongNo.add(ngayDenHan);
        daThanhToanCongNo.add(false);

        System.out.printf("[+] Đối chiếu thành công. Ghi nhận nợ %,d VND, hạn trả %s.%n",
                tongTien, DateUtil.formatDate(ngayDenHan));
    }

    private void hienThiVaThanhToanCongNo(Scanner sc) {
        if (maHoaDonCongNo.isEmpty()) {
            System.out.println(">> Danh mục công nợ trống.");
            return;
        }

        ArrayList<Integer> dsChuaTra = new ArrayList<>();
        System.out.println("\n=== CÔNG NỢ NHÀ CUNG CẤP ===");
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

    public void thanhToanCongNo(Scanner sc, QuanLyHoaDon quanLyHoaDon) {
        while (true) {
            System.out.println("\n=== KẾ TOÁN: CÔNG NỢ NHÀ CUNG CẤP ===");
            System.out.println("1. Đối chiếu phiếu nhập kho & Ghi nhận nợ");
            System.out.println("2. Xem danh sách & Thanh toán công nợ");
            System.out.println("0. Thoát");

            int luaChon = Validation.luaChon(sc, "Chọn chức năng: ", 0, 2);
            if (luaChon == 0) {
                return;
            }
            if (luaChon == 1) {
                doiChieuVaGhiNhanCongNo(sc, quanLyHoaDon);
            } else {
                hienThiVaThanhToanCongNo(sc);
            }
        }
    }
}
