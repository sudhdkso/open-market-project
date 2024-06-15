# open-market-project (개인 프로젝트)
> 구매자-판매자가 있는 오픈 마켓 플랫폼

### 목표
<hr/>

1. 프로젝트 완성
2. 테스트 코드를 작성하며 프로젝트 진행하기
3. 테스트커버리지 80%이상

### 기능
1. 🛒 상품 구매 기능
    - 고객은 판매자가 등록한 상품에 대해서 구매를 할 수 있습니다.
    - 이때, 상품의 재고가 있는 경우에만 상품을 구매할 수 있습니다.
    - 상품을 구매할 때 고객의 소지 금액에서 차감됩니다. (소지 금액이 부족한 경우 물건을 살 수 없습니다.)
    - 구매 확정 시 고객은 구매 금액의 2%를 포인트로 받을 수 있습니다.
    - 구매 확정은 주문 상태가 `배송 완료`일 때만 가능합니다.
      - 주문 상태가 `배송 완료`가 되고 하루 뒤 자동으로 주문 확정됩니다.
2. 💵 상품 판매 기능
   - 판매자는 상품을 등록하여 판매할 수 있습니다.
   - 판매자는 상품에 대한 이름, 내용, 금액, 재고를 등록, 수정할 수 있습니다.
      - 판매자는 같은 이름의 상품을 여러개 등록할 수 없습니다.
   - 판매자는 상품을 판매한 경우 판매 금액에 대한 95%를 얻을 수 있습니다.

### 개발 환경 (Backend)
<hr/>

- InteliiJ
- Spring Boot
- gradle
- Spring JPA
- JDK 17.0.9
- H2
- Sonar Cloud

### 개발 기간 (24.01.14 ~ 24.05.24)
<hr/>

| 기간                  |                        |                                                |
|---------------------|------------------------|------------------------------------------------|
| 24.01.14 ~ 24.01.15 | 개발 환경 셋팅               | jUnit, H2, SpringBoot, Spring JPA, Sonar Cloud |
| 24.01.15 ~ 24.01.16 | 개발 환경 테스트              | Sonar Cloud의 동작 테스트                            |
| 24.01.16 ~ 24.02.08 | 고객 회원가입 구현             | -                                              | 
| 24.02.20 ~ 24.02.26 | 고객 로그인 구현              | 세션, LocalThread                                |
| 24.02.26 ~ 24.03.06 | 판매자 회원가입 & 로그인 구현      | 세션, LocalThread                                |
| 24.03.07 ~ 24.03.13 | 상품 CRUD 구현             | -                                              |
| 24.04.05 ~ 24.04.07 | 주문 CRUD 구현             | - |
| 24.04.23 ~ 24.04.30 | 주문 확정 및 자동 주문 확정 기능 구현 | @Scheduler |
| 24.05.03 ~ 24.05.10 | 리뷰 기능 구현 | - |
| 24.05.24 ~ 24.05.24 | 상품 검색 기능 개선 | Paging, Sort 활용해 검색 속도 개선 |

### 🧂 추후
- 코드 리팩토링
- 구매순으로 상품 조회하기
- 추천인 링크 기능 추가
  - 사용자가 타 사용자에게 상품의 링크를 전달하고, 그 링크로 물건을 구입했을 시 사용자에게 일부 포인트가 적립되는 기능


