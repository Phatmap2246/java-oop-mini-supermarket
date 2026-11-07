package dichvu ;
import sanpham.SanPham;
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
    public KhuyenMaiTrucTiep(String maKhuyenMai , String loaiKhuyenMai , LocalDate ngayBatDau , LocalDate ngayKetThuc , long mucChiToiThieu , ArrayList<SanPham> danhSachKhuyenMai , long soTienGiam)
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
        soTienGiam = Validation.inputLong(sc, "Nhập số tiền giảm giá trực tiếp: ");
    }
    public String toString()
    {
        return super.toString() + " | Số tiền giảm giá trực tiếp: " + soTienGiam ;
    }

}
