package sanpham;

import java.time.LocalDate;

public abstract class SanPham {
    private int maSanPham;
    private String tenSanPham;
    private double giaGoc;
    private double giaBan;
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

    public double getGiaGoc() {
        return giaGoc;
    }

    public void setGiaGoc(double giaGoc) {
        this.giaGoc = giaGoc;
    }

    public double getGiaBan() {
        return giaBan;
    }

    public void setGiaBan(double giaBan) {
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

    public int getMaSanPham() {
        return maSanPham;
    }

    public void setMaSanPham(int maSanPham) {
        this.maSanPham = maSanPham;
    }

    public SanPham(int soLuong, LocalDate ngayNhap, double giaBan, String tenSanPham, int maSanPham, double giaGoc, String loaiSanPham) {
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
}
