package person;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

import hoadon.HoaDon;
import hoadon.HoaDonBanHang;
import hoadon.HoaDonNhapHang;
import utils.DateUtil;
import utils.Validation;

public class KeToan extends NhanVien {
    // Mặc định kỳ hạn thanh toán công nợ với NCC là 30 ngày kể từ ngày lập hóa đơn
    private static final int KY_HAN_CONG_NO_NGAY = 30;

    public KeToan() {
        super("KeToan");
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

    // ==================== 1. NHÓM HÀM TÍNH TOÁN TÀI CHÍNH ====================

    public long tinhTongDoanhThu(ArrayList<HoaDon> dshd, LocalDate ngayBatDau, LocalDate ngayKetThuc) {
        if (dshd == null || dshd.isEmpty()) {
            return 0;
        }
        long tongDoanhThu = 0;
        for (HoaDon hd : dshd) {
            if (hd == null || hd.getNgayLap() == null) continue;
            LocalDate ngayLap = hd.getNgayLap().toLocalDate();
            if (hd instanceof HoaDonBanHang hdb
                    && hdb.isDaThanhToan()
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
            if (hd instanceof HoaDonNhapHang hdn
                    && hdn.isDuyet()
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

    // ==================== 2. NHÓM HÀM XỬ LÝ CÔNG NỢ====================

    // Lọc các hóa đơn nhập hàng đã được Quản lý duyệt từ danh sách hóa đơn chung
    public ArrayList<HoaDonNhapHang> layDanhSachHoaDonCongNo(ArrayList<HoaDon> dshd) {
        ArrayList<HoaDonNhapHang> dsCongNo = new ArrayList<>();
        if (dshd == null) return dsCongNo;

        for (HoaDon hd : dshd) {
            if (hd instanceof HoaDonNhapHang hdn && hdn.isDuyet()) {
                dsCongNo.add(hdn);
            }
        }
        return dsCongNo;
    }

    // Lọc các hóa đơn nhập công nợ chưa thanh toán
    public ArrayList<HoaDonNhapHang> layDanhSachCongNoChuaTra(ArrayList<HoaDon> dshd,
            ArrayList<String> dsMaDaThanhToan) {
        ArrayList<HoaDonNhapHang> dsChuaTra = new ArrayList<>();
        for (HoaDonNhapHang hdn : layDanhSachHoaDonCongNo(dshd)) {
            if (!kiemTraDaThanhToan(hdn, dsMaDaThanhToan)) {
                dsChuaTra.add(hdn);
            }
        }
        return dsChuaTra;
    }

    private boolean kiemTraDaThanhToan(HoaDon hd, ArrayList<String> dsMaDaThanhToan) {
        if (hd instanceof HoaDonNhapHang hdn && hdn.isDaThanhToan()) {
            return true;
        }
        return daThanhToan(hd.getMaHoaDon(), dsMaDaThanhToan);
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
        if (kiemTraDaThanhToan(hd, dsMaDaThanhToan)) {
            return "Đã thanh toán";
        }
        LocalDate han = tinhNgayDenHan(hd);
        return han.isBefore(LocalDate.now()) ? "QUÁ HẠN" : "Chưa thanh toán";
    }

    // Chỉ hiển thị danh sách công nợ
    public void hienThiDanhSachCongNo(ArrayList<HoaDon> dshd, ArrayList<String> dsMaDaThanhToan) {
        System.out.println("\n=== CÔNG NỢ NHÀ CUNG CẤP ===");
        ArrayList<HoaDonNhapHang> dsCongNo = layDanhSachHoaDonCongNo(dshd);
        if (dsCongNo.isEmpty()) {
            System.out.println(">> Danh mục công nợ trống.");
            return;
        }

        for (int i = 0; i < dsCongNo.size(); i++) {
            HoaDonNhapHang hdn = dsCongNo.get(i);
            LocalDate han = tinhNgayDenHan(hdn);
            String trangThai = layTrangThaiCongNo(hdn, dsMaDaThanhToan);

            System.out.printf("%d. HĐ: %s | Mã NCC: %s | Tiền: %,d VND | Hạn: %s | [%s]%n",
                    i + 1,
                    hdn.getMaHoaDon(),
                    hdn.getMaNhaCungCap(),
                    hdn.tinhTongTien(),
                    DateUtil.formatDate(han),
                    trangThai);
        }
    }

    private void hienThiMenuChonNoChuaTra(ArrayList<HoaDonNhapHang> dsChuaTra) {
        System.out.println("\n=== THANH TOÁN CÔNG NỢ ===");
        for (int i = 0; i < dsChuaTra.size(); i++) {
            HoaDonNhapHang hdn = dsChuaTra.get(i);
            System.out.printf("%d. HĐ: %s - NCC: %s (%,d VND - Hạn: %s)%n",
                    i + 1,
                    hdn.getMaHoaDon(),
                    hdn.getMaNhaCungCap(),
                    hdn.tinhTongTien(),
                    DateUtil.formatDate(tinhNgayDenHan(hdn)));
        }
        System.out.println("0. Quay lại");
    }

    private void thucHienChuyenKhoan(Scanner sc, HoaDonNhapHang hoaDonNo, ArrayList<String> dsMaDaThanhToan) {
        String nganHang = Validation.inputStringRegex(
                sc, "Nhập ngân hàng thụ hưởng: ", ".+", "Không được để trống!");
        String stk = Validation.inputStringRegex(
                sc, "Nhập số tài khoản: ", "\\S+", "Không được để trống!");

        System.out.printf("[+] Đã lập ủy nhiệm chi %,d VND tới %s (STK: %s) cho HĐ %s (NCC: %s).%n",
                hoaDonNo.tinhTongTien(), nganHang, stk,
                hoaDonNo.getMaHoaDon(), hoaDonNo.getMaNhaCungCap());

        hoaDonNo.setDaThanhToan(true);
        if (dsMaDaThanhToan != null && !daThanhToan(hoaDonNo.getMaHoaDon(), dsMaDaThanhToan)) {
            dsMaDaThanhToan.add(hoaDonNo.getMaHoaDon());
        }
        System.out.println(">> Cập nhật trạng thái: Đã thanh toán thành công!");
    }

    public void hienThiVaThanhToanCongNo(Scanner sc, ArrayList<HoaDon> dshd,
            ArrayList<String> dsMaDaThanhToan) {
        hienThiDanhSachCongNo(dshd, dsMaDaThanhToan);
        ArrayList<HoaDonNhapHang> dsChuaTra = layDanhSachCongNoChuaTra(dshd, dsMaDaThanhToan);
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

    // ==================== 4. HÀM GỌI TỪ GIAODIEN ====================

    public void thucHienNghiepVu(Scanner sc, ArrayList<HoaDon> dshd, ArrayList<String> dsMaDaThanhToan) {
        while (true) {
            System.out.println("\n=== NGHIỆP VỤ KẾ TOÁN ===");
            System.out.println("1. Quản lý & Thanh toán công nợ");
            System.out.println("2. Thống kê theo khoảng ngày");
            System.out.println("3. Thống kê theo Quý");
            System.out.println("4. Thống kê theo Năm");
            System.out.println("0. Thoát");

            int chon = Validation.luaChon(sc, "Chọn chức năng: ", 0, 4);
            if (chon == 0) return;
            switch (chon) {
                case 1 -> thanhToanCongNo(sc, dshd, dsMaDaThanhToan);
                case 2 -> {
                    LocalDate bd = Validation.inputLocalDateRegex(sc, "Từ ngày (dd/MM/yyyy): ");
                    LocalDate kt = Validation.inputLocalDateRegex(sc, "Đến ngày (dd/MM/yyyy): ");
                    if (kt.isBefore(bd)) { LocalDate tam = bd; bd = kt; kt = tam; }
                    thongKe(dshd, dsMaDaThanhToan, bd, kt);
                }
                case 3 -> {
                    int quy = Validation.luaChon(sc, "Nhập quý (1-4): ", 1, 4);
                    int nam = Validation.inputInt(sc, "Nhập năm: ");
                    thongKe(quy, nam, dshd, dsMaDaThanhToan);
                }
                case 4 -> {
                    int nam = Validation.inputInt(sc, "Nhập năm: ");
                    thongKeTheoNam(nam, dshd, dsMaDaThanhToan);
                }
            }
        }
    }
}