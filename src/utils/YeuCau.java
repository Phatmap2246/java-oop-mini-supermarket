package utils;

import utils.DongDuLieu;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

public class YeuCau implements interfaces.BanGhiFile {
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
        return utils.HienThi.canLe("Mã nhân viên: " + maNV + " | Vai trò: " + vaitro
                + " | Nội dung: " + noiDung + " | Trạng thái: " + trangThai);
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

        if (TepVanBan.dinhDangMoi(filePath.toString())) {
            ArrayList<BanGhiDataBase> moi = new ArrayList<>();
            for (String dong : TepVanBan.doc(filePath.toString())) {
                if (dong.startsWith("LOICU;")) {
                    String[] cot = dong.split(";", 2);
                    moi.add(BanGhiDataBase.dongCuSaiDinhDang(cot[1]));
                } else {
                    try { moi.add(new BanGhiDataBase(YeuCau.tuDongFile(dong))); }
                    catch (RuntimeException e) { throw new IOException("Yêu cầu không hợp lệ trong " + filePath, e); }
                }
            }
            return moi;
        }
        List<String> dongDuLieu = Files.readAllLines(filePath, StandardCharsets.UTF_8);
        if (dongDuLieu.isEmpty()) {
            return new ArrayList<>();
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
        ArrayList<String> cacDong = new ArrayList<>();
        try {
            for (BanGhiDataBase ghi : danhSach) {
                if (ghi == null) throw new IOException("Yêu cầu null.");
                // Dòng lịch sử lỗi cũ giữ nguyên sau LOICU; không xử lý escape mới.
                String dong = ghi.laDongLoi() ? "LOICU;" + ghi.getDongLoi()
                        : new YeuCau(ghi.maNV, ghi.vaitro, ghi.getNoiDung(), ghi.trangThai).toDongFile();
                cacDong.add(dong);
            }
            TepVanBan.ghi(filePath.toString(), "YeuCau", cacDong);
        } catch (IllegalArgumentException e) { throw new IOException(e.getMessage(), e); }
    }

    public static final class BanGhiDataBase {
        private String maNV = "";
        private String vaitro = "";
        private String noiDung = "";
        private String trangThai = "";
        private String dongLoi = "";

        public BanGhiDataBase() {
        }

        private BanGhiDataBase(YeuCau yeuCau) {
            maNV = yeuCau.maNV;
            vaitro = yeuCau.vaitro;
            noiDung = yeuCau.noiDung;
            trangThai = yeuCau.trangThai;
        }

        private static BanGhiDataBase dongCuSaiDinhDang(String dong) {
            BanGhiDataBase ghi = new BanGhiDataBase();
            ghi.dongLoi = dong;
            return ghi;
        }

        public String getMaNV() {
            return maNV;
        }

        public String getVaitro() {
            return vaitro;
        }

        public String getNoiDung() {
            return noiDung;
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
            return !dongLoi.isEmpty();
        }

        public String getDongLoi() {
            return dongLoi;
        }

    }

    public static YeuCau taoYeuCau(Scanner sc) {
        String maNV = Validation.inputStringRegex(sc, "Nhập mã nhân viên: ", Validation.MaNV_REGEX,
                "Mã nhân viên không đúng định dạng!", person.NhanVien.getSoThuTu());

        System.out.print("Nhập vai trò: ");
        String vaitro = sc.nextLine().trim();

        System.out.print("Nhập nội dung yêu cầu: ");
        String noiDung = sc.nextLine().trim();

        return new YeuCau(maNV, vaitro, noiDung, "ChoDuyet");
    }

    // TXT đơn giản: mỗi thuộc tính ứng với một cột, tách bằng dấu ;.
    @Override public String toDongFile() {
        kiemTraDuLieuFile();
        return DongDuLieu.ghep("YEUCAU", maNV, vaitro, noiDung, trangThai);
    }
    public static YeuCau tuDongFile(String dong) {
        String[] p = dong.split(";", -1);
        DongDuLieu.yeuCau(p.length == 5 && "YEUCAU".equals(p[0]), "Sai dòng YEUCAU (cần 5 cột).");
        YeuCau obj = new YeuCau();
        obj.maNV = p[1];
        obj.vaitro = p[2];
        obj.noiDung = p[3];
        obj.trangThai = p[4];
        obj.kiemTraDuLieuFile();
        return obj;
    }

    public void kiemTraDuLieuFile() {
    }
}
