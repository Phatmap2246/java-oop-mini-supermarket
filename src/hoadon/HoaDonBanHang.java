package hoadon;

import java.util.Scanner;
import utils.Validation;

public class HoaDonBanHang extends HoaDon {
    private double tienKhuyenMai;
    
    // SỬA: Chứa đối tượng thanh toán (Đa hình), KHÔNG implements IThanhToan nữa
    private IThanhToan phuongThucThanhToan; 
    private boolean daThanhToan;

    public static final String MA_HD_BAN_REGEX = "^HD\\d+$";

    public HoaDonBanHang(String maHoaDon, String tenDoiTac) {
        super(maHoaDon, tenDoiTac);
        this.tienKhuyenMai = 0.0;
        this.phuongThucThanhToan = null;
        this.daThanhToan = false;
    }

    public HoaDonBanHang() {
        super();
        this.tienKhuyenMai = 0.0;
        this.phuongThucThanhToan = null;
        this.daThanhToan = false;
    }

    @Override 
    public double tinhTongTien(){
        double tongHang = 0;
        for(CTHoaDon ct : danhSachChiTiet){
            tongHang += ct.tinhThanhTien();
        }
        double thanhToan = tongHang - this.tienKhuyenMai;
        return (thanhToan > 0) ? thanhToan : 0; 
    }
   
    @Override 
    public void hienThiHoaDon(){
        System.out.println("\n\n==========================================================================================");
        System.out.println("                                    SIÊU THỊ MINI OOP                                     ");
        System.out.println("             Đ/c: 123 Đường Số 1, Q.1, TP.HCM                   ĐT: 0123.456.789          ");
        System.out.println("==========================================================================================");
        System.out.println("                                     HÓA ĐƠN BÁN LẺ                                       ");
        System.out.println("Ngày: " + ngayLap + " \t\t\t\t\t Số: " + maHoaDon);
        System.out.println("KH:   " + tenDoiTac);
        System.out.println("------------------------------------------------------------------------------------------");
        
        // SỬA: Tăng độ rộng cột
        System.out.printf("%-5s | %-25s | %-5s | %15s | %15s\n", 
                "STT", "Tên Sản Phẩm", "SL", "Đơn Giá", "T.Tiền");
        System.out.println("------------------------------------------------------------------------------------------");

        int stt = 1;
        double tongHang = 0;
        for (CTHoaDon ct : danhSachChiTiet) {
            System.out.printf("%-5d | %-25s | %-5d | %15.2f | %15.2f\n",
                    stt, ct.getTenSanPham(), ct.getSoLuong(), ct.getDonGia(), ct.tinhThanhTien());
            tongHang += ct.tinhThanhTien();
            stt++;
        }

        System.out.println("------------------------------------------------------------------------------------------");
        
        System.out.printf("%70s %15.2f\n", "Tổng tiền hàng:", tongHang);
        
        if (this.tienKhuyenMai > 0) {
            System.out.printf("%70s -%14.2f\n", "Khuyến mãi:", this.tienKhuyenMai);
        }
        
        System.out.printf("%70s %15.2f\n", "TỔNG THANH TOÁN:", tinhTongTien());
        
        System.out.println("==========================================================================================");
        
        if (daThanhToan && phuongThucThanhToan != null) {
            System.out.println(" TRẠNG THÁI: [X] ĐÃ THANH TOÁN");
            phuongThucThanhToan.inBienLai(); 
        } else {
            System.out.println(" TRẠNG THÁI: [ ] CHƯA THANH TOÁN");
        }
        
        System.out.println("==========================================================================================");
        System.out.println("                          HÂN HẠNH ĐƯỢC PHỤC VỤ QUÝ KHÁCH                                ");
        System.out.println("==========================================================================================");
        System.out.println(); // Thêm dòng trống để phân biệt hóa đơn
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

        this.tienKhuyenMai = Validation.inputDouble(sc, "Nhap tien khuyen mai cho hoa don (Nhap 0 neu khong co): ");
        if (this.tienKhuyenMai < 0) {
            this.tienKhuyenMai = 0;
        }

        System.out.println("--- Nhap danh sach san pham ---");
        while (true) {
            CTHoaDon ct = new CTHoaDon();
            ct.nhapThongTin(sc, true); 
            this.themChiTiet(ct);
            
            int tiepTuc = Validation.inputInt(sc, "Nhap tiep san pham? (1: Co / 0: Khong): ");
            if (tiepTuc == 0) {
                break;
            }
        }

        // SỬA: MENU CHỌN PHƯƠNG THỨC THANH TOÁN
                // SỬA: MENU CHỌN PHƯƠNG THỨC THANH TOÁN
        System.out.println("\n--- CHỌN PHƯƠNG THỨC THANH TOÁN ---");
        System.out.println("1. Tiền mặt");
        System.out.println("2. Chuyển khoản ngân hàng");
        int chon = Validation.inputInt(sc, "Lựa chọn của bạn: ");

        if (chon == 1) {
            this.phuongThucThanhToan = new ThanhToanTienMat();
        } else {
            System.out.print("Nhập tên ngân hàng: ");
            String bank = sc.nextLine();
            System.out.print("Nhập số tài khoản: ");
            String stk = sc.nextLine();
            
            ThanhToanChuyenKhoan ck = new ThanhToanChuyenKhoan(bank, stk);
            // SỬA: TẠO MÃ QR TRƯỚC KHI THANH TOÁN
            ck.taoMaQR(tinhTongTien());
            this.phuongThucThanhToan = ck;
        }

        // Gọi hàm thanh toán (Đa hình)
         this.daThanhToan = this.phuongThucThanhToan.thucHienThanhToan(sc, tinhTongTien());
        
        if (this.daThanhToan) {
            System.out.println("\n[+] Thanh toán thành công!");
            System.out.println("=================================================");
            System.out.println("         IN HÓA ĐƠN (ĐÃ THANH TOÁN)              ");
            System.out.println("=================================================");
            this.hienThiHoaDon(); // TỰ ĐỘNG IN HÓA ĐƠN LUÔN Ở ĐÂY
        } else {
            System.out.println("[!] Thanh toán thất bại (Khách đưa thiếu tiền)!");
        }
    }
    // getters - setters
    public double getTienKhuyenMai() { return tienKhuyenMai; }
    public void setTienKhuyenMai(double tienKhuyenMai) { this.tienKhuyenMai = tienKhuyenMai; }

    public IThanhToan getPhuongThucThanhToan() { return phuongThucThanhToan; }
    public void setPhuongThucThanhToan(IThanhToan phuongThucThanhToan) { this.phuongThucThanhToan = phuongThucThanhToan; }

    public boolean isDaThanhToan() { return daThanhToan; }
    public void setDaThanhToan(boolean daThanhToan) { this.daThanhToan = daThanhToan; }
}