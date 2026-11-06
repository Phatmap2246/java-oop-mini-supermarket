import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import sanpham.DienTu;
import utils.*;
public class UnitTest {
    @Test
    void CSVLINETEST(){
        DienTu sp = new DienTu("msp","dodientu","mcc",12,32,1,24);
        System.out.println(DataBase.getCSVLine(sp));
    }
}
