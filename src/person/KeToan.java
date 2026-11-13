package person;

import dichvu.Danhsachdoitac;

import hoadon.DanhSachCongNo;

import hoadon.DanhSachHoaDonNhap;

import hoadon.DanhSachHoaDonBan;

import java.util.ArrayList;
import java.util.Scanner;

import hoadon.HoaDon;
import utils.Validation;

public class KeToan extends NhanVien {
    private DanhSachHoaDonBan danhSachHoaDonBan = new DanhSachHoaDonBan();
    private DanhSachHoaDonNhap danhSachHoaDonNhap = new DanhSachHoaDonNhap();
    private Danhsachdoitac danhSachDoiTac = new Danhsachdoitac();
    private DanhSachCongNo danhSachCongNo = new DanhSachCongNo();

    public long tinhTongDoanhThu(ArrayList<HoaDon> dshd,long songay)
    {
        return danhSachHoaDonBan.tinhTongDoanhThu(dshd, songay);
    }

    public long tinhTongChiPhi(ArrayList<HoaDon> dshd,long songay)
    {
        return danhSachHoaDonNhap.tinhTongChiPhi(dshd, songay);
    }

    public long tinhLoiNhuan(ArrayList<HoaDon> dshd,long songay) {
        return tinhTongDoanhThu(dshd,songay) - tinhTongChiPhi(dshd,songay);
    }

    public long tinhLoiNhuanRong(ArrayList<HoaDon> dshd,long songay) {
        long loiNhuanTruocThue = tinhLoiNhuan(dshd,songay);
        if (loiNhuanTruocThue <= 0) {
            return loiNhuanTruocThue;
        }

        long thueTNDN = (long) (loiNhuanTruocThue * 0.2);
        return loiNhuanTruocThue - thueTNDN;
    }

    public void thanhToanCongNo(Scanner sc, DanhSachHoaDonNhap danhSachHoaDonNhap, Danhsachdoitac danhSachDoiTac) {
        while (true) {
            System.out.println("\n=== KẾ TOÁN: CÔNG NỢ NHÀ CUNG CẤP ===");
            System.out.println("1. Đối chiếu phiếu nhập kho & Ghi nhận nợ");
            System.out.println("2. Xem danh sách & Thanh toán công nợ");
            System.out.println("0. Thoát");

            int luaChon = Validation.luaChon(sc, "Chọn chức năng: ", 0, 2);
            if (luaChon == 0) {
                return;
            }
            if (luaChon == 1) {
                danhSachCongNo.doiChieuVaGhiNhanCongNo(sc, danhSachHoaDonNhap, danhSachDoiTac);
            } else {
                danhSachCongNo.hienThiVaThanhToanCongNo(sc);
            }
        }
    }
    public void thongKe(int songay,ArrayList<HoaDon> dshd){
        System.out.println("BANG THONG KE: ");
        System.out.println("1. Doanh thu tu " + songay +"truoc: "+ tinhTongDoanhThu(dshd, songay));
        System.out.println("2. Loi nhuan tu " + songay +"truoc: "+ tinhLoiNhuanRong(dshd, songay));
        danhSachCongNo.thongKeCongNo();
    }

    @Override
    public String getVaiTro() {
        return "KeToan";
    }

    public void setDanhSachHoaDonBan(DanhSachHoaDonBan danhSachHoaDonBan) {
        if (danhSachHoaDonBan == null) throw new IllegalArgumentException("Danh sách hóa đơn bán không được null.");
        this.danhSachHoaDonBan = danhSachHoaDonBan;
    }

    public void setDanhSachHoaDonNhap(DanhSachHoaDonNhap danhSachHoaDonNhap) {
        if (danhSachHoaDonNhap == null) throw new IllegalArgumentException("Danh sách hóa đơn nhập không được null.");
        this.danhSachHoaDonNhap = danhSachHoaDonNhap;
    }

    public void setDanhSachDoiTac(Danhsachdoitac danhSachDoiTac) {
        if (danhSachDoiTac == null) throw new IllegalArgumentException("Danh sách đối tác không được null.");
        this.danhSachDoiTac = danhSachDoiTac;
    }

    public void setDanhSachCongNo(DanhSachCongNo danhSachCongNo) {
        if (danhSachCongNo == null) throw new IllegalArgumentException("Danh sách công nợ không được null.");
        this.danhSachCongNo = danhSachCongNo;
    }

    public long tinhTongDoanhThu(long songay) {
        return danhSachHoaDonBan.tinhTongDoanhThu(songay);
    }

    public long tinhTongChiPhi(long songay) {
        return danhSachHoaDonNhap.tinhTongChiPhi(songay);
    }

    public long tinhLoiNhuan(long songay) {
        return tinhTongDoanhThu(songay) - tinhTongChiPhi(songay);
    }

    public long tinhLoiNhuanRong(long songay) {
        long loiNhuanTruocThue = tinhLoiNhuan(songay);
        if (loiNhuanTruocThue <= 0) return loiNhuanTruocThue;
        long thueTNDN = (long) (loiNhuanTruocThue * 0.2);
        return loiNhuanTruocThue - thueTNDN;
    }

    public void thongKe(int songay) {
        System.out.println("BANG THONG KE: ");
        System.out.println("1. Doanh thu tu " + songay + " truoc: " + tinhTongDoanhThu(songay));
        System.out.println("2. Loi nhuan tu " + songay + " truoc: " + tinhLoiNhuanRong(songay));
        danhSachCongNo.thongKeCongNo();
    }

    public void thucHienNghiepVu(Scanner sc)
    {
        int luaChon ;
        do {
            System.out.println("===================NGHIỆP VỤ KẾ TOÁN===================");
            System.out.println("1. Xem báo cáo hóa đơn bán!");
            System.out.println("2. Xem báo cáo hóa đơn nhập!");
            System.out.println("3. Thống kê doanh thu và lợi nhuận!");
            System.out.println("4. Đối chiếu và thanh toán công nợ!");
            System.out.println("5. Xem danh sách công nợ!");
            System.out.println("0. Trở về.");
            luaChon = Validation.luaChon(sc, "Nhập lựa chọn: ", 0, 5);
            switch (luaChon)
            {
                case 1: {
                    danhSachHoaDonBan.xemBaoCao(sc);
                    break;
                }
                case 2: {
                    danhSachHoaDonNhap.xemBaoCao(sc);
                    break;
                }
                case 3: {
                    int soNgay = Validation.inputInt(sc, "Nhập số ngày cần thống kê: ");
                    thongKe(soNgay);
                    break;
                }
                case 4: {
                    thanhToanCongNo(sc, danhSachHoaDonNhap, danhSachDoiTac);
                    break;
                }
                case 5: {
                    danhSachCongNo.thongKeCongNo();
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
