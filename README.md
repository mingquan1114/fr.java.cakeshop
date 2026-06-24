# 🍰 蛋糕管理系统

> 一个基于 Spring Boot 的蛋糕销售电商平台，支持用户购物、骑士配送和管理员管理等功能。

---

## 📋 目录

- [项目简介](#项目简介)
- [技术栈](#技术栈)
- [功能特性](#功能特性)
- [快速开始](#快速开始)
- [项目结构](#项目结构)
- [API 接口](#api-接口)
- [数据库设计](#数据库设计)
- [部署说明](#部署说明)
- [许可证](#许可证)

---

## 🌟 项目简介

蛋糕管理系统是一个综合性的电商平台，包含以下三大模块：

| 模块 | 描述 | 访问路径 |
|------|------|----------|
| **用户端** | 商品浏览、购物车、订单下单 | `/` |
| **骑士端** | 订单接单、配送管理、收入统计 | `/knight/login` |
| **管理员端** | 用户管理、商品管理、订单管理 | `/admin/index` |

---

## 🛠️ 技术栈

| 分类 | 技术 | 版本 |
|------|------|------|
| 后端框架 | Spring Boot | 2.2.6.RELEASE |
| 数据库 | MySQL | 5.7+ |
| ORM框架 | MyBatis Plus | 3.5.0 |
| 前端模板 | Thymeleaf | 3.0.15 |
| 前端样式 | Bootstrap | 4.5.0 |
| 图标库 | Font Awesome | 5.15.0 |
| 构建工具 | Maven | 3.8+ |

---

## ✨ 功能特性

### 用户端功能
- ✅ 用户注册与登录
- ✅ 商品浏览与搜索
- ✅ 购物车管理
- ✅ 订单下单与支付
- ✅ 订单状态跟踪

### 骑士端功能
- ✅ 骑士登录与认证
- ✅ 待接单订单列表
- ✅ 订单接单/取货/送达操作
- ✅ 收入统计与明细
- ✅ 消息通知

### 管理员端功能
- ✅ 用户管理（审核、编辑、删除）
- ✅ 商品管理（新增、编辑、删除、库存预警）
- ✅ 分类管理
- ✅ 订单管理（发货、完成、筛选）
- ✅ 操作日志记录
- ✅ 多语言支持（中文/英文/日语）

---

## 🚀 快速开始

### 环境要求

- JDK 1.8+
- Maven 3.8+
- MySQL 5.7+

### 安装步骤

1. **克隆项目**

```bash
git clone https://github.com/your-repo/cake-management-system.git
cd cake-management-system
```

2. **配置数据库**

创建数据库并导入初始化脚本：

```sql
CREATE DATABASE cakeshop CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE cakeshop;
SOURCE knight.sql;
```

3. **修改配置**

编辑 `src/main/resources/application.yml`：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/cakeshop?useUnicode=true&characterEncoding=utf-8&serverTimezone=Asia/Shanghai
    username: your_username
    password: your_password
```

4. **运行项目**

```bash
mvn spring-boot:run
```

5. **访问项目**

- 用户首页：http://localhost:8080
- 骑士端：http://localhost:8080/knight/login
- 管理员端：http://localhost:8080/admin/index

### 默认账号

| 角色 | 用户名 | 密码 |
|------|--------|------|
| 管理员 | admin | admin |
| 骑士 | knight1 | 123456 |
| 普通用户 | test | test123 |

---

## 📁 项目结构

```
cake-management-system/
├── src/
│   └── main/
│       ├── java/com/fr/
│       │   ├── controller/        # 控制器层
│       │   │   ├── AdminController.java    # 管理员控制器
│       │   │   ├── UserController.java     # 用户控制器
│       │   │   ├── KnightController.java   # 骑士控制器
│       │   │   ├── GoodsController.java    # 商品控制器
│       │   │   ├── CartController.java     # 购物车控制器
│       │   │   ├── OrderController.java    # 订单控制器
│       │   │   └── PaymentController.java  # 支付控制器
│       │   ├── service/           # 业务逻辑层
│       │   ├── mapper/            # 数据访问层
│       │   ├── javaBean/          # 实体类
│       │   ├── config/            # 配置类
│       │   └── util/              # 工具类
│       └── resources/
│           ├── templates/         # 前端模板
│           │   ├── admin/         # 管理员页面
│           │   ├── knight/        # 骑士页面
│           │   └── *.html         # 用户页面
│           ├── static/            # 静态资源
│           ├── messages.properties    # 国际化资源
│           └── application.yml    # 应用配置
├── docs/                          # 文档目录
├── knight.sql                     # 数据库初始化脚本
├── Dockerfile                     # Docker构建文件
├── docker-compose.yml             # Docker Compose配置
└── pom.xml                       # Maven配置
```

---

## 🔌 API 接口

### 用户认证

| API | 方法 | 描述 |
|-----|------|------|
| `/user/login` | POST | 用户登录 |
| `/user/register` | POST | 用户注册 |
| `/user/loginout` | GET | 用户退出 |

### 商品管理

| API | 方法 | 描述 |
|-----|------|------|
| `/goods/list` | GET | 获取商品列表 |
| `/goods/detail` | GET | 获取商品详情 |

### 购物车管理

| API | 方法 | 描述 |
|-----|------|------|
| `/cart/add` | POST | 添加购物车 |
| `/cart/list` | GET | 获取购物车列表 |
| `/cart/update` | POST | 更新购物车数量 |
| `/cart/delete` | GET | 删除购物车项 |

### 订单管理

| API | 方法 | 描述 |
|-----|------|------|
| `/order/create` | POST | 创建订单 |
| `/order/list` | GET | 获取订单列表 |
| `/order/detail` | GET | 获取订单详情 |
| `/order/cancel` | GET | 取消订单 |

### 骑士端接口

| API | 方法 | 描述 |
|-----|------|------|
| `/knight/login` | GET | 骑士登录页 |
| `/knight/doLogin` | POST | 骑士登录 |
| `/knight/index` | GET | 骑士首页 |
| `/knight/acceptOrder` | POST | 确认接单 |
| `/knight/pickupOrder` | POST | 确认取货 |
| `/knight/deliverOrder` | POST | 确认送达 |
| `/knight/income` | GET | 收入统计 |

### 管理员接口

| API | 方法 | 描述 |
|-----|------|------|
| `/admin/index` | GET | 管理员首页 |
| `/admin/userList` | GET | 用户列表 |
| `/admin/goodsList` | GET | 商品列表 |
| `/admin/orderList` | GET | 订单列表 |
| `/admin/shipOrder` | POST | 订单发货 |
| `/admin/completeOrder` | POST | 完成订单 |

---

## 🗄️ 数据库设计

### 主要数据表

| 表名 | 说明 |
|------|------|
| `user` | 用户表 |
| `goods` | 商品表 |
| `type` | 分类表 |
| `order` | 订单表 |
| `order_item` | 订单明细表 |
| `cart` | 购物车表 |
| `knight` | 骑士表 |
| `log` | 操作日志表 |

### 订单状态说明

| 状态值 | 状态名称 | 说明 |
|--------|----------|------|
| 0 | 待付款 | 用户未支付 |
| 2 | 待接单 | 用户已支付，等待骑士接单 |
| 3 | 待取货 | 骑士已接单，等待取货 |
| 4 | 配送中 | 骑士已取货，正在配送 |
| 5 | 已完成 | 订单已送达 |

---

## 📦 部署说明

### Docker 部署

```bash
# 构建并启动容器
docker-compose up -d

# 查看容器状态
docker-compose ps

# 查看日志
docker-compose logs -f cake-app
```

### 生产环境部署

1. 打包项目：
```bash
mvn clean package -DskipTests
```

2. 运行：
```bash
java -jar target/cake-management-system.jar
```

---

## 📝 许可证

本项目采用 MIT 许可证，详情请参阅 [LICENSE](LICENSE) 文件。

---

## 🤝 贡献

欢迎提交 Issue 和 Pull Request！

---

**项目版本**：v1.0  
**最后更新**：2026年6月