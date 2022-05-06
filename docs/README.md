## 구현해야할 기능

### 상품 도메인

- [x] Entity
    - [x] 물품명, 가격, 수량 VO로 구현
    - [x] 주문이 들어오면 수량 감소 기능
- [x] Repository
    - [x] CRUD
- [ ] Service
    - [ ] 수량 업데이트
- [x] Controller
    - [x] 관리페이지
    - [x] REST API

### 주문 도메인

- [ ] Entity
    - [ ] Order
        - [ ] 회원 정보 VO
    - [ ] OrderItem
        - [ ] 일급컬렉션?
- [ ] Repository
    - [ ] CRUD
- [ ] Service
    - [ ] 재고보다 주문 개수가 더 많으면 예외
    - [ ] 재고 차감 로직
- [ ] Controller
    - [ ] REST API 
