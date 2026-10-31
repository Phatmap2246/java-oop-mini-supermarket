package dichvu;

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
        this.danhSachDoiTac = danhSachDoiTac ;
    }

    public ArrayList<Doitac> getDanhSachDoiTac() {
        return danhSachDoiTac;
    }

    public void setDanhSachDoiTac(ArrayList<Doitac> danhSachDoiTac) {
        this.danhSachDoiTac = danhSachDoiTac;
    }
    public void themDoiTac(Scanner sc)
    {
        System.out.println("Chọn loại đối tác cần thêm:");
        System.out.println("1. Nhà cung cấp!");
        System.out.println("2. Đơn vị vận chuyển!");
        int luaChon = Validation.luaChon(sc, "Nhập lựa chọn: ", 1, 2);
        if (luaChon == 1) themNhaCungCap(sc);
        else themDonViVanChuyen(sc);
    }
    public void themNhaCungCap(Scanner sc)
    {
        Nhacungcap nhaCungCap = new Nhacungcap();
        nhaCungCap.setDoiTac(sc);
        kiemTraTrung(sc, nhaCungCap);
        danhSachDoiTac.add(nhaCungCap);
        System.out.println("Thêm nhà cung cấp thành công!");
    }
    public void themDonViVanChuyen(Scanner sc)
    {
        Donvivanchuyen donViVanChuyen = new Donvivanchuyen();
        donViVanChuyen.setDoiTac(sc);
        kiemTraTrung(sc, donViVanChuyen);
        danhSachDoiTac.add(donViVanChuyen);
        System.out.println("Thêm đơn vị vận chuyển thành công!");
    }
    public void taoDuLieuDoiTac(Scanner sc)
    {
        System.out.println("Tạo danh sách đối tác!");
        do {
            themDoiTac(sc);
        } while (Nhapdulieu.luaChon(sc, "Bạn có muốn tiếp tục thêm đối tác(1/0): ", 0, 1) == 1);
        System.out.println("Tạo dữ liệu đối tác thành công!");
    }
    public int traCuuThongTinDoiTac(String maDoiTac)
    {
        for (int i = 0; i < danhSachDoiTac.size(); i++)
            if (danhSachDoiTac.get(i).getMaDoiTac().equalsIgnoreCase(maDoiTac)) return i;
        return -1;
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
    public void timKiemDoiTac(String maDoiTac)
    {
        int index = traCuuThongTinDoiTac(maDoiTac);
        if (index == -1)
        {
            System.out.println("Mã đối tác không tồn tại!");
            return;
        }
        System.out.println(danhSachDoiTac.get(index));
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
            System.out.println("8. Đơn vị vận chuyển mặc định!");
        }
        int luaChon = Validation.luaChon(sc, "Nhập lựa chọn: ", 1,
                doiTac instanceof Nhacungcap ? 8 : 5);
        switch (luaChon)
        {
            case 1:
                System.out.println("Nhập tên đối tác mới: ");
                doiTac.setTenDoiTac(sc.nextLine());
                break;
            case 2:
                doiTac.setSoDienThoai(Validation.inputStringRegex(sc, "Nhập số điện thoại mới: ",
                        Validation.phoneRegex, "Số điện thoại không đúng định dạng!"));
                break;
            case 3:
                System.out.println("Nhập địa chỉ mới: ");
                doiTac.setDiaChi(sc.nextLine());
                break;
            case 4:
                doiTac.setEmail(Validation.inputStringRegex(sc, "Nhập email mới: ",
                        Validation.emailRegex, "Email không đúng định dạng!"));
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
            case 8:
                String maVanChuyen = Validation.inputStringRegex(sc, "Nhập mã đơn vị vận chuyển: ",
                        "DT\\d{3}", "Mã đối tác không đúng định dạng (VD: DT001)!");
                int viTri = traCuuThongTinDoiTac(maVanChuyen);
                if (viTri == -1 || !(danhSachDoiTac.get(viTri) instanceof Donvivanchuyen))
                {
                    System.out.println("Mã đơn vị vận chuyển không tồn tại!");
                    return;
                }
                assert doiTac instanceof Nhacungcap;
                ((Nhacungcap) doiTac).setMaDonViVanChuyen(danhSachDoiTac.get(viTri).getMaDoiTac());
                break;
        }
        System.out.println("Sửa thông tin đối tác thành công!");
    }
    public void inDanhSachDoiTac()
    {
        System.out.println("Danh sách đối tác!");
        for (Doitac doiTac : danhSachDoiTac)
            System.out.println(doiTac);
    }
    public void inDanhSachNhaCungCap()
    {
        System.out.println("Danh sách nhà cung cấp!");
        for (Doitac doiTac : danhSachDoiTac)
            if (doiTac instanceof Nhacungcap) System.out.println(doiTac);
    }
    public void inDanhSachDonViVanChuyen()
    {
        System.out.println("Danh sách đơn vị vận chuyển!");
        for (Doitac doiTac : danhSachDoiTac)
            if (doiTac instanceof Donvivanchuyen) System.out.println(doiTac);
    }
}
