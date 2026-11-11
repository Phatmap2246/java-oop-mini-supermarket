package dichvu ;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;

import interfaces.IQuanLy;
import utils.Validation;

public class Danhsachdoitac implements IQuanLy<Doitac> {
    private ArrayList<Doitac> danhSachDoiTac ;
    public Danhsachdoitac()
    {
        danhSachDoiTac = new ArrayList<>() ;
    }
    public Danhsachdoitac( ArrayList<Doitac> danhSachDoiTac)
    {
        this.danhSachDoiTac = danhSachDoiTac ;
    }

    public ArrayList<Doitac> getDanhSachDoiTac() {
        return danhSachDoiTac;
    }

    public void setDanhSachDoiTac(ArrayList<Doitac> danhSachDoiTac) {
        this.danhSachDoiTac = danhSachDoiTac;
    }

    public void themDoiTac(Scanner sc) {
        themNhaCungCap(sc);
    }

    @Override
    public void them(Scanner sc) {
        themDoiTac(sc);
    }

    public void themNhaCungCap(Scanner sc)
    {
        Nhacungcap nhaCungCap = new Nhacungcap();
        nhaCungCap.setDoiTac(sc);
        kiemTraTrung(sc, nhaCungCap);
        danhSachDoiTac.add(nhaCungCap);
        System.out.println("Thêm nhà cung cấp thành công!");
    }
    public void taoDS(Scanner sc)
    {
        System.out.println("Tạo danh sách đối tác!");
        do {
            themDoiTac(sc);}
        while (Validation.luaChon(sc, "Bạn có muốn tiếp tục thêm đối tác(1/0): ", 0, 1) == 1);
        System.out.println("Tạo dữ liệu đối tác thành công!");
    }

    @Override
    public void taoDanhSach(Scanner sc) {
        taoDS(sc);
    }

    public int traCuuThongTinDoiTac(String maDoiTac)
    {
        for (int i = 0; i < danhSachDoiTac.size(); i++)
            if (danhSachDoiTac.get(i).getMaDoiTac().equalsIgnoreCase(maDoiTac)) return i;
        return -1;
    }

    @Override
    public int timViTri(String ma) {
        return traCuuThongTinDoiTac(ma);
    }

    @Override
    public Doitac timMa(String ma) {
        int index = timViTri(ma);
        return index == -1 ? null : danhSachDoiTac.get(index);
    }

    @Override
    public Doitac timTen(String ten) {
        if (ten == null || ten.isBlank()) {
            return null;
        }
        String khoa = ten.trim().toLowerCase(Locale.ROOT);
        for (Doitac doiTac : danhSachDoiTac) {
            if (containsIgnoreCase(doiTac.getTenDoiTac(), khoa)) {
                return doiTac;
            }
        }
        return null;
    }

    @Override
    public Doitac timKhoa(String khoa) {
        if (khoa == null || khoa.isBlank()) {
            return null;
        }
        String tuKhoa = khoa.trim().toLowerCase(Locale.ROOT);
        for (Doitac doiTac : danhSachDoiTac) {
            if (containsIgnoreCase(doiTac.getMaDoiTac(), tuKhoa)
                    || containsIgnoreCase(doiTac.getTenDoiTac(), tuKhoa)
                    || containsIgnoreCase(doiTac.getSoDienThoai(), tuKhoa)
                    || containsIgnoreCase(doiTac.getEmail(), tuKhoa)
                    || containsIgnoreCase(doiTac.getMaSoThue(), tuKhoa)) {
                return doiTac;
            }
        }
        return null;
    }

    private static boolean containsIgnoreCase(String value, String lowerCaseQuery) {
        return value != null && value.toLowerCase(Locale.ROOT).contains(lowerCaseQuery);
    }

    public void kiemTraTrung(Scanner sc, Doitac doiTac)
    {
        while (traCuuThongTinDoiTac(doiTac.getMaDoiTac()) != -1)
        {
            System.out.println("Mã đối tác đã tồn tại vui lòng nhập mã mới!");
            doiTac.setMaDoiTac(Validation.inputStringRegex(sc, "Nhập mã đối tác: ",
                    "DT\\d{3}", "Mã đối tác không đúng định dạng (VD: DT001)!"));
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
        danhSachDoiTac.remove(index);
        System.out.println("Xoá đối tác thành công!");
    }

    @Override
    public boolean xoa(String ma) {
        int index = timViTri(ma);
        if (index == -1) {
            System.out.println("Mã đối tác không tồn tại!");
            return false;
        }
        danhSachDoiTac.remove(index);
        System.out.println("Xoá đối tác thành công!");
        return true;
    }
    public void suaThongTinDoiTac(String maDoiTac, Scanner sc)
    {
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
    }

    @Override
    public boolean sua(String ma, Scanner sc) {
        if (timViTri(ma) == -1) {
            System.out.println("Mã đối tác không tồn tại!");
            return false;
        }
        suaThongTinDoiTac(ma, sc);
        return true;
    }

    public void xuat()
    {
        System.out.println("Danh sách đối tác!");
        for (Doitac doiTac : danhSachDoiTac)
            System.out.println(doiTac);
    }

    @Override
    public void xuatDanhSach() {
        xuat();
    }

}
