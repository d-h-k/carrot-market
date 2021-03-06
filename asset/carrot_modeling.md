# 당근마켓 데이터 모델을 분석하고 ER-D 작성하는 글

## 당근마켓 도메인 모델링
- 이전에 도메인 모델링과 관련된 학습자료 몇개를 접하면서 어떻게 도메인을 모델링해야할까에 대한 생각을 조금씩 하고 있었는데요
  - [마틴 파울러 - 소프트웨어 아키텍처의 중요성 / 영상, 20분 Link](https://youtu.be/4E1BHTvhB7Y)
  - [생활코딩 관계형 데이터모델링 / 강좌 Link ](https://opentutorials.org/course/3883)
  - [클린 소프트웨어 / YES24 책 Link ](http://www.yes24.com/Product/Goods/39497990)
- 이번에 당근마켓 백엔드 클론코딩을 진행하면서, 작고 간단하고 100% 끝가지 도달하지 못할 프로젝트일지라도 의식의 흐름대로 마구잡이식 난개발을 진행하지 말고 어느정도는 Minimum Viable 는 구현할수 있는 방향으로 데이터를 모델링하기로 결심했습니다. 
- 복잡한 도메인 로직이 존재하는 당근마켓 서비를 클론코딩하기 전 Service Ownership 을 가질수 있도록 기획단계를 거쳤는데요
- 도메인 모델링에 상당히 비중높게 글을 작성하는 이유는 좋은 소프트웨어 아키텍쳐가 중요하기 때문이고, 아키텍쳐가 제대로 나오기 위해서는 모델링이 잘 되어있어야 하기 때문입니다. 
  - 그전에 기획단계에서 잘 구성되어있어야 하고, 기획도 완벽하지 않으니까 trial&error 가 필요합니다. 그래서 빠르게 도전하고 실패하는 agile process 가 필요한데, 돌고돌아 결국 협업이고 같이 일하는 사람을 얼마냐 잘 배려해주고 퍼포먼스를 낼수 있도록 협력하는게 중요한 시대~
> ![](https://user-images.githubusercontent.com/31065684/150246437-c19cebc3-c984-4f6d-8768-dfc958972692.png)
- 이 사진은 위에서 소개한 [소프트웨어 아키텍처의 중요성](https://youtu.be/4E1BHTvhB7Y) 이라는 영상의 한 부분을 캡쳐한건데요, 기능이 많고 고도화될수록 아키텍쳐의 중요성이 더 커지게 된다는걸 설명한 부분입니다.
- 사족을 달아보면 모든 구성원들과 개발자들의 동작속도가 B(O) 가 log-N 이 될수도, n^2 이 될수도 있다는건데요
- 짧지만 회사다니는 2년동안 No-Design 의 코드를 유지보수를 하는데 대부분의 시간을 사용했고, Good-Design 으로 리팩토링이 끝나고 얼마 지나지 않아 회사가 어려워졌었던 경험이 있는데요
- 앞으로의 커리어에서는 Good-Design Code 에서 기획과 마케팅이 정교하게 결합된 고도화된 서비스를 만들어 제 목표인 `1억명이 사용하는 서비스 만들기` 를 달성하고 싶습니다. 

> 요약 : 좋은 결정은 빨리 내려지길 원한다. 소프트웨어의 가격을 고려할때 가장 높은 valuation 은 미래가치(확장성, 유지보수성, 좋은 아키텍쳐와 모델링)에 두어야만 한다!

## 모델링 순서
- 도메인을 모델링하기 위해 순서를 거쳤는데
  - 1] 구현할 `기능`을 먼저 생각한다
  - 2] 기능에 따라 적절한 페이지의 `VIEW 와 API 의 URI 를 지정`한다
  - 3] 저장해야할 `데이터`를 기능으로부터 생각해 `테이블`과 `필드변수`들을 생각해낸다
  - 4] 만족스러울때까지 앞단계들을 반복한다

## VIEW 와 API 의 URI 지정

### VIEW Page 정리
| 번호  | 설명  | VIEW   |  |
|:---------:|:----------|:---------|:---------|
|1 | 회원가입                                 | ?? | |
|1.1| 랜딩페이지                              | ?? |  |
|1.2| 회원가입 화면                           | `/login/join` |  |
|1.3| 로그인 화면                             | `/login` |  |
|1.4| (추가기능) 토큰만료 화면                | `/login/fail` | |
| 2 | 상품관련                                |  |  |
|2.1| 상품등록 페이지                         | `/posts/new` | |
|2.2| 카테고리 페이지                         | - | |
|2.3| 상품 상세 페이지                        | `/posts/{id}`| |
|2.4| 이 판매자의 다른 판매상품               | `/posts/otherSales`| |
| 3 | 마이페이지                              | | |
|3.1| 마이 페이지 메인(나의 당근 페이지)      | `/accounts/`| |
|3.2| 프로필 수정 페이지                      | `/accounts/userModify`| |
|3.3| 판매내역 페이지                         | `/accounts/posts`| |
|3.4| 관심목록                                | `/accounts/favoritePost`| |
| 4 | 댓글기능                                | | |
|4.1| 댓글달기 기능                           | - | |
|4.2| 댓글수정 기능                           | - | |
|4.3| 댓글삭제 기능                           | - | |
| 5 | 추가기능                                | | |
|5.1| 글 보호잠금 기능 / 사기피해예방         | | |
|5.2| 유효한 이메일인지 확인 / 업자 필터      | | |
|5.3| 중고 매물 검색기능                      | | |
|5.4| 알람 기능 (키워드, 관심상품, 내 작성글) | | |
|5.5| 대댓글 기능                             | | |
|5.6| 뱃지 기능                               | | |
|5.7| 매너온도 기능                           | | |
|5.8| 채팅 기능 추가                          | | |
|5.9| 글 끌어올림 기능                        | | |

## 전체 모델링
- 전체 모델링은 이렇게 작성했습니다
![](https://user-images.githubusercontent.com/31065684/149715006-9b5058b0-e2b6-4106-ab2c-81027bd0a379.jpeg)
