package dichvu ;
import Sanpham.Sanpham;
import utils.Validation;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

public class KhuyenMaiTrucTiep extends Khuyenmai{
    private long soTienGiam ;
    public KhuyenMaiTrucTiep()
    {
        super();
        soTienGiam = 0 ;
        super.setLoaiKhuyenMai("Giảm giá trực tiếp");
    }
    public KhuyenMaiTrucTiep(String maKhuyenMai , String loaiKhuyenMai , LocalDate ngayBatDau , LocalDate ngayKetThuc , long mucChiToiThieu , ArrayList<Sanpham> danhSachKhuyenMai , long soTienGiam)
    {
        super(maKhuyenMai, loaiKhuyenMai, ngayBatDau, ngayKetThuc, mucChiToiThieu, danhSachKhuyenMai);
        this.soTienGiam = soTienGiam ;
    }

    public long getSoTienGiam() {
        return soTienGiam;
    }

    public void setSoTienGiam(long soTienGiam) {
        this.soTienGiam = soTienGiam;
    }
    public void setKM( Scanner sc )
    {
        super.setKM(sc);
        soTienGiam = Validation.inputInt(sc , "Nhập số tiền giảm giá trực tiếp: ") ;
        while ( soTienGiam > super.getMucChiToiThieu() )
        {
            System.out.println("Số tiền giảm giá không thể lớn hơn mức tiêu tối thiểu, vui lòng nhập lại: ");
            soTienGiam = Validation.inputInt(sc , "Nhập số tiền giảm giá trực tiếp: " ) ;
        }
    }
    public String toString()
    {
        return super.toString() + " | Số tiền giảm giá trực tiếp: " + soTienGiam ;
    }

}
