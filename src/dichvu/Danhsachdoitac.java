package dichvu ;

import java.util.Locale;

import java.util.ArrayList;
import java.util.Scanner;
import utils.Validation;

public class Danhsachdoitac {
    private ArrayList<Doitac> danhSachDoiTac ;
    public Danhsachdoitac()
    {
        danhSachDoiTac = new ArrayList<>() ;
    }
    public Danhsachdoitac( ArrayList<Doitac> danhSachDoiTac)
    {
        this.danhSachDoiTac = danhSachDoiTac == null ? new ArrayList<>() : danhSachDoiTac;
    }

    public ArrayList<Doitac> getDanhSachDoiTac() {
        return danhSachDoiTac;
    }

    public void setDanhSachDoiTac(ArrayList<Doitac> danhSachDoiTac) {
        if (danhSachDoiTac == null) {
            throw new IllegalArgumentException("Danh sách không được null.");
        }
        for (Object phanTu : danhSachDoiTac) {
            if (phanTu == null) throw new IllegalArgumentException("Danh sách có phần tử null.");
        }

        this.danhSachDoiTac = danhSachDoiTac;
    }
    public void taoDuLieuDoiTac(Scanner sc)
    {
        System.out.println("Tạo danh sách đối tác!");
        do {
            themDoiTac(sc);}
        while (Validation.luaChon(sc, "Bạn có muốn tiếp tục thêm đối tác(1/0): ", 0, 1) == 1);
        System.out.println("Tạo dữ liệu đối tác thành công!");
    }
    public void inDanhSachDoiTac()
    {
        System.out.println("Danh sách đối tác!");
        System.out.println("Đang tải danh sách...");
        Validation.danghoatdong(1);
        for (Doitac doiTac : danhSachDoiTac)
            if (doiTac != null && doiTac.isTonTai()) System.out.println(doiTac);
    }
    public void inDanhSachNhaCungCap()
    {
        System.out.println("======================DANH SÁCH NHÀ CUNG CẤP=======================");
        System.out.println("Đang tải danh sách...");
        Validation.danghoatdong(1);
        for (Doitac doiTac : danhSachDoiTac)
            if (doiTac instanceof Nhacungcap && doiTac.isTonTai()) System.out.println(doiTac);
    }

    public void themDoiTac(Scanner sc) {
        themNhaCungCap(sc);
    }

    public void themNhaCungCap(Scanner sc)
    {
        Nhacungcap nhaCungCap = new Nhacungcap();
        nhaCungCap.setDoiTac(sc);
        kiemTraTrung(sc, nhaCungCap);
        danhSachDoiTac.add(nhaCungCap);
        System.out.println("Thêm nhà cung cấp thành công!");
    }
    public void themKDoiTac( Scanner sc , int k)
    {
        System.out.println("Thêm hợp tác với " + k + " đối tác!");
        for ( int i = 0 ; i < k ; i++ )
        {
            themDoiTac(sc);
        }
        System.out.println("Thêm " + k + " đối tác thành công!");
    }
    public void suaThongTinDoiTac(String maDoiTac, Scanner sc)
    {
        try {

            int index = traCuuThongTinDoiTac(maDoiTac);
            if (index == -1)
            {
                System.out.println("Mã đối tác không tồn tại!");
                return;
            }
            Doitac doiTac = danhSachDoiTac.get(index);
            System.out.println("Chọn thông tin cần sửa:");
            System.out.println("1. Tên đối tác!");
            System.out.println("2. Số điện thoại!");
            System.out.println("3. Địa chỉ!");
            System.out.println("4. Email!");
            System.out.println("5. Mã số thuế!");
            if (doiTac instanceof Nhacungcap)
            {
                System.out.println("6. Ngành hàng cung cấp!");
                System.out.println("7. Hạn mức nợ công!");
            }
            int luaChon = Validation.luaChon(sc, "Nhập lựa chọn: ", 1,
                    doiTac instanceof Nhacungcap ? 7 : 5);
            switch (luaChon)
            {
                case 1:
                    System.out.println("Nhập tên đối tác mới: ");
                    doiTac.setTenDoiTac(sc.nextLine());
                    break;
                case 2:
                    doiTac.setSoDienThoai(Validation.inputStringRegex(sc, "Nhập số điện thoại mới: ",
                            Validation.PHONE_REGEX, "Số điện thoại không đúng định dạng!"));
                    break;
                case 3:
                    System.out.println("Nhập địa chỉ mới: ");
                    doiTac.setDiaChi(sc.nextLine());
                    break;
                case 4:
                    doiTac.setEmail(Validation.inputStringRegex(sc, "Nhập email mới: ",
                            Validation.EMAIL_REGEX, "Email không đúng định dạng!"));
                    break;
                case 5:
                    System.out.println("Nhập mã số thuế mới: ");
                    doiTac.setMaSoThue(sc.nextLine());
                    break;
                case 6:
                    System.out.println("Nhập ngành hàng cung cấp mới: ");
                    assert doiTac instanceof Nhacungcap;
                    ((Nhacungcap) doiTac).setNganhHangCungCap(sc.nextLine());
                    break;
                case 7:
                    long hanMuc;
                    do {
                        hanMuc = Validation.inputLong(sc, "Nhập hạn mức nợ công mới (không âm): ");
                    } while (hanMuc < 0);
                    assert doiTac instanceof Nhacungcap;
                    ((Nhacungcap) doiTac).setHanMucNoCong(hanMuc);
                    break;

            }
            System.out.println("Sửa thông tin đối tác thành công!");
        } catch (IllegalArgumentException e) {
            System.out.println("[!] " + e.getMessage());
        }
    }
    public void xoaDoiTac(String maDoiTac)
    {
        int index = traCuuThongTinDoiTac(maDoiTac);
        if (index == -1)
        {
            System.out.println("Mã đối tác không tồn tại!");
            return;
        }
        danhSachDoiTac.get(index).setTonTai(false);
        System.out.println("Xoá đối tác thành công!");
    }
    public int traCuuThongTinDoiTac(String maDoiTac)
    {
        for (int i = 0; i < danhSachDoiTac.size(); i++)
            if (danhSachDoiTac.get(i) != null && danhSachDoiTac.get(i).isTonTai() && (maDoiTac != null && maDoiTac.equalsIgnoreCase(danhSachDoiTac.get(i).getMaDoiTac()))) return i;
        System.out.println("Không tìm thấy đối tác hoặc đối tác đã bị xóa!");
        return -1;
    }
    public void timKiemDoiTac(String maDoiTac)
    {
        int index = traCuuThongTinDoiTac(maDoiTac);
        if (index == -1)
        {
            System.out.println("Mã đối tác không tồn tại!");
            return;
        }
        System.out.println("Đang tra cứu...");
        Validation.danghoatdong(1);
        System.out.println(danhSachDoiTac.get(index));
    }

    public void timKiemTheoNhieuKhoa(Scanner sc) {
        while (true) {
            System.out.println("================ TÌM KIẾM ĐỐI TÁC ================");
            System.out.println("1. Mã đối tác");
            System.out.println("2. Tên đối tác");
            System.out.println("3. Loại đối tác");
            System.out.println("4. Địa chỉ");
            System.out.println("5. Số điện thoại");
            System.out.println("6. Mã số thuế");
            System.out.println("7. Email");
            System.out.println("0. Trở về");
            int luaChon = Validation.luaChon(sc, "Nhập lựa chọn: ", 0, 7);
            if (luaChon == 0) return;
            boolean flag = false;
            System.out.println("Kết quả tra cứu:");
            switch (luaChon) {
                case 1: {
                    String tuKhoa = Validation.inputStringRegex(sc, "Nhập mã đối tác cần tìm: ", Validation.MaDT_REGEX, "Mã đối tác không đúng định dạng. Vui lòng nhập lại!");
                    System.out.println("Đang tra cứu...");
                    Validation.danghoatdong(1);
                    for (Doitac doiTac : danhSachDoiTac) {
                        if (doiTac != null && doiTac.isTonTai() && ((doiTac.getMaDoiTac()) != null && doiTac.getMaDoiTac().equalsIgnoreCase(tuKhoa))) {
                            System.out.println(doiTac);
                            flag = true;
                        }
                    }
                    break;
                }
                case 2: {
                    String tuKhoa = Validation.inputStringRegex(sc, "Nhập tên đối tác cần tìm: ", ".+", "Tên đối tác không được rỗng.");
                    System.out.println("Đang tra cứu...");
                    Validation.danghoatdong(1);
                    for (Doitac doiTac : danhSachDoiTac) {
                        if (doiTac != null && doiTac.isTonTai() && ((doiTac.getTenDoiTac()) != null && (doiTac.getTenDoiTac()).toLowerCase(Locale.ROOT).contains(tuKhoa.toLowerCase(Locale.ROOT)))) {
                            System.out.println(doiTac);
                            flag = true;
                        }
                    }
                    break;
                }
                case 3: {
                    String tuKhoa = Validation.inputStringRegex(sc, "Nhập loại đối tác cần tìm: ", ".+", "Loại đối tác không được rỗng.");
                    System.out.println("Đang tra cứu...");
                    Validation.danghoatdong(1);
                    for (Doitac doiTac : danhSachDoiTac) {
                        if (doiTac != null && doiTac.isTonTai() && ((doiTac.getLoaiDoiTac()) != null && (doiTac.getLoaiDoiTac()).toLowerCase(Locale.ROOT).contains(tuKhoa.toLowerCase(Locale.ROOT)))) {
                            System.out.println(doiTac);
                            flag = true;
                        }
                    }
                    break;
                }
                case 4: {
                    String tuKhoa = Validation.inputStringRegex(sc, "Nhập địa chỉ cần tìm: ", ".+", "Địa chỉ không được rỗng.");
                    System.out.println("Đang tra cứu...");
                    Validation.danghoatdong(1);
                    for (Doitac doiTac : danhSachDoiTac) {
                        if (doiTac != null && doiTac.isTonTai() && ((doiTac.getDiaChi()) != null && (doiTac.getDiaChi()).toLowerCase(Locale.ROOT).contains(tuKhoa.toLowerCase(Locale.ROOT)))) {
                            System.out.println(doiTac);
                            flag = true;
                        }
                    }
                    break;
                }
                case 5: {
                    String tuKhoa = Validation.inputStringRegex(sc, "Nhập số điện thoại cần tìm: ", Validation.PHONE_REGEX, "Số điện thoại không đúng định dạng. Vui lòng nhập lại!");
                    System.out.println("Đang tra cứu...");
                    Validation.danghoatdong(1);
                    for (Doitac doiTac : danhSachDoiTac) {
                        if (doiTac != null && doiTac.isTonTai() && ((doiTac.getSoDienThoai()) != null && doiTac.getSoDienThoai().equalsIgnoreCase(tuKhoa))) {
                            System.out.println(doiTac);
                            flag = true;
                        }
                    }
                    break;
                }
                case 6: {
                    String tuKhoa = Validation.inputStringRegex(sc, "Nhập mã số thuế cần tìm: ", Validation.MaSoThue_REGEX, "Mã số thuế không đúng định dạng. Vui lòng nhập lại!");
                    System.out.println("Đang tra cứu...");
                    Validation.danghoatdong(1);
                    for (Doitac doiTac : danhSachDoiTac) {
                        if (doiTac != null && doiTac.isTonTai() && ((doiTac.getMaSoThue()) != null && doiTac.getMaSoThue().equalsIgnoreCase(tuKhoa))) {
                            System.out.println(doiTac);
                            flag = true;
                        }
                    }
                    break;
                }
                case 7: {
                    String tuKhoa = Validation.inputStringRegex(sc, "Nhập email cần tìm: ", Validation.EMAIL_REGEX, "Email không đúng định dạng. Vui lòng nhập lại!");
                    System.out.println("Đang tra cứu...");
                    Validation.danghoatdong(1);
                    for (Doitac doiTac : danhSachDoiTac) {
                        if (doiTac != null && doiTac.isTonTai() && ((doiTac.getEmail()) != null && doiTac.getEmail().equalsIgnoreCase(tuKhoa))) {
                            System.out.println(doiTac);
                            flag = true;
                        }
                    }
                    break;
                }
                default:
                    continue;
            }
            if (!flag) System.out.println("Không tìm thấy kết quả phù hợp!");
        }
    }

    public Doitac traCuuDoiTac(String maDoiTac)
    {
        int index = traCuuThongTinDoiTac(maDoiTac);
        if (index == -1) return null;
        return danhSachDoiTac.get(index);
    }

    public ArrayList<Nhacungcap> layDanhSachNhaCungCap()
    {
        ArrayList<Nhacungcap> dsNhaCungCap = new ArrayList<>();
        for (Doitac doiTac : danhSachDoiTac) {
            if (doiTac instanceof Nhacungcap) dsNhaCungCap.add((Nhacungcap) doiTac);
        }
        return dsNhaCungCap;
    }
    public void kiemTraTrung(Scanner sc, Doitac doiTac)
    {
        while (traCuuThongTinDoiTac(doiTac.getMaDoiTac()) != -1)
        {
            System.out.println("Mã đối tác đã tồn tại vui lòng nhập mã mới!");
            doiTac.setMaDoiTac(Validation.inputStringRegex(sc, "Nhập mã đối tác: ",
                    Validation.MaDT_REGEX, "Mã đối tác không đúng định dạng (VD: NCC001)!"));
        }
    }

    public void quanLyDanhSach(Scanner sc)
    {
        int luaChon ;
        do {
            System.out.println("===================QUẢN LÝ ĐỐI TÁC===================");
            System.out.println("1. Nhập danh sách đối tác!");
            System.out.println("2. Xuất danh sách đối tác!");
            System.out.println("3. Thêm đối tác!");
            System.out.println("4. Thêm k đối tác!");
            System.out.println("5. Sửa đối tác!");
            System.out.println("6. Xóa đối tác!");
            System.out.println("7. Tìm kiếm đối tác!");
            System.out.println("0. Trở về.");
            luaChon = Validation.luaChon(sc, "Nhập lựa chọn: ", 0, 7);
            switch (luaChon)
            {
                case 1: {
                    taoDuLieuDoiTac(sc);
                    break;
                }
                case 2: {
                    inDanhSachDoiTac();
                    break;
                }
                case 3: {
                    themDoiTac(sc);
                    break;
                }
                case 4: {
                    int k = Validation.inputInt(sc, "Nhập số phần tử cần thêm: ");
                    themKDoiTac(sc, k);
                    break;
                }
                case 5: {
                    String ma = Validation.inputStringRegex(sc, "Nhập mã đối tác: ", Validation.MaDT_REGEX, "Mã đối tác không đúng định dạng!");
                    suaThongTinDoiTac(ma, sc);
                    break;
                }
                case 6: {
                    String ma = Validation.inputStringRegex(sc, "Nhập mã đối tác: ", Validation.MaDT_REGEX, "Mã đối tác không đúng định dạng!");
                    xoaDoiTac(ma);
                    break;
                }
                case 7: {
                    timKiemTheoNhieuKhoa(sc);
                    break;
                }
                case 0:
                    break;
                default:
                    System.out.println("Lựa chọn không hợp lệ!");
                    break;
            }
        } while (luaChon != 0);
    }
}
