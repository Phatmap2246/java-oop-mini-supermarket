package utils;

import java.util.Locale;

/** Căn nhãn thống nhất cho thông tin in ra màn hình. */
public final class HienThi {
    private HienThi() { }

    public static String canLe(String noiDung) {
        StringBuilder ketQua = new StringBuilder();
        for (String dong : noiDung.split("\\R|\\s*\\|\\s*")) {
            dong = dong.trim();
            if (dong.isEmpty()) continue;
            int viTri = dong.indexOf(':');
            if (ketQua.length() > 0) ketQua.append(" | ");
            if (viTri < 0) {
                ketQua.append("  ").append(dong);
                continue;
            }
            String nhan = dong.substring(0, viTri).trim();
            String giaTri = dong.substring(viTri + 1).trim();
            String nhanThuong = nhan.toLowerCase(Locale.ROOT);
            if (giaTri.matches("-?\\d+") && (nhanThuong.contains("giá")
                    || nhanThuong.contains("tiền") || nhanThuong.contains("lương")
                    || nhanThuong.contains("phụ cấp") || nhanThuong.contains("chi tiêu")
                    || nhanThuong.contains("chi phí") || nhanThuong.contains("hạn mức"))) {
                try { giaTri = String.format(Locale.forLanguageTag("vi-VN"), "%,d", Long.parseLong(giaTri)) + " VNĐ"; }
                catch (NumberFormatException e) { /* Giữ nguyên giá trị ngoài giới hạn long. */ }
            }
            int rong = nhanThuong.contains("nội dung") || nhanThuong.contains("địa chỉ") ? 70
                    : nhanThuong.contains("tên") || nhanThuong.contains("khuyến mãi") ? 48 : 38;
            String o = nhan + ": " + giaTri;
            ketQua.append(String.format("%-" + rong + "s", o));
        }
        return ketQua.toString();
    }
}
