package hoadon;

public abstract class CTHoaDon {
   private String maSanpham , tenSanPham ;
   private int soLuong ;
   private long donGia ;
   public CTHoaDon()
   {
       maSanpham = tenSanPham = "" ;
       soLuong = 0 ;
       donGia = 0 ;
   }

    public String getMaSanpham() {
        return maSanpham;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public long getDonGia() {
        return donGia;
    }

    public String getTenSanPham() {
        return tenSanPham;
    }

    public void setDonGia(long donGia) {
        if (donGia < 0) {
            throw new IllegalArgumentException("Đơn giá không được âm.");
        }
        this.donGia = donGia;
    }

    public void setMaSanpham(String maSanpham) {
        if (maSanpham == null || !maSanpham.matches("(?i)SP\\d+")) {
            throw new IllegalArgumentException("Mã sản phẩm không hợp lệ.");
        }
        this.maSanpham = maSanpham;
    }

    public void setTenSanPham(String tenSanPham) {
        if (tenSanPham == null || tenSanPham.isBlank()) {
            throw new IllegalArgumentException("Tên sản phẩm không được rỗng.");
        }
        this.tenSanPham = tenSanPham;
    }

    public void setSoLuong(int soLuong) {
        if (soLuong < 0) {
            throw new IllegalArgumentException("Số lượng không được âm.");
        }
        this.soLuong = soLuong;
    }
    public String toString()
    {
        return "Mã sản phẩm: " + maSanpham +
                " | Tên sản phẩm: " + tenSanPham +
                " | Số lượng: " + soLuong +
                " | Đơn giá: " + donGia ;
    }
}
