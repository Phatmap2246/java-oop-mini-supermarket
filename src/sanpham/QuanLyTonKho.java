package sanpham;

import hoadon.CTHoaDon;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class QuanLyTonKho {
    private QuanLyTonKho() {
    }

    public static SanPham timSanPham(List<SanPham> danhSachSanPham, String maSanPham) {
        if (danhSachSanPham == null || maSanPham == null) {
            return null;
        }
        for (SanPham sanPham : danhSachSanPham) {
            if (sanPham.getMaSanPham().equalsIgnoreCase(maSanPham)) {
                return sanPham;
            }
        }
        return null;
    }

    public static int soLuongDaCo(List<CTHoaDon> chiTietHoaDon, String maSanPham) {
        long soLuong = 0;
        for (CTHoaDon chiTiet : chiTietHoaDon) {
            if (chiTiet.getMaSanPham().equalsIgnoreCase(maSanPham)) {
                soLuong += chiTiet.getSoLuongTonKho();
            }
        }
        return soLuong > Integer.MAX_VALUE ? Integer.MAX_VALUE : (int) soLuong;
    }

    public static boolean kiemTraDuTonKho(List<SanPham> danhSachSanPham,
            List<CTHoaDon> chiTietHoaDon) {
        if (danhSachSanPham == null || chiTietHoaDon == null) {
            System.out.println("[!] Không có danh sách sản phẩm để kiểm tra tồn kho.");
            return false;
        }

        for (Map.Entry<String, Long> entry : tongSoLuongTheoMa(chiTietHoaDon).entrySet()) {
            SanPham sanPham = timSanPham(danhSachSanPham, entry.getKey());
            if (sanPham == null) {
                System.out.println("[!] Không tìm thấy sản phẩm " + entry.getKey() + " trong danh mục.");
                return false;
            }
            if (entry.getValue() > sanPham.getSoLuongTonKho()) {
                System.out.println("[!] Sản phẩm " + sanPham.getTenSanPham()
                        + " chỉ còn " + sanPham.getSoLuongTonKho()
                        + ", hóa đơn yêu cầu " + entry.getValue() + ".");
                return false;
            }
        }
        return true;
    }

    public static boolean truTonKho(List<SanPham> danhSachSanPham,
            List<CTHoaDon> chiTietHoaDon) {
        if (!kiemTraDuTonKho(danhSachSanPham, chiTietHoaDon)) {
            return false;
        }
        for (Map.Entry<String, Long> entry : tongSoLuongTheoMa(chiTietHoaDon).entrySet()) {
            SanPham sanPham = timSanPham(danhSachSanPham, entry.getKey());
            sanPham.setSoLuongTonKho(sanPham.getSoLuongTonKho() - entry.getValue().intValue());
        }
        return true;
    }

    public static boolean congTonKho(List<SanPham> danhSachSanPham,
            List<CTHoaDon> chiTietHoaDon) {
        if (danhSachSanPham == null || chiTietHoaDon == null) {
            System.out.println("[!] Không có danh sách sản phẩm để cập nhật tồn kho.");
            return false;
        }

        Map<String, Long> soLuongTheoMa = tongSoLuongTheoMa(chiTietHoaDon);
        Map<String, SanPham> sanPhamTheoMa = new LinkedHashMap<>();
        for (Map.Entry<String, Long> entry : soLuongTheoMa.entrySet()) {
            SanPham sanPham = timSanPham(danhSachSanPham, entry.getKey());
            if (sanPham == null) {
                System.out.println("[!] Không thể duyệt phiếu: sản phẩm "
                        + entry.getKey() + " chưa có trong danh mục sản phẩm.");
                return false;
            }
            if (entry.getValue() > Integer.MAX_VALUE - (long) sanPham.getSoLuongTonKho()) {
                System.out.println("[!] Không thể cập nhật tồn kho cho "
                        + sanPham.getTenSanPham() + ": số lượng vượt giới hạn.");
                return false;
            }
            sanPhamTheoMa.put(entry.getKey().toUpperCase(), sanPham);
        }

        for (Map.Entry<String, Long> entry : soLuongTheoMa.entrySet()) {
            SanPham sanPham = sanPhamTheoMa.get(entry.getKey().toUpperCase());
            sanPham.setSoLuongTonKho(sanPham.getSoLuongTonKho() + entry.getValue().intValue());
        }
        return true;
    }

    private static Map<String, Long> tongSoLuongTheoMa(List<CTHoaDon> chiTietHoaDon) {
        Map<String, Long> soLuongTheoMa = new LinkedHashMap<>();
        for (CTHoaDon chiTiet : chiTietHoaDon) {
            String maSanPham = chiTiet.getMaSanPham().toUpperCase();
            soLuongTheoMa.merge(maSanPham, (long) chiTiet.getSoLuongTonKho(), Long::sum);
        }
        return soLuongTheoMa;
    }
}
