package sanpham;

import java.time.LocalDate;

public abstract class SanPham {
    private String maSanPham;
    private String tenSanPham;
    private Long giaGoc;
    private Long giaBan;
    private LocalDate ngayNhap;
    private int soLuong;
    private String loaiSanPham;

    //getter setter


    public String getTenSanPham() {
        return tenSanPham;
    }

    public void setTenSanPham(String tenSanPham) {
        this.tenSanPham = tenSanPham;
    }

    public Long getGiaGoc() {
        return giaGoc;
    }

    public void setGiaGoc(Long giaGoc) {
        this.giaGoc = giaGoc;
    }

    public Long getGiaBan() {
        return giaBan;
    }

    public void setGiaBan(Long giaBan) {
        this.giaBan = giaBan;
    }

    public LocalDate getNgayNhap() {
        return ngayNhap;
    }

    public void setNgayNhap(LocalDate ngayNhap) {
        this.ngayNhap = ngayNhap;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public String getMaSanPham() {
        return maSanPham;
    }

    public void setMaSanPham(String maSanPham) {
        this.maSanPham = maSanPham;
    }

    public SanPham(int soLuong, LocalDate ngayNhap, Long giaBan, String tenSanPham, String maSanPham, Long giaGoc, String loaiSanPham) {
        this.soLuong = soLuong;
        this.ngayNhap = ngayNhap;
        this.giaBan = giaBan;
        this.tenSanPham = tenSanPham;
        this.maSanPham = maSanPham;
        this.giaGoc = giaGoc;
        this.loaiSanPham = loaiSanPham;
    }
    public SanPham(){

    }

    public String getLoaiSanPham() {
        return loaiSanPham;
    }

    public void setLoaiSanPham(String loaiSanPham) {
        this.loaiSanPham = loaiSanPham;
    }
    //TODO qua kho de lam, can tim giai phap
    public String getMaNhaCungCap() {
        return "";
    }
}