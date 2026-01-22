# Iku Project

## 📌 Giới thiệu

**Iku Project** là một dự án **Spring Boot REST API** được xây dựng trong quá trình **thực tập Backend Java**.  
Dự án tập trung vào việc quản lý **User – Project – Task**, áp dụng kiến trúc **RESTful API**, tách lớp rõ ràng và xử lý exception tập trung.

---

## 🎯 Mục tiêu

- Làm quen với cấu trúc project Spring Boot chuẩn
- Xây dựng REST API theo mô hình Controller – Service – Repository
- Áp dụng DTO, Validation, Exception Handling
- Thực hành sử dụng Git & GitHub để quản lý source code
- Chuẩn bị nền tảng cho các module nâng cao trong các tuần tiếp theo

---

## 📄 README Structure

- Giới thiệu dự án
- Mục tiêu thực tập
- Công nghệ sử dụng
- Cấu trúc project
- Chức năng đã hoàn thành
- API mẫu
- Quy ước API
- Cấu hình Database
- Hướng dẫn chạy project
- Kế hoạch phát triển

---

## 🛠 Công nghệ sử dụng

- **Java 17**
- **Spring Boot**
- **Spring Web**
- **Spring Data JPA**
- **Hibernate**
- **ModelMapper**
- **Maven**
- **Database**: SQL Server
- **Postman** (test API)
- **Git & GitHub**

---

## 📂 Cấu trúc project

```
src/main/java/com/example/projectiku
│
├── controller        # Xử lý request/response API
├── service           # Interface service
│   └── impl          # Business logic
├── repository        # JPA Repository
├── dto               # Request / Response DTO
├── entity            # Entity mapping database
├── enums             # Enum trạng thái
├── exception         # Custom Exception & Global Handler
├── config            # Cấu hình (ModelMapper, etc.)
└── ProjectIkuApplication.java
```

---

## 🚀 Chức năng đã hoàn thành

### 👤 User Module

- Lấy danh sách user
- Lấy user theo ID
- Thêm mới user
- Cập nhật user
- Xóa user
- Validate dữ liệu đầu vào
- Kiểm tra **trùng username & email**
- Custom Exception & Global Exception Handler

### 📌 Project Module _(đang phát triển)_

- CRUD Project
- Quản lý trạng thái Project

### 📝 Task Module _(đang phát triển)_

- CRUD Task
- Gán Task cho User
- Gán Task cho Project

---

## 📮 API mẫu

### User API

| Method | Endpoint          | Mô tả              |
| ------ | ----------------- | ------------------ |
| GET    | `/api/users`      | Lấy danh sách user |
| GET    | `/api/users/{id}` | Lấy user theo ID   |
| POST   | `/api/users`      | Thêm user          |
| PUT    | `/api/users/{id}` | Cập nhật user      |
| DELETE | `/api/users/{id}` | Xóa user           |

---

## 📌 Quy ước API

- API tuân theo chuẩn **RESTful**
- Request / Response sử dụng **JSON**
- HTTP Status Code:
  - `200` – Thành công
  - `201` – Tạo mới thành công
  - `400` – Dữ liệu không hợp lệ
  - `404` – Không tìm thấy tài nguyên
  - `409` – Dữ liệu bị trùng
  - `500` – Lỗi hệ thống

---

## ⚠️ Xử lý Exception

- `CustomResourceNotFoundException` → **404 NOT FOUND**
- `CustomDuplicateResourceException` → **409 CONFLICT**
- Validation Error → **400 BAD REQUEST**
- Global Exception Handling bằng `@ControllerAdvice`

---

## 🗄 Database Configuration (SQL Server)

### 📌 Database

- **DBMS**: Microsoft SQL Server
- **ORM**: Spring Data JPA (Hibernate)
- **Database Name**: `project_iku`
- **Port**: `1433`

---

## ⚙️ Application Configuration

Cấu hình trong file:  
`src/main/resources/application.properties`

```properties
spring.datasource.url=jdbc:sqlserver://localhost:1433;databaseName=project_iku;trustServerCertificate=true
spring.datasource.username=sa
spring.datasource.password=123

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.SQLServerDialect
```

### ⚠️ Lưu ý

- username và password chỉ mang tính minh họa
- Cần cài đặt SQL Server và đảm bảo service đang chạy
- Database project_iku phải được tạo trước

### 🧠 Hibernate Configuration

- ddl-auto=update
→ Tự động tạo & cập nhật bảng theo Entity, không làm mất dữ liệu

- show-sql=true
→ Hiển thị SQL trên console để debug

### 🗂 Entity Mapping

| Entity  | Mô tả               |
| ------- | ------------------- |
| User    | Người dùng hệ thống |
| Project | Dự án               |
| Task    | Công việc           |

### Quan hệ

Một User có nhiều Task

Một Project có nhiều Task

Mỗi Task thuộc về một User và một Project

---

## ▶️ Cách chạy project

### 1️⃣ Clone project

Sao chép mã: git clone https://github.com/hkhanh0402/Iku-Project.git

### 2️⃣ Tạo database

Sao chép mã: CREATE DATABASE project_iku;

### 3️⃣ Chạy ứng dụng

mvn spring-boot:run
Hoặc chạy trực tiếp bằng IDE (IntelliJ / Eclipse).

---

## 🧪 Test API

Sử dụng Postman để test API

Test các trường hợp:

Thành công

Dữ liệu không hợp lệ

Không tìm thấy tài nguyên

Trùng dữ liệu

---

## 📅 Kế hoạch phát triển

Xây dựng cấu trúc project Spring Boot

Hoàn thành User Module

Hoàn thành Project Module

Hoàn thành Task Module

Thêm Authentication & Authorization

Viết Unit Test

Hoàn thiện tài liệu API

---

## 👨‍💻 Thông tin sinh viên

Họ tên: Hoàng Nam Khánh

Vị trí: Thực tập sinh Backend Java

Công nghệ: Java – Spring Boot

GitHub: https://github.com/hkhanh0402

---

```

```
