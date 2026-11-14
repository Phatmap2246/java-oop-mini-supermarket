package dichvu;

import interfaces.IThongTin;
import sanpham.QuanLySanPham;
import utils.Validation;

import java.util.Scanner;

public class SanPhamKhuyenMai implements IThongTin {
    private boolean tonTai = true;

    public boolean isTonTai() { return tonTai; }

    public void setTonTai(boolean tonTai) { this.tonTai = tonTai; }

    private String maSanPham, maKhuyenMai;

    public SanPhamKhuyenMai() {
        maKhuyenMai = maSanPham = "";
    }

    public SanPhamKhuyenMai(String maSanPham, String maKhuyenMai) {
        this.maKhuyenMai = maKhuyenMai;
        this.maSanPham = maSanPham;
    }

    public String getMaKhuyenMai() {
        return maKhuyenMai;
    }

    public String getMaSanPham() {
        return maSanPham;
    }

    public void setMaKhuyenMai(String maKhuyenMai) {
        if (maKhuyenMai == null || !maKhuyenMai.matches("(?i)KM\\d+")) {
            throw new IllegalArgumentException("MaKhuyenMai không đúng định dạng.");
        }
        this.maKhuyenMai = maKhuyenMai;
    }

    public void setMaSanPham(String maSanPham) {
        if (maSanPham == null || !maSanPham.matches("(?i)SP\\d+")) {
            throw new IllegalArgumentException("MaSanPham không đúng định dạng.");
        }
        this.maSanPham = maSanPham;
    }

    public void setSanPhamKhuyenMai(Scanner sc) {
        maSanPham = Validation.inputStringRegex(sc, "Nhập mã sản phẩm: ", Validation.MaSP_REGEX,
                "Mã sản phẩm không đúng định dạng vui lòng nhập lại!(ví dụ hợp lệ: SP001)", sanpham.SanPham.getSoThuTu());
        maKhuyenMai = Validation.inputStringRegex(sc, "Nhập mã khuyến mãi: ", Validation.MaKM_REGEX,
                "Mã khuyến mãi không đúng định dạng vui lòng nhập lại!(ví dụ hợp lệ: KM001)", dichvu.Khuyenmai.getSoThuTu());
    }

    public void setSanPhamKhuyenMai(Scanner sc, QuanLySanPham qlsp, Danhsachkhuyenmai dskm) {
        while (true) {
            String maSP = Validation.inputStringRegex(sc, "Nhập mã sản phẩm: ", Validation.MaSP_REGEX,
                    "Mã sản phẩm không đúng định dạng (VD: SP001)!");
            if (qlsp != null && qlsp.traCuuSanPham(maSP) == null) {
                System.out.println("[!] Mã sản phẩm không tồn tại trong danh mục!");
                continue;
            }
            this.maSanPham = maSP;
            break;
        }
        while (true) {
            String maKM = Validation.inputStringRegex(sc, "Nhập mã khuyến mãi: ", Validation.MaKM_REGEX,
                    "Mã khuyến mãi không đúng định dạng (VD: KM001)!");
            if (dskm != null && dskm.traCuuKhuyenMai(maKM) == null) {
                System.out.println("[!] Mã khuyến mãi không tồn tại trong danh sách!");
                continue;
            }
            this.maKhuyenMai = maKM;
            break;
        }
    }

    @Override
    public void nhapThongTin(Scanner sc) {
        setSanPhamKhuyenMai(sc);
    }

    @Override
    public String toString() {
        return "Mã sản phẩm: " + maSanPham + " | Mã khuyến mãi: " + maKhuyenMai;
    }
}
