package hoadon;

import java.time.LocalDate;
import java.util.Scanner;

import dichvu.*;
import sanpham.QuanLySanPham;
import sanpham.SanPham;
import sanpham.ThucPham;
import utils.Validation;

public class CTHoaDonBan extends CTHoaDon {
    private boolean tonTai = true;

    public boolean isTonTai() { return tonTai; }

    public void setTonTai(boolean tonTai) { this.tonTai = tonTai; }

    private long thanhTienSauKhuyenMai;
    private int soLuongTangKem;
    private int soLuongPhaiTra;
    private long soTienGiam;
    private String loaiKhuyenMai;

    public CTHoaDonBan() {
        super();
        this.thanhTienSauKhuyenMai = 0;
        this.soTienGiam = 0;
        this.soLuongTangKem = 0;
        this.soLuongPhaiTra = 0;
        this.loaiKhuyenMai = "Không áp dụng";
    }

    public CTHoaDonBan(String maSanPham, String tenSanPham, int soLuong, long donGia, String loaiKhuyenMai) {
        super();
        if (maSanPham == null || !maSanPham.matches("(?i)SP\\d+")) {
            throw new IllegalArgumentException("MaSanPham không đúng định dạng.");
        }
        if (tenSanPham == null || tenSanPham.isBlank()) {
            throw new IllegalArgumentException("TenSanPham không được để trống.");
        }
        if (soLuong < 0) {
            throw new IllegalArgumentException("SoLuong không được âm.");
        }
        if (donGia < 0) {
            throw new IllegalArgumentException("DonGia không được âm.");
        }
        super.setMaSanpham(maSanPham);
        super.setTenSanPham(tenSanPham);
        super.setSoLuong(soLuong);
        super.setDonGia(donGia);
        this.soTienGiam = 0;
        this.soLuongTangKem = 0;
        this.soLuongPhaiTra = soLuong;
        this.loaiKhuyenMai = (loaiKhuyenMai == null || loaiKhuyenMai.isBlank()) ? "Không áp dụng" : loaiKhuyenMai;
        this.thanhTienSauKhuyenMai = tinhThanhTienSauKhuyenMai();
    }

    public void nhapThongTinBan(Scanner sc, QuanLySanPham qlsp, Danhsachkhuyenmai dskm, DanhSachSPKM dsspkm) {
        while (true) {
            setMaSanpham(Validation.inputStringRegex(sc, "Nhập mã sản phẩm (VD: SP01): ", Validation.MaSP_REGEX,
                    "Lỗi: Mã SP phải bắt đầu bằng SP và theo sau là số!"));
            SanPham sanPham = qlsp.traCuuSanPham(super.getMaSanpham());
            if (sanPham == null || sanPham.getSoLuongTonKho() == 0 || !sanPham.isTonTai()
                    || (sanPham instanceof ThucPham && ((ThucPham) sanPham).kiemTraSanPhamHong())) {
                System.out.println("[!] Mã sản phẩm không có trong danh mục/tồn kho.");
                continue;
            }
            setTenSanPham(sanPham.getTenSanPham());
            setDonGia(sanPham.getGiaBan());
            int soLuongConLai = sanPham.getSoLuongTonKho();
            do {
                setSoLuong(Validation.inputInt(sc, "Nhập số lượng (còn " + soLuongConLai + "): "));
                if (super.getSoLuong() == 0 || super.getSoLuong() > soLuongConLai) {
                    System.out.println("[!] Số lượng phải từ 1 đến " + soLuongConLai + ".");
                }
            } while (super.getSoLuong() == 0 || super.getSoLuong() > soLuongConLai);

            this.soLuongPhaiTra = super.getSoLuong();
            this.soTienGiam = 0;
            this.soLuongTangKem = 0;
            this.loaiKhuyenMai = "Không áp dụng";

            int index_spkm = dsspkm.timKiemSanPhamKM(super.getMaSanpham());
            if (index_spkm == -1) {
                System.out.println("Sản phẩm không có giảm giá!");
            } else {
                SanPhamKhuyenMai spkm = dsspkm.getSanPhamKhuyenMais()[index_spkm];
                int indexKm = dskm.traCuuThongTinKhuyenMai(spkm.getMaKhuyenMai());
                Khuyenmai km = indexKm == -1 ? null : dskm.getKhuyenmais()[indexKm];
                if (km != null && km.dangHoatDong(LocalDate.now()) && km.isTonTai()
                        && super.getDonGia() * super.getSoLuong() >= km.getMucChiToiThieu()) {
                    this.soLuongTangKem = km.tinhSoLuongTang(super.getSoLuong());
                    this.soTienGiam = km.tinhTienGiam(super.getDonGia(), super.getSoLuong());
                    this.soLuongPhaiTra = Math.max(0, super.getSoLuong() - this.soLuongTangKem);
                    this.loaiKhuyenMai = km.getLoaiKhuyenMai();
                }
            }
            break;
        }
        thanhTienSauKhuyenMai = tinhThanhTien();
        System.out.println("Thêm chi tiết hoá đơn thành công!");
    }

    private long tinhThanhTienSauKhuyenMai() {
        return Math.max(0, super.getDonGia() * super.getSoLuong() - soTienGiam);
    }

    public long tinhThanhTien() {
        this.thanhTienSauKhuyenMai = tinhThanhTienSauKhuyenMai();
        return this.thanhTienSauKhuyenMai;
    }

    @Override
    public String toString() {
        return super.toString() +
                " | Số lượng phải trả: " + soLuongPhaiTra +
                " | Số tiền được giảm: " + soTienGiam +
                " | Thành tiền: " + tinhThanhTien() +
                " | Loại khuyến mãi: " + loaiKhuyenMai;
    }

    @Override
    public void setMaSanpham(String maSanPham) {
        setMaSanPham(maSanPham);
    }

    public String getMaSanPham() {
        return super.getMaSanpham();
    }

    public void setMaSanPham(String maSanPham) {
        if (maSanPham == null || !maSanPham.matches("(?i)SP\\d+")) {
            throw new IllegalArgumentException("MaSanPham không đúng định dạng.");
        }
        super.setMaSanpham(maSanPham);
    }

    @Override
    public String getTenSanPham() {
        return super.getTenSanPham();
    }

    @Override
    public void setTenSanPham(String tenSanPham) {
        if (tenSanPham == null || tenSanPham.isBlank()) {
            throw new IllegalArgumentException("TenSanPham không được để trống.");
        }
        super.setTenSanPham(tenSanPham);
    }

    @Override
    public int getSoLuong() {
        return super.getSoLuong();
    }

    @Override
    public void setSoLuong(int soLuong) {
        if (soLuong < 0) {
            throw new IllegalArgumentException("SoLuong không được âm.");
        }
        super.setSoLuong(soLuong);
        this.soLuongPhaiTra = Math.max(0, soLuong - this.soLuongTangKem);
        this.thanhTienSauKhuyenMai = tinhThanhTienSauKhuyenMai();
    }

    @Override
    public long getDonGia() {
        return super.getDonGia();
    }

    @Override
    public void setDonGia(long donGia) {
        if (donGia < 0) {
            throw new IllegalArgumentException("DonGia không được âm.");
        }
        super.setDonGia(donGia);
        this.thanhTienSauKhuyenMai = tinhThanhTienSauKhuyenMai();
    }

    public boolean kemHangChoTang() {
        return this.soLuongTangKem > 0;
    }

    public long getThanhTienSauKhuyenMai() {
        return tinhThanhTien();
    }

    public void setThanhTienSauKhuyenMai(long thanhTienSauKhuyenMai) {
        if (thanhTienSauKhuyenMai < 0) {
            throw new IllegalArgumentException("Thành tiền sau khuyến mãi không được âm.");
        }
        this.thanhTienSauKhuyenMai = thanhTienSauKhuyenMai;
    }

    public int getSoLuongTangKem() {
        return soLuongTangKem;
    }

    public void setSoLuongTangKem(int soLuongTangKem) {
        if (soLuongTangKem < 0) {
            throw new IllegalArgumentException("SoLuongTangKem không được âm.");
        }
        this.soLuongTangKem = soLuongTangKem;
        this.soLuongPhaiTra = Math.max(0, super.getSoLuong() - soLuongTangKem);
        this.thanhTienSauKhuyenMai = tinhThanhTienSauKhuyenMai();
    }

    public String getLoaiKhuyenMai() {
        return loaiKhuyenMai;
    }

    public void setLoaiKhuyenMai(String loaiKhuyenMai) {
        if (loaiKhuyenMai == null || loaiKhuyenMai.isBlank()) {
            throw new IllegalArgumentException("Loại khuyến mãi không được rỗng.");
        }
        this.loaiKhuyenMai = loaiKhuyenMai;
    }

    public long getSoTienGiam() {
        return soTienGiam;
    }

    public void setSoTienGiam(long soTienGiam) {
        if (soTienGiam < 0) {
            throw new IllegalArgumentException("SoTienGiam không được âm.");
        }
        this.soTienGiam = soTienGiam;
        this.thanhTienSauKhuyenMai = tinhThanhTienSauKhuyenMai();
    }

    public int getSoLuongPhaiTra() {
        return soLuongPhaiTra;
    }

    public void setSoLuongPhaiTra(int soLuongPhaiTra) {
        if (soLuongPhaiTra < 0) {
            throw new IllegalArgumentException("SoLuongPhaiTra không được âm.");
        }
        this.soLuongPhaiTra = soLuongPhaiTra;
    }
}
