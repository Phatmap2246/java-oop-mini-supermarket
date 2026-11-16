package hoadon;

import utils.DongDuLieu;
import java.time.LocalDate;
import java.util.Scanner;

import dichvu.*;

import sanpham.QuanLySanPham;
import sanpham.SanPham;
import sanpham.ThucPham;
import utils.Validation;
public class CTHoaDonBan extends CTHoaDon{
    private boolean tonTai = true;

    public boolean isTonTai() { return tonTai; }

    public void setTonTai(boolean tonTai) { this.tonTai = tonTai; }

    private long thanhTienSauKhuyenMai;
    private int soLuongTangKem;
    private int soLuongPhaiTra;
    private long soTienGiam;
    private String loaiKhuyenMai ;
    // Chỉ dùng khi đang lập/sửa chi tiết; dữ liệu lịch sử giữ nguyên ưu đãi đã lưu.
    private transient Khuyenmai khuyenMaiApDung;

    // constructor
    public CTHoaDonBan() {
        super();
        this.thanhTienSauKhuyenMai = 0;
        this.soTienGiam = 0;
        this.soLuongTangKem = 0;
        this.soLuongPhaiTra = 0;
        this.loaiKhuyenMai = "Không áp dụng" ;
    }

    public CTHoaDonBan(String maSanPham, String tenSanPham, int soLuong, long donGia , String loaiKhuyenMai ) {
        this();
        setMaSanPham(maSanPham);
        setTenSanPham(tenSanPham);
        setSoLuong(soLuong);
        setDonGia(donGia);
        this.soTienGiam = 0;
        this.loaiKhuyenMai = (loaiKhuyenMai == null || loaiKhuyenMai.isBlank()) ? "Không áp dụng" : loaiKhuyenMai ;
        this.thanhTienSauKhuyenMai = tinhThanhTienSauKhuyenMai();
        this.soLuongTangKem =0;
        this.soLuongPhaiTra=soLuong;
    }
    public void nhapThongTinBan(Scanner sc, QuanLySanPham qlsp, Danhsachkhuyenmai dskm, DanhSachSPKM dsspkm) {
        apDungKhuyenMai(null);
        setSoLuong(0);
        while (true) {
            setMaSanpham(Validation.inputStringRegex(sc, "Nhập mã sản phẩm (VD: SP001): ", Validation.MaSP_REGEX, "Lỗi: Mã SP phải bắt đầu bằng SP và theo sau là 3 đến 5 chữ số (VD: SP001)!" , SanPham.getSoThuTu()));
            SanPham sanPham = qlsp.timMa(super.getMaSanpham()) ;
            if (sanPham == null || sanPham.getSoLuongTonKho() == 0 || !sanPham.isTonTai() || (sanPham instanceof ThucPham && ((ThucPham) sanPham).kiemTraSanPhamHong())) {
                System.out.println("[!] Mã sản phẩm không có trong danh mục/tồn kho.");
                continue;
            }
            setTenSanPham(sanPham.getTenSanPham());
            setDonGia(sanPham.getGiaBan());
            int soLuongConLai = sanPham.getSoLuongTonKho();
            do {
                int soLuongNhap = Validation.inputInt(sc, "Nhập tổng số lượng lấy khỏi kho, gồm quà tặng nếu có (còn " + soLuongConLai + "): ");
                try {
                    setSoLuong(soLuongNhap);
                } catch (IllegalArgumentException e) {
                    System.out.println(e.getMessage());
                    setSoLuong(0);
                }
                if (super.getSoLuong() == 0 || super.getSoLuong() > soLuongConLai) {
                    System.out.println("[!] Số lượng phải từ 1 đến " + soLuongConLai + ".");
                }
            } while (super.getSoLuong() == 0 || super.getSoLuong() > soLuongConLai);
            // Cho phép nhập lại trên cùng đối tượng mà không giữ ưu đãi của lần trước.
            soTienGiam = 0;
            soLuongTangKem = 0;
            soLuongPhaiTra = super.getSoLuong();
            loaiKhuyenMai = "Không áp dụng";
            int index_spkm = dsspkm.timViTri(super.getMaSanpham()) ;
            if ( index_spkm == -1 )
            {
                System.out.println("Sản phẩm không có giảm giá!");
            }
            else
            {
                SanPhamKhuyenMai spkm = dsspkm.getSanPhamKhuyenMais()[index_spkm] ;
                int indexKm = dskm.timViTri(spkm.getMaKhuyenMai());
                Khuyenmai km = indexKm == -1 ? null : dskm.getKhuyenmais()[indexKm];
                apDungKhuyenMai(km);
            }
            break;
        }
        thanhTienSauKhuyenMai = tinhThanhTien();
        System.out.println("Thêm chi tiết hoá đơn thành công!");
    }
    private long tinhThanhTienSauKhuyenMai() {
        return Math.max(0, tienHang(super.getDonGia(), super.getSoLuong()) - soTienGiam);
    }

    private static long tienHang(long donGia, int soLuong) {
        try {
            return Math.multiplyExact(donGia, (long) soLuong);
        } catch (ArithmeticException e) {
            throw new IllegalArgumentException("Thành tiền vượt giới hạn. Vui lòng giảm số lượng hoặc đơn giá.", e);
        }
    }

    public void apDungKhuyenMai(Khuyenmai khuyenMai) {
        khuyenMaiApDung = khuyenMai;
        capNhatKhuyenMai();
    }

    private void capNhatKhuyenMai() {
        long tong = tienHang(super.getDonGia(), super.getSoLuong());
        soTienGiam = 0;
        soLuongTangKem = 0;
        loaiKhuyenMai = "Không áp dụng";
        if (khuyenMaiApDung != null && khuyenMaiApDung.isTonTai()
                && khuyenMaiApDung.dangHoatDong(LocalDate.now())
                && tong >= khuyenMaiApDung.getMucChiToiThieu()) {
            soLuongTangKem = khuyenMaiApDung.tinhSoLuongTang(super.getSoLuong());
            soTienGiam = khuyenMaiApDung.tinhTienGiam(super.getDonGia(), super.getSoLuong());
            loaiKhuyenMai = khuyenMaiApDung.getLoaiKhuyenMai();
        }
        soLuongPhaiTra = Math.max(0, super.getSoLuong() - soLuongTangKem);
        thanhTienSauKhuyenMai = tinhThanhTienSauKhuyenMai();
    }

    public long tinhThanhTien() {
        this.thanhTienSauKhuyenMai = tinhThanhTienSauKhuyenMai();
        return this.thanhTienSauKhuyenMai;
    }
    @Override
    public String toString() {
        return utils.HienThi.canLe(super.toString() +
                " | Số lượng phải trả: " + soLuongPhaiTra +
                " | Số tiền được giảm: " + soTienGiam +
                " | Thành tiền: " + tinhThanhTien() +
                " | Loại khuyến mãi: " + loaiKhuyenMai);
    }

    // getters - setters
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
        tienHang(super.getDonGia(), soLuong);
        super.setSoLuong(soLuong);
        if (khuyenMaiApDung != null) {
            capNhatKhuyenMai();
            return;
        }
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
        tienHang(donGia, super.getSoLuong());
        super.setDonGia(donGia);
        if (khuyenMaiApDung != null) {
            capNhatKhuyenMai();
            return;
        }
        this.thanhTienSauKhuyenMai = tinhThanhTienSauKhuyenMai();
    }


    public boolean kemHangChoTang(){
        return this.soLuongTangKem>0;
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

    // ================= ĐỌC / GHI FILE TXT =================

    @Override public String toDongFile() {
        kiemTraDuLieuFile();
        return "CTBAN;" + thongTinChiTietFile() + ";" + DongDuLieu.ghep(tonTai, thanhTienSauKhuyenMai, soLuongTangKem, soLuongPhaiTra, soTienGiam, loaiKhuyenMai);
    }

    public static CTHoaDonBan tuDongFile(String dong) {
        String[] p = dong.split(";", -1);
        DongDuLieu.yeuCau(p.length == 11 && "CTBAN".equals(p[0]), "Sai dòng CTBAN (cần 11 cột).");
        CTHoaDonBan obj = new CTHoaDonBan();
        obj.docThongTinChiTietFile(p);
        obj.tonTai = DongDuLieu.logic(p[5]);
        obj.thanhTienSauKhuyenMai = Long.parseLong(p[6]);
        obj.soLuongTangKem = Integer.parseInt(p[7]);
        obj.soLuongPhaiTra = Integer.parseInt(p[8]);
        obj.soTienGiam = Long.parseLong(p[9]);
        obj.loaiKhuyenMai = p[10];
        obj.kiemTraDuLieuFile();
        return obj;
    }

    public void kiemTraDuLieuFile() {
        super.kiemTraDuLieuFile();
        tienHang(super.getDonGia(), super.getSoLuong());
        DongDuLieu.khongAm(thanhTienSauKhuyenMai, "thanhTienSauKhuyenMai");
        DongDuLieu.khongAm(soLuongTangKem, "soLuongTangKem");
        DongDuLieu.khongAm(soLuongPhaiTra, "soLuongPhaiTra");
        DongDuLieu.khongAm(soTienGiam, "soTienGiam");
    }
}
