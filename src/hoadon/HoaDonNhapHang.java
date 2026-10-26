package hoadon;

import java.util.Scanner;
import utils.Validation;

public class HoaDonNhapHang extends HoaDon {
    private String maNhaCungCap;
    private double chiPhiVanChuyen;
    private double thueVAT; // Lưu ý VAT lưu dạng 10 (10%)

    // Mã hóa đơn nhập: Bắt đầu bằng chữ "HDN" (in hoa) và theo sau là các chữ số. VD: HDN001
    public static final String MA_HD_NHAP_REGEX = "^HDN\\d+$";
    
    // Mã nhà cung cấp: Bắt đầu bằng chữ "NCC" (in hoa) và theo sau là các chữ số. VD: NCC01
    public static final String MA_NCC_REGEX = "^NCC\\d+$";    

    // Constructors
    public HoaDonNhapHang() {
        super();
        this.maNhaCungCap = "";
        this.thueVAT = 0.0;
        this.chiPhiVanChuyen = 0.0;
    }

    public HoaDonNhapHang(String maHoaDon, String tenNhaCungCap, String maNhaCungCap, double chiPhiVanChuyen, double thueVAT) {
        super(maHoaDon, tenNhaCungCap);
        this.maNhaCungCap = maNhaCungCap; // ĐÃ THÊM DÒNG NÀY
        this.chiPhiVanChuyen = chiPhiVanChuyen;
        this.thueVAT = thueVAT;
    }

    // Override
    @Override 
    public double tinhTongTien(){
        double tongTienHang = 0;
        for(CTHoaDon ct : danhSachChiTiet){
            tongTienHang += ct.tinhThanhTien();
        }
        double tienThue = (tongTienHang * thueVAT) / 100; 
        // ĐÃ SỬA: Cộng thêm chi phí vận chuyển
        return tongTienHang + tienThue + chiPhiVanChuyen; 
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
                    stt, ct.getTenSanPham(), ct.getSoLuong(), ct.getDonGia(), ct.tinhThanhTien());
            stt++;
        }

        System.out.println("----------------------------------------------------------");
        
        // Tính toán để in ra cho đẹp
        double tienHang = 0;
        for (CTHoaDon ct : danhSachChiTiet) {
            tienHang += ct.tinhThanhTien();
        }
        double tienThue = tienHang * thueVAT / 100.0;

        System.out.printf("%43s %12.2f\n", "Tiền hàng:", tienHang);
        System.out.printf("%43s %12.2f\n", "Tiền thuế VAT:", tienThue);
        // ĐÃ THÊM: In chi phí vận chuyển
        System.out.printf("%43s %12.2f\n", "Phí vận chuyển:", chiPhiVanChuyen); 
        System.out.printf("%43s %12.2f\n", "TỔNG CỘNG PHẢI TRẢ:", tinhTongTien());
        
        System.out.println("==========================================================");
    }

    // Nhập hóa đơn nhập hàng
    public void nhapHoaDon(Scanner sc){
        this.maHoaDon = Validation.inputStringRegex(sc, 
            "Nhập mã hóa đơn nhập (VD: HDN001): ", 
            MA_HD_NHAP_REGEX, 
            "Lỗi: Mã HĐ nhập phải bắt đầu bằng HDN và theo sau là số!");
            
        this.tenDoiTac = Validation.inputStringRegex(sc, 
            "Nhập tên nhà cung cấp: ", 
            Validation.HOTEN_REGEX, 
            "Lỗi: Tên nhà cung cấp không hợp lệ!");

        this.maNhaCungCap = Validation.inputStringRegex(sc, 
            "Nhập mã nhà cung cấp (VD: NCC01): ", 
            MA_NCC_REGEX, 
            "Lỗi: Mã NCC phải bắt đầu bằng NCC và theo sau là số!");
            
        this.thueVAT = Validation.inputDouble(sc, "Nhập thuế VAT (%): ");
        this.chiPhiVanChuyen = Validation.inputDouble(sc, "Nhập chi phí vận chuyển (VND): ");

        System.out.println("--- Nhập danh sách sản phẩm nhập ---");
        while (true) {
            CTHoaDon ct = new CTHoaDon();
            // Truyền false vì đây là nhập hàng, không hỏi chiết khấu sản phẩm
            ct.nhapThongTin(sc, false); 
            this.themChiTiet(ct); 
            
            int tiepTuc = Validation.inputInt(sc, "Nhập tiếp sản phẩm? (1: Có / 0: Không): ");
            if (tiepTuc == 0) {
                break;
            }
        }
    }

    // getters - setters
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

// Getter/Setter cho chi phí vận chuyển
    public double getChiPhiVanChuyen() {
        return chiPhiVanChuyen;
    }

    public void setChiPhiVanChuyen(double chiPhiVanChuyen) {
        this.chiPhiVanChuyen = chiPhiVanChuyen;
    }
}