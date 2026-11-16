package sanpham;

import utils.DongDuLieu;

import java.util.Scanner;
import utils.Validation;
public class TieuDung extends SanPham
{
    private String thuongHieu , noiSanXuat , phanLoai , luuYAnToan ;
    public TieuDung()
    {
        super(0.05, "Tiêu dùng");
        thuongHieu = noiSanXuat = phanLoai = luuYAnToan = "" ;
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
        if (noiSanXuat == null || noiSanXuat.isBlank()) {
            throw new IllegalArgumentException("NoiSanXuat không được để trống.");
        }
        this.noiSanXuat = noiSanXuat;
    }

    public String getThuongHieu() {
        return thuongHieu;
    }

    public String getLuuYAnToan() {
        return luuYAnToan;
    }

    public void setLuuYAnToan(String luuYAnToan) {
        if (luuYAnToan == null || luuYAnToan.isBlank()) {
            throw new IllegalArgumentException("LuuYAnToan không được để trống.");
        }
        this.luuYAnToan = luuYAnToan;
    }

    public String getPhanLoai() {
        return phanLoai;
    }

    public void setThuongHieu(String thuongHieu) {
        if (thuongHieu == null || thuongHieu.isBlank()) {
            throw new IllegalArgumentException("ThuongHieu không được để trống.");
        }
        this.thuongHieu = thuongHieu;
    }

    public void setPhanLoai(String phanLoai) {
        if (phanLoai == null || phanLoai.isBlank()) {
            throw new IllegalArgumentException("PhanLoai không được để trống.");
        }
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
        return (long) (super.getThue() * super.getGiaBan()) ;
    }//

    @Override
    public String toString() {
        return utils.HienThi.canLe("Mã sản phẩm: " + super.getMaSanPham() + " | Tên sản phẩm: " + super.getTenSanPham() + " | Giá bán: " + super.getGiaBan() + " | Thương hiệu: " + thuongHieu + " | Nơi sản xuất: " + noiSanXuat + " | Phân loại: " + phanLoai + " | Lưu ý an toàn: " + luuYAnToan);
    }

    // ================= ĐỌC / GHI FILE TXT =================

    @Override public String toDongFile() {
        kiemTraDuLieuFile();
        return "TIEUDUNG;" + thongTinSanPhamFile() + ";" + DongDuLieu.ghep(thuongHieu, noiSanXuat, phanLoai, luuYAnToan);
    }

    public static TieuDung tuDongFile(String dong) {
        String[] p = dong.split(";", -1);
        DongDuLieu.yeuCau(p.length == 14 && "TIEUDUNG".equals(p[0]), "Sai dòng TIEUDUNG (cần 14 cột).");
        int dem = SanPham.getSoThuTu();
        TieuDung obj = new TieuDung();
        SanPham.datBoDemFile(dem); // Constructor đọc file không cấp mã mới.
        obj.docThongTinSanPhamFile(p);
        obj.thuongHieu = p[10];
        obj.noiSanXuat = p[11];
        obj.phanLoai = p[12];
        obj.luuYAnToan = p[13];
        obj.kiemTraDuLieuFile();
        obj.capNhatMaFile();
        return obj;
    }

    public void kiemTraDuLieuFile() {
        super.kiemTraDuLieuFile();
    }
}
