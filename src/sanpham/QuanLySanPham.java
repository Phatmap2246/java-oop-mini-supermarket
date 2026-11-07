package sanpham;
import utils.DataBase;

import java.util.ArrayList;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Scanner;

public class QuanLySanPham {
   public static ArrayList<SanPham> danhSachSanPham = new ArrayList<SanPham>();
   public static void inDanhSachSanPham(){
      for (SanPham sp : danhSachSanPham){
         String msg = String.format("stt: %d, ten san pham: %s, loai sanpham: %s",danhSachSanPham.indexOf(sp),sp.getTenSanPham(), sp.getClass().toString());
         System.out.println(msg);
      }
   }
   public ArrayList<SanPham> getDanhSachSanPham(){return danhSachSanPham;}
   public static int chonSanPham(Scanner sc){
       for(SanPham sp: danhSachSanPham){
           System.out.println("STT "+ danhSachSanPham.indexOf(sp) +sp.getMaSanPham() +"," + sp.getTenSanPham() + "," + sp.getTenSanPham());
       }
      System.out.println("chon STT muon chon");
      return sc.nextInt();
   }
   public void themSanPham(SanPham sp){
       danhSachSanPham.add(sp);
   }
   public void suaThongTinSanPham(Scanner sc){
       int id = chonSanPham(sc);
      SanPham sp = danhSachSanPham.get(id);
      ArrayList<Field> fields = DataBase.getALlField(sp.getClass());
      System.out.println("Cac gia tri co the sua");
      for(Field field : fields){
         field.setAccessible(true);
         System.out.println("STT:" +fields.indexOf(field) + " , " +field.getName());
      }
      //lay input stt tu user
      System.out.println("Chon STT de sua;");
      int stt = sc.nextInt();
      Field toBeEdit = fields.get(stt);
      //lay input gia tri moi tu user
       System.out.println("Nhap gia tri: ");
       String valueString = sc.nextLine();
       Object value = DataBase.getDataType(valueString, toBeEdit.getType());
       try {
           toBeEdit.set(sp,value);
       } catch (IllegalAccessException e) {
           System.err.println(e.getStackTrace());
       }
   }
   public void xoaSanPham(Scanner sc){

       int id = chonSanPham(sc);
       System.out.println("xoa san pham co STT" + id);
       danhSachSanPham.remove(id);
   }
   public void taoSanPham(Scanner sc){
       SanPham sp;
       System.out.println("""
               chon san pham muon them
               1: DienTu
               2: ThucPham
               2: TieuDung
               """);
       int sptype = sc.nextInt();
       switch(sptype){
           case 1:  themSanPham(DienTu.taoDienTu(sc));
           case 2:  themSanPham(ThucPham.taoThucPham(sc));
           case 3:  themSanPham(TieuDung.taoTieuDung(sc));
           default: System.out.println("khong co san pham nay");
       };
   }



}
