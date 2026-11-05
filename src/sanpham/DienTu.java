package sanpham;

public class DienTu extends SanPham {
    int soThangBaoHanh;


    public DienTu(String maSanPham,String tenSanPham, String maNhaCungCap, long giaNhap, long giaBan, int soLuong, int soThangBaoHanh) {
        super(maSanPham, "dienTu", tenSanPham, maNhaCungCap, giaNhap, giaBan, soLuong);
        this.soThangBaoHanh =soThangBaoHanh;
    }
}
