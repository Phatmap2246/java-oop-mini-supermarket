package dichvu;
import Sanpham.Sanpham;
import utils.Validation;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

public class KhuyenMaiMuaTang extends Khuyenmai{
    private int soLuongMuaToiThieu  , soLuongTang;
    public KhuyenMaiMuaTang()
    {
        super();
        soLuongMuaToiThieu = soLuongTang = 0;
        super.setLoaiKhuyenMai("Khuyến mãi mua tặng");
    }
    public KhuyenMaiMuaTang(String maKhuyenMai , String loaiKhuyenMai , LocalDate ngayBatDau , LocalDate ngayKetThuc , long mucChiToiThieu , ArrayList<Sanpham> danhSachKhuyenMai , int soLuongMuaToiThieu , int soLuongTang)
    {
        super(maKhuyenMai, loaiKhuyenMai, ngayBatDau, ngayKetThuc, mucChiToiThieu, danhSachKhuyenMai);
        this.soLuongMuaToiThieu = soLuongMuaToiThieu ;
        this.soLuongTang = soLuongTang ;
    }

    public void setSoLuongMuaToiThieu(int soLuongMuaToiThieu) {
        this.soLuongMuaToiThieu = soLuongMuaToiThieu;
    }

    public void setSoLuongTang(int soLuongTang) {
        this.soLuongTang = soLuongTang;
    }

    public int getSoLuongMuaToiThieu() {
        return soLuongMuaToiThieu;
    }

    public int getSoLuongTang() {
        return soLuongTang;
    }
    public void setKM(Scanner sc)
    {
        super.setKM(sc);
        soLuongMuaToiThieu = Validation.inputInt(sc , "Nhập số lượng sản phẩm mua tối thiểu để nhận khuyến mãi: " ) ;
        soLuongTang = Validation.inputInt(sc , "Nhập số lượng sản phẩm tặng: ") ;
        super.setLoaiKhuyenMai("Mua " + soLuongMuaToiThieu + " tặng " + soLuongTang );
    }
}
