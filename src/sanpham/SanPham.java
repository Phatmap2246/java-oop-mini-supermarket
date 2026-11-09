package sanpham;


import utils.Validation;
import interfaces.TrongFile;

import java.util.Scanner;

public abstract class SanPham {
    private String maSanPham , tenSanPham , loaiSanPham , maNhaCungCap;
    private long giaNhap , giaBan ;
    private int soLuongTonKho  ;
    private double thue ;
    private static int soThuTu = 1 ;
    public SanPham()
    {
        tenSanPham = loaiSanPham = maNhaCungCap = "" ;
        giaNhap = giaBan = 0 ;
        soLuongTonKho = 0 ;
        thue = 0 ;
        maSanPham = Validation.taoMa("SP%03d" , soThuTu) ;
        soThuTu++ ;
    }
    public SanPham( String maSanPham , String tenSanPham , String loaiSanPham , long giaNhap , long giaBan , int soLuongTonKho , String maNhaCungCap , double thue , int soThuTu)
    {
        this.maSanPham = maSanPham ;
        this.tenSanPham = tenSanPham ;
        this.loaiSanPham = loaiSanPham ;
        this.giaNhap = giaNhap ;
        this.giaBan = giaBan ;
        this.soLuongTonKho = soLuongTonKho ;
        this.maNhaCungCap = maNhaCungCap ;
        this.thue = thue ;
        SanPham.soThuTu = soThuTu ;
    }

    public static int getSoThuTu() {
        return soThuTu;
    }

    public static void setSoThuTu(int soThuTu) {
        SanPham.soThuTu = soThuTu;
    }

    public void setThue(double thue) {
        this.thue = thue;
    }

    public double getThue() {
        return thue;
    }

    public void setMaSanPham(String maSanPham) {
        this.maSanPham = maSanPham;
    }

    public void setTenSanPham(String tenSanPham) {
        this.tenSanPham = tenSanPham;
    }

    public void setMaNhaCungCap(String maNhaCungCap) {
        this.maNhaCungCap = maNhaCungCap;
    }

    public void setSoLuongTonKho(int soLuongTonKho) {
        this.soLuongTonKho = soLuongTonKho;
    }

    public void setGiaNhap(long giaNhap) {
        this.giaNhap = giaNhap;
    }

    public void setGiaBan(long giaBan) {
        this.giaBan = giaBan;
    }

    public String getTenSanPham() {
        return tenSanPham;
    }

    public String getMaSanPham() {
        return maSanPham;
    }

    public String getLoaiSanPham() {
        return loaiSanPham;
    }

    public void setLoaiSanPham(String loaiSanPham) {
        this.loaiSanPham = loaiSanPham;
    }

    public String getMaNhaCungCap() {
        return maNhaCungCap;
    }

    public int getSoLuongTonKho() {
        return soLuongTonKho;
    }


    public long getGiaBan() {
        return giaBan;
    }

    public long getGiaNhap() {
        return giaNhap;
    }

    public void setSanPham(Scanner sc)
    {
        System.out.println("Nhập tên sản phẩm: ");
        tenSanPham = sc.nextLine() ;
        giaBan = Validation.inputLong(sc , "Nhập giá bán: ") ;
        giaNhap = Validation.inputLong(sc , "Nhập giá nhập hàng: ") ;
        soLuongTonKho = Validation.inputInt(sc , "Nhập số lượng tồn kho: ") ;
    }
    public abstract long tinhThue() ;
    @Override
    public String toString() {
        return "Mã sản phẩm: " + maSanPham + " | Tên sản phẩm: " + tenSanPham + " | Giá nhập: " + giaNhap + " | Giá bán: " + giaBan + " | Số sản phẩm tồn kho : " + soLuongTonKho + " | Nhà cung cấp : " + maNhaCungCap;
    }

}
