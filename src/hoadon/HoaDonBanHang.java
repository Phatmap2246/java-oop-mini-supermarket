package hoadon;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.List;
import java.util.function.Predicate;
import dichvu.DanhSachSPKM;
import dichvu.Danhsachkhuyenmai;
import dichvu.KhuyenMaiService;
import sanpham.QuanLySanPham;
import sanpham.QuanLyTonKho;
import sanpham.SanPham;
import utils.Validation;

public class HoaDonBanHang extends HoaDon {
    // SỬA: Chứa đối tượng thanh toán (Đa hình), KHÔNG implements IThanhToan nữa
    private ArrayList<CTHoaDonBan> ctHoaDonBans ;
    private String maKhachHang ;
    private IThanhToan phuongThucThanhToan; 
    private boolean daThanhToan;

    public static final String MA_HD_BAN_REGEX = "^HD\\d+$";
    public HoaDonBanHang(String maHoaDon , String maNhanvien , LocalDate ngayLap , String maKhachHang , boolean daThanhToan) {
        super(maHoaDon, maNhanvien , ngayLap , HoaDon.getDemHoaDon() );
        this.phuongThucThanhToan = null;
        this.maKhachHang = maKhachHang ;
        this.daThanhToan = daThanhToan;
    }

    public HoaDonBanHang() {
        super();
        this.phuongThucThanhToan = null;
        this.daThanhToan = false;
        maKhachHang = "" ;
    }

    @Override
    public long tinhTongTien(){
        long tongHang = 0;
        for(CTHoaDonBan ctHoaDonBan: ctHoaDonBans ){
            tongHang += ctHoaDonBan.tinhThanhTien() + ctHoaDonBan.getMucGiamGia() ;
        }
        return tongHang;
    }
    public long thanhTienHoaDon()
    {
        long thanhTien = 0 ;
        for ( CTHoaDonBan ct: ctHoaDonBans )
            thanhTien = ct.tinhThanhTien() ;
        return thanhTien ;
    }
    @Override 
    public void hienThiHoaDon(){
        System.out.println("Thông tin hoá đơn!");
        System.out.println(toString());
        for ( CTHoaDonBan ct:ctHoaDonBans )
        {
            System.out.println(ct);
        }
        System.out.println("Tổng tiền thanh toán " + tinhTongTien() + " | Thành tiền: " + thanhTienHoaDon() + " | Bạn đã tiết kiệm được " + (tinhTongTien()-thanhTienHoaDon()) + "VNĐ");
        System.out.println("Phương thức thanh toán: " + phuongThucThanhToan.getTenPhuongThuc() + " | Trạng thái: " + ((daThanhToan)?"Đã thanh toán":"Chưa thanh toán"));
    }

    public boolean nhapHoaDon(Scanner sc, DanhSachSPKM danhSachSPKM,
            Danhsachkhuyenmai danhSachKhuyenMai ,
            List<SanPham> danhSachSanPham) {
        if (danhSachSanPham == null || danhSachSanPham.isEmpty()) {
            System.out.println("[!] Chưa có sản phẩm trong danh mục/tồn kho để lập hóa đơn bán.");
            return false;
        }
            
        this.maKhachHang = Validation.inputStringRegex(sc,
            "Nhập mã khách hàng: ",
            "KH\\03d",
            "Loi: mã khách hàng không hợp lệ vui lòng nhập lại(VD:KH001)!"); // thiếu ktra mã sản phẩm có tồn tại k

        System.out.println("--- Nhap danh sach san pham ---");
        while (true) {
            CTHoaDon ct = new CTHoaDonBan();
            ct.nhapThongTinBan(sc, danhSachSanPham, danhSachChiTiet);
            this.themChiTiet(ct);
            
            int tiepTuc = Validation.inputInt(sc, "Nhap tiep san pham? (1: Co / 0: Khong): ");
            if (tiepTuc == 0) {
                break;
            }
        }

        long tienGiam = KhuyenMaiService.apDungKhuyenMai(
                this, danhSachSPKM, danhSachKhuyenMai, danhSachSanPham);
        if (!QuanLyTonKho.kiemTraDuTonKho(danhSachSanPham, danhSachChiTiet)) {
            return false;
        }
        if (tienGiam > 0) {
            System.out.println("[+] Đã áp dụng khuyến mãi: giảm "
                    + tienGiam + " VND.");
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
        return true;
    }
    public String toString()
    {
        return super.toString() + " | Mã khách hàng: " + maKhachHang ;
    }
    // getters - setters
    public IThanhToan getPhuongThucThanhToan() { return phuongThucThanhToan; }
    public void setPhuongThucThanhToan(IThanhToan phuongThucThanhToan) { this.phuongThucThanhToan = phuongThucThanhToan; }

    public boolean isDaThanhToan() { return daThanhToan; }
    public void setDaThanhToan(boolean daThanhToan) { this.daThanhToan = daThanhToan; }
    public String getMaKhachHang() {
        return maKhachHang;
    }

    public void setMaKhachHang(String maKhachHang) {
        this.maKhachHang = maKhachHang;
    }

    public void setCtHoaDonBans(ArrayList<CTHoaDonBan> ctHoaDonBans) {
        this.ctHoaDonBans = ctHoaDonBans;
    }

    public ArrayList<CTHoaDonBan> getCtHoaDonBans() {
        return ctHoaDonBans;
    }
}