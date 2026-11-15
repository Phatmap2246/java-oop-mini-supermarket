package hoadon;

import utils.DongDuLieu;


import sanpham.QuanLySanPham;
import sanpham.SanPham;
import utils.Validation;

import java.util.Scanner;

public class CTHoaDonNhap extends CTHoaDon {
   public void nhapThongTin (Scanner sc , QuanLySanPham qlsp )
   {
       super.setMaSanpham(Validation.inputStringRegex(sc , "Nhập mã sản phẩm: " , Validation.MaSP_REGEX , "Mã sản phẩm không đúng định dạng vui lòng nhập lại!(VD: SP001)", SanPham.getSoThuTu()));
       SanPham sp = qlsp.timMa(super.getMaSanpham()) ;
       while ( sp == null || !sp.isTonTai())
       {
           System.out.println("Sản phẩm đang tra cứu không tồn tại vui lòng nhập lại!");
           setMaSanpham(Validation.inputStringRegex(sc , "Nhập mã sản phẩm: " , Validation.MaSP_REGEX , "Mã sản phẩm không đúng định dạng vui lòng nhập lại!(VD: SP001)", SanPham.getSoThuTu()));
           sp = qlsp.timMa(super.getMaSanpham());
       }
       super.setSoLuong(Validation.inputInt(sc , "Nhập số lương cần nhập: "));
       super.setTenSanPham(sp.getTenSanPham());
       super.setDonGia(sp.getGiaNhap());
       System.out.println("Nhập chi tiết thành công!");
   }
   public void nhapThongTin( Scanner sc , SanPham sp )
   {
       if (sp == null) {
           System.out.println("Không tìm thấy sản phẩm cần nhập!");
           return;
       }
       super.setSoLuong(Validation.inputInt(sc , "Nhập số lương cần nhập: "));
       super.setTenSanPham(sp.getTenSanPham());
       super.setDonGia(sp.getGiaNhap());
       super.setMaSanpham(sp.getMaSanPham());
       System.out.println("Nhập chi tiết thành công!");
   }
   public long thanhTien()
   {
       return super.getSoLuong()*super.getDonGia() ;
   }

    // ================= ĐỌC / GHI FILE TXT =================

    @Override public String toDongFile() {
        kiemTraDuLieuFile();
        return "CTNHAP;" + thongTinChiTietFile();
    }

    public static CTHoaDonNhap tuDongFile(String dong) {
        String[] p = dong.split(";", -1);
        DongDuLieu.yeuCau(p.length == 5 && "CTNHAP".equals(p[0]), "Sai dòng CTNHAP (cần 5 cột).");
        CTHoaDonNhap obj = new CTHoaDonNhap();
        obj.docThongTinChiTietFile(p);
        obj.kiemTraDuLieuFile();
        return obj;
    }

    public void kiemTraDuLieuFile() {
        super.kiemTraDuLieuFile();
    }
}
