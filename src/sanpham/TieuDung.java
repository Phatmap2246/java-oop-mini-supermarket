package sanpham;

import java.util.Scanner;
public class TieuDung extends SanPham
{
    private String thuongHieu , noiSanXuat , phanLoai , luuYAnToan ;
    public TieuDung()
    {
        super();
        thuongHieu = noiSanXuat = phanLoai = luuYAnToan = "" ;
        super.setThue(0.05);
    }
    public TieuDung( String maSanPham , String tenSanPham , String loaiSanPham , long giaNhap , long giaBan , int soLuongTonKho , String maNhaCungCap , double thue ,
                     String thuongHieu , String noiSanXuat , String phanLoai , String luuYAnToan)
    {
        super( maSanPham, tenSanPham, loaiSanPham, giaNhap, giaBan, soLuongTonKho, maNhaCungCap , thue);
        this.thuongHieu = thuongHieu ;
        this.noiSanXuat = noiSanXuat ;
        this.phanLoai = phanLoai ;
        this.luuYAnToan = luuYAnToan ;
    }

    public String getNoiSanXuat() {
        return noiSanXuat;
    }

    public void setNoiSanXuat(String noiSanXuat) {
        this.noiSanXuat = noiSanXuat;
    }

    public String getThuongHieu() {
        return thuongHieu;
    }

    public String getLuuYAnToan() {
        return luuYAnToan;
    }

    public void setLuuYAnToan(String luuYAnToan) {
        this.luuYAnToan = luuYAnToan;
    }

    public String getPhanLoai() {
        return phanLoai;
    }

    public void setThuongHieu(String thuongHieu) {
        this.thuongHieu = thuongHieu;
    }

    public void setPhanLoai(String phanLoai) {
        this.phanLoai = phanLoai;
    }

    public void setSanPham(Scanner sc)
    {
        super.setSanPham(sc);
        System.out.println("Nhập thương hiệu của sản phẩm: ");
        thuongHieu = sc.nextLine() ;
        System.out.println("Nhập nơi sản xuất: ");
        noiSanXuat = sc.nextLine() ;
        System.out.println("Phân loại: ");
        phanLoai = sc.nextLine() ;
        System.out.println("Lưu ý an toàn: ");
        luuYAnToan = sc.nextLine() ;
    }

    @Override
    public long tinhThue() {
        return (long)super.getThue()*super.getGiaBan() ;
    }//

    @Override
    public String toString() {
        return super.toString() + " | Phân loại: " + phanLoai + " | Nơi sản xuất: " + noiSanXuat + " | Phân loại: " + phanLoai + " | Lưu ý an toàn: " + luuYAnToan;
    }
    public String toStringKhachHang()
    {
        return "Mã sản phẩm: " + super.getMaSanPham() + " | Tên sản phẩm: " + super.getTenSanPham() + " | Giá bán: " + super.getGiaBan() + " | Phân loại: " + phanLoai + " | Nơi sản xuất: " + noiSanXuat + " | Phân loại: " + phanLoai + " | Lưu ý an toàn: " + luuYAnToan;
    }
}
