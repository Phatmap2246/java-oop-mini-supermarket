import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import sanpham.DienTu;
import sanpham.SanPham;
import utils.*;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;

public class UnitTest {
    static String userinput;
    static Scanner sc = new Scanner(System.in);
    void assertAllEqual(Object expected, Object actual) throws Exception {
        Class <?> clazz = expected.getClass();
        ArrayList<Field> fields = DataBase.getALlField(clazz);
        for(Field field : fields){
            field.setAccessible(true);
            Object expectedValue = field.get(expected);
            Object actualValue = field.get(actual);
            Assertions.assertEquals(expectedValue,actualValue);
        }
    }
    @Test
    void CSVLINETEST(){
        DienTu sp = new DienTu("msp","dodientu","mcc",12,32,1,"mcc",0.05,24,0,"",LocalDate.now());
        System.out.println(DataBase.getCSVHeader(sp));
        System.out.println(DataBase.getCSVLine(sp));
    }
    @Test
    void OBJECTFROMCSVLINETEST() throws Exception {
        DienTu sp = new DienTu("msp","dodientu","mcc",12,32,1,"mcc",0.05,24,0,"",LocalDate.now());
        String headers = DataBase.getCSVHeader(sp);
        String csvline = DataBase.getCSVLine(sp);
        DienTu sp2 = DataBase.taoObject(DienTu.class,headers,csvline);
        assertAllEqual(sp,sp2);
    }
    @Test
    void WRITEOBJECTTOFILE(){
        DienTu sp = new DienTu("msp","dodientu","mcc",12,32,1,"mcc",0.05,24,0,"",LocalDate.now());
        DataBase.ghiVaoFile(sp,"test/dientu.csv",true);
        DataBase.ghiVaoFile(sp,"test/dientu.csv",false);
        DataBase.ghiVaoFile(sp,"test/dientu.csv",false);
    }
    @Test
    void READFROMFILE() throws Exception {
        ArrayList<DienTu> sps = new ArrayList<>();
        DienTu sp = new DienTu("msp","dodientu","mcc",12,32,1,"mcc",0.05,24,0,"",LocalDate.now());
        sps.add(sp);
        sps.add(sp);
        sps.add(sp);
        java.nio.file.Path file = java.nio.file.Files.createTempFile("dientu", ".csv");
        DataBase.ghiVaoFile(new ArrayList<Object>(sps), file.toString(), true);
        ArrayList<DienTu> sps2 = DataBase.docfile(DienTu.class, file.toString());
        Iterator<?> itr1 = sps.iterator();
        Iterator<?> itr2 = sps2.iterator();
        while(itr1.hasNext() || itr2.hasNext()){
            assertAllEqual(itr1.next(),itr2.next());
        }
    }
    // Đã bỏ TESTUSERINPUT: nó kiểm tra DienTu.taoDienTu(Scanner), factory không tồn tại trong repo này.
}