package dichvu;

import interfaces.TrongFile;
import sanpham.SanPham;
import utils.Validation;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

public class KhuyenMaiTrucTiep extends Khuyenmai implements TrongFile {
    private long soTienGiam;

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
        if (soTienGiam <= 0)
            throw new IllegalArgumentException("Số tiền giảm phải lớn hơn 0!");
        this.soTienGiam = soTienGiam;
    }

    @Override
    public long tinhTienGiam(long donGia, int soLuong) {
        long tong = donGia * soLuong;
        if (!dangHoatDong(LocalDate.now()) || tong < getMucChiToiThieu()) {
            return 0;
        }
        return Math.min(tong, soTienGiam);
    }

    @Override
    public void setKM(Scanner sc) {
        super.setKM(sc);
        soTienGiam = Validation.inputLong(sc, "Nhập số tiền giảm giá trực tiếp: ");
    }

    @Override
    public String toString() {
        return super.toString() + " | Số tiền giảm giá trực tiếp: " + soTienGiam;
    }

    @Override
    public String getFilePath() {
        return "src/luutru/KhuyenMaiTrucTiep.csv";
    }
}
