import org.junit.jupiter.api.Test;
import sanpham.SanPham;
import utils.*;
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
    void ghivaojson() throws Exception {
        ArrayList<String> jsol = new ArrayList<String>();
        ResultSet res = database.query("select * from 'test/products.csv'");
        while (res.next()) {
            listSanPham.add(database.napSanPham(res));
        }
        for (SanPham sp : listSanPham) {
            String jso = json.mapper.writeValueAsString(sp);
            jsol.add(jso);
        }
        json.themVaoTemp(jsol);
    }
    @Test
    void doctufilevaobang()throws Exception{
        database.schemas();
        database.execute("insert into sanpham select * from 'test/products.csv'");
        database.execute("insert into sanpham by name select * from read_json('src/utils/temp.json',ignore_errors = true)");
        ResultSet res = database.query("select * from sanpham");
       while (res.next()){
       listSanPham.add(database.napSanPham(res));
       }
       for (SanPham sp : listSanPham){
           System.out.println(sp.getMaSanPham());
       }
    }
}
