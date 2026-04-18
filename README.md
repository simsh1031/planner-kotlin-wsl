# Planner - Backend Service

## 1. 프로젝트 개요

**Planner**는 사용자의 일정, 메모, 할 일을 통합적으로 관리할 수 있는 백엔드 REST API 서비스입니다.

사용자는 회원가입 및 인증 후 다음과 같은 기능을 활용할 수 있습니다:
- 📅 **일정 관리**: 시작일, 종료일을 설정하여 일정을 생성하고 관리
- 📝 **메모 관리**: 제목과 내용으로 메모를 작성하고 보관
- 🏷️ **태그 시스템**: 메모에 태그를 추가하여 효율적으로 분류 및 정리
- ✅ **할 일 관리**: 특정 일정에 속한 할 일 항목을 생성하고 완료 상태 관리

JWT 기반의 인증 시스템으로 안전한 사용자 인증을 제공하며, 각 사용자는 자신의 데이터만 접근할 수 있습니다.

---

## 2. 기술 스택

| 구분 | 기술 |
|------|------|
| **언어** | Kotlin 2.2.21 |
| **프레임워크** | Spring Boot 4.0.3 |
| **데이터베이스** | MySQL |
| **ORM** | Spring Data JPA |
| **인증** | JWT (jjwt 0.11.5) |
| **보안** | Spring Security |
| **API 문서화** | SpringDoc OpenAPI 2.8.6 (Swagger UI) |
| **검증** | Spring Validation |
| **빌드 도구** | Gradle |
| **Java 버전** | Java 17 |

---

## 3. 엔티티 관계도

```
User (사용자)
├── 1 : N ──> Memo (메모)
│            └── 1 : N ──> MemoTag (메모-태그 매핑)
│                         └── N : 1 ──> Tag (태그)
│
└── 1 : N ──> Schedule (일정)
             └── 1 : N ──> Todo (할 일)
```

### 엔티티 설명

| 엔티티 | 설명 | 주요 필드 |
|--------|------|---------|
| **User** | 시스템 사용자 | userId, email, password, nickname, role, createdAt |
| **Memo** | 사용자 메모 | memoId, title, content, createdAt, updatedAt, user(FK) |
| **Tag** | 메모 분류용 태그 | tagId, name |
| **MemoTag** | Memo와 Tag의 N:N 관계 매핑 | memoTagId, memo(FK), tag(FK) |
| **Schedule** | 사용자 일정 | scheduleId, title, description, startDate, endDate, createdAt, user(FK) |
| **Todo** | 일정에 속한 할 일 항목 | todoId, content, completed, createdAt, dueDate, schedule(FK) |

---

## 4. API 목록 및 역할

### 4.1 인증 API (`/api/auth`)

| 메서드 | 엔드포인트 | 설명 | 요청 | 응답 |
|--------|----------|------|------|------|
| **POST** | `/login` | 사용자 로그인 | `{ email, password }` | `{ accessToken, tokenType, expiresIn, userId }` |

**역할**: 사용자 이메일과 비밀번호로 인증 후 JWT 토큰을 발급합니다. 발급된 토큰은 이후 모든 API 요청의 Authorization 헤더에 포함되어야 합니다.

---

### 4.2 사용자 API (`/api/users`)

| 메서드 | 엔드포인트 | 설명 | 필요 인증 |
|--------|----------|------|---------|
| **POST** | `/` | 회원가입 | ❌ 불필요 |
| **GET** | `/{userId}` | 사용자 정보 조회 | ❌ 불필요 |
| **PATCH** | `/{userId}` | 사용자 정보 수정 (닉네임, 비밀번호) | ✅ 필요 |
| **DELETE** | `/{userId}` | 회원탈퇴 | ✅ 필요 |

**역할**: 사용자 계정 관리 기능을 제공합니다.
- **회원가입**: 이메일, 비밀번호, 닉네임으로 새로운 계정 생성
- **조회**: 사용자ID로 사용자 정보 조회
- **수정**: 본인의 닉네임, 비밀번호 변경
- **탈퇴**: 본인 계정 삭제 (비밀번호 확인 필수)

---

### 4.3 메모 API (`/api/memos`)

| 메서드 | 엔드포인트 | 설명 | 필요 인증 |
|--------|----------|------|---------|
| **POST** | `/` | 메모 생성 | ✅ 필요 |
| **GET** | `/` | 사용자의 모든 메모 조회 | ✅ 필요 |
| **PUT** | `/{memoId}` | 메모 수정 | ✅ 필요 |
| **DELETE** | `/{memoId}` | 메모 삭제 | ✅ 필요 |

**역할**: 사용자의 메모 관리 기능을 제공합니다.
- **생성**: 제목과 내용으로 새로운 메모 작성
- **조회**: 인증된 사용자의 모든 메모 목록 조회
- **수정**: 기존 메모의 제목 및 내용 변경
- **삭제**: 메모 삭제

---

### 4.4 메모-태그 API (`/api/memos/{memoId}/tags`)

| 메서드 | 엔드포인트 | 설명 | 필요 인증 |
|--------|----------|------|---------|
| **POST** | `/{memoId}/tags` | 메모에 태그 추가 | ✅ 필요 |
| **GET** | `/{memoId}/tags` | 메모의 모든 태그 조회 | ✅ 필요 |
| **DELETE** | `/{memoId}/tags/{tagId}` | 메모에서 태그 제거 | ✅ 필요 |

**역할**: 메모와 태그의 N:N 관계를 관리합니다.
- **추가**: 특정 메모에 태그 연결 (중복 불가)
- **조회**: 특정 메모에 연결된 모든 태그 조회
- **제거**: 메모에서 특정 태그 제거

---

### 4.5 태그 API (`/api/tags`)

| 메서드 | 엔드포인트 | 설명 | 필요 인증 |
|--------|----------|------|---------|
| **POST** | `/` | 태그 생성 | ❌ 불필요 |
| **GET** | `/` | 모든 태그 조회 | ❌ 불필요 |

**역할**: 시스템 전체의 태그를 관리합니다.
- **생성**: 새로운 태그 생성 (태그명 중복 불가)
- **조회**: 시스템에 등록된 모든 태그 조회

---

### 4.6 일정 API (`/api/schedules`)

| 메서드 | 엔드포인트 | 설명 | 필요 인증 |
|--------|----------|------|---------|
| **POST** | `/` | 일정 생성 | ✅ 필요 |
| **GET** | `/` | 사용자의 일정 조회 (필터링 가능) | ✅ 필요 |
| **PUT** | `/{scheduleId}` | 일정 수정 | ✅ 필요 |
| **DELETE** | `/{scheduleId}` | 일정 삭제 | ✅ 필요 |

**역할**: 사용자의 일정을 관리합니다.
- **생성**: 제목, 설명, 시작일, 종료일로 새로운 일정 작성
- **조회**: 
  - 모든 일정 조회
  - 특정 날짜의 일정 조회 (`?date=YYYY-MM-DD`)
  - 기간별 일정 조회 (`?start=YYYY-MM-DD&end=YYYY-MM-DD`)
- **수정**: 기존 일정의 정보 변경
- **삭제**: 일정 삭제

---

### 4.7 할 일 API (`/api/todos`)

| 메서드 | 엔드포인트 | 설명 | 필요 인증 |
|--------|----------|------|---------|
| **POST** | `/` | 할 일 생성 | ❌ 불필요 |
| **GET** | `/` | 사용자의 할 일 조회 (필터링 가능) | ✅ 필요 |
| **PATCH** | `/{todoId}/complete` | 할 일 완료 처리 | ❌ 불필요 |

**역할**: 특정 일정에 속한 할 일 항목을 관리합니다.
- **생성**: 특정 일정(Schedule)에 속한 할 일 작성
- **조회**: 
  - 사용자의 모든 할 일 조회
  - 완료/미완료 상태로 필터링 (`?completed=true/false`)
- **완료 처리**: 할 일의 완료 상태 변경

---

## 5. 사용 예시

### 5.1 회원가입 및 로그인
```bash
# 회원가입
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{
    "email": "user@example.com",
    "password": "password123",
    "nickname": "User"
  }'

# 로그인
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "user@example.com",
    "password": "password123"
  }'
```

### 5.2 일정 생성 및 조회
```bash
# 일정 생성
curl -X POST http://localhost:8080/api/schedules \
  -H "Authorization: Bearer {accessToken}" \
  -H "Content-Type: application/json" \
  -d '{
    "title": "프로젝트 회의",
    "description": "팀 회의",
    "startDate": "2024-04-20T14:00:00",
    "endDate": "2024-04-20T16:00:00"
  }'

# 특정 날짜의 일정 조회
curl -X GET "http://localhost:8080/api/schedules?date=2024-04-20" \
  -H "Authorization: Bearer {accessToken}"
```

### 5.3 메모 및 태그 관리
```bash
# 메모 생성
curl -X POST http://localhost:8080/api/memos \
  -H "Authorization: Bearer {accessToken}" \
  -H "Content-Type: application/json" \
  -d '{
    "title": "개발 노트",
    "content": "Spring Boot 프로젝트 정리"
  }'

# 태그 생성
curl -X POST http://localhost:8080/api/tags \
  -H "Content-Type: application/json" \
  -d '{"name": "개발"}'

# 메모에 태그 추가
curl -X POST http://localhost:8080/api/memos/{memoId}/tags \
  -H "Authorization: Bearer {accessToken}" \
  -H "Content-Type: application/json" \
  -d '{"tagId": 1}'
```

---

## 6. 빌드 및 실행

### 프로젝트 빌드
```bash
./gradlew build
```

### 개발 환경 실행
```bash
./gradlew bootRun
```

### Docker로 실행
```bash
docker build -t planner:latest .
docker run -p 8080:8080 planner:latest
```

---

## 7. 환경 설정

프로젝트 루트의 `.env` 파일 또는 `application.yml` 파일에서 다음 설정을 구성합니다:

```yaml
spring:
  jpa:
    hibernate:
      ddl-auto: update
    database-platform: org.hibernate.dialect.MySQL8Dialect
  datasource:
    url: jdbc:mysql://localhost:3306/planner_db
    username: root
    password: password
```

---

## 8. API 문서

프로젝트 실행 후 다음 URL에서 Swagger UI 기반의 API 문서를 확인할 수 있습니다:
- **Swagger UI**: `http://localhost:8080/swagger-ui.html`
- **OpenAPI JSON**: `http://localhost:8080/v3/api-docs`
