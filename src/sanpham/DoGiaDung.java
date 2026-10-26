package sanpham;

import java.time.LocalDate;

public class DoGiaDung extends SanPham {
    public DoGiaDung(int soLuong, LocalDate ngayNhap, double giaBan, String tenSanPham, int IDSP, double giaGoc, String loaisp) {
        super(soLuong, ngayNhap, giaBan, tenSanPham, IDSP, giaGoc,loaisp);
    }
    public DoGiaDung(){}
}