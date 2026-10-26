package hoadon;

import java.util.Scanner;
import utils.Validation;

public class HoaDonBanHang extends HoaDon implements IThanhToan {
    private double tienKhachDua;
    private double tienThua;
    private double tienKhuyenMai; // Tiền giảm giá cho toàn hóa đơn

    // Mã hóa đơn bán: Bắt đầu bằng chữ "HD" (in hoa) và theo sau là các chữ số. VD: HD001
    public static final String MA_HD_BAN_REGEX = "^HD\\d+$";

    // constructor
    public HoaDonBanHang(String maHoaDon, String tenDoiTac) {
        super(maHoaDon, tenDoiTac);
        this.tienKhachDua = 0.0;
        this.tienThua = 0.0;
        this.tienKhuyenMai = 0.0;
    }

    public HoaDonBanHang() {
        super();
        this.tienKhachDua = 0.0;
        this.tienThua = 0.0;
        this.tienKhuyenMai = 0.0;
    }

    // Ghi đè (HoaDon)
    @Override 
    public double tinhTongTien(){
        double tongHang = 0;
        for(CTHoaDon ct : danhSachChiTiet){
            tongHang += ct.tinhThanhTien();
        }
        // SỬA: Trừ đi tiền khuyến mãi. Không để tổng tiền bị âm.
        double thanhToan = tongHang - this.tienKhuyenMai;
        return (thanhToan > 0) ? thanhToan : 0; 
    }
   
    @Override 
    public void hienThiHoaDon(){
        System.out.println("==========================================================");
        System.out.println("                    SIÊU THỊ MINI OOP                     ");
        System.out.println("             Đ/c: 123 Đường Số 1, Q.1, TP.HCM             ");
        System.out.println("                     ĐT: 0123.456.789                     ");
        System.out.println("==========================================================");
        System.out.println("                     HÓA ĐƠN BÁN LẺ                        ");
        System.out.println("Ngày: " + ngayLap + " \t\t\t Số: " + maHoaDon);
        System.out.println("KH:   " + tenDoiTac);
        System.out.println("----------------------------------------------------------");
        
        System.out.printf("%-5s | %-20s | %-5s | %-10s | %-10s\n", 
                "STT", "Tên Sản Phẩm", "SL", "Đơn Giá", "T.Tiền");
        System.out.println("----------------------------------------------------------");

        int stt = 1;
        double tongHang = 0;
        for (CTHoaDon ct : danhSachChiTiet) {
            System.out.printf("%-5d | %-20s | %-5d | %-10.2f | %-10.2f\n",
                    stt, ct.getTenSanPham(), ct.getSoLuong(), ct.getDonGia(), ct.tinhThanhTien());
            tongHang += ct.tinhThanhTien();
            stt++;
        }

        System.out.println("----------------------------------------------------------");
        System.out.printf("%43s %12.2f\n", "Tổng tiền hàng:", tongHang);
        
        // SỬA: In khuyến mãi nếu có
        if (this.tienKhuyenMai > 0) {
            System.out.printf("%43s -%11.2f\n", "Khuyến mãi:", this.tienKhuyenMai);
        }
        
        System.out.printf("%43s %12.2f\n", "TỔNG THANH TOÁN:", tinhTongTien());
        
        if (tienKhachDua > 0) {
            System.out.printf("%43s %12.2f\n", "Tiền khách đưa:", tienKhachDua);
            System.out.printf("%43s %12.2f\n", "Tiền thừa trả khách:", tienThua);
        }

        System.out.println("==========================================================");
        System.out.println("                 HÂN HẠNH ĐƯỢC PHỤC VỤ QUÝ KHÁCH          ");
        System.out.println("==========================================================");
        System.out.println("[Hệ thống: Đang tạo mã QR thanh toán...]");
    }
    
    // Ghi đè (Interface)
    @Override 
    public boolean kiemTraThanhToan(double soTienKhachDua){
        this.tienKhachDua = soTienKhachDua;
        double tongTien = tinhTongTien();
        if (this.tienKhachDua >= tongTien) {
            this.tienThua = this.tienKhachDua - tongTien;
            return true;
        }
        return false;
    }

    @Override 
    public void inBienLai(){
        System.out.println("\n**************************************************");
        System.out.println("*               BIÊN LAI THANH TOÁN              *");
        System.out.println("**************************************************");
        System.out.println(" Mã giao dịch: " + maHoaDon);
        System.out.println(" Khách hàng:   " + tenDoiTac);
        System.out.println(" Thời gian:    " + java.time.LocalDateTime.now());
        System.out.println("--------------------------------------------------");
        System.out.printf(" Tổng tiền:    %15.2f VND\n", tinhTongTien());
        System.out.printf(" Khách đưa:    %15.2f VND\n", tienKhachDua);
        System.out.printf(" Tiền thừa:    %15.2f VND\n", tienThua);
        System.out.println("--------------------------------------------------");
        System.out.println(" Trạng thái:   [X] ĐÃ THANH TOÁN THÀNH CÔNG");
        System.out.println("**************************************************");
        System.out.println("        Cảm ơn quý khách và hẹn gặp lại!          ");
    }

    // Hàm nhập dùng Validation
    public void nhapHoaDon(Scanner sc) {
        this.maHoaDon = Validation.inputStringRegex(sc,  
            "Nhap ma hoa don (VD: HD001): ", 
            MA_HD_BAN_REGEX, 
            "Loi: Ma HD phai bat dau bang HD va theo sau la so!");
            
        this.tenDoiTac = Validation.inputStringRegex(sc, 
            "Nhap ten khach hang: ", 
            Validation.HOTEN_REGEX, 
            "Loi: Ten khach hang khong hop le!");

        // SỬA: Hỏi nhập khuyến mãi hóa đơn
        this.tienKhuyenMai = Validation.inputDouble(sc, "Nhap tien khuyen mai cho hoa don (Nhap 0 neu khong co): ");
        if (this.tienKhuyenMai < 0) {
            this.tienKhuyenMai = 0;
        }

        System.out.println("--- Nhap danh sach san pham ---");
        while (true) {
            CTHoaDon ct = new CTHoaDon();
            ct.nhapThongTin(sc, true); // true: Hỏi chiết khấu từng sản phẩm
            this.themChiTiet(ct);
            
            int tiepTuc = Validation.inputInt(sc, "Nhap tiep san pham? (1: Co / 0: Khong): ");
            if (tiepTuc == 0) {
                break;
            }
        }

        double tienDua = Validation.inputDouble(sc, "Nhap so tien khach dua: ");
        
        if (!kiemTraThanhToan(tienDua)) {
            System.out.println("[!] Canh bao: Khach dua khong du tien!");
        } else {
            System.out.println("[+] Khach da thanh toan du tien.");
        }
    }

    // getter - setter
    public double getTienKhachDua() { return tienKhachDua; }
    public void setTienKhachDua(double tienKhachDua) { this.tienKhachDua = tienKhachDua; }

    public double getTienThua() { return tienThua; }
    public void setTienThua(double tienThua) { this.tienThua = tienThua; }

    // SỬA: Thêm Getter/Setter cho Khuyến mãi
    public double getTienKhuyenMai() { return tienKhuyenMai; }
    public void setTienKhuyenMai(double tienKhuyenMai) { this.tienKhuyenMai = tienKhuyenMai; }

    public static String getMaHdBanRegex() { return MA_HD_BAN_REGEX; }
}