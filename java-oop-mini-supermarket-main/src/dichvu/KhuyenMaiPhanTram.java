package dichvu ;
import Sanpham.Sanpham;
import utils.Validation;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

public class KhuyenMaiPhanTram extends  Khuyenmai{
    private double phanTramGiamGia ;
    private long mucGiamToiDa ;
    public KhuyenMaiPhanTram()
    {
        super();
        phanTramGiamGia = 0 ;
        mucGiamToiDa = 0 ;
        super.setLoaiKhuyenMai("Giảm theo phần trăm");
    }
    public KhuyenMaiPhanTram(String maKhuyenMai , String loaiKhuyenMai , LocalDate ngayBatDau , LocalDate ngayKetThuc , long mucChiToiThieu , ArrayList<Sanpham> danhSachKhuyenMai , double phanTramGiamGia , long mucGiamToiDa )
    {
        super(maKhuyenMai, loaiKhuyenMai, ngayBatDau, ngayKetThuc, mucChiToiThieu, danhSachKhuyenMai);
        this.phanTramGiamGia = phanTramGiamGia ;
        this.mucGiamToiDa = mucGiamToiDa ;
    }

    public void setPhanTramGiamGia(double phanTramGiamGia) {
        this.phanTramGiamGia = phanTramGiamGia;
    }

    public double getPhanTramGiamGia() {
        return phanTramGiamGia;
    }

    public long getMucGiamToiDa() {
        return mucGiamToiDa;
    }

    public void setMucGiamToiDa(long mucGiamToiDa) {
        this.mucGiamToiDa = mucGiamToiDa;
    }

    public void setKM(Scanner sc )
    {
        super.setKM(sc);
        phanTramGiamGia = Validation.inputDouble(sc , "Nhập phần trăm giảm giá cho sản phẩm: ") ;
        if ( phanTramGiamGia > 1 ) phanTramGiamGia *= 0.01 ;
        mucGiamToiDa = Validation.inputLong(sc , "Nhập số tiền được giảm tối đa: ") ;
    }
    public String toString()
    {
        return super.toString()  + " | Phần trăm giảm giá: " + phanTramGiamGia + " | Mức giảm tối đa: " + mucGiamToiDa ;
    }
}
