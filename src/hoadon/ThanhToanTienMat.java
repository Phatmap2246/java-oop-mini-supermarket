package hoadon;

import java.util.Scanner;
import utils.Validation;

public class ThanhToanTienMat implements IThanhToan {
    private double tienKhachDua;
    private double tienThua;

    public ThanhToanTienMat() {}

    public ThanhToanTienMat(double tienKhachDua, double tienThua){
        this.tienKhachDua = tienKhachDua;
        this.tienThua = tienThua;
    }

    // Ghi đè Interface
    @Override 
    public boolean thucHienThanhToan(Scanner sc, double tongTien){
        this.tienKhachDua = Validation.inputDouble(sc, "Nhap so tien khach dua: ");
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
        System.out.printf(" - Tong hoa don: %.2f VND\n", (tienKhachDua - tienThua)); // Tính ngược lại tổng tiền
        System.out.printf(" - Khach dua: %.2f VND\n", tienKhachDua);
        System.out.printf(" - Tien thua: %.2f VND\n", tienThua);
        System.out.println(" - Ghi chu: Da kiem tra du tien va da thoi lai cho khach.");
    }

    public double getTienKhachDua() { return tienKhachDua; }
    public double getTienThua() { return tienThua; }
}
