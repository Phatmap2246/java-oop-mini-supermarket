package dichvu ;
import sanpham.SanPham;
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
    public KhuyenMaiPhanTram(String maKhuyenMai , String loaiKhuyenMai , LocalDate ngayBatDau , LocalDate ngayKetThuc , long mucChiToiThieu , ArrayList<SanPham> danhSachKhuyenMai , double phanTramGiamGia , long mucGiamToiDa )
    {
        super(maKhuyenMai, loaiKhuyenMai, ngayBatDau, ngayKetThuc, mucChiToiThieu, danhSachKhuyenMai);
        setPhanTramGiamGia(phanTramGiamGia > 1
                ? phanTramGiamGia / 100.0 : phanTramGiamGia);
        this.mucGiamToiDa = mucGiamToiDa ;
    }

    public void setPhanTramGiamGia(double phanTramGiamGia) {
        this.phanTramGiamGia = Math.max(0, Math.min(1, phanTramGiamGia));
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
        double phanTramNhap = Validation.inputDouble(sc, "Nhập phần trăm giảm giá cho sản phẩm: ");
        while (phanTramNhap > 100) {
            System.out.println("[!] Phần trăm giảm giá không được vượt quá 100%.");
            phanTramNhap = Validation.inputDouble(sc, "Nhập phần trăm giảm giá cho sản phẩm: ");
        }
        setPhanTramGiamGia(phanTramNhap / 100.0);
        do {
            mucGiamToiDa = Validation.inputLong(sc, "Nhập số tiền được giảm tối đa: ");
            if (mucGiamToiDa == 0) {
                System.out.println("[!] Mức giảm tối đa phải lớn hơn 0.");
            }
        } while (mucGiamToiDa == 0);
    }
    public String toString()
    {
        return super.toString() + String.format(
                " | Phần trăm giảm giá: %.2f%% | Mức giảm tối đa: %d",
                phanTramGiamGia * 100, mucGiamToiDa);
    }
}
