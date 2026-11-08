package sanpham;

import interfaces.TrongFile;
import utils.DataBase;
import java.util.Scanner;
public class TieuDung extends SanPham implements TrongFile {
    public TieuDung(){};
    public TieuDung(String maSanPham,String tenSanPham, String maNhaCungCap, long giaNhap, long giaBan, int soLuong) {
        super(maSanPham,"tieuDung", tenSanPham, maNhaCungCap, giaNhap, giaBan, soLuong);
    }
    public static TieuDung taoTieuDung(Scanner sc){
        return DataBase.taoObjectTuTerminal(TieuDung.class, sc);
    }
    @Override
    public String getFilePath(){
       return "src/luutru/TieuDung.csv";
    }
}
