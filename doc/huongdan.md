sử dụng duckdb vì, nhỏ nhẹ, hỗ trợ csv lẫn json
Hạn chế: vì sử lý file text nên tác vụ xóa sửa phức tạp hơn

thư mục utils chứa các class trợ giúp như database, khi dùng nhớ import utils

Sử dụng LocalDate thay vì Date vì nó hiện đại hơn Date

Quy trình nạp query vào class: match đúng trường hợp -> tạo class rỗng -> nạp variable -> return class

Quy trình đọc từ class vào bảng, sử dụng appender của duckdb, appender đê điền vào mỗi cột trên một dòng các giá trị mà
nó được truyền vào.


