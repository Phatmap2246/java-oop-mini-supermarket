package sanpham;

import java.time.LocalDate;

public class ThucPham extends SanPham {
    private LocalDate hanSudung;

    public ThucPham(int soLuong, LocalDate ngayNhap, double giaBan, String tenSanPham, String IDSP, double giaGoc, LocalDate hsd, String loaisp) {
        super(soLuong, ngayNhap, giaBan, tenSanPham, IDSP, giaGoc, loaisp);
        this.hanSudung = hsd;
    }

    public ThucPham() {

    }

    public LocalDate getHanSudung() {
        return hanSudung;
    }

    public void setHanSudung(LocalDate hanSudung) {
        this.hanSudung = hanSudung;
    }
}