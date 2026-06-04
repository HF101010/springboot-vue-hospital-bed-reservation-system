# 医院床位预约管理系统项目总结

本文档基于前后端源码整理，用于说明项目技术栈、功能模块、数据库设计思路、前后端代码位置以及核心功能实现方式。

当前总结对应两个子项目：

- `HospitalManagement.springboot`：后端项目
- `HospitalManagement.elementui`：前端项目

项目定位：

**一个基于 Spring Boot + Vue 的医院床位预约管理系统，支持管理员后台管理与普通用户前台预约。**

---

## 1. 使用了什么技术

### 1.1 后端技术栈

后端配置文件位于：

- `HospitalManagement.springboot/pom.xml`

后端主要使用了以下技术：

- Java 17
- Spring Boot 3
- Spring Web
- Spring JDBC
- MyBatis-Plus
- MySQL
- Redis
- JWT
- Maven
- Lombok
- Apache POI
- Commons BeanUtils

后端代码采用典型分层结构：

- `controller`：接口控制层
- `service / service.impl`：业务逻辑层
- `mapper`：数据访问层
- `entity`：数据库实体
- `dto`：数据传输对象
- `tools`：工具类、JWT、Redis 锁、拦截器、统一响应等

### 1.2 前端技术栈

前端配置文件位于：

- `HospitalManagement.elementui/package.json`

前端主要使用了以下技术：

- Vue 3
- Vite
- Vue Router
- Pinia
- Pinia 持久化插件
- Element Plus
- Axios
- Sass
- NProgress
- ECharts

前端结构主要包括：

- `views/Admin`：管理员后台页面
- `views/Front`：普通用户前台页面
- `router/index.js`：前端路由
- `store/index.js`：状态管理
- `api/http.js`：接口封装
- `components/Tables/PaginationTable.vue`：通用分页表格组件
- `utils/department.js` / `utils/status.js`：展示映射工具

### 1.3 系统运行环境

推荐环境如下：

- JDK 17
- Node.js 18+
- MySQL 8.x
- Redis 6.x / 7.x
- Maven 3.8+

数据库连接主配置位于：

- `HospitalManagement.springboot/src/main/resources/application.yml`

---

## 2. 毕设代码有什么功能

系统按使用角色分为两大部分：

- 管理员后台
- 普通用户前台

### 2.1 管理员后台功能

管理员端主要功能包括：

1. 账号管理
- 后台账号的增删改查
- 登录、找回密码、修改密码、重置密码

2. 用户信息管理
- 管理普通用户资料

3. 病区管理
- 管理病区名称、所属科室、总床位数、可用床位数

4. 床位管理
- 管理床位编号、所属病区、状态、备注

5. 床位预约管理
- 查看预约记录
- 审核预约申请

6. 床位池查看
- 按科室查看病区与床位聚合结果
- 查看床位统计与明细

7. 出入院登记
- 管理入院、出院记录

8. 系统通知管理
- 查看通知记录
- 管理通知状态

9. 权限管理
- 管理权限项

10. 角色权限关联管理
- 管理角色与权限关系

### 2.2 普通用户前台功能

普通用户端主要功能包括：

1. 注册与登录
2. 个人中心
3. 修改个人资料
4. 修改密码
5. 提交床位预约
6. 查看预约记录
7. 查看系统通知
8. 自动分配床位

### 2.3 通用功能

系统还包含以下通用能力：

- 文件上传
- Excel 导出
- JWT 登录鉴权
- Redis 分布式锁
- 自动分配审计记录

---

## 3. 数据库有哪些表，以及对应关系

虽然 `框架` 目录当前不再包含完整 SQL 初始化脚本，但从后端实体和项目设计可以整理出数据库结构。

### 3.1 核心表

#### 1. `user`
用户表，用于保存管理员和普通用户。

关键字段：

- `id`
- `username`
- `password`
- `real_name`
- `phone`
- `email`
- `birth`
- `role_type`
- `role`
- `status`

#### 2. `ward`
病区表。

关键字段：

- `id`
- `ward_name`
- `department`
- `total_beds`
- `available_beds`
- `remark`

#### 3. `bed`
床位表。

关键字段：

- `id`
- `bed_number`
- `ward_id`
- `status`
- `remark`
- `specialties`
- `priority`
- `next_available_time`

#### 4. `bed_reservation`
床位预约表。

关键字段：

- `id`
- `patient_id`
- `ward_id`
- `bed_id`
- `reservation_time`
- `status`
- `reason`
- `audit_user_id`
- `audit_time`
- `allocation_mode`
- `priority`

#### 5. `admission`
出入院登记表。

关键字段：

- `id`
- `patient_id`
- `bed_id`
- `admission_date`
- `discharge_date`
- `status`
- `remark`

#### 6. `notification`
系统通知表。

关键字段：

- `id`
- `title`
- `content`
- `sender_id`
- `receiver_id`
- `create_time`
- `is_read`

#### 7. `permission`
权限表。

#### 8. `role_permission`
角色权限关联表。

#### 9. `allocation_audit`
自动分配审计表。

关键字段：

- `id`
- `reservation_id`
- `bed_id`
- `algorithm`
- `score`
- `reason`
- `created_at`

### 3.2 扩展表

项目中还设计了以下扩展表：

- `bed_pools`
- `bed_pool_members`
- `reservation_waitlist`

这些表体现了床位池和候补机制的扩展设计思路。

### 3.3 主要关系

1. `ward` 1 --- n `bed`
- 一个病区有多张床位

2. `user` 1 --- n `bed_reservation`
- 一个用户可以发起多条预约

3. `ward` 1 --- n `bed_reservation`
- 一个病区可对应多条预约

4. `bed` 1 --- n `bed_reservation`
- 一张床位可对应多条历史预约

5. `user` 1 --- n `admission`
- 一个患者可对应多条住院记录

6. `bed` 1 --- n `admission`
- 一张床位可对应多条出入院记录

7. `user` 1 --- n `notification`
- 作为发送者或接收者

8. `permission` 1 --- n `role_permission`

9. `bed_reservation` 1 --- n `allocation_audit`
- 一条预约可以对应多条分配审计记录

10. `bed` 1 --- n `allocation_audit`
- 一张床位可出现在多条历史审计记录中

---

## 4. 功能对应的前后端代码在哪里

### 4.1 登录、注册、鉴权

前端：

- `HospitalManagement.elementui/src/views/Login.vue`
- `HospitalManagement.elementui/src/views/Register.vue`
- `HospitalManagement.elementui/src/views/ForgetPassword.vue`
- `HospitalManagement.elementui/src/store/index.js`
- `HospitalManagement.elementui/src/router/index.js`

后端：

- `HospitalManagement.springboot/src/main/java/com/example/web/controller/AppUserController.java`
- `HospitalManagement.springboot/src/main/java/com/example/web/service/impl/AppUserServiceImpl.java`
- `HospitalManagement.springboot/src/main/java/com/example/web/tools/JWTUtils.java`

### 4.2 用户信息管理

前端：

- `HospitalManagement.elementui/src/views/Admin/UserList.vue`
- `HospitalManagement.elementui/src/views/Front/Layout/UserCenter.vue`
- `HospitalManagement.elementui/src/views/Front/UserInfoEdit.vue`

后端：

- `HospitalManagement.springboot/src/main/java/com/example/web/controller/userController.java`
- `HospitalManagement.springboot/src/main/java/com/example/web/service/impl/userServiceImpl.java`

### 4.3 病区管理

前端：

- `HospitalManagement.elementui/src/views/Admin/wardList.vue`

后端：

- `HospitalManagement.springboot/src/main/java/com/example/web/controller/wardController.java`
- `HospitalManagement.springboot/src/main/java/com/example/web/service/impl/wardServiceImpl.java`

### 4.4 床位管理

前端：

- `HospitalManagement.elementui/src/views/Admin/bedList.vue`

后端：

- `HospitalManagement.springboot/src/main/java/com/example/web/controller/bedController.java`
- `HospitalManagement.springboot/src/main/java/com/example/web/service/impl/bedServiceImpl.java`

### 4.5 床位预约

前端：

- 管理端：`HospitalManagement.elementui/src/views/Admin/bed_reservationList.vue`
- 用户端：`HospitalManagement.elementui/src/views/Front/Reservation.vue`

后端：

- `HospitalManagement.springboot/src/main/java/com/example/web/controller/bed_reservationController.java`
- `HospitalManagement.springboot/src/main/java/com/example/web/service/impl/bed_reservationServiceImpl.java`

### 4.6 床位池查看

前端：

- `HospitalManagement.elementui/src/views/Admin/bedPoolList.vue`
- `HospitalManagement.elementui/src/utils/department.js`
- `HospitalManagement.elementui/src/utils/status.js`

后端：

- `HospitalManagement.springboot/src/main/java/com/example/web/controller/wardController.java`
- `HospitalManagement.springboot/src/main/java/com/example/web/service/impl/wardServiceImpl.java`
- `HospitalManagement.springboot/src/main/java/com/example/web/dto/BedPoolDto.java`

### 4.7 出入院登记

前端：

- `HospitalManagement.elementui/src/views/Admin/admissionList.vue`

后端：

- `HospitalManagement.springboot/src/main/java/com/example/web/controller/admissionController.java`
- `HospitalManagement.springboot/src/main/java/com/example/web/service/impl/admissionServiceImpl.java`

### 4.8 系统通知

前端：

- 管理端：`HospitalManagement.elementui/src/views/Admin/notificationList.vue`
- 用户端：`HospitalManagement.elementui/src/views/Front/Notification.vue`

后端：

- `HospitalManagement.springboot/src/main/java/com/example/web/controller/notificationController.java`
- `HospitalManagement.springboot/src/main/java/com/example/web/service/impl/notificationServiceImpl.java`

### 4.9 权限管理

前端：

- `HospitalManagement.elementui/src/views/Admin/permissionList.vue`
- `HospitalManagement.elementui/src/views/Admin/role_permissionList.vue`

后端：

- `HospitalManagement.springboot/src/main/java/com/example/web/controller/permissionController.java`
- `HospitalManagement.springboot/src/main/java/com/example/web/service/impl/permissionServiceImpl.java`
- `HospitalManagement.springboot/src/main/java/com/example/web/controller/role_permissionController.java`
- `HospitalManagement.springboot/src/main/java/com/example/web/service/impl/role_permissionServiceImpl.java`

---

## 5. 功能是怎么实现的

### 5.1 登录与鉴权

系统采用 JWT 进行登录鉴权。

实现流程：

1. 前端登录页提交账号和密码
2. 后端根据用户名和加密后的密码查询用户
3. 登录成功后生成 JWT Token
4. 前端将 Token 持久化到本地
5. 路由守卫根据 Token 和用户角色跳转到管理员端或用户端

### 5.2 通用 CRUD 实现方式

项目大部分基础管理模块都采用统一的 CRUD 模式实现：

1. 前端页面通过 `PaginationTable.vue` 调用分页接口
2. 后端 Controller 提供：
- `/List`
- `/Get`
- `/CreateOrEdit`
- `/Delete`
- `/BatchDelete`
3. Service 层用 MyBatis-Plus 进行条件构建、分页查询、保存更新和删除

这套模式被广泛用于：

- 用户管理
- 病区管理
- 床位管理
- 通知管理
- 权限管理
- 出入院登记

### 5.3 病区与床位管理实现方式

1. `ward` 表管理病区
2. `bed` 表管理床位
3. 床位通过 `ward_id` 关联病区
4. 前端通过病区下拉框进行选择和管理
5. 后端通过 `wardMapper` 和 `bedMapper` 完成数据操作

### 5.4 床位预约实现方式

用户端预约页面位于：

- `HospitalManagement.elementui/src/views/Front/Reservation.vue`

后端核心逻辑位于：

- `HospitalManagement.springboot/src/main/java/com/example/web/service/impl/bed_reservationServiceImpl.java`

实现流程：

1. 用户填写预约信息：
- 科室
- 可选病区
- 可选床位
- 预约时间
- 原因

2. 如果只选科室，不选病区和床位：
- 系统进入自动分配模式

3. 自动分配时：
- 先按科室查病区
- 再在这些病区下查可用床位
- 使用 Redis 分布式锁避免并发冲突
- 自动选取一张可用床位

4. 保存预约记录后：
- 状态设为待审核
- 给管理员发送通知

5. 管理员审核后：
- 通过：预约状态变为已通过，床位状态变为已占用
- 拒绝：预约状态变为已拒绝

### 5.5 床位池实现方式

当前项目中的床位池采用“按科室聚合病区和床位”的逻辑实现方式。

实现步骤：

1. 管理员在前端选择一个科室
2. 前端调用 `/ward/GetBedPool`
3. 后端按 `department` 查询该科室下所有病区
4. 再查询这些病区下的全部床位
5. 使用 `StatusUtil` 统一床位状态
6. 汇总统计：
- 总床位数
- 可用床位数
- 已占用床位数
- 维修中床位数
7. 封装为 `BedPoolDto` 返回前端
8. 前端以“统计卡片 + 病区明细 + 床位列表”的形式展示

### 5.6 自动分配与床位池结合

床位池不仅用于展示，也为自动预约分配提供基础。

当用户只选科室时：

1. 系统在该科室范围内查找所有病区
2. 再查可用床位
3. 自动分配床位
4. 写入预约记录
5. 写入 `allocation_audit` 审计记录

### 5.7 分配审计实现方式

自动分配成功后，系统会向 `allocation_audit` 表写入一条记录，记录：

- 预约 ID
- 床位 ID
- 算法标识
- 分配原因
- 创建时间

这使得自动分配过程具备可追踪性。

### 5.8 通知功能实现方式

通知功能主要由预约业务触发：

1. 用户提交预约后，通知管理员
2. 管理员审核通过后，通知用户
3. 管理员审核拒绝后，通知用户

通知数据保存在 `notification` 表中，前后端均可查看。

### 5.9 出入院登记实现方式

管理员通过后台录入：

- 患者
- 床位
- 入院时间
- 出院时间
- 状态
- 备注

数据保存到 `admission` 表中，用于辅助住院流程管理和历史记录展示。

---

## 6. 项目特点总结

### 6.1 优点

- 前后端分层清晰
- 管理端和用户端角色分离
- CRUD 结构统一
- 已接入 JWT 鉴权
- 已接入 Redis 分布式锁
- 已实现床位池与自动分配功能
- 已支持分配审计

### 6.2 当前实现特点

- 床位池是“按科室聚合病区和床位”的逻辑池
- 不是完全由 `bed_pools` / `bed_pool_members` 驱动的实体池
- 自动分配已经可运行，但仍属于基础规则分配

### 6.3 项目适合作为毕设的原因

该项目不仅是一个常规管理系统，还具备：

- 登录鉴权
- 权限分离
- 分布式锁
- 资源聚合展示
- 自动分配
- 审计记录

因此既有基础管理系统的完整性，也具备一定业务流程处理与资源调度能力。

---

## 7. 一句话总结

本系统采用 Spring Boot + Vue3 前后端分离架构，以 MySQL 作为核心数据存储，Redis 用于并发锁控制，围绕医院床位资源管理与预约业务构建了用户、病区、床位、预约、出入院、通知、权限等模块，并在此基础上实现了面向科室维度的床位池聚合展示与自动床位分配功能。
