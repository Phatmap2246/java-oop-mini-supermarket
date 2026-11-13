import java.util.Scanner;

public class Main {
    public static void main(String[] args)
    {
        Scanner sc = new Scanner(System.in) ;
        GiaoDien giaoDien = new GiaoDien() ;
        giaoDien.TrangChu(sc);
        sc.close();
    }
}
