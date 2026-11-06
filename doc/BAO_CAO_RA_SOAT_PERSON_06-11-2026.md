# Báo cáo rà soát nhánh `feature/person-module`

**Ngày rà soát:** 06/11/2026
**Phạm vi:** Các lớp thuộc module `person`, tập trung vào quản lý nhân viên và kế toán.
**Trạng thái:** Chỉ rà soát và kiểm thử; không chỉnh sửa mã nguồn.

## Tóm tắt

| Mức độ | Phát hiện |
|---|---|
| Cao | Đổi mã nhân viên có thể yêu cầu nhập lại vô hạn khi danh sách có nhân viên khác. |
| Trung bình | Hiển thị tiền công nợ dùng định dạng số thực cho giá trị `Long`, gây lỗi lúc chạy. |
| Cần xác nhận nghiệp vụ | Công thức `tinhLoiNhuanRong()` chỉ lấy 1,6% lợi nhuận làm kết quả. |

## Chi tiết phát hiện

### 1. Điều kiện kiểm tra trùng mã nhân viên chưa chính xác

- **Mức độ:** Cao
- **Vị trí:** [`DSNhanVien.java`](../src/person/DSNhanVien.java), phương thức `suaNV`, đoạn kiểm tra mã mới.
- **Hành vi chủ đích:** Khi mã mới thật sự trùng, yêu cầu nhập lại là đúng và cần được giữ để tránh mã nhân viên bị lặp.
- **Vấn đề:** Trong vòng lặp, `biTrung` được đặt thành `true` mỗi khi gặp một nhân viên khác có mã khác `null`. Điều kiện không so sánh mã đang xét với `maMoi`, nên danh sách có nhân viên khác là đủ để bị xem như mã trùng.
- **Cách tái hiện:** Tạo danh sách có ít nhất hai nhân viên; sửa mã của một người và nhập mã hợp lệ nhưng không trùng với nhân viên nào. Chương trình vẫn yêu cầu nhập lại. Vì các nhân viên khác tiếp tục thỏa điều kiện sai, người dùng không thể thoát vòng nhập bằng cách nhập một mã mới hợp lệ.
- **Ảnh hưởng:** Không thể hoàn tất thao tác sửa mã nhân viên trong trường hợp trên.
- **Hướng xử lý:** Giữ cơ chế nhập lại khi trùng; chỉ đặt `biTrung = true` nếu mã của nhân viên khác bằng `maMoi` (nên so sánh không phân biệt hoa/thường nếu mã được xem là không phân biệt hoa/thường).
- **Đoạn code đề xuất** (thay điều kiện `if` hiện tại; giữ nguyên phần nhập lại mã bên trong):

```java
if (i != idx
        && maTrongDanhSach != null
        && maTrongDanhSach.equalsIgnoreCase(maMoi)) {
    biTrung = true;
    maMoi = Validation.inputStringRegex(
            sc, "Ma da ton tai, nhap ma nhan vien khac: ",
            Validation.MaNV_REGEX,
            "Ma nhan vien chua dung dinh dang (NVxxx)!");
    break;
}
```

Sau thay đổi này, vòng lặp vẫn yêu cầu nhập mã khác khi bị trùng thật; mã hợp lệ và chưa được dùng sẽ được chấp nhận. Đây là đoạn đề xuất trong báo cáo, chưa áp dụng vào `DSNhanVien.java`.

### 2. Định dạng tiền công nợ không tương thích kiểu `Long`

- **Mức độ:** Trung bình
- **Vị trí:** [`KeToan.java`](../src/person/KeToan.java), các dòng hiển thị tiền tại `hienThiVaThanhToanCongNo`.
- **Vấn đề:** `soTienCongNo` lưu `Long`, nhưng các lệnh `printf` truyền giá trị này cho placeholder `%.2f`, vốn yêu cầu kiểu số thực.
- **Cách tái hiện:** Ghi nhận một khoản công nợ, sau đó chọn chức năng hiển thị/theo dõi công nợ. Khi in số tiền, Java có thể ném `java.util.IllegalFormatConversionException`.
- **Ảnh hưởng:** Không hiển thị được danh sách công nợ; luồng thanh toán công nợ bị gián đoạn trước khi người dùng chọn khoản cần thanh toán.
- **Hướng xử lý:** Dùng định dạng số nguyên phù hợp với tiền VND (`%,d`) hoặc chuyển đổi sang kiểu số thực một cách có chủ đích nếu nghiệp vụ thật sự cần phần thập phân. Kiểm tra cả ba vị trí định dạng trong phương thức.

### 3. Công thức lợi nhuận ròng cần xác nhận

- **Mức độ:** Cần xác nhận nghiệp vụ, chưa kết luận là lỗi.
- **Vị trí:** [`KeToan.java`](../src/person/KeToan.java), phương thức `tinhLoiNhuanRong`.
- **Hiện trạng:** Phương thức tính `(long) (lợi nhuận * 0.08 * 0.2)`. Với lợi nhuận `120.000`, kết quả là `1.920`, tương ứng 1,6% lợi nhuận.
- **Cần xác nhận:** Nếu mục tiêu là tính thuế TNDN 20% trên lợi nhuận sau khi trừ VAT hoặc khoản thuế khác, công thức hiện tại có thể không đúng. Cần thống nhất rõ VAT được tính trên doanh thu hay lợi nhuận và phương thức này trả về tiền thuế hay lợi nhuận sau thuế trước khi thay đổi.

## Kiểm thử đã thực hiện

- Biên dịch toàn bộ mã nguồn `src` và `test`: **thành công**.
- Chạy `person.PersonPackageTest`: **qua 60 assertions**.
- Bộ test hiện có chưa kiểm tra trường hợp đổi mã nhân viên khi danh sách có nhiều người và chưa chạy luồng hiển thị công nợ; vì vậy hai lỗi nêu trên không bị phát hiện bởi các assertions hiện tại.

## Kết luận

Module `person` biên dịch được và các bài kiểm thử hiện có đều qua. Tuy nhiên, nên ưu tiên sửa lỗi đổi mã nhân viên và định dạng tiền công nợ trước khi merge; đồng thời xác nhận quy tắc tính lợi nhuận ròng với nhóm kế toán.
