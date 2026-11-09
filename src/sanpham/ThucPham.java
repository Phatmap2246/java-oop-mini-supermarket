package sanpham;
import utils.Validation;

import java.time.LocalDate;
import java.util.Scanner;
public class ThucPham extends SanPham
{
    private LocalDate ngaySanXuat , hanSuDung ;
    private double nhietDoBaoQuan ;
    private String baoQuan ;
    public ThucPham()
    {
        super();
        ngaySanXuat = hanSuDung = LocalDate.now() ;
        nhietDoBaoQuan = 0 ;
        baoQuan = "" ;
        super.setThue(0.08);
    }
    public ThucPham ( String maSanPham , String tenSanPham , String loaiSanPham , long giaNhap , long giaBan , int soLuongTonKho , String maNhaCungCap , double thue ,
                      LocalDate ngaySanXuat , LocalDate hanSuDung , double nhietDoBaoQuan , String baoQuan , int soThuTu)
    {
        super( maSanPham, tenSanPham, loaiSanPham, giaNhap, giaBan, soLuongTonKho, maNhaCungCap , thue , soThuTu);
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
        this.hanSuDung = hanSuDung;
    }

    public void setNhietDoBaoQuan(double nhietDoBaoQuan) {
        this.nhietDoBaoQuan = nhietDoBaoQuan;
    }

    public String getBaoQuan() {
        return baoQuan;
    }

    public void setBaoQuan(String baoQuan) {
        this.baoQuan = baoQuan;
    }

    public void setNgaySanXuat(LocalDate ngaySanXuat) {
        this.ngaySanXuat = ngaySanXuat;
    }

    @Override
    public void setSanPham(Scanner sc)
    {
        super.setSanPham(sc);
        ngaySanXuat = Validation.inputLocalDateRegex(sc , "Nhập ngày sản xuất(dd/MM/yyyy): ") ; // chưa có ktra ngày hợp lệ
        hanSuDung = Validation.inputLocalDateRegex(sc , "Nhập hạn sử dụng(dd/MM/yyyy): ") ;
        System.out.println("Nhập môi trường bảo quản: ");
        baoQuan = sc.nextLine() ;
        System.out.println("Nhập nhiệt độ bảo quản: ");
        nhietDoBaoQuan = Validation.inputInt(sc , "Nhập nhiệt độ bảo quản: ") ;
    }
    public int ktraNgayHetHan() {
        return Math.max(hanSuDung.getDayOfYear() - LocalDate.now().getDayOfYear(), 0);
    }
    public boolean kiemTraSanPhamHong()
    {
        return ktraNgayHetHan() == 0 ;
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
