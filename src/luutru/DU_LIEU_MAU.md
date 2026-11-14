# Dữ liệu mẫu để thử chương trình

Bộ mẫu đã được nạp vào `hien-tai/`, dùng trực tiếp khi chạy `Main`. Đây là dữ liệu
giả lập để thử nghiệp vụ, không tạo giao dịch ngân hàng thật. Ngày tạo: 14/11/2026.

## Đăng nhập

| Chọn ở trang chủ | Tài khoản | Mật khẩu |
|---|---|---|
| 1. Thu ngân | `ThuNganSieuThi` | `thungan@123` |
| 2. Quản kho | `QuanKhoSieuThi` | `quankho@123` |
| 3. Quản lý | `QuanLySieuThi` | `quanly@123` |
| 4. Kế toán | `KeToanSieuThi` | `ketoan@123` |

## Sản phẩm và khuyến mãi

| Mã | Sản phẩm | Giá bán | Tồn hiện tại | Trường hợp thử |
|---|---|---:|---:|---|
| SP001 | Sữa tươi hộp 1 lít | 32.000 | 40 | KM001 giảm 10%, tối đa 50.000 |
| SP002 | Sữa chua dâu | 8.000 | 15 | Cận hạn: 19/11/2026 |
| SP003 | Bánh mì sandwich | 18.000 | 6 | Đã hết hạn: 12/11/2026, không được bán |
| SP004 | Nước giặt 3 lít | 150.000 | 25 | KM002 mua 2 tặng 1 |
| SP005 | Dầu gội 650 ml | 95.000 | 12 | KM004 đã kết thúc, không áp dụng |
| SP006 | Bàn chải đánh răng | 18.000 | 0 | Hết hàng, không được bán |
| SP007 | Nồi cơm điện 1.8 lít | 650.000 | 5 | KM003 giảm 50.000 |
| SP008 | Ấm đun siêu tốc | 350.000 | 3 | Điện tử không khuyến mãi |

Ba khuyến mãi KM001–KM003 có hiệu lực đến 13/01/2027. KM004 hết hiệu lực 13/11/2026.
Tồn kho là số lượng hiện tại của bộ mẫu; các hóa đơn là một phần lịch sử minh họa.

## Các dữ liệu khác

- Nhà cung cấp: NCC001 thực phẩm, NCC002 tiêu dùng, NCC003 điện tử.
- Nhân viên: bốn người NV001–NV004, tương ứng quản lý, quản kho, thu ngân, kế toán.
- Khách hàng: KH001 Nguyễn Thảo Linh (Đồng), KH002 Trần Hoàng Nam (Bạc), KH003 Lê Hà My (Vàng).
- Hóa đơn bán: HD001 đã trả bằng tiền mặt, HD002 đã trả bằng chuyển khoản, HD003 chưa trả.
- Hóa đơn nhập: HD004 và HD005 đã duyệt/nhập kho; HD006 chưa duyệt, chưa nhập kho.
- Công nợ HD004/PNK001 quá hạn và chưa trả; HD005/PNK002 đã trả.
- File `YeuCau.txt` có sẵn được giữ nguyên, không bị thay bởi bộ mẫu.

## Thử nhanh

1. Đăng nhập thu ngân, chọn 3 để xem hóa đơn, chọn 8 để thấy HD003 chưa thanh toán.
2. Chọn 1 để lập hóa đơn mới. Dùng KH001, thử SP001, SP004 hoặc SP007 để xem khuyến mãi.
3. Thử SP003 hoặc SP006 để kiểm tra chặn sản phẩm hết hạn/hết hàng.
4. Đăng nhập kế toán, chọn 4 để xem hai khoản công nợ, chọn 3 nếu muốn thử thanh toán khoản HD004.
5. Chọn 1 của kế toán, nhập khoảng 01/10/2026–30/11/2026 để xem số liệu tài chính mẫu.
6. Chọn 0 trở về rồi 0 thoát. Mở lại `Main` để kiểm tra thay đổi đã được lưu.

## Tạo lại trong thư mục riêng

`test/TaoDuLieuMau.java` tạo bộ mẫu theo ngày chạy. Công cụ chỉ nhận bộ dữ liệu rỗng
để không ghi đè dữ liệu bạn đã nhập. Sau khi biên dịch cùng `src`, có thể chạy:

```text
java -cp "<output>;lib/*" TaoDuLieuMau <thu-muc-mau-moi>
java -cp "<output>;lib/*" TaoDuLieuMau <thu-muc-mau-moi> check
```
