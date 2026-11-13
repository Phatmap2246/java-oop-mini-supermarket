package person;

import java.time.LocalDate;
import java.util.Scanner;

import utils.DateUtil;
import utils.Validation;

public class KhachHang extends Person {

    private String maKhachHang;
    private LocalDate ngayDangKy;
    private long tongChiTieu;
    private String hangThanhVien;
    private int diemTichLuy;
    private boolean isTonTai;
    private static int soThuTu = 1 ;

    // ========================= CONSTRUCTOR =========================

    public KhachHang() {
        super();
        this.maKhachHang = Validation.taoMa("KH%d03" , soThuTu) ;
        this.ngayDangKy = LocalDate.now();
        this.tongChiTieu = 0;
        this.hangThanhVien = "Đồng";
        this.diemTichLuy = 0;
        this.isTonTai = true;
        soThuTu++ ;
    }
    // Cần lưu số thứ tự mỗi khi đọc file
    public KhachHang(String email, String hoTen, String soDienThoai, DiaChi diaChi, LocalDate ngaySinh,
            String maKhachHang, LocalDate ngayDangKy) {
        super(email, hoTen, soDienThoai, diaChi, ngaySinh);
        this.maKhachHang = maKhachHang;
        this.ngayDangKy = (ngayDangKy != null) ? ngayDangKy : LocalDate.now();
        this.tongChiTieu = 0;
        this.hangThanhVien = "Đồng";
        this.diemTichLuy = 0;
        this.isTonTai = true;
    }

    public KhachHang(String email, String hoTen, String soDienThoai, DiaChi diaChi, LocalDate ngaySinh,
            String maKhachHang, LocalDate ngayDangKy, long tongChiTieu, String hangThanhVien, int diemTichLuy,
            boolean isTonTai) {
        super(email, hoTen, soDienThoai, diaChi, ngaySinh);
        this.maKhachHang = maKhachHang;
        this.ngayDangKy = (ngayDangKy != null) ? ngayDangKy : LocalDate.now();
        this.tongChiTieu = tongChiTieu;
        this.hangThanhVien = hangThanhVien;
        this.diemTichLuy = diemTichLuy;
        this.isTonTai = isTonTai;
    }

    // ========================= KẾ THỪA PERSON =========================

    @Override
    public String getVaiTro() {
        return "Khách hàng";
    }

    // ========================= VALIDATION MÃ KH =========================

    public static boolean isValidMaKhachHang(String maKh) {
        return maKh != null && maKh.matches(Validation.MaKH_REGEX);
    }

    // ========================= GETTER / SETTER =========================

    public String getMaKhachHang() {
        return maKhachHang;
    }

    public void setMaKhachHang(String maKhachHang) {
        if (maKhachHang == null || !maKhachHang.matches(Validation.MaKH_REGEX)) {
            throw new IllegalArgumentException("MaKhachHang không đúng định dạng.");
        }
        this.maKhachHang = maKhachHang;
    }

    public LocalDate getNgayDangKy() {
        return ngayDangKy;
    }

    public void setNgayDangKy(LocalDate ngayDangKy) {
        if (ngayDangKy == null) {
            throw new IllegalArgumentException("ngayDangKy không được null.");
        }
        this.ngayDangKy = ngayDangKy;
    }

    public long getTongChiTieu() {
        return tongChiTieu;
    }

    public void setTongChiTieu(long tongChiTieu) {
        if (tongChiTieu < 0) {
            throw new IllegalArgumentException("TongChiTieu không được âm.");
        }
        this.tongChiTieu = tongChiTieu;
    }

    public String getHangThanhVien() {
        return hangThanhVien;
    }

    public void setHangThanhVien(String hangThanhVien) {
        if (hangThanhVien == null || hangThanhVien.isBlank()) {
            throw new IllegalArgumentException("HangThanhVien không được để trống.");
        }
        this.hangThanhVien = hangThanhVien;
    }

    public int getDiemTichLuy() {
        return diemTichLuy;
    }

    public void setDiemTichLuy(int diemTichLuy) {
        if (diemTichLuy < 0) {
            throw new IllegalArgumentException("DiemTichLuy không được âm.");
        }
        this.diemTichLuy = diemTichLuy;
    }

    public boolean isTonTai() {
        return isTonTai;
    }

    public void setTonTai(boolean isTonTai) {
        this.isTonTai = isTonTai;
    }


    public static int getSoThuTu() {
        return soThuTu;
    }
    // ========================= NGHIỆP VỤ THÀNH VIÊN =========================

    public void capNhatThuHang() {
        if (tongChiTieu >= 10_000_000) {
            hangThanhVien = "Kim Cương";
        } else if (tongChiTieu >= 5_000_000) {
            hangThanhVien = "Vàng";
        } else if (tongChiTieu >= 1_000_000) {
            hangThanhVien = "Bạc";
        } else {
            hangThanhVien = "Đồng";
        }
    }

    public double tinhChietKhau() {
        return switch (hangThanhVien == null ? "" : hangThanhVien) {
            case "Bạc" -> 0.03;
            case "Vàng" -> 0.05;
            case "Kim Cương" -> 0.10;
            default -> 0.01;
        };
    }

    public void tichLuyChiTieu(long soTien) {
        this.tongChiTieu += soTien;
        this.diemTichLuy += (int) (soTien / 100_000);
        capNhatThuHang();
    }

    // ========================= NHẬP THÔNG TIN KHÁCH HÀNG =========================

    @Override
    public void nhapThongTin(Scanner sc) {
        if (sc == null)
            sc = new Scanner(System.in);

        this.maKhachHang = Validation.inputStringRegex(
                sc,
                "Nhập mã khách hàng (VD: KH001): ",
                Validation.MaKH_REGEX,
                "Mã KH không hợp lệ (phải bắt đầu bằng KH kèm 3-5 chữ số). Vui lòng nhập lại!");

        super.nhapThongTin(sc);

        this.ngayDangKy = Validation.inputLocalDateRegex(
                sc,
                "Nhập ngày đăng ký thành viên (dd/MM/yyyy): ");
        this.isTonTai = true;
    }

    // ========================= TOSTRING =========================

    @Override
    public String toString() {
        String ngayDkStr = DateUtil.formatDate(ngayDangKy);
        return "Mã KH: " + maKhachHang
                + " | " + super.toString()
                + " | Ngày ĐK: " + (ngayDkStr.isEmpty() ? "N/A" : ngayDkStr)
                + " | Hạng: " + hangThanhVien
                + " | Điểm tích lũy: " + diemTichLuy
                + " | Tổng chi tiêu: " + String.format("%,d", tongChiTieu) + " VNĐ"
                + " | Trạng thái: " + (isTonTai ? "Đang hoạt động" : "Đã xóa");
    }
}
