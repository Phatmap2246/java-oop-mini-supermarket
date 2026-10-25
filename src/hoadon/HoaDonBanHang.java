package hoadon;

import java.util.Scanner;
import utils.Validation;

public class HoaDonBanHang extends HoaDon implements IThanhToan {
    private double tienKhachDua;
    private double tienThua;

    // constructor
    public HoaDonBanHang(String maHoaDon, String tenDoiTac) {
        super(maHoaDon, tenDoiTac);
    }

    public HoaDonBanHang() {
        super();
        this.tienKhachDua = 0.0;
        this.tienThua = 0.0;
    }

    // Ghi đè (HoaDon)
    @Override 
    public double tinhTongTien(){
        double tong = 0;
        for(CTHoaDon ct : danhSachChiTiet){
            tong += ct.tinhThanhTien();
        }
        return tong;
    }
   
    @Override 
    public void hienThiHoaDon(){
         // 1. IN PHẦN HEADER (Đầu hóa đơn)
        System.out.println("==========================================================");
        System.out.println("                    SIÊU THỊ MINI OOP                     ");
        System.out.println("             Đ/c: 123 Đường Số 1, Q.1, TP.HCM             ");
        System.out.println("                     ĐT: 0123.456.789                     ");
        System.out.println("==========================================================");
        System.out.println("                     HÓA ĐƠN BÁN LẺ                        ");
        System.out.println("Ngày: " + ngayLap + " \t\t\t Số: " + maHoaDon);
        System.out.println("KH:   " + tenDoiTac);
        System.out.println("----------------------------------------------------------");
        
        // 2. IN TIÊU ĐỀ BẢNG
        // %-5s: căn trái, rộng 5 ký tự
        // %25s: căn phải, rộng 25 ký tự
        System.out.printf("%-5s | %-20s | %-5s | %-10s | %-10s\n", 
                "STT", "Tên Sản Phẩm", "SL", "Đơn Giá", "T.Tiền");
        System.out.println("----------------------------------------------------------");

        // 3. IN CHI TIẾT TỪNG MÓN HÀNG (Dùng vòng lặp)
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

        // 4. IN TỔNG TIỀN VÀ THANH TOÁN
        // %43s: Đẩy chữ sang lề phải cho đẹp
        System.out.printf("%43s %12.2f\n", "Tổng số tiền thanh toán:", tinhTongTien());
        
        if (tienKhachDua > 0) {
            System.out.printf("%43s %12.2f\n", "Tiền khách đưa:", tienKhachDua);
            System.out.printf("%43s %12.2f\n", "Tiền thừa trả khách:", tienThua);
        }

        // 5. IN FOOTER (Cuối hóa đơn)
        System.out.println("==========================================================");
        System.out.println("                 HÂN HẠNH ĐƯỢC PHỤC VỤ QUÝ KHÁCH          ");
        System.out.println("==========================================================");
        
        // 6. TÍNH NĂNG SÁNG TẠO: IN MÃ QR THANH TOÁN
        // (Gọi hàm tạo QR mà chúng ta sẽ viết ở bước sau)
        // this.xuatMaQRThanhToan(); 
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
    public void nhapHoaDon(Scanner sc){
        this.maHoaDon = Validation.inputStringRegex(sc,  "Nhap ma hoa don (VD: HD001): ", "^HD\\d+$", "Loi: Ma HD phai bat dau bang HD va theo sau la so!");
        System.out.print("Nhap ten khach hang: ");
        this.tenDoiTac = sc.nextLine();
        double tienDua = Validation.inputDouble(sc, "Nhap so tien khach dua: ");
        if (!kiemTraThanhToan(tienDua)) {
            System.out.println("[!] Canh bao: Khach dua khong du tien!");
        }
    }
    
}
