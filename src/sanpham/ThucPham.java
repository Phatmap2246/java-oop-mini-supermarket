package sanpham;

import utils.DongDuLieu;
import utils.Validation;

import java.time.LocalDate;
import java.util.Scanner;
import java.time.temporal.ChronoUnit;
public class ThucPham extends SanPham
{
    private LocalDate ngaySanXuat , hanSuDung ;
    private double nhietDoBaoQuan ;
    private String baoQuan ;
    public ThucPham()
    {
        super(0.08, "Thực phẩm");
        ngaySanXuat = hanSuDung = null ;
        nhietDoBaoQuan = 0 ;
        baoQuan = "" ;
    }
    public ThucPham ( String maSanPham , String tenSanPham , String loaiSanPham , long giaNhap , long giaBan , int soLuongTonKho , String maNhaCungCap , double thue ,
                      LocalDate ngaySanXuat , LocalDate hanSuDung , double nhietDoBaoQuan , String baoQuan)
    {
        super( maSanPham, tenSanPham, loaiSanPham, giaNhap, giaBan, soLuongTonKho, maNhaCungCap , thue );
        if (!Double.isFinite(nhietDoBaoQuan)) throw new IllegalArgumentException("Nhiệt độ không hợp lệ.");
        if (ngaySanXuat != null && hanSuDung != null && hanSuDung.isBefore(ngaySanXuat)) {
            throw new IllegalArgumentException("Hạn sử dụng không được trước ngày sản xuất.");
        }
        this.ngaySanXuat = ngaySanXuat ;
        this.hanSuDung = hanSuDung ;
        this.nhietDoBaoQuan = nhietDoBaoQuan ;
        this.baoQuan = baoQuan ;
    }

    public double getNhietDoBaoQuan() {
        return nhietDoBaoQuan;
    }

    public LocalDate getHanSuDung() {
        return hanSuDung;
    }

    public LocalDate getNgaySanXuat() {
        return ngaySanXuat;
    }

    public void setHanSuDung(LocalDate hanSuDung) {
        if (hanSuDung == null) {
            throw new IllegalArgumentException("Hạn sử dụng không được null.");
        }
        if (ngaySanXuat!= null && hanSuDung.isBefore(ngaySanXuat)) {
            throw new IllegalArgumentException("Hạn sử dụng không được trước ngày sản xuất!");
        }
        this.hanSuDung = hanSuDung;
    }

    public void setNhietDoBaoQuan(double nhietDoBaoQuan) {
        if (!Double.isFinite(nhietDoBaoQuan)) {
            throw new IllegalArgumentException("Nhiệt độ phải là số hữu hạn.");
        }
        this.nhietDoBaoQuan = nhietDoBaoQuan;
    }

    public String getBaoQuan() {
        return baoQuan;
    }

    public void setBaoQuan(String baoQuan) {
        if (baoQuan == null || baoQuan.isBlank()) {
            throw new IllegalArgumentException("Thông tin bảo quản không được rỗng.");
        }
        this.baoQuan = baoQuan;
    }

    public void setNgaySanXuat(LocalDate ngaySanXuat) {
        if (ngaySanXuat == null) {
            throw new IllegalArgumentException("Ngày sản xuất không được null.");
        }
        if ( hanSuDung != null && ngaySanXuat.isAfter(hanSuDung))
        {
            throw new IllegalArgumentException("Ngày sản xuất không được sau hạn sử dụng!") ;
        }
        this.ngaySanXuat = ngaySanXuat;
    }

    @Override
    public void setSanPham(Scanner sc)
    {
        super.setSanPham(sc);
        ngaySanXuat = Validation.inputLocalDateRegex(sc , "Nhập ngày sản xuất(dd/MM/yyyy): ") ;
        hanSuDung = Validation.inputLocalDateRegex(sc , "Nhập hạn sử dụng(dd/MM/yyyy): ") ;
        while ( hanSuDung.isBefore(ngaySanXuat))
        {
            System.out.println("Ngày hêt hạn không được trươc ngày sản xuất!");
            hanSuDung = Validation.inputLocalDateRegex(sc , "Nhập lại hạn sử dụng(dd/MM/yyyy): ") ;
        }
        System.out.println("Nhập môi trường bảo quản: ");
        baoQuan = sc.nextLine() ;
        System.out.println("Nhập nhiệt độ bảo quản: ");
        nhietDoBaoQuan = Validation.inputInt(sc , "Nhập nhiệt độ bảo quản: ") ;
        while ( nhietDoBaoQuan < 0 || Double.isFinite(nhietDoBaoQuan))
        {
            System.out.println("Sản phẩm không nên được bảo quản ở nhiệt độ quá lạnh và nhiệt độ không nhập số thập phân!");
            nhietDoBaoQuan = Validation.inputInt(sc , "Nhập nhiệt độ bảo quản: ") ;
        }
    }
    public int ktraNgayHetHan() {
        if ( ngaySanXuat == null || hanSuDung == null )
        {
            System.out.println("Bạn chưa nhập ngày cung cấp hoặc hạn sử dụng");
            return 0 ;
        }
        return Math.toIntExact(Math.max(ChronoUnit.DAYS.between(LocalDate.now() , hanSuDung), 0));
    }
    public int kiemTraSoNgaySuDung ()
    {
        if (ngaySanXuat == null || hanSuDung == null) {
            System.out.println("Chưa có ngày sản xuất hoặc hạn sử dụng!");
            return 0;
        }
        return Math.toIntExact(ChronoUnit.DAYS.between(ngaySanXuat , hanSuDung)) ;
    }
    public boolean kiemTraSanPhamHong()
    {
        if ( ngaySanXuat == null || hanSuDung == null )
        {
            System.out.println("Bạn chưa nhập ngày cung cấp hoặc hạn sử dụng");
            return false ;
        }
        return !hanSuDung.isAfter(LocalDate.now()) ;
    }


    @Override
    public long tinhThue() { //thuế VAT, thuế hàng hoá nếu có nhập lại,.... cộng lại thiết lập giá trị cho biến thuế

        return (long) (super.getThue()*super.getGiaBan());
    }

    @Override
    public String toString() {
        return utils.HienThi.canLe(super.toString() + " | Ngày sản xuất: " + ngaySanXuat + " | Hạn sử dụng: " + hanSuDung + " | Môi trường bảo quản: " + baoQuan + " | Nhiệt độ bảo quản : " + nhietDoBaoQuan);
    }

    // ================= ĐỌC / GHI FILE TXT =================

    @Override public String toDongFile() {
        kiemTraDuLieuFile();
        return "THUCPHAM;" + thongTinSanPhamFile() + ";" + DongDuLieu.ghep(ngaySanXuat, hanSuDung, nhietDoBaoQuan, baoQuan);
    }

    public static ThucPham tuDongFile(String dong) {
        String[] p = dong.split(";", -1);
        DongDuLieu.yeuCau(p.length == 14 && "THUCPHAM".equals(p[0]), "Sai dòng THUCPHAM (cần 14 cột).");
        int dem = SanPham.getSoThuTu();
        ThucPham obj = new ThucPham();
        SanPham.datBoDemFile(dem); // Constructor đọc file không cấp mã mới.
        obj.docThongTinSanPhamFile(p);
        obj.ngaySanXuat = DongDuLieu.ngay(p[10]);
        obj.hanSuDung = DongDuLieu.ngay(p[11]);
        obj.nhietDoBaoQuan = Double.parseDouble(p[12]);
        obj.baoQuan = p[13];
        obj.kiemTraDuLieuFile();
        obj.capNhatMaFile();
        return obj;
    }

    public void kiemTraDuLieuFile() {
        super.kiemTraDuLieuFile();
        DongDuLieu.yeuCau(Double.isFinite(nhietDoBaoQuan), "Nhiệt độ không hữu hạn.");
        DongDuLieu.thuTuNgay(ngaySanXuat, hanSuDung);
    }
}
