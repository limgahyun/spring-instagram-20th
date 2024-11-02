# spring-instagram-20th
CEOS 20th BE study - instagram clone coding

## DB Modeling
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

### POST
`BaseEntity` : `Post`와 `PostComment` Entity에서 `createdAt`, `updatedAt` 속성을 공통적으로 사용하고 있어 `BaseEntity`로 분리하여 구현하였다.

`PostComment` : `PostComment`는 자기 자신을 참조하여 `parent_id`를 foriegn key로 갖는다. 이 값이 NULL인 경우 댓글, 값이 존재하는 경우 대댓글이다.

### MESSAGE
`Message` : Message의 Type이 text, image, post 세가지로 분류되므로, 다음과 같은 상속관계를 갖는다
![img_1.png](img_1.png)
이때 각각 테이블을 생성하여 저장공간을 효율적으로 사용하기 위하여 조인 전략을 선택하였고, `@Inheritance(strategy=InheritanceType.JOINED)` annotation을 사용하였다.
메세지 조회 기능을 사용할 일이 없다고 예상되어 조인 전략을 선택하였으나 데이터를 저장하는 데에도 성능이 저하될 가능성이 있을지 우려된다. `JOINED`, `SINGLE_TABLE` 둘 중 어느 것이 더 적절할지 고민할 필요가 있다.

`ChattingRoom` : 채팅을 하던 상대방이 인스타그램을 탈퇴하더라도 채팅 기록은 남아있도록 하기 위하여 `ChattingRoom`을 분리하여 구현하였다.

---
## JPA 심화
### 주요 기능
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

### UserService
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
### UserServiceTest
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
### N+1 문제
`jpql` + `@EntityGraph` 혼합 방법을 사용하여 N+1문제를 해결하였습니다.
```java
@Test
@EntityGraph(attributePaths = {"user"})
@Query("select p from Post p")
public void findAllEntityGraph () {
    ...
}
```

## Global Exception
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
## API 명세서

### 개발 전 명세서 ?

DTO를 생성할 때에도, CRUD api 를 생성하려고 할 때에도, 기능들에 대한 정리가 안되어있다보니 어디부터 손대할지 막막하고 개발 도주에 수정할 것들이 많아져서 뒤죽박죽이 되는 느낌이었다. 이를 해소하고자 API 명세서를 작성하는 방법부터 찾아보았는데 우선 내가 개발하는 데에 쓰는 용도로 notion을 이용해서 제작하였다.

다음과 같은 형식으로 기능, http method, api path, token, 각 기능에 필요한 DTO를 정리하려고 하였다.

![image](https://github.com/user-attachments/assets/df0c130d-f8cf-48fd-8aa6-511e287f6bb8)

### 협업 때 사용할 명세서

노션은 내가 보려고 한거고.. 실제로 노션을 쓰면 실제 코드와 동기화가 안되기 때문에 비효율적 + 에러핸들링 어려움의 문제가 있을 것이라고 생각하였다.

코드 기반으로 api 명세서를 생성하는 swagger, spring REST docs 에 대해 찾아보았다

1. swagger
- 장점 : swagger-ui 문서에서 api test 가능
- 단점 : 코드에 어노테이션을 추가해야 하므로 서비스코드와 api 명세서 관리가 혼합된다
2. spring REST docs
- 장점 : 코드에 영향 X
- 단점 : 테스트 코드를 기반으로 생성되므로 모든 테스트 코드를 작성해야한다

프론트 개발을 할 때 swagger-ui를 사용해보았는데 ui가 그리 보기 편하진 않았어서.. REST docs를 사용하고 싶었지만..! 테스트를 만들어서 실행하는게 익숙하지 않기 때문에 처음에는 swagger를 시도해보기로 결정하였다.

### swagger-ui
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

## Controller 통합 테스트
에러 발생 시 설정해둔 exception status, message가 표시되는 것을 확인하였다.

<img width="1427" alt="image" src="https://github.com/user-attachments/assets/18d6c70d-f3f1-4ce1-94b2-e2c6cff061ad">
