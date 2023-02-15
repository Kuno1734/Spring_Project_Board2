# Spring_Project_Board2 
### Level2 구현
### API명세서
<p align =center><img width="887" alt="스크린샷 2023-02-13 오전 9 54 39" src="https://user-images.githubusercontent.com/121773974/218348347-c9db3136-50b8-4181-bdd1-465de7376b81.png"></p>

### ERD

### 요구사항
1. 회원 가입 API
    - username, password를 Client에서 전달받기
    - username은  `최소 4자 이상, 10자 이하이며 알파벳 소문자(a~z), 숫자(0~9)`로 구성되어야 한다.
    - password는  `최소 8자 이상, 15자 이하이며 알파벳 대소문자(a~z, A~Z), 숫자(0~9), 특수문자`로 구성되어야 한다.
    - DB에 중복된 username이 없다면 회원을 저장하고 Client 로 성공했다는 메시지, 상태코드 반환하기
    - 회원 권한 부여하기 (ADMIN, USER) - ADMIN 회원은 모든 게시글, 댓글 수정 / 삭제 가능
    
2. 로그인 API
    - username, password를 Client에서 전달받기
    - DB에서 username을 사용하여 저장된 회원의 유무를 확인하고 있다면 password 비교하기
    - 로그인 성공 시, 로그인에 성공한 유저의 정보와 JWT를 활용하여 토큰을 발급하고, 
    발급한 토큰을 Header에 추가하고 성공했다는 메시지, 상태코드 와 함께 Client에 반환하기
    
3. 댓글 작성 API
    - 토큰을 검사하여, 유효한 토큰일 경우에만 댓글 작성 가능
    - 선택한 게시글의 DB 저장 유무를 확인하기
    - 선택한 게시글이 있다면 댓글을 등록하고 등록된 댓글 반환하기
    
4. 댓글 수정 API
    - 토큰을 검사한 후, 유효한 토큰이면서 해당 사용자가 작성한 댓글만 수정 가능
    - 선택한 댓글의 DB 저장 유무를 확인하기
    - 선택한 댓글이 있다면 댓글 수정하고 수정된 댓글 반환하기
    
5. 댓글 삭제 API
    - 토큰을 검사한 후, 유효한 토큰이면서 해당 사용자가 작성한 댓글만 삭제 가능
    - 선택한 댓글의 DB 저장 유무를 확인하기
    - 선택한 댓글이 있다면 댓글 삭제하고 Client 로 성공했다는 메시지, 상태코드 반환하기
    
6. 예외 처리
    - 토큰이 필요한 API 요청에서 토큰을 전달하지 않았거나 정상 토큰이 아닐 때는 "토큰이 유효하지 않습니다." 라는 에러메시지와 statusCode: 400을 Client에 반환하기
    - 토큰이 있고, 유효한 토큰이지만 해당 사용자가 작성한 게시글/댓글이 아닌 경우에는 “작성자만 삭제/수정할 수 있습니다.”라는 에러메시지와 statusCode: 400을 Client에 반환하기
    - DB에 이미 존재하는 username으로 회원가입을 요청한 경우 "중복된 username 입니다." 라는 에러메시지와 statusCode: 400을 Client에 반환하기
    - 로그인 시, 전달된 username과 password 중 맞지 않는 정보가 있다면 "회원을 찾을 수 없습니다."라는 에러메시지와 statusCode: 400을 Client에 반환하기
    
3. 전체 게시글 목록 조회 API
    - 제목, 작성자명(username), 작성 내용, 작성 날짜를 조회하기
    - 작성 날짜 기준 내림차순으로 정렬하기

---

1. 전체 게시글 목록 조회 API
    - 제목, 작성자명(username), 작성 내용, 작성 날짜를 조회하기
    - 작성 날짜 기준 내림차순으로 정렬하기
    - 각각의 게시글에 등록된 모든 댓글을 게시글과 같이 Client에 반환하기
    - 댓글은 작성 날짜 기준 내림차순으로 정렬하기
    
2. 게시글 작성 API
    - 토큰을 검사하여, 유효한 토큰일 경우에만 게시글 작성 가능
    - 제목, 작성자명(username), 작성 내용을 저장하고
    - 저장된 게시글을 Client 로 반환하기
    
3. 선택한 게시글 조회 API
    - 선택한 게시글의 제목, 작성자명(username), 작성 날짜, 작성 내용을 조회하기 
    (검색 기능이 아닙니다. 간단한 게시글 조회만 구현해주세요.)
    - 선택한 게시글에 등록된 모든 댓글을 선택한 게시글과 같이 Client에 반환하기
    - 댓글은 작성 날짜 기준 내림차순으로 정렬하기
    
4. 선택한 게시글 수정 API
    - 토큰을 검사한 후, 유효한 토큰이면서 해당 사용자가 작성한 게시글만 수정 가능
    - 제목, 작성 내용을 수정하고 수정된 게시글을 Client 로 반환하기
    
5. 선택한 게시글 삭제 API
    - 토큰을 검사한 후, 유효한 토큰이면서 해당 사용자가 작성한 게시글만 삭제 가능
    - 선택한 게시글을 삭제하고 Client 로 성공했다는 메시지, 상태코드 반환하기
    
---

1. 처음 설계한 API 명세서에 변경사항이 있었나요? 변경 되었다면 어떤 점 때문 일까요? 첫 설계의 중요성에 대해 작성해 주세요!
    - 처음 설계한 명세서에 대해서는 크게 변경사항은 없었으나 데이터를 불러오는 방법이나 클라이언트와 데이터의 송/수신 방법에 대해 새롭게 알게 됬습니다.
    - ex)ResponsEntiy<> 와 같은.
    
2. ERD를 먼저 설계한 후 Entity를 개발했을 때 어떤 점이 도움이 되셨나요?
    - ERD를 먼저 만들지 않고 명세서만으로 처음설계를 진행.
    - 연관관계를 도식화하지 않아 설정에 어려움이 있었습니다.
    
3. JWT를 사용하여 인증/인가를 구현 했을 때의 장점은 무엇일까요?
    - 토큰의 보호를 받는 리소스로 돌아갈 떄마다 자격 증명을 다시 입력할 핋요없이 엑세스가 가능하다 -> 불필요한 인증과정 감소로 트래픽 위험 저조
    - 별도의 저장소가 불필요하기 떄문에 서버의 자원을 절약할 수 있다.
    
4. 반대로 JWT를 사용한 인증/인가의 한계점은 무엇일까요?
    - 암호가 풀린다면 토큰에 포함된 사용자 데이터가 누출될 우려가 있다.
    - 노출 가능성으로 인해 저장할 수 있는 정보가 제한적이다.
    - 저장소가 없으므로, 만료시간을 변경하거나, 사용자 세션을 강제로 끊을 수 없다.

5.만약 댓글이 달려있는 게시글을 삭제하려고 할 때 무슨 문제가 발생할까요? JPA가 아닌 Database 테이블 관점에서 해결방법이 무엇일까요?
    - 연관관계가 명확하지 않다면 게시글과 댓글의 연관성이 없어지므로 서로가 어떤 정보를 가지고 호출할 수 있는지 알 수 없게된다.
    
6. 5번과 같은 문제가 발생했을 때 JPA에서는 어떻게 해결할 수 있을까요?
    - 연관관계를 명확히 한다 이는 외부키 제약조건으로 처리할 수 있다.
    - 이는 하나의 테이블이 다른 테이블에 의존하는 형태를 말한다 상세내용은 다음으로 확인이 가능하다.
    - https://makand.tistory.com/entry/SQL-FOREIGN-KEY-%EC%A0%9C%EC%95%BD-%EC%A1%B0%EA%B1%B4

7. IoC / DI 에 대해 간략하게 설명해 주세요!
