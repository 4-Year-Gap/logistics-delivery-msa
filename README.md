## About
- 물류 관리 및 배송 시스템을 위한 MSA(Microservices Architecture) 기반 플랫폼입니다.
- MSA의 복잡성을 이해하고, 팀원들과 함께 MSA를 구축하면서 실무에서 발생할 수 있는 다양한 문제들을 간접적으로 경험하고 해결하는 것을 목표로 합니다.
![service](https://github.com/user-attachments/assets/9205843b-9aae-4c0f-9079-ca32675032c5)



## System Archtecture
![sd](https://github.com/user-attachments/assets/291297a9-0298-4e50-9aa7-97208df853e7)



## Tech Stack
<img src="https://img.shields.io/badge/Java-CA6201?style=plastic&logo=OpenJDK&logoColor=white">
<img src="https://img.shields.io/badge/Spring Boot-6DB33F?style=plastic&logo=springboot&logoColor=white">
<img src="https://img.shields.io/badge/Spring Cloud-6DB33F?style=plastic&logo=spring&logoColor=white">
<img src="https://img.shields.io/badge/MySQL-4479A1?style=plastic&logo=MySQL&logoColor=white">
<img src="https://img.shields.io/badge/Redis-FF4438?style=plastic&logo=Redis&logoColor=white">
<img src="https://img.shields.io/badge/Kafka-231F20?style=plastic&logo=apachekafka&logoColor=white">
<img src="https://img.shields.io/badge/Docker-2496ED?style=plastic&logo=Docker&logoColor=white">
<img src="https://img.shields.io/badge/JWT-000000?style=plastic&logo=jsonwebtokens&logoColor=white">



## Troubleshooting
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
  - 확장성 및 유연성 증가
    - 각 서버가 직접 레디스에 접근하여 데이터를 조회하기 때문에 서버 간 의존성이 줄어들고, 시스템 확장성 향상
   
   ![before](https://github.com/user-attachments/assets/a9a5e190-e798-4b40-af1f-6846d4ff6bc9)
   **Before**

   ![after](https://github.com/user-attachments/assets/5c8319a9-0ca9-4cfd-9895-43b5bafd66cd)
   **After**

    Before와 비교했을 때 약 51% 증가, 평균 테스트 시간은 약 32.47% 감소



### 2. 허브 간 최소 이동 경로 관리
![image](https://github.com/user-attachments/assets/e136f253-d7ae-4aec-b61f-09da623414b9)
- **문제**
  - 경로 데이터가 필요할 때마다, 다익스트라로 허브 간 최소 이동 경로 계산 시 평균 TPS 289
  - 허브가 추가로 만들어질수록 TPS 저하 우려
- **해결 방안**
  - 계산된 허브 간 최소 이동 경로 데이터를 레디스 서버에 캐싱
- **결론**
  - 레디스에 캐싱된 데이터 읽기 연산 시 평균 TPS 11,865

![route-b](https://github.com/user-attachments/assets/8de80a4c-7a50-40d5-91db-6d2d811f0896)
**Before**

![route-a](https://github.com/user-attachments/assets/3d92cb6a-3a20-41f8-b7ba-8c7bdad8bce1)
**After**



### 3. 동시성 문제 제어
- **문제**
  - 상품 재고 변경 시 동시성 문제 발생 가능
  - 허브/업체 배송 담당자 추가 또는 지정 시 동시성 문제 발생 가능
- **해결 방안**
- 
- **결론**
- 



## Entity-Relationship Diagram
![sparta](https://github.com/user-attachments/assets/f1632c2e-0d67-4914-a57b-81a868b307f0)



## API Documentation
https://documenter.getpostman.com/view/42556921/2sAYkKHHAj



## Responsibilities by Team Member
|문준영|박지영|임규진|이서우|
|:---:|:---:|:---:|:---:|
|[Github](https://github.com/JunYoungMoon)|[Github](https://github.com/jyooung)|[Github](https://github.com/kylim99)|[Github](https://github.com/leeseowoo)
|Hub/Management|Company/Product|Order/Delivery|User/Gateway|
