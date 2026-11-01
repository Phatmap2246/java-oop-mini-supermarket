package hoadon;

import utils.QRCodeGenerator;
import java.util.Random;
import java.util.Scanner;
import java.io.File;
import java.awt.Desktop;

public class ThanhToanChuyenKhoan implements IThanhToan {
    private String tenNganHang;
    private String soTaiKhoan;
    private String maGiaoDich;
    private String noiDungQR;

    public ThanhToanChuyenKhoan(String tenNganHang, String soTaiKhoan) {
        this.tenNganHang = tenNganHang;
        this.soTaiKhoan = soTaiKhoan;
        this.maGiaoDich = "";
        this.noiDungQR = "";
    }

    public String taoMaQR(double tongTien) {
        this.maGiaoDich = "FT" + (100000 + new Random().nextInt(900000));
        
        this.noiDungQR = "Chuyen tien den: " + tenNganHang + 
                         " | STK: " + soTaiKhoan + 
                         " | So tien: " + tongTien + " VND" +
                         " | Ma GD: " + this.maGiaoDich;
        
        System.out.println("\n[Hệ thống] Đang tạo mã QR thanh toán...");
        
        // Tự động tạo thư mục QR
        File dir = new File("QR");
        if (!dir.exists()) dir.mkdirs();
        
        try {
            String duongDanAnh = "QR/QR_" + this.maGiaoDich + ".png";
            boolean thanhCong = QRCodeGenerator.taoAnhQR(this.noiDungQR, duongDanAnh);
            if (thanhCong) {
                System.out.println("[Hệ thống] Đã tạo file ảnh QR tại: " + duongDanAnh);
            }
        } catch (Throwable e) { 
            System.out.println("[!] CẢNH BÁO: Không tìm thấy thư viện ZXing.");
        }
        
        return this.noiDungQR;
    }

    @Override 
    public boolean thucHienThanhToan(Scanner sc, double tongTien) {
        System.out.println("\n[Hệ thống] Đang chờ thanh toán từ ngân hàng " + tenNganHang + "...");
        System.out.println("[Hệ thống] Mã giao dịch: " + this.maGiaoDich);
        
        // 1. Vẽ QR ra màn hình bằng ký tự Unicode (hiển thị được trên mọi máy)
        System.out.println("\nQuét mã QR sau để thanh toán:");
        inQRRaConsole(this.noiDungQR);
        
        // 2. Tự động bật file ảnh lên màn hình cho ngầu
        try {
            File qrFile = new File("QR/QR_" + this.maGiaoDich + ".png");
            if (qrFile.exists() && Desktop.isDesktopSupported()) {
                Desktop.getDesktop().open(qrFile);
                System.out.println("[Hệ thống] Đã tự động mở file ảnh QR trên màn hình.");
            }
        } catch (Exception e) {
            // Bỏ qua nếu có lỗi
        }
        
        System.out.print("\n[Hệ thống] Nhấn Enter để giả lập ngân hàng báo có tiền...");
        sc.nextLine(); 

        System.out.println("[Hệ thống] Đã nhận được tiền từ ngân hàng " + tenNganHang + "!");
        System.out.println("[Hệ thống] Giao dịch thành công!");
        return true;
    }

    @Override
    public String getTenPhuongThuc() { return "Chuyen Khoan"; }

    @Override
    public void inBienLai() {
        System.out.println(" - Hinh thuc: Chuyen khoan ngan hang");
        System.out.println(" - Ngan hang: " + tenNganHang);
        System.out.println(" - So TK: " + soTaiKhoan);
        System.out.println(" - Ma giao dich: " + maGiaoDich);
    }

    // Hàm vẽ QR bằng ký tự vuông (Không dùng màu, hiển thị tốt trên VS Code)
    private void inQRRaConsole(String noiDung) {
        try {
            com.google.zxing.qrcode.QRCodeWriter writer = new com.google.zxing.qrcode.QRCodeWriter();
            java.util.Map<com.google.zxing.EncodeHintType, Object> hints = new java.util.HashMap<>();
            hints.put(com.google.zxing.EncodeHintType.MARGIN, 1);
            
            com.google.zxing.common.BitMatrix matrix = writer.encode(noiDung, com.google.zxing.BarcodeFormat.QR_CODE, 0, 0, hints);
            int width = matrix.getWidth();
            int height = matrix.getHeight();
            
            String den = "██"; // Khối đen
            String trang = "  "; // Khối trắng
            
            for (int y = 0; y < height; y++) {
                StringBuilder sb = new StringBuilder();
                for (int x = 0; x < width; x++) {
                    sb.append(matrix.get(x, y) ? den : trang);
                }
                System.out.println(sb.toString());
            }
        } catch (Exception e) {
            System.out.println("[!] Khong the ve QR ra man hinh Console.");
        }
    }

    public String getMaGiaoDich() { return maGiaoDich; }
    public String getNoiDungQR() { return noiDungQR; }
}