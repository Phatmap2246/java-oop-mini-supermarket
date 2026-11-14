package interfaces;

import java.util.Scanner;

public interface IQuanLy<T> extends ITimKiem<T> {
    void taoDanhSach(Scanner sc);
    void them(Scanner sc);
    boolean sua(String ma, Scanner sc);
    boolean xoa(String ma);
    void xuatDanhSach();
}
