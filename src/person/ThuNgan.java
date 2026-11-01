package person;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;
import hoadon.HoaDon;
import hoadon.HoaDonBanHang;

public class ThuNgan extends NhanVien {
    private long tongTienDauCa; 
    private ArrayList<HoaDonBanHang> dsHoaDonBanHang = new ArrayList<>();
    public ThuNgan(){
        super();
        tongTienDauCa = 0;
    }
    public ThuNgan(String email,String hoten,String sdt,DiaChi diachi, LocalDate ngsinh,String maNV,String vaitro,long luong,long phuCapTrachNhiem,long tongTienDauCa){
        super(email,hoten,sdt,diachi,ngsinh,maNV,vaitro,luong,phuCapTrachNhiem);
        this.tongTienDauCa = tongTienDauCa;
    }
    public ThuNgan(ThuNgan tn){
        super(tn);
        tongTienDauCa = tn.tongTienDauCa;
    }
    public long getTienDauCa(){
        return tongTienDauCa;
    }
    public long tinhTienCuoiCa(){
        long tongTienCuoiCa = 0; 
        for(HoaDonBanHang i: dsHoaDonBanHang)
            tongTienCuoiCa += i.tinhTongTien();
        return tongTienDauCa +  tongTienCuoiCa;
    }
    public void taoHoaDon(Scanner sc){
        HoaDonBanHang hdbh = new HoaDonBanHang();
        hdbh.nhapHoaDon(sc);
        dsHoaDonBanHang.add(hdbh);
    }

}
