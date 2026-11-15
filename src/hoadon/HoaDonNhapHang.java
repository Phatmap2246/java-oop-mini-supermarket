package hoadon;

import utils.DongDuLieu;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Scanner;

import dichvu.Danhsachdoitac;
import dichvu.Nhacungcap;
import sanpham.QuanLySanPham;
import utils.Validation;

public class HoaDonNhapHang extends HoaDon {
    private String maNhaCungCap;
    private long chiPhiVanChuyen;
    private double thueVAT; // Lưu ý VAT lưu dạng 10 (10%)
    private ArrayList<CTHoaDonNhap> ctHoaDonNhaps ;
    private boolean Duyet ;
    private boolean daNhapSP ;

    // Mã hóa đơn nhập: Bắt đầu bằng chữ "HDN" (in hoa) và theo sau là các chữ số. VD: HDN001
    public static final String MA_HD_NHAP_REGEX = "(?i)^HD(N)?\\d+$";
    
    // Mã nhà cung cấp: Bắt đầu bằng chữ "NCC" (in hoa) và theo sau là các chữ số. VD: NCC01
    public static final String MA_NCC_REGEX = "^NCC\\d+$";    

    // Constructors
    public HoaDonNhapHang() {
        this("");
    }
    public HoaDonNhapHang( String maNhanVien) {
        super(maNhanVien, "Hoá đơn nhập");
        this.maNhaCungCap = "";
        this.thueVAT = 0;
        this.chiPhiVanChuyen = 0;
        this.Duyet = false ;
        this.daNhapSP = false ;
        this.ctHoaDonNhaps = new ArrayList<>() ;
    }

    public HoaDonNhapHang(String maHoaDon, LocalDateTime ngayLap, String maNhanvien, String loaiHoaDon , String maNhaCungCap, long chiPhiVanChuyen, double thueVAT , boolean Duyet , boolean daNhapSP) {
        super(maHoaDon, ngayLap , maNhanvien , loaiHoaDon);
        if (chiPhiVanChuyen < 0) throw new IllegalArgumentException("Chi phí vận chuyển không được âm.");
        if (!Double.isFinite(thueVAT) || thueVAT < 0 || thueVAT > 100) throw new IllegalArgumentException("Thuế VAT phải từ 0 đến 100.");
        this.maNhaCungCap = maNhaCungCap; // ĐÃ THÊM DÒNG NÀY
        this.chiPhiVanChuyen = chiPhiVanChuyen;
        this.thueVAT = thueVAT;
        this.Duyet = Duyet ;
        this.daNhapSP = daNhapSP ;
        this.ctHoaDonNhaps = new ArrayList<>();
    }

    // Override
    @Override 
    public long tinhTongTien(){
        long tongTienHang = 0;
        for(CTHoaDonNhap ct : ctHoaDonNhaps){
            if (ct == null) continue;
            tongTienHang += ct.thanhTien();
        }
        long tienThue = (long)((tongTienHang * thueVAT) / 100);
        // ĐÃ SỬA: Cộng thêm chi phí vận chuyển
        return tongTienHang + tienThue + chiPhiVanChuyen; 
    }

    @Override
    public void hienThiHoaDon() {
        System.out.println("====================HOÁ ĐƠN NHẬP HÀNG====================");
        System.out.println("Chi tiết hoá đơn: ");
        for ( CTHoaDonNhap ct: ctHoaDonNhaps )
            System.out.println(ct);
        System.out.println("Tổng tiền nhập hàng: " + tinhTongTien());
        System.out.println(toString());
    }

    // Nhập hóa đơn nhập hàng
    public void nhapHoaDon(Scanner sc , QuanLySanPham qlsp , Danhsachdoitac dsdt ){
        this.maNhaCungCap = Validation.inputStringRegex(sc , "Nhập mã nhà cung cấp: " , Validation.MaNCC_REGEX , "Mã nhà cung cấp đang nhập sai định dạng vui lòng nhập lại!(VD:NCC001): " , Nhacungcap.getSoThuTuNCC()) ;
        while (dsdt.timViTri(maNhaCungCap) == -1)
        {
            this.maNhaCungCap = Validation.inputStringRegex(sc , "Nhập mã nhà cung cấp: " , Validation.MaNCC_REGEX , "Mã nhà cung cấp đang nhập sai định dạng vui lòng nhập lại!(VD:NCC001): " , Nhacungcap.getSoThuTuNCC()) ;
            System.out.println("Mã nhà cung cấp không tồn tại vui lòng nhập lại!");
        }
        this.thueVAT = Validation.inputDouble(sc, "Nhập thuế VAT (%): ");
        while (this.thueVAT > 15 )
        {
            System.out.println("Thuế VAT không vượt quá 15% giá trị hoá đơn vui lòng nhập lại!");
            this.thueVAT = Validation.inputDouble(sc, "Nhập thuế VAT (%): ");
        }
        this.chiPhiVanChuyen = Validation.inputLong(sc, "Nhập chi phí vận chuyển (VND): ");

        System.out.println("--- Nhập danh sách sản phẩm nhập ---");
        do {
            CTHoaDonNhap ct = new CTHoaDonNhap();
            ct.nhapThongTin(sc, qlsp);
            boolean kiemTraTrungSP = false ;
            for ( CTHoaDonNhap cthd:ctHoaDonNhaps)
            {
                if ( cthd.getMaSanpham().equalsIgnoreCase(ct.getMaSanpham()))
                {
                    int soLuongMoi = cthd.getSoLuong() + ct.getSoLuong() ;
                    if (soLuongMoi > Integer.MAX_VALUE) {
                        throw new IllegalArgumentException("Số lượng vượt giới hạn.");
                    }
                    cthd.setSoLuong(soLuongMoi);
                    kiemTraTrungSP = true ;
                }
            }
            if (!kiemTraTrungSP) ctHoaDonNhaps.add(ct) ;
        } while (Validation.inputCoKhong(sc, "Bạn có muốn tiếp tục(Có/Không): "));
    }
    public void nhapHoaDon( Scanner sc , ArrayList<CTHoaDonNhap> cthoaDonNhaps , String maNhaCungCap)
    {
        this.thueVAT = Validation.inputDouble(sc, "Nhập thuế VAT (%): ");
        while (this.thueVAT > 0.15 )
        {
            System.out.println("Thuế VAT không vượt quá 15% giá trị hoá đơn vui lòng nhập lại!");
            this.thueVAT = Validation.inputDouble(sc, "Nhập thuế VAT (%): ");
        }
        this.chiPhiVanChuyen = Validation.inputLong(sc, "Nhập chi phí vận chuyển (VND): ");
        setCtHoaDonNhaps(cthoaDonNhaps);
        this.maNhaCungCap = maNhaCungCap ;
    }

    // getters - setters
    public String getMaNhaCungCap() {
        return maNhaCungCap;
    }

    public void setMaNhaCungCap(String maNhaCungCap) {
        if (maNhaCungCap == null || !maNhaCungCap.matches("(?i)NCC\\d+")) {
            throw new IllegalArgumentException("MaNhaCungCap không đúng định dạng.");
        }
        this.maNhaCungCap = maNhaCungCap;
    }

    public double getThueVAT() {
        return thueVAT;
    }

    public void setThueVAT(double thueVAT) {
        if (!Double.isFinite(thueVAT) || thueVAT < 0 || thueVAT > 100) {
            throw new IllegalArgumentException("thueVAT không hợp lệ.");
        }
        this.thueVAT = thueVAT;
    }

// Getter/Setter cho chi phí vận chuyển
    public long getChiPhiVanChuyen() {
        return chiPhiVanChuyen;
    }

    public void setChiPhiVanChuyen(long chiPhiVanChuyen) {
        if (chiPhiVanChuyen < 0) {
            throw new IllegalArgumentException("ChiPhiVanChuyen không được âm.");
        }
        this.chiPhiVanChuyen = chiPhiVanChuyen;
    }

    public ArrayList<CTHoaDonNhap> getCtHoaDonNhaps() {
        return ctHoaDonNhaps;
    }

    public void setCtHoaDonNhaps(ArrayList<CTHoaDonNhap> ctHoaDonNhaps) {
        if (ctHoaDonNhaps == null || ctHoaDonNhaps.contains(null)) {
            throw new IllegalArgumentException("Chi tiết hóa đơn không được null hoặc chứa phần tử null.");
        }
        this.ctHoaDonNhaps = ctHoaDonNhaps;
    }

    public boolean isDuyet() {
        return Duyet;
    }

    public void setDuyet(boolean duyet) {
        Duyet = duyet;
    }

    public boolean isDaNhapSP() {
        return daNhapSP;
    }

    public void setDaNhapSP(boolean daNhapSP) {
        this.daNhapSP = daNhapSP;
    }

    @Override
    public String toString() {
        return utils.HienThi.canLe(super.toString() + " | Mã nhà cung cấp: " + maNhaCungCap + " | Chi phí vận chuyển: " + chiPhiVanChuyen + " | Thuế VAT: " + thueVAT + " | Trạng thái duyệt: " + ((Duyet)?"Đã duyệt":"Chưa duyệt"));
    }

    // ================= ĐỌC / GHI FILE TXT =================

    @Override public String toDongFile() {
        kiemTraDuLieuFile();
        return "HDNHAP;" + thongTinHoaDonFile() + ";" + DongDuLieu.ghep(maNhaCungCap, chiPhiVanChuyen, thueVAT, Duyet, daNhapSP);
    }

    public static HoaDonNhapHang tuDongFile(String dong) {
        String[] p = dong.split(";", -1);
        DongDuLieu.yeuCau(p.length == 10 && "HDNHAP".equals(p[0]), "Sai dòng HDNHAP (cần 10 cột).");
        int dem = HoaDon.getSoThuTu();
        HoaDonNhapHang obj = new HoaDonNhapHang();
        HoaDon.datBoDemFile(dem); // Constructor đọc file không cấp mã mới.
        obj.docThongTinHoaDonFile(p);
        obj.maNhaCungCap = p[5];
        obj.chiPhiVanChuyen = Long.parseLong(p[6]);
        obj.thueVAT = Double.parseDouble(p[7]);
        obj.Duyet = DongDuLieu.logic(p[8]);
        obj.daNhapSP = DongDuLieu.logic(p[9]);
        obj.kiemTraDuLieuFile();
        obj.capNhatMaFile();
        return obj;
    }

    public void kiemTraDuLieuFile() {
        super.kiemTraDuLieuFile();
        DongDuLieu.khongAm(chiPhiVanChuyen, "chiPhiVanChuyen");
        DongDuLieu.tyLe(thueVAT, 100, "thuế VAT");
    }
}
