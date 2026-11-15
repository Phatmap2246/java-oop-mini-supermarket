package hoadon;

import utils.DongDuLieu;

import java.util.Scanner;
import interfaces.TrongFile ;
import interfaces.IThanhToan;
import utils.Validation;

public class ThanhToanTienMat implements IThanhToan, TrongFile, interfaces.BanGhiFile {
    private long tienKhachDua;
    private long tienThua;

    public ThanhToanTienMat() {}

    public ThanhToanTienMat(long tienKhachDua, long tienThua){
        this.tienKhachDua = tienKhachDua;
        this.tienThua = tienThua;
    }

    // Ghi đè Interface
    @Override 
    public boolean thucHienThanhToan(Scanner sc, long tongTien){
        this.tienKhachDua = Validation.inputLong(sc, "Nhap so tien khach dua: ");
        if (this.tienKhachDua >= tongTien) {
            this.tienThua = this.tienKhachDua - tongTien;
            return true;
        }
        return false;
    }

    @Override 
    public String getTenPhuongThuc(){return "Tien Mat";}

    @Override
    public void inBienLai() {
        System.out.println(" - Hinh thuc: Tien mat");
        System.out.printf(" - Tong hoa don: %d VND\n", (tienKhachDua - tienThua)); 
        System.out.printf(" - Khach dua: %d VND\n", tienKhachDua);
        System.out.printf(" - Tien thua: %d VND\n", tienThua);
        System.out.println(" - Ghi chu: Da kiem tra du tien va da thoi lai cho khach.");
    }

    public long getTienKhachDua() { return tienKhachDua; }
    public long getTienThua() { return tienThua; }

    // ================= ĐỌC / GHI FILE TXT =================

    @Override
    public String getFilePath(){
        return utils.DuongDanLuuTru.file("ThanhToanTienMat.txt");
    }

    @Override public String toDongFile() {
        kiemTraDuLieuFile();
        return DongDuLieu.ghep("TIENMAT", tienKhachDua, tienThua);
    }

    public static ThanhToanTienMat tuDongFile(String dong) {
        String[] p = dong.split(";", -1);
        DongDuLieu.yeuCau(p.length == 3 && "TIENMAT".equals(p[0]), "Sai dòng TIENMAT (cần 3 cột).");
        ThanhToanTienMat obj = new ThanhToanTienMat();
        obj.tienKhachDua = Long.parseLong(p[1]);
        obj.tienThua = Long.parseLong(p[2]);
        obj.kiemTraDuLieuFile();
        return obj;
    }

    public void kiemTraDuLieuFile() {
        DongDuLieu.khongAm(tienKhachDua, "tienKhachDua");
        DongDuLieu.khongAm(tienThua, "tienThua");
    }
}
