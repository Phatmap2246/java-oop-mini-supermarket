package utils;

import java.nio.file.*;

/** Một nơi khai báo đường dẫn, để dữ liệu không nằm rải rác trong mã nguồn. */
public class DuongDanLuuTru {
    public static Path thuMuc() {
        Path goc = timGoc(Paths.get("").toAbsolutePath());
        if (goc == null) {
            try {
                Path viTriLop = Paths.get(DuongDanLuuTru.class.getProtectionDomain()
                        .getCodeSource().getLocation().toURI());
                goc = timGoc(viTriLop);
            } catch (Exception e) {
                // Khi chạy bản build độc lập, dùng thư mục làm việc.
            }
        }
        if (goc == null) goc = Paths.get("").toAbsolutePath();
        return goc.resolve("src/luutru").normalize();
    }

    public static String file(String tenFile) {
        return thuMuc().resolve(tenFile).toString();
    }

    private static Path timGoc(Path viTri) {
        for (Path p = viTri; p != null; p = p.getParent()) {
            if (Files.isRegularFile(p.resolve("src/Main.java"))
                    && Files.isDirectory(p.resolve("src/luutru"))) return p;
        }
        return null;
    }
}
