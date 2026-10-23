sử dụng duckdb vì, nhỏ nhẹ, hỗ trợ csv lẫn json
Hạn chế: vì sử lý file text nên tác vụ xóa sửa phức tạp hơn

thư mục utils chứa các class trợ giúp như database, khi dùng nhớ import utils

Sử dụng LocalDate thay vì Date vì nó hiện đại hơn Date

Quy trình nạp query vào class: match đúng trường hợp -> tạo class rỗng -> nạp variable -> return class

Quy trình từ class vào database/table: sử dụng objectmapper trong thư viện jackson để chuyển class thành json ->
duckdb đọc file json, lý do: tránh nạp object trực tiếp vào database vì phức tạp

Cách tạo class, class cơ sở nên có enum phân biệt các class kế thừa