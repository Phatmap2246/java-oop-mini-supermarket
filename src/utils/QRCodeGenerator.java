package utils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class QRCodeGenerator {
    public static boolean taoAnhQR(String noiDung, String tenFileAnh) {
        try {
            QRCodeWriter writer = new QRCodeWriter();
            Map<com.google.zxing.EncodeHintType, Object> hints = new HashMap<>();
            hints.put(com.google.zxing.EncodeHintType.CHARACTER_SET, "UTF-8");
            
            BitMatrix matrix = writer.encode(noiDung, BarcodeFormat.QR_CODE, 300, 300, hints);
            MatrixToImageWriter.writeToPath(matrix, "PNG", new File(tenFileAnh).toPath());
            return true;
        } catch (Exception e) {
            System.out.println("[!] Không thể tạo ảnh QR: " + e.getMessage());
            return false;
        }
    }
}