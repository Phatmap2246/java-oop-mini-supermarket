package hoadon;

import utils.DongDuLieu;

public abstract class CTHoaDon implements interfaces.BanGhiFile {
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
        return utils.HienThi.canLe("Mã sản phẩm: " + maSanpham +
                " | Tên sản phẩm: " + tenSanPham +
                " | Số lượng: " + soLuong +
                " | Đơn giá: " + donGia );
    }

    // ================= ĐỌC / GHI FILE TXT =================

    protected String thongTinChiTietFile() {
        return DongDuLieu.ghep(maSanpham, tenSanPham, soLuong, donGia);
    }

    protected void docThongTinChiTietFile(String[] p) {
        maSanpham = p[1];
        tenSanPham = p[2];
        soLuong = Integer.parseInt(p[3]);
        donGia = Long.parseLong(p[4]);
    }

    public static CTHoaDon tuDongFile(String dong) {
        String loai = dong.split(";", -1)[0];
        if ("CTBAN".equals(loai)) return CTHoaDonBan.tuDongFile(dong);
        if ("CTNHAP".equals(loai)) return CTHoaDonNhap.tuDongFile(dong);
        throw new IllegalArgumentException("Loại CTHoaDon không hợp lệ: " + loai);
    }

    public void kiemTraDuLieuFile() {
        DongDuLieu.khongAm(soLuong, "soLuong");
        DongDuLieu.khongAm(donGia, "donGia");
    }
}
