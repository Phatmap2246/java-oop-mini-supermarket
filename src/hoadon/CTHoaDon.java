package hoadon;
import java.util.Scanner;
import utils.Validation;
public class CTHoaDon {
    private String maSanPham;
    private String tenSanPham;
    private  int soLuong;
    private  double donGia;
    private double chietKhau;

    // TODO: Tạm thời khai báo ở đây, sau này class SanPham có thể chuyển sang đó
    public static final String MA_SP_REGEX = "^SP\\d+$";
    //

    // constructor
    public CTHoaDon() {
        this.maSanPham = "";
        this.tenSanPham = "";
        this.soLuong = 0;
        this.donGia = 0.0;
        this.chietKhau =0.0;
    }

    public CTHoaDon(String maSanPham, String tenSanPham, int soLuong, double donGia, double chietKhau) {
        this.maSanPham = maSanPham;
        this.tenSanPham = tenSanPham;
        this.soLuong = soLuong;
        this.donGia = donGia;
        this.chietKhau = chietKhau;
    }

    // methods
    public void nhapThongTin(Scanner sc, boolean isBanHang){
        this.maSanPham = Validation.inputStringRegex(sc, "Nhập mã sản phẩm (VD: SP01): ", MA_SP_REGEX, "Lỗi: Mã SP phải bắt đầu bằng SP in hoa và theo sau là số!");
        System.out.print("Nhập tên sản phẩm: ");
        this.tenSanPham = sc.nextLine();
        this.soLuong = Validation.inputInt(sc, "Nhập số lượng: ");
        this.donGia = Validation.inputDouble(sc, "Nhập đơn giá: ");
        
        // Hỏi xem có phải đang nhập hóa đơn bán hàng không
        if (isBanHang) {
            this.chietKhau = Validation.inputDouble(sc, "Nhập % chiết khấu sản phẩm (Nhập 0 nếu không có): ");
            if (this.chietKhau < 0 || this.chietKhau > 100) {
                System.out.println("[!] Chiết khấu không hợp lệ, đặt về 0%.");
                this.chietKhau = 0;
            }
        } else {
            this.chietKhau = 0; // Nhập hàng mặc định không chiết khấu lẻ
        }
    }

    public double tinhThanhTien(){
        double tienGoc = this.soLuong * this.donGia;
        double tienGiam = tienGoc * (this.chietKhau / 100.0);
        return tienGoc - tienGiam;
    }

    public void hienThiChiTiet(){
        String ck = (chietKhau > 0) ? String.format(" (CK: %.0f%%)", chietKhau) : "";
        System.out.printf("  - Mã SP: %-10s | Tên: %-20s | SL: %-5d | Đơn giá: %-10.2f | Thành tiền: %.2f%s\n", 
                maSanPham, tenSanPham, soLuong, donGia, tinhThanhTien(), ck);
    }

    // getters - setters
    public String getMaSanPham() {
        return maSanPham;
    }

    public void setMaSanPham(String maSanPham) {
        this.maSanPham = maSanPham;
    }

    public String getTenSanPham() {
        return tenSanPham;
    }

    public void setTenSanPham(String tenSanPham) {
        this.tenSanPham = tenSanPham;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public double getDonGia() {
        return donGia;
    }

    public void setDonGia(double donGia) {
        this.donGia = donGia;
    }
}
