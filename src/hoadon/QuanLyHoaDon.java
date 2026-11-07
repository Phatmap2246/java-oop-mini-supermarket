package hoadon;

import java.util.ArrayList;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.util.Base64;
import java.util.Scanner;
import dichvu.Danhsachdoitac;
import dichvu.Doitac;
import dichvu.Nhacungcap;
import dichvu.DanhSachSPKM;
import dichvu.Danhsachkhuyenmai;
import sanpham.QuanLySanPham;
import sanpham.QuanLyTonKho;
import utils.Validation;

public class QuanLyHoaDon {
    private ArrayList<HoaDon> danhSachHoaDon;

    public QuanLyHoaDon() {
        this.danhSachHoaDon = new ArrayList<>();
    }

    public ArrayList<HoaDon> getDanhSachHoaDon() { return danhSachHoaDon; }

    public void nhapDanhSachHoaDon(Scanner sc) {
        nhapDanhSachHoaDon(sc, null);
    }

    public void nhapDanhSachHoaDon(Scanner sc, Danhsachdoitac danhSachDoiTac) {
        nhapDanhSachHoaDon(sc, danhSachDoiTac, new DanhSachSPKM(), new Danhsachkhuyenmai());
    }

    public void nhapDanhSachHoaDon(Scanner sc, Danhsachdoitac danhSachDoiTac,
            DanhSachSPKM danhSachSPKM, Danhsachkhuyenmai danhSachKhuyenMai) {
        nhapDanhSachHoaDon(sc, danhSachDoiTac, danhSachSPKM,
                danhSachKhuyenMai, new QuanLySanPham());
    }

    public void nhapDanhSachHoaDon(Scanner sc, Danhsachdoitac danhSachDoiTac,
            DanhSachSPKM danhSachSPKM, Danhsachkhuyenmai danhSachKhuyenMai,
            QuanLySanPham quanLySanPham) {
        int n = Validation.inputInt(sc, "Nhập số lượng hóa đơn muốn tạo: ");
        for (int i = 0; i < n; i++) {
            System.out.println("\n--- Nhập hóa đơn thứ " + (i + 1) + " ---");
            System.out.println("1. Hóa đơn bán hàng");
            System.out.println("2. Hóa đơn nhập hàng");
            int chon = Validation.inputInt(sc, "Chọn loại hóa đơn (1/2): ");
            
            if (chon == 1) {
                HoaDonBanHang hoaDonBan = new HoaDonBanHang();
                boolean daLap = hoaDonBan.nhapHoaDon(sc, danhSachSPKM, danhSachKhuyenMai,
                        maHoaDon -> timKiemTheoMa(maHoaDon) != null,
                        quanLySanPham.getDanhSachSanPham());
                if (!daLap) {
                    continue;
                }
                if (hoaDonBan.isDaThanhToan()
                        && !QuanLyTonKho.truTonKho(
                                quanLySanPham.getDanhSachSanPham(),
                                hoaDonBan.getDanhSachChiTiet())) {
                    System.out.println("[!] Thanh toán đã thành công nhưng không thể cập nhật kho; cần đối soát hóa đơn "
                            + hoaDonBan.getMaHoaDon() + ".");
                }
                danhSachHoaDon.add(hoaDonBan);
                System.out.println("[+] Đã thêm hóa đơn thành công!");
                continue;
            } else if (chon == 2) {
                if (danhSachDoiTac == null) {
                    System.out.println("[!] Cần danh sách đối tác để tạo hóa đơn nhập qua quy trình quản kho.");
                    continue;
                }
                taoHoaDonNhapHang(sc, danhSachDoiTac);
                continue;
            } else {
                System.out.println("Lựa chọn không hợp lệ, bỏ qua!");
                continue;
            }

        }
    }

    public boolean taoHoaDonNhapHang(Scanner sc, Danhsachdoitac danhSachDoiTac) {
        ArrayList<Nhacungcap> nhaCungCaps = new ArrayList<>();
        for (Doitac doiTac : danhSachDoiTac.getDanhSachDoiTac()) {
            if (doiTac instanceof Nhacungcap) {
                nhaCungCaps.add((Nhacungcap) doiTac);
            }
        }

        if (nhaCungCaps.isEmpty()) {
            System.out.println("[!] Chưa có nhà cung cấp trong danh sách đối tác.");
            return false;
        }

        System.out.println("--- Chọn nhà cung cấp ---");
        for (int i = 0; i < nhaCungCaps.size(); i++) {
            Nhacungcap nhaCungCap = nhaCungCaps.get(i);
            System.out.println((i + 1) + ". " + nhaCungCap.getTenDoiTac()
                    + " (" + nhaCungCap.getMaDoiTac() + ")");
        }
        int luaChon = Validation.luaChon(sc, "Chọn nhà cung cấp: ", 1, nhaCungCaps.size());

        HoaDonNhapHang hoaDonNhap = new HoaDonNhapHang();
        hoaDonNhap.khoiTaoThongTinChung(sc, nhaCungCaps.get(luaChon - 1));
        if (timKiemTheoMa(hoaDonNhap.getMaHoaDon()) != null) {
            System.out.println("[!] Mã hóa đơn đã tồn tại; phiếu nhập chưa được tạo.");
            return false;
        }

        hoaDonNhap.quanKhoNhapChiTiet(sc);
        danhSachHoaDon.add(hoaDonNhap);
        System.out.println("[+] Đã tạo phiếu nhập " + hoaDonNhap.getMaHoaDon()
                + ". Phiếu đang chờ nhà cung cấp báo phí vận chuyển.");
        return true;
    }

    public boolean nhaCungCapBaoPhiVanChuyen(Scanner sc, String maHoaDon,
            Danhsachdoitac danhSachDoiTac) {
        HoaDon hoaDon = timKiemTheoMa(maHoaDon);
        if (!(hoaDon instanceof HoaDonNhapHang)) {
            System.out.println("[!] Không tìm thấy hóa đơn nhập có mã " + maHoaDon + ".");
            return false;
        }

        HoaDonNhapHang hoaDonNhap = (HoaDonNhapHang) hoaDon;
        for (Doitac doiTac : danhSachDoiTac.getDanhSachDoiTac()) {
            if (doiTac instanceof Nhacungcap
                    && doiTac.getMaDoiTac().equalsIgnoreCase(hoaDonNhap.getMaNhaCungCap())) {
                return ((Nhacungcap) doiTac).baoPhiVanChuyen(sc, hoaDonNhap, danhSachDoiTac);
            }
        }

        System.out.println("[!] Không tìm thấy nhà cung cấp của hóa đơn trong danh sách đối tác.");
        return false;
    }

    public boolean duyetHoaDonNhapHang(String maHoaDon, QuanLySanPham quanLySanPham) {
        HoaDon hoaDon = timKiemTheoMa(maHoaDon);
        if (!(hoaDon instanceof HoaDonNhapHang)) {
            System.out.println("[!] Không tìm thấy hóa đơn nhập có mã " + maHoaDon + ".");
            return false;
        }

        HoaDonNhapHang hoaDonNhap = (HoaDonNhapHang) hoaDon;
        if (!HoaDonNhapHang.TRANG_THAI_CHO_QUAN_LY_DUYET.equals(
                hoaDonNhap.getTrangThai())) {
            System.out.println("[!] Hóa đơn chưa sẵn sàng để duyệt nhập kho.");
            return false;
        }
        if (!QuanLyTonKho.congTonKho(
                quanLySanPham.getDanhSachSanPham(),
                hoaDonNhap.getDanhSachChiTiet())) {
            return false;
        }
        return hoaDonNhap.xacNhanDuyetNhapKho();
    }

    public void hienThiDanhSach() {
        if (this.danhSachHoaDon.isEmpty()) {
            System.out.println("[!] Danh sách hóa đơn đang trống!");
            return;
        }
        System.out.println("\n=== DANH SÁCH TOÀN BỘ HÓA ĐƠN ===");
        for (HoaDon hd : this.danhSachHoaDon) {
            hd.hienThiHoaDon();
        }
    }

    public void themHoaDon(HoaDon hd) { this.danhSachHoaDon.add(hd); }

    public HoaDon timKiemTheoMa(String maHD) {
        for (HoaDon hd : this.danhSachHoaDon) {
            if (hd.getMaHoaDon().equalsIgnoreCase(maHD)) return hd;
        }
        return null;
    }

    public boolean suaHoaDon(String maHD, Scanner sc) {
        HoaDon hd = timKiemTheoMa(maHD);
        if (hd == null) return false;

        int choice = -1;
        // TODO: sửa lại bỏ sửa mã HD
        while (choice != 0) {
            System.out.println("\n--- SỬA HÓA ĐƠN: " + hd.getMaHoaDon() + " ---");
            System.out.println("1. Sửa Mã Hóa Đơn");
            System.out.println("2. Sửa Tên Đối Tác");
            
            if (hd instanceof HoaDonBanHang) {
                System.out.println("3. Thực hiện lại thanh toán");
            } else if (hd instanceof HoaDonNhapHang) {
                System.out.println("3. Sửa Mã Nhà Cung Cấp");
                System.out.println("4. Sửa Thuế VAT");
                System.out.println("5. Sửa Phí Vận Chuyển");
            }
            System.out.println("0. Hoàn tất sửa");
            
            choice = Validation.inputInt(sc, "Chọn thông tin muốn sửa: ");

            switch (choice) {
                case 1:
                    String maMoi = Validation.inputStringRegex(sc, "Nhập mã HĐ mới: ", 
                        (hd instanceof HoaDonBanHang) ? HoaDonBanHang.MA_HD_BAN_REGEX : HoaDonNhapHang.MA_HD_NHAP_REGEX,
                        "Lỗi: Mã HĐ không đúng định dạng!");
                    HoaDon checkTrung = timKiemTheoMa(maMoi);
                    if (checkTrung != null && checkTrung != hd) {
                        System.out.println("[!] LỖI: Mã HĐ đã được dùng cho hóa đơn khác!");
                    } else {
                        hd.setMaHoaDon(maMoi);
                        System.out.println("Đã cập nhật Mã HĐ!");
                    }
                    break;

                case 2:
                    String tenMoi = Validation.inputStringRegex(sc, "Nhập tên đối tác mới: ", Validation.HOTEN_REGEX, "Lỗi: Tên không hợp lệ!");
                    hd.setTenDoiTac(tenMoi);
                    System.out.println("Đã cập nhật Tên đối tác!");
                    break;

                case 3:
                    if (hd instanceof HoaDonBanHang) {
                        HoaDonBanHang hdBan = (HoaDonBanHang) hd;
                        if (hdBan.isDaThanhToan()) {
                            System.out.println("[!] Hóa đơn này đã thanh toán, không thể thu tiền lần nữa.");
                        } else if (!QuanLyTonKho.kiemTraDuTonKho(
                                new QuanLySanPham().getDanhSachSanPham(),
                                hdBan.getDanhSachChiTiet())) {
                            System.out.println("[!] Chưa thể thu tiền do tồn kho không đủ.");
                        } else if (hdBan.getPhuongThucThanhToan() != null) {
                            boolean ketQua = hdBan.getPhuongThucThanhToan().thucHienThanhToan(sc, hdBan.tinhTongTien());
                            hdBan.setDaThanhToan(ketQua);
                            if (ketQua) {
                                QuanLyTonKho.truTonKho(
                                        new QuanLySanPham().getDanhSachSanPham(),
                                        hdBan.getDanhSachChiTiet());
                                System.out.println("Thanh toán thành công; tồn kho đã cập nhật.");
                            } else {
                                System.out.println("Thanh toán chưa thành công; tồn kho không thay đổi.");
                            }
                        } else {
                            System.out.println("[!] Lỗi: Hóa đơn này chưa có phương thức thanh toán!");
                        }
                    } else if (hd instanceof HoaDonNhapHang) {
                        HoaDonNhapHang hdNhap = (HoaDonNhapHang) hd;
                        String maNCCMoi = Validation.inputStringRegex(sc, "Nhập mã NCC mới: ", HoaDonNhapHang.MA_NCC_REGEX, "Lỗi: Mã NCC không đúng!");
                        hdNhap.setMaNhaCungCap(maNCCMoi);
                        System.out.println("Đã cập nhật Mã NCC!");
                    }
                    break;

                case 4:
                    if (hd instanceof HoaDonNhapHang) {
                        HoaDonNhapHang hdNhap = (HoaDonNhapHang) hd;
                        // SỬA: Thuế VAT giữ nguyên double thay vì long
                        double vatMoi = Validation.inputDouble(sc, "Nhập thuế VAT mới (%): ");
                        hdNhap.setThueVAT(vatMoi);
                        System.out.println("Đã cập nhật Thuế VAT!");
                    } else {
                        System.out.println("[!] Tiền khuyến mãi được tính từ chương trình, không sửa trực tiếp.");
                    }
                    break;

                case 5:
                    if (hd instanceof HoaDonNhapHang) {
                        HoaDonNhapHang hdNhap = (HoaDonNhapHang) hd;
                        long phiMoi = Validation.inputLong(sc, "Nhập phí vận chuyển mới: ");
                        hdNhap.setChiPhiVanChuyen(phiMoi);
                        System.out.println("Đã cập nhật Phí vận chuyển!");
                    }
                    break;

                case 0:
                    System.out.println("Hoàn tất quá trình sửa.");
                    break;
                default:
                    System.out.println("Lựa chọn không hợp lệ!");
            }
        }
        return true; 
    }

    public boolean xoaHoaDon(String maHD) {
        HoaDon hd = timKiemTheoMa(maHD);
        if (hd != null) {
            this.danhSachHoaDon.remove(hd);
            return true;
        }
        return false;
    }

    public void timKiemNhieuKhoa(String keyword) {
        boolean found = false;
        System.out.println("\n--- KẾT QUẢ TÌM KIẾM CHO: '" + keyword + "' ---");
        for (HoaDon hd : this.danhSachHoaDon) {
            if (hd.getMaHoaDon().toLowerCase().contains(keyword.toLowerCase()) || 
                hd.getTenDoiTac().toLowerCase().contains(keyword.toLowerCase())) {
                hd.hienThiHoaDon();
                found = true;
            }
        }
        if (!found) System.out.println("Không tìm thấy hóa đơn nào phù hợp!");
    }

    public void thongKe() {
        if (this.danhSachHoaDon.isEmpty()) {
            System.out.println("[!] Không có dữ liệu để thống kê!");
            return;
        }
        int soLuongBan = 0, soLuongNhap = 0;
        long tongDoanhThuBan = 0, tongChiPhiNhap = 0;
        
        for (HoaDon hd : this.danhSachHoaDon) {
            if (hd instanceof HoaDonBanHang) {
                soLuongBan++;
                tongDoanhThuBan += hd.tinhTongTien();
            } else if (hd instanceof HoaDonNhapHang) {
                soLuongNhap++;
                tongChiPhiNhap += hd.tinhTongTien();
            }
        }
        System.out.println("\n========== BẢNG THỐNG KÊ ==========");
        System.out.println("Tổng số hóa đơn: " + this.danhSachHoaDon.size());
        System.out.println("Số hóa đơn BÁN: " + soLuongBan + " | Tổng doanh thu: " + tongDoanhThuBan + " VND");
        System.out.println("Số hóa đơn NHẬP: " + soLuongNhap + " | Tổng chi phí: " + tongChiPhiNhap + " VND");
        System.out.println("Lợi nhuận tạm tính: " + (tongDoanhThuBan - tongChiPhiNhap) + " VND");
        System.out.println("===================================");
    }

    public void ghiHoaDonBanText(String tenFile) throws IOException {
        ArrayList<String> dongDuLieu = new ArrayList<>();
        for (HoaDon hoaDon : danhSachHoaDon) {
            if (!(hoaDon instanceof HoaDonBanHang)) {
                continue;
            }
            HoaDonBanHang hoaDonBan = (HoaDonBanHang) hoaDon;
            dongDuLieu.add(String.join("|", "BAN4",
                    maHoa(hoaDonBan.getMaHoaDon()),
                    maHoa(hoaDonBan.getTenDoiTac()),
                    hoaDonBan.getNgayLap().toString(),
                    Long.toString(hoaDonBan.getTienKhuyenMai()),
                    Boolean.toString(hoaDonBan.isDaThanhToan()),
                    Boolean.toString(hoaDonBan.isKhuyenMaiPhanBoTheoChiTiet()),
                    hoaDonBan.getKieuChietKhauHoaDon(),
                    hoaDonBan.getGiaTriChietKhauHoaDon()));
            for (CTHoaDon chiTiet : hoaDonBan.getDanhSachChiTiet()) {
                dongDuLieu.add(ghiChiTietMoi(chiTiet));
            }
        }
        ghiFileThayThe(Paths.get(tenFile), dongDuLieu);
    }

    public void docHoaDonBanText(String tenFile) throws IOException {
        ArrayList<HoaDon> hoaDonDocDuoc = docHoaDon(Paths.get(tenFile), true);
        thayTheLoaiHoaDon(hoaDonDocDuoc, true);
    }

    public void ghiHoaDonNhapText(String tenFile) throws IOException {
        ArrayList<String> dongDuLieu = new ArrayList<>();
        for (HoaDon hoaDon : danhSachHoaDon) {
            if (!(hoaDon instanceof HoaDonNhapHang)) {
                continue;
            }
            HoaDonNhapHang hoaDonNhap = (HoaDonNhapHang) hoaDon;
            dongDuLieu.add(String.join("|", "NHAP2",
                    maHoa(hoaDonNhap.getMaHoaDon()),
                    maHoa(hoaDonNhap.getTenDoiTac()),
                    maHoa(hoaDonNhap.getMaNhaCungCap()),
                    hoaDonNhap.getNgayLap().toString(),
                    Double.toString(hoaDonNhap.getThueVAT()),
                    Long.toString(hoaDonNhap.getChiPhiVanChuyen()),
                    maHoa(hoaDonNhap.getTrangThai()),
                    maHoa(hoaDonNhap.getMaDonViVanChuyen()),
                    maHoa(hoaDonNhap.getMaXeVanChuyen()),
                    maHoa(hoaDonNhap.getLoaiXeVanChuyen())));
            for (CTHoaDon chiTiet : hoaDonNhap.getDanhSachChiTiet()) {
                dongDuLieu.add(ghiChiTietMoi(chiTiet));
            }
        }
        ghiFileThayThe(Paths.get(tenFile), dongDuLieu);
    }

    public void docHoaDonNhapText(String tenFile) throws IOException {
        ArrayList<HoaDon> hoaDonDocDuoc = docHoaDon(Paths.get(tenFile), false);
        thayTheLoaiHoaDon(hoaDonDocDuoc, false);
    }

    private ArrayList<HoaDon> docHoaDon(Path duongDan, boolean docHoaDonBan)
            throws IOException {
        ArrayList<HoaDon> ketQua = new ArrayList<>();
        HoaDon hoaDonHienTai = null;
        ArrayList<String> dongDuLieu = new ArrayList<>(
                Files.readAllLines(duongDan, StandardCharsets.UTF_8));
        for (int i = 0; i < dongDuLieu.size(); i++) {
            String dong = dongDuLieu.get(i);
            if (dong.trim().isEmpty()) {
                continue;
            }
            String[] truong = dong.split("\\|", -1);
            try {
                if (laDongDauHoaDonBan(truong)) {
                    if (!docHoaDonBan) {
                        throw new IllegalArgumentException("File hóa đơn nhập chứa hóa đơn bán.");
                    }
                    hoaDonHienTai = docHoaDonBan(truong);
                    ketQua.add(hoaDonHienTai);
                } else if (laDongDauHoaDonNhap(truong)) {
                    if (docHoaDonBan) {
                        throw new IllegalArgumentException("File hóa đơn bán chứa hóa đơn nhập.");
                    }
                    hoaDonHienTai = docHoaDonNhap(truong);
                    ketQua.add(hoaDonHienTai);
                } else if (laDongChiTiet(truong)) {
                    if (hoaDonHienTai == null) {
                        throw new IllegalArgumentException("Chi tiết không thuộc hóa đơn nào.");
                    }
                    hoaDonHienTai.getDanhSachChiTiet().add(docChiTiet(truong));
                } else {
                    throw new IllegalArgumentException("Loại dòng không được hỗ trợ.");
                }
            } catch (IllegalArgumentException ex) {
                throw new IOException("Lỗi dữ liệu tại " + duongDan + ", dòng "
                        + (i + 1) + ": " + ex.getMessage(), ex);
            }
        }

        kiemTraMaHoaDonDocDuoc(ketQua, docHoaDonBan);
        return ketQua;
    }

    private HoaDonBanHang docHoaDonBan(String[] truong) {
        HoaDonBanHang hoaDon;
        if ("BAN4".equals(truong[0])) {
            if (truong.length != 9) {
                throw new IllegalArgumentException("Dòng BAN4 cần có 9 trường.");
            }
            hoaDon = new HoaDonBanHang(giaiMa(truong[1]), giaiMa(truong[2]));
            hoaDon.setNgayLap(LocalDate.parse(truong[3]));
            hoaDon.setTienKhuyenMai(Long.parseLong(truong[4]));
            hoaDon.setDaThanhToan(docBoolean(truong[5]));
            hoaDon.setKhuyenMaiPhanBoTheoChiTiet(docBoolean(truong[6]));
            hoaDon.setChietKhauHoaDon(truong[7], truong[8]);
            return hoaDon;
        }

        boolean coChietKhau = "BAN3".equals(truong[0]);
        if ((!coChietKhau && truong.length != 5)
                || (coChietKhau && truong.length != 7)) {
            throw new IllegalArgumentException("Dòng hóa đơn bán cũ sai định dạng.");
        }
        hoaDon = new HoaDonBanHang(truong[1], truong[2]);
        hoaDon.setTienKhuyenMai(Long.parseLong(truong[3]));
        hoaDon.setDaThanhToan(docBoolean(truong[4]));
        hoaDon.setKhuyenMaiPhanBoTheoChiTiet(!"BAN".equals(truong[0]));
        if (coChietKhau) {
            hoaDon.setChietKhauHoaDon(truong[5], truong[6]);
        }
        return hoaDon;
    }

    private HoaDonNhapHang docHoaDonNhap(String[] truong) {
        HoaDonNhapHang hoaDon;
        if ("NHAP2".equals(truong[0])) {
            if (truong.length != 11) {
                throw new IllegalArgumentException("Dòng NHAP2 cần có 11 trường.");
            }
            hoaDon = new HoaDonNhapHang(giaiMa(truong[1]), giaiMa(truong[2]),
                    giaiMa(truong[3]), Long.parseLong(truong[6]),
                    Double.parseDouble(truong[5]));
            hoaDon.setNgayLap(LocalDate.parse(truong[4]));
            hoaDon.setThongTinVanChuyen(giaiMa(truong[8]), giaiMa(truong[9]),
                    giaiMa(truong[10]), giaiMa(truong[7]));
            return hoaDon;
        }

        if (truong.length != 6 && truong.length != 10) {
            throw new IllegalArgumentException("Dòng hóa đơn nhập cũ sai định dạng.");
        }
        hoaDon = new HoaDonNhapHang(truong[1], truong[2], truong[3],
                Long.parseLong(truong[5]), Double.parseDouble(truong[4]));
        if (truong.length == 10) {
            hoaDon.setThongTinVanChuyen(truong[7], truong[8], truong[9], truong[6]);
        }
        return hoaDon;
    }

    private CTHoaDon docChiTiet(String[] truong) {
        if ("CT4".equals(truong[0])) {
            if (truong.length != 8) {
                throw new IllegalArgumentException("Dòng CT4 cần có 8 trường.");
            }
            return new CTHoaDon(giaiMa(truong[1]), giaiMa(truong[2]),
                    Integer.parseInt(truong[3]), Long.parseLong(truong[4]),
                    Double.parseDouble(truong[5]), docBoolean(truong[6]),
                    Long.parseLong(truong[7]));
        }
        if (!"CT".equals(truong[0])
                || (truong.length != 6 && truong.length != 7 && truong.length != 8)) {
            throw new IllegalArgumentException("Dòng chi tiết hóa đơn cũ sai định dạng.");
        }
        CTHoaDon chiTiet = new CTHoaDon(truong[1], truong[2],
                Integer.parseInt(truong[3]), Long.parseLong(truong[4]),
                Double.parseDouble(truong[5]),
                truong.length >= 7 && docBoolean(truong[6]));
        if (truong.length == 8) {
            chiTiet.setThanhTienSauKhuyenMai(Long.parseLong(truong[7]));
        }
        return chiTiet;
    }

    private boolean laDongDauHoaDonBan(String[] truong) {
        return truong.length > 0 && ("BAN4".equals(truong[0])
                || "BAN".equals(truong[0]) || "BAN2".equals(truong[0])
                || "BAN3".equals(truong[0]));
    }

    private boolean laDongDauHoaDonNhap(String[] truong) {
        return truong.length > 0 && ("NHAP2".equals(truong[0])
                || "NHAP".equals(truong[0]));
    }

    private boolean laDongChiTiet(String[] truong) {
        return truong.length > 0 && ("CT4".equals(truong[0]) || "CT".equals(truong[0]));
    }

    private String ghiChiTietMoi(CTHoaDon chiTiet) {
        return String.join("|", "CT4",
                maHoa(chiTiet.getMaSanPham()),
                maHoa(chiTiet.getTenSanPham()),
                Integer.toString(chiTiet.getSoLuong()),
                Long.toString(chiTiet.getDonGia()),
                Double.toString(chiTiet.getChietKhau()),
                Boolean.toString(chiTiet.isHangKhuyenMai()),
                Long.toString(chiTiet.getThanhTienSauKhuyenMai()));
    }

    private void kiemTraMaHoaDonDocDuoc(ArrayList<HoaDon> hoaDonDocDuoc,
            boolean laHoaDonBan) throws IOException {
        ArrayList<String> maDaDoc = new ArrayList<>();
        for (HoaDon hoaDon : hoaDonDocDuoc) {
            for (String ma : maDaDoc) {
                if (ma.equalsIgnoreCase(hoaDon.getMaHoaDon())) {
                    throw new IOException("Mã hóa đơn bị lặp trong file: "
                            + hoaDon.getMaHoaDon());
                }
            }
            maDaDoc.add(hoaDon.getMaHoaDon());
            HoaDon hoaDonKhacLoai = timKiemTheoMa(hoaDon.getMaHoaDon());
            if (hoaDonKhacLoai != null
                    && (laHoaDonBan != (hoaDonKhacLoai instanceof HoaDonBanHang))) {
                throw new IOException("Mã hóa đơn đã được dùng cho loại hóa đơn khác: "
                        + hoaDon.getMaHoaDon());
            }
        }
    }

    private void thayTheLoaiHoaDon(ArrayList<HoaDon> hoaDonMoi, boolean laHoaDonBan) {
        danhSachHoaDon.removeIf(hoaDon -> laHoaDonBan
                ? hoaDon instanceof HoaDonBanHang
                : hoaDon instanceof HoaDonNhapHang);
        danhSachHoaDon.addAll(hoaDonMoi);
    }

    private void ghiFileThayThe(Path duongDan, ArrayList<String> dongDuLieu)
            throws IOException {
        Path duongDanTuyetDoi = duongDan.toAbsolutePath();
        Path thuMuc = duongDanTuyetDoi.getParent();
        Files.createDirectories(thuMuc);
        Path tepTam = Files.createTempFile(thuMuc, duongDan.getFileName().toString(), ".tmp");
        try {
            Files.write(tepTam, dongDuLieu, StandardCharsets.UTF_8,
                    StandardOpenOption.TRUNCATE_EXISTING);
            try {
                Files.move(tepTam, duongDanTuyetDoi, StandardCopyOption.REPLACE_EXISTING,
                        StandardCopyOption.ATOMIC_MOVE);
            } catch (AtomicMoveNotSupportedException ex) {
                Files.move(tepTam, duongDanTuyetDoi, StandardCopyOption.REPLACE_EXISTING);
            }
        } finally {
            Files.deleteIfExists(tepTam);
        }
    }

    private String maHoa(String giaTri) {
        return Base64.getUrlEncoder().withoutPadding()
                .encodeToString(giaTri.getBytes(StandardCharsets.UTF_8));
    }

    private String giaiMa(String giaTri) {
        return new String(Base64.getUrlDecoder().decode(giaTri), StandardCharsets.UTF_8);
    }

    private boolean docBoolean(String giaTri) {
        if ("true".equalsIgnoreCase(giaTri)) {
            return true;
        }
        if ("false".equalsIgnoreCase(giaTri)) {
            return false;
        }
        throw new IllegalArgumentException("Giá trị boolean không hợp lệ.");
    }

    public void ghiFileText(String tenFile) {
        try (java.io.BufferedWriter bw = new java.io.BufferedWriter(new java.io.OutputStreamWriter(new java.io.FileOutputStream(tenFile), java.nio.charset.StandardCharsets.UTF_8))) {
            for (HoaDon hd : this.danhSachHoaDon) {
                if (hd instanceof HoaDonBanHang) {
                    HoaDonBanHang hdBan = (HoaDonBanHang) hd;
                    String loaiHoaDonBan;
                    if (!hdBan.isKhuyenMaiPhanBoTheoChiTiet()) {
                        loaiHoaDonBan = "BAN";
                    } else if (!"NONE".equals(hdBan.getKieuChietKhauHoaDon())) {
                        loaiHoaDonBan = "BAN3";
                    } else {
                        loaiHoaDonBan = "BAN2";
                    }
                    bw.write(loaiHoaDonBan + "|" + hdBan.getMaHoaDon() + "|"
                            + hdBan.getTenDoiTac() + "|" + hdBan.getTienKhuyenMai()
                            + "|" + hdBan.isDaThanhToan());
                    if (loaiHoaDonBan.equals("BAN3")) {
                        bw.write("|" + hdBan.getKieuChietKhauHoaDon()
                                + "|" + hdBan.getGiaTriChietKhauHoaDon());
                    }
                    bw.newLine();
                    bw.newLine();
                } else if (hd instanceof HoaDonNhapHang) {
                    HoaDonNhapHang hdNhap = (HoaDonNhapHang) hd;
                    bw.write("NHAP|" + hdNhap.getMaHoaDon() + "|" + hdNhap.getTenDoiTac()
                            + "|" + hdNhap.getMaNhaCungCap() + "|" + hdNhap.getThueVAT()
                            + "|" + hdNhap.getChiPhiVanChuyen() + "|" + hdNhap.getTrangThai()
                            + "|" + hdNhap.getMaDonViVanChuyen() + "|" + hdNhap.getMaXeVanChuyen()
                            + "|" + hdNhap.getLoaiXeVanChuyen());
                    bw.newLine();
                }
                for (CTHoaDon ct : hd.getDanhSachChiTiet()) {
                    bw.write("CT|" + ct.getMaSanPham() + "|" + ct.getTenSanPham() + "|"
                            + ct.getSoLuong() + "|" + ct.getDonGia() + "|"
                            + ct.getChietKhau() + "|" + ct.isHangKhuyenMai()
                            + "|" + ct.getThanhTienSauKhuyenMai());
                    bw.newLine();
                }
            }
            System.out.println("[+] Đã ghi dữ liệu thành công vào file: " + tenFile);
        } catch (java.io.IOException e) {
            System.out.println("[!] Lỗi khi ghi file: " + e.getMessage());
        }
    }

    public void docFileText(String tenFile) {
        this.danhSachHoaDon.clear();
        try (java.io.BufferedReader br = new java.io.BufferedReader(new java.io.FileReader(tenFile, java.nio.charset.StandardCharsets.UTF_8))) {
            String line;
            HoaDon hdHienTai = null;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\|", -1);
                if (parts.length > 0) {
                    String loai = parts[0];
                    if (((loai.equals("BAN") || loai.equals("BAN2")) && parts.length == 5)
                            || (loai.equals("BAN3") && parts.length == 7)) {
                        if (timKiemTheoMa(parts[1]) == null) {
                            hdHienTai = new HoaDonBanHang(parts[1], parts[2]);
                            ((HoaDonBanHang) hdHienTai).setTienKhuyenMai(Long.parseLong(parts[3]));
                            ((HoaDonBanHang) hdHienTai).setDaThanhToan(Boolean.parseBoolean(parts[4]));
                            ((HoaDonBanHang) hdHienTai)
                                    .setKhuyenMaiPhanBoTheoChiTiet(!loai.equals("BAN"));
                            if (loai.equals("BAN3")) {
                                ((HoaDonBanHang) hdHienTai)
                                        .setChietKhauHoaDon(parts[5], parts[6]);
                            }
                            this.danhSachHoaDon.add(hdHienTai);
                        } else {
                            System.out.println("[!] Cảnh báo: Bỏ qua hóa đơn trùng mã " + parts[1] + " trong file!");
                            hdHienTai = null;
                        }
                    } else if (loai.equals("NHAP") && (parts.length == 6 || parts.length == 10)) {
                        if (timKiemTheoMa(parts[1]) == null) {
                            // SỬA: Long.parseLong cho chi phí vận chuyển, Double.parseDouble cho VAT
                            hdHienTai = new HoaDonNhapHang(parts[1], parts[2], parts[3], 
                                        Long.parseLong(parts[5]), Double.parseDouble(parts[4]));
                            if (parts.length == 10) {
                                ((HoaDonNhapHang) hdHienTai).setThongTinVanChuyen(
                                        parts[7], parts[8], parts[9], parts[6]);
                            }
                            this.danhSachHoaDon.add(hdHienTai);
                        } else {
                            System.out.println("[!] Cảnh báo: Bỏ qua hóa đơn trùng mã " + parts[1] + " trong file!");
                            hdHienTai = null;
                        }
                    } else if (loai.equals("CT") && hdHienTai != null
                            && (parts.length == 6 || parts.length == 7 || parts.length == 8)) {
                        boolean hangKhuyenMai = parts.length >= 7
                                && Boolean.parseBoolean(parts[6]);
                        CTHoaDon ct = new CTHoaDon(parts[1], parts[2],
                                Integer.parseInt(parts[3]), Long.parseLong(parts[4]),
                                Double.parseDouble(parts[5]), hangKhuyenMai);
                        if (parts.length == 8) {
                            ct.setThanhTienSauKhuyenMai(Long.parseLong(parts[7]));
                        }
                        hdHienTai.themChiTiet(ct);
                    }
                }
            }
            System.out.println("[+] Đã đọc dữ liệu thành công từ file: " + tenFile);
        } catch (java.io.IOException e) {
            System.out.println("[!] Lỗi khi đọc file (Có thể file chưa tồn tại): " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("[!] Lỗi định dạng số: File dữ liệu bị hỏng hoặc bị sửa tay sai!");
        }
    }
}