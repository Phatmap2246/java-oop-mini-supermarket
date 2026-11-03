package dichvu;
import java.util.Scanner;

public class Xe {
    private String maXe , tenXe , loaiXe ;
    private long giaMotKm , sucChuaToiDa , giaVanChuyenCoBan;
    public Xe()
    {
        maXe = tenXe = loaiXe = "" ;
        giaMotKm = sucChuaToiDa = giaVanChuyenCoBan = 0 ;
    }
    public Xe( String maXe , String tenXe , String loaiXe , long giaMotKm , long sucChuaToiDa , long giaVanChuyenCoBan)
    {
        this.maXe = maXe;
        this.tenXe = tenXe;
        this.loaiXe = loaiXe;
        this.giaMotKm = giaMotKm;
        this.sucChuaToiDa = sucChuaToiDa;
        this.giaVanChuyenCoBan = giaVanChuyenCoBan;
    }

    public void setSucChuaToiDa(long sucChuaToiDa) {
        this.sucChuaToiDa = sucChuaToiDa;
    }

    public long getSucChuaToiDa() {
        return sucChuaToiDa;
    }

    public long getGiaMotKm() {
        return giaMotKm;
    }

    public String getLoaiXe() {
        return loaiXe;
    }

    public String getMaXe() {
        return maXe;
    }

    public String getTenXe() {
        return tenXe;
    }

    public void setGiaMotKm(long giaMotKm) {
        this.giaMotKm = giaMotKm;
    }

    public void setLoaiXe(String loaiXe) {
        this.loaiXe = loaiXe;
    }


    public void setMaXe(String maXe) {
        this.maXe = maXe;
    }

    public void setTenXe(String tenXe) {
        this.tenXe = tenXe;
    }

    public long getGiaVanChuyenCoBan() {
        return giaVanChuyenCoBan;
    }

    public void setGiaVanChuyenCoBan(long giaVanChuyenCoBan) {
        this.giaVanChuyenCoBan = giaVanChuyenCoBan;
    }

    public void setXe ( Scanner sc)
    {
        maXe = utils.Validation.inputStringRegex(sc , "Nhập mã số xe: " , "XE\\d{3}" , "Mã xe không đúng định dạng vui lòng nhập lại(VD: XE001)!");
        System.out.println("Nhập tên xe: ");
        tenXe = sc.nextLine() ;
        System.out.println("Nhập loại xe: ");
        loaiXe = sc.nextLine() ;
        giaMotKm = utils.Validation.inputInt(sc , "Nhập chi phí vận chuyển(Giá/km): ") ;
        giaVanChuyenCoBan = utils.Validation.inputInt(sc , "Nhập giá vận chuyển cơ bản: ") ;
        sucChuaToiDa = utils.Validation.inputInt(sc , "Nhập sức chứa tối đa của xe: ") ;
    }
    @Override
    public String toString() {
        return "Mã số xe: " + maXe + " | Tên xe"  + tenXe + " | Loại xe: " + loaiXe +  " | Giá/km: " + giaMotKm + " | Sức chở tối đa: " + sucChuaToiDa;
    }
}
