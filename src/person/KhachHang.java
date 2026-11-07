package person;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

import hoadon.CTHoaDon;
import utils.DateUtil;
import utils.Validation;

public class KhachHang extends Person {

    private String maKhachHang;
    private LocalDate ngayDangKy;
    private long tongChiTieu;
    private String hangThanhVien;
    private int diemTichLuy;

    // Giỏ hàng chứa danh sách các món khách hàng muốn mua
    private ArrayList<CTHoaDon> gioHang;

    // ========================= CONSTRUCTOR =========================

    public KhachHang() {
        super();
        this.maKhachHang = "";
        this.ngayDangKy = LocalDate.now();
        this.tongChiTieu = 0;
        this.hangThanhVien = "Đồng";
        this.diemTichLuy = 0;
        this.gioHang = new ArrayList<>();
    }

    public KhachHang(String email, String hoTen, String soDienThoai, DiaChi diaChi, LocalDate ngaySinh,
            String maKhachHang, LocalDate ngayDangKy) {
        super(email, hoTen, soDienThoai, diaChi, ngaySinh);
        this.maKhachHang = maKhachHang;
        this.ngayDangKy = (ngayDangKy != null) ? ngayDangKy : LocalDate.now();
        this.tongChiTieu = 0;
        this.hangThanhVien = "Đồng";
        this.diemTichLuy = 0;
        this.gioHang = new ArrayList<>();
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
        this.maKhachHang = maKhachHang;
    }

    public LocalDate getNgayDangKy() {
        return ngayDangKy;
    }

    public void setNgayDangKy(LocalDate ngayDangKy) {
        this.ngayDangKy = ngayDangKy;
    }

    public long getTongChiTieu() {
        return tongChiTieu;
    }

    public void setTongChiTieu(long tongChiTieu) {
        this.tongChiTieu = tongChiTieu;
    }

    public String getHangThanhVien() {
        return hangThanhVien;
    }

    public void setHangThanhVien(String hangThanhVien) {
        this.hangThanhVien = hangThanhVien;
    }

    public int getDiemTichLuy() {
        return diemTichLuy;
    }

    public void setDiemTichLuy(int diemTichLuy) {
        this.diemTichLuy = diemTichLuy;
    }

    public ArrayList<CTHoaDon> getGioHang() {
        return gioHang;
    }

    public void setGioHang(ArrayList<CTHoaDon> gioHang) {
        this.gioHang = (gioHang != null) ? gioHang : new ArrayList<>();
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
        switch (hangThanhVien) {
            case "Bạc":
                return 0.03;
            case "Vàng":
                return 0.05;
            case "Kim Cương":
                return 0.10;
            case "Đồng":
            default:
                return 0.01;
        }
    }

    public void tichLuyChiTieu(long soTien) {
        this.tongChiTieu += soTien;
        this.diemTichLuy += (int) (soTien / 100_000);
        capNhatThuHang();
    }

    // ========================= CÁC PHƯƠNG THỨC GIỎ HÀNG =========================

    /**
     * Thêm một món vào giỏ hàng. Nếu món đã có (trùng mã sản phẩm), tự động cộng
     * dồn số lượng.
     */
    public void themVaoGioHang(CTHoaDon ct) {
        if (ct == null) {
            return;
        }
        for (CTHoaDon item : gioHang) {
            if (item.getMaSanPham().equalsIgnoreCase(ct.getMaSanPham())) {
                item.setSoLuong(item.getSoLuong() + ct.getSoLuong());
                System.out.printf("[+] Đã cập nhật %s: %d sản phẩm.%n",
                        ct.getTenSanPham(), item.getSoLuong());
                return;
            }
        }
        gioHang.add(ct);
        System.out.println("[+] Đã thêm " + ct.getTenSanPham() + " vào giỏ hàng.");
    }

    /**
     * Nhập thêm món hàng vào giỏ từ bàn phím qua Scanner.
     */
    public void themVaoGioHang(Scanner sc) {
        if (sc == null) {
            sc = new Scanner(System.in);
        }
        System.out.println("\n=== THÊM SẢN PHẨM VÀO GIỎ HÀNG ===");
        while (true) {
            CTHoaDon ct = new CTHoaDon();
            ct.nhapThongTin(sc, true);
            themVaoGioHang(ct);

            int tiepTuc = Validation.luaChon(
                    sc, "Thêm sản phẩm khác? (1: Có / 0: Không): ", 0, 1);
            if (tiepTuc == 0) {
                break;
            }
        }
    }

    /**
     * Xóa một sản phẩm khỏi giỏ hàng theo mã sản phẩm.
     */
    public boolean xoaKhoiGioHang(String maSP) {
        if (maSP == null || gioHang.isEmpty()) {
            return false;
        }
        for (int i = 0; i < gioHang.size(); i++) {
            if (gioHang.get(i).getMaSanPham().equalsIgnoreCase(maSP.trim())) {
                CTHoaDon removed = gioHang.remove(i);
                System.out.println("[+] Đã xóa " + removed.getTenSanPham() + " khỏi giỏ hàng.");
                return true;
            }
        }
        System.out.println("[!] Không tìm thấy sản phẩm mã " + maSP + " trong giỏ hàng.");
        return false;
    }

    /**
     * Chỉnh sửa số lượng của một món trong giỏ hàng.
     * Nếu số lượng mới <= 0, món hàng sẽ tự động bị xóa khỏi giỏ.
     */
    public boolean suaSoLuongTrongGioHang(String maSP, int soLuongMoi) {
        if (maSP == null || gioHang.isEmpty()) {
            return false;
        }
        for (int i = 0; i < gioHang.size(); i++) {
            CTHoaDon item = gioHang.get(i);
            if (item.getMaSanPham().equalsIgnoreCase(maSP.trim())) {
                if (soLuongMoi <= 0) {
                    gioHang.remove(i);
                    System.out.println("[+] Đã xóa " + item.getTenSanPham()
                            + " vì số lượng mới không lớn hơn 0.");
                } else {
                    item.setSoLuong(soLuongMoi);
                    System.out.println("[+] Đã cập nhật số lượng " + item.getTenSanPham()
                            + " thành " + soLuongMoi + ".");
                }
                return true;
            }
        }
        System.out.println("[!] Không tìm thấy sản phẩm mã " + maSP + " trong giỏ hàng.");
        return false;
    }

    /**
     * Tính tổng tiền tạm tính của tất cả các món trong giỏ hàng.
     */
    public long tinhTongTienGioHang() {
        long tongTien = 0;
        for (CTHoaDon ct : gioHang) {
            tongTien += ct.tinhThanhTien();
        }
        return tongTien;
    }

    /**
     * Hiển thị danh sách các món hiện có trong giỏ hàng kèm tổng tiền tạm tính.
     */
    public void xemGioHang() {
        System.out.println("\n=== GIỎ HÀNG: "
                + (getHoten() != null ? getHoten().toUpperCase() : "KHÁCH HÀNG") + " ===");
        if (gioHang.isEmpty()) {
            System.out.println("Giỏ hàng đang trống.");
            return;
        }

        System.out.printf("%-4s | %-10s | %-24s | %5s | %12s | %14s%n",
                "STT", "Mã SP", "Tên sản phẩm", "SL", "Đơn giá", "Thành tiền");
        System.out.println("--------------------------------------------------------------------------");

        int stt = 1;
        for (CTHoaDon ct : gioHang) {
            System.out.printf("%-4d | %-10s | %-24s | %5d | %,12d | %,14d%n",
                    stt++, ct.getMaSanPham(), ct.getTenSanPham(), ct.getSoLuong(), ct.getDonGia(), ct.tinhThanhTien());
        }
        System.out.println("--------------------------------------------------------------------------");
        System.out.printf("Tổng tạm tính: %,d VND%n", tinhTongTienGioHang());
    }

    /**
     * Làm rỗng giỏ hàng (thường dùng sau khi đã thanh toán thành công).
     */
    public void xoaRongGioHang() {
        gioHang.clear();
    }

    /**
     * Kiểm tra giỏ hàng có rỗng không.
     */
    public boolean isGioHangRong() {
        return gioHang == null || gioHang.isEmpty();
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
                + " | Món trong giỏ: " + (gioHang != null ? gioHang.size() : 0);
    }
}
