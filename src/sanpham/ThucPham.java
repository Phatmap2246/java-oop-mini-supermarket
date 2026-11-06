package sanpham;

import java.time.LocalDate;

public abstract class ThucPham extends SanPham {

    public LocalDate hanSuDung;

    public ThucPham(String maSanPham, String tenSanPham, String maNhaCungCap, long giaNhap, long giaBan, int soLuong, LocalDate hanSuDung) {
        super(maSanPham, "thucPham", tenSanPham, maNhaCungCap, giaNhap, giaBan, soLuong);
        this.hanSuDung = hanSuDung;
    }

    public LocalDate getHanSuDung() {
        return hanSuDung;
    }

    public void setHanSuDung(LocalDate hanSuDung) {
        this.hanSuDung = hanSuDung;
    }
}
