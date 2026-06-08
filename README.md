# 甜品管理系统

一个基于 Spring Boot + Vue 的甜品管理系统。

## 技术栈

### 后端
- Java 21
- Spring Boot 3.2.x
- MyBatis
- MySQL 8.0+

### 前端
- Vue 3
- Vite
- TailwindCSS

## 项目结构

```
├── dessert-backend/      # 后端 Spring Boot 项目
├── dessert-management/   # 前端 Vue 项目
└── README.md
```

## 环境要求

- JDK 21+
- Node.js 18+
- MySQL 8.0+

## 快速开始

### 1. 数据库初始化

使用 MySQL 客户端执行 `dessert-backend/src/main/resources/schema.sql`：

```bash
mysql -u root -p < dessert-backend/src/main/resources/schema.sql
```

或者在 MySQL 命令行中：

```sql
source /path/to/dessert-backend/src/main/resources/schema.sql;
```

### 2. 配置数据库连接

修改 `dessert-backend/src/main/resources/application.properties`：

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/dessert_db?useUnicode=true&characterEncoding=utf-8&serverTimezone=Asia/Shanghai
spring.datasource.username=root
spring.datasource.password=your_password
```

### 3. 启动后端服务

```bash
cd dessert-backend
mvn spring-boot:run
```

后端服务将在 `http://localhost:8080` 启动。

### 4. 启动前端服务

```bash
cd dessert-management
npm install
npm run dev
```

前端服务将在 `http://localhost:5173` 启动。

## 测试账户

| 用户名 | 密码 | 角色 |
|--------|------|------|
| admin | password | 管理员 |
| user | password | 普通用户 |

## API 接口

### 用户管理
- `GET /api/users` - 获取用户列表
- `POST /api/users` - 创建用户
- `GET /api/users/{id}` - 获取用户详情
- `PUT /api/users/{id}` - 更新用户
- `DELETE /api/users/{id}` - 删除用户

### 分类管理
- `GET /api/categories` - 获取分类列表
- `POST /api/categories` - 创建分类
- `PUT /api/categories/{id}` - 更新分类
- `DELETE /api/categories/{id}` - 删除分类

### 甜品管理
- `GET /api/desserts` - 获取甜品列表（支持分页和筛选）
- `POST /api/desserts` - 创建甜品
- `PUT /api/desserts/{id}` - 更新甜品
- `DELETE /api/desserts/{id}` - 删除甜品

### 登录记录
- `GET /api/login-records` - 获取登录记录
