# Notice-Application

- 공지사항 웹 애플리케이션

## 설명

- 요구사항에 사용자라고 적혀있지만 일반적인 공지사항에서는 관리자의 권한을 가진 사람만 글을 작성, 수정, 삭제를 할 수 있다고 생각하여 개발을 진행하였습니다.
- 일반적인 사용자도 볼 수 있기 때문에 전체가 볼 수 있도록 했습니다.
- 공지라고 하는 것이 글을 나타내는 것인지 옵션을 나타내는 것인지 모호하여 게시물로 판단하고 개발을 진행하였습니다.

## 프로젝트 빌드

```java
git clone https://github.com/sujl95/Notice-Application.git

cd (clone경로) 
gradle build
java -jar -Dfile.encoding="UTF-8" build/libs/notice-application-0.0.1-SNAPSHOT.jar
```

## 실행 방법

---

### 사용법

- [http://localhost:8080/notices](http://localhost:8080/notices) 접속
1. 회원가입
    1. 일반 회원가입은 권한(유저, 관리자) 설정가능
        1. 아이디 중복 체크
        2. id : 소문자, 대문자, 숫자 조합(4~20자리)
        3. pw : 8~20자리
        4. email, 역할 지정
    2. 소셜 로그인(OAuth)은 권한(유저)
2. 로그인(관리자 권한)
3. 공지 목록(페이징 기능, 공지 등록 가능)
4. 공지 등록 (유저 권한 X, 관리자 권한만 가능)
    1. 제목, 내용 필수, 파일 필수 X ( 다중 파일 첨부 가능)
    2. 등록
5. 공지 등록된 글 상세보기
    1. 파일 첨부 시 다운로드가능
    2. 수정, 삭제 가능
    3. 수정
        1. 제목, 내용 수정
    4. 삭제

### API 명세서 (Swagger)

- [http://localhost:8080/swagger-ui-custom.html](http://localhost:8080/swagger-ui-custom.html)

### 실행

---

### 실행 환경

```java
- Window 10 
- gradle 6.8.3
- java 11
- spring boot 2.4.5
- h2 database
```

## 기술 스택

- Spring Boot
- Spring Security
- Spring Data JPA
- H2
- Thymeleaf
- JUnit5
- Mockito
- Swagger
- OAuth2.0