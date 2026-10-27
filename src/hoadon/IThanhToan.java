package hoadon;

import java.util.Scanner;
public interface IThanhToan {
    boolean thucHienThanhToan(Scanner sc,double tongTien); // kiểm tra thực hiện thanh toán => true nếu thành công
    void inBienLai();
    String getTenPhuongThuc(); // Lấy tên phương thức thanh toán 
}
