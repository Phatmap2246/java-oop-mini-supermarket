package sanpham;
import utils.DataBase;

import java.util.ArrayList;
import java.lang.reflect.Field;
import java.util.List;

public class QuanLySanPham {
   public static ArrayList<SanPham> danhSachSanPham = new ArrayList<SanPham>();
   public static void inDanhSachSanPham(){
      for (SanPham sp : danhSachSanPham){
         String msg = String.format("stt: %d, ten san pham: %s, loai sanpham: %s",danhSachSanPham.indexOf(sp),sp.getTenSanPham(), sp.getClass().toString());
         System.out.println(msg);
      }
   }
   public static void suaThongTinSanPham(int id){
      SanPham sp = danhSachSanPham.get(id);
      ArrayList<Field> fields = DataBase.getALlField(sp.getClass());
      System.out.println("Cac gia tri co the sua");
      for(Field field : fields){
         field.setAccessible(true);
         System.out.println("STT:" +fields.indexOf(field) + " , " +field.getName());
      }
      //lay input stt tu user
      int stt =1;

      Field toBeEdit = fields.get(stt);
      //lay input gia tri moi tu user
       try {
           toBeEdit.set(sp,null);
       } catch (IllegalAccessException e) {
           System.err.println(e.getStackTrace());
       }
   }

   // (ĐỨC PHÁT) Thêm getDanhSach 
   public ArrayList<SanPham> getDanhSachSanPham(){
      return danhSachSanPham;
   }
}
