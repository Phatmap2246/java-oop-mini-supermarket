package dichvu;

import utils.Validation;

import java.util.ArrayList;
import java.util.Scanner;

public class DanhSachSPKM {
    private ArrayList<SanPhamKhuyenMai> sanPhamKhuyenMais ;
    public DanhSachSPKM()
    {
        sanPhamKhuyenMais = new ArrayList<>() ;
    }

    public ArrayList<SanPhamKhuyenMai> getSanPhamKhuyenMais() {
        return sanPhamKhuyenMais;
    }

    public void setSanPhamKhuyenMais(ArrayList<SanPhamKhuyenMai> sanPhamKhuyenMais) {
        this.sanPhamKhuyenMais = sanPhamKhuyenMais;
    }
    public void themSanPhamKhuyenMai(Scanner sc)
    {
        System.out.println("Thêm sản phẩm khuyến mãi!");
        SanPhamKhuyenMai spkm = new SanPhamKhuyenMai() ;
        spkm.setSanPhamKhuyenMai(sc);
        while ( timKiemSanPhamKM(spkm.getMaSanPham()) != -1 )
        {
            System.out.println("Bạn không thể dùng hai mã khuyến mãi trên 1 loại sản phẩm!");
            spkm.setSanPhamKhuyenMai(sc);
        }
        sanPhamKhuyenMais.add(spkm) ;
    }
    public void khoiTaoDanhSachSanPhamKhuyenMai(Scanner sc )
    {
        System.out.println("Khởi tạo danh sách khuyến mãi!");
        do {
            themSanPhamKhuyenMai(sc);
        } while (Validation.inputCoKhong(sc, "Bạn có muốn tiếp tục thêm sản phẩm khuyến mãi(Có/Không): "));
        System.out.println("Tạo dữ liệu sản phẩm khuyến mãi thành công!");
    }
    public int timKiemSanPhamKM( String maSanPham )
    {
        for ( int i = 0 ; i < sanPhamKhuyenMais.size() ; i++ )
        {
            if ( sanPhamKhuyenMais.get(i).getMaSanPham().equalsIgnoreCase(maSanPham)) return i ;
        }
        return -1 ;
    }
    public void xoaSanPhamKM( String maSanPham )
    {
        int index = timKiemSanPhamKM(maSanPham) ;
        if ( index == -1 )
        {
            System.out.println("Mã sản phẩm không tồn tại");
            return;
        }
        sanPhamKhuyenMais.remove(index) ;
        System.out.println("Xoá sản phẩm khuyến mãi thành công!");
    }
    public void suaThongTinSanPhamKhuyenMai( Scanner sc , String maSanPham)
    {
        int index = timKiemSanPhamKM(maSanPham) ;
        if ( index == -1 )
        {
            System.out.println("Mã sản phẩm không tồn tại");
            return;
        }
        System.out.println("Sửa thông tin sản phẩm khuyến mãi!");
        System.out.println("1. Sửa mã sản phẩm!");
        System.out.println("2. Sửa mã khuyến mãi!");
        int luaChon = Validation.inputInt( sc , "Nhập lựa chọn của bạn!") ;
        switch (luaChon)
        {
            case 1:
                sanPhamKhuyenMais.get(index).setMaSanPham(Validation.inputStringRegex( sc , "Nhập mã sản phẩm mới: " , "SP\\d{3}" , "Mã sản phẩm không hợp lệ vui lòng nhập lại!(ví dụ hợp lệ: SP001)"));
                while (kiemTraSanPhamTrung(sanPhamKhuyenMais.get(index).getMaKhuyenMai()) )
                {
                    System.out.println("Bạn không thể dùng hai mã khuyến mãi trên 1 loại sản phẩm vui lòng nhập lại!");
                    sanPhamKhuyenMais.get(index).setMaSanPham(Validation.inputStringRegex( sc , "Nhập mã sản phẩm mới: " , "SP\\d{3}" , "Mã sản phẩm không hợp lệ vui lòng nhập lại!(ví dụ hợp lệ: SP001)"));
                }
                break;
            case 2:
                sanPhamKhuyenMais.get(index).setMaKhuyenMai(Validation.inputStringRegex(sc , "Nhập mã khuyến mãi mới: " , "KM\\d{3}" , "Mã khuyến mãi sai định dạng vui lòng nhập lại!(ví dụ hợp lệ: KM001)"));
                break;
            default:
                System.out.println("Lựa chọn không hợp lệ!");
                return;
        }
        System.out.println("Sửa thông tin sản phẩm khuyến mãi thành công!");
    }
    public boolean kiemTraSanPhamTrung (String maSanPham )
    {
        int dem = 0 ;
        for ( SanPhamKhuyenMai spkm:sanPhamKhuyenMais )
            if ( spkm.getMaSanPham().equalsIgnoreCase(maSanPham)) dem++ ;
        return dem > 1 ;
    }
    public void inDanhSachSanPhamKM()
    {
        System.out.println("Danh sách sản phẩm khuyến mãi!");
        for ( SanPhamKhuyenMai spkm: sanPhamKhuyenMais )
            System.out.println(spkm);
    }
}
