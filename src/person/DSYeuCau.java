package person;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import utils.Validation;
import utils.YeuCau;

public class DSYeuCau implements interfaces.IQuanLy<YeuCau> {
    private YeuCau[] list = new YeuCau[0];
    private transient String tenFileNguon;

    public void cauHinhFile(String tenFile) { tenFileNguon = tenFile; }
    private String tenFile() {
        return tenFileNguon == null ? utils.DuongDanLuuTru.file("YeuCau.txt") : tenFileNguon;
    }
    private void napTruocKhiSua() throws IOException {
        if (Files.exists(Paths.get(tenFile()))) docDanhSachYeuCau(tenFile());
    }
    private boolean luuMoi(YeuCau[] moi) {
        try {
            YeuCau.ghiDanhSachYeuCau(Paths.get(tenFile()), new ArrayList<>(Arrays.asList(moi)));
            list = moi;
            return true;
        } catch (IOException e) {
            System.out.println("Không thể lưu yêu cầu: " + e.getMessage());
            return false;
        }
    }

    @Override public void taoDanhSach(Scanner sc) {
        int n = Validation.inputInt(sc, "Nhập số yêu cầu cần thêm: ");
        for (int i = 0; i < n; i++) them(sc);
    }

    @Override public void them(Scanner sc) {
        try {
            napTruocKhiSua();
            YeuCau yc = YeuCau.taoYeuCau(sc);
            if (yc.getMaNV().isBlank() || yc.getVaitro().isBlank() || yc.getNoiDung().isBlank()) {
                System.out.println("Mã nhân viên, vai trò và nội dung không được trống.");
                return;
            }
            YeuCau[] moi = Arrays.copyOf(list, list.length + 1);
            moi[list.length] = yc;
            luuMoi(moi);
        } catch (IOException e) {
            System.out.println("Không thể đọc yêu cầu: " + e.getMessage());
        }
    }

    @Override public boolean sua(String ma, Scanner sc) {
        try {
            napTruocKhiSua();
            int i = timViTri(ma);
            if (i == -1) return false;
            YeuCau cu = list[i];
            String noiDung = Validation.inputStringRegex(sc, "Nội dung mới: ", ".+", "Không được trống.");
            int chon = Validation.luaChon(sc, "Trạng thái: 1. Chờ duyệt | 2. Đã duyệt | 3. Không duyệt: ", 1, 3);
            YeuCau moi = new YeuCau(cu.getMaNV(), cu.getVaitro(), noiDung,
                    chon == 1 ? "ChoDuyet" : chon == 2 ? "DaDuyet" : "KhongDuyet");
            YeuCau[] dsMoi = Arrays.copyOf(list, list.length);
            dsMoi[i] = moi;
            return luuMoi(dsMoi);
        } catch (IOException e) { System.out.println(e.getMessage()); return false; }
    }

    @Override public boolean xoa(String ma) {
        try {
            napTruocKhiSua();
            int i = timViTri(ma);
            if (i == -1) return false;
            YeuCau[] moi = new YeuCau[list.length - 1];
            for (int j = 0, k = 0; j < list.length; j++) {
                if (j != i) {
                    moi[k++] = list[j];
                }
            }
            return luuMoi(moi);
        } catch (IOException e) { System.out.println(e.getMessage()); return false; }
    }

    @Override public void xuatDanhSach() {
        for (YeuCau yc : list) if (yc != null) System.out.println(yc);
    }

    @Override public int timViTri(String ma) {
        if (ma == null || ma.isBlank()) return -1;
        for (int i = 0; i < list.length; i++) {
            if (list[i] != null && ma.trim().equalsIgnoreCase(list[i].getMaNV())) return i;
        }
        return -1;
    }

    @Override public YeuCau timMa(String ma) {
        int i = timViTri(ma);
        return i == -1 ? null : list[i];
    }

    @Override public YeuCau timTen(String ten) {
        if (ten == null || ten.isBlank()) return null;
        for (YeuCau yc : list) {
            if (yc != null && yc.getNoiDung() != null && yc.getNoiDung().toLowerCase(java.util.Locale.ROOT)
                    .contains(ten.trim().toLowerCase(java.util.Locale.ROOT))) return yc;
        }
        return null;
    }

    @Override public YeuCau timKhoa(String khoa) {
        if (khoa == null || khoa.isBlank()) return null;
        for (YeuCau yc : list) {
            if (yc != null && yc.toString().toLowerCase(java.util.Locale.ROOT)
                    .contains(khoa.trim().toLowerCase(java.util.Locale.ROOT))) return yc;
        }
        return null;
    }

    public DSYeuCau() {
    }

    public DSYeuCau(YeuCau[] list) {
        if (list == null) throw new IllegalArgumentException("Danh sách yêu cầu không được null.");
        this.list = list;
    }

    public YeuCau[] getList() {
        return list;
    }

    /** Thêm vào bộ nhớ khi khôi phục dữ liệu nhân viên, không ghi file yêu cầu riêng. */
    public void them(YeuCau yeuCau) {
        if (yeuCau == null) throw new IllegalArgumentException("Yêu cầu không được null.");
        YeuCau[] moi = Arrays.copyOf(list, list.length + 1);
        moi[list.length] = yeuCau;
        list = moi;
    }

    public void ghiFile(String file) throws IOException {
        YeuCau.ghiDanhSachYeuCau(Paths.get(file), new ArrayList<>(Arrays.asList(list)));
    }

    public void docFile(String file) throws IOException {
        docDanhSachYeuCau(file);
    }

    public void docDanhSachYeuCau(String file) throws IOException {
        YeuCau.BanGhiDataBase[] danhSach = YeuCau.docDanhSach(Paths.get(file))
                .toArray(new YeuCau.BanGhiDataBase[0]);
        int count = 0;
        for (YeuCau.BanGhiDataBase yeuCau : danhSach) {
            if (!yeuCau.laDongLoi()) count++;
        }
        YeuCau[] danhSachMoi = new YeuCau[count];
        int idx = 0;
        for (YeuCau.BanGhiDataBase yeuCau : danhSach) {
            if (yeuCau.laDongLoi()) continue;
            danhSachMoi[idx++] = new YeuCau(yeuCau.getMaNV(), yeuCau.getVaitro(),
                    yeuCau.getNoiDung(), yeuCau.getTrangThai());
        }
        list = danhSachMoi;
        tenFileNguon = file;
    }

    public void duyetYeuCau(Scanner sc) throws IOException {
        Path filePath = Paths.get(tenFile());
        if (!Files.exists(filePath)) {
            System.out.println("Chưa có file YeuCau.txt.");
            return;
        }

        YeuCau.BanGhiDataBase[] dsYeuCau = YeuCau.docDanhSach(filePath)
                .toArray(new YeuCau.BanGhiDataBase[0]);
        boolean coThayDoi = false;
        for (int index = 0; index < dsYeuCau.length; index++) {
            YeuCau.BanGhiDataBase yeuCau = dsYeuCau[index];
            if (yeuCau.laDongLoi()) {
                System.err.println("[!] Dòng yêu cầu sai định dạng, giữ nguyên: "
                        + yeuCau.getDongLoi());
                continue;
            }
            if (!yeuCau.getTrangThai().equals("ChoDuyet")) {
                continue;
            }

            System.out.printf("Yêu cầu: %s | %s | %s%n",
                    yeuCau.getMaNV(), yeuCau.getVaitro(), yeuCau.getNoiDung());
            String quyetDinh = Validation.inputStringRegex(
                    sc, "Duyệt: 1 / Không duyệt: 0: ", "^[01]$",
                    "Chỉ được nhập 0 hoặc 1.");
            yeuCau.setTrangThai(quyetDinh.equals("1") ? "DaDuyet" : "KhongDuyet");
            coThayDoi = true;
        }

        if (coThayDoi) {
            YeuCau.ghiDanhSach(filePath, new ArrayList<>(Arrays.asList(dsYeuCau)));
            docDanhSachYeuCau(filePath.toString());
        }
    }

    public void xemLichSuDuyet(String file) {
        try {
            docDanhSachYeuCau(file);
            System.out.println("LỊCH SỬ YÊU CẦU DUYỆT");
            for (YeuCau yc : list) System.out.println(yc);
        } catch (IOException e) {
            System.err.println("Không thể đọc lịch sử yêu cầu: " + e.getMessage());
        }
    }

    public void thongKe() {
        Map<String, Integer> yeuCauTheoTrangThai = new HashMap<>();
        System.out.println("Đang thống kê...");
        Validation.danghoatdong(1);
        for (YeuCau yeuCau : list) {
            if (yeuCau != null)
                yeuCauTheoTrangThai.merge(yeuCau.getTrangThai(), 1, Integer::sum);
        }

        System.out.println("\n--- YÊU CẦU DUYỆT ---");
        System.out.printf("Tổng: %d | Đã duyệt: %d | Chờ duyệt: %d | Không duyệt: %d%n",
                list.length,
                yeuCauTheoTrangThai.getOrDefault("DaDuyet", 0),
                yeuCauTheoTrangThai.getOrDefault("ChoDuyet", 0),
                yeuCauTheoTrangThai.getOrDefault("KhongDuyet", 0));
    }
}
