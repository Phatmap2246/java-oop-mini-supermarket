package hoadon;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import dichvu.DanhSachSPKM;
import dichvu.Danhsachkhuyenmai;
import sanpham.SanPham;

public class DanhSachHoaDonBan {
    private ArrayList<HoaDonBanHang> dsHoaDonBanHang;

    public DanhSachHoaDonBan(){
        this.dsHoaDonBanHang = new ArrayList<>();
    }

    public DanhSachHoaDonBan(ArrayList<HoaDonBanHang> danhSachHoaDonBan){
        this.dsHoaDonBanHang = danhSachHoaDonBan;
    }

    public void themHoaDon(Scanner sc, DanhSachSPKM danhSachSPKM, Danhsachkhuyenmai danhSachKhuyenMai, List<SanPham> danhSachSanPham){
        HoaDonBanHang hdb = new HoaDonBanHang();
        if (hdb.nhapHoaDon(sc, danhSachSPKM, danhSachKhuyenMai, danhSachSanPham)) {
            hdb.taoMaHoaDonBan();
            dsHoaDonBanHang.add(hdb);
            System.out.println("Thêm hóa đơn "+hdb.getMaHoaDon()+" Thành công!");
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
        
}
