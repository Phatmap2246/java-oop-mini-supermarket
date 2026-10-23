package sanpham;

import java.time.LocalDate;

public abstract class sanPham {
    private int IDSP;
    private String tenSanPham;
    private int giaGoc;
    private int giaBan;
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

    public int getGiaGoc() {
        return giaGoc;
    }

    public void setGiaGoc(int giaGoc) {
        this.giaGoc = giaGoc;
    }

    public int getGiaBan() {
        return giaBan;
    }

    public void setGiaBan(int giaBan) {
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

    public int getIDSP() {
        return IDSP;
    }

    public void setIDSP(int IDSP) {
        this.IDSP = IDSP;
    }

    public sanPham(int soLuong, LocalDate ngayNhap, int giaBan, String tenSanPham, int IDSP, int giaGoc, String loaiSanPham) {
        this.soLuong = soLuong;
        this.ngayNhap = ngayNhap;
        this.giaBan = giaBan;
        this.tenSanPham = tenSanPham;
        this.IDSP = IDSP;
        this.giaGoc = giaGoc;
        this.loaiSanPham = loaiSanPham;
    }
    public sanPham(){

    }

    public String getLoaiSanPham() {
        return loaiSanPham;
    }

    public void setLoaiSanPham(String loaiSanPham) {
        this.loaiSanPham = loaiSanPham;
    }
}
