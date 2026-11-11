package hoadon;

import java.util.ArrayList;

import utils.Validation;

import java.time.LocalDateTime;

public abstract class HoaDon {
    protected String maHoaDon;
    protected LocalDateTime ngayLap;
    protected String sdtDoiTac;
    private static int demHoaDon = 0;
    protected ArrayList<CTHoaDonBan> danhSachChiTiet;
    
    public HoaDon() {
        this.maHoaDon = "";
        this.ngayLap = LocalDateTime.now(); 
        this.sdtDoiTac = "";
        this.danhSachChiTiet = new ArrayList<>();
    }

    public HoaDon(String maHoaDon, String sdtDoiTac) {
        this.maHoaDon = maHoaDon;
        this.ngayLap = LocalDateTime.now();
        this.sdtDoiTac = sdtDoiTac;
        this.danhSachChiTiet = new ArrayList<>();
    }

    //Tự động gộp số lượng nếu thêm trùng mã sản phẩm
    public void themChiTiet(CTHoaDonBan ct){
        for (CTHoaDonBan existing : this.danhSachChiTiet) {
            boolean cungSanPham = existing.getMaSanPham().equalsIgnoreCase(ct.getMaSanPham()) && existing.getDonGia() == ct.getDonGia();
            boolean cungKhongGiamGia = existing.getSoTienGiam() == 0 && ct.getSoTienGiam() == 0;

            if (cungSanPham && cungKhongGiamGia){
                int tongTangKem = existing.getSoLuongTangKem() + ct.getSoLuongTangKem();
                existing.setSoLuongTangKem(tongTangKem);
                existing.setSoLuong(existing.getSoLuong()+ct.getSoLuong());
                return;
            }
        }
        this.danhSachChiTiet.add(ct);
    }

    public boolean xoaChiTietMSP(String maSanPham){
        for( int i = 0 ; i<this.danhSachChiTiet.size();i++){
            if (danhSachChiTiet.get(i).getMaSanPham().equals(maSanPham)) {
                danhSachChiTiet.remove(i);
                return true;
            }
        }
        return false;
    }

    public static int getDemHoaDon(){ return demHoaDon; }
    
    public abstract long tinhTongTien();
    public abstract void hienThiHoaDon();

    public String getMaHoaDon() { return maHoaDon; }
    public void setMaHoaDon(String maHoaDon) { this.maHoaDon = maHoaDon; }
    
    public String getSDTDoiTac(){ return this.sdtDoiTac; };
    public void setSDTDoiTac(String sdtDoiTacMoi){ this.sdtDoiTac = sdtDoiTacMoi; }
    public ArrayList<CTHoaDonBan> getDanhSachChiTiet() { return danhSachChiTiet; }
    public void setDanhSachChiTiet(ArrayList<CTHoaDonBan> danhSachChiTiet) { this.danhSachChiTiet = danhSachChiTiet; }

    public LocalDateTime getNgayLap() {
        return ngayLap;
    }

    public void setNgayLap(LocalDateTime ngayLap) {
        this.ngayLap = ngayLap;
    }

    public String getSdtDoiTac() {
        return sdtDoiTac;
    }

    public void setSdtDoiTac(String sdtDoiTac) {
        this.sdtDoiTac = sdtDoiTac;
    }

    public static void setDemHoaDon(int demHoaDon) {
        HoaDon.demHoaDon = demHoaDon;
    }
    public void taoMaHoaDonBan(){
        demHoaDon++;
        this.maHoaDon = Validation.taoMa(Validation.MaHDB_REGEX, demHoaDon);
    }
}