package interfaces;

public interface ITimKiem<T> {
    T timMa(String ma);
    T timTen(String ten);
    T timKhoa(String khoa);
    int timViTri(String ma);
}
