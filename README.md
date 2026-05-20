# 基于 Spring Boot 的学生信息管理系统

[![CI](https://github.com/qijiejin25-design/student-info-system/actions/workflows/ci.yml/badge.svg)](https://github.com/qijiejin25-design/student-info-system/actions/workflows/ci.yml)
![Java](https://img.shields.io/badge/Java-21-orange)
![Spring Boot](https://img.shields.io/badge/SpringBoot-3.3-brightgreen)
![MyBatis](https://img.shields.io/badge/MyBatis-3-blue)

一个基于 **Spring Boot 3 + MyBatis** 的学生信息后台管理系统，实现增删改查、条件检索、分页、登录权限、Excel 导入导出、参数校验、全局异常处理与缓存优化。

> 默认使用 **H2 内嵌数据库 + Caffeine 内存缓存**，clone 下来 `mvn spring-boot:run` 即可启动，无需安装 MySQL 与 Redis。
> 生产环境切换 MySQL + Redis 的配置见下方"切换到 MySQL/Redis"小节。

## 功能特性

- 学生信息 CRUD + 多条件检索（姓名 / 学号 / 班级 / 性别）
- 分页查询
- 简单的登录权限控制（Session 拦截器）
- **Excel 导入**：批量上传学生数据，参数校验后入库
- **Excel 导出**：当前查询条件导出 `.xlsx`
- 全局参数校验（`@Valid` + Bean Validation）
- 全局异常处理（统一 `ApiResponse` 返回结构）
- 高频查询缓存（`@Cacheable` + Caffeine）

## 技术栈

| 类别 | 技术 |
|------|------|
| 框架 | Spring Boot 3.3 |
| 持久层 | MyBatis 3 |
| 默认数据库 | H2（运行时初始化 `schema.sql` + `data.sql`） |
| 可选数据库 | MySQL 8（启用 `mysql` profile） |
| 缓存 | Caffeine（生产可换 Redis） |
| Excel | Apache POI 5 |
| 校验 | Jakarta Bean Validation |
| 工具 | Lombok |
| 构建 | Maven |

## 目录结构

```
student-info-system/
├── pom.xml
└── src/main/
    ├── java/com/qijiejin/studentinfo/
    │   ├── StudentInfoApplication.java
    │   ├── config/         # 缓存、Mybatis、拦截器注册
    │   ├── controller/     # REST 接口
    │   ├── service/        # 业务逻辑
    │   ├── mapper/         # MyBatis Mapper 接口
    │   ├── entity/         # 数据库实体
    │   ├── dto/            # 请求/响应对象
    │   ├── interceptor/    # 登录拦截器
    │   └── exception/      # 全局异常处理
    └── resources/
        ├── application.yml
        ├── application-mysql.yml
        ├── schema.sql       # 表结构
        ├── data.sql         # 初始化数据（默认账号 admin / admin123）
        └── mapper/          # MyBatis XML
```

## 快速开始

```bash
# 编译并启动
mvn spring-boot:run
```

默认监听 `http://localhost:8080`。

### 默认账号

| 用户名 | 密码 |
|--------|------|
| admin | admin123 |

## 主要 API

> 除 `/api/auth/login` 外，所有 `/api/**` 接口需先登录。

| 方法 | 路径 | 说明 |
|------|------|------|
| POST | `/api/auth/login` | 登录，返回 sessionId |
| POST | `/api/auth/logout` | 退出 |
| GET | `/api/students?page=1&size=10&name=&studentNo=&className=&gender=` | 分页 + 条件查询 |
| GET | `/api/students/{id}` | 查询详情（带缓存） |
| POST | `/api/students` | 新增 |
| PUT | `/api/students/{id}` | 修改 |
| DELETE | `/api/students/{id}` | 删除 |
| GET | `/api/students/export` | 导出当前条件结果为 Excel |
| POST | `/api/students/import` | 上传 Excel 批量导入 |
| GET | `/h2-console` | H2 数据库控制台（开发环境，账号 sa） |

### 请求示例

登录：
```bash
curl -i -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123"}'
```

带 cookie 查询：
```bash
curl -b cookies.txt -X POST http://localhost:8080/api/auth/login -d '...' -c cookies.txt
curl -b cookies.txt "http://localhost:8080/api/students?page=1&size=5&className=计算机1班"
```

## 缓存策略

- `studentService.getById` 走 Caffeine 缓存（最大 1000 条，10 分钟过期）
- 增删改时清除对应缓存项
- 替换 Redis 见 `config/CacheConfig.java` 中的注释

## 切换到 MySQL / Redis

激活 mysql profile：
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=mysql
```

修改 `src/main/resources/application-mysql.yml` 中的 host / 账号密码。MySQL 需手动执行 `schema.sql`。

替换 Caffeine 为 Redis 的最小改动：
1. `pom.xml` 加入 `spring-boot-starter-data-redis`
2. `CacheConfig.java` 用 `RedisCacheManager` 替换 `CaffeineCacheManager`
3. `application.yml` 配置 `spring.data.redis.host`

## 测试

```bash
mvn test
```

包含 Mapper 层（基于 H2）的若干单元测试。
