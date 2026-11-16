import hoadon.ThanhToanTienMat;
import java.util.Scanner;

public class ThanhToanTienMatNhapLaiTest {
    public static void main(String[] args) {
        ThanhToanTienMat tt = new ThanhToanTienMat();
        if (!tt.thucHienThanhToan(new Scanner("9\nabc\n-1\n150000\n"), 100000)
                || tt.getTienKhachDua() != 150000 || tt.getTienThua() != 50000)
            throw new AssertionError("Retry and change");
        if (tt.thucHienThanhToan(new Scanner("9\n0\n"), 100000) || tt.getTienThua() != 0)
            throw new AssertionError("Cancel and reset");
        if (!tt.thucHienThanhToan(new Scanner("100000\n"), 100000) || tt.getTienThua() != 0)
            throw new AssertionError("Exact payment");
        System.out.println("PASS: insufficient, invalid, negative, cancel, exact and excess payment.");
    }
}
