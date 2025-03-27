## 프로젝트 소개
- 물류 관리 및 배송 시스템을 위한 MSA(Microservices Architecture) 기반 플랫폼입니다.
- MSA의 복잡성을 이해하고, 팀원들과 함께 MSA를 구축하면서 실무에서 발생할 수 있는 다양한 문제들을 간접적으로 경험하고 해결하는 것을 목표로 합니다.

### 주문부터 최종 배송까지의 물류 흐름
<p align="center">
  <img src="https://github.com/user-attachments/assets/9205843b-9aae-4c0f-9079-ca32675032c5" width="700"/>
</p>



## 시스템 아키텍처
<p align="center">
  <img src="https://github.com/user-attachments/assets/291297a9-0298-4e50-9aa7-97208df853e7" width="600"/>
</p>


## 기술 스택
<p>
<img src="https://img.shields.io/badge/Java-CA6201?style=plastic&logo=OpenJDK&logoColor=white">
</p>
<p>
<img src="https://img.shields.io/badge/Spring Boot-6DB33F?style=plastic&logo=springboot&logoColor=white">
<img src="https://img.shields.io/badge/Spring Cloud-6DB33F?style=plastic&logo=spring&logoColor=white">
<img src="https://img.shields.io/badge/Spring Security-6DB33F?style=plastic&logo=springsecurity&logoColor=white">
<img src="https://img.shields.io/badge/Spring Data JPA-6DB33F?style=plastic&logo=spring&logoColor=white">
</p>
<p>
<img src="https://img.shields.io/badge/MySQL-4479A1?style=plastic&logo=MySQL&logoColor=white">
<img src="https://img.shields.io/badge/Redis-FF4438?style=plastic&logo=Redis&logoColor=white">
</p>
<p>
<img src="https://img.shields.io/badge/Kafka-231F20?style=plastic&logo=apachekafka&logoColor=white">
</p>
<p>
<img src="https://img.shields.io/badge/Docker-2496ED?style=plastic&logo=Docker&logoColor=white">
</p>
<p>
<img src="https://img.shields.io/badge/JWT-000000?style=plastic&logo=jsonwebtokens&logoColor=white">
</p>



## 서비스 구성
### 공통 사항
- 서비스 간 통신: 단순 조회 시 REST API를 통해 통신하며 FeignClient를 이용, 단순 조회를 제외한 서버 간 통신은 메시지 큐 이용
- 캐싱: 빈번하게 사용되는 조회 데이터를 Redis에 캐싱하여 응답 속도 개선
- 동시성 제어: 분산 환경에서 발생할 수 있는 동시성 문제를 해결하기 위해 Redis의 원자성과 빠른 처리 속도를 이용하여 재고 감소, 배정 등 경쟁 조건 발생 작업의 정합성 보장
- Audit 필드 추가/논리적 삭제:모든 테이블에 created_at, created_by, updated_at, updated_by, deleted_at, deleted_by 필드를 추가하여 데이터 감사 로그 기록
- 트랜잭션 관리: 여러 엔티티가 동시에 업데이트될 경우 트랜잭션을 사용하여 일관성을 유지하며 실패 시 전체 롤백 처리


### 허브 관리
- 허브 위치: 17개 지역(서울, 대전, 부산 등)에 허브 센터 운영
- 허브 간 이동 정보: 다익스트라 알고리즘을 사용하여 허브 간 최단 경로 매핑을 통해 물류 운반 지원

<details>
<summary>17개 허브 센터 리스트</summary>
<ol>
    <li>서울특별시 센터 : 서울특별시 송파구 송파대로 55</li>
    <li>경기 북부 센터 : 경기도 고양시 덕양구 권율대로 570</li>
    <li>경기 남부 센터 : 경기도 이천시 덕평로 257-21</li>
    <li>부산광역시 센터 : 부산 동구 중앙대로 206</li>
    <li>대구광역시 센터 : 대구 북구 태평로 161</li>
    <li>인천광역시 센터 : 인천 남동구 정각로 29</li>
    <li>광주광역시 센터 : 광주 서구 내방로 111</li>
    <li>대전광역시 센터 : 대전 서구 둔산로 100</li>
    <li>울산광역시 센터 : 울산 남구 중앙로 201</li>
    <li>세종특별자치시 센터 : 세종특별자치시 한누리대로 2130</li>
    <li>강원특별자치도 센터 : 강원특별자치도 춘천시 중앙로 1 </li>
    <li>충청북도 센터 : 충북 청주시 상당구 상당로 82</li>
    <li>충청남도 센터 : 충남 홍성군 홍북읍 충남대로 21</li>
    <li>전북특별자치도 센터 : 전북특별자치도 전주시 완산구 효자로 225</li>
    <li>전라남도 센터 : 전남 무안군 삼향읍 오룡길 1</li>
    <li>경상북도 센터 : 경북 안동시 풍천면 도청대로 455</li>
    <li>경상남도 센터 : 경남 창원시 의창구 중앙대로 300</li>
  </ol>
</details>

<details>
<summary>17개 허브 센터 연결 정보</summary>
  <ul>
    <li>연결된 허브 간 배송만 가능 (예를 들어, 서울-부산 배송 시 서울-경기남부-대구-부산 순으로 배송)</li>
    <ul>
      <li>경기남부: 경기북부, 서울, 인천, 강원도, 경상북도, 대전, 대구</li>
      <li>대전: 충청남도, 충청북도, 세종, 전라북도, 광주, 전라남도, 경기남부, 대구</li>
      <li>대구: 경상북도, 경상남도, 부산, 울산, 경상북도, 경기남부, 대전</li>
      <li>경상북도: 경기남부, 대구</li>
    </ul>
  </ul>
</details>

### 배송 담당자 관리
- 배송 담당자 타입: 허브 배송 담당자와 업체 배송 담당자로 구분
- 허브 배송 담당자: 허브 간 물류 운반 담당 (전체 시스템에 10명 존재)
- 업체 배송 담당자: 목적지 허브에서 수령 업체까지 물류 운반 담당 (허브 당 10명 존재)
- 배송 담당자 배정: 배송 순번 기준 라운드로빈 방식으로 순차적 배정

### 업체 관리
- 업체 타입: 생산 업체와 수령 업체로 구분
- 업체 소속: 모든 업체는 특정 허브에 소속

### 상품 관리
- 상품 소속: 모든 상품은 특정 업체와 허브에 소속
- 재고 관리: 입출고 시 수량 갱신 및 재고 부족 시 대응

### 주문 관리
- 주문 생성: 주문 생성 시 재고 수량 감소
- 주문 취소: 주문 취소 시 재고 수량 복원
- 재고 확인: 재고 부족 시 대응

### 배송 관리
- 배송 데이터: 주문 생성 시 배송 및 배송 경로 기록 자동 생성
- 배송 담당자 배정: 배송 유형(허브, 업체)에 따른 배송 담당자 자동 배정

### 슬랙 메시지 관리
- 슬랙 메시지 발송: 주문 발생 시점에 슬랙을 통해 허브 담당자에게 생성된 메시지 발송
<details>
  
<summary>전달 메시지 예시</summary>
    주문 번호: 1 <br>
    주문자 정보: 홍길동 / tester@4yeargap.com <br>
    상품 정보 : 천혜향 80 박스 <br>
    요청 사항 : 04월 15일 12시 전까지 보내주시면 감사하겠습니다. <br>
    발송지 : 경기 북부 센터 <br>
    경유지 : 대전광역시 센터, 부산광역시 센터 <br> 
    도착지 : 부산시 사하구 낙동대로 1번길 1 4살터울 <br><br>
    위 내용을 기반으로 도출된 최종 발송 시한은 04월 12일 오전 11시 입니다.
  
</details>

### 사용자 관리
- 사용자 정보: 모든 사용자 정보 관리
- 권한: 마스터 관리자, 허브 관리자, 배송 담당자, 업체 담당자
<details>
  
<summary>권한 리스트</summary>
<ul>
  <li>마스터 관리자: 모든 기능에 대한 권한이 있는 관리자</li>
  <li>허브 관리자: 담당 허브 관리, 허브에 속한 배송 담당자 관리, 허브에 속한 업체 관리</li>
  <li>배송 담당자: 허브 배송 담당자는 허브 간 배송 담당(허브 → 업체 배송은 불가), 업체 배송 담당자는 허브에서 업체까지의 배송 담당(허브 간 배송은 불가)</li>
  <li>업체 담당자: 소속된 업체 정보 관리, 업체가 등록한 상품 관리</li>
</ul>
  
</details>



## 트러블슈팅
### 1. ID 통합 관리
<img width="1365" alt="스크린샷 2025-03-25 오후 4 15 13" src="https://github.com/user-attachments/assets/8bcd70e7-5d42-4528-9579-b10e3be4ba3d" />

- **문제**
  - 마이크로서비스 아키텍처에서 각 서비스(User, Hub, Order, Delivery, Company)가 서로 다른 데이터베이스를 사용하고, 다른 서버의 데이터를 자주 조회해야 하는 상황이어서 API 호출로 인한 비효율성, 성능 저하, 복잡성 증가
- **해결 방안**
  - 각 서버에서 관리하는 ID값을 단일 지점인 레디스 서버에 캐시하여 관리
    - 카프카를 통한 ID 값 전달
      - ID 값 변경(Create, Update, Delete) 시, 카프카 메시지를 통해 이를 User 서버에 전달
    - 레디스에 ID 캐싱
      - User 서버가 수신한 카프카 메시지 이벤트 타입에 따라 레디스에 ID 값을 create, update, delete
      - 각 서버는 ID 값 필요 시 직접 레디스에서 조회하여, 각 서버가 필요로 하는 ID 값을 빠르게 캐시에서 읽기 가능
- **결론**
  - 비효율성 제거
    - 한 서버에서 여러 다른 서버로 API 요청을 보내는 로직을 제거하여 코드량을 줄이고, 유지보수 비용절감
  - 성능 향상
    - API 호출 대신 레디스에서 직접 데이터를 조회하여 응답 시간 단축
    - Before와 비교했을 때 약 51% 증가, 평균 테스트 시간은 약 32.47% 감소
  - 확장성 및 유연성 증가
    - 각 서버가 직접 레디스에 접근하여 데이터를 조회하기 때문에 서버 간 의존성이 줄어들고, 시스템 확장성 향상

<img src="https://github.com/user-attachments/assets/a9a5e190-e798-4b40-af1f-6846d4ff6bc9" width="850"/> <br>
**Before**

<img src="https://github.com/user-attachments/assets/5c8319a9-0ca9-4cfd-9895-43b5bafd66cd" width="850"/> <br>
**After**


### 2. 허브 간 최소 이동 경로 정보 관리
<img src="https://github.com/user-attachments/assets/e136f253-d7ae-4aec-b61f-09da623414b9" width="260"/> <br>
- **문제**
  - 경로 데이터가 필요할 때마다, 다익스트라로 허브 간 최소 이동 경로 계산 시 평균 TPS 289
  - 허브가 추가로 만들어질수록 TPS 저하 우려
- **해결 방안**
  - 계산된 허브 간 최소 이동 경로 데이터를 레디스 서버에 캐싱
- **결론**
  - 레디스에 캐싱된 데이터 읽기 연산 시 평균 TPS 11,865

<img src="https://github.com/user-attachments/assets/8de80a4c-7a50-40d5-91db-6d2d811f0896" width="850"/> <br>
**Before**

<img src="https://github.com/user-attachments/assets/3d92cb6a-3a20-41f8-b7ba-8c7bdad8bce1" width="850"/> <br>
**After**



### 3. 동시성 문제 제어
- **문제**
  - 상품 재고 변경 시 동시성 문제 발생 가능
  - 허브/업체 배송 담당자 추가 또는 배정 시 동시성 문제 발생 가능
- **해결 방안**
  - **첫 번째 방안**: 비관적 락(Pessimestic_write) 사용
    - **문제**: 공유 자원에 대한 원자성을 보장하였으나, 이로 인해 TPS가 저하되는 문제 확인
  - **두 번째 방안**: MySQL 이진로그 버퍼 개수를 증가시키고, 로그 버퍼를 디스크에 플러시하는 빈도를 감소시킴
    <details>
      <summary>TPS를 증가시키기 위한 DB 설정 변경</summary>
      innodb_flush_log_at_trx_commit = 2 <br>
      innodb_thread_concurrency = 16 <br>
      sync_binlog = 1000 <br>
    </details>

    - **문제**: 트랜잭션 로그와 이진 로그를 즉시 디스크에 기록하지 않고 버퍼에 임시 저장한 후 처리하는 방식으로 TPS가 향상되지만, 시스템 안정성은 감소하고 복제 환경에서 데이터 일관성 저하 우려
  - **세 번째 방안**: Redis의 싱글 스레드 특성을 활용하여 루아 스크립트를 작성하고, DB 업데이트를 비동기 방식로 전환
- **결론**
  - 138 -> 611 -> 3,015 평균 TPS 증가

<img src="https://github.com/user-attachments/assets/f1e2ffd7-0030-4168-b188-f6922e7ae80e" width="850"/> <br>
**비관적 락 적용 후 TPS 측정 결과**

<img src="https://github.com/user-attachments/assets/1bd44574-3cff-4cf9-8b07-370b0db878ae" width="850"/> <br>
**MySQL 설정 변경 후 TPS 측정 결과**

<img src="https://github.com/user-attachments/assets/bf87c022-6cac-4776-8828-4926818f42af" width="850"/> <br>
**Redis 도입 후 TPS 측정 결과**


## 4 계층 아키텍처
비즈니스 로직을 더 세분화하고, 관심사 분리를 강화하기 위해 4 계층 아키텍처 적용 <br><br>
![image](https://github.com/user-attachments/assets/4ecc84e2-251f-412c-8e89-931f1616c678) <br>

### Interfaces Layer
- 요청을 전달받고 요청을 수행하기 위해 작업을 하위 계층에 위임
- 응답 전달

### Application Layer
- 하나의 요구사항을 수행하기 위해 필요한 작은 단위의 작업들을 정의하고 하위 계층에 작업 위임
- 트랜잭션으로 묶여야 하는 도메인 로직이나 트랜잭션으로 묶일 필요는 없지만 애그리게이션이 필요한 로직 정의

### Domain Layer
- 도메인 정의
- 비즈니스 로직 작성

### Infrastructure Layer
- 상위 계층의 작업을 지원하는 기술적 기능 제공
- 도메인 영속화, 메시지 전송 등



## ERD(Entity Relationship Diagram)
![sparta](https://github.com/user-attachments/assets/f1632c2e-0d67-4914-a57b-81a868b307f0)



## API 문서
https://documenter.getpostman.com/view/42556921/2sAYkKHHAj



## 프로젝트 기여자
|문준영|박지영|임규진|이서우|
|:---:|:---:|:---:|:---:|
|[Github](https://github.com/JunYoungMoon)|[Github](https://github.com/jyooung)|[Github](https://github.com/kylim99)|[Github](https://github.com/leeseowoo)
|Hub / Management|Company / Product|Order / Delivery|User / Gateway|
