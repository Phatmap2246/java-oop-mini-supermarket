package sanpham;
import utils.Validation;

import java.time.LocalDate;
import java.util.Scanner;
import java.time.temporal.ChronoUnit;
public class ThucPham extends SanPham
{
    private LocalDate ngaySanXuat , hanSuDung ;
    private double nhietDoBaoQuan ;
    private String baoQuan ;
    public ThucPham()
    {
        super();
        ngaySanXuat = hanSuDung = null ;
        nhietDoBaoQuan = 0 ;
        baoQuan = "" ;
        super.setThue(0.08);
        super.setLoaiSanPham("Thực phẩm");
    }
    public ThucPham ( String maSanPham , String tenSanPham , String loaiSanPham , long giaNhap , long giaBan , int soLuongTonKho , String maNhaCungCap , double thue ,
                      LocalDate ngaySanXuat , LocalDate hanSuDung , double nhietDoBaoQuan , String baoQuan)
    {
        super( maSanPham, tenSanPham, loaiSanPham, giaNhap, giaBan, soLuongTonKho, maNhaCungCap , thue );
        this.ngaySanXuat = ngaySanXuat ;
        this.hanSuDung = hanSuDung ;
        this.nhietDoBaoQuan = nhietDoBaoQuan ;
        this.baoQuan = baoQuan ;
    }

    public double getNhietDoBaoQuan() {
        return nhietDoBaoQuan;
    }

    public LocalDate getHanSuDung() {
        return hanSuDung;
    }

    public LocalDate getNgaySanXuat() {
        return ngaySanXuat;
    }

    public void setHanSuDung(LocalDate hanSuDung) {
        if (hanSuDung == null) {
            throw new IllegalArgumentException("Hạn sử dụng không được null.");
        }
        this.hanSuDung = hanSuDung;
    }

    public void setNhietDoBaoQuan(double nhietDoBaoQuan) {
        if (!Double.isFinite(nhietDoBaoQuan)) {
            throw new IllegalArgumentException("Nhiệt độ phải là số hữu hạn.");
        }
        this.nhietDoBaoQuan = nhietDoBaoQuan;
    }

    public String getBaoQuan() {
        return baoQuan;
    }

    public void setBaoQuan(String baoQuan) {
        if (baoQuan == null || baoQuan.isBlank()) {
            throw new IllegalArgumentException("Thông tin bảo quản không được rỗng.");
        }
        this.baoQuan = baoQuan;
    }

    public void setNgaySanXuat(LocalDate ngaySanXuat) {
        if (ngaySanXuat == null) {
            throw new IllegalArgumentException("Ngày sản xuất không được null.");
        }
        this.ngaySanXuat = ngaySanXuat;
    }

    @Override
    public void setSanPham(Scanner sc)
    {
        super.setSanPham(sc);
        ngaySanXuat = Validation.inputLocalDateRegex(sc , "Nhập ngày sản xuất(dd/MM/yyyy): ") ;
        hanSuDung = Validation.inputLocalDateRegex(sc , "Nhập hạn sử dụng(dd/MM/yyyy): ") ;
        while ( hanSuDung.isBefore(ngaySanXuat))
        {
            System.out.println("Ngày hêt hạn không được trươc ngày sản xuất!");
            hanSuDung = Validation.inputLocalDateRegex(sc , "Nhập lại hạn sử dụng(dd/MM/yyyy): ") ;
        }
        System.out.println("Nhập môi trường bảo quản: ");
        baoQuan = sc.nextLine() ;
        System.out.println("Nhập nhiệt độ bảo quản: ");
        nhietDoBaoQuan = Validation.inputInt(sc , "Nhập nhiệt độ bảo quản: ") ;
        while ( nhietDoBaoQuan < 0 )
        {
            System.out.println("Sản phẩm không nên được bảo quản ở nhiệt độ quá lạnh!");
            nhietDoBaoQuan = Validation.inputInt(sc , "Nhập nhiệt độ bảo quản: ") ;
        }
    }
    public int ktraNgayHetHan() {
        if ( ngaySanXuat == null || hanSuDung == null )
        {
            System.out.println("Bạn chưa nhập ngày cung cấp hoặc hạn sử dụng");
            return 0 ;
        }
        return Math.toIntExact(Math.max(ChronoUnit.DAYS.between(LocalDate.now() , hanSuDung), 0));
    }
    public int kiemTraSoNgaySuDung ()
    {
        if (ngaySanXuat == null || hanSuDung == null) {
            System.out.println("Chưa có ngày sản xuất hoặc hạn sử dụng!");
            return 0;
        }
        return Math.toIntExact(ChronoUnit.DAYS.between(ngaySanXuat , hanSuDung)) ;
    }
    public boolean kiemTraSanPhamHong()
    {
        if ( ngaySanXuat == null || hanSuDung == null )
        {
            System.out.println("Bạn chưa nhập ngày cung cấp hoặc hạn sử dụng");
            return false ;
        }
        return !hanSuDung.isAfter(LocalDate.now()) ;
    }


    @Override
    public long tinhThue() { //thuế VAT, thuế hàng hoá nếu có nhập lại,.... cộng lại thiết lập giá trị cho biến thuế

        return (long) (super.getThue()*super.getGiaBan());
    }

    @Override
    public String toString() {
        return super.toString() + " | Ngày sản xuất: " + ngaySanXuat + " | Hạn sử dụng: " + hanSuDung + " | Môi trường bảo quản: " + baoQuan + " | Nhiệt độ bảo quản : " + nhietDoBaoQuan;
    }
}
