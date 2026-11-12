package hoadon;
import java.util.Scanner;
import java.util.List;
import dichvu.DanhSachSPKM;
import dichvu.Danhsachkhuyenmai;
import interfaces.IThanhToan;
import sanpham.QuanLyTonKho;
import sanpham.SanPham;
import utils.Validation;

public class HoaDonBanHang extends HoaDon {
    private IThanhToan phuongThucThanhToan; 
    private boolean daThanhToan;

    public HoaDonBanHang(String maHoaDon, String sdtDoiTac) {
        super(maHoaDon, sdtDoiTac);
        this.phuongThucThanhToan = null;
        this.daThanhToan = false;
    }

    public HoaDonBanHang() {
        super();
        this.phuongThucThanhToan = null;
        this.daThanhToan = false;
    }

    @Override 
    public long tinhTongTien(){
        long tongTienHang = 0;
        for(CTHoaDonBan ct : danhSachChiTiet){
            tongTienHang += ct.getThanhTienSauKhuyenMai();
        }
        return tongTienHang;
    }
   
    @Override 
    public void hienThiHoaDon(){
        System.out.println("\n\n==========================================================================================");
        System.out.println("                                    SIÊU THỊ MINI OOP                                     ");
        System.out.println("             Đ/c: 273 Đường ADV, P.Chợ Quán, TP.HCM                   ĐT: 0123.456.789          ");
        System.out.println("==========================================================================================");
        System.out.println("                                     HÓA ĐƠN BÁN LẺ                                       ");
        System.out.println("Ngày: " + ngayLap + " \t\t\t\t\t Số: " + maHoaDon);
        System.out.println("Số điện thoại khách hàng:   " + sdtDoiTac);
        System.out.println("------------------------------------------------------------------------------------------");
        System.out.printf("%-5s | %-25s | %-5s | %15s | %15s| %15s\n", 
                "STT", "Tên Sản Phẩm", "SL", "Đơn Giá","Số tiền giảm" ,"T.Tiền");
        System.out.println("------------------------------------------------------------------------------------------");
        int stt = 1;
        for (CTHoaDonBan ct : danhSachChiTiet) {
            String tenHienThi = ct.getTenSanPham();
            if (ct.isHangKhuyenMai()) {
                tenHienThi = tenHienThi+" (Khuyến mãi - giảm) "+ ct.getSoTienGiam();
                System.out.printf("%-5d | %-25s | %-5d | %15d | %15d| %15d\n",
                    stt, tenHienThi, ct.getSoLuong(), ct.getDonGia(), ct.getSoTienGiam(),ct.getThanhTienSauKhuyenMai());
            }
            else if (ct.isKemHangChoTang()) {
                tenHienThi = tenHienThi+" (Mua tặng) "+ct.getSoLuongTangKem(); 
                System.out.printf("%-5d | %-25s | %-5d | %15d | %15d| %15d\n",
                    stt, tenHienThi, ct.getSoLuong(), ct.getDonGia(), ct.getSoLuongTangKem(),ct.getThanhTienSauKhuyenMai());
            }
            else{
                System.out.printf("%-5s | %-25s | %-5s | %15s | %15s\n", 
                "STT", "Tên Sản Phẩm", "SL", "Đơn Giá","T.Tiền");
                System.out.println("------------------------------------------------------------------------------------------");
                System.out.printf("%-5d | %-25s | %-5d | %15d | %15d| %15d\n",
                    stt, tenHienThi, ct.getSoLuong(), ct.getDonGia(),ct.getThanhTienSauKhuyenMai());
            }
            stt++;
        }
        System.out.println("------------------------------------------------------------------------------------------");        
        System.out.printf("%70s %15d\n", "TỔNG THANH TOÁN:", tinhTongTien());
        System.out.println("==========================================================================================");
        if (daThanhToan) {
            System.out.println(" TRẠNG THÁI: [X] ĐÃ THANH TOÁN");
            if (phuongThucThanhToan != null) {
                phuongThucThanhToan.inBienLai();
            }
        } else {
            System.out.println(" TRẠNG THÁI: [ ] CHƯA THANH TOÁN");
        }
        
        System.out.println("==========================================================================================");
        System.out.println("                          HÂN HẠNH ĐƯỢC PHỤC VỤ QUÝ KHÁCH                                ");
        System.out.println("==========================================================================================");
        System.out.println(); 
    }

    // Hàm nhập dùng Validation
    public boolean nhapHoaDon(Scanner sc, DanhSachSPKM danhSachSPKM, Danhsachkhuyenmai danhSachKhuyenMai, List<SanPham> danhSachSanPham) {
        if (danhSachSanPham == null || danhSachSanPham.isEmpty()) {
            System.out.println("[!] Chưa có sản phẩm trong danh mục/tồn kho để lập hóa đơn bán.");
            return false;
        }    
        this.sdtDoiTac = Validation.inputStringRegex(sc, 
            "Nhap số điện thoại khách hàng: ", 
            Validation.PHONE_REGEX, 
            "Loi: số điện thoại khach hang khong hop le!");

        System.out.println("--- Nhap danh sach san pham ---");
        while (true) {
            CTHoaDonBan ct = new CTHoaDonBan();
            ct.nhapThongTinBan(sc, danhSachSanPham,danhSachSPKM,danhSachKhuyenMai);
            this.themChiTiet(ct);
            
            int tiepTuc = Validation.luaChon(sc, "Nhap tiep san pham? (1: Co / 0: Khong): ",0,1);
            if (tiepTuc == 0) {
                break;
            }
        }
        if (!QuanLyTonKho.kiemTraDuTonKho(danhSachSanPham, danhSachChiTiet)) {
            return false;
        }
        System.out.println("\n--- CHỌN PHƯƠNG THỨC THANH TOÁN ---");
        System.out.println("1. Tiền mặt");
        System.out.println("2. Chuyển khoản ngân hàng");
        int chon = Validation.luaChon(sc, "Lựa chọn của bạn: ", 1, 2);

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
        return this.daThanhToan;
    }
    // getters - setters
    public IThanhToan getPhuongThucThanhToan() { return phuongThucThanhToan; }
    public void setPhuongThucThanhToan(IThanhToan phuongThucThanhToan) { this.phuongThucThanhToan = phuongThucThanhToan; }

    public boolean isDaThanhToan() { return daThanhToan; }
    public void setDaThanhToan(boolean daThanhToan) { this.daThanhToan = daThanhToan; }
}