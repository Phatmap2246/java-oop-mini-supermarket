package sanpham;

import interfaces.TrongFile;
import utils.DataBase;
import java.time.LocalDate;
import java.util.Scanner;
public class ThucPham extends SanPham implements TrongFile {

    public LocalDate hanSuDung;
    public ThucPham(){};
    public ThucPham(String maSanPham, String tenSanPham, String maNhaCungCap, long giaNhap, long giaBan, int soLuong, LocalDate hanSuDung) {
        super(maSanPham, "thucPham", tenSanPham, maNhaCungCap, giaNhap, giaBan, soLuong);
        this.hanSuDung = hanSuDung;
    }

    public LocalDate getHanSuDung() {
        return hanSuDung;
    }

    public void setHanSuDung(LocalDate hanSuDung) {
        this.hanSuDung = hanSuDung;
    }
    public static ThucPham taoThucPham(Scanner sc){
       return DataBase.taoObjectTuTerminal(ThucPham.class,sc);
    }

    @Override
    public String getFilePath() {
        return "src/luutru/ThucPham.csv";
    }
}
