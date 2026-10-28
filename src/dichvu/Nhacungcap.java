package dichvu;
import Sanpham.Sanpham;
import utils.Nhapdulieu;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;
public class Nhacungcap extends Doitac{
    private String nganhHangCungCap  ;
    private long hanMucNoCong ;
    private ArrayList<Sanpham> sanPhamCungCap ;
    public Nhacungcap()
    {
        nganhHangCungCap = "" ;
        hanMucNoCong = 0 ;
        super.setLoaiDoiTac("Nhà cung cấp");
        sanPhamCungCap = new ArrayList<>() ;
    }
    public Nhacungcap ( String maDoiTac , String tenDoiTac , String soDienThoai , String diaChi , String maSoThue , String email , String loaiDoiTac , String nganhHangCungCap , long hanMucNoCong )
    {
        super(maDoiTac, tenDoiTac, soDienThoai, diaChi, maSoThue, email , loaiDoiTac);
        this.nganhHangCungCap = nganhHangCungCap;
        this.hanMucNoCong = hanMucNoCong;
        sanPhamCungCap = new ArrayList<>() ;
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

    public ArrayList<Sanpham> getSanPhamCungCap() {
        return sanPhamCungCap;
    }

    public void setSanPhamCungCap(ArrayList<Sanpham> sanPhamCungCap) {
        this.sanPhamCungCap = sanPhamCungCap;
    }

    public void setDoiTac( Scanner sc)
    {
        super.setDoiTac( sc );
        System.out.println("Nhập ngành hàng cung cấp: ");
        nganhHangCungCap = sc.nextLine() ;
        hanMucNoCong = Nhapdulieu.nhapSoNguyen(sc , "Nhập hạn mức nợ công!") ;
    }
    public void nhanDon (Sanpham sp , int soLuong , LocalDate ngayDatHang )
    {

    }
    public int timKiemSanPham ( String maSanPham )
    {
        for (int i = 0 ; i < sanPhamCungCap.size() ; i++ )
        {
            if ( sanPhamCungCap.get(i).getMaSanPham().equalsIgnoreCase(maSanPham)) return i ;
        }
        return -1;
    }
    public void inDanhSachSanPham ()
    {
        System.out.println("Danh sách sản phẩm do nhà cung cấp " + super.getTenDoiTac() + " cung cấp!");
        for( Sanpham sp:sanPhamCungCap )
            System.out.println(sp);
    }
    public void hienThiSanPhamTraCuu ( String maSanPham )
    {
        int index = timKiemSanPham(maSanPham) ;
        if ( index == -1 )
        {
            System.out.println("Mã sản phẩm không tồn tại!");
            return;
        }
        System.out.println("Sản phẩm cần tra cứu là: ");
        System.out.println(sanPhamCungCap.get(index));
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
