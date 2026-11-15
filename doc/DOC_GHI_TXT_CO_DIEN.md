# Đọc/ghi TXT cổ điển

Giữ cách ghi từng thuộc tính theo dòng đã có trước khi tích hợp PR #43. Đã bỏ các nhánh đọc bản lưu tổng quát và chuyển dữ liệu đang dùng sang TXT dạng cột.

## Cách đọc/ghi hiện tại

1. `Main` gọi `GiaoDien.docDuLieu()` khi mở và `luuDuLieu()` khi kết thúc.
2. Lớp danh sách gọi `TepVanBan.doc()`: mở `BufferedReader` UTF-8, lấy từng dòng bằng `readLine()`.
3. Đối tượng tự đọc thuộc tính: `String[] p = dong.split(";", -1)`, chuyển số bằng `parseInt`/`parseLong`/`parseDouble`.
4. Khi ghi, đối tượng tự ghép thuộc tính bằng `toDongFile()`; lớp danh sách gọi `TepVanBan.ghi()`, dùng `BufferedWriter.write()` và `newLine()`.

Ví dụ một dòng:

```text
KMPHANTRAM;KM001;Giảm theo phần trăm;15/10/2026;13/01/2027;0;true;0.1;50000
```

Giữ UTF-8, các cột hiện tại, chú thích đầu file, bộ đếm mã và ghi qua file tạm như trước PR. Không thêm cơ chế lưu mới. Xem [cấu trúc cột](CAU_TRUC_FILE_TXT.md).

## Các phần đã sửa

| Phần | Thay đổi | Vị trí |
|---|---|---|
| `src/dichvu/Danhsachdoitac.java` | Bỏ nhánh đọc bản lưu tổng quát; giữ phần đọc TXT theo thuộc tính đã có. | [docFile():417](<C:/Users/Admin/IdeaProjects/My Project/java-oop-mini-supermarket/java-oop-mini-supermarket/src/dichvu/Danhsachdoitac.java:417>) |
| `src/dichvu/Danhsachkhuyenmai.java` | Bỏ nhánh đọc bản lưu tổng quát; giữ phần đọc TXT theo thuộc tính đã có. | [docFile():707](<C:/Users/Admin/IdeaProjects/My Project/java-oop-mini-supermarket/java-oop-mini-supermarket/src/dichvu/Danhsachkhuyenmai.java:707>) |
| `src/dichvu/DanhSachSPKM.java` | Bỏ nhánh đọc bản lưu tổng quát; giữ phần đọc TXT theo thuộc tính đã có. | [docFile():451](<C:/Users/Admin/IdeaProjects/My Project/java-oop-mini-supermarket/java-oop-mini-supermarket/src/dichvu/DanhSachSPKM.java:451>) |
| `src/hoadon/DanhSachCongNo.java` | Bỏ nhánh đọc bản lưu tổng quát; giữ phần đọc TXT theo thuộc tính đã có. | [docFile():319](<C:/Users/Admin/IdeaProjects/My Project/java-oop-mini-supermarket/java-oop-mini-supermarket/src/hoadon/DanhSachCongNo.java:319>) |
| `src/hoadon/DanhSachHoaDonBan.java` | Bỏ nhánh đọc bản lưu tổng quát; giữ phần đọc TXT theo thuộc tính đã có. | [docFile():669](<C:/Users/Admin/IdeaProjects/My Project/java-oop-mini-supermarket/java-oop-mini-supermarket/src/hoadon/DanhSachHoaDonBan.java:669>) |
| `src/hoadon/DanhSachHoaDonNhap.java` | Bỏ nhánh đọc bản lưu tổng quát; giữ phần đọc TXT theo thuộc tính đã có. | [docFile():1077](<C:/Users/Admin/IdeaProjects/My Project/java-oop-mini-supermarket/java-oop-mini-supermarket/src/hoadon/DanhSachHoaDonNhap.java:1077>) |
| `src/person/DSKhachHang.java` | Bỏ nhánh đọc bản lưu tổng quát; giữ phần đọc TXT theo thuộc tính đã có. | [docFile():739](<C:/Users/Admin/IdeaProjects/My Project/java-oop-mini-supermarket/java-oop-mini-supermarket/src/person/DSKhachHang.java:739>) |
| `src/person/DSNhanVien.java` | Bỏ nhánh đọc bản lưu tổng quát; giữ phần đọc TXT theo thuộc tính đã có. | [docFile():561](<C:/Users/Admin/IdeaProjects/My Project/java-oop-mini-supermarket/java-oop-mini-supermarket/src/person/DSNhanVien.java:561>) |
| `src/person/DSYeuCau.java` | Bỏ nhánh đọc bản lưu tổng quát; giữ phần đọc TXT theo thuộc tính đã có. | [docFile():204](<C:/Users/Admin/IdeaProjects/My Project/java-oop-mini-supermarket/java-oop-mini-supermarket/src/person/DSYeuCau.java:204>) |
| `src/sanpham/QuanLySanPham.java` | Bỏ nhánh đọc bản lưu tổng quát; giữ phần đọc TXT theo thuộc tính đã có. | [docFile():863](<C:/Users/Admin/IdeaProjects/My Project/java-oop-mini-supermarket/java-oop-mini-supermarket/src/sanpham/QuanLySanPham.java:863>) |
| `src/utils/DataBase.java` | Bỏ wrapper BanLuu và các bộ đọc cũ. Chỉ điều phối TXT dạng dòng; API đọc danh sách không còn nhánh CSV tổng quát. | [ghiDoiTuong():17](<C:/Users/Admin/IdeaProjects/My Project/java-oop-mini-supermarket/java-oop-mini-supermarket/src/utils/DataBase.java:17>), [docDoiTuong():22](<C:/Users/Admin/IdeaProjects/My Project/java-oop-mini-supermarket/java-oop-mini-supermarket/src/utils/DataBase.java:22>), [docfile():213](<C:/Users/Admin/IdeaProjects/My Project/java-oop-mini-supermarket/java-oop-mini-supermarket/src/utils/DataBase.java:213>) |
| `src/utils/KhoDuLieuTxt.java` | Nhân viên đăng nhập cũng chỉ đọc qua bộ TXT dạng dòng. | [docVaiTro():77](<C:/Users/Admin/IdeaProjects/My Project/java-oop-mini-supermarket/java-oop-mini-supermarket/src/utils/KhoDuLieuTxt.java:77>) |
| `src/utils/YeuCau.java` | Bỏ Base64 và nhánh CSV mã hóa; nội dung yêu cầu giữ bằng chuỗi bình thường. Vẫn giữ các dòng lịch sử lỗi để không mất lịch sử. | [docDanhSach():90](<C:/Users/Admin/IdeaProjects/My Project/java-oop-mini-supermarket/java-oop-mini-supermarket/src/utils/YeuCau.java:90>), [ghiDanhSach():134](<C:/Users/Admin/IdeaProjects/My Project/java-oop-mini-supermarket/java-oop-mini-supermarket/src/utils/YeuCau.java:134>) |

Đã bỏ ba file nguồn không còn dùng: `src/utils/XmlDoiTuongCu.java`, `src/utils/VanBanDoiTuong.java`, `src/utils/DocTxt2Cu.java`. Mã nguồn ứng dụng không còn gọi bộ đọc XML.

`Main.java`, `GiaoDien.java`, `DongDuLieu.java`, `TepVanBan.java`, `DuongDanLuuTru.java` và cơ chế lưu đủ mười file được giữ nguyên trong đợt này. Các hàm nghiệp vụ tích hợp từ PR #43 cũng giữ nguyên; ở các lớp danh sách chỉ sửa hàm đọc file.

## Dữ liệu đã chuyển

- Chuyển 25 file `.txt` đang có: mười file hiện tại, mười file bản trước, hai file trong lượt ghi cũ chưa hoàn thành, file yêu cầu và hai file xuất mẫu chỉ có chú thích.
- Đọc/ghi lại để đối chiếu trước khi thay dữ liệu. Tám sản phẩm, bốn nhân viên, ba khách hàng, ba nhà cung cấp, bốn khuyến mãi, ba hóa đơn bán, ba hóa đơn nhập và hai khoản công nợ vẫn được giữ.
- `src/luutru` không còn file TXT chứa cấu trúc XML. Nội dung yêu cầu dùng tiếng Việt trực tiếp.
- Bản gốc và mã nguồn trước đợt này nằm riêng ở `out/txt-co-dien/before`. Ứng dụng không nạp thư mục đó.

## Kiểm chứng

- Biên dịch toàn bộ `src` và các bộ kiểm thử được chọn: đạt.
- 58 kiểm tra logic PR #43, 72 kiểm tra TXT, 76 kiểm tra danh sách và 22 kiểm tra mã đã cấp: đạt.
- Lưu và mở lại ở hai JVM riêng: đạt; so sánh bộ dữ liệu TXT sau hai lần ghi: đạt.
- Đọc trực tiếp dữ liệu thật đã chuyển trong `src/luutru` rồi ghi vào thư mục thử: đạt.
- `Main` đọc TXT, đăng nhập kế toán, kết thúc khi EOF và lưu lại TXT: đạt trên thư mục thử.
- Bốn test `UnitTest` qua JUnit: đạt.
- 105 điều kiện đối chiếu xác nhận chỉ thay phần đọc/ghi và giữ logic đã tích hợp: đạt.

Các kiểm thử có ghi dữ liệu đều chạy trong `out/txt-co-dien/test-root` hoặc thư mục thử khác trong `out/txt-co-dien`. Dữ liệu thật chỉ được thay bằng bản TXT đã đối chiếu, không dùng làm nơi chạy nghiệp vụ thử.

Xem [vị trí toàn bộ hàm đọc/ghi](VI_TRI_HAM_DOC_GHI_TXT.md). Nhật ký và dấu kiểm dữ liệu nằm ở `out/txt-co-dien/test-results.json`, `data-manifest.json`, `audit.json` và `main-eof.log`.
