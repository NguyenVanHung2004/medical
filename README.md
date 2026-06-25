# Medical App (Android)

Dự án ứng dụng Y tế (Medical App) phát triển trên nền tảng Android, sử dụng ngôn ngữ Kotlin và Jetpack Compose. Dự án áp dụng mô hình MVVM và Clean Architecture. Hệ thống phân chia thành 2 luồng người dùng chính: **Bác sĩ (Doctor)** và **Bệnh nhân (Patient)**.

## 🚀 Các tính năng và Màn hình đã hoàn thành

### 1. 🔐 Xác thực (Authentication) & Khởi động
- **Intro / Welcome**: Màn hình chào mừng và giới thiệu ứng dụng.
- **Login**: Đăng nhập hệ thống (Hỗ trợ tài khoản thường và Google Auth).
- **Register**: Đăng ký tài khoản mới.
- **Forgot Password**: Quên và khôi phục mật khẩu.

### 2. 👨‍⚕️ Dành cho Bác sĩ (Doctor)
- **Home Dashboard**: Bảng điều khiển chính, tổng quan lịch trình làm việc và thống kê.
- **Profile / Complete Profile**: Quản lý và cập nhật hồ sơ cá nhân/chuyên môn của bác sĩ.
- **Appointment Management (Quản lý lịch hẹn)**:
  - **Appointment List**: Danh sách các lịch khám (Sắp tới, đã hoàn thành, bị hủy...).
  - **Appointment Detail**: Chi tiết một lịch khám, hồ sơ bệnh nhân và thông tin thanh toán.
- **Patient Management (Quản lý bệnh nhân)**:
  - **Patient List**: Danh sách bệnh nhân bác sĩ đang phụ trách.
  - **Patient Detail**: Xem hồ sơ y tế chi tiết của từng bệnh nhân.
- **Notifications**: Màn hình thông báo nhắc nhở lịch khám, thay đổi trạng thái.

### 3. 🤒 Dành cho Bệnh nhân (Patient)
- **Patient Home**: Màn hình chính dành cho bệnh nhân (Gợi ý chuyên khoa, dịch vụ, bác sĩ nổi bật).
- **Profile / Complete Profile**: Quản lý hồ sơ bệnh nhân, lịch sử y tế.
- **Doctor Discovery & Booking (Tìm kiếm & Đặt lịch)**:
  - **Doctor List**: Danh sách các bác sĩ, cho phép tìm kiếm và lọc.
  - **Booking**: Quy trình đặt lịch hẹn khám với bác sĩ được chọn.
- **Appointment Management (Quản lý lịch khám)**:
  - **Appointments**: Theo dõi các lịch hẹn cá nhân.
  - **Appointment Detail**: Chi tiết lịch hẹn, thông tin bác sĩ, trạng thái đặt khám.
- **Notifications**: Các thông báo về thay đổi trạng thái lịch hẹn, hệ thống.

## 🛠 Công nghệ sử dụng chính
- **Ngôn ngữ**: Kotlin
- **UI Toolkit**: Jetpack Compose (Modern Android UI)
- **Kiến trúc**: MVVM (Model-View-ViewModel), Clean Architecture
- **Dependency Injection**: Koin
- **Bất đồng bộ & Data Stream**: Kotlin Coroutines & Flow
