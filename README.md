# bulletin-board For R

## 비지니스 목표
- 공지사항 등록, 수정, 삭제, 조회 API 구현
- 공지사항 등록시 입력 항목
  - 제목, 내용, 공지 시작일시, 공지 종료일시, 첨부파일 (여러개) 
- 공지사항 조회시 응답
  - 제목, 내용, 등록일시, 조회수, 작성자

## 테크
- 테스트 : 단위테스 & 통합테스트 모두 작성
- 대용량 트래픽 고려
  - 고속처리 : 몽고? 레디스? 
  - 스케일아웃 : 위한 토큰방식? >> 계정까지 필요한데..
  - 비동기 큐잉 : ??
  - 최소한 읽기성능만큼은 신경쓰기
- 첨부파일 기능 : 첨부파일 S3 or 로컬머신 저장
- 기술적인 부분의 문제해결 전략, 실행 방법을 잘 정리하기
- REST API >> Spring HEATOAS 적용 (우선순위 낮음)
- 스웨거로 문서화
- 테스트전송 샘플


## Redis 를 위한 도커 config 
```
$ docker-compose -f ./asset/redis_standalone.yml up -d
```



## 메타데이터 작성
- https://docs.spring.io/spring-boot/docs/current/reference/html/configuration-metadata.html
- 프로퍼티 커스텀 디파인 정의
