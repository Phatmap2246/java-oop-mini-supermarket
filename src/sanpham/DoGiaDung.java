package sanpham;

import java.time.LocalDate;

public class DoGiaDung extends SanPham {
    public DoGiaDung(int soLuong, LocalDate ngayNhap, Long giaBan, String tenSanPham, String IDSP, Long giaGoc, String loaisp) {
        super(soLuong, ngayNhap, giaBan, tenSanPham, IDSP, giaGoc,loaisp);
    }
    public DoGiaDung(){}
}