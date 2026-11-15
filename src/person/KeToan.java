package person;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

import hoadon.HoaDon;
import hoadon.HoaDonBanHang;
import hoadon.DanhSachHoaDonBan;
import hoadon.DanhSachHoaDonNhap;
import hoadon.DanhSachCongNo;
import dichvu.Danhsachdoitac;
import utils.DateUtil;
import utils.Validation;

public class KeToan extends NhanVien {
    // Mặc định kỳ hạn thanh toán công nợ với NCC là 30 ngày kể từ ngày lập hóa đơn
    private static final int KY_HAN_CONG_NO_NGAY = 30;

    public KeToan() {
        super();
        super.setVaiTro("KeToan");
    }

    public KeToan(String email, String hoten, String sdt, DiaChi diachi, LocalDate ngsinh,
            String maNV, long luong, long phuCapTrachNhiem) {
        super(email, hoten, sdt, diachi, ngsinh, maNV, "KeToan", luong, phuCapTrachNhiem);
    }

    public KeToan(KeToan kt) {
        super(kt);
    }

    @Override
    public String getVaiTro() {
        return "KeToan";
    }

    public void thucHienNghiepVu(Scanner sc, DanhSachHoaDonBan dsHoaDonBan,
            DanhSachHoaDonNhap dsHoaDonNhap, Danhsachdoitac dsDoiTac, DanhSachCongNo dsCongNo) {
        dsCongNo.cauHinh(dsHoaDonNhap, dsDoiTac);
        while (true) {
            System.out.println("\n=== NGHIỆP VỤ KẾ TOÁN ===");
            System.out.println("1. Thống kê tài chính theo khoảng ngày");
            System.out.println("2. Đối chiếu và ghi nhận công nợ");
            System.out.println("3. Xem và thanh toán công nợ");
            System.out.println("4. Thống kê công nợ");
            System.out.println("0. Trở về trang chủ");
            int chon = Validation.luaChon(sc, "Chọn chức năng: ", 0, 4);
            switch (chon) {
                case 0:
                    return;
                case 1:
                    LocalDate batDau = Validation.inputLocalDateRegex(sc, "Ngày bắt đầu (dd/MM/yyyy): ");
                    LocalDate ketThuc = Validation.inputLocalDateRegex(sc, "Ngày kết thúc (dd/MM/yyyy): ");
                    if (ketThuc.isBefore(batDau)) {
                        System.out.println("Ngày kết thúc phải từ ngày bắt đầu trở đi.");
                        break;
                    }
                    ArrayList<HoaDon> hoaDons = new ArrayList<>();
                    hoaDons.addAll(dsHoaDonBan.getDsHoaDonBanHang());
                    hoaDons.addAll(dsHoaDonNhap.getDsHoaDonNhapHang());
                    System.out.printf("Doanh thu: %,d VND%nChi phí nhập hàng: %,d VND%n"
                                    + "Lợi nhuận: %,d VND%nLợi nhuận ròng: %,d VND%n",
                            tinhTongDoanhThu(hoaDons, batDau, ketThuc),
                            tinhTongChiPhi(hoaDons, batDau, ketThuc),
                            tinhLoiNhuan(hoaDons, batDau, ketThuc),
                            tinhLoiNhuanRong(hoaDons, batDau, ketThuc));
                    break;
                case 2:
                    dsCongNo.them(sc);
                    break;
                case 3:
                    dsCongNo.hienThiVaThanhToanCongNo(sc);
                    break;
                case 4:
                    dsCongNo.xuatDanhSach();
                    break;
            }
        }
    }

    // ==================== 1. NHÓM HÀM TÍNH TOÁN TÀI CHÍNH ====================

    public long tinhTongDoanhThu(ArrayList<HoaDon> dshd, LocalDate ngayBatDau, LocalDate ngayKetThuc) {
        if (dshd == null || dshd.isEmpty()) {
            return 0;
        }
        long tongDoanhThu = 0;
        for (HoaDon hd : dshd) {
            if (hd == null || hd.getNgayLap() == null) continue;
            LocalDate ngayLap = hd.getNgayLap().toLocalDate();
            if (hd instanceof HoaDonBanHang
                    && !ngayLap.isBefore(ngayBatDau)
                    && !ngayLap.isAfter(ngayKetThuc)) {
                tongDoanhThu += hd.tinhTongTien();
            }
        }
        return tongDoanhThu;
    }

    public long tinhTongChiPhi(ArrayList<HoaDon> dshd, LocalDate ngayBatDau, LocalDate ngayKetThuc) {
        if (dshd == null || dshd.isEmpty()) {
            return 0;
        }
        long tongChiPhi = 0;
        for (HoaDon hd : dshd) {
            if (hd == null || hd.getNgayLap() == null) continue;
            LocalDate ngayLap = hd.getNgayLap().toLocalDate();
            // Đã sửa: lấy các hóa đơn KHÔNG phải HoaDonBanHang làm chi phí
            if (!(hd instanceof HoaDonBanHang)
                    && !ngayLap.isBefore(ngayBatDau)
                    && !ngayLap.isAfter(ngayKetThuc)) {
                tongChiPhi += hd.tinhTongTien();
            }
        }
        return tongChiPhi;
    }

    public long tinhLoiNhuan(ArrayList<HoaDon> dshd, LocalDate ngayBatDau, LocalDate ngayKetThuc) {
        return tinhTongDoanhThu(dshd, ngayBatDau, ngayKetThuc)
                - tinhTongChiPhi(dshd, ngayBatDau, ngayKetThuc);
    }

    public long tinhLoiNhuanRong(ArrayList<HoaDon> dshd, LocalDate ngayBatDau, LocalDate ngayKetThuc) {
        long loiNhuanTruocThue = tinhLoiNhuan(dshd, ngayBatDau, ngayKetThuc);
        if (loiNhuanTruocThue <= 0) {
            return loiNhuanTruocThue;
        }
        long thueTNDN = (long) (loiNhuanTruocThue * 0.2);
        return loiNhuanTruocThue - thueTNDN;
    }

    // ==================== 2. NHÓM HÀM XỬ LÝ CÔNG NỢ (DÙNG LIST THAM SỐ) ====================

    // Lọc các hóa đơn nhập/chi phí từ danh sách hóa đơn chung
    public ArrayList<HoaDon> layDanhSachHoaDonCongNo(ArrayList<HoaDon> dshd) {
        ArrayList<HoaDon> dsCongNo = new ArrayList<>();
        if (dshd == null) return dsCongNo;

        for (HoaDon hd : dshd) {
            if (hd != null && !(hd instanceof HoaDonBanHang)) {
                dsCongNo.add(hd);
            }
        }
        return dsCongNo;
    }

    // Lọc các hóa đơn công nợ chưa thanh toán
    public ArrayList<HoaDon> layDanhSachCongNoChuaTra(ArrayList<HoaDon> dshd,
            ArrayList<String> dsMaDaThanhToan) {
        ArrayList<HoaDon> dsChuaTra = new ArrayList<>();
        for (HoaDon hd : layDanhSachHoaDonCongNo(dshd)) {
            if (!daThanhToan(hd.getMaHoaDon(), dsMaDaThanhToan)) {
                dsChuaTra.add(hd);
            }
        }
        return dsChuaTra;
    }

    private boolean daThanhToan(String maHD, ArrayList<String> dsMaDaThanhToan) {
        if (maHD == null || dsMaDaThanhToan == null) return false;
        for (String ma : dsMaDaThanhToan) {
            if (maHD.equalsIgnoreCase(ma)) {
                return true;
            }
        }
        return false;
    }

    private LocalDate tinhNgayDenHan(HoaDon hd) {
        if (hd.getNgayLap() == null) return LocalDate.now();
        return hd.getNgayLap().toLocalDate().plusDays(KY_HAN_CONG_NO_NGAY);
    }

    private String layTrangThaiCongNo(HoaDon hd, ArrayList<String> dsMaDaThanhToan) {
        if (daThanhToan(hd.getMaHoaDon(), dsMaDaThanhToan)) {
            return "Đã thanh toán";
        }
        LocalDate han = tinhNgayDenHan(hd);
        return han.isBefore(LocalDate.now()) ? "QUÁ HẠN" : "Chưa thanh toán";
    }

    // Chỉ hiển thị danh sách công nợ (gọi được độc lập ở mọi nơi)
    public void hienThiDanhSachCongNo(ArrayList<HoaDon> dshd, ArrayList<String> dsMaDaThanhToan) {
        System.out.println("\n=== CÔNG NỢ NHÀ CUNG CẤP ===");
        ArrayList<HoaDon> dsCongNo = layDanhSachHoaDonCongNo(dshd);
        if (dsCongNo.isEmpty()) {
            System.out.println(">> Danh mục công nợ trống.");
            return;
        }

        for (int i = 0; i < dsCongNo.size(); i++) {
            HoaDon hd = dsCongNo.get(i);
            LocalDate han = tinhNgayDenHan(hd);
            String trangThai = layTrangThaiCongNo(hd, dsMaDaThanhToan);

            System.out.printf("%d. HĐ: %s | Đối tác: %s | Tiền: %,d VND | Hạn: %s | [%s]%n",
                    i + 1,
                    hd.getMaHoaDon(),
                    hd.tinhTongTien(),
                    DateUtil.formatDate(han),
                    trangThai);
        }
    }

    private void hienThiMenuChonNoChuaTra(ArrayList<HoaDon> dsChuaTra) {
        System.out.println("\n=== THANH TOÁN CÔNG NỢ ===");
        for (int i = 0; i < dsChuaTra.size(); i++) {
            HoaDon hd = dsChuaTra.get(i);
            System.out.printf("%d. HĐ: %s - %s (%,d VND - Hạn: %s)%n",
                    i + 1,
                    hd.getMaHoaDon(),
                    hd.tinhTongTien(),
                    DateUtil.formatDate(tinhNgayDenHan(hd)));
        }
        System.out.println("0. Quay lại");
    }

    private void thucHienChuyenKhoan(Scanner sc, HoaDon hoaDonNo, ArrayList<String> dsMaDaThanhToan) {
        String nganHang = Validation.inputStringRegex(
                sc, "Nhập ngân hàng thụ hưởng: ", ".+", "Không được để trống!");
        String stk = Validation.inputStringRegex(
                sc, "Nhập số tài khoản: ", "\\S+", "Không được để trống!");

        System.out.printf("[+] Đã lập ủy nhiệm chi %,d VND tới %s (STK: %s) cho HĐ %s.%n",
                hoaDonNo.tinhTongTien(), nganHang, stk, hoaDonNo.getMaHoaDon());

        if (dsMaDaThanhToan != null && !daThanhToan(hoaDonNo.getMaHoaDon(), dsMaDaThanhToan)) {
            dsMaDaThanhToan.add(hoaDonNo.getMaHoaDon());
        }
        System.out.println(">> Cập nhật trạng thái: Đã thanh toán thành công!");
    }

    public void hienThiVaThanhToanCongNo(Scanner sc, ArrayList<HoaDon> dshd,
            ArrayList<String> dsMaDaThanhToan) {
        hienThiDanhSachCongNo(dshd, dsMaDaThanhToan);
        ArrayList<HoaDon> dsChuaTra = layDanhSachCongNoChuaTra(dshd, dsMaDaThanhToan);
        if (dsChuaTra.isEmpty()) {
            System.out.println(">> Không còn khoản công nợ nào chưa thanh toán.");
            return;
        }

        hienThiMenuChonNoChuaTra(dsChuaTra);
        int chon = Validation.luaChon(sc, "Chọn khoản nợ cần trả: ", 0, dsChuaTra.size());
        if (chon == 0) {
            return;
        }

        thucHienChuyenKhoan(sc, dsChuaTra.get(chon - 1), dsMaDaThanhToan);
    }

    public void thanhToanCongNo(Scanner sc, ArrayList<HoaDon> dshd,
            ArrayList<String> dsMaDaThanhToan) {
        while (true) {
            System.out.println("\n=== KẾ TOÁN: CÔNG NỢ NHÀ CUNG CẤP ===");
            System.out.println("1. Xem danh sách & Thanh toán công nợ");
            System.out.println("2. Chỉ xem danh sách công nợ");
            System.out.println("0. Thoát");

            int luaChon = Validation.luaChon(sc, "Chọn chức năng: ", 0, 2);
            if (luaChon == 0) {
                return;
            } else if (luaChon == 1) {
                hienThiVaThanhToanCongNo(sc, dshd, dsMaDaThanhToan);
            } else {
                hienThiDanhSachCongNo(dshd, dsMaDaThanhToan);
            }
        }
    }

    // ==================== 3. NHÓM HÀM THỐNG KÊ ====================

    public void thongKe(ArrayList<HoaDon> dshd, ArrayList<String> dsMaDaThanhToan,
            LocalDate ngayBatDau, LocalDate ngayKetThuc) {
        System.out.println("\n=== BẢNG THỐNG KÊ TÀI CHÍNH ===");
        System.out.printf("1. Doanh thu từ %s đến %s: %,d VND%n",
                DateUtil.formatDate(ngayBatDau), DateUtil.formatDate(ngayKetThuc),
                tinhTongDoanhThu(dshd, ngayBatDau, ngayKetThuc));
        System.out.printf("2. Lợi nhuận ròng từ %s đến %s: %,d VND%n",
                DateUtil.formatDate(ngayBatDau), DateUtil.formatDate(ngayKetThuc),
                tinhLoiNhuanRong(dshd, ngayBatDau, ngayKetThuc));

        hienThiDanhSachCongNo(dshd, dsMaDaThanhToan);
    }

    public void thongKe(int quy, int nam, ArrayList<HoaDon> dshd,
            ArrayList<String> dsMaDaThanhToan) {
        LocalDate ngayBatDau;
        LocalDate ngayKetThuc;

        switch (quy) {
            case 1:
                ngayBatDau = LocalDate.of(nam, 1, 1);
                ngayKetThuc = LocalDate.of(nam, 3, 31);
                break;
            case 2:
                ngayBatDau = LocalDate.of(nam, 4, 1);
                ngayKetThuc = LocalDate.of(nam, 6, 30);
                break;
            case 3:
                ngayBatDau = LocalDate.of(nam, 7, 1);
                ngayKetThuc = LocalDate.of(nam, 9, 30);
                break;
            case 4:
                ngayBatDau = LocalDate.of(nam, 10, 1);
                ngayKetThuc = LocalDate.of(nam, 12, 31);
                break;
            default:
                System.out.println("[!] Số quý đưa vào không hợp lệ (chỉ từ 1 đến 4)!");
                return;
        }

        thongKe(dshd, dsMaDaThanhToan, ngayBatDau, ngayKetThuc);
    }

    public void thongKeTheoNam(int nam, ArrayList<HoaDon> dshd,
            ArrayList<String> dsMaDaThanhToan) {
        LocalDate dauNam = LocalDate.of(nam, 1, 1);
        LocalDate cuoiNam = LocalDate.of(nam, 12, 31);
        thongKe(dshd, dsMaDaThanhToan, dauNam, cuoiNam);
    }
}
