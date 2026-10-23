package sanpham;

import java.time.LocalDate;

public class thucPham extends sanPham {
    private LocalDate hanSudung;

    public thucPham(int soLuong, LocalDate ngayNhap, int giaBan, String tenSanPham, int IDSP, int giaGoc, LocalDate hsd, String loaisp) {
        super(soLuong, ngayNhap, giaBan, tenSanPham, IDSP, giaGoc, loaisp);
        this.hanSudung = hsd;
    }

    public thucPham() {

    }

    public LocalDate getHanSudung() {
        return hanSudung;
    }

    public void setHanSudung(LocalDate hanSudung) {
        this.hanSudung = hanSudung;
    }
}
