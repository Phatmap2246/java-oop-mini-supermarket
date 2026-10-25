package hoadon;

import java.util.Scanner;
import utils.Validation;

public class HoaDonNhapHang extends HoaDon{
    private String maNhaCungCap;
    private double thueVAT; // Lưu ý VAT lưu dạng 67 (67%)

    // Constructors
    public HoaDonNhapHang() {
        super();
        this.maNhaCungCap = "";
        this.thueVAT = 0.0;
    }

    public HoaDonNhapHang(String maHoaDon, String tenNhaCungCap, String maNhaCungCap, double thueVAT) {
        super(maHoaDon, tenNhaCungCap);
        this.thueVAT = thueVAT;
    }

    // Override
    @Override 
    public double tinhTongTien(){
        double tongTienHang = 0;
        for(CTHoaDon ct : danhSachChiTiet){
            tongTienHang += ct.tinhThanhTien();
        }
        return tongTienHang + (tongTienHang*thueVAT)/100; 
    }

    @Override
    public void hienThiHoaDon() {
        System.out.println("\n==========================================================");
        System.out.println("                   HÓA ĐƠN NHẬP HÀNG                      ");
        System.out.println("==========================================================");
        System.out.println("Mã HĐ: " + maHoaDon + " | Ngày nhập: " + ngayLap);
        System.out.println("Nhà cung cấp: " + tenDoiTac + " (Mã: " + maNhaCungCap + ")");
        System.out.println("Thuế VAT áp dụng: " + thueVAT + "%");
        System.out.println("----------------------------------------------------------");
        
        // In tiêu đề bảng
        System.out.printf("%-5s | %-20s | %-5s | %-10s | %-10s\n", 
                "STT", "Tên Hàng Hóa", "SL", "Giá Nhập", "T.Tiền");
        System.out.println("----------------------------------------------------------");

        // In chi tiết
        int stt = 1;
        for (CTHoaDon ct : danhSachChiTiet) {
            System.out.printf("%-5d | %-20s | %-5d | %-10.2f | %-10.2f\n",
                    stt, 
                    ct.getTenSanPham(), 
                    ct.getSoLuong(), 
                    ct.getDonGia(), 
                    ct.tinhThanhTien());
            stt++;
        }

        System.out.println("----------------------------------------------------------");
        
        // Tính toán để in ra cho đẹp
        double tienHang = 0;
        for (CTHoaDon ct : danhSachChiTiet) tienHang += ct.tinhThanhTien();
        double tienThue = tienHang * thueVAT / 100.0;

        System.out.printf("%43s %12.2f\n", "Tiền hàng:", tienHang);
        System.out.printf("%43s %12.2f\n", "Tiền thuế VAT:", tienThue);
        System.out.printf("%43s %12.2f\n", "TỔNG CỘNG PHẢI TRẢ:", tinhTongTien());
        
        System.out.println("==========================================================");
    }

    // Nhập hóa đơn nhập hàng
    public void nhapHoaDon(Scanner sc){
        this.maHoaDon = Validation.inputStringRegex(sc,  "Nhập mã hóa đơn nhập (VD: HDN001): ", "^HDN\\d+$", "Lỗi: Mã HĐ nhập phải bắt đầu bằng HDN và theo sau là số!");
        this.thueVAT = Validation.inputDouble(sc, "Nhập thuế VAT (%): ");
    }

    //getters - setters
    public String getMaNhaCungCap() {
        return maNhaCungCap;
    }

    public void setMaNhaCungCap(String maNhaCungCap) {
        this.maNhaCungCap = maNhaCungCap;
    }

    public double getThueVAT() {
        return thueVAT;
    }

    public void setThueVAT(double thueVAT) {
        this.thueVAT = thueVAT;
    }

    
}
