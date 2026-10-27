# Mini Supermarket Management System (Java OOP)

Đồ án môn học Lập trình Hướng đối tượng. Dự án xây dựng hệ thống quản lý siêu thị mini với đầy đủ các nghiệp vụ cơ bản: quản lý nhân sự, sản phẩm, hóa đơn, khuyến mãi và thanh toán.

## Công nghệ sử dụng

- **Ngôn ngữ:** Java (JDK 21+)
- **Quản lý mã nguồn:** Git & GitHub
- **IDE khuyến nghị:** Visual Studio Code / IntelliJ IDEA

## 📁 Cấu trúc thư mục (Project Structure)

Toàn bộ mã nguồn nằm trong thư mục `src/`. Mỗi thành viên chỉ làm việc trong thư mục được phân công của mình để tránh xung đột code (conflict).

```text
java-oop-mini-supermarket/
│
├── src/
│   ├── person/          # Quản lý con người (Person, KhachHang, NhanVien...)
│   ├── sanpham/         # Quản lý sản phẩm (SanPham, ThucPham, TieuDung...)
│   ├── thanhtoan/       # Phương thức thanh toán (TienMat, ChuyenKhoan...)
│   ├── dichvu/          # Dịch vụ tiện ích (KhuyenMai, KhachHangThanThiet...)
│   ├── luutru/          # Quản lý danh sách (DSNhanVien, DSSanPham, DSHoaDon...)
│   ├── hoadon/          # Quản lý hóa đơn (HoaDon, CTHoaDon, IThanhToan...)
│   ├── interfaces/      # Chứa các Interface dùng chung (QuyenHan...)
│   ├── utils/           # Chứa các class tiện ích dùng chung (Validation.java...)
│   └── Main.java        # Chương trình chính (Chứa menu tổng)
│
├── lib/                 # Chứa các thư viện ngoài (.jar) như ZXing (nếu dùng QR Code)
├── doc/                 # Chứa tài liệu thiết kế, báo cáo, file hướng dẫn (huongdan.md...)
├── .gitignore           # Bỏ qua các file rác của Java (.class, bin/...) và IDE
└── README.md            # File hiển thị trang chủ GitHub (giới thiệu và hướng dẫn nhóm)

## Module Tiện Ích Dùng Chung (Utils)

Thư mục `src/utils/` chứa các class hỗ trợ (Helper Class) được sử dụng chung cho toàn bộ dự án, giúp giảm thiểu việc lặp lại code (DRY) và tăng tính ổn định cho chương trình.

### Class `FileUtil`
`FileUtil` đọc và ghi danh sách bản ghi dạng các trường phân cách bằng dấu phẩy, sử dụng UTF-8. Mỗi dòng là một bản ghi; các trường có dấu phẩy hoặc dấu ngoặc kép sẽ được đặt trong ngoặc kép và dấu ngoặc kép bên trong được nhân đôi. Trường không được chứa ký tự xuống dòng.

```java
import java.io.IOException;
import java.util.ArrayList;
import utils.FileUtil;

ArrayList<String[]> records = new ArrayList<>();
records.add(new String[] {"NV001", "Nguyễn Văn An", "12, Lê Lợi"});
FileUtil.writeFile("data/nhanvien.txt", records);

ArrayList<String[]> loadedRecords = FileUtil.readFile("data/nhanvien.txt");
```

Hai phương thức `readFile` và `writeFile` ném `IOException`; nơi gọi cần xử lý hoặc khai báo ngoại lệ để lỗi đọc/ghi không bị bỏ qua.

### 1. Class `Validation`
Class này được tạo ra để giải quyết 2 vấn đề nhức nhối khi làm việc với Console (theo yêu cầu bẫy lỗi của môn học):
- **Chống sập chương trình (Try-Catch):** Khi người dùng nhập chữ cái vào chỗ yêu cầu nhập số, chương trình sẽ không bị văng lỗi `InputMismatchException` mà sẽ báo lỗi và yêu cầu nhập lại.
- **Kiểm tra định dạng (Regex):** Đảm bảo người dùng nhập đúng chuẩn dữ liệu (VD: Mã hóa đơn phải bắt đầu bằng HD, Mã sản phẩm bắt đầu bằng SP...).

**Danh sách các hàm hỗ trợ:**
- `inputInt(Scanner sc, String message)`: Nhập số nguyên an toàn. Tự động bắt lỗi và yêu cầu nhập lại nếu nhập sai.
- `inputDouble(Scanner sc, String message)`: Nhập số thực (double) an toàn. Tự động bắt lỗi và yêu cầu nhập lại nếu nhập sai.
- `inputStringRegex(Scanner sc, String message, String regex, String errorMessage)`: Nhập chuỗi và kiểm tra bằng biểu thức chính quy (Regex). Nếu sai định dạng, in ra `errorMessage` và yêu cầu nhập lại.

**Ví dụ cách sử dụng trong code:**
Thay vì dùng `sc.nextInt()` dễ gây sập chương trình, hãy dùng:
```java
import utils.Validation;

// Nhập số lượng (tự động bắt lỗi nếu nhập chữ)
int soLuong = Validation.inputInt(sc, "Nhập số lượng: ");

// Nhập mã hóa đơn (Bắt buộc bắt đầu bằng HD + số)
String maHD = Validation.inputStringRegex(sc, 
    "Nhập mã HĐ: ", 
    "^HD\\d+$", 
    "Lỗi: Mã HĐ phải bắt đầu bằng HD và theo sau là số!");
```
## 📌 Quy tắc Code & Push Git (Bắt buộc đọc)

Để dự án chạy mượt mà, tránh xung đột code (conflict) và dễ dàng kiểm soát lỗi, tất cả thành viên cần tuân thủ nghiêm ngặt quy trình sau:

1. **Package Name:** File `.java` nằm trong thư mục nào thì dòng đầu tiên phải khai báo đúng package đó.
   - *Ví dụ:* File `Person.java` nằm trong thư mục `person/` thì dòng đầu là `package person;`
2. **Tên biến/hàm:** Sử dụng chuẩn camelCase (VD: `tinhTongTien()`, `hoTen`, `ngaySinh`).
3. **Không sửa file của người khác:** Chỉ tạo và sửa file trong thư mục mình được phân công.
4. **Ghi chú (Comment):** Mọi hàm phức tạp cần có comment giải thích và ghi ngày tháng làm (theo yêu cầu môn học).

## Quy trình làm việc với Git (Git Workflow)

**TUYỆT ĐỐI KHÔNG** code và push trực tiếp lên nhánh `main`. Hãy làm theo quy trình sau:
### Bước 1: Cập nhật code mới nhất từ nhánh main
```bash
git checkout main
git pull origin main
```

### Bước 2: Tạo nhánh (branch) mới cho phần việc của bạn
Đặt tên nhánh theo cú pháp: `feature/ten-phan-viec` hoặc `fix/ten-loi`.
```bash
# Ví dụ:
git checkout -b feature/person-module
git checkout -b feature/hoadon-module
```

### Bước 3: Code và Commit
Sau khi code xong, lưu file và chạy lệnh:
```bash
git add .
git commit -m "Ten thanh vien - Mo ta chuc nang vua lam"
```

### Bước 4: Đẩy nhánh của bạn lên GitHub
```bash
git push -u origin feature/ten-phan-viec
```

### Bước 5: Tạo Pull Request (PR) trên GitHub
1. Lên trang GitHub của nhóm, bấm "Compare & pull request".
2. Gán người review (thường là Nhóm trưởng).
3. Đợi Nhóm trưởng check code và bấm "Merge pull request" để gộp vào `main`.

### Bước 6: Dọn dẹp sau khi Merge
Sau khi code đã được gộp vào `main`, bạn quay lại máy và xóa nhánh cũ để tránh rác:
```bash
git checkout main
git pull origin main
git branch -d feature/ten-phan-viec
```

---
