package dichvu;

import utils.DongDuLieu;
import interfaces.TrongFile;
import sanpham.SanPham;
import utils.Validation;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

public class KhuyenMaiMuaTang extends Khuyenmai implements TrongFile {
    private int soLuongMuaToiThieu  , soLuongTang;
    public KhuyenMaiMuaTang() {
        super("Khuyến mãi mua tặng");
        soLuongMuaToiThieu = soLuongTang = 0;
    }
    public KhuyenMaiMuaTang(String maKhuyenMai, String loaiKhuyenMai, LocalDate ngayBatDau, LocalDate ngayKetThuc, long mucChiToiThieu, ArrayList<SanPham> danhSachKhuyenMai, int soLuongMuaToiThieu, int soLuongTang, int soThuTu) {
        super(maKhuyenMai, loaiKhuyenMai, ngayBatDau, ngayKetThuc, mucChiToiThieu, soThuTu);
        if (soLuongMuaToiThieu < 0 || soLuongTang < 0) {
            throw new IllegalArgumentException("Số lượng mua và tặng không được âm!");
        }
        this.soLuongMuaToiThieu = soLuongMuaToiThieu;
        this.soLuongTang = soLuongTang;
    }

    public void setSoLuongMuaToiThieu(int soLuongMuaToiThieu) {
        if ( soLuongMuaToiThieu <= 0 )
            throw new IllegalArgumentException("Số lượng sản phẩm mua phải lớn hơn 0!") ;
        this.soLuongMuaToiThieu = soLuongMuaToiThieu;
    }

    public void setSoLuongTang(int soLuongTang) {
        if ( soLuongTang <= 0 )
            throw new IllegalArgumentException("Số lượng sản phẩm tặng phải lớn hơn 0!") ;
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
        while(true)
        {
            try {
                setSoLuongMuaToiThieu(Validation.inputInt(sc , "Nhập số lượng sản phẩm mua tối thiểu để nhận khuyến mãi: " )) ;
                break;
            }
            catch ( IllegalArgumentException e )
            {
                System.out.println(e.getMessage());
            }
        }
        while(true)
        {
            try {
                setSoLuongTang(Validation.inputInt(sc , "Nhập số lượng sản phẩm tặng: ")) ;
                break;
            }
            catch ( IllegalArgumentException e )
            {
                System.out.println(e.getMessage());
            }
        }super.setLoaiKhuyenMai("Mua " + soLuongMuaToiThieu + " tặng " + soLuongTang );
    }


    public int tinhSoLuongTang(int soLuong) {
        if (!dangHoatDong(LocalDate.now()) || soLuong <= 0 || soLuongMuaToiThieu <= 0) {
            return 0;
        }
        long cum = (long) soLuongMuaToiThieu + soLuongTang;
        return (int) ((soLuong / cum) * soLuongTang);
    }

    public long tinhTienGiam(long donGia, int soLuong) {
        long tong = donGia * soLuong;
        if (!dangHoatDong(LocalDate.now()) || tong < getMucChiToiThieu()) {
            return 0;
        }
        return tinhSoLuongTang(soLuong) * donGia;
    }

    // ================= ĐỌC / GHI FILE TXT =================

    @Override
    public String getFilePath(){
       return utils.DuongDanLuuTru.file("KhuyenMaiMuaTang.txt");
    }

    @Override public String toDongFile() {
        kiemTraDuLieuFile();
        return "KMMUATANG;" + thongTinKhuyenMaiFile() + ";" + DongDuLieu.ghep(soLuongMuaToiThieu, soLuongTang);
    }

    public static KhuyenMaiMuaTang tuDongFile(String dong) {
        String[] p = dong.split(";", -1);
        DongDuLieu.yeuCau(p.length == 9 && "KMMUATANG".equals(p[0]), "Sai dòng KMMUATANG (cần 9 cột).");
        int dem = Khuyenmai.getSoThuTu();
        KhuyenMaiMuaTang obj = new KhuyenMaiMuaTang();
        Khuyenmai.datBoDemFile(dem); // Constructor đọc file không cấp mã mới.
        obj.docThongTinKhuyenMaiFile(p);
        obj.soLuongMuaToiThieu = Integer.parseInt(p[7]);
        obj.soLuongTang = Integer.parseInt(p[8]);
        obj.kiemTraDuLieuFile();
        obj.capNhatMaFile();
        return obj;
    }

    public void kiemTraDuLieuFile() {
        super.kiemTraDuLieuFile();
        DongDuLieu.khongAm(soLuongMuaToiThieu, "soLuongMuaToiThieu");
        DongDuLieu.khongAm(soLuongTang, "soLuongTang");
    }
}
