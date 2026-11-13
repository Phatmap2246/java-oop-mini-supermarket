package hoadon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import dichvu.DanhSachSPKM;
import dichvu.Danhsachkhuyenmai;
import sanpham.QuanLyTonKho;
import sanpham.SanPham;

public class DanhSachHoaDonBan {
    private ArrayList<HoaDonBanHang> dsHoaDonBanHang;

    public DanhSachHoaDonBan(){
        this.dsHoaDonBanHang = new ArrayList<>();
    }

    public DanhSachHoaDonBan(ArrayList<HoaDonBanHang> danhSachHoaDonBan){
        this.dsHoaDonBanHang = danhSachHoaDonBan;
    }
    public ArrayList<HoaDonBanHang> getDS()
    {
        return dsHoaDonBanHang; 
    }
    //Sửa thêm việc trừ sp sau khi bán
    public void themHoaDon(Scanner sc, DanhSachSPKM danhSachSPKM, Danhsachkhuyenmai danhSachKhuyenMai, List<SanPham> danhSachSanPham){
    HoaDonBanHang hdb = new HoaDonBanHang();
    if (hdb.nhapHoaDon(sc, danhSachSPKM, danhSachKhuyenMai, danhSachSanPham)) {
        // Bổ sung trừ tồn kho tại đây sau khi thanh toán thành công
        if (!QuanLyTonKho.truTonKho(danhSachSanPham, hdb.getDanhSachChiTiet())) {
            System.out.println("[!] Cảnh báo: Thanh toán thành công nhưng cập nhật tồn kho thất bại!");
        }
        hdb.taoMaHoaDonBan();
        dsHoaDonBanHang.add(hdb);
        System.out.println("Thêm hóa đơn " + hdb.getMaHoaDon() + " Thành công!");
        return;
    }
    System.out.println("Thêm hóa đơn mới thất bại!");
}

    public void hienThiDanhSach(){
        if (dsHoaDonBanHang.isEmpty()) {
            System.out.println("Danh sách hóa đơn đang trống !");
            return;
        }
        for (HoaDonBanHang hdb : dsHoaDonBanHang){
            hdb.hienThiHoaDon();
        }
    }

    public HoaDonBanHang timKiemHoaDonBanHang(String maHD){
        if (dsHoaDonBanHang.isEmpty()) {
            System.out.println("Danh sách hóa đơn đang trống !");
            return null;
        }
        for(HoaDonBanHang hdb : dsHoaDonBanHang){
            if (hdb.getMaHoaDon().equalsIgnoreCase(maHD)) {
                return hdb;
            }
        }
        return null;
    }

    public int timKiemHoaDonBanHangViTri(String maHD){
        if (dsHoaDonBanHang.isEmpty()) {
            System.out.println("Danh sách hóa đơn đang trống !");
            return -1;
        }
        for(int i =0; i<dsHoaDonBanHang.size();i++){
            if (dsHoaDonBanHang.get(i).getMaHoaDon().equalsIgnoreCase(maHD)) {
                return i ;
            }
        }
        return -1;
    }

    public HoaDonBanHang timKiemHoaDonBanHang_SDT(String SDT){
        if (dsHoaDonBanHang.isEmpty()) {
            System.out.println("Danh sách hóa đơn đang trống !");
            return null;
        }
        for(HoaDonBanHang hdb : dsHoaDonBanHang){
            if (hdb.getSdtDoiTac().equalsIgnoreCase(SDT)) {
                return hdb;
            }
        }
        return null;
    }
    
    public void ghiFileText(String tenFile) {
        try (java.io.BufferedWriter bw = new java.io.BufferedWriter(new java.io.OutputStreamWriter(new java.io.FileOutputStream(tenFile), java.nio.charset.StandardCharsets.UTF_8))) {
            for (HoaDonBanHang hdb : dsHoaDonBanHang) {
                // Lưu thông tin chung: BAN|MaHD|SDT|DaThanhToan
                bw.write("BAN|" + hdb.getMaHoaDon() + "|" + hdb.getSdtDoiTac() + "|" + hdb.isDaThanhToan());
                bw.newLine();
                
                // Lưu chi tiết: CT|MaSP|TenSP|SoLuong|DonGia|SoTienGiam|SoLuongTangKem
                for (CTHoaDonBan ct : hdb.getDanhSachChiTiet()) {
                    bw.write("CT|" + ct.getMaSanPham() + "|" + ct.getTenSanPham() + "|" + 
                             ct.getSoLuong() + "|" + ct.getDonGia() + "|" + 
                             ct.getSoTienGiam() + "|" + ct.getSoLuongTangKem());
                    bw.newLine();
                }
            }
            System.out.println("[+] Đã ghi dữ liệu hóa đơn bán hàng vào file: " + tenFile);
        } catch (Exception e) {
            System.out.println("[!] Lỗi khi ghi file: " + e.getMessage());
        }
    }

    public void docFileText(String tenFile) {
        this.dsHoaDonBanHang.clear(); 
        try (java.io.BufferedReader br = new java.io.BufferedReader(new java.io.InputStreamReader(new java.io.FileInputStream(tenFile), java.nio.charset.StandardCharsets.UTF_8))) {
            String line;
            HoaDonBanHang hdbHienTai = null;
            
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\|", -1);
                if (parts.length > 0) {
                    String loai = parts[0];
                    
                    if (loai.equals("BAN") && parts.length == 4) {
                        if (timKiemHoaDonBanHang(parts[1]) == null) {
                            hdbHienTai = new HoaDonBanHang(parts[1], parts[2]);
                            hdbHienTai.setDaThanhToan(Boolean.parseBoolean(parts[3]));
                            this.dsHoaDonBanHang.add(hdbHienTai);
                        } else {
                            hdbHienTai = null; 
                        }
                    } 
                    else if (loai.equals("CT") && hdbHienTai != null && parts.length == 7) {
                        CTHoaDonBan ct = new CTHoaDonBan(parts[1], parts[2], Integer.parseInt(parts[3]), Long.parseLong(parts[4]));
                        ct.setSoTienGiam(Long.parseLong(parts[5]));
                        ct.setSoLuongTangKem(Integer.parseInt(parts[6]));
                        hdbHienTai.themChiTiet(ct);
                    }
                }
            }
            System.out.println("[+] Đã đọc dữ liệu hóa đơn bán hàng từ file: " + tenFile);
        } catch (Exception e) {
            System.out.println("[!] Lỗi khi đọc file (Có thể file chưa tồn tại): " + e.getMessage());
        }
    }
    // sáng tạo 
    public ArrayList<SanPham> topNSanPhams(int n, List<SanPham> danhSachSanPham) {
        if (n <= 0 || dsHoaDonBanHang == null || danhSachSanPham == null) {
            return new ArrayList<>();
        }

        Map<String, Integer> mapSoLuong = new HashMap<>();
        Map<String, Long> mapDoanhThu = new HashMap<>();

        // 1. Cộng dồn số lượng và doanh thu thực tế của các hóa đơn đã thanh toán
        for (HoaDonBanHang hdb : dsHoaDonBanHang) {
            if (!hdb.isDaThanhToan()) continue;
            for (CTHoaDonBan ct : hdb.getDanhSachChiTiet()) {
                String ma = ct.getMaSanPham().trim().toUpperCase();
                mapSoLuong.merge(ma, ct.getSoLuong(), Integer::sum);
                mapDoanhThu.merge(ma, ct.getThanhTienSauKhuyenMai(), Long::sum);
            }
        }

        // 2. Chỉ giữ lại sản phẩm có doanh thu vượt qua vốn nhập hàng
        ArrayList<SanPham> ketQua = new ArrayList<>();
        for (String ma : mapSoLuong.keySet()) {
            SanPham sp = QuanLyTonKho.timSanPham(danhSachSanPham, ma);
            if (sp != null) {
                long doanhThu = mapDoanhThu.get(ma);
                long vonNhap = (long) mapSoLuong.get(ma) * sp.getGiaNhap();
                if (doanhThu > vonNhap) {
                    ketQua.add(sp);
                }
            }
        }

        // 3. Sắp xếp giảm dần theo số lượng bán và cắt lấy Top N
        ketQua.sort((a, b) -> Integer.compare(
                mapSoLuong.get(b.getMaSanPham().trim().toUpperCase()),
                mapSoLuong.get(a.getMaSanPham().trim().toUpperCase())
        ));

        return new ArrayList<>(ketQua.subList(0, Math.min(n, ketQua.size())));
    }

    public void inTopNSanPhams(int n, List<SanPham> danhSachSanPham) {
        ArrayList<SanPham> topList = topNSanPhams(n, danhSachSanPham);
        System.out.println("\n=== TOP " + n + " SẢN PHẨM BÁN CHẠY (DOANH THU > VỐN) ===");
        if (topList.isEmpty()) {
            System.out.println("[!] Không có sản phẩm nào đạt điều kiện.");
            return;
        }
        for (int i = 0; i < topList.size(); i++) {
            System.out.println((i + 1) + ". " + topList.get(i));
        }
    }
}
