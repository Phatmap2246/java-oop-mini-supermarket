package sanpham;

import utils.DongDuLieu;

import utils.Validation;

import java.time.LocalDate;
import java.util.Scanner;
public class DienTu extends SanPham
{
    private int thoiGianBaoHanh ;
    private String chatLieu ;
    public DienTu()
    {
        super(0.05, "Điện tử");
        thoiGianBaoHanh = 0 ;
        chatLieu = "" ;
    }
    public DienTu( String maSanPham , String tenSanPham , String loaiSanPham , long giaNhap , long giaBan , int soLuongTonKho , String maNhaCungCap , double thue ,
                   int thoiGianBaoHanh , long tienBaoHanh , String chatLieu , LocalDate ngayMua)
    {
        super( maSanPham, tenSanPham, loaiSanPham, giaNhap, giaBan, soLuongTonKho, maNhaCungCap , thue );
        if (thoiGianBaoHanh < 0) throw new IllegalArgumentException("ThoiGianBaoHanh không được âm.");
        this.thoiGianBaoHanh = thoiGianBaoHanh ;
        this.chatLieu = chatLieu ;
    }

    public void setThoiGianBaoHanh(int thoiGianBaoHanh) {
        if (thoiGianBaoHanh < 0) {
            throw new IllegalArgumentException("ThoiGianBaoHanh không được âm.");
        }
        this.thoiGianBaoHanh = thoiGianBaoHanh;
    }

    public int getThoiGianBaoHanh() {
        return thoiGianBaoHanh;
    }

    public String getChatLieu() {
        return chatLieu;
    }

    public void setChatLieu(String chatLieu) {
        if (chatLieu == null || chatLieu.isBlank()) {
            throw new IllegalArgumentException("ChatLieu không được để trống.");
        }
        this.chatLieu = chatLieu;
    }

    public void setSanPham(Scanner sc)
    {
        super.setSanPham(sc);
        System.out.println("Nhập chất liệu: ");
        chatLieu = Validation.inputStringRegex(sc, "Nhập thông tin: ", "(?s).+", "Thông tin không được để trống!");
        thoiGianBaoHanh = Validation.inputInt(sc , "Thời gian bảo hành(theo tháng): ") ;
    }


    @Override
    public long tinhThue() {
        return (long) (super.getThue() * super.getGiaBan());
    }

    @Override
    public String toString() {
        return utils.HienThi.canLe(super.toString() + " | Chất liệu: " + chatLieu + " | Thời gian bảo hành: " + thoiGianBaoHanh );
    }
    public LocalDate ngayHetHanBaoHanh( LocalDate ngayMua )
    {
        if (ngayMua == null) throw new IllegalArgumentException("Ngày mua không được null.");
        return ngayMua.plusMonths(thoiGianBaoHanh) ;
    }
    public boolean kiemTraBaoHanh ( LocalDate ngayMua)
    {
        return LocalDate.now().isAfter(ngayHetHanBaoHanh(ngayMua)) ;
    }

    // ================= ĐỌC / GHI FILE TXT =================

    @Override public String toDongFile() {
        kiemTraDuLieuFile();
        return "DIENTU;" + thongTinSanPhamFile() + ";" + DongDuLieu.ghep(thoiGianBaoHanh, chatLieu);
    }

    public static DienTu tuDongFile(String dong) {
        String[] p = dong.split(";", -1);
        DongDuLieu.yeuCau(p.length == 12 && "DIENTU".equals(p[0]), "Sai dòng DIENTU (cần 12 cột).");
        int dem = SanPham.getSoThuTu();
        DienTu obj = new DienTu();
        SanPham.datBoDemFile(dem); // Constructor đọc file không cấp mã mới.
        obj.docThongTinSanPhamFile(p);
        obj.thoiGianBaoHanh = Integer.parseInt(p[10]);
        obj.chatLieu = p[11];
        obj.kiemTraDuLieuFile();
        obj.capNhatMaFile();
        return obj;
    }

    public void kiemTraDuLieuFile() {
        super.kiemTraDuLieuFile();
        DongDuLieu.khongAm(thoiGianBaoHanh, "thoiGianBaoHanh");
    }
}
