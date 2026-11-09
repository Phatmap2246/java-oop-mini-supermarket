package hoadon;

import utils.Validation;
import java.time.LocalDate;

public abstract class HoaDon {
    private String maHoaDon;
    private LocalDate ngayLap;
    private String maNhanVien ;
    private static int demHoaDon = 1; // Sửa: Thêm = 0
    
    public HoaDon() {
        this.maHoaDon = Validation.taoMa("HD%03d" , demHoaDon) ;
        this.ngayLap = LocalDate.now(); 
        this.maNhanVien = "" ;
        demHoaDon++;
    }

    public HoaDon(String maHoaDon, String maNhanVien , LocalDate ngayLap , int DemHoaDon) {
        this.maHoaDon = maHoaDon;
        this.ngayLap = ngayLap ;
        this.maNhanVien = maNhanVien ;
        demHoaDon = DemHoaDon ;
    }
    public static int getDemHoaDon(){ return demHoaDon; }
    
    public abstract long tinhTongTien();
    public abstract void hienThiHoaDon();

    public String getMaHoaDon() { return maHoaDon; }
    public void setMaHoaDon(String maHoaDon) { this.maHoaDon = maHoaDon; }
    public LocalDate getNgayLap() { return ngayLap; }
    public void setNgayLap(LocalDate ngayLap) { this.ngayLap = ngayLap; }

    public static void setDemHoaDon(int demHoaDon) {
        HoaDon.demHoaDon = demHoaDon;
    }

    public String getMaNhanVien() {
        return maNhanVien;
    }

    public void setMaNhanVien(String maNhanVien) {
        this.maNhanVien = maNhanVien;
    }

    @Override
    public String toString() {
        return "Mã hoá đơn: " + maHoaDon + " | Mã nhân viên: " + maNhanVien + " | Ngày lập: " + ngayLap ;
    }
}