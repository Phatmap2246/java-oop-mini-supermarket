package hoadon;

import java.util.Scanner;

import interfaces.IThanhToan;
import utils.Validation;
import interfaces.TrongFile;
public class ThanhToanTienMat implements IThanhToan, TrongFile {
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
    @Override
    public String getFilePath(){
        return "src/luutru/ThanhToanTienMat.csv";
    }
    public long getTienKhachDua() { return tienKhachDua; }
    public long getTienThua() { return tienThua; }
}
