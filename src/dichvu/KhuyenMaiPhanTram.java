package dichvu ;

import utils.DongDuLieu;
import interfaces.TrongFile;
import sanpham.SanPham;
import utils.Validation;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

public class KhuyenMaiPhanTram extends Khuyenmai implements TrongFile {
    private double phanTramGiamGia ;
    private long mucGiamToiDa ;
    public KhuyenMaiPhanTram() {
        super("Giảm theo phần trăm");
        phanTramGiamGia = 0;
        mucGiamToiDa = 0;
    }
    public KhuyenMaiPhanTram(String maKhuyenMai, String loaiKhuyenMai, LocalDate ngayBatDau, LocalDate ngayKetThuc, long mucChiToiThieu, ArrayList<SanPham> danhSachKhuyenMai, double phanTramGiamGia, long mucGiamToiDa, int soThuTu) {
        super(maKhuyenMai, loaiKhuyenMai, ngayBatDau, ngayKetThuc, mucChiToiThieu, soThuTu);
        if (mucGiamToiDa < 0) {
            throw new IllegalArgumentException("Mức giảm tối đa không được âm.");
        }
        double rate = phanTramGiamGia > 1 ? phanTramGiamGia / 100.0 : phanTramGiamGia;
        if (!Double.isFinite(rate) || rate < 0 || rate > 1) {
            throw new IllegalArgumentException("Tỷ lệ giảm giá phải từ 0 đến 1.");
        }
        this.phanTramGiamGia = rate;
        this.mucGiamToiDa = mucGiamToiDa;
    }

    public void setPhanTramGiamGia(double phanTramGiamGia) {
        if (!Double.isFinite(phanTramGiamGia) || phanTramGiamGia < 0 || phanTramGiamGia > 1) {
            throw new IllegalArgumentException("Tỷ lệ giảm giá phải từ 0 đến 1.");
        }
        this.phanTramGiamGia = phanTramGiamGia;
    }

    public double getPhanTramGiamGia() {
        return phanTramGiamGia;
    }

    public long getMucGiamToiDa() {
        return mucGiamToiDa;
    }

    public void setMucGiamToiDa(long mucGiamToiDa) {
        if (mucGiamToiDa <= 0 )
            throw new IllegalArgumentException("Mức giảm tối đa phải lớn hơn 0") ;
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
        return utils.HienThi.canLe(super.toString() + String.format(
                " | Phần trăm giảm giá: %.2f%% | Mức giảm tối đa: %d",
                phanTramGiamGia * 100, mucGiamToiDa));
    }


    @Override
    public long tinhTienGiam(long donGia, int soLuong) {
        long tong = donGia * soLuong;
        if (!dangHoatDong(LocalDate.now()) || tong < getMucChiToiThieu()) return 0;
        long giam = (long) (tong * phanTramGiamGia);
        return Math.min(tong, Math.min(giam, mucGiamToiDa));
    }

    // ================= ĐỌC / GHI FILE TXT =================

    @Override
    public String getFilePath(){
        return utils.DuongDanLuuTru.file("KhuyenMaiPhanTram.txt");
    }

    @Override public String toDongFile() {
        kiemTraDuLieuFile();
        return "KMPHANTRAM;" + thongTinKhuyenMaiFile() + ";" + DongDuLieu.ghep(phanTramGiamGia, mucGiamToiDa);
    }

    public static KhuyenMaiPhanTram tuDongFile(String dong) {
        String[] p = dong.split(";", -1);
        DongDuLieu.yeuCau(p.length == 9 && "KMPHANTRAM".equals(p[0]), "Sai dòng KMPHANTRAM (cần 9 cột).");
        int dem = Khuyenmai.getSoThuTu();
        KhuyenMaiPhanTram obj = new KhuyenMaiPhanTram();
        Khuyenmai.datBoDemFile(dem); // Constructor đọc file không cấp mã mới.
        obj.docThongTinKhuyenMaiFile(p);
        obj.phanTramGiamGia = Double.parseDouble(p[7]);
        obj.mucGiamToiDa = Long.parseLong(p[8]);
        obj.kiemTraDuLieuFile();
        obj.capNhatMaFile();
        return obj;
    }

    public void kiemTraDuLieuFile() {
        super.kiemTraDuLieuFile();
        DongDuLieu.khongAm(mucGiamToiDa, "mucGiamToiDa");
        DongDuLieu.tyLe(phanTramGiamGia, 1, "tỷ lệ giảm");
    }
}
