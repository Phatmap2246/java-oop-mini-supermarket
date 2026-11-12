package dichvu;
import utils.Validation;
import interfaces.IThongTin;

import java.util.Scanner;

public class SanPhamKhuyenMai implements IThongTin {
    private String maSanPham , maKhuyenMai ;
    public SanPhamKhuyenMai()
    {
        maKhuyenMai = maSanPham = "" ;
    }
    public SanPhamKhuyenMai( String maSanPham , String maKhuyenMai )
    {
        this.maKhuyenMai = maKhuyenMai ;
        this.maSanPham = maSanPham ;
    }

    public String getMaKhuyenMai() {
        return maKhuyenMai;
    }

    public String getMaSanPham() {
        return maSanPham;
    }

    public void setMaKhuyenMai(String maKhuyenMai) {
        this.maKhuyenMai = maKhuyenMai;
    }

    public void setMaSanPham(String maSanPham) {
        this.maSanPham = maSanPham;
    }
    public void setSanPhamKhuyenMai(Scanner sc )
    {
        maSanPham = Validation.inputStringRegex(sc , "Nhập mã sản phẩm: " , "SP\\d{3}" , "Mã sản phẩm không đúng đi dạng vui lòng nhập lại!(ví dụ hợp lệ: SP001)", sanpham.SanPham.getSoThuTu());
        maKhuyenMai = Validation.inputStringRegex( sc , "Nhập mã khuyến mãi: " , "KM\\d{3}" , "Mã khuyến mãi không đúng định dạng vui lòng nhập lại!(ví dụ hợp lệ: KM001", dichvu.Khuyenmai.getSoThuTu());
    }

    @Override
    public void nhapThongTin(Scanner sc) {
        setSanPhamKhuyenMai(sc);
    }

    @Override
    public String toString() {
        return "Mã sản phẩm: " + maSanPham + " | Mã khuyến mãi: " + maKhuyenMai ;
    }
}
