package dichvu ;

import utils.DongDuLieu;
import interfaces.TrongFile;
import sanpham.SanPham;
import utils.Validation;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

public class KhuyenMaiTrucTiep extends Khuyenmai implements TrongFile {
    private long soTienGiam ;
    public KhuyenMaiTrucTiep() {
        super("Giảm giá trực tiếp");
        soTienGiam = 0;
    }
    public KhuyenMaiTrucTiep(String maKhuyenMai, String loaiKhuyenMai, LocalDate ngayBatDau, LocalDate ngayKetThuc, long mucChiToiThieu, ArrayList<SanPham> danhSachKhuyenMai, long soTienGiam, int soThuTu) {
        super(maKhuyenMai, loaiKhuyenMai, ngayBatDau, ngayKetThuc, mucChiToiThieu, soThuTu);
        if (soTienGiam < 0) {
            throw new IllegalArgumentException("Số tiền giảm không được âm!");
        }
        this.soTienGiam = soTienGiam;
    }

    public long getSoTienGiam() {
        return soTienGiam;
    }

    public void setSoTienGiam(long soTienGiam) {
        if ( soTienGiam <= 0 )
            throw new IllegalArgumentException("Số tiền giảm phải lớn hơn 0!") ;
        this.soTienGiam = soTienGiam;
    }
    public void setKM( Scanner sc )
    {
        super.setKM(sc);
        soTienGiam = Validation.inputLong(sc, "Nhập số tiền giảm giá trực tiếp: ");
    }
    public String toString()
    {
        return utils.HienThi.canLe(super.toString() + " | Số tiền giảm giá trực tiếp: " + soTienGiam );
    }


    public long tinhTienGiam(long donGia, int soLuong) {
        long tong = donGia * soLuong;
        if (!dangHoatDong(LocalDate.now()) || tong < getMucChiToiThieu()) {
            return 0;
        }
        return Math.min(tong, soTienGiam);
    }

    // ================= ĐỌC / GHI FILE TXT =================

    @Override
    public String getFilePath(){
        return utils.DuongDanLuuTru.file("KhuyenMaiTrucTiep.txt");
    }

    @Override public String toDongFile() {
        kiemTraDuLieuFile();
        return "KMTRUCTIEP;" + thongTinKhuyenMaiFile() + ";" + DongDuLieu.ghep(soTienGiam);
    }

    public static KhuyenMaiTrucTiep tuDongFile(String dong) {
        String[] p = dong.split(";", -1);
        DongDuLieu.yeuCau(p.length == 8 && "KMTRUCTIEP".equals(p[0]), "Sai dòng KMTRUCTIEP (cần 8 cột).");
        int dem = Khuyenmai.getSoThuTu();
        KhuyenMaiTrucTiep obj = new KhuyenMaiTrucTiep();
        Khuyenmai.datBoDemFile(dem); // Constructor đọc file không cấp mã mới.
        obj.docThongTinKhuyenMaiFile(p);
        obj.soTienGiam = Long.parseLong(p[7]);
        obj.kiemTraDuLieuFile();
        obj.capNhatMaFile();
        return obj;
    }

    public void kiemTraDuLieuFile() {
        super.kiemTraDuLieuFile();
        DongDuLieu.khongAm(soTienGiam, "soTienGiam");
    }
}
