package dichvu;
import interfaces.TrongFile;
import sanpham.SanPham;
import sanpham.QuanLySanPham;
import utils.Validation;

import java.util.Scanner;
public class Nhacungcap extends Doitac implements TrongFile {
    private String nganhHangCungCap;
    private long hanMucNoCong ;
    private QuanLySanPham sanPhamCungCap ;
    public Nhacungcap()
    {
        nganhHangCungCap = "" ;
        hanMucNoCong = 0 ;
        super.setLoaiDoiTac("Nhà cung cấp");
        sanPhamCungCap = new QuanLySanPham() ;
        super.setMaDoiTac(Validation.taoMa("NCC%03d" , Doitac.getSoThuTuNCC()));
        Doitac.setSoThuTuNCC(Doitac.getSoThuTuNCC()+1);
    }
    public Nhacungcap ( String maDoiTac , String tenDoiTac , String soDienThoai , String diaChi , String maSoThue , String email , String loaiDoiTac , String nganhHangCungCap , long hanMucNoCong , int soThuTu)
    {
        super(maDoiTac, tenDoiTac, soDienThoai, diaChi, maSoThue, email , loaiDoiTac);
        Doitac.setSoThuTuNCC(soThuTu);
        this.nganhHangCungCap = nganhHangCungCap;
        this.hanMucNoCong = hanMucNoCong;
        sanPhamCungCap = new QuanLySanPham() ;
    }

    public String getNganhHangCungCap() {
        return nganhHangCungCap;
    }

    public void setNganhHangCungCap(String nganhHangCungCap) {
        this.nganhHangCungCap = nganhHangCungCap;
    }

    public void setHanMucNoCong(long hanMucNoCong) {
        if ( hanMucNoCong < 0 )
            throw new IllegalArgumentException("Hạn mức nợ cồng không âm!") ;
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

    public void setDoiTac(Scanner sc)
    {
        super.setDoiTac( sc );
        System.out.println("Nhập ngành hàng cung cấp: ");
        nganhHangCungCap = sc.nextLine() ;
        hanMucNoCong = Validation.inputInt(sc , "Nhập hạn mức nợ công!") ;
    }

    public void KhoiTaoDanhSachSPCungCap ( QuanLySanPham qlsp )
    {
        sanPhamCungCap.getDanhSachSanPham().clear();
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
    @Override
    public String getFilePath(){
        return "src/luutru/Nhacungcap.csv";
    }
}
