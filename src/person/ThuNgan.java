package person;

import java.time.LocalDate;
import java.util.Scanner;
import dichvu.DanhSachSPKM;
import dichvu.Danhsachkhuyenmai;
import hoadon.HoaDonBanHang;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import utils.YeuCau;

public class ThuNgan extends NhanVien {
    private long tongTienDauCa; 
    private HoaDonBanHang[] dsHoaDonBanHang = new HoaDonBanHang[0];
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
        for(HoaDonBanHang i: dsHoaDonBanHang) {
            if (i.isDaThanhToan()) {
                tongTienCuoiCa += i.tinhTongTien();
            }
        }
        return tongTienDauCa +  tongTienCuoiCa;
    }
    public void taoHoaDonThanhToan(Scanner sc){
        HoaDonBanHang hdbh = new HoaDonBanHang();
        hdbh.nhapHoaDon(sc);
        HoaDonBanHang[] dsMoi = new HoaDonBanHang[dsHoaDonBanHang.length + 1];
        for (int i = 0; i < dsHoaDonBanHang.length; i++) {
            dsMoi[i] = dsHoaDonBanHang[i];
        }
        dsHoaDonBanHang = dsMoi;
        dsHoaDonBanHang[dsHoaDonBanHang.length - 1] = hdbh;
    }



    public void thayCa(ThuNgan tn) throws IOException {
        File f = new File("hoadon.LichSuHoaDon");
        if(!f.exists()) {
            System.out.println("File khong ton tai");
            return;
        }
        BufferedWriter bw = new BufferedWriter( new FileWriter(f)) ;
        for(int i = 0; i<dsHoaDonBanHang.length; i++)
        {
            String[] data = new String[4];
            data[0] = maNV; data[1] = dsHoaDonBanHang[i].getMaHoaDon();
            data[2] = dsHoaDonBanHang[i].getTenDoiTac(); data[3] = dsHoaDonBanHang[i].getNgayLap().toString();
            String line = String.join(";", data);
            bw.newLine();
            bw.write(line);
        }
        tn.tongTienDauCa = tinhTienCuoiCa();
        bw.close();
    }

}
