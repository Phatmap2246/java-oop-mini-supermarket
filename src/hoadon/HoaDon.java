package hoadon;

import java.util.ArrayList;
import java.time.LocalDate;

public abstract class HoaDon {
    protected String maHoaDon;
    protected LocalDate ngayLap;
    protected String tenDoiTac;
    private static int demHoaDon;
    protected ArrayList<CTHoaDon> danhSachChiTiet;
    
    // Constructors
    public HoaDon() {
        this.maHoaDon = "";
        this.ngayLap = LocalDate.now(); // Tự động lấy ngày hiện tại
        this.tenDoiTac = "";
        this.danhSachChiTiet = new ArrayList<>();
        demHoaDon++;
    }

    public HoaDon(String maHoaDon, String tenDoiTac) {
        this.maHoaDon = maHoaDon;
        this.ngayLap = LocalDate.now();
        this.tenDoiTac = tenDoiTac;
        this.danhSachChiTiet = new ArrayList<>();
        demHoaDon++;
    }

    // Methodss
    public void themChiTiet( CTHoaDon ct){
        this.danhSachChiTiet.add(ct);
    }

    // xóa chi tiết bằng mã sản phẩm 
    public boolean xoaChiTietMSP(String maSanPham){
        for( int i = 0 ; i<this.danhSachChiTiet.size();i++){
            if (danhSachChiTiet.get(i).getMaSanPham().equals(maSanPham)) {
                danhSachChiTiet.remove(i);
                return true;
            }
        }
        return false;
    }

    public static int getDemHoaDon(){
        return demHoaDon;
    }
    // Phương thức trừu tượng
    public abstract double tinhTongTien();
    public abstract void hienThiHoaDon();

    //getter - setter
    public String getMaHoaDon() {
        return maHoaDon;
    }

    public void setMaHoaDon(String maHoaDon) {
        this.maHoaDon = maHoaDon;
    }

    public LocalDate getNgayLap() {
        return ngayLap;
    }

    public void setNgayLap(LocalDate ngayLap) {
        this.ngayLap = ngayLap;
    }

    public String getTenDoiTac() {
        return tenDoiTac;
    }

    public void setTenDoiTac(String tenDoiTac) {
        this.tenDoiTac = tenDoiTac;
    }

    public static void setDemHoaDon(int demHoaDon) {
        HoaDon.demHoaDon = demHoaDon;
    }

    public ArrayList<CTHoaDon> getDanhSachChiTiet() {
        return danhSachChiTiet;
    }

    public void setDanhSachChiTiet(ArrayList<CTHoaDon> danhSachChiTiet) {
        this.danhSachChiTiet = danhSachChiTiet;
    }

    
}
