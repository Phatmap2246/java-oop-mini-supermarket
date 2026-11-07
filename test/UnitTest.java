import org.junit.Assert;
import org.junit.jupiter.api.Test;
import sanpham.DienTu;
import sanpham.SanPham;
import utils.*;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.ExecutionException;

public class UnitTest {
    void assertAllEqual(Object expected, Object actual) throws Exception {
        Class <?> clazz = expected.getClass();
        ArrayList<Field> fields = DataBase.getALlField(clazz);
        for(Field field : fields){
            field.setAccessible(true);
            Object expectedValue = field.get(expected);
            Object actualValue = field.get(actual);
            Assert.assertEquals(expectedValue,actualValue);
        }
    }
    @Test
    void CSVLINETEST(){
        DienTu sp = new DienTu("msp","dodientu","mcc",12,32,1,24);
        System.out.println(DataBase.getCSVHeader(sp));
        System.out.println(DataBase.getCSVLine(sp));
    }
    @Test
    void OBJECTFROMCSVLINETEST() throws Exception {
        DienTu sp = new DienTu("msp","dodientu","mcc",12,32,1,24);
        String headers = DataBase.getCSVHeader(sp);
        String csvline = DataBase.getCSVLine(sp);
        DienTu sp2 = DataBase.taoObject(DienTu.class,headers,csvline);
        assertAllEqual(sp,sp2);
    }
    @Test
    void WRITEOBJECTTOFILE(){
        DienTu sp = new DienTu("msp","dodientu","mcc",12,32,1,24);
        DataBase.ghiVaoFile(sp,"test/dientu.csv",true);
        DataBase.ghiVaoFile(sp,"test/dientu.csv",false);
        DataBase.ghiVaoFile(sp,"test/dientu.csv",false);
    }
    @Test
    void READFROMFILE() throws Exception {
        ArrayList<DienTu> sps = new ArrayList<>();
        DienTu sp = new DienTu("msp","dodientu","mcc",12,32,1,24);
        sps.add(sp);
        sps.add(sp);
        sps.add(sp);
        ArrayList<DienTu> sps2 = DataBase.docfile(DienTu.class,"test/dientu.csv");
        Iterator<?> itr1 = sps.iterator();
        Iterator<?> itr2 = sps2.iterator();
        while(itr1.hasNext() || itr2.hasNext()){
            assertAllEqual(itr1.next(),itr2.next());
        }
    }
}
