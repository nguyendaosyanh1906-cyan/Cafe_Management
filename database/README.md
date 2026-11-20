# Hướng Dẫn Cài Đặt & Khởi Tạo Cơ Sở Dữ Liệu (`cafe_management_db`)

Thư mục `database/` chứa các kịch bản SQL dùng để khởi tạo cấu trúc bảng và dữ liệu mẫu cho hệ thống **Quản Lý Quán Cà Phê (CafeShopManagement)**:
* **`schema.sql`**: Tạo database `cafe_management_db` (chuẩn mã hóa `utf8mb4`) cùng toàn bộ 8 bảng dữ liệu và các ràng buộc khóa ngoại.
* **`seed.sql`**: Nạp dữ liệu mẫu ban đầu (tài khoản nhân sự, danh mục, sản phẩm, topping, khách hàng thân thiết và hóa đơn mẫu) để phục vụ việc kiểm thử.

---

## 1. Yêu cầu môi trường
Trước khi thực hiện, hãy đảm bảo máy tính của bạn đã cài đặt:
* **MySQL Server** (khuyến nghị phiên bản 8.0 trở lên) đang chạy ở cổng mặc định `3306`.
* Một trong các công cụ để thực thi SQL: **MySQL Workbench**, **DBeaver**, **Terminal/Command Prompt**, hoặc **IntelliJ IDEA**.

---

## 2. Hướng dẫn chạy file SQL (Chọn 1 trong 3 cách)

> **Lưu ý quan trọng:** Luôn chạy file **`schema.sql` trước**, sau đó mới chạy file **`seed.sql`**. Nếu trên máy đã có sẵn database `cafe_management_db` cũ và muốn làm mới lại từ đầu, hãy chạy lệnh `DROP DATABASE IF EXISTS cafe_management_db;` trước.

### Cách 1: Sử dụng Terminal / Command Prompt (Nhanh & Ít lỗi nhất)
Bạn có thể dùng Terminal tích hợp sẵn ngay trong IntelliJ IDEA (biểu tượng `>_` ở thanh công cụ bên trái) hoặc Command Prompt/Terminal của hệ điều hành:

1. Di chuyển đến thư mục gốc của dự án và đăng nhập vào MySQL:
   ```bash
   mysql -u root -p
   ```
   *(Nhập mật khẩu tài khoản `root` của bạn và nhấn Enter).*

2. Khi dấu nhắc chuyển sang `mysql>`, chạy lần lượt 2 lệnh sau để nạp cấu trúc và dữ liệu:
   ```sql
   SOURCE database/schema.sql;
   SOURCE database/seed.sql;
   ```

3. Kiểm tra nhanh xem dữ liệu đã vào đủ chưa:
   ```sql
   SHOW TABLES;
   SELECT username, role, full_name FROM Users;
   ```

---

### Cách 2: Sử dụng MySQL Workbench hoặc DBeaver (Giao diện trực quan)
1. Mở **MySQL Workbench** (hoặc DBeaver) và kết nối vào server MySQL Local (`localhost:3306`).
2. Vào **File -> Open SQL Script...**, chọn file `database/schema.sql`.
3. Bấm biểu tượng **Execute All** (hình tia sét không có chữ I) để chạy toàn bộ file. Đảm bảo bảng thông báo bên dưới hiện dấu tích xanh cho tất cả các lệnh.
4. Tiếp tục mở file `database/seed.sql` và bấm **Execute All** để nạp dữ liệu mẫu.
5. Bấm **Refresh** ở cột danh sách Schemas bên trái để kiểm tra `cafe_management_db` đã xuất hiện kèm đủ 8 bảng.

---

### Cách 3: Chạy trực tiếp trên IntelliJ IDEA Community (Dùng Plugin DB Navigator)
Nếu bạn dùng bản IntelliJ IDEA Community (miễn phí) và muốn chạy SQL ngay trong IDE:

1. **Cài đặt Plugin:** Vào **Settings -> Plugins -> Marketplace**, tìm và cài đặt **`Database Navigator`**, sau đó khởi động lại IDE.
2. **Tạo kết nối MySQL:**
   * Mở **Settings -> Tools -> Database Navigator -> Connections**.
   * Bấm dấu **`+`** -> chọn **MySQL**.
   * Điền các thông số:
     * **Host:** `localhost` | **Port:** `3306`
     * **Database:** `mysql` *(dùng database hệ thống để tránh lỗi khi chưa tạo `cafe_management_db`)*
     * **User / Password:** Nhập tài khoản MySQL của bạn.
   * Chuyển sang thẻ **Details** của kết nối vừa tạo, tích chọn **`Auto-Commit`** để dữ liệu từ file `seed.sql` tự động được lưu xuống ổ đĩa, sau đó bấm **Apply -> OK**.
3. **Thực thi SQL:**
   * Mở file `schema.sql`, chọn kết nối vừa tạo trên thanh công cụ của file và chạy lần lượt các khối lệnh từ trên xuống dưới.
   * Mở file `seed.sql` và thực hiện tương tự để nạp dữ liệu mẫu.
   * LƯU Ý: nhớ chạy từng đoạn code nhỏ từ trên xuống dưới để tránh lỗi(schema.sql rồi tới seed.sql)

---

## 3. Cấu hình kết nối trong mã nguồn Java
Sau khi tạo database thành công, hãy mở file `DBConnection.java` trong thư mục `src/` và cập nhật thông tin đăng nhập khớp với MySQL trên máy của bạn:

```java
private static final String URL = "jdbc:mysql://localhost:3306/cafe_management_db";
private static final String USER = "root";
private static final String PASSWORD = "your_mysql_password"; // Thay bằng mật khẩu của bạn
```

---

## 4. Dữ liệu mẫu dùng để đăng nhập & kiểm thử (Test)
Sau khi chạy xong `seed.sql`, bạn có thể khởi chạy ứng dụng (`CafeApp.java` hoặc `Launcher.java`) và dùng các thông tin sau để test:

| Loại dữ liệu | Thông tin đăng nhập / Tra cứu | Ghi chú phân quyền & nghiệp vụ |
| :--- | :--- | :--- |
| **Tài khoản Quản trị (Admin)** | User: `admin` / Pass: `123` | Toàn quyền quản lý món, topping, nhân viên, khách hàng và xem thống kê doanh thu. |
| **Tài khoản Nhân viên (Staff)** | User: `staff1` / Pass: `123` | Truy cập giao diện bán hàng (POS), tạo hóa đơn và tích/đổi điểm cho khách. |
| **Khách hàng thân thiết mẫu** | SĐT: `104240047` | Tên: *Nguyễn Phúc Vinh* — Có sẵn **1.000 điểm** để test tính năng giảm giá khi thanh toán. |