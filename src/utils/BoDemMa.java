package utils;

/** Bộ đếm mã của sáu nhóm dữ liệu, truy cập tường minh. */
public final class BoDemMa {
    private BoDemMa() { }
    public static int[] chup() {
        return new int[]{sanpham.SanPham.getSoThuTu(), person.TaiKhoanNhanVien.getSoThuTu(), person.KhachHang.getSoThuTu(), hoadon.HoaDon.getSoThuTu(), dichvu.Khuyenmai.getSoThuTu(), doitac.Nhacungcap.getSoThuTuNCC()};
    }
    public static void khoiPhuc(int[] values) {
        DongDuLieu.yeuCau(values.length == 6, "Sai bộ đếm mã.");
        for (int n : values) DongDuLieu.yeuCau(n > 0, "Bộ đếm mã phải dương.");
        sanpham.SanPham.datBoDemFile(values[0]);
        person.TaiKhoanNhanVien.datBoDemFile(values[1]);
        person.KhachHang.datBoDemFile(values[2]);
        hoadon.HoaDon.datBoDemFile(values[3]);
        dichvu.Khuyenmai.datBoDemFile(values[4]);
        doitac.Nhacungcap.datBoDemFile(values[5]);
    }
    public static void hopNhat(int[] values) {
        int[] current = chup();
        for (int i = 0; i < current.length; i++) current[i] = Math.max(current[i], values[i]);
        khoiPhuc(current);
    }
}
