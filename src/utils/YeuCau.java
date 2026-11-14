package utils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class YeuCau {
    private String maNV;
    private String vaitro;
    private String noiDung;
    private String trangThai;

    public YeuCau() {
        this("", "", "", "ChoDuyet");
    }

    public YeuCau(String maNV, String vaitro, String noiDung, String trangThai) {
        this.maNV = maNV;
        this.vaitro = vaitro;
        this.noiDung = noiDung;
        this.trangThai = trangThai;
    }

    public String getMaNV() {
        return maNV;
    }

    public void setMaNV(String maNV) {
        if (maNV == null || maNV.isBlank()) {
            throw new IllegalArgumentException("MaNV không được để trống.");
        }
        this.maNV = maNV;
    }

    public String getVaitro() {
        return vaitro;
    }

    public void setVaitro(String vaitro) {
        if (vaitro == null || vaitro.isBlank()) {
            throw new IllegalArgumentException("Vaitro không được để trống.");
        }
        this.vaitro = vaitro;
    }

    public String getNoiDung() {
        return noiDung;
    }

    public void setNoiDung(String noiDung) {
        if (noiDung == null || noiDung.isBlank()) {
            throw new IllegalArgumentException("NoiDung không được để trống.");
        }
        this.noiDung = noiDung;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        if (trangThai == null || trangThai.isBlank()) {
            throw new IllegalArgumentException("TrangThai không được để trống.");
        }
        this.trangThai = trangThai;
    }

    @Override
    public String toString() {
        return "YeuCau{"
                + "maNV='" + maNV + '\''
                + ", vaitro='" + vaitro + '\''
                + ", noiDung='" + noiDung + '\''
                + ", trangThai='" + trangThai + '\''
                + '}';
    }

    public static void themVaoFile(YeuCau yeuCau, Path filePath) throws IOException {
        ArrayList<BanGhiDataBase> danhSach = docDanhSach(filePath);
        danhSach.add(new BanGhiDataBase(yeuCau));
        ghiDanhSach(filePath, danhSach);
    }

    public static ArrayList<BanGhiDataBase> docDanhSach(Path filePath) throws IOException {
        if (!Files.exists(filePath) || Files.size(filePath) == 0) {
            return new ArrayList<>();
        }

        List<String> dongDuLieu = Files.readAllLines(filePath, StandardCharsets.UTF_8);
        if (dongDuLieu.isEmpty()) {
            return new ArrayList<>();
        }

        String header = DataBase.getCSVHeader(new BanGhiDataBase());
        if (header.equals(dongDuLieu.get(0))) {
            ArrayList<BanGhiDataBase> danhSach =
                    DataBase.docfile(BanGhiDataBase.class, filePath.toString());
            if (danhSach.size() != dongDuLieu.size() - 1
                    || danhSach.stream().anyMatch(ghi -> ghi == null)) {
                throw new IOException("Không thể đọc đầy đủ dữ liệu yêu cầu từ " + filePath);
            }
            return danhSach;
        }

        ArrayList<BanGhiDataBase> danhSach = new ArrayList<>();
        for (String dong : dongDuLieu) {
            String[] truong = dong.split("\\|", -1);
            if (truong.length == 4) {
                danhSach.add(new BanGhiDataBase(
                        new YeuCau(truong[0], truong[1], truong[2], truong[3])));
            } else if (!dong.isEmpty()) {
                danhSach.add(BanGhiDataBase.dongCuSaiDinhDang(dong));
            }
        }
        return danhSach;
    }

    public static void ghiDanhSachYeuCau(Path filePath, ArrayList<YeuCau> danhSach) throws IOException {
        ArrayList<BanGhiDataBase> moi = new ArrayList<>();
        for (YeuCau yc : danhSach) moi.add(new BanGhiDataBase(yc));
        // Giữ lại các dòng lịch sử sai định dạng như luồng duyệt yêu cầu hiện tại.
        for (BanGhiDataBase ghi : docDanhSach(filePath)) if (ghi.laDongLoi()) moi.add(ghi);
        ghiDanhSach(filePath, moi);
    }

    public static void ghiDanhSach(Path filePath, ArrayList<BanGhiDataBase> danhSach)
            throws IOException {
        Path thuMuc = filePath.toAbsolutePath().getParent();
        if (thuMuc != null) {
            Files.createDirectories(thuMuc);
        }

        if (danhSach.isEmpty()) {
            Files.writeString(filePath, DataBase.getCSVHeader(new BanGhiDataBase()) + System.lineSeparator(), StandardCharsets.UTF_8);
            return;
        }

        ArrayList<Object> cacBanGhi = new ArrayList<>(danhSach);
        DataBase.ghiVaoFile(cacBanGhi, filePath.toString(), true);

        ArrayList<BanGhiDataBase> daGhi =
                DataBase.docfile(BanGhiDataBase.class, filePath.toString());
        if (daGhi.size() != danhSach.size()) {
            throw new IOException("Không ghi đủ dữ liệu yêu cầu vào " + filePath);
        }
        for (int i = 0; i < danhSach.size(); i++) {
            BanGhiDataBase mongDoi = danhSach.get(i);
            BanGhiDataBase thucTe = daGhi.get(i);
            if (!mongDoi.maNV.equals(thucTe.maNV)
                    || !mongDoi.vaitro.equals(thucTe.vaitro)
                    || !mongDoi.noiDungBase64.equals(thucTe.noiDungBase64)
                    || !mongDoi.trangThai.equals(thucTe.trangThai)
                    || !mongDoi.dongLoiBase64.equals(thucTe.dongLoiBase64)) {
                throw new IOException("Dữ liệu yêu cầu ghi ra không khớp: " + filePath);
            }
        }
    }

    public static final class BanGhiDataBase {
        private String maNV = "";
        private String vaitro = "";
        private String noiDungBase64 = "";
        private String trangThai = "";
        private String dongLoiBase64 = "";

        public BanGhiDataBase() {
        }

        private BanGhiDataBase(YeuCau yeuCau) {
            maNV = yeuCau.maNV;
            vaitro = yeuCau.vaitro;
            noiDungBase64 = maHoa(yeuCau.noiDung);
            trangThai = yeuCau.trangThai;
        }

        private static BanGhiDataBase dongCuSaiDinhDang(String dong) {
            BanGhiDataBase ghi = new BanGhiDataBase();
            ghi.dongLoiBase64 = maHoa(dong);
            return ghi;
        }

        public String getMaNV() {
            return maNV;
        }

        public String getVaitro() {
            return vaitro;
        }

        public String getNoiDung() {
            return giaiMa(noiDungBase64);
        }

        public String getTrangThai() {
            return trangThai;
        }

        public void setTrangThai(String trangThai) {
        if (trangThai == null || trangThai.isBlank()) {
            throw new IllegalArgumentException("TrangThai không được để trống.");
        }
            this.trangThai = trangThai;
        }

        public boolean laDongLoi() {
            return !dongLoiBase64.isEmpty();
        }

        public String getDongLoi() {
            return giaiMa(dongLoiBase64);
        }

        private static String maHoa(String giaTri) {
            return Base64.getUrlEncoder().withoutPadding()
                    .encodeToString(giaTri.getBytes(StandardCharsets.UTF_8));
        }

        private static String giaiMa(String giaTri) {
            return new String(Base64.getUrlDecoder().decode(giaTri), StandardCharsets.UTF_8);
        }
    }

    public static YeuCau taoYeuCau(Scanner sc) {
        System.out.print("Nhập mã nhân viên: ");
        String maNV = sc.nextLine().trim();

        System.out.print("Nhập vai trò: ");
        String vaitro = sc.nextLine().trim();

        System.out.print("Nhập nội dung yêu cầu: ");
        String noiDung = sc.nextLine().trim();

        return new YeuCau(maNV, vaitro, noiDung, "ChoDuyet");
    }
    
}
