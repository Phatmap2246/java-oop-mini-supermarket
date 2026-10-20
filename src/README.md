# Mini Supermarket Management System (Java OOP)

Đồ án môn học Lập trình Hướng đối tượng. Dự án xây dựng hệ thống quản lý siêu thị mini với đầy đủ các nghiệp vụ cơ bản: quản lý nhân sự, sản phẩm, hóa đơn, khuyến mãi và thanh toán.

## Công nghệ sử dụng

- **Ngôn ngữ:** Java (JDK 21+)
- **Quản lý mã nguồn:** Git & GitHub
- **IDE khuyến nghị:** Visual Studio Code / IntelliJ IDEA

## 📁 Cấu trúc thư mục (Project Structure)

Toàn bộ mã nguồn nằm trong thư mục `src/`. Mỗi thành viên chỉ làm việc trong thư mục được phân công của mình để tránh xung đột code (conflict).

```text
SieuThiMini_Project/
│
├── src/
│   ├── person/          # Quản lý con người (Person, KhachHang, NhanVien...)
│   ├── sanpham/         # Quản lý sản phẩm (SanPham, ThucPham, TieuDung...)
│   ├── thanhtoan/       # Phương thức thanh toán (TienMat, ChuyenKhoan...)
│   ├── dichvu/          # Dịch vụ tiện ích (KhuyenMai, KhachHangThanThiet...)
│   ├── luutru/          # Quản lý danh sách (DSNhanVien, DSSanPham, DSHoaDon...)
│   ├── hoadon/          # Quản lý hóa đơn (HoaDon, CTHoaDon...)
│   ├── interfaces/      # Chứa các Interface (QuyenHan...)
│   └── Main.java        # Chương trình chính (Chứa menu tổng)
│
├── doc/                 # Chứa file README.md, tài liệu thiết kế, báo cáo
└── .gitignore           # Bỏ qua các file rác của Java (.class, bin/...)
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

