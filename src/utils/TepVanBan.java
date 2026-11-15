package utils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;

/** Đọc từng dòng và ghi từng dòng TXT UTF-8. */
public final class TepVanBan {
    public static final String TIEU_DE = "#SIEUTHI_TXT_3";
    private TepVanBan() { }

    public static boolean dinhDangMoi(String tenFile) throws IOException {
        try (BufferedReader r = Files.newBufferedReader(Paths.get(tenFile), StandardCharsets.UTF_8)) {
            String dau = r.readLine();
            return TIEU_DE.equals(dau);
        }
    }

    public static String nhom(String tenFile) throws IOException {
        try (BufferedReader r = Files.newBufferedReader(Paths.get(tenFile), StandardCharsets.UTF_8)) {
            if (!TIEU_DE.equals(r.readLine())) throw new IOException("Sai tiêu đề TXT: " + tenFile);
            String dong = r.readLine();
            if (dong == null || !dong.startsWith("#NHOM;")) throw new IOException("Thiếu nhóm dữ liệu: " + tenFile);
            return dong.substring(6);
        }
    }

    public static void ghi(String tenFile, String nhom, List<String> cacDong) throws IOException {
        for (String dong : cacDong) {
            if (dong == null || dong.isBlank() || dong.startsWith("#") || dong.contains("\n") || dong.contains("\r")) {
                throw new IOException("Dòng TXT không hợp lệ.");
            }
        }
        Path file = Paths.get(tenFile).toAbsolutePath();
        Files.createDirectories(file.getParent());
        try (BufferedWriter bw = Files.newBufferedWriter(file, StandardCharsets.UTF_8)) {
            bw.write(TIEU_DE); bw.newLine();
            bw.write("#NHOM;" + nhom); bw.newLine();
            int[] dem = BoDemMa.chup();
            bw.write(DongDuLieu.ghep("#BODEM", dem[0], dem[1], dem[2], dem[3], dem[4], dem[5])); bw.newLine();
            for (String dong : cacDong) {
                bw.write(dong);
                bw.newLine();
            }
        }
    }

    public static ArrayList<String> doc(String tenFile) throws IOException {
        ArrayList<String> cacDong = new ArrayList<>();
        int[] dem = null;
        int soDong = 1;
        try (BufferedReader br = Files.newBufferedReader(Paths.get(tenFile), StandardCharsets.UTF_8)) {
            if (!TIEU_DE.equals(br.readLine())) throw new IOException("Sai tiêu đề TXT.");
            String dong;
            while ((dong = br.readLine()) != null) {
                soDong++;
                if (dong.startsWith("#BODEM;")) {
                    String[] p = dong.split(";", -1);
                    DongDuLieu.yeuCau(p.length == 7 && dem == null, "Bộ đếm mã không hợp lệ.");
                    dem = new int[6];
                    for (int i = 0; i < dem.length; i++) {
                        dem[i] = Integer.parseInt(p[i + 1]);
                        DongDuLieu.yeuCau(dem[i] > 0, "Bộ đếm mã phải dương.");
                    }
                } else if (!dong.isBlank() && !dong.startsWith("#")) cacDong.add(dong);
            }
        } catch (RuntimeException | IOException e) {
            throw new IOException(tenFile + ", dòng " + soDong + ": " + e.getMessage(), e);
        }
        if (dem != null) BoDemMa.hopNhat(dem);
        return cacDong;
    }

    public static void khongTrung(Set<String> maDaDoc, String ma) {
        DongDuLieu.yeuCau(ma != null && maDaDoc.add(ma.toUpperCase(Locale.ROOT)), "Trùng hoặc thiếu mã: " + ma);
    }
}
