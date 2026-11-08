package dichvu;
import hoadon.CTHoaDon ;
import hoadon.HoaDonNhapHang;
import sanpham.SanPham;
import sanpham.QuanLySanPham;
import utils.Validation;

import java.util.ArrayList;
import java.util.Scanner;
public class Nhacungcap extends Doitac{
    private String nganhHangCungCap  , maDonViVanChuyen;
    private long hanMucNoCong ;
    private QuanLySanPham sanPhamCungCap ;
    private ArrayList<HoaDonNhapHang> hoaDonNhapHangs ;
    public Nhacungcap()
    {
        nganhHangCungCap = maDonViVanChuyen = "" ;
        hanMucNoCong = 0 ;
        super.setLoaiDoiTac("Nhà cung cấp");
        sanPhamCungCap = new QuanLySanPham() ;
        hoaDonNhapHangs = new ArrayList<>() ;
        super.setMaDoiTac(Validation.taoMa("NCC%03d" , Doitac.getSoThuTuNCC()));
        Doitac.setSoThuTuNCC(Doitac.getSoThuTuNCC()+1);
    }
    public Nhacungcap ( String maDoiTac , String tenDoiTac , String soDienThoai , String diaChi , String maSoThue , String email , String loaiDoiTac , String nganhHangCungCap , String maDonViVanChuyen , long hanMucNoCong  )
    {
        super(maDoiTac, tenDoiTac, soDienThoai, diaChi, maSoThue, email , loaiDoiTac);
        this.nganhHangCungCap = nganhHangCungCap;
        this.hanMucNoCong = hanMucNoCong;
        this.maDonViVanChuyen = maDonViVanChuyen ;
        sanPhamCungCap = new QuanLySanPham() ;
    }

    public String getMaDonViVanChuyen() {
        return maDonViVanChuyen;
    }

    public void setMaDonViVanChuyen(String maDonViVanChuyen) {
        this.maDonViVanChuyen = maDonViVanChuyen;
    }

    public String getNganhHangCungCap() {
        return nganhHangCungCap;
    }

    public void setNganhHangCungCap(String nganhHangCungCap) {
        this.nganhHangCungCap = nganhHangCungCap;
    }

    public void setHanMucNoCong(long hanMucNoCong) {
        this.hanMucNoCong = hanMucNoCong;
    }

    public long getHanMucNoCong() {
        return hanMucNoCong;
    }

    public QuanLySanPham getSanPhamCungCap() {
        return sanPhamCungCap;
    }

    public void setSanPhamCungCap(QuanLySanPham sanPhamCungCap) {
        this.sanPhamCungCap = sanPhamCungCap;
    }

    public void setHoaDonNhapHangs(ArrayList<HoaDonNhapHang> hoaDonNhapHangs) {
        this.hoaDonNhapHangs = hoaDonNhapHangs;
    }

    public ArrayList<HoaDonNhapHang> getHoaDonNhapHangs() {
        return hoaDonNhapHangs;
    }

    public void setDoiTac(Scanner sc)
    {
        super.setDoiTac( sc );
        System.out.println("Nhập ngành hàng cung cấp: ");
        nganhHangCungCap = sc.nextLine() ;
        hanMucNoCong = Validation.inputInt(sc , "Nhập hạn mức nợ công!") ;
    }
    public void nhanDon ( ArrayList<CTHoaDon> ctHoaDonNhaps )
    {
        HoaDonNhapHang hoaDonNhapHang = new HoaDonNhapHang() ;
        for ( CTHoaDon cthd: ctHoaDonNhaps )
        {

        }
    }
    public boolean baoPhiVanChuyen(Scanner sc, HoaDonNhapHang hoaDonNhap,
            Danhsachdoitac danhSachDoiTac) {
        if (!super.getMaDoiTac().equalsIgnoreCase(hoaDonNhap.getMaNhaCungCap())) {
            System.out.println("[!] Hóa đơn này không thuộc nhà cung cấp " + super.getMaDoiTac() + ".");
            return false;
        }
        return hoaDonNhap.nhaCungCapKhaiBaoVanChuyen(sc, danhSachDoiTac);
    }

    public void KhoiTaoDanhSachSPCungCap ( QuanLySanPham qlsp )
    {
        for ( SanPham sp: qlsp.getDanhSachSanPham() )
            if ( sp.getMaNhaCungCap().equalsIgnoreCase(super.getMaDoiTac()) )
                sanPhamCungCap.getDanhSachSanPham().add(sp) ;
        System.out.println("Khởi tao danh sách sản phẩm thành công!");
    }
    public void inDanhSachSanPham ()
    {
        System.out.println("Danh sách sản phẩm do nhà cung cấp " + super.getTenDoiTac() + " cung cấp!");
        sanPhamCungCap.inDanhSachSanPham();
    }
    public boolean kiemTraHanMucNoCong ()
    {
        return hanMucNoCong <= 100000000;
    }
    public String toString()
    {
        return super.toString() + " | Ngành hàng cung cấp: " + nganhHangCungCap + " | Hạn mức nợ công: " + hanMucNoCong ;
    }
}
