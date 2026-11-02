package sanpham;

import java.time.LocalDate;

public class DoTieuDung extends SanPham {

    public DoTieuDung(int soLuong, LocalDate ngayNhap, Long giaBan, String tenSanPham, String IDSP, Long giaGoc, String loaisp) {
        super(soLuong, ngayNhap, giaBan, tenSanPham, IDSP, giaGoc, loaisp);
    }
    public DoTieuDung(){}
}