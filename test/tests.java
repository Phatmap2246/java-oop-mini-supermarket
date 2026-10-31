import hoadon.*;
import org.junit.jupiter.api.Test;
import sanpham.SanPham;
import sanpham.ThucPham;
import utils.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.sql.*;

public class tests {
    ArrayList<SanPham> listSanPham = new ArrayList<SanPham>();

    @Test
    void loadcsv() throws Exception {
        System.out.println("load csv test:");
        ResultSet res = database.query("select * from 'test/products.csv'");
        while (res.next()) {
            System.out.println(res.getString("hanSuDung"));
        }
    }
    @Test
    void loadInvoicesDetails() throws Exception{
       System.out.println("load invoices and details");
       ResultSet res = database.query("select * from 'test/details.csv'");
       ResultSet res2 = database.query("select * from 'test/invoices.csv'");
       while(res.next()){
           System.out.println(res.getString("maHoaDon"));
       }
       while(res2.next()){
           System.out.println(res2.getString("maHoaDon"));
       }
    }
    @Test
    void napvaoinvoice() throws Exception{
        ArrayList<HoaDon> invoices = new ArrayList<HoaDon>();
        System.out.println("load from table into hoadon class");
        ResultSet res = database.query("select * from 'test/invoices.csv'");
        while(res.next()){
            invoices.add(database.napHoaDon(res));
        }
        for( HoaDon in: invoices){
            System.out.println(in.getClass());
        }
    }
    @Test
    void napvaoclass() throws Exception {
        System.out.println("turning into objects tests");
        ResultSet res = database.query("select * from 'test/products.csv'");
        while (res.next()) {
            listSanPham.add(database.napSanPham(res));
        }
        for (SanPham sp : listSanPham) {
            System.out.println(sp.getClass());
        }
    }


    @Test
    void tuobjectvaobang() throws Exception{
        database.schemas();
        database.execute("insert into sanpham select * from 'test/products.csv';");
        ThucPham tp = new ThucPham(12, LocalDate.now(),1212,"helochaocacban","SP1037",199,LocalDate.now(), "ThucPham");
        database.napVaoBangSanPham(tp);
        ResultSet res = database.query("select * from sanpham");
        while(res.next()){
            System.out.println(res.getString("maSanPham"));
        }
    }
}
