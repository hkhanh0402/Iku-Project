# Iku Project

## 📌 Giới thiệu

**Iku Project** là một dự án **Spring Boot REST API** được xây dựng trong quá trình **thực tập Backend Java**.  
Dự án tập trung vào việc quản lý **User – Project – Task**, áp dụng kiến trúc **RESTful API**, tách lớp rõ ràng và xử lý exception tập trung.

---

## 📋 Mô tả nghiệp vụ (Business Description)

Hệ thống **Iku Project** là hệ thống quản lý công việc nội bộ,
được xây dựng nhằm hỗ trợ việc quản lý **người dùng (User)**,
**dự án (Project)** và **công việc (Task)**.

### 👤 User

User là người sử dụng hệ thống, có thể tham gia nhiều Project
và được gán nhiều Task khác nhau.

### 📌 Project

Project đại diện cho một dự án cụ thể.
Mỗi Project bao gồm nhiều Task và có trạng thái riêng
(PLANNING, IN_PROGRESS, DONE).

### 📝 Task

Task là đơn vị công việc nhỏ nhất trong hệ thống.

Mỗi Task:

- Thuộc về **một Project**
- Được gán cho **một User**
- Có trạng thái xử lý (TODO, IN_PROGRESS, DONE)

### 🔗 Quan hệ nghiệp vụ

- Một User có thể được gán nhiều Task
- Một Project có thể chứa nhiều Task
- Mỗi Task chỉ thuộc về một User và một Project tại một thời điểm

Hệ thống hỗ trợ các chức năng:
- Quản lý User
- Quản lý Project
- Quản lý Task
- Theo dõi trạng thái công việc

---

## 🎯 Mục tiêu

- Làm quen với cấu trúc project Spring Boot chuẩn
- Xây dựng REST API theo mô hình Controller – Service – Repository
- Áp dụng DTO, Validation, Exception Handling
- Thực hành Unit Test với JUnit & Mockito
- Triển khai tài liệu API với Swagger UI
- Đóng gói ứng dụng và phân tách môi trường (Dev/Prod)

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
- **Spring Boot 3.x**
- **Spring Data JPA & Hibernate**
- **Spring Security & JWT (JSON Web Token)**
- **Springdoc OpenAPI (Swagger UI)**
- **JUnit 5 & Mockito** (Unit Testing)
- **Database**: SQL Server
- **Công cụ**: Maven, Postman, Git & GitHub

---

## 📂 Cấu trúc project

```
src/main/java/com/example/projectiku
│
├── config        # Cấu hình Security, Swagger, ModelMapper, v.v.
├── controller    # Xử lý request/response API
├── dto           # Request / Response DTO
├── entity        # Entity mapping database
├── enums         # Enum trạng thái
├── exception     # Custom Exception & Global Handler
├── repository    # JPA Repository
├── security      # Filter và xử lý JWT
└── service       # Interface service & Business logic (impl)
```

---

## 🚀 Chức năng đã hoàn thành

### 👤 User Module

- CRUD User
- Validate dữ liệu
- Kiểm tra trùng username, email
- DTO + ModelMapper
- Custom Exception + Global Handler

### 📌 Project Module _

- CRUD Project
- Quản lý trạng thái (PLANNING, IN_PROGRESS, DONE)
- Quan hệ 1-N với Task

### 📝 Task Module 

- CRUD Task
- Gán Task cho User
- Gán Task cho Project
- Lấy Task theo User
- Lấy Task theo Project
- Quản lý trạng thái (TODO, IN_PROGRESS, DONE)
- Validate dữ liệu đầu vào
- Kiểm tra tồn tại User & Project trước khi tạo Task
- Mapping quan hệ @ManyToOne
- Xử lý Exception khi không tìm thấy User / Project

### 🔐 Authentication & Authorization

- Xác thực người dùng bằng JWT
- Phân quyền USER / MANAGER bằng Spring Security
- Bảo vệ API theo role
- Xử lý 401 (Unauthorized) và 403 (Forbidden)
- Lấy thông tin user từ token
- API `/api/tasks/me` cho USER

---

## 📮 API mẫu

### Task API

### Task API

| Method | Endpoint                        | Mô tả                 |
|-------|----------------------------------|------                 |
| GET   | `/api/tasks`                     | Lấy tất cả task       |
| GET   | `/api/tasks/user/{userId}`       | Lấy task theo user    |
| GET   | `/api/tasks/project/{projectId}` | Lấy task theo project |
| POST  | `/api/tasks`                     | Tạo task              |
| PUT   | `/api/tasks/{id}`                | Cập nhật task         |
| DELETE| `/api/tasks/{id}`                | Xóa task              |

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
  - API được bảo vệ bằng JWT
  - Truyền token qua Header: Authorization: Bearer <token>
  
---

## ⚠️ Xử lý Exception

- `CustomResourceNotFoundException` → **404 NOT FOUND**
- `CustomDuplicateResourceException` → **409 CONFLICT**
- Validation Error → **400 BAD REQUEST**
- Global Exception Handling bằng `@ControllerAdvice`

---

## ⚙️ Cấu hình Hệ thống (Profile & Database)

Dự án hỗ trợ 2 môi trường thông qua Profile:
- **DEV (`application-dev.properties`)**: Chạy ở Local, hiển thị log SQL chi tiết.
- **PROD (`application-prod.properties`)**: Chạy ở Server, ẩn log SQL để bảo mật và tối ưu hiệu năng.

### 🗄 Database (SQL Server)
- **Database Name**: `project_iku`
- **Port**: `1433`

Đảm bảo bạn đã tạo Database trước khi chạy ứng dụng:
```sql
CREATE DATABASE project_iku;
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

- Một User – N Task

- Một Project – N Task

- Task chứa:

+ user_id (FK)

+ project_id (FK)

---

## ▶️ Hướng dẫn Setup & Chạy Project

### Cách 1: Chạy trực tiếp qua IDE (Dành cho Dev)
1. Clone project: `git clone https://github.com/hkhanh0402/Iku-Project.git`
2. Mở file `application-dev.properties` và sửa thông tin `username`/`password` SQL Server của bạn.
3. Chạy file `ProjectIkuApplication.java` trên IntelliJ / Eclipse. Mặc định hệ thống sẽ dùng profile `dev`.

### Cách 2: Build và chạy file JAR (Dành cho Deploy)
1. Mở Terminal tại thư mục gốc của project, tiến hành build JAR: ``` mvn clean package -DskipTests ```
2. Chạy file JAR với môi trường Production: ``` java -jar target/project-iku-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod ```

---

## 🧪 Hướng dẫn Test API qua Swagger (Auth)

Dự án đã tích hợp sẵn **Swagger UI** để test API trực quan thay vì dùng Postman.

1. **Truy cập Swagger**: Mở trình duyệt và vào link `http://localhost:8080/swagger-ui/index.html`
2. **Lấy Token (Login)**:
   - Kéo xuống mục **1. Authentication**, mở API `POST /api/auth/login`.
   - Bấm **Try it out**, nhập `username` và `password` (Nên dùng tài khoản có role MANAGER).
   - Bấm **Execute**. Copy chuỗi Token trả về trong phần Response (không copy dấu ngoặc kép).
3. **Xác thực (Authorize)**:
   - Cuộn lên đầu trang, bấm vào nút **Authorize** (hình ổ khóa).
   - Dán chuỗi token vừa copy vào ô Value và bấm Authorize.
4. **Test API**: Bây giờ bạn có thể test bất kỳ API nào của Project, Task hay User trực tiếp trên web.

---

## 📅 Kế hoạch phát triển

- Xây dựng cấu trúc project Spring Boot ✅
- Hoàn thành User Module ✅
- Hoàn thành Task Module cơ bản ✅
- Hoàn thành CRUD Project cơ bản ✅
- Thêm Authentication & Authorization (JWT, Spring Security) ✅
- Viết Unit Test ✅
- Tách cấu hình Profile & Build JAR ✅
- Hoàn thiện Swagger / OpenAPI ✅

---

## 👨‍💻 Thông tin sinh viên

Họ tên: Hoàng Nam Khánh

Vị trí: Thực tập sinh Backend Java

Công nghệ: Java – Spring Boot

GitHub: https://github.com/hkhanh0402

---

```

```
