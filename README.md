# Croffle - 보건 의료 용어 사전
Android Studio + 보건 의료 용어 사전
<br>
<br>

## 🖥 프로젝트 소개
의료 종사자 및 의대, 보건 계열 대학생들이 의학 용어를 쉽게 검색하고, 해당 의학 용어의 발음과 함께 공부할 수 있도록 지원 해 주는 것을 목표로 개발하였습니다.


## 🕰 개발 기간
- 2023.04.26 ~ 2023.06.11


### 👤 멤버 구성 
- 팀장: 조성은 - 의학 용어 검색(인덱스, 텍스트 검색), 나만의 단어장 구현
- 조원: 이주영 - 회원가입, 로그인, 마이 페이지 구현
- 조원: 장혜민 - 의학 단어 음성 듣기(tts), 코드 병합
- 조원: 김예희 - 보고서 작성


### ⚙ 개발 환경
- Android Studio(java)
- XAMPP(Apache, MySQL)
- VSCode(php)


## 📌 주요 기능
### ① 로그인
![login](https://github.com/Hwa-A/Croffle/assets/100755494/74728b1b-05a1-4097-a9d3-20457e5719b3)

- 회원가입 화면 전환 버튼
- 아이디, 비밀번호 입력 후 로그인 버튼을 통해 메뉴 화면으로 전환

  
### ② 회원가입
![signup](https://github.com/Hwa-A/Croffle/assets/100755494/0ac28084-7486-47c3-8c65-9fc97342d1e8)

- 닉네임 입력
- 아이디 중복 확인
- 비밀번호 일치 검증(일치하지 않을 경우, '비밀번호가 다릅니다' 메시지 출력)
- 아이디 중복확인을 거치지 않고 회원가입 버튼을 누른 경우, '중복확인을 진행해주세요' 메시지 출력
- 비밀번호 일치 버튼을 누르지 않고 회원가입 버튼을 누른 경우, '비밀번호 확인을 진행해주세요' 메시지 출력
- 아이디, 비밀번호 입력 확인 후 회원가입 버튼을 통해 회원가입과 동시에 로그인 화면으로 전환

  
### ③ 메뉴
![menu](https://github.com/Hwa-A/Croffle/assets/100755494/daec7f01-333a-4090-a071-3229717f0ada)

- 목록 구현(인덱스, 텍스트, 단어장, 마이페이지)


### ④ 단어 검색
![index2](https://github.com/Hwa-A/Croffle/assets/100755494/cd414610-148e-4bfe-80c1-160d534a7945)

- recyclerview를 스크롤 해 검색된 의학 용어 확인
- ☆ 버튼을 눌러 단어장 등록/삭제
- 🔊 버튼을 눌러 의학 용어 발음 듣기
- view를 클릭하여 의학 용어 설명 확인
  
#### ④-1. 인덱스 검색
![index](https://github.com/Hwa-A/Croffle/assets/100755494/fb700a14-bf35-4a2e-91cb-765ba6560508)

- 버튼(A ~ Z / ㄱ ~ ㅎ)을 통해 검색할 용어의 초성을 선택
- 한/영 인덱스에 따른 오름차순 정렬
  (1) 영어 인덱스: 의학 용어의 원어(영어)를 기준
  (2) 한글 인덱스: 의학 용어의 표준어(한글)을 기준

  
#### ④-2. 텍스트 검색
![text](https://github.com/Hwa-A/Croffle/assets/100755494/6793f319-cc2e-4adc-ac74-b468f8545127)

- 공백 입력 후, 검색 버튼 클릭시 '검색어를 입력해주세요' 메시지 출력
- 존재하지 않는 용어 검색 시 '검색 결과 없음' 메시지 출력
- 의학 용어 원어(영어)를 기준으로 오름차순 정렬
  (1) 동일한 용어: 첫 상단에 위치
  (2) 관련 용어: 오름차순 정렬
  

### ⑤ 단어장
![word](https://github.com/Hwa-A/Croffle/assets/100755494/3b0e1a34-384a-499e-b4c5-de519de170a3)

- recyclerview를 스크롤 해 등록된 의학 용어 확인
- ★ 버튼을 눌러 단어장 삭제
- 🔊 버튼을 눌러 의학 용어 발음 듣기
- view를 클릭하여 의학 용어 설명 확인
- 의학 용어 원어(영어)를 기준으로 오름차순 정렬


### ⑥ 마이 페이지
![mypage](https://github.com/Hwa-A/Croffle/assets/100755494/7444062f-34ec-48d2-974c-afb92b477a8a)

- 사용자의 아이디와 함께 '~님의 정보' 출력
- 회원정보 수정, 회원 탈퇴, 로그아웃으로 구성


#### ⑥-1. 회원정보 수정
![user](https://github.com/Hwa-A/Croffle/assets/100755494/257386a1-3f73-47cc-8bcd-1f2c6714cf93)

- 이름, 비밀번호 수정 가능
- 비밀번호의 경우, 일치 여부를 거친 후 수정 가능


#### ⑥-2. 회원 탈퇴
![out](https://github.com/Hwa-A/Croffle/assets/100755494/b52caef0-9687-4328-abcb-2adf28f7fb4f)

- 마이 페이지에서 회원 탈퇴 버튼을 누른 후, 회원 탈퇴와 동시에 자동으로 로그인 화면으로 전환


#### ⑥-3. 로그아웃
![logout](https://github.com/Hwa-A/Croffle/assets/100755494/b4d0929b-7351-4749-93b1-a7df5615a00a)
- 마이 페이지에서 로그아웃 버튼을 누른 후, 로그아웃과 동시에 자동으로 로그인 화면으로 전환
