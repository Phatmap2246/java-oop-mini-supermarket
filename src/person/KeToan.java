package person;

import java.util.ArrayList;
import java.util.Scanner;
import java.time.LocalDate;

import hoadon.CTHoaDon;
import hoadon.HoaDon;
import hoadon.HoaDonNhapHang;
import hoadon.QuanLyHoaDon;
import utils.Validation;

public class KeToan extends NhanVien {
    ArrayList<Long> doanhThu = new ArrayList<>(); // đọc thanhTien từ file hoadonban
    ArrayList<Long> chiPhi = new ArrayList<>(); // đọc chiPhi từ file hoadonnhap, chiphivanhanh,...
    private final ArrayList<String> maHoaDonCongNo = new ArrayList<>();
    private final ArrayList<String> maPhieuNhapCongNo = new ArrayList<>();
    private final ArrayList<String> maNhaCungCapCongNo = new ArrayList<>();
    private final ArrayList<String> tenNhaCungCapCongNo = new ArrayList<>();
    private final ArrayList<Double> soTienCongNo = new ArrayList<>();
    private final ArrayList<LocalDate> ngayDenHanCongNo = new ArrayList<>();
    private final ArrayList<Boolean> daThanhToanCongNo = new ArrayList<>();

    public long tinhTongDoanhThu(){
        long doanhthu = 0; 
        for(long i : doanhThu)
            doanhthu += i; 
        return doanhthu;
    }
    public long tinhTongChiPhi(){
        long chiphi = 0; 
        for(long i : chiPhi)
            chiphi += i; 
        return chiphi;
    }
    public long tinhLoiNhuan(){
        return tinhTongDoanhThu()-tinhTongChiPhi();
    }
    public long tinhLoiNhuanRong(){
        double VAT = 0.08;
        double TNDN = 0.2;
        return (long)(tinhLoiNhuan()*VAT*TNDN);
    }

    public void thanhToanCongNo(Scanner sc, QuanLyHoaDon quanLyHoaDon) {

        while (true) {
            System.out.println("\n========== QUẢN LÝ CÔNG NỢ NHÀ CUNG CẤP ==========");
            System.out.println("1. Đối chiếu phiếu nhập kho với hóa đơn");
            System.out.println("2. Theo dõi và thanh toán công nợ đến hạn");
            System.out.println("0. Thoát");
            int luaChon = Validation.luaChon(sc, "Chọn chức năng: ", 0, 2);

            if (luaChon == 0) {
                return;
            }
            if (luaChon == 1) {
                doiChieuVaGhiNhanCongNo(sc, quanLyHoaDon);
            } else {
                hienThiVaThanhToanCongNo(sc);
            }
        }
    }

    private void doiChieuVaGhiNhanCongNo(Scanner sc, QuanLyHoaDon quanLyHoaDon) {
        String maHoaDon = Validation.inputStringRegex(sc, "Nhập mã hóa đơn nhà cung cấp: ",
                HoaDonNhapHang.MA_HD_NHAP_REGEX, "Mã hóa đơn nhập không hợp lệ!");
        HoaDon hoaDon = quanLyHoaDon.timKiemTheoMa(maHoaDon);
        if (!(hoaDon instanceof HoaDonNhapHang)) {
            System.out.println("Không tìm thấy hóa đơn nhập hàng có mã " + maHoaDon + ".");
            return;
        }
        HoaDonNhapHang hoaDonNhap = (HoaDonNhapHang) hoaDon;

        for (String maHoaDonDaGhi : maHoaDonCongNo) {
            if (maHoaDonDaGhi.equalsIgnoreCase(maHoaDon)) {
                System.out.println("Hóa đơn này đã được ghi nhận trong danh mục công nợ.");
                return;
            }
        }

        if (hoaDonNhap.getDanhSachChiTiet().isEmpty()) {
            System.out.println("Hóa đơn không có mặt hàng để đối chiếu.");
            return;
        }

        System.out.println("Nhà cung cấp: " + hoaDonNhap.getTenDoiTac()
                + " (" + hoaDonNhap.getMaNhaCungCap() + ")");
        System.out.println("Nhập các mặt hàng thực nhận trên phiếu nhập kho.");

        int soMatHang;
        do {
            soMatHang = Validation.inputInt(sc, "Nhập số dòng mặt hàng thực nhận: ");
            if (soMatHang <= 0) {
                System.out.println("Số dòng mặt hàng phải lớn hơn 0.");
            }
        } while (soMatHang <= 0);

        ArrayList<String> maSanPhamThucNhan = new ArrayList<>();
        ArrayList<Integer> soLuongThucNhan = new ArrayList<>();
        ArrayList<Double> donGiaThucNhan = new ArrayList<>();
        for (int i = 0; i < soMatHang; i++) {
            System.out.println("--- Mặt hàng thực nhận thứ " + (i + 1) + " ---");
            String maSanPham = Validation.inputStringRegex(sc, "Nhập mã sản phẩm: ",
                    CTHoaDon.MA_SP_REGEX, "Mã sản phẩm không hợp lệ!").toUpperCase();
            int soLuong;
            do {
                soLuong = Validation.inputInt(sc, "Nhập số lượng thực nhận: ");
                if (soLuong <= 0) {
                    System.out.println("Số lượng thực nhận phải lớn hơn 0.");
                }
            } while (soLuong <= 0);

            double donGia = Validation.inputDouble(sc, "Nhập đơn giá theo hóa đơn/hợp đồng: ");
            if (!Double.isFinite(donGia) || donGia < 0) {
                System.out.println("Đơn giá không hợp lệ; hủy đối chiếu.");
                return;
            }
            maSanPhamThucNhan.add(maSanPham);
            soLuongThucNhan.add(soLuong);
            donGiaThucNhan.add(donGia);
        }

        ArrayList<CTHoaDon> chiTietHoaDon = hoaDonNhap.getDanhSachChiTiet();
        for (int i = 0; i < chiTietHoaDon.size(); i++) {
            CTHoaDon chiTiet = chiTietHoaDon.get(i);
            if (chiTiet.getSoLuong() <= 0 || !Double.isFinite(chiTiet.getDonGia())
                    || chiTiet.getDonGia() < 0) {
                System.out.println("Chi tiết hóa đơn có số lượng hoặc đơn giá không hợp lệ.");
                return;
            }
            long soLuongHoaDon = 0;
            long soLuongThucTe = 0;
            boolean daKiemTraMa = false;
            for (int j = 0; j < chiTietHoaDon.size(); j++) {
                CTHoaDon chiTietCungMa = chiTietHoaDon.get(j);
                if (!chiTiet.getMaSanPham().equalsIgnoreCase(chiTietCungMa.getMaSanPham())) {
                    continue;
                }
                if (j < i) {
                    daKiemTraMa = true;
                    break;
                }
                if (Math.abs(chiTiet.getDonGia() - chiTietCungMa.getDonGia()) > 0.005) {
                    System.out.println("Hóa đơn có nhiều đơn giá cho cùng một sản phẩm.");
                    return;
                }
                soLuongHoaDon += chiTietCungMa.getSoLuong();
            }
            if (daKiemTraMa) {
                continue;
            }
            int viTriThucNhan = -1;
            for (int j = 0; j < maSanPhamThucNhan.size(); j++) {
                if (!chiTiet.getMaSanPham().equalsIgnoreCase(maSanPhamThucNhan.get(j))) {
                    continue;
                }
                if (Math.abs(chiTiet.getDonGia() - donGiaThucNhan.get(j)) > 0.005) {
                    System.out.println("Đơn giá thực nhận của " + chiTiet.getMaSanPham()
                            + " không thống nhất.");
                    return;
                }
                soLuongThucTe += soLuongThucNhan.get(j);
                viTriThucNhan = j;
            }
            if (viTriThucNhan == -1) {
                System.out.println("Đối chiếu thất bại: thiếu sản phẩm " + chiTiet.getMaSanPham() + ".");
                return;
            }
            if (soLuongHoaDon != soLuongThucTe) {
                System.out.println("Đối chiếu thất bại: số lượng thực nhận của "
                        + chiTiet.getMaSanPham() + " không khớp hóa đơn.");
                return;
            }
        }
        for (String maSanPham : maSanPhamThucNhan) {
            boolean coTrenHoaDon = false;
            for (CTHoaDon chiTiet : chiTietHoaDon) {
                if (maSanPham.equalsIgnoreCase(chiTiet.getMaSanPham())) {
                    coTrenHoaDon = true;
                    break;
                }
            }
            if (!coTrenHoaDon) {
                System.out.println("Đối chiếu thất bại: sản phẩm " + maSanPham
                        + " không có trên hóa đơn.");
                return;
            }
        }

        double tongTien = hoaDonNhap.tinhTongTien();
        if (!Double.isFinite(tongTien) || tongTien < 0) {
            System.out.println("Tổng tiền hóa đơn không hợp lệ; không ghi nhận công nợ.");
            return;
        }

        String maPhieuNhap = Validation.inputStringRegex(sc, "Nhập mã phiếu nhập kho: ",
                "^PNK\\d+$", "Mã phiếu nhập kho phải có dạng PNK theo sau là chữ số!");
        for (String maPhieuNhapDaGhi : maPhieuNhapCongNo) {
            if (maPhieuNhapDaGhi.equalsIgnoreCase(maPhieuNhap)) {
                System.out.println("Mã phiếu nhập kho này đã được sử dụng.");
                return;
            }
        }

        System.out.println("Đối chiếu số lượng và đơn giá thành công.");
        LocalDate ngayNhan = nhapNgayNhanHang(sc);
        System.out.println("Chọn kỳ hạn thanh toán:");
        System.out.println("1. 7 ngày");
        System.out.println("2. 15 ngày");
        System.out.println("3. 30 ngày");
        int kyHan = Validation.luaChon(sc, "Chọn kỳ hạn: ", 1, 3);
        int soNgay = kyHan == 1 ? 7 : kyHan == 2 ? 15 : 30;
        LocalDate ngayDenHan = ngayNhan.plusDays(soNgay);

        maHoaDonCongNo.add(maHoaDon);
        maPhieuNhapCongNo.add(maPhieuNhap);
        maNhaCungCapCongNo.add(hoaDonNhap.getMaNhaCungCap());
        tenNhaCungCapCongNo.add(hoaDonNhap.getTenDoiTac());
        soTienCongNo.add(tongTien);
        ngayDenHanCongNo.add(ngayDenHan);
        daThanhToanCongNo.add(false);
        System.out.println("Đã ghi nhận công nợ " + tongTien + " VND cho nhà cung cấp "
                + hoaDonNhap.getTenDoiTac() + ". Hạn thanh toán: "
                + utils.DateUtil.formatDate(ngayDenHan) + ".");
    }

    private void hienThiVaThanhToanCongNo(Scanner sc) {
        if (maHoaDonCongNo.isEmpty()) {
            System.out.println("Danh mục công nợ chưa có khoản nào. Hãy đối chiếu phiếu nhập trước.");
            return;
        }

        ArrayList<Integer> congNoDenHan = new ArrayList<>();
        System.out.println("\n--- DANH MỤC CÔNG NỢ NHÀ CUNG CẤP ---");
        for (int i = 0; i < maHoaDonCongNo.size(); i++) {
            String trangThai;
            if (daThanhToanCongNo.get(i)) {
                trangThai = "Đã thanh toán";
            } else if (ngayDenHanCongNo.get(i).isAfter(LocalDate.now())) {
                trangThai = "Chưa đến hạn";
            } else {
                trangThai = "Đến hạn";
                congNoDenHan.add(i);
            }
            System.out.printf("HĐ: %s | NCC: %s (%s) | Phiếu nhập: %s | "
                            + "Số tiền: %.2f VND | Hạn: %s | %s%n",
                    maHoaDonCongNo.get(i), tenNhaCungCapCongNo.get(i),
                    maNhaCungCapCongNo.get(i), maPhieuNhapCongNo.get(i),
                    soTienCongNo.get(i), utils.DateUtil.formatDate(ngayDenHanCongNo.get(i)),
                    trangThai);
        }

        if (congNoDenHan.isEmpty()) {
            System.out.println("Không có khoản công nợ nào đến hạn thanh toán.");
            return;
        }

        System.out.println("\nChọn khoản công nợ đến hạn cần thanh toán:");
        for (int i = 0; i < congNoDenHan.size(); i++) {
            int viTriCongNo = congNoDenHan.get(i);
            System.out.printf("%d. %s | %s | %.2f VND%n",
                    i + 1, maHoaDonCongNo.get(viTriCongNo),
                    tenNhaCungCapCongNo.get(viTriCongNo), soTienCongNo.get(viTriCongNo));
        }
        int luaChon = Validation.luaChon(sc, "Lựa chọn: ", 1, congNoDenHan.size());
        int viTriCongNo = congNoDenHan.get(luaChon - 1);

        String tenNganHang = Validation.inputStringRegex(sc, "Nhập tên ngân hàng nhà cung cấp: ",
                ".+", "Tên ngân hàng không được để trống!");
        String soTaiKhoan = Validation.inputStringRegex(sc, "Nhập số tài khoản nhà cung cấp: ",
                "\\S+", "Số tài khoản không được để trống!");
        System.out.printf("Lệnh chuyển khoản %.2f VND đến %s, tài khoản %s.%n",
                soTienCongNo.get(viTriCongNo), tenNganHang, soTaiKhoan);
        System.out.print("Nhấn Enter để xác nhận chuyển khoản đã hoàn tất: ");
        sc.nextLine();
        daThanhToanCongNo.set(viTriCongNo, true);
        System.out.println("Đã thanh toán công nợ hóa đơn "
                + maHoaDonCongNo.get(viTriCongNo) + ".");
    }

    private LocalDate nhapNgayNhanHang(Scanner sc) {
        while (true) {
            System.out.print("Nhập ngày nhận hàng (dd/MM/yyyy): ");
            LocalDate ngayNhan = utils.DateUtil.parseDate(sc.nextLine());
            if (ngayNhan != null) {
                return ngayNhan;
            }
            System.out.println("Ngày không hợp lệ. Vui lòng nhập đúng định dạng dd/MM/yyyy.");
        }
    }

}
