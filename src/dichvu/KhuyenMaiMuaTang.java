package dichvu;

import interfaces.TrongFile;
import sanpham.SanPham;
import utils.Validation;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

public class KhuyenMaiMuaTang extends Khuyenmai implements TrongFile {
    private int soLuongMuaToiThieu, soLuongTang;

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
        if (soLuongMuaToiThieu <= 0)
            throw new IllegalArgumentException("Số lượng sản phẩm mua phải lớn hơn 0!");
        this.soLuongMuaToiThieu = soLuongMuaToiThieu;
    }

    public void setSoLuongTang(int soLuongTang) {
        if (soLuongTang <= 0)
            throw new IllegalArgumentException("Số lượng sản phẩm tặng phải lớn hơn 0!");
        this.soLuongTang = soLuongTang;
    }

    public int getSoLuongMuaToiThieu() {
        return soLuongMuaToiThieu;
    }

    public int getSoLuongTang() {
        return soLuongTang;
    }

    @Override
    public int tinhSoLuongTang(int soLuong) {
        if (!dangHoatDong(LocalDate.now()) || soLuongMuaToiThieu <= 0) {
            return 0;
        }
        int cum = soLuongMuaToiThieu + soLuongTang;
        return (soLuong / cum) * soLuongTang;
    }

    @Override
    public long tinhTienGiam(long donGia, int soLuong) {
        long tong = donGia * soLuong;
        if (!dangHoatDong(LocalDate.now()) || tong < getMucChiToiThieu()) {
            return 0;
        }
        return tinhSoLuongTang(soLuong) * donGia;
    }

    @Override
    public void setKM(Scanner sc) {
        super.setKM(sc);
        while (true) {
            try {
                setSoLuongMuaToiThieu(Validation.inputInt(sc, "Nhập số lượng sản phẩm mua tối thiểu để nhận khuyến mãi: "));
                break;
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
        while (true) {
            try {
                setSoLuongTang(Validation.inputInt(sc, "Nhập số lượng sản phẩm tặng: "));
                break;
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
        super.setLoaiKhuyenMai("Mua " + soLuongMuaToiThieu + " tặng " + soLuongTang);
    }

    @Override
    public String getFilePath() {
        return "src/luutru/KhuyenMaiMuaTang.csv";
    }
}
