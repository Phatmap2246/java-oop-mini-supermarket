import dichvu.*;
import sanpham.*;
import java.time.LocalDate;
import java.nio.file.*;
import utils.DongDuLieu;

public class GiamGiaCanHanTest {
    static int checks;
    static void check(boolean ok) { if (!ok) throw new AssertionError("Case " + (checks+1)); checks++; }
    public static void main(String[] args) throws Exception {
        QuanLySanPham sp = new QuanLySanPham();
        Danhsachkhuyenmai km = new Danhsachkhuyenmai();
        DanhSachSPKM lk = new DanhSachSPKM();
        int[] days = {-1,0,1,3,4,7,8,15,16};
        double[] rates = {0,.5,.5,.5,.3,.3,.15,.15,0};
        for (int i=0;i<days.length;i++) {
            sp.getDanhSachSanPham().add(ThucPham.tuDongFile(DongDuLieu.ghep("THUCPHAM",
                    String.format("SP%03d",i+1),"Thử cận hạn","Thực phẩm","NCC001",5000,10000,10,.08,true,
                    LocalDate.now().minusDays(40),LocalDate.now().plusDays(days[i]),5,"Giữ mát")));
        }
        check(lk.tuDongGiamGiaCanHSD(sp,km)==7);
        for (int i=0;i<days.length;i++) {
            SanPhamKhuyenMai link=lk.timMa(sp.getDanhSachSanPham().get(i).getMaSanPham());
            if (rates[i]==0) check(link==null);
            else {
                KhuyenMaiPhanTram promo=(KhuyenMaiPhanTram)km.timMa(link.getMaKhuyenMai());
                check(promo.getPhanTramGiamGia()==rates[i]);
                check(promo.tinhTienGiam(10000,1)==(long)(10000*rates[i]));
            }
        }
        lk.tuDongGiamGiaCanHSD(sp,km);
        check(km.getSoLuongKhuyenMai()==7 && lk.getSoLuongSanPham()==7);
        ThucPham p=(ThucPham)sp.getDanhSachSanPham().get(7);
        p.setHanSuDung(LocalDate.now().plusDays(2));
        lk.tuDongGiamGiaCanHSD(sp,km);
        check(((KhuyenMaiPhanTram)km.timMa(lk.timMa(p.getMaSanPham()).getMaKhuyenMai())).getPhanTramGiamGia()==.5);
        KhuyenMaiTrucTiep manual=new KhuyenMaiTrucTiep();
        manual.setNgayBatDau(LocalDate.now()); manual.setNgayKetThuc(LocalDate.now().plusDays(10));
        manual.setSoTienGiam(1000); km.them(manual);
        lk.timMa(p.getMaSanPham()).setMaKhuyenMai(manual.getMaKhuyenMai());
        lk.tuDongGiamGiaCanHSD(sp,km);
        check(lk.timMa(p.getMaSanPham()).getMaKhuyenMai().equals(manual.getMaKhuyenMai()));
        p.setSoLuongTonKho(0);
        check(lk.tuDongGiamGiaCanHSD(sp,km)==6);
        Path dir=Files.createTempDirectory(Paths.get(args[0]),"can-han-");
        km.ghiFile(dir.resolve("KhuyenMai.txt").toString());
        lk.ghiFile(dir.resolve("SanPhamKhuyenMai.txt").toString());
        Danhsachkhuyenmai docKM=new Danhsachkhuyenmai(); docKM.docFile(dir.resolve("KhuyenMai.txt").toString());
        DanhSachSPKM docLK=new DanhSachSPKM(); docLK.docFile(dir.resolve("SanPhamKhuyenMai.txt").toString());
        check(docKM.getSoLuongKhuyenMai()==8 && docLK.getSoLuongSanPham()==7);
        System.out.println("PASS: "+checks+" near-expiry checks");
    }
}
