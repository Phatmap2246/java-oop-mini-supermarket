package hoadon;
import java.util.Scanner;
import utils.Validation;
public class CTHoaDon {
    private String maSanPham;
    private String tenSanPham;
    private  int soLuong;
    private  double donGia;

    // constructor
    public CTHoaDon() {
        this.maSanPham = "";
        this.tenSanPham = "";
        this.soLuong = 0;
        this.donGia = 0.0;
    }

    public CTHoaDon(String maSanPham, String tenSanPham, int soLuong, double donGia) {
        this.maSanPham = maSanPham;
        this.tenSanPham = tenSanPham;
        this.soLuong = soLuong;
        this.donGia = donGia;
    }

    // methods
    public void nhapThongTin(Scanner sc){
        this.maSanPham = Validation.inputStringRegex(sc, "Nhập mã sản phẩm (VD: SP01): ", "^[A-Z]{2}\\d+$", "Lỗi: Mã SP phải bắt đầu bằng 2 chữ in hoa và theo sau là số!");
        System.out.print("Nhập tên sản phẩm: ");
        this.tenSanPham = sc.nextLine();
        this.soLuong = Validation.inputInt(sc, "Nhập số lượng: ");
        this.donGia = Validation.inputDouble(sc, "Nhập đơn giá: ");
    }
    public double tinhThanhTien(){
        return this.soLuong * this.donGia;
    }

    public void hienThiChiTiet(){
        System.out.printf("  - Mã SP: %-10s | Tên: %-20s | SL: %-5d | Đơn giá: %-10.2f | Thành tiền: %.2f\n", 
                maSanPham, tenSanPham, soLuong, donGia, tinhThanhTien());
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
