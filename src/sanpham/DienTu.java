package sanpham;

import utils.Validation;

import java.time.LocalDate;
import java.util.Scanner;
public class DienTu extends SanPham
{
    private int thoiGianBaoHanh ;
    private String chatLieu ;
    public DienTu()
    {
        super();
        thoiGianBaoHanh = 0 ;
        chatLieu = "" ;
        super.setThue(0.05) ;
    }
    public DienTu( String maSanPham , String tenSanPham , String loaiSanPham , long giaNhap , long giaBan , int soLuongTonKho , String maNhaCungCap , double thue ,
                   int thoiGianBaoHanh , long tienBaoHanh , String chatLieu , LocalDate ngayMua)
    {
        super( maSanPham, tenSanPham, loaiSanPham, giaNhap, giaBan, soLuongTonKho, maNhaCungCap , thue );
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
        return 0;
    }

    @Override
    public String toString() {
        return super.toString() + " | Chất liệu: " + chatLieu + " | Thời gian bảo hành: " + thoiGianBaoHanh ;
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
}
