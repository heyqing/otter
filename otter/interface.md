# Otter 社交平台接口文档

## 用户模块

### 1. 用户注册

- **URL**: `/api/users/register`
- **Method**: `POST`
- **Description**: 注册新用户
- **Request Body**:
  ```json
  {
    "username": "string", // 用户名，4-20个字符
    "email": "string",    // 邮箱地址
    "walletAddress": "string", // 钱包地址
    "signature": "string", // 签名
    "nonce": "string"     // 随机字符串
  }
  ```
- **Response**:
  ```json
  {
    "code": 200,
    "message": "success",
    "data": {
      "id": "string",           // 用户ID
      "username": "string",     // 用户名
      "email": "string",        // 邮箱
      "bio": "string",          // 个人简介
      "avatarIpfsHash": "string", // 头像IPFS哈希
      "walletAddress": "string", // 钱包地址
      "isVerified": boolean,    // 邮箱是否已验证
      "roles": ["string"],      // 角色列表
      "createdAt": "string"     // 创建时间
    }
  }
  ```

### 2. 用户登录

- **URL**: `/api/users/login`
- **Method**: `POST`
- **Description**: 用户登录
- **Request Body**:
  ```json
  {
    "walletAddress": "string", // 钱包地址
    "signature": "string",     // 签名
    "nonce": "string"         // 随机字符串
  }
  ```
- **Response**:
  ```json
  {
    "code": 200,
    "message": "success",
    "data": "string" // JWT token
  }
  ```

### 3. 获取当前用户信息

- **URL**: `/api/users/me`
- **Method**: `GET`
- **Description**: 获取当前登录用户信息
- **Headers**:
  ```
  Authorization: Bearer <token>
  ```
- **Response**:
  ```json
  {
    "code": 200,
    "message": "success",
    "data": {
      "id": "string",           // 用户ID
      "username": "string",     // 用户名
      "email": "string",        // 邮箱
      "bio": "string",          // 个人简介
      "avatarIpfsHash": "string", // 头像IPFS哈希
      "walletAddress": "string", // 钱包地址
      "isVerified": boolean,    // 邮箱是否已验证
      "roles": ["string"],      // 角色列表
      "createdAt": "string"     // 创建时间
    }
  }
  ```

### 4. 更新用户信息

- **URL**: `/api/users/me`
- **Method**: `PUT`
- **Description**: 更新当前用户信息
- **Headers**:
  ```
  Authorization: Bearer <token>
  ```
- **Request Body**:
  ```json
  {
    "bio": "string",          // 个人简介
    "avatarIpfsHash": "string" // 头像IPFS哈希
  }
  ```
- **Response**:
  ```json
  {
    "code": 200,
    "message": "success",
    "data": {
      "id": "string",           // 用户ID
      "username": "string",     // 用户名
      "email": "string",        // 邮箱
      "bio": "string",          // 个人简介
      "avatarIpfsHash": "string", // 头像IPFS哈希
      "walletAddress": "string", // 钱包地址
      "isVerified": boolean,    // 邮箱是否已验证
      "roles": ["string"],      // 角色列表
      "createdAt": "string"     // 创建时间
    }
  }
  ```

### 5. 请求密码重置

- **URL**: `/api/users/password/reset-request`
- **Method**: `POST`
- **Description**: 请求重置密码
- **Request Body**:
  ```json
  {
    "email": "string" // 用户邮箱
  }
  ```
- **Response**:
  ```json
  {
    "code": 200,
    "message": "success"
  }
  ```

### 6. 重置密码

- **URL**: `/api/users/password/reset`
- **Method**: `POST`
- **Description**: 重置密码
- **Request Body**:
  ```json
  {
    "email": "string",     // 用户邮箱
    "token": "string",     // 重置令牌
    "newPassword": "string" // 新密码
  }
  ```
- **Response**:
  ```json
  {
    "code": 200,
    "message": "success"
  }
  ```

### 7. 发送邮箱验证邮件

- **URL**: `/api/users/email/verification-request`
- **Method**: `POST`
- **Description**: 发送邮箱验证邮件
- **Request Body**:
  ```json
  {
    "email": "string" // 用户邮箱
  }
  ```
- **Response**:
  ```json
  {
    "code": 200,
    "message": "success"
  }
  ```

### 8. 验证邮箱

- **URL**: `/api/users/email/verify`
- **Method**: `POST`
- **Description**: 验证邮箱
- **Request Body**:
  ```json
  {
    "token": "string" // 验证令牌
  }
  ```
- **Response**:
  ```json
  {
    "code": 200,
    "message": "success"
  }
  ```

### 9. 封禁用户

- **URL**: `/api/users/{userId}/ban`
- **Method**: `POST`
- **Description**: 封禁用户（需要管理员权限）
- **Headers**:
  ```
  Authorization: Bearer <token>
  ```
- **Request Body**:
  ```json
  {
    "reason": "string",   // 封禁原因
    "duration": integer   // 封禁时长（小时）
  }
  ```
- **Response**:
  ```json
  {
    "code": 200,
    "message": "success"
  }
  ```

### 10. 解封用户

- **URL**: `/api/users/{userId}/unban`
- **Method**: `POST`
- **Description**: 解封用户（需要管理员权限）
- **Headers**:
  ```
  Authorization: Bearer <token>
  ```
- **Response**:
  ```json
  {
    "code": 200,
    "message": "success"
  }
  ```

### 11. 导出用户数据

- **URL**: `/api/users/{userId}/export`
- **Method**: `GET`
- **Description**: 导出用户数据
- **Headers**:
  ```
  Authorization: Bearer <token>
  ```
- **Response**:
  ```json
  {
    "code": 200,
    "message": "success",
    "data": "string" // JSON格式的用户数据
  }
  ```

### 12. 获取用户统计信息

- **URL**: `/api/users/{userId}/statistics`
- **Method**: `GET`
- **Description**: 获取用户统计信息
- **Headers**:
  ```
  Authorization: Bearer <token>
  ```
- **Response**:
  ```json
  {
    "code": 200,
    "message": "success",
    "data": {
      "userId": "string",
      "loginCount": integer,
      "postCount": integer,
      "commentCount": integer,
      "followerCount": integer,
      "followingCount": integer,
      "lastLoginAt": "string",
      "lastActivityAt": "string",
      "createdAt": "string"
    }
  }
  ```

### 13. 获取用户操作日志

- **URL**: `/api/users/{userId}/activity-logs`
- **Method**: `GET`
- **Description**: 获取用户操作日志
- **Headers**:
  ```
  Authorization: Bearer <token>
  ```
- **Query Parameters**:
  - `page`: 页码（默认0）
  - `size`: 每页大小（默认20）
- **Response**:
  ```json
  {
    "code": 200,
    "message": "success",
    "data": [
      {
        "id": "string",
        "userId": "string",
        "action": "string",
        "ipAddress": "string",
        "userAgent": "string",
        "details": "string",
        "createdAt": "string"
      }
    ]
  }
  ```

## 错误码说明

- 200: 成功
- 400: 请求参数错误
- 401: 未认证
- 403: 无权限
- 404: 资源不存在
- 500: 服务器内部错误

## 注意事项

1. 所有请求和响应均使用 JSON 格式
2. 需要认证的接口需要在请求头中携带 JWT token
3. 时间格式统一使用 ISO 8601 标准
4. 文件上传使用 IPFS 存储，返回 IPFS 哈希值
5. 用户操作日志会记录所有重要操作
6. 邮箱验证和密码重置令牌有效期为24小时
7. 用户封禁时长单位为小时，0表示永久封禁 