package hoadon;

public interface IThanhToan {
    boolean thucHienThanhToan(double tongTien); // kiểm tra thực hiện thanh toán => true nếu thành công
    void inBienLai();
    String getTenPhuongThuc(); // Lấy tên phương thức thanh toán 
}
