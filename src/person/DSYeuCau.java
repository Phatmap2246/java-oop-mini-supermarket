package person;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import utils.Validation;
import utils.YeuCau;

public class DSYeuCau {
    private ArrayList<YeuCau> list = new ArrayList<>();

    public DSYeuCau() {
    }

    public DSYeuCau(ArrayList<YeuCau> list) {
        if (list == null) throw new IllegalArgumentException("Danh sách yêu cầu không được null.");
        this.list = list;
    }

    public ArrayList<YeuCau> getList() {
        return list;
    }

    public void docDanhSachYeuCau(String file) throws IOException {
        ArrayList<YeuCau.BanGhiDataBase> danhSach = YeuCau.docDanhSach(Paths.get(file));
        ArrayList<YeuCau> danhSachMoi = new ArrayList<>();
        for (YeuCau.BanGhiDataBase yeuCau : danhSach) {
            if (yeuCau.laDongLoi()) continue;
            danhSachMoi.add(new YeuCau(yeuCau.getMaNV(), yeuCau.getVaitro(),
                    yeuCau.getNoiDung(), yeuCau.getTrangThai()));
        }
        list = danhSachMoi;
    }

    public void duyetYeuCau(Scanner sc) throws IOException {
        Path filePath = Paths.get("YeuCau.txt");
        if (!Files.exists(filePath)) {
            System.out.println("Chưa có file YeuCau.txt.");
            return;
        }

        ArrayList<YeuCau.BanGhiDataBase> dsYeuCau = YeuCau.docDanhSach(filePath);
        boolean coThayDoi = false;
        for (int index = 0; index < dsYeuCau.size(); index++) {
            YeuCau.BanGhiDataBase yeuCau = dsYeuCau.get(index);
            if (yeuCau.laDongLoi()) {
                System.err.println("[!] Dòng yêu cầu sai định dạng, giữ nguyên: " + yeuCau.getDongLoi());
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
            YeuCau.ghiDanhSach(filePath, dsYeuCau);
            System.out.println("[+] Đã cập nhật kết quả duyệt yêu cầu vào YeuCau.txt.");
        } else {
            System.out.println("[i] Không có yêu cầu nào đang ở trạng thái Chờ duyệt.");
        }
    }

    public void xemLichSuDuyet(String file) {
        System.out.println("\n=== LỊCH SỬ YÊU CẦU DUYỆT ===");
        try {
            ArrayList<YeuCau.BanGhiDataBase> dsDuyet = YeuCau.docDanhSach(Paths.get(file));
            if (dsDuyet.isEmpty()) {
                System.out.println("[i] Danh sách yêu cầu trống.");
                return;
            }
            for (YeuCau.BanGhiDataBase yc : dsDuyet) {
                if (yc.laDongLoi()) {
                    System.out.println("[!] Dòng lỗi: " + yc.getDongLoi());
                } else {
                    System.out.printf("Mã NV: %s | Vai trò: %s | Nội dung: %s | Trạng thái: %s%n",
                            yc.getMaNV(), yc.getVaitro(), yc.getNoiDung(), yc.getTrangThai());
                }
            }
        } catch (IOException e) {
            System.out.println("[!] Không đọc được file yêu cầu: " + e.getMessage());
        }
    }

    public void thongKe() {
        Map<String, Integer> yeuCauTheoTrangThai = new HashMap<>();
        for (YeuCau yeuCau : list) {
            if (yeuCau != null)
                yeuCauTheoTrangThai.merge(yeuCau.getTrangThai(), 1, Integer::sum);
        }

        System.out.println("\n--- YÊU CẦU DUYỆT ---");
        System.out.printf("Tổng: %d | Đã duyệt: %d | Chờ duyệt: %d | Không duyệt: %d%n",
                list.size(),
                yeuCauTheoTrangThai.getOrDefault("DaDuyet", 0),
                yeuCauTheoTrangThai.getOrDefault("ChoDuyet", 0),
                yeuCauTheoTrangThai.getOrDefault("KhongDuyet", 0));
    }
}
