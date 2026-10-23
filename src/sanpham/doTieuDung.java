package sanpham;

import java.time.LocalDate;

public class doTieuDung extends sanPham{

    public doTieuDung(int soLuong, LocalDate ngayNhap, int giaBan, String tenSanPham, int IDSP, int giaGoc, String loaisp) {
        super(soLuong, ngayNhap, giaBan, tenSanPham, IDSP, giaGoc, loaisp);
    }
    public doTieuDung(){}
}
