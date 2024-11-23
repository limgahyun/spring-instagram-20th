# spring-instagram-20th
CEOS 20th BE study - instagram clone coding

# DB Modeling
인스타그램 전체를 클론 코딩하기 보다는 기능을 최대한 축소시키고자 먼저 기능 정리를 하였다.

> 1. 회원가입
>   - 회원가입을 할 때, user의 email, 실명, 닉네임, 비밀번호, 전화번호 정보를 입력받는다
>   - 전화번호를 제외한 모든 값은 필수로 입력받는다.
>   - 회원정보를 변경하는 경우, 비민번호 인증 후에 email을 제외한 정보를 수정할 수 있다.
>   - 로그아웃 후 로그인을 할 때에는 (email, 비밀번호) 또는 (닉네임, 비밀번호) 또는 (전화번호, 비밀번호)를 입력받는다.
>   - 회원 탈퇴 시 비밀번호 인증 후에 탈퇴할 수 있다.
>   - 기존 인스타그램과 다르게 계정 공개 상태를 결정하지 않고, 모두 ‘전체 공개’ 상태로 한다.
> 2. 팔로우
>   - 회원은 다른 회원은 팔로우할 수 있다.
>   - 모두 전체 공개 상태이므로 팔로우는 요청 즉시 수락된다.
> 3. 게시글
>   - 게시글은 내용, 사진을 포함하여 작성할 수 있다.
>   - 내용은 필수가 아니며, 사진은 필수로 포함해야한다. 
>   - 게시글에 좋아요를 등록할 수 있으며, 이때 좋아요를 누른 회원의 정보가 공개될 수 있다. 
>   - 게시글에 댓글을 등록할 수 있다. 
>   - 게시글에 댓글을 등록할 때, 이미 존재하는 댓글에 대댓글을 등록할 수 있다. 
>   - 게시글은 글을 작성한 회원만 수정, 삭제할 수 있다. 
>   - 댓글은 해당 댓글을 작성한 회원만 수정, 삭제할 수 있다. 
>   - 게시글 또는 댓글을 수정하면 수정한 일시로 일시정보가 업데이트된다. 
>   - 게시글을 작성한 회원이 탈퇴하는 경우 게시글도 자동으로 삭제된다. 
>   - 게시글을 삭제하면 해당 게시글에 있던 좋아요, 댓글 정보도 자동으로 삭제된다.
> 4. DM
>   - 회원은 다른 회원과 1:1 채팅룸을 생성하여 채팅할 수 있다.
>   - 채팅으로는 한번에 텍스트, 이미지, 게시글 공유 중 1가지를 전송할 수 있다.
>   - 채팅은 전송 후 수정, 삭제가 불가능하다.
>   - 채팅은 이전 메세지를 멘션한 후 내용을 작성하여 전송할 수 있다.
>   - 채팅한 상대회원이 탈퇴하더라도 채팅기록은 유지된다.

![img.png](img.png)

## POST
`BaseEntity` : `Post`와 `PostComment` Entity에서 `createdAt`, `updatedAt` 속성을 공통적으로 사용하고 있어 `BaseEntity`로 분리하여 구현하였다.

`PostComment` : `PostComment`는 자기 자신을 참조하여 `parent_id`를 foriegn key로 갖는다. 이 값이 NULL인 경우 댓글, 값이 존재하는 경우 대댓글이다.

## MESSAGE
`Message` : Message의 Type이 text, image, post 세가지로 분류되므로, 다음과 같은 상속관계를 갖는다
![img_1.png](img_1.png)
이때 각각 테이블을 생성하여 저장공간을 효율적으로 사용하기 위하여 조인 전략을 선택하였고, `@Inheritance(strategy=InheritanceType.JOINED)` annotation을 사용하였다.
메세지 조회 기능을 사용할 일이 없다고 예상되어 조인 전략을 선택하였으나 데이터를 저장하는 데에도 성능이 저하될 가능성이 있을지 우려된다. `JOINED`, `SINGLE_TABLE` 둘 중 어느 것이 더 적절할지 고민할 필요가 있다.

`ChattingRoom` : 채팅을 하던 상대방이 인스타그램을 탈퇴하더라도 채팅 기록은 남아있도록 하기 위하여 `ChattingRoom`을 분리하여 구현하였다.

---
# JPA 심화
## 주요 기능
- 회원 가입
- 회원정보 변경
- 로그인
- 회원 탈퇴
- 팔로우
- 게시글 작성
- 게시글 좋아요 등록
- 게시글 댓글 등록
- 게시글, 댓글 수정
- 게시글 삭제
- DM 전송

## UserService
회원가입을 할 때, `nickname`과, `email` 중복을 방지하기 위해 `checkNicknameDuplication`, `checkEmailDuplication`을 사용하였습니다.
```java
@Transactional(readOnly = true)
public void checkNicknameDuplication(String nickname) {
    boolean nicknameDuplicate = userRepository.existsByNickname(nickname);
    if (nicknameDuplicate) {
        throw new IllegalStateException("Nickname already exists");
    }
}
```
```java
@Transactional
public void joinUser(UserJoinRequestDto userDto) {
    checkEmailDuplication(userDto.email());
    checkNicknameDuplication(userDto.nickname());

    User user = userDto.toEntity();
    userRepository.save(user);
}
```
## UserServiceTest
`nickname`에 대한 중복제한이 잘 구현이 되었는지 테스트하기 위하여 `nickname`이 중복인 경우와 아닌 경우를 구분하여 테스트를 진행하였습니다.
```java
// nickname이 중복인 경우 회원가입 테스트
@Test
void joinSameUserTest() {
    //given
    final UserJoinRequestDto request = new UserJoinRequestDto("test", "nickname", "1234", "test@test.com", "01012345678");
    when(userRepository.existsByNickname(request.nickname())).thenReturn(true); // exsitsByNickname = true 반환
    when(userRepository.existsByEmail(request.email())).thenReturn(false);

    //when
    IllegalStateException exception = assertThrows(
        IllegalStateException.class,
        () -> userService.joinUser(request)
    );

    //then
    System.out.println("Test Result: " + exception.getMessage());

    assertEquals("Nickname already exists", exception.getMessage());
    verify(userRepository).existsByNickname(request.nickname());
    verify(userRepository, never()).save(any(User.class));
}
```
## N+1 문제
`jpql` + `@EntityGraph` 혼합 방법을 사용하여 N+1문제를 해결하였습니다.
```java
@Test
@EntityGraph(attributePaths = {"user"})
@Query("select p from Post p")
public void findAllEntityGraph () {
    ...
}
```

# CRUD API
```java
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class PostController {
    private final PostService postService;
    
    @PostMapping
    public ResponseEntity<Void> createPost(@RequestBody final PostCreatRequestDto requestDto, final String username) {
        postService.createPost(requestDto, username);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public ResponseEntity<List<PostListResponseDto>> getAllPosts() {
        final List<PostListResponseDto> posts = postService.getAllPosts();
        return ResponseEntity.ok().body(posts);
    }
}
```


# Global Exception
1. `exception code`, `success code` 정의
    ```java
    BAD_REQUEST_ERROR(HttpStatus.BAD_REQUEST, "Bad Request Exception"),
    NOT_FOUND_POST(HttpStatus.NOT_FOUND, "게시글이 존재하지 않습니다"),
    ```
    ```java
    SELECT_SUCCESS(200, "200", "SELECT SUCCESS"),
    DELETE_SUCCESS(200, "200", "DELETE SUCCESS"),
    INSERT_SUCCESS(201, "201", "INSERT SUCCESS"),
    UPDATE_SUCCESS(204, "204", "UPDATE SUCCESS");
    ```
2. service 예외 처리
    ```java
    public PostResponseDto getPost(final Long postId) {
        final Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException(ExceptionCode.NOT_FOUND_POST.getMessage()));
        final List<PostComment> commentList = commentRepository.findByPost(post);
    
        return PostResponseDto.of(post, commentList);
    }
    ```
   게시글을 조회했을 때 해당 게시글이 존재하지 않을 때 `NotFoundException`을 통해 설정해둔 `NOT_FOUND_POST`에 대한 exception message를 표시한다.
# API 명세서

## 개발 전 명세서 ?

DTO를 생성할 때에도, CRUD api 를 생성하려고 할 때에도, 기능들에 대한 정리가 안되어있다보니 어디부터 손대할지 막막하고 개발 도주에 수정할 것들이 많아져서 뒤죽박죽이 되는 느낌이었다. 이를 해소하고자 API 명세서를 작성하는 방법부터 찾아보았는데 우선 내가 개발하는 데에 쓰는 용도로 notion을 이용해서 제작하였다.

다음과 같은 형식으로 기능, http method, api path, token, 각 기능에 필요한 DTO를 정리하려고 하였다.

![image](https://github.com/user-attachments/assets/df0c130d-f8cf-48fd-8aa6-511e287f6bb8)

## 협업 때 사용할 명세서

노션은 내가 보려고 한거고.. 실제로 노션을 쓰면 실제 코드와 동기화가 안되기 때문에 비효율적 + 에러핸들링 어려움의 문제가 있을 것이라고 생각하였다.

코드 기반으로 api 명세서를 생성하는 swagger, spring REST docs 에 대해 찾아보았다

1. swagger
- 장점 : swagger-ui 문서에서 api test 가능
- 단점 : 코드에 어노테이션을 추가해야 하므로 서비스코드와 api 명세서 관리가 혼합된다
2. spring REST docs
- 장점 : 코드에 영향 X
- 단점 : 테스트 코드를 기반으로 생성되므로 모든 테스트 코드를 작성해야한다

프론트 개발을 할 때 swagger-ui를 사용해보았는데 ui가 그리 보기 편하진 않았어서.. REST docs를 사용하고 싶었지만..! 테스트를 만들어서 실행하는게 익숙하지 않기 때문에 처음에는 swagger를 시도해보기로 결정하였다.

## swagger-ui
```java
@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "API Test",
                description = "Instagram clone coding API 명세서",
                version = "v1"
        ),
        servers = {@Server(url = "http://localhost:8080", description = "local server")}
)
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .components(new Components());
    }
}
```
SwaggerConfig 파일에서 로컬 서버로 url 설정을 해둔 후, `@Tag`, `@Operation` 등의 어노테이션을 사용하여 각 api 동작이 swagger-ui와 연동되도록 한다.

<img width="1512" alt="image" src="https://github.com/user-attachments/assets/d3a57457-8df8-4f1b-bf38-f4c6c0a2d824">

# Controller 통합 테스트
에러 발생 시 설정해둔 exception status, message가 표시되는 것을 확인하였다.

<img width="1427" alt="image" src="https://github.com/user-attachments/assets/18d6c70d-f3f1-4ce1-94b2-e2c6cff061ad">

# JWT 인증(Authentication) 방법
## Cookie
- 보안이 좋지 않음

<img width="742" alt="image" src="https://github.com/user-attachments/assets/93cb990b-d60b-4f10-9638-a39af23a07b0">

## Cookie + Session
> cookie : 브라우저에 저장

> session : 서버에 저장
- cookie만 사용하는 것에 비해 보안 유지가 가능함
  - session id가 탈취되더라도 session data를 전부 지워버리는 방법으로 대처 가능
- 요청을 보낼 때마다 session id를 조회해야함
- 사용자가 많아질수록 메모리를 차지함
- stateful -> scale out이 번거로움

<img width="752" alt="image" src="https://github.com/user-attachments/assets/3d0d0b84-d256-409b-9c80-7d6b59255367">

❗️cookie, session의 단점을 보완하는 방법 → JWT !!

## JWT (Access Token + Refresh Token)
인증에 필요한 정보들을 Token에 담아 암호화시켜 사용하는 방식

**서명된 토큰 → stateless**

구성요소
1. Header
   ```json
   {
     "typ": "JWT",
     "alg": "HS512"
   }
   ```
2. Payload
   ```json
   {
     "sub": "1",
     "iss": "ori",
     "exp": 1636989718,
     "iat": 1636987918
   }
   ```
   - iss (Issuer) : 토큰 발급자
   - sub (Subject) : 토큰 제목 - 토큰에서 사용자에 대한 식별값이 됨
   - aud (Audience) : 토큰 대상자
   - exp (Expiration Time) : 토큰 만료 시간
   - nbf (Not Before) : 토큰 활성 날짜 (이 날짜 이전의 토큰은 활성화 되지 않음을 보장)
   - iat (Issued At) : 토큰 발급 시간
   - jti (JWT Id) : JWT 토큰 식별자 (issuer가 여러명일 때 이를 구분하기 위한 값)
   - 식별을 위해 필요한 정보만 담고, 민감한 정보들을 담지 않도록 주의

3. Signature

   header를 디코딩한 값, payload를 디코딩한 값을 합치고 이를 서버가 가지고 있는 개인키(your-256-bit-secret)를 가지고 암호화 되어있는 상태

**jwt는 cookie와 session의 단점 보완**
- 이미 토큰 자체가 인증된 정보이기 때문에 세션 저장소와 같은 별도의 인증 저장소가 필수적으로 필요하지 않음
- 세션과는 다르게 클라이언트의 상태를 서버가 저장하지 않아도됨
- signature를 공통키 개인키 암호화를 통해 막아두었기 때문에 데이터에 대한 보완성 향상

❓그렇다면 jwt의 단점은 없을까?

> 토큰이 탈취당하면 만료될 때까지 대처가 불가능 !

이를 해결하기 위해서는 Expiration Time(만료시간)을 짧게 설정할 수 있다

만료시간이 짧은 경우 UX적으로 불편함. 이을 해결하고 짧은 만료시간을 보완하기 위한 재발급 방식
1. Sliding Session : 특정한 서비스를 계속 사용하고 있는 특정 유저에 대해 만료 시간을 연장 시켜주는 방법
2. Refresh Token : JWT를 처음 발급할 때 Access Token과 함께 Refresh Token이라는 토큰을 발급하는 방법

<img width="701" alt="image" src="https://github.com/user-attachments/assets/7a58c87c-6b14-41fd-bb52-b2d06e7615e0">

```text
1. 사용자가 로그인한다.
2. 서버는 회원 확인 후 서명된 JWT 생성하여 클라이언트에 응답한다.
이때 Access Token과 Refresh Token을 같이 전달한다.
3. 사용자가 요청할 때마다 Access Token와 함께 보낸다.
4. 서버에서 Access Token을 검증한다.
5. 검증이 완료되면 응답을 보낸다.
6. 🚨 Access Token 만료되었다.
7. 사용자가 Access Token과 함께 데이터를 요청한다.
8. 서버에서 Access Token이 만료된 것을 확인한다.
9. 만료되었다는 것을 알려주는 응답을 보낸다.
10. 사용자는 만료 응답을 받고 Access Token과 Refresh Token을 같이 담아 발급 요청을 보낸다.
11. Refresh Token을 확인한 후 Access Token을 발급한다.
12. Access Token과 함께 응답을 보낸다.
```

## OAuth (Open Authorization)
<img width="744" alt="image" src="https://github.com/user-attachments/assets/b05ff5e8-4f19-43d6-b887-865bc35c0f0b">

# Docker

Go언어로 작성된 리눅스 **컨테이너 기반**으로하는 **오픈소스 가상화 플랫폼**

컨테이너를 사용하여 각각의 프로그램을 분리된 환경에서 실행 및 관리할 수 있는 툴

### 가상화를 왜 사용하나요?

CPU 사용률이 낮은 서비스들을 위해 서버를 실행하는건 서버 리소스 낭비임. 그렇다고 모든 서비스를 한 서버에 올리면 안정성 문제가 발생할 수 있음

이러한 문제를 해결함으로써 컴퓨터의 성능을 효율적으로 사용하고자 하는 방법이 서버 가상화! → 안정성 강화, 리소스 활용

### 가상머신 (VM) vs 컨테이너

<img width="648" alt="image" src="https://github.com/user-attachments/assets/4bde0063-d59e-4d69-afa4-e2a3f008be7c">

1. Virtual Machine : Host OS위에 Hypervison engine, 그리고 그 위에 Guest OS를 올려 사용함. 가상화된 하드웨어 위에 OS가 올라가는 형태이므로 Host와 완전히 분리

   → 장점 : 격리 레벨이 높아 보안성 좋음. 커널을 공유하지 않기 때문에 멀티 OS가 가능

2. 컨테이너 : Docker Engine 위에 어플리케이션 실행에 필요한 바이너리만 올라감. 따라서 Host의 커널을 공유하고, 이 덕분에 io 처리가 쉬워 성능의 효율을 높일 수 있음

   → 장점 : 성능 향상, 이식성이 좋음, 쉽게 scale out을 할 수 있는 유연성


⇒ 컨테이너를 사용하는 것은 가상 머신을 생성하는 것이 아니라, Host OS가 사용하는 자원을 분리하여 여러 환경으로 만든 것

### Container

1. 개념

   윈도우 환경 사용자 : 윈도우 환경을 사용해보면 하나의 컴퓨터에 여러 사용자로 나눠서 사용할 수 있게 구성되어있음. 각 사용자의 환경에 들어가보면 독립적으로 세팅이 되어있음.

   컨테이너 : 컨테이너도 이처럼 하나의 컴퓨터 환경에서 독립적인 컴퓨터 환경을 구성해서, 각 환경에 프로그램을 별도로 설치할 수 있게 만든 개념.

   이러한 **미니 컴퓨터**를 docker에서 container라고 함.

   컨테이너들을 포함하고 있는 컴퓨터 : host 컴퓨터


1. 컨테이너의 특징 : **독립성**
- 디스크 (저장 공간) : 각 컨테이너마다 서로 각자의 저장 공간을 가지고 있음. 일반적으로 서로 다른 컨테이너의 파일을 접근할 수 없음
- 네트워크 (IP, Port) : 컨테이너는 각자의 IP주소를 가지고 있음

### Docker Image

닌텐도의 칩같은 역할..! (게임기에 게임 칩을 꽂으면 해당 게임이 실행됨)

mysql 서버를 이미지로 만들었다면, 이 이미지를 docker로 실행시키는 순간 container 환경에서 mysql 서버가 실행됨

- 프로그램을 실행하는 데에 필요한 설치 과정, 설정, 버전 정보 등 (프로그램 실행에 필요한 모든 것을 포함)

### Dockerfile

```docker
FROM openjdk:17
ARG JAR_FILE=/build/libs/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar", "/app.jar"]
```

- FROM

  기반이 되는 이미지 레이어

  <이미지 이름>:<태그> 형식으로 작성

- MAINTAINER

  메인테이너 정보

- RUN

  도커 이미지가 생성되기 전에 수행할 쉘 명령어

- VOLUME

  디렉터리 내용을 컨테이너에 저장하지 않고 호스트에 저장하도록 설정.

- CMD

  CMD에서 설정한 실행 파일이 실행될 디렉터리

- EXPOSE

  호스트와 연결할 포트 번호

- COPY

  빌드 시 layer 캐싱 활용

- COPY

  이미지화 시킬 파일 경로, 이미지로 만들 때 읽을 파일 경로

  도커 내부 이미지 저장 경로


### 도커 동작 구조

[Dockerfile] - build -> [docker Image] - run -> [docker Container]

<img width="602" alt="image" src="https://github.com/user-attachments/assets/35950a68-aeae-42ae-85f3-0e616588a1fe">

`docker build -t [도커_이미지_이름:tag]` : build (도커 이미지 생성)

`docker run --name [도커_컨테이너_이름:tag] [도커_이미지_이름:tag]` : run (도커 컨테이너 생성 및 실행)

### .dockerignore

Docker 이미지 생성 시 이미지 안에 들어가지 않을 파일 지정

```docker
node_modules
npm-debug.log
Dockerfile*
docker-compose*
.dockerignore
.git
.gitignore
README.md
LICENSE
.vscode
```

### Docker Architecture

![image](https://github.com/user-attachments/assets/c1ceefbd-3709-4955-9763-6800d99eac8e)

1. Client : docker 명령어를 사용하여 컨테이너를 실행, 관리, 모니터링 하고 이미지 빌드나 다운로드 등의 행동을 수행

   command : `run`, `build`, `pull`

2. Host

   client의 요청을 받아들여 작업을 수행

   docker daemon을 실행하여 컨테이너를 생성, 시작, 중지 및 관리를 수행

3. Registry

   docker 이미지를 저장하고 관리하는 중앙 집중식 저장소

   docker hub, 독립적인 docker registry가 있음

   - Docker Hub & Docker Registry

      <img width="677" alt="image" src="https://github.com/user-attachments/assets/a9a5d143-a8ef-47e7-8021-06025208344b">

      1. Docker Hub : 이미지를 저장하고 관리 (github 같은…)
      2. Docker Registry : docker hub가 공개적인 저장소라면, docker registry는 비공개적으로 격리된 저장소


## Docker 실습

1. [docker 설치](https://www.docker.com/products/docker-desktop/)
2. Gradle 탭에서 Tasks-build-bootJar 실행 → build/libs 경로에 jar파일 생성
3. Dockerfile 생성
4. mysql 도커 이미지 생성 및 실행
   1. 도커 이미지 생성

       ```bash
       docker build -t mysql .
       ```

   2. 도커 이미지 실행 → 컨테이너 생성

       ```bash
       docker run --name mysql -e MYSQL_ROOT_PASSWORD=password -e MYSQL_DATABASE=InstagramDB -p 3306:3306 -d mysql:latest
       ```

5. instagram 도커 이미지 생성 및 실행
   1. 도커 이미지 생성

       ```bash
       docker build -t instagram .
       ```

   2. 도커 이미지 실행 → 컨테이너 생성

       ```bash
       docker run --name instagram -p 8080:8080 mysql:latest
       ```

6. http://localhost:8080 에 서버가 잘 띄워져 있다면 성공!

### 이미지 하나씩 만들기 귀찮은데? 한번에 관리할 수 없나 ??

- 이미지를 여러개 생성해서 각각 실행시키기 귀찮음 ..
- 상호작용하는 이미지들이 하나의 네트워크 상에 있어야함
- 파일 시스템 공유 방식 제어 필요

## Docker-compose

```yaml
version: "3"

services:

# mysql
  database:
    container_name: mysql
    image: mariadb:latest #mac
    environment:
      MYSQL_ROOT_PASSWORD: ${LOCAL_DB_PASSWORD}
      MYSQL_DATABASE: InstagramDB
      TZ: 'Asia/Seoul'
    volumes:
      - dbdata:/var/lib/mysql
    ports:
      - 3306:3306
    restart: always
    networks:
      - network
    healthcheck: #
      test: [ "CMD-SHELL", "mysqladmin ping -h 127.0.0.1 -p${DB_PASSWORD} --silent" ]
      interval: 30s
      retries: 5

#instagram
  web:
    container_name: instagram
    build:
      dockerfile: Dockerfile
    ports:
      - "8080:8080"
    depends_on:
      database:
        condition: service_healthy
    environment:
      url: ${LOCAL_DB_URL}
      username: ${LOCAL_DB_USERNAME}
      password: ${LOCAL_DB_PASSWORD}
    restart: always
    volumes:
      - app:/app
    networks:
      - network
    env_file: #환경변수는 .env파일 참조
      - .env

volumes: #파일 시스템 공유 방식
  dbdata:
  app:

networks:
  network:
    driver: bridge
```


docker-compose 실행 : `docker-compose -f docker-compose.yml up --build`

### Depends_on

서비스 간의 **의존관계**를 정의하는 데에 사용함 (서비스 실행의 선후관계)

- `depends_on`은 서비스의 **컨테이너를 실행하는 순서**만 관리
- 컨테이너 내부 애플리케이션의 준비 상태(health 상태)까지 보장하지 않음 → healthcheck

### Healthcheck

컨테이너와 어플리케이션 상태를 모니터링하기 위한 서비스

docker에서 컨테이너의 상태를 주기적으로 점검하는 기능

- healthy (정상) : 종료 코드 0
- unhealthy (비정상) : 종료 코드 1 이상, 설정 시간 내에 응답하지 못한 경우
- none (설정되지 않음)

컨테이너 실행 후에 healthcheck를 추가하거나 변경할 수 없음. 이미지 빌드 시에 dockerfile (docker-compose.yml)에서 정의

장점
- 어플리케이션의 신뢰성 향상
- 자동 복구 및 장애 대응 (unhealthy인 경우 오케스트레이션 도구가 해당 컨테이너를 재시작하거나 교체)
- 장애 원인 파악의 용이성
- 오케스트레이션과의 통합

### 오케스트레이션

1. 개념

   여러 컨테이너를 사용하는 복잡한 시스템에서 컨테이너의 배포, 관리, 확장 및 복구를 자동화 하는 과정

   컨테이너 관리 도구 : Docker Swarm, Kubernetes, Amazon ECS

2. 주요 기능
   - 컨테이너 배포 : 컨테이너를 적절한 노드(서버)에 자동으로 배포
   - 확장 (scaling) :
      - 사용자가 늘어나면 컨테이너 수를 자동으로 증가(수평 확장)
      - 트래픽이 줄어들면 컨테이너 수를 줄여 비용을 절감
   - 상태 관리
      - 컨테이너 상태를 지속적으로 모니터링
      - 컨테이너가 비정상 종료되거나 `HEALTHCHECK`에서 "unhealthy"로 표시되면 자동으로 재시작
   - 로드 밸런싱 : 들어오는 요청을 여러 컨테이너에 고르게 분산하여 성능을 최적화
   - 네트워크 관리 : 컨테이너 간의 네트워크를 구성하고, 통신을 안전하게 관리
   - 롤링 업데이트 : 새로운 버전의 애플리케이션을 배포할 때 중단 없는 업데이트 지원
   - 로깅 및 모니터링 : 실행 중인 컨테이너의 로그를 수집하고 상태를 모니터링

## trouble shooting

### Error message

```json
The last packet sent successfully to the server was 0 milliseconds ago. The driver has not received any packets from the server.

Caused by: com.mysql.cj.exceptions.CJCommunicationsException: Communications link failure
Caused by: java.net.ConnectException: Connection refused
```

### 문제 원인 및 시도

1. `useSSL=false` 설정

   접속하려는 mysql 데이타베이스의 SSL설정의 기본값이 true인 경우, 접속하려는 클라이언트가 ssl로 접속하지 않고, ssl이 아닌 일반적인 연결로 접속하기 때문에 발생한다. mysql서버쪽에서는 유효하지 않는 패킷이 넘어오는 것이 때문에 오류를 낸다고 생각하면 된다.

   JDBC 접속시에 SSL연결이 아닌 일반적인 연결(물론 보안이 약함)이라고 명시적으로 설정을 하면 된다.

   - SSL 이란 Secure Socket Layer의 약자로서 클라이언트와 서버가 통신을 할때 암호화를 통해서 보안을 높힌 접속을 말한다.

2. mysql container 실행을 안해둔 상태였다.. 그런데 실행하려고 보니? 이미 3306포트를 사용중이라고 떴다
   - `lsof -i :3306` 명령어를 통해 3306 포트를 사용중인지 확인하였으나 없었다 .. 
   - 무언가 문제가 생긴 것 같아 `docker system prune -a` 명령어로 모든 캐시를 삭제한 후 다시 mysql container 생성 → 성공!

3. 이후 다시 application image를 빌드한 후 실행하였으나 같은 에러 발생 ..
   트러블 슈팅 실패 ... 
4. docker-compose 실행 성공 후 healthcheck를 통해 mysql 서버에 대한 상태 체크를 해줄 필요가 있는 것 같아 Dockerfile 수정

    ```dockerfile
    FROM mysql:latest
    HEALTHCHECK --interval=10s CMD mysqladmin ping -h localhost || exit 1
    ```
   dockerfile이 달라졌기 때문에 image build부터 다시 실행
   - instagram 이미지 생성 : `docker build -t instagram .`
    
    - mysql 컨테이너 생성 및 실행 : `docker run --name mysqltest -e MYSQL_ROOT_PASSWORD=password -e MYSQL_DATABASE=InstagramDB -p 3306:3306 -d mysql:latest`

    - instagram 컨테이너 생성 및 실행 : `docker run --name instagramtest -e MYSQL_ROOT_PASSWORD=password -p 8080:8080 instagram:latest`
   <img width="1506" alt="image" src="https://github.com/user-attachments/assets/12db835b-1a8e-4369-9754-56847846a6ca">
    성공 !!

### 결론
    
instagram 컨테이너를 실행하기 위해 mysql 컨테이너의 정상적인 작동이 선행되어야 하고, 이를 확인하기 위해 healthcheck를 사용해야함.

### 근데 모르겠는 점..

healthcheck를 한다는건 mysql 서비스의 상태가 unhealty|healthy 중 무엇인지 체크를 하는거지, unhealthy한 서비스를 healthy하게 수정보완해주는 건 아닌데, 왜 healthcheck를 함으로써 문제가 해결된건지 ?? 

instagram 컨테이너를 생성&실행 할 때, 처음에는 mysql 컨테이너가 unhealthy하다가 정상화가 되는데, healthcheck를 하지 않으면 mysql이 unhealthy한 순간 instagram 컨테이너 실행이 실패하고, retry를 하지 않기 때문에 오류가 났던건가?? 

healthcheck를 하게되면 mysql이 잠시 unhealthy하더라도 instagram을 재부팅 하기 때문에 위의 문제가 해결..!! 이라고 추측 중인데 맞는지 모르겠음 ㅜㅜ

### docker-compose 실습
<img width="1363" alt="image" src="https://github.com/user-attachments/assets/4207400a-835c-48f1-8bc0-0ca690f27d9d">
<img width="1362" alt="image" src="https://github.com/user-attachments/assets/e4de3f12-80a0-4796-b25c-194826f27eef">


## 수동 배포

### 배포 방법
### ec2 빌드

1. 콘솔 회원가입, 로그인
    1. 지메일 +1 숫자 하면 계속 만들수잇음
    2. limgh+1@gmail.com
2. 우분투..
3. instance 세부정보 → 연결 버튼 → ec2 인스턴스에 연결 ‘연결’ 버튼

### cloudshell

1. sudo apt update
2. 도커 설치

   https://everydayyy.tistory.com/121

   버전 두개있음 이거 docker에서 제공하는걸로 다운받으셈

    ```bash
    # docker repository 접근을 위한 gpg 키 설정
    curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo apt-key add -
    
    # docker repository 등록
    sudo add-apt-repository "deb [arch=amd64] https://download.docker.com/linux/ubuntu focal stable"
    
    # 업데이트
    sudo apt update
    
    # docker 설치
    sudo apt install docker-ce
    ```

3. 스왑 메모리 사용할 수 있도록 하기

   https://repost.aws/ko/knowledge-center/ec2-memory-swap-file

   https://ssue-dev.tistory.com/2

    ```bash
    df -h #하드디스크 용량
    
    # 2G의 의미는 2G만큼을 swapfile로 생성하게 되어집니다.
    # 따라서 {N}G와 같이, N에 본인이 생성할 스왑메모리를 할당합니다.
    sudo fallocate -l 2G /swapfile
    
    chmod 600 /swapfile # swap 파일 권한 수정
    
    # 생성된 swapfile을 이용하여 swap memory 활성화
    sudo mkswap /swapfile
    sudo swapon /swapfile
    
    # 시스템이 재부팅 되어도 swap이 적용되도록 설정
    sudo echo '/swapfile none swap sw 0 0' | sudo tee -a /etc/fstab
    
    # Swap 메모리 활용 수준 확인 (기본값 60)
    sysctl vm.swappiness
    
    # 디렉토리와 inode 오브젝트에 대한 캐시로 사용된 메모리를 반환하는 경향의 정도 (기본값 100)
    sysctl vm.vfs_cache_pressure
    ```

4. sudo docker login -u [username] → password

### 내 프로젝트

1. build
2. 도커 이미지 생성 : docker build - -platform linux/amd64 -t dingdong20/instagram .
3. 도커 허브에 푸시 : docker push dingdong20/instagram

### cloudshell

1. 이미지 풀 받기 : sudo docker pull dingdong20/instagram
2. sudo docker pull mysql
3. mysql 컨테이너 생성 및 실행 : sudo docker run -e MYSQL_ROOT_PASSWORD=password -d -p 3306:3306 mysql
4. .env파일을 별도로 만들어둠 (vi .env)
5. instagram 컨테이너 생성 및 실행 : sudo docker run -e .env -d -p 80:8080 dingdong20/instagram
6. 그럼 퍼블릭 아이피에 서버 올라간거 확인할 수 있음

### 서버 내리는 방법

ec2 인스턴스 중지를 하면 컴퓨터 전체를 끝낸다고 생각하면됨 (근데 중지해도 ip 할당은 받은 상태라서 돈은 나감)

서버 내릴때는 sudo docker rm -f instagram 으로 컨테이너를 제거!!

## Trouble Shooting

### instagram container 실행이 안되는 문제 발생

1. `.env` 환경 변수 수정 - rds에서 설정한대로
    - DB_URL=[database-2.cjqwug44qqcy.ap-northeast-2.rds.amazonaws.com:3306/instagramDB](http://database-2.cjqwug44qqcy.ap-northeast-2.rds.amazonaws.com:3306/instagramDB)
        - [database 엔드포인트]/[db 이름]
    - DB_USERNAME : admin
    - DB_PASSWORD : password
2. ec2 방화벽 규칙 업데이트

    ```bash
    sudo ufw status # ufw 방화벽 규칙 확인
    
    # 결과 : "Status: inactive"
    ```

    ```bash
    sudo ufw allow 80 # HTTP(80) 트래픽 허용
    
    # 결과 : "Rules updated"
    ```

3. log 확인 → MYSQL_ROOT_PASSWORD 설정해야한다고 .. 왤까 ….

    ```bash
    sudo docker run -e MYSQL_ROOT_PASSWORD=1234 -e .env -d -p 80:8080 dingdong20/instagram
    ```


### container 실행까지는 성공했는데, 서버가 안뜨는 문제
<img width="1497" alt="image" src="https://github.com/user-attachments/assets/642ed04e-d892-43c1-9174-8ba98050ad95">

![image](https://github.com/user-attachments/assets/fcb34f81-ca96-41b8-bd29-73cd930a1d61)
