package dichvu ;

import utils.Validation;

import java.util.ArrayList;
import java.util.Scanner;

public class Danhsachkhuyenmai {
    private ArrayList<Khuyenmai> khuyenmais ;
    public Danhsachkhuyenmai()
    {
        khuyenmais = new ArrayList<>() ;
    }

    public void setKhuyenmais(ArrayList<Khuyenmai> khuyenmais) {
        this.khuyenmais = khuyenmais;
    }

    public ArrayList<Khuyenmai> getKhuyenmais() {
        return khuyenmais;
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
                khuyenmais.add(new_km) ;
                break;
            case 2:
                new_km = new KhuyenMaiTrucTiep() ;
                new_km.setKM(sc);
                khuyenmais.add(new_km) ;
                break;
            case 3:
                new_km = new KhuyenMaiMuaTang() ;
                new_km.setKM(sc);
                khuyenmais.add(new_km) ;
                break;
            default:
                System.out.println("Lựa chọn không hợp lệ!");
                return;
        }
    }
    public void taoDuLieuKhuyenMai(Scanner sc )
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
    public int traCuuThongTinKhuyenMai( String maKhuyenMai )
    {
        for ( int i = 0 ; i < khuyenmais.size() ; i++ )
        {
            if ( khuyenmais.get(i).getMaKhuyenMai().equalsIgnoreCase(maKhuyenMai))
                return i ;
        }
        return -1 ;
    }
    public void xoaKhuyenMai(String maKhuyenmai )
    {
        if ( traCuuThongTinKhuyenMai(maKhuyenmai) == -1 )
        {
            System.out.println("Mã khuyến mãi không tồn tại!");
            return;
        }
        khuyenmais.remove(traCuuThongTinKhuyenMai(maKhuyenmai)) ;
        System.out.println("Xoá khuyến mãi thành công!");
    }
    public void suaThongTinKhuyenMai( String maKhuyenMai , Scanner sc )
    {
        int index = traCuuThongTinKhuyenMai(maKhuyenMai) ;
        Khuyenmai km = khuyenmais.get(index) ;
        if ( index == -1 )
        {
            System.out.println("Mã khuyến mãi không tồn tại!");
            return;
        }
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
                    ((KhuyenMaiPhanTram) km).setPhanTramGiamGia(Validation.inputDouble(sc , "Nhập phần traăm khuyến mãi mới: "));
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
                    km.setNgayBatDau(Validation.inputLocalDateRegex(sc , "Nhập ngày bắt đầu giảm giá mới: "));
                    break;
                case 2:
                    km.setNgayKetThuc(Validation.inputLocalDateRegex(sc , "Nhập ngày kết thúc mới: "));
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
    public void timKiemKhuyenMai( String maKhuyenMai)
    {
        int index = traCuuThongTinKhuyenMai(maKhuyenMai) ;
        if ( index == -1 )
        {
            System.out.println("Mã khuyến mãi không tồn tại!");
            return;
        }
        System.out.println("Khuyến mãi cần tìm!");
        System.out.println(khuyenmais.get(index));
    }
    public void inDanhSachKhuyenMai()
    {
        System.out.println("Danh sách khuyến mãi!");
        for ( Khuyenmai km: khuyenmais )
            System.out.println(km);
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
                    for ( Khuyenmai km: khuyenmais )
                        if ( km.kiemTraHetHanKhuyenMai() ) xoaKhuyenMai(km.getMaKhuyenMai());
                    System.out.println("Xoá tất cả khuyến mãi hết hạn thành công!");
                    break;
                case 2:
                    while (true)
                    {

                        String maKhuyenMai = Validation.inputStringRegex( sc , "Nhập mã khuyến mãi bạn muốn xoá: ", "KM\\d{3}" , "Mã khuyến mãi không đúng định dạng vui lòng nhập lại(ví dụ hợp lệ: KM001): ");
                        int index = traCuuThongTinKhuyenMai(maKhuyenMai) ;
                        if ( index == -1 )
                        {
                            System.out.println("Mã khuyến mãi không tồn tại!");
                            continue;
                        }
                        else if ( !khuyenmais.get(index).kiemTraHetHanKhuyenMai() )
                        {
                            if ( Validation.inputCoKhong(sc , "Khuyến mãi vẫn còn thời hạn bạn vẫn muốn xoá?(Có/Không): " ))
                                xoaKhuyenMai(maKhuyenMai);
                        }
                        else xoaKhuyenMai(maKhuyenMai);
                        System.out.println("Xoá khuyến mãi thành công!");
                        if ( !Validation.inputCoKhong(sc ,"Bạn có muốn tiếp tục(Có/Không): " )) break;
                    }
                default:
                    System.out.println("Lựa chọn không hợp lệ!");
                    return;
            }
        }
    }
}
