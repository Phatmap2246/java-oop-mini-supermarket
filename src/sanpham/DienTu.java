package sanpham;

import interfaces.TrongFile;
import utils.DataBase;
import java.util.Scanner;
public class DienTu extends SanPham implements TrongFile{
    int soThangBaoHanh;
    public DienTu(){
        super();
    };
    public DienTu(String maSanPham,String tenSanPham, String maNhaCungCap, long giaNhap, long giaBan, int soLuong, int soThangBaoHanh) {
        super(maSanPham, "DienTu", tenSanPham, maNhaCungCap, giaNhap, giaBan, soLuong);
        this.soThangBaoHanh =soThangBaoHanh;
    }
    public static DienTu taoDienTu(Scanner sc){
        return DataBase.taoObjectTuTerminal(DienTu.class, sc);
    }

    @Override
    public String getFilePath() {
        return "src/luutru/DienTu.csv";
    }
}
