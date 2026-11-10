package dichvu;

import hoadon.CTHoaDon;
import hoadon.HoaDonBanHang;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import sanpham.QuanLyTonKho;
import sanpham.SanPham;

public final class KhuyenMaiService {
    private KhuyenMaiService() {
    }

    public static long apDungKhuyenMai(HoaDonBanHang hoaDon,
            DanhSachSPKM danhSachSanPhamKhuyenMai,
            Danhsachkhuyenmai danhSachKhuyenMai) {
        return apDungKhuyenMai(hoaDon, danhSachSanPhamKhuyenMai,
                danhSachKhuyenMai, null);
    }

    public static long apDungKhuyenMai(HoaDonBanHang hoaDon,
            DanhSachSPKM danhSachSanPhamKhuyenMai,
            Danhsachkhuyenmai danhSachKhuyenMai,
            List<SanPham> danhSachSanPham) {
        ArrayList<CTHoaDon> chiTietHoaDon = hoaDon.getDanhSachChiTiet();
        chiTietHoaDon.removeIf(CTHoaDon::isHangKhuyenMai);
        for (CTHoaDon chiTiet : chiTietHoaDon) {
            chiTiet.setThanhTienSauKhuyenMai((long) chiTiet.getSoLuongTonKho() * chiTiet.getDonGia());
        }

        if (danhSachSanPhamKhuyenMai == null || danhSachKhuyenMai == null) {
            return 0;
        }

        long tongTienHang = 0;
        for (CTHoaDon chiTiet : chiTietHoaDon) {
            tongTienHang += (long) chiTiet.getSoLuongTonKho() * chiTiet.getDonGia();
        }

        Map<String, Khuyenmai> khuyenMaiApDung = new LinkedHashMap<>();
        Map<String, ArrayList<CTHoaDon>> dongHangTheoKhuyenMai = new LinkedHashMap<>();
        Map<String, ArrayList<CTHoaDon>> dongHangTangTheoKhuyenMai = new LinkedHashMap<>();
        Map<String, Long> soLuongMuaTheoKhuyenMaiVaSanPham = new LinkedHashMap<>();
        Map<String, CTHoaDon> sanPhamTheoKhuyenMaiVaSanPham = new LinkedHashMap<>();
        Map<String, Long> soLuongTangDuKienTheoMaSanPham = new LinkedHashMap<>();

        LocalDate homNay = LocalDate.now();
        for (CTHoaDon chiTiet : chiTietHoaDon) {
            SanPhamKhuyenMai lienKet = timLienKet(
                    danhSachSanPhamKhuyenMai, chiTiet.getMaSanPham());
            if (lienKet == null) {
                continue;
            }

            Khuyenmai khuyenMai = timKhuyenMai(danhSachKhuyenMai, lienKet.getMaKhuyenMai());
            if (khuyenMai == null || !khuyenMai.dangHoatDong(homNay)
                    || tongTienHang < khuyenMai.getMucChiToiThieu()) {
                continue;
            }

            String maKhuyenMai = khuyenMai.getMaKhuyenMai().toUpperCase();
            khuyenMaiApDung.putIfAbsent(maKhuyenMai, khuyenMai);
            dongHangTheoKhuyenMai
                    .computeIfAbsent(maKhuyenMai, key -> new ArrayList<>())
                    .add(chiTiet);

            if (khuyenMai instanceof KhuyenMaiMuaTang) {
                String khoaSanPham = maKhuyenMai + "|" + chiTiet.getMaSanPham().toUpperCase();
                soLuongMuaTheoKhuyenMaiVaSanPham.merge(
                        khoaSanPham, (long) chiTiet.getSoLuongTonKho(), Long::sum);
                sanPhamTheoKhuyenMaiVaSanPham.putIfAbsent(khoaSanPham, chiTiet);
            }
        }

        for (Map.Entry<String, Long> entry : soLuongMuaTheoKhuyenMaiVaSanPham.entrySet()) {
            String[] khoa = entry.getKey().split("\\|", 2);
            Khuyenmai khuyenMai = khuyenMaiApDung.get(khoa[0]);
            KhuyenMaiMuaTang muaTang = (KhuyenMaiMuaTang) khuyenMai;
            int soLuongMuaToiThieu = muaTang.getSoLuongMuaToiThieu();
            if (soLuongMuaToiThieu <= 0 || muaTang.getSoLuongTang() <= 0) {
                continue;
            }

            long soLanDatMuc = entry.getValue() / soLuongMuaToiThieu;
            if (soLanDatMuc > Integer.MAX_VALUE / muaTang.getSoLuongTang()) {
                System.out.println("[!] Số lượng hàng tặng vượt giới hạn và không được thêm vào hóa đơn.");
                continue;
            }
            long soLuongTang = soLanDatMuc * muaTang.getSoLuongTang();
            if (soLuongTang > 0) {
                CTHoaDon sanPham = sanPhamTheoKhuyenMaiVaSanPham.get(entry.getKey());
                SanPham sanPhamTrongKho = QuanLyTonKho.timSanPham(
                        danhSachSanPham, sanPham.getMaSanPham());
                long soLuongDaCo = QuanLyTonKho.soLuongDaCo(
                        chiTietHoaDon, sanPham.getMaSanPham());
                long soLuongTangDaDuKien = soLuongTangDuKienTheoMaSanPham
                        .getOrDefault(sanPham.getMaSanPham().toUpperCase(), 0L);
                if (sanPhamTrongKho == null
                        || soLuongDaCo + soLuongTangDaDuKien + soLuongTang
                                > sanPhamTrongKho.getSoLuongTonKho()) {
                    System.out.println("[!] Không đủ tồn kho để cấp hàng tặng "
                            + sanPham.getTenSanPham() + "; bỏ qua phần quà này.");
                    continue;
                }
                CTHoaDon hangTang = new CTHoaDon(
                        sanPham.getMaSanPham(), sanPham.getTenSanPham(),
                        (int) soLuongTang, 0, 0,0, true);
                soLuongTangDuKienTheoMaSanPham.merge(
                        sanPham.getMaSanPham().toUpperCase(), soLuongTang, Long::sum);
                dongHangTangTheoKhuyenMai
                        .computeIfAbsent(khoa[0], key -> new ArrayList<>())
                        .add(hangTang);
            }
        }

        long tienGiam = 0;
        for (Map.Entry<String, Khuyenmai> entry : khuyenMaiApDung.entrySet()) {
            Khuyenmai khuyenMai = entry.getValue();
            ArrayList<CTHoaDon> dongDuocApDung = dongHangTheoKhuyenMai.get(entry.getKey());
            long tienGiamChoKhuyenMai = 0;

            if (khuyenMai instanceof KhuyenMaiPhanTram) {
                KhuyenMaiPhanTram giamTheoPhanTram = (KhuyenMaiPhanTram) khuyenMai;
                double phanTram = giamTheoPhanTram.getPhanTramGiamGia();
                if (phanTram > 1) {
                    phanTram /= 100.0;
                }
                phanTram = Math.max(0, Math.min(1, phanTram));
                long mucGiamConLai = Math.max(0, giamTheoPhanTram.getMucGiamToiDa());
                for (CTHoaDon chiTiet : dongDuocApDung) {
                    long tienGiamTrenDong = BigDecimal.valueOf(chiTiet.tinhThanhTien())
                            .multiply(BigDecimal.valueOf(phanTram))
                            .setScale(0, RoundingMode.DOWN).longValueExact();
                    tienGiamTrenDong = Math.min(tienGiamTrenDong, mucGiamConLai);
                    tienGiamTrenDong = apDungGiamTrenDong(chiTiet, tienGiamTrenDong);
                    tienGiamChoKhuyenMai += tienGiamTrenDong;
                    mucGiamConLai -= tienGiamTrenDong;
                }
            } else if (khuyenMai instanceof KhuyenMaiTrucTiep) {
                long mucGiamMoiDong = Math.max(
                        0, ((KhuyenMaiTrucTiep) khuyenMai).getSoTienGiam());
                for (CTHoaDon chiTiet : dongDuocApDung) {
                    tienGiamChoKhuyenMai += apDungGiamTrenDong(
                            chiTiet, mucGiamMoiDong);
                }
            }

            tienGiam += tienGiamChoKhuyenMai;

            ArrayList<CTHoaDon> hangTang = dongHangTangTheoKhuyenMai.get(entry.getKey());
            if (hangTang != null) {
                for (CTHoaDon chiTietTang : hangTang) {
                    hoaDon.themChiTiet(chiTietTang);
                }
            }
        }

        return tienGiam;
    }

    private static long apDungGiamTrenDong(CTHoaDon chiTiet, long tienGiamYeuCau) {
        long tienGiam = Math.min(Math.max(0, tienGiamYeuCau), chiTiet.tinhThanhTien());
        if (tienGiam == 0) {
            return 0;
        }
        chiTiet.setThanhTienSauKhuyenMai(chiTiet.tinhThanhTien() - tienGiam);
        return tienGiam;
    }

    private static SanPhamKhuyenMai timLienKet(DanhSachSPKM danhSach,
            String maSanPham) {
        for (SanPhamKhuyenMai lienKet : danhSach.getSanPhamKhuyenMais()) {
            if (lienKet.getMaSanPham().equalsIgnoreCase(maSanPham)) {
                return lienKet;
            }
        }
        return null;
    }

    private static Khuyenmai timKhuyenMai(Danhsachkhuyenmai danhSach,
            String maKhuyenMai) {
        for (Khuyenmai khuyenMai : danhSach.getKhuyenmais()) {
            if (khuyenMai.getMaKhuyenMai().equalsIgnoreCase(maKhuyenMai)) {
                return khuyenMai;
            }
        }
        return null;
    }
}
