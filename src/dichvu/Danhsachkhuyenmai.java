package dichvu ;

import utils.Validation;

import java.time.LocalDate;
import java.util.Locale;
import java.util.Scanner;

import interfaces.IQuanLy;

public class Danhsachkhuyenmai implements IQuanLy<Khuyenmai> {
    private Khuyenmai[] khuyenmais;
    private int soLuongKhuyenMai;
    public Danhsachkhuyenmai()
    {
        soLuongKhuyenMai = 0;
        khuyenmais = new Khuyenmai[soLuongKhuyenMai];
    }

    public void setKhuyenmais(Khuyenmai[] khuyenmais) {
        this.khuyenmais = khuyenmais;
        this.soLuongKhuyenMai = khuyenmais.length;
    }

    public Khuyenmai[] getKhuyenmais() {
        return khuyenmais;
    }
    public int getSoLuongKhuyenMai() {
        return soLuongKhuyenMai;
    }

    public void themKhuyenMai(Khuyenmai khuyenMai) {
        int newSoLuongKhuyenMai = soLuongKhuyenMai + 1;
        Khuyenmai[] tam = new Khuyenmai[newSoLuongKhuyenMai];
        System.arraycopy(khuyenmais, 0, tam, 0, soLuongKhuyenMai);
        tam[soLuongKhuyenMai] = khuyenMai;
        khuyenmais = tam;
        soLuongKhuyenMai = newSoLuongKhuyenMai;
    }

    public void themKhuyenMai(Scanner sc )
    {
        Khuyenmai new_km ;
        System.out.println("Chọn loại khuyến mãi cần thêm: ");
        System.out.println("1. Khuyến mãi theo phần trăm!");
        System.out.println("2. Khuyến mãi trực tiếp!");
        System.out.println("3. Khuyến mãi mua tặng!");
        int Luachon = Validation.inputInt(sc , "Nhập lựa chọn: ") ;
        switch (Luachon)
        {
            case 1:
                new_km = new KhuyenMaiPhanTram() ;
                new_km.setKM(sc);
                themKhuyenMai(new_km);
                break;
            case 2:
                new_km = new KhuyenMaiTrucTiep() ;
                new_km.setKM(sc);
                themKhuyenMai(new_km);
                break;
            case 3:
                new_km = new KhuyenMaiMuaTang() ;
                new_km.setKM(sc);
                themKhuyenMai(new_km);
                break;
            default:
                System.out.println("Lựa chọn không hợp lệ!");
                return;
        }
    }

    @Override
    public void them(Scanner sc) {
        themKhuyenMai(sc);
    }

    public void taoDS(Scanner sc )
    {
        System.out.println("Tạo các khuyến mãi!");
        while (true)
        {
            themKhuyenMai(sc);
            System.out.println("Bạn có muốn tiếp tục thêm khuyến mãi(1/0): ");
            int luaChon = sc.nextInt() ;
            sc.nextLine();
            if ( luaChon == 0 ) break;
        }
        System.out.println("Tạo dữ liệu sản phẩm thành công!");
    }

    @Override
    public void taoDanhSach(Scanner sc) {
        taoDS(sc);
    }

    public int traCuuThongTinKhuyenMai( String maKhuyenMai )
    {
        for ( int i = 0 ; i < soLuongKhuyenMai ; i++ )
        {
            if ( khuyenmais[i].getMaKhuyenMai().equalsIgnoreCase(maKhuyenMai))
                return i ;
        }
        return -1 ;
    }

    @Override
    public int timViTri(String ma) {
        return traCuuThongTinKhuyenMai(ma);
    }

    @Override
    public Khuyenmai timMa(String ma) {
        int index = timViTri(ma);
        return index == -1 ? null : khuyenmais[index];
    }

    @Override
    public Khuyenmai timTen(String ten) {
        if (ten == null || ten.isBlank()) {
            return null;
        }
        String khoa = ten.trim().toLowerCase(Locale.ROOT);
        for (int i = 0; i < soLuongKhuyenMai; i++) {
            if (containsIgnoreCase(khuyenmais[i].getLoaiKhuyenMai(), khoa)) {
                return khuyenmais[i];
            }
        }
        return null;
    }

    @Override
    public Khuyenmai timKhoa(String khoa) {
        if (khoa == null || khoa.isBlank()) {
            return null;
        }
        String tuKhoa = khoa.trim().toLowerCase(Locale.ROOT);
        for (int i = 0; i < soLuongKhuyenMai; i++) {
            Khuyenmai khuyenMai = khuyenmais[i];
            if (containsIgnoreCase(khuyenMai.getMaKhuyenMai(), tuKhoa)
                    || containsIgnoreCase(khuyenMai.getLoaiKhuyenMai(), tuKhoa)
                    || String.valueOf(khuyenMai.getNgayBatDau()).contains(tuKhoa)
                    || String.valueOf(khuyenMai.getNgayKetThuc()).contains(tuKhoa)) {
                return khuyenMai;
            }
        }
        return null;
    }

    private static boolean containsIgnoreCase(String value, String lowerCaseQuery) {
        return value != null && value.toLowerCase(Locale.ROOT).contains(lowerCaseQuery);
    }
    public void xoaKhuyenMai(String maKhuyenmai )
    {
        int index = traCuuThongTinKhuyenMai(maKhuyenmai);
        if (index == -1)
        {
            System.out.println("Mã khuyến mãi không tồn tại!");
            return;
        }
        int newSoLuongKhuyenMai = soLuongKhuyenMai - 1;
        Khuyenmai[] tam = new Khuyenmai[newSoLuongKhuyenMai];
        System.arraycopy(khuyenmais, 0, tam, 0, index);
        System.arraycopy(khuyenmais, index + 1, tam, index,
                soLuongKhuyenMai - index - 1);
        khuyenmais = tam;
        soLuongKhuyenMai = newSoLuongKhuyenMai;
        System.out.println("Xoá khuyến mãi thành công!");
    }

    @Override
    public boolean xoa(String ma) {
        if (timViTri(ma) == -1) {
            System.out.println("Mã khuyến mãi không tồn tại!");
            return false;
        }
        xoaKhuyenMai(ma);
        return true;
    }
    public void suaThongTinKhuyenMai( String maKhuyenMai , Scanner sc )
    {
        int index = traCuuThongTinKhuyenMai(maKhuyenMai) ;
        if ( index == -1 )
        {
            System.out.println("Mã khuyến mãi không tồn tại!");
            return;
        }
        Khuyenmai km = khuyenmais[index];
        int luaChon ;
        System.out.println("Chọn các giá trị cần sửa thông tin!");
        System.out.println("1. Ngày bắt đầu!");
        System.out.println("2. Ngày kết thúc!");
        System.out.println("3. Mức chi tối thiểu!");
        if ( km instanceof  KhuyenMaiPhanTram)
        {
            System.out.println("4. Phần trăm giảm giá!");
            System.out.println("5. Mức giảm giá tối đa!");
            luaChon = Validation.inputInt( sc , "Nhập lựa chọn: ") ;
            switch (luaChon)
            {
                case 1:
                    km.setNgayBatDau(Validation.inputLocalDateRegex(sc , "Nhập ngày bắt đầu giảm giá mới: "));
                    break;
                case 2:
                    km.setNgayKetThuc(Validation.inputLocalDateRegex(sc , "Nhập ngày kết thúc mới: "));
                    break;
                case 3:
                    km.setMucChiToiThieu(Validation.inputLong(sc , "Nhập mức chi tiêu tối thiểu mới: "));
                    break;
                case 4:
                    double phanTramMoi = Validation.inputDouble(sc,
                            "Nhập phần trăm khuyến mãi mới: ");
                    while (phanTramMoi > 100) {
                        System.out.println("[!] Phần trăm giảm giá không được vượt quá 100%.");
                        phanTramMoi = Validation.inputDouble(sc,
                                "Nhập phần trăm khuyến mãi mới: ");
                    }
                    ((KhuyenMaiPhanTram) km).setPhanTramGiamGia(phanTramMoi / 100.0);
                    break;
                case 5:
                    ((KhuyenMaiPhanTram) km).setMucGiamToiDa(Validation.inputLong(sc , "Nhập mức giảm tối đa mới: "));
                    break;
                default:
                    System.out.println("Lựa chọn không hợp lệ!");
                    return;
            }
        } else if ( km instanceof  KhuyenMaiTrucTiep) {
            System.out.println("4. Số tiền giảm!");
            luaChon = Validation.inputInt( sc , "Nhập lựa chọn: ") ;
            switch (luaChon )
            {
                case 1:
                    km.setNgayBatDau(Validation.inputLocalDateRegex(sc , "Nhập ngày bắt đầu giảm giá mới: "));
                    break;
                case 2:
                    km.setNgayKetThuc(Validation.inputLocalDateRegex(sc , "Nhập ngày kết thúc mới: "));
                    break;
                case 3:
                    km.setMucChiToiThieu(Validation.inputLong(sc , "Nhập mức chi tiêu tối thiểu mới: "));
                    break;
                case 4:
                    ((KhuyenMaiTrucTiep) km).setSoTienGiam(Validation.inputLong(sc , "Nhập số tiền giảm giá mới: "));
                    break;
                default:
                    System.out.println("Lựa chọn không hợp lệ!");
                    return;
            }
        } else if ( km instanceof  KhuyenMaiMuaTang) {
            System.out.println("4. Số lượng mua tối thiểu!");
            System.out.println("5. Số lượng tặng!");
            luaChon = Validation.inputInt( sc , "Nhập lựa chọn: ") ;
            switch (luaChon )
            {
                case 1:
                    while (true)
                    {
                        try{
                            km.setNgayBatDau(Validation.inputLocalDateRegex(sc , "Nhập ngày bắt đầu giảm giá mới: "));
                            break;
                        }
                        catch (IllegalArgumentException e )
                        {
                            System.out.println(e.getMessage());
                        }
                    }

                    break;
                case 2:
                    while (true)
                    {
                        try{
                            km.setNgayKetThuc(Validation.inputLocalDateRegex(sc , "Nhập ngày kết thúc giảm giá mới: "));
                            break;
                        }
                        catch (IllegalArgumentException e )
                        {
                            System.out.println(e.getMessage());
                        }
                    }

                    break;
                case 3:
                    km.setMucChiToiThieu(Validation.inputLong(sc , "Nhập mức chi tiêu tối thiểu mới: "));
                    break;
                case 4:
                    ((KhuyenMaiMuaTang) km).setSoLuongMuaToiThieu(Validation.inputInt(sc , "Nhập số lượng mua tối thiểu mới: "));
                    break;
                case 5:
                    ((KhuyenMaiMuaTang) km).setSoLuongTang(Validation.inputInt( sc , "Nhập số lượng tặng mới: "));
                    break;
                default:
                    System.out.println("Lựa chọn không hợp lệ!");
                    return;
            }
        }
    }

    @Override
    public boolean sua(String ma, Scanner sc) {
        if (timViTri(ma) == -1) {
            System.out.println("Mã khuyến mãi không tồn tại!");
            return false;
        }
        suaThongTinKhuyenMai(ma, sc);
        return true;
    }

    public void xuat()
    {
        System.out.println("Danh sách khuyến mãi!");
        for ( Khuyenmai km: khuyenmais )
            System.out.println(km);
    }

    @Override
    public void xuatDanhSach() {
        xuat();
    }

    public void inDanhSachKhuyenMaiHetHan( Scanner sc)
    {
        System.out.println("Danh sách khuyến mãi hết hạn!");
        for ( Khuyenmai km: khuyenmais )
            if ( km.kiemTraHetHanKhuyenMai() ) System.out.println(km);
        if ( Validation.inputCoKhong(sc , "Bạn có muốn xoá các khuyến mãi không(Có/Không): "))
        {
            System.out.println("Nhập yêu cầu bạn muốn!");
            System.out.println("1. Xoá hết khuyến mãi hết hạn!");
            System.out.println("2. Xoá từng khuyến mãi hết hạn!");
            int Luachon = Validation.inputInt(sc ,"Nhập lựa chọn: " ) ;
            switch (Luachon)
            {
                case 1:
                    for (int i = soLuongKhuyenMai - 1; i >= 0; i--) {
                        if (khuyenmais[i].kiemTraHetHanKhuyenMai()) {
                            xoaKhuyenMai(khuyenmais[i].getMaKhuyenMai());
                        }
                    }
                    System.out.println("Xoá tất cả khuyến mãi hết hạn thành công!");
                    break;
                case 2:
                    while (true)
                    {

                        String maKhuyenMai = Validation.inputStringRegex( sc , "Nhập mã khuyến mãi bạn muốn xoá: ", "KM\\d{3}" , "Mã khuyến mãi không đúng định dạng vui lòng nhập lại(ví dụ hợp lệ: KM001): ", Khuyenmai.getSoThuTu());
                        int index = traCuuThongTinKhuyenMai(maKhuyenMai) ;
                        if ( index == -1 )
                        {
                            System.out.println("Mã khuyến mãi không tồn tại!");
                            continue;
                        }
                        else if ( !khuyenmais[index].kiemTraHetHanKhuyenMai() )
                        {
                            if ( Validation.inputCoKhong(sc , "Khuyến mãi vẫn còn thời hạn bạn vẫn muốn xoá?(Có/Không): " ))
                                xoaKhuyenMai(maKhuyenMai);
                        }
                        else xoaKhuyenMai(maKhuyenMai);
                        System.out.println("Xoá khuyến mãi thành công!");
                        if ( !Validation.inputCoKhong(sc ,"Bạn có muốn tiếp tục(Có/Không): " )) break;
                    }
                    break;
                default:
                    System.out.println("Lựa chọn không hợp lệ!");
                    break;
            }
        }
    }
    public void themKSanPhamKM( Scanner sc , int kSanPham )
    {
        System.out.println("Nhập sản phẩm khuyến mãi muốn thêm!");
        for ( int i = 0 ; i < kSanPham ; i++ )
        {
            themKhuyenMai(sc);
        }
        System.out.println("Thêm k sản phẩm khuyến mãi thành công!");
    }
    public void thongKeCacLoaiKhuyenMai()
    {
        System.out.println("Thống kê theo loại khuyến mãi!");
        System.out.println("Khuyến mãi theo phần trăm!");
        for ( Khuyenmai km: khuyenmais )
            if (km instanceof KhuyenMaiPhanTram)
                System.out.println(km);
        System.out.println("Khuyến mãi theo giảm giá trực tiếp sản phẩm!");
        for ( Khuyenmai km: khuyenmais )
            if (km instanceof KhuyenMaiTrucTiep)
                System.out.println(km);
        System.out.println("Khuyến mãi mua sản phẩm tặng sản phẩm!");
        for ( Khuyenmai km: khuyenmais )
            if (km instanceof KhuyenMaiMuaTang)
                System.out.println(km);
    }
    public void thongKeTrangThaiKhuyenMai()
    {
         System.out.println("Trạng thái của từng khuyến mãi!");
         System.out.println("Các khuyến mãi đang hoạt động!");
         for( Khuyenmai km : khuyenmais)
             if (km.dangHoatDong(LocalDate.now()))
                 System.out.println(km);
        System.out.println("Các khuyến mãi hết hạn!");
        for( Khuyenmai km : khuyenmais)
            if (km.kiemTraHetHanKhuyenMai())
                System.out.println(km);
        System.out.println("Các khuyến mãi chưa đến thời gian hoạt động!");
        for( Khuyenmai km : khuyenmais)
            if (km.chuaHoatDong())
                System.out.println(km);
    }
    public void timKiemNhieuKhoa ( String message )
    {
        if ( message == null || message.isBlank() )
        {
            System.out.println("Vui lòng nhập từ khoá!");
            return;
        }
        boolean flag = false ;
        System.out.println("Kết quả tìm kiếm trả về: ");
        for ( Khuyenmai km: khuyenmais)
        {
            if ( (km.getMaKhuyenMai().contains(message) || km.getLoaiKhuyenMai().contains(message)))
            {
                System.out.println(km);
                flag = true ;
            }
        }
        if (!flag)
            System.out.println("Không tìm thấy khuyến mãi phù hợp!");
    }
    public void timKiemTheoKhoa( LocalDate time )
    {
        if ( time == null )
        {
            System.out.println("Vui lòng nhập từ khoá!");
            return;
        }
        boolean flag = false ;
        for ( Khuyenmai km: khuyenmais )
        {
            if ( km.getNgayBatDau().equals(time) || km.getNgayKetThuc().equals(time ))
            {
                System.out.println(km);
                flag = true ;
            }
        }
        if (!flag ) System.out.println("Không tìm thấy khuyến mãi phù hợp!");
    }
}
