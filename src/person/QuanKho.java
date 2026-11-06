package person;

import dichvu.Danhsachdoitac;
import hoadon.QuanLyHoaDon;
import java.util.Scanner;

public class QuanKho extends NhanVien {
    public boolean lapHoaDonNhapHang(Scanner sc, QuanLyHoaDon quanLyHoaDon,
            Danhsachdoitac danhSachDoiTac) {
        return quanLyHoaDon.taoHoaDonNhapHang(sc, danhSachDoiTac);
    }
}
