package interfaces;

public interface ITimKiem<T> {
    // Trả về đối tượng đầu tiên phù hợp; null khi không tìm thấy.
    T timMa(String ma);
    T timTen(String ten);
    T timKhoa(String khoa);
    // Trả về -1 nếu không tìm thấy mã trong danh sách đang hoạt động.
    int timViTri(String ma);
}
