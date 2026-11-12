package dichvu ;
import utils.Validation;
import interfaces.IThongTin;
import java.time.LocalDate;
import java.util.Scanner;

public abstract class Khuyenmai implements IThongTin {
    private String maKhuyenMai ;
    private String loaiKhuyenMai ;
    private LocalDate ngayBatDau , ngayKetThuc ;
    private long mucChiToiThieu ;
    private static int soThuTu = 1 ;
    public Khuyenmai()
    {
        maKhuyenMai = loaiKhuyenMai = "" ;
        ngayBatDau = ngayKetThuc = null ;
        mucChiToiThieu = 0 ;
        maKhuyenMai = (Validation.taoMa("KM%03d" , Khuyenmai.getSoThuTu())) ;
        soThuTu++ ;
    }
    public Khuyenmai ( String maKhuyenMai , String loaiKhuyenMai , LocalDate ngayBatDau , LocalDate ngayKetThuc , long mucChiToiThieu , int soThuTu)
    {
        this.maKhuyenMai = maKhuyenMai ;
        this.loaiKhuyenMai = loaiKhuyenMai ;
        this.ngayBatDau = ngayBatDau ;
        this.ngayKetThuc= ngayKetThuc ;
        this.mucChiToiThieu = mucChiToiThieu;
        Khuyenmai.soThuTu = soThuTu ;
    }
    public void setKM ( Scanner sc )
    {
        ngayBatDau = Validation.inputLocalDateRegex(sc , "Nhập ngày bắt đầu(dd/MM/yyyy): ") ;
        ngayKetThuc = Validation.inputLocalDateRegex(sc , "Nhập ngày kết thúc(dd/MM/yyyy): ");
        while (true)
        {
            if ( ngayKetThuc.isBefore(ngayBatDau))
            {
                System.out.println("Ngày kết thúc không được sớm hơn ngày bắt đầu!");
                ngayKetThuc = Validation.inputLocalDateRegex(sc , "Nhập ngày kết thúc(dd/MM/yyyy): ");
            }
            else break;
        }
        mucChiToiThieu = Validation.inputInt(sc, "Mức chi tiêu tối thiểu để sử dụng khuyến mãi: " );
    }

    @Override
    public void nhapThongTin(Scanner sc) {
        setKM(sc);
    }

    public static int getSoThuTu() {
        return soThuTu;
    }

    public static void setSoThuTu(int soThuTu) {
        Khuyenmai.soThuTu = soThuTu;
    }
    public LocalDate getNgayBatDau() {
        return ngayBatDau;
    }

    public LocalDate getNgayKetThuc() {
        return ngayKetThuc;
    }

    public long getMucChiToiThieu() {
        return mucChiToiThieu;
    }

    public String getLoaiKhuyenMai() {
        return loaiKhuyenMai;
    }

    public String getMaKhuyenMai() {
        return maKhuyenMai;
    }

    public void setLoaiKhuyenMai(String loaiKhuyenMai) {
        this.loaiKhuyenMai = loaiKhuyenMai;
    }

    public void setNgayBatDau(LocalDate ngayBatDau) {
        if ( ngayKetThuc != null && ngayBatDau.isAfter(ngayKetThuc) )
            throw new IllegalArgumentException("Ngày bắt đầu không được sau ngày kêt thúc!") ;
        this.ngayBatDau = ngayBatDau;
    }

    public void setMucChiToiThieu(long mucChiToiThieu) {
        this.mucChiToiThieu = mucChiToiThieu;
    }

    public void setNgayKetThuc(LocalDate ngayKetThuc) {
        if ( ngayBatDau != null && ngayKetThuc.isBefore(ngayBatDau))
            throw new IllegalArgumentException("Ngày kết thúc không được bé hơn ngày bắt đầu!!") ;
        this.ngayKetThuc = ngayKetThuc;
    }

    public void setMaKhuyenMai(String maKhuyenMai) {
        this.maKhuyenMai = maKhuyenMai;
    }
    public boolean kiemTraHetHanKhuyenMai()
    {
        return ngayKetThuc != null && LocalDate.now().isAfter(ngayKetThuc) ;
    }

    public boolean dangHoatDong(LocalDate ngay) {
        return ngay != null
                && ngayBatDau != null
                && ngayKetThuc != null
                && !ngay.isBefore(ngayBatDau)
                && !ngay.isAfter(ngayKetThuc);
    }
    public boolean chuaHoatDong()
    {
        return ngayBatDau != null && LocalDate.now().isBefore(ngayBatDau) ;
    }
    @Override
    public String toString() {
        return "Mã khuyến mãi: " + maKhuyenMai + " | Loại khuyến mãi: " + loaiKhuyenMai + " | Ngày bắt đầu: " + ngayBatDau + " | Ngày kết thúc: " + ngayKetThuc +  " | Mức chi tiêu tối thiểu: " + mucChiToiThieu ;
    }

}