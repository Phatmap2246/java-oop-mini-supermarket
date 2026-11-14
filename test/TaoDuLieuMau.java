import dichvu.*;
import hoadon.*;
import person.*;
import sanpham.*;

import java.lang.reflect.Field;
import java.time.*;
import java.util.*;

/** Chỉ tạo mẫu khi các danh sách còn rỗng; không tự ghi đè dữ liệu đã nhập. */
public class TaoDuLieuMau {
    public static void main(String[] args) throws Exception {
        String thuMuc = args.length == 0 ? GiaoDien.THU_MUC_DU_LIEU : args[0];
        if (args.length > 1 && args[1].equals("check")) {
            kiemTra(GiaoDien.docDuLieu(thuMuc));
            System.out.println("PASS: dữ liệu mẫu đọc lại đầy đủ.");
            return;
        }
        GiaoDien gd = GiaoDien.docDuLieu(thuMuc);
        QuanLySanPham dsSP = lay(gd, "dsSanPham");
        DSNhanVien dsNV = lay(gd, "dsNhanVien");
        DSKhachHang dsKH = lay(gd, "dsKhachHang");
        Danhsachdoitac dsDT = lay(gd, "dsDoiTac");
        Danhsachkhuyenmai dsKM = lay(gd, "dsKhuyenMai");
        DanhSachSPKM dsSPKM = lay(gd, "dsSanPhamKhuyenMai");
        DanhSachHoaDonBan dsBan = lay(gd, "dsHoaDonBan");
        DanhSachHoaDonNhap dsNhap = lay(gd, "dsHoaDonNhap");
        DanhSachCongNo dsNo = lay(gd, "dsCongNo");
        if (!dsSP.getDanhSachSanPham().isEmpty() || !dsNV.getDanhSachNhanVien().isEmpty()
                || dsKH.getSoLuong() != 0 || !dsDT.getDanhSachDoiTac().isEmpty()
                || dsKM.getSoLuongKhuyenMai() != 0 || dsSPKM.getSoLuongSanPham() != 0
                || !dsBan.getDsHoaDonBanHang().isEmpty() || !dsNhap.getDsHoaDonNhapHang().isEmpty()
                || !((List<?>) lay(dsNo, "maHoaDonCongNo")).isEmpty()) {
            throw new IllegalStateException("Đã có dữ liệu. Hãy chọn thư mục mẫu riêng để tránh ghi đè.");
        }
        LocalDate homNay = LocalDate.now();
        String[] tenNV = {"Nguyễn Minh An", "Trần Quốc Bình", "Lê Ngọc Chi", "Phạm Thanh Dũng"};
        String[] vaiTro = {"QuanLy", "QuanKho", "ThuNgan", "KeToan"};
        NhanVien[] nhanViens = {lay(gd, "quanLy"), lay(gd, "quanKho"), lay(gd, "thuNgan"), lay(gd, "keToan")};
        for (int i = 0; i < nhanViens.length; i++) {
            NhanVien nv = nhanViens[i];
            nv.setHoten(tenNV[i]); nv.setVaiTro(vaiTro[i]);
            nv.setEmail("nhanvien" + (i + 1) + "@example.com"); nv.setSdt("090100000" + i);
            nv.setDiachi(diaChi(i)); nv.setNgsinh(LocalDate.of(1990 + i, 3, 15));
            nv.setLuong(i == 0 ? 15000000 : 8000000); nv.setPhuCapTrachNhiem(1000000);
            dsNV.getDanhSachNhanVien().add(nv);
        }

        String[] tenNCC = {"Thực phẩm An Nhiên", "Gia dụng Bình Minh", "Điện máy Hòa Phát"};
        Nhacungcap[] ncc = new Nhacungcap[3];
        for (int i = 0; i < ncc.length; i++) {
            ncc[i] = new Nhacungcap(); ncc[i].setTenDoiTac(tenNCC[i]);
            ncc[i].setSoDienThoai("091200000" + i); ncc[i].setDiaChi((20 + i) + " Nguyễn Trãi, TP Hồ Chí Minh");
            ncc[i].setEmail("nhacungcap" + (i + 1) + "@example.com"); ncc[i].setMaSoThue("031000000" + i);
            ncc[i].setNganhHangCungCap(i == 0 ? "Thực phẩm" : i == 1 ? "Tiêu dùng" : "Điện tử");
            ncc[i].setHanMucNoCong(50000000); dsDT.getDanhSachDoiTac().add(ncc[i]);
        }

        String[] tenSP = {"Sữa tươi hộp 1 lít", "Sữa chua dâu", "Bánh mì sandwich", "Nước giặt 3 lít",
                "Dầu gội 650 ml", "Bàn chải đánh răng", "Nồi cơm điện 1.8 lít", "Ấm đun siêu tốc"};
        long[] giaNhap = {25000, 6000, 12000, 110000, 70000, 10000, 450000, 220000};
        long[] giaBan = {32000, 8000, 18000, 150000, 95000, 18000, 650000, 350000};
        int[] tonKho = {40, 15, 6, 25, 12, 0, 5, 3};
        ArrayList<SanPham> sanPhams = dsSP.getDanhSachSanPham();
        for (int i = 0; i < tenSP.length; i++) {
            SanPham sp;
            if (i < 3) {
                ThucPham tp = new ThucPham();
                tp.setNgaySanXuat(homNay.minusDays(20));
                tp.setHanSuDung(homNay.plusDays(i == 0 ? 90 : i == 1 ? 5 : -2));
                tp.setBaoQuan("Bảo quản mát, tránh ánh nắng"); tp.setNhietDoBaoQuan(5);
                sp = tp;
            } else if (i < 6) {
                TieuDung td = new TieuDung(); td.setThuongHieu("Bình Minh"); td.setNoiSanXuat("Việt Nam");
                td.setPhanLoai("Chăm sóc gia đình"); td.setLuuYAnToan("Để xa tầm tay trẻ em");
                td.setLoaiSanPham("Tiêu dùng"); sp = td;
            } else {
                DienTu dt = new DienTu(); dt.setThoiGianBaoHanh(12); dt.setChatLieu("Nhựa và inox");
                dt.setLoaiSanPham("Điện tử"); sp = dt;
            }
            int indexNCC = i < 3 ? 0 : i < 6 ? 1 : 2;
            sp.setTenSanPham(tenSP[i]); sp.setGiaNhap(giaNhap[i]); sp.setGiaBan(giaBan[i]);
            sp.setSoLuongTonKho(tonKho[i]); sp.setMaNhaCungCap(ncc[indexNCC].getMaDoiTac());
            sanPhams.add(sp); ncc[indexNCC].getSanPhamCungCap().getDanhSachSanPham().add(sp);
        }

        KhuyenMaiPhanTram km1 = new KhuyenMaiPhanTram(); km1.setPhanTramGiamGia(0.10); km1.setMucGiamToiDa(50000);
        KhuyenMaiMuaTang km2 = new KhuyenMaiMuaTang(); km2.setSoLuongMuaToiThieu(2); km2.setSoLuongTang(1);
        KhuyenMaiTrucTiep km3 = new KhuyenMaiTrucTiep(); km3.setSoTienGiam(50000);
        KhuyenMaiTrucTiep km4 = new KhuyenMaiTrucTiep(); km4.setSoTienGiam(10000);
        Khuyenmai[] kms = {km1, km2, km3, km4};
        for (int i = 0; i < kms.length; i++) {
            kms[i].setNgayBatDau(homNay.minusDays(30)); kms[i].setNgayKetThuc(homNay.plusDays(i == 3 ? -1 : 60));
            kms[i].setMucChiToiThieu(0);
        }
        dsKM.setKhuyenmais(kms);
        dsSPKM.setSanPhamKhuyenMais(new SanPhamKhuyenMai[]{
                new SanPhamKhuyenMai(sanPhams.get(0).getMaSanPham(), km1.getMaKhuyenMai()),
                new SanPhamKhuyenMai(sanPhams.get(3).getMaSanPham(), km2.getMaKhuyenMai()),
                new SanPhamKhuyenMai(sanPhams.get(6).getMaSanPham(), km3.getMaKhuyenMai()),
                new SanPhamKhuyenMai(sanPhams.get(4).getMaSanPham(), km4.getMaKhuyenMai())});

        KhachHang[] khs = new KhachHang[3];
        String[] tenKH = {"Nguyễn Thảo Linh", "Trần Hoàng Nam", "Lê Hà My"};
        for (int i = 0; i < khs.length; i++) {
            khs[i] = new KhachHang(); khs[i].setMaKhachHang(String.format("KH%03d", i + 1));
            khs[i].setHoten(tenKH[i]); khs[i].setEmail("khachhang" + (i + 1) + "@example.com");
            khs[i].setSdt("093300000" + i); khs[i].setDiachi(diaChi(i + 4));
            khs[i].setNgsinh(LocalDate.of(1995 + i, 6, 10)); khs[i].setNgayDangKy(homNay.minusMonths(6));
            khs[i].setTongChiTieu(i == 0 ? 250000 : i == 1 ? 1500000 : 5500000);
            khs[i].setDiemTichLuy((int) (khs[i].getTongChiTieu() / 1000)); khs[i].capNhatThuHang();
        }
        dsKH.setList(khs, khs.length);

        HoaDonBanHang ban1 = hoaDonBan("HD001", nhanViens[2], khs[0], homNay.minusDays(2), true);
        ban1.getCtHoaDonBans().add(chiTietBan(sanPhams.get(0), 2, 6400, km1.getLoaiKhuyenMai()));
        ban1.getCtHoaDonBans().add(chiTietBan(sanPhams.get(1), 4, 0, "Không áp dụng"));
        HoaDonBanHang ban2 = hoaDonBan("HD002", nhanViens[2], khs[1], homNay.minusDays(1), true);
        ban2.setPhuongThucThanhToan(new ThanhToanChuyenKhoan("Ngân hàng mẫu", "0123456789"));
        ban2.getCtHoaDonBans().add(chiTietBan(sanPhams.get(6), 1, 50000, km3.getLoaiKhuyenMai()));
        HoaDonBanHang ban3 = hoaDonBan("HD003", nhanViens[2], khs[2], homNay, false);
        CTHoaDonBan ctTang = chiTietBan(sanPhams.get(3), 2, 0, km2.getLoaiKhuyenMai()); ctTang.setSoLuongTangKem(1);
        ban3.getCtHoaDonBans().add(ctTang);
        dsBan.getDsHoaDonBanHang().addAll(Arrays.asList(ban1, ban2, ban3));

        HoaDonNhapHang nhap1 = hoaDonNhap("HD004", nhanViens[1], ncc[1], sanPhams.get(3), 50, homNay.minusDays(40), true);
        HoaDonNhapHang nhap2 = hoaDonNhap("HD005", nhanViens[1], ncc[2], sanPhams.get(6), 10, homNay.minusDays(10), true);
        HoaDonNhapHang nhap3 = hoaDonNhap("HD006", nhanViens[1], ncc[0], sanPhams.get(0), 20, homNay, false);
        dsNhap.getDsHoaDonNhapHang().addAll(Arrays.asList(nhap1, nhap2, nhap3));
        congNo(dsNo, nhap1, ncc[1], "PNK001", homNay.minusDays(10), false);
        congNo(dsNo, nhap2, ncc[2], "PNK002", homNay.plusDays(20), true);
        Field boDemHD = HoaDon.class.getDeclaredField("soThuTu"); boDemHD.setAccessible(true);
        boDemHD.setInt(null, Math.max(7, boDemHD.getInt(null)));
        gd.ghiDuLieu(thuMuc);
        kiemTra(GiaoDien.docDuLieu(thuMuc));
        System.out.println("Đã tạo mẫu: 8 sản phẩm, 4 nhân viên, 3 khách hàng, 3 NCC, 4 khuyến mãi, 4 liên kết, 6 hóa đơn, 2 công nợ.");
        System.out.println("Ngày dữ liệu mẫu: " + homNay + "; thư mục: " + thuMuc);
    }

    private static DiaChi diaChi(int i) { return new DiaChi("" + (10 + i), "Nguyễn Trãi", "Bến Thành", "Quận 1", "TP Hồ Chí Minh"); }

    private static HoaDonBanHang hoaDonBan(String ma, NhanVien nv, KhachHang kh, LocalDate ngay, boolean daTra) {
        HoaDonBanHang hd = new HoaDonBanHang(nv.getMaNV()); hd.setMaHoaDon(ma);
        hd.setMaKhachHang(kh.getMaKhachHang()); hd.setNgayLap(ngay.atTime(10, 30)); hd.setDaThanhToan(daTra);
        if (daTra) hd.setPhuongThucThanhToan(new ThanhToanTienMat());
        return hd;
    }

    private static CTHoaDonBan chiTietBan(SanPham sp, int sl, long giam, String loai) {
        CTHoaDonBan ct = new CTHoaDonBan(sp.getMaSanPham(), sp.getTenSanPham(), sl, sp.getGiaBan(), loai);
        ct.setSoTienGiam(giam); ct.setThanhTienSauKhuyenMai(sl * sp.getGiaBan() - giam); return ct;
    }

    private static HoaDonNhapHang hoaDonNhap(String ma, NhanVien nv, Nhacungcap ncc, SanPham sp, int sl, LocalDate ngay, boolean daNhap) {
        HoaDonNhapHang hd = new HoaDonNhapHang(nv.getMaNV()); hd.setMaHoaDon(ma);
        hd.setNgayLap(ngay.atTime(9, 0)); hd.setMaNhaCungCap(ncc.getMaDoiTac()); hd.setDuyet(daNhap); hd.setDaNhapSP(daNhap);
        CTHoaDonNhap ct = new CTHoaDonNhap(); ct.setMaSanpham(sp.getMaSanPham());
        ct.setTenSanPham(sp.getTenSanPham()); ct.setSoLuong(sl); ct.setDonGia(sp.getGiaNhap());
        hd.getCtHoaDonNhaps().add(ct); return hd;
    }

    private static void congNo(DanhSachCongNo ds, HoaDonNhapHang hd, Nhacungcap ncc, String phieu, LocalDate han, boolean daTra) throws Exception {
        them(ds, "maHoaDonCongNo", hd.getMaHoaDon()); them(ds, "maPhieuNhapCongNo", phieu);
        them(ds, "maNhaCungCapCongNo", ncc.getMaDoiTac()); them(ds, "tenNhaCungCapCongNo", ncc.getTenDoiTac());
        them(ds, "soTienCongNo", hd.tinhTongTien()); them(ds, "ngayDenHanCongNo", han);
        them(ds, "ngayThanhToanCongNo", daTra ? LocalDate.now().minusDays(1) : null);
        them(ds, "nganHangThanhToanCongNo", daTra ? "Ngân hàng mẫu" : null); them(ds, "daThanhToanCongNo", daTra);
    }

    private static void kiemTra(GiaoDien gd) throws Exception {
        QuanLySanPham sp = lay(gd, "dsSanPham");
        if (sp.getDanhSachSanPham().size() != 8 || ((DSKhachHang) lay(gd, "dsKhachHang")).getSoLuong() != 3
                || ((DSNhanVien) lay(gd, "dsNhanVien")).getDanhSachNhanVien().size() != 4
                || ((Danhsachdoitac) lay(gd, "dsDoiTac")).getDanhSachDoiTac().size() != 3
                || ((Danhsachkhuyenmai) lay(gd, "dsKhuyenMai")).getSoLuongKhuyenMai() != 4
                || ((List<?>) lay(lay(gd, "dsCongNo"), "maHoaDonCongNo")).size() != 2
                || ((DanhSachSPKM) lay(gd, "dsSanPhamKhuyenMai")).getSoLuongSanPham() != 4
                || ((DanhSachHoaDonBan) lay(gd, "dsHoaDonBan")).getDsHoaDonBanHang().size() != 3
                || ((DanhSachHoaDonNhap) lay(gd, "dsHoaDonNhap")).getDsHoaDonNhapHang().size() != 3) {
            throw new AssertionError("Dữ liệu mẫu chưa đủ.");
        }
        Nhacungcap ncc = (Nhacungcap) ((Danhsachdoitac) lay(gd, "dsDoiTac")).getDanhSachDoiTac().get(0);
        if (ncc.getSanPhamCungCap().getDanhSachSanPham().get(0) != sp.getDanhSachSanPham().get(0)) {
            throw new AssertionError("Liên kết sản phẩm và nhà cung cấp không đúng.");
        }
    }

    @SuppressWarnings("unchecked")
    private static <T> T lay(Object obj, String ten) throws Exception {
        Field f = obj.getClass().getDeclaredField(ten); f.setAccessible(true); return (T) f.get(obj);
    }
    private static void them(Object obj, String ten, Object val) throws Exception {
        List<Object> ds = lay(obj, ten); ds.add(val);
    }
}
