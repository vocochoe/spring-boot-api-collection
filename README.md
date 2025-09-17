# Spring Boot API Collection

Spring Boot 기반으로 다양한 주제의 API 서비스를 연습/구현한 멀티 모듈 프로젝트입니다.  
각 모듈은 독립적으로 실행 가능하며, 공통적으로 **Spring Data JPA**, **H2 Database**, **RESTful API 설계 원칙**을 기반으로 구현되었습니다.

---

## 프로젝트 목록

### 1. [간단한 할 일(Todo) 목록 관리](./todolist)
- 주요 기능
    - Todo CRUD
    - 제목 검색, 완료 여부 필터링
    - 마감일 지난 항목 조회
- 기술 포인트
    - `@PrePersist`, `@PreUpdate`로 자동 시간 관리
    - DTO 분리(`TodoRequest`, `TodoResponse`)
    - MockMvc 기반 컨트롤러 테스트
- 문서
    - ERD, API 명세, 구현 결정, 실행 방법 포함

---

### 2. [병원 진료 예약 서비스(Hospital Booking)](./hospitalbooking)
- 주요 기능
    - 관리자: 의사 등록, 전체 유저 조회
    - 환자: 예약 생성, 예약 취소
    - 의사: 예약 목록 확인
    - 공통: 의사 목록, 예약 가능 시간 조회
- 기술 포인트
    - User–Doctor(1:1), Doctor–Appointment(1:N) 연관관계 매핑
    - 예약 시 미래 시간 & 중복 예약 검증
    - `NotFoundException`, `InvalidAppointmentException` 커스텀 예외 처리
    - 데이터 초기화(`data.sql`) + `.http` 파일로 API 테스트
- 구현 결정
    - 보안(Authentication/Authorization)은 Spring Security 미적용
        - 이유: 간단한 API 학습 프로젝트이므로 권한 검증 로직은 추후 Security 도입 시 구현
        - 현재는 Service 계층에서 역할 기반 시나리오만 단순히 분리

---

## 실행 방법 (공통)

1. 빌드 & 실행
   ```bash
   ./gradlew bootRun
   ```

2. **H2 콘솔 접속**
    - URL: http://localhost:8080/h2-console
    - JDBC URL: `jdbc:h2:mem:testdb`
    - username: `sa`
    - password: (빈 값)

3. **API 테스트**
    - 각 모듈에 포함된 `.http` 파일 실행
    - Postman / IntelliJ HTTP Client 지원

4. **테스트 실행**
   ```bash
   ./gradlew test --info
   ```
    - 실행 완료 후 결과 리포트 확인:
    - `build/reports/tests/test/index.html`