package dichvu;

import utils.Validation;
import sanpham.SanPham ;
import java.util.Scanner;

import interfaces.IQuanLy;

public class DanhSachSPKM implements  IQuanLy<SanPhamKhuyenMai>
{
    private SanPhamKhuyenMai[] sanPhamKhuyenMais ;
    private int soLuongSanPham ;
    public DanhSachSPKM()
    {
        soLuongSanPham = 0 ;
        sanPhamKhuyenMais = new SanPhamKhuyenMai[soLuongSanPham] ;
    }
    public void setSanPhamKhuyenMais(SanPhamKhuyenMai[] sanPhamKhuyenMais) {
        this.sanPhamKhuyenMais = sanPhamKhuyenMais;
    }

    public SanPhamKhuyenMai[] getSanPhamKhuyenMais() {
        return sanPhamKhuyenMais;
    }

    public int getSoLuongSanPham() {
        return soLuongSanPham;
    }

    public void setSoLuongSanPham(int soLuongSanPham) {
        this.soLuongSanPham = soLuongSanPham;
    }

    public void themSanPhamKhuyenMai(Scanner sc )
    {
        System.out.println("Thêm sản phẩm khuyến mãi!");
        SanPhamKhuyenMai spkm = new SanPhamKhuyenMai() ;
        spkm.setSanPhamKhuyenMai(sc);
        while (timViTri(spkm.getMaSanPham()) != -1)
        {
            System.out.println("Bạn không thể dùng hai mã khuyến mãi trên 1 loại sản phẩm!");
            spkm.setSanPhamKhuyenMai(sc);
        }
        themSanPhamKhuyenMai(spkm);
    }

    @Override
    public void them(Scanner sc) {
        themSanPhamKhuyenMai(sc);
    }

    public void themSanPhamKhuyenMai(SanPhamKhuyenMai spkm)
    {
        int newSoLuongSanPham = soLuongSanPham + 1 ;
        SanPhamKhuyenMai[] tam = new SanPhamKhuyenMai[newSoLuongSanPham];
        System.arraycopy(sanPhamKhuyenMais, 0, tam, 0, soLuongSanPham);
        tam[soLuongSanPham] = spkm ;
        sanPhamKhuyenMais = tam ;
        soLuongSanPham = newSoLuongSanPham ;
        System.out.println("Thêm sản phẩm mới thành công");
    }
    public void taoDS(Scanner sc )
    {
        System.out.println("Khởi tạo danh sách khuyến mãi!");
        do {
            themSanPhamKhuyenMai(sc);
        } while (Validation.inputCoKhong(sc, "Bạn có muốn tiếp tục thêm sản phẩm khuyến mãi(Có/Không): "));
        System.out.println("Tạo dữ liệu sản phẩm khuyến mãi thành công!");
    }

    @Override
    public void taoDanhSach(Scanner sc) {
        taoDS(sc);
    }

    public void xoaSanPhamKM( String maSanPham )
    {
        int index = timViTri(maSanPham);
        if ( index == -1 )
        {
            System.out.println("Mã sản phẩm không tồn tại");
            return;
        }
        int newSoLuongSanPham = soLuongSanPham - 1 ;
        SanPhamKhuyenMai[] tam = new SanPhamKhuyenMai[newSoLuongSanPham] ;
        if (index >= 0) System.arraycopy(sanPhamKhuyenMais, 0, tam, 0, index);
        if (soLuongSanPham - (index + 1) >= 0)
            System.arraycopy(sanPhamKhuyenMais, index + 1, tam, index, soLuongSanPham - (index + 1));
        soLuongSanPham = newSoLuongSanPham ;
        sanPhamKhuyenMais = tam ;
        System.out.println("Xoá sản phẩm khuyến mãi thành công!");
    }

    @Override
    public boolean xoa(String ma) {
        if (timViTri(ma) == -1) {
            System.out.println("Mã sản phẩm không tồn tại");
            return false;
        }
        xoaSanPhamKM(ma);
        return true;
    }
    public void suaThongTinSanPhamKhuyenMai( Scanner sc , String maSanPham)
    {
        int index = timViTri(maSanPham);
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
                sanPhamKhuyenMais[index].setMaSanPham(Validation.inputStringRegex( sc , "Nhập mã sản phẩm mới: " , "SP\\d{3}" , "Mã sản phẩm không hợp lệ vui lòng nhập lại!(ví dụ hợp lệ: SP001)", SanPham.getSoThuTu()));
                while (kiemTraSanPhamTrung(sanPhamKhuyenMais[index].getMaSanPham()) )
                {
                    System.out.println("Bạn không thể dùng hai mã khuyến mãi trên 1 loại sản phẩm vui lòng nhập lại!");
                    sanPhamKhuyenMais[index].setMaSanPham(Validation.inputStringRegex( sc , "Nhập mã sản phẩm mới: " , "SP\\d{3}" , "Mã sản phẩm không hợp lệ vui lòng nhập lại!(ví dụ hợp lệ: SP001)", SanPham.getSoThuTu()));
                }
                break;
            case 2:
                sanPhamKhuyenMais[index].setMaKhuyenMai(Validation.inputStringRegex(sc , "Nhập mã khuyến mãi mới: " , "KM\\d{3}" , "Mã khuyến mãi sai định dạng vui lòng nhập lại!(ví dụ hợp lệ: KM001)", dichvu.Khuyenmai.getSoThuTu()));
                break;
            default:
                System.out.println("Lựa chọn không hợp lệ!");
                return;
        }
        System.out.println("Sửa thông tin sản phẩm khuyến mãi thành công!");
    }

    @Override
    public boolean sua(String ma, Scanner sc) {
        if (timViTri(ma) == -1) {
            System.out.println("Mã sản phẩm không tồn tại");
            return false;
        }
        suaThongTinSanPhamKhuyenMai(sc, ma);
        return true;
    }

    public boolean kiemTraSanPhamTrung (String maSanPham )
    {
        int dem = 0 ;
        for ( SanPhamKhuyenMai spkm:sanPhamKhuyenMais )
            if ( spkm.getMaSanPham().equalsIgnoreCase(maSanPham)) dem++ ;
        return dem > 1 ;
    }
    @Override 
    public void xuatDanhSach()
    {
        System.out.println("Danh sách sản phẩm khuyến mãi!");
        for ( SanPhamKhuyenMai spkm: sanPhamKhuyenMais )
            System.out.println(spkm);
    }


    @Override
    public int timViTri(String ma) {
        for ( int i = 0 ; i < soLuongSanPham ; i++ )
        {
            if (sanPhamKhuyenMais[i].getMaSanPham().equalsIgnoreCase(ma)) return i;
        }
        return -1 ;
    }

    @Override
    public SanPhamKhuyenMai timMa(String ma) {
        int index = timViTri(ma);
        return index == -1 ? null : sanPhamKhuyenMais[index];
    }

    @Override
    public SanPhamKhuyenMai timKhoa(String ma) {
        return timMa(ma);
    }

    @Override
    public SanPhamKhuyenMai timTen(String ten) {
        System.out.println("Ham nay khon sua dung");
        return  null; 
    }
}
