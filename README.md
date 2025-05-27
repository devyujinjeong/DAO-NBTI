
## 📚 목차

1. [🏆 팀 소개](#1--팀-소개)
2. [📘 프로젝트 기획서](#2--프로젝트-기획서)
3. [🛠️ 기술 스택](#3-%EF%B8%8F-기술-스택)
4. [📁 파일 구조](#4--파일-구조)
5. [📊 산출물](#5--산출물)
    - [📊 WBS](#1--wbs-작업-분배-및-일정)
    - [📄 요구사항 정의](#2--요구사항-정의서)
    - [🔄 플로우 차트 & UML](#3--플로우-차트--uml)
    - [📦 DDD](#4--ddd)
    - [📌 ERD & 테이블 정의서](#5--erd--테이블-정의서)
    - [🧩 기능 정의서](#6--기능-정의서)
    - [📐 시스템 아키텍처 구조도](#7--시스템-아키텍처-구조도)
    - [💻 화면 설계서](#8--화면-설계서)
    - [🧾 테스트 케이스 정의서](#9--테스트-케이스-정의서)
    - [📚 SWAGGER 문서](#10--swagger-문서)
    - [🧪 테스트 케이스 상세](#11--테스트-케이스-상세)
    - [🗄️ 빌드 및 배포](#12-%EF%B8%8F-빌드-및-배포)
6. [🫂 팀원 회고](#6--팀원-회고)

<br>

---

## 1. 🏆 팀 소개

|                              곽진웅                              |                              김여진                              |                              김태인                              |                              김운경                               |                              정유진                              |
|:-------------------------------------------------------------:|:-------------------------------------------------------------:|:-------------------------------------------------------------:|:--------------------------------------------------------------:|:-------------------------------------------------------------:|
| <img src="./assets/images/istj.jpg" width="100" height="100"> | <img src="./assets/images/istj.jpg" width="100" height="100"> | <img src="./assets/images/isfj.png" width="100" height="100"> | <img src="./assets/images/isfp.webp" width="100" height="100"> | <img src="./assets/images/ESTJ.png" width="100" height="100"> |
|          [mijuckboon](https://github.com/mijuckboon)          |            [meowdule](https://github.com/meowdule)            |           [Taein5415](https://github.com/Taein5415)           |            [splguyjr](https://github.com/splguyjr)             |       [devyujinjeong](https://github.com/devyujinjeong)       |


<br>

---

## 2. 📘 프로젝트 기획서

# 🧠 NBTI

**National Brain Training Interface**  
*생각의 힘을 키우는 두뇌 트레이닝 플랫폼*

| <img src="./assets/images/main.png"> | <img src="./assets/images/problem.png"> |
|--------------------------------------|-----------------------------------------|
| 메인 화면                                | 문제 풀이 화면                                |

<br>


<details>
<summary> &emsp;1️⃣ 프로젝트 개요</summary>

### 📌 서비스명 / 슬로건

- **서비스명**: NBTI (National Brain Training Interface)
- **슬로건**: *생각의 힘을 키우는 두뇌 트레이닝 플랫폼*

---

### 📌 한 줄 소개

> NBTI는 현대인의 인지 역량을 쉽고 정확하게 측정하고,  
> 개인화된 루틴 트레이닝을 통해  
> **생각의 힘을 키워가는 데이터 기반 두뇌 성장 플랫폼**입니다.

🎯 웩슬러 지능검사를 대중적으로 재해석하여  
누구나 재미있고 꾸준히 훈련할 수 있는 **지속 가능한 뇌 트레이닝 서비스**입니다.

</details>

<br>

<details>
<summary> &emsp;2️⃣ 개발 배경 및 필요성</summary>

### 🔹 사회적/인지적 배경

#### 📉 기억하지 않아도 되는 시대 → 기억력·사고력의 퇴화

- 전화번호조차 외우지 않고, 모르면 "구글링"
- 이제는 **ChatGPT로 사고 과정 자체를 생략**
- 정보 ‘찾기’는 쉬워졌지만, **사고력은 외주화**

#### 📚 상식이 붕괴된 시대 → 사고력의 중요성 부각

- AI는 정답은 주지만, **문제 해결력은 스스로 키워야 함**
- **자기 주도적 사고 훈련의 필요성**이 점점 커짐

#### 🧠 그래서 왜 두뇌 트레이닝이 필요한가?

- 단기 암기보다 중요한 건 **패턴 인식 / 전략 수립 / 문제 해결력 강화**
- 단순 게임이 아닌, **인지과학 기반의 구조화된 훈련**이 필요
- **AI 시대에 맞는 지적 자기계발 도구**로서의 필요성 대두

---

### 🔹 검사 및 훈련 도구의 현실

- 웩슬러 지능검사는 임상 심리·교육 분야에서 널리 쓰이지만,  
  **시간·비용·전문성 등의 장벽**으로 대중화는 어려움
- 기존 지능 테스트는 대부분 **일회성 진단**에 그침 → 이후 트레이닝으로 이어지지 않음
- 자기계발, 취업 준비, 학습 효율 향상 등  
  다양한 목적에 부합하는 **지속 가능한 뇌 트레이닝 서비스가 부재**

---

### 🔹 실질적 필요성과 확장성

#### 📂 인적성 검사와의 연관성

- **국내 대기업 직무적성검사(GSAT 등)와 유사한 문제 유형** 다수 포함
- 단순 오락이 아닌, **실전 감각을 높이는 검사 경험** 제공 가능

</details>

<br>

<details>
<summary> &emsp;3️⃣ 서비스 목적</summary>

- 검사 → 분석 → 학습 → 성장의 **선순환 시스템** 구축
- **무겁지 않으면서도 과학적인** 두뇌 성장 루틴 제공
- **게임적 재미** + **인지 훈련 효과**의 결합

</details>

<br>

<details>
<summary> &emsp;4️⃣ 시장 배경</summary>

- 1인 가구 증가, 자기계발 트렌드, AI 면접 등  
  **역량 점검과 강화에 대한 수요 급증**
- 기존 검사 서비스는 **피드백 부족 / 반복 학습 부재**
- **지속적인 동기 부여**를 위한 게임화, 데이터 기반 분석, 개인화 추천이 필요

<br>

<details>
<summary> &emsp;🔍 참고 기사</summary>

- 📰 [뇌 발달 막는 스마트폰… 집중력·언어능력 저하](https://www.hani.co.kr/arti/society/society_general/1123349.html)
- 🧠 [AI가 당신의 기억력 떨어뜨린다](https://weekly.chosun.com/news/articleView.html?idxno=39963)
- ⚠️ [기초 없이 AI 너무 의존하면 사고력 저하… 균형 있게 활용해야](https://www.yna.co.kr/view/AKR20250304067200003)
- [중앙일보 관련 기사](https://www.joongang.co.kr/article/25335123)

</details>

</details>

<br>

<details>
<summary> &emsp;5️⃣ 핵심 기능 소개</summary>

### 🧠 사용자 중심 기능

| **기능명** | **설명** |
|-----------|----------|
| 🧪 정식 검사 / 맛보기 검사 | 웩슬러 기반 18문항 검사 (회원), 6문항 체험형 검사 (비회원) |
| 📊 AI 분석 리포트 | 검사 결과에 따라 육각형 그래프 및 강약점 분석 자동 생성 |
| 🔁 학습 모드 | 각 분야 문제 풀이 |
| ✍️ 이의 제기 기능 | 사용자가 문제 오류나 점수에 대해 직접 피드백 가능 |
| 📂 마이페이지 기록 관리 | 검사/학습 내역, 이의제기 내역 등 통합 관리 |
| 🔗 공유 기능 | 검사 결과를 카카오톡/URL로 바로 공유 가능 |

---

### 🛠️ 관리자 기능 (Admin)

| **기능명** | **설명** |
|-----------|----------|
| 🧩 문제 관리 페이지 | 문제 등록/수정/삭제 기능 |
| 🧾 검사 이력 관리 | 검사 풀이 내역 전체 조회 |
| 🧪 학습 내역 관리 | 학습 문제 풀이 내역 전체 조회 |
| 🔎 검색 및 정렬 고도화 | ID 기반 검색, 날짜/이름 정렬 등 고급 필터링 기능 |
| 🗂️ 분야/유형 관리 페이지 | 학습/검사에 사용되는 문제 분류 체계를 직접 관리 가능 |

</details>

<br>

<details>
<summary> &emsp;6️⃣ 기대 효과</summary>

### 👤 사용자 관점

- **정확하고 신뢰도 높은 인지 진단**을 부담 없이 경험 가능
- AI 분석 기반으로 **자신의 강점/약점 시각화 → 학습 동기 유도**
- **누적 기록 및 피드백 시스템**으로 자기계발의 지속성 확보

### 🎓 교육적 관점

- 언어이해, 추론, 기억력 등 **기초 인지 역량 강화**
- 반복 학습을 통해 **문제 해결력 및 전략적 사고 능력 향상**
- 이의제기 기능으로 **사용자 참여형 학습 콘텐츠 강화**

### 💡 플랫폼 관점

- AI 분석, 시각화 리포트, 공유 기능으로 **서비스 확산 유도**
- 관리자 기능 고도화 → **문제 품질과 콘텐츠 신뢰도 향상**

</details>

<br>

<details>
<summary> &emsp;7️⃣ 향후 발전 과제</summary>

### 🔮 기능 고도화 방향

| **분류** | **과제명** | **설명** |
|----------|------------|----------|
| 🧠 AI | AI 오답 추천 고도화 | 유사 오답 패턴 분석 기반 맞춤 추천 |
| 🏆 UX | 챌린지 / 랭킹 시스템 도입 | 사용자 간 경쟁 및 동기 유발 구조 설계 |
| 🧪 콘텐츠 | 트레이닝 프로그램 확장 | 기억력, 시각처리 속도 등 세부 프로그램 강화 |
| 🧰 B2B | 기업/교육기관 대상 확장 | 직무 기반 테스트 제공, 집단 통계 리포트 기능 |
| 🖥️ 관리자 | 문제/유형 관리 고도화 | 문제 유형별 정렬, 자동 문제 생성 보조 시스템 |
| 📱 UI/UX | 반응형 개선 | 모바일 기기에서 학습/검사 편의성 강화 |

</details>

<br>

---

## 3. 🛠️ 기술 스택

### 💻 Backend  
<div>
  <img src="https://img.shields.io/badge/Java-007396?style=flat&logo=Java&logoColor=white" />
  <img src="https://img.shields.io/badge/Gradle-02303A?style=flat&logo=Gradle&logoColor=white" />
  <img src="https://img.shields.io/badge/SpringBoot-6DB33F?style=flat&logo=SpringBoot&logoColor=white" />
  <img src="https://img.shields.io/badge/Spring Security-6DB33F?style=flat&logo=springsecurity&logoColor=white" />
  <img src="https://img.shields.io/badge/JPA-6DB33F?style=flat&logo=Hibernate&logoColor=white" />
  <img src="https://img.shields.io/badge/JWT-000000?style=flat&logo=JSON%20web%20tokens&logoColor=white" />
  <img src="https://img.shields.io/badge/Junit5-25A162?style=flat&logo=JUnit5&logoColor=white" />
  <img src="https://img.shields.io/badge/Swagger-85EA2D?style=flat&logo=swagger&logoColor=black" />
  <img src="https://img.shields.io/badge/ChatGPT-10A37F?style=flat&logo=openai&logoColor=white" />  
  <img src="https://img.shields.io/badge/Kakao API-FFCD00?style=flat&logo=kakao&logoColor=black" />
</div>

### 🎨 Frontend
<div>
  <img src="https://img.shields.io/badge/HTML5-E34F26?style=flat&logo=html5&logoColor=white" />
  <img src="https://img.shields.io/badge/CSS3-1572B6?style=flat&logo=css3&logoColor=white" />
  <img src="https://img.shields.io/badge/JavaScript-F7DF1E?style=flat&logo=javascript&logoColor=black" />
  <img src="https://img.shields.io/badge/Vue.js-4FC08D?style=flat&logo=vue.js&logoColor=white" />
  <img src="https://img.shields.io/badge/Bootstrap-7952B3?style=flat&logo=bootstrap&logoColor=white" />
  <img src="https://img.shields.io/badge/Pinia-FFD300?style=flat&logo=pinia&logoColor=black" />
  <img src="https://img.shields.io/badge/chart.js-FF6384?style=flat&logo=chartdotjs&logoColor=white" />
</div>

### 📦 Database
<div>
  <img src="https://img.shields.io/badge/MariaDB-003545?style=flat&logo=MariaDB&logoColor=white" />
  <img src="https://img.shields.io/badge/Amazon S3-569A31?style=flat&logo=AmazonS3&logoColor=white" />
  <img src="https://img.shields.io/badge/Redis-DC382D?style=flat&logo=redis&logoColor=white" />
</div>

### 🛠️ Tool
<div>
  <img src="https://img.shields.io/badge/Git-F05032?style=flat&logo=Git&logoColor=white" />
  <img src="https://img.shields.io/badge/GitHub-181717?style=flat&logo=GitHub&logoColor=white" />
  <img src="https://img.shields.io/badge/Postman-FF6C37?style=flat&logo=Postman&logoColor=white" />
  <img src="https://img.shields.io/badge/Notion-000000?style=flat&logo=Notion&logoColor=white" />
  <img src="https://img.shields.io/badge/Figma-F24E1E?style=flat&logo=Figma&logoColor=white" />
  <img src="https://img.shields.io/badge/Miro-050038?style=flat&logo=Miro&logoColor=white" />
  <img src="https://img.shields.io/badge/Google Sheets-34A853?style=flat&logo=google-sheets&logoColor=white" />
  <img src="https://img.shields.io/badge/Discord-5865F2?style=flat&logo=discord&logoColor=white" />
</div>

### ⚙️ DevOps
<div>
  <img src="https://img.shields.io/badge/Docker-2496ED?style=flat&logo=docker&logoColor=white" />
  <img src="https://img.shields.io/badge/Kubernetes-326CE5?style=flat&logo=kubernetes&logoColor=white" />
  <img src="https://img.shields.io/badge/Jenkins-D24939?style=flat&logo=jenkins&logoColor=white" />
  <img src="https://img.shields.io/badge/Elasticsearch-005571?style=flat&logo=elasticsearch&logoColor=white" />
  <img src="https://img.shields.io/badge/Logstash-000000?style=flat&logo=logstash&logoColor=white" />
  <img src="https://img.shields.io/badge/Kibana-E8478B?style=flat&logo=kibana&logoColor=white" />
  <img src="https://img.shields.io/badge/Terraform-623CE4?style=flat&logo=terraform&logoColor=white" />
</div>

<br>

---

## 4. 📁 파일 구조

```

📦 be15-4th-DAO-NBTI-devops
├── 📁 .idea
├── 📁 assets
│   ├── 📁 api-docs            # API 문서 자료
│   ├── 📁 images              # 이미지 리소스
│   └── 📁 testcase            # 테스트 데이터
├── 📁 backend                 # 백엔드(Spring Boot)
│   └── 📁 src
│       └── 📁 main
│           ├── 📁 java
│           │   └── 📁 com.example.dao
│           │       ├── 📁 controller
│           │       ├── 📁 service
│           │       └── 📁 repository
│           └── 📁 resources
│               ├── 📄 application.properties 
│               └── 📄 logback.xml
├── 📁 docker
│   └── 📁 logstash           # 로그 수집 설정
│       └── 📄 logstash.conf
├── 📁 frontend               # 프론트엔드(Vue 3)
│   ├── 📁 public
│   ├── 📁 src
│   │   ├── 📁 assets
│   │   ├── 📁 api            # API 모듈
│   │   ├── 📁 components     # 공통 컴포넌트
│   │   ├── 📁 views          # 라우팅 뷰
│   │   └── 📄 main.js
│   └── 📄 package.json
├── 📁 k8s                    # K8s 배포 설정
├── 📁 terraform              # Terraform IaC 설정
├── 📄 docker-compose.yml     # 통합 실행 설정
└── 📄 .gitignore

```

<br>


---

## 5. 📊 산출물

### 1. 📊 WBS (작업 분배 및 일정)

🔗 [WBS 명세서 링크](https://docs.google.com/spreadsheets/d/e/2PACX-1vS2PCFj3vhrSqJrXER0JPMTuIF6BAbb2YIH8Bzl6IEY3B6_UMLni89lNHIcSA1w8R8tclbtuDl1wDe2/pubhtml?gid=1971515931&single=true)

<img src="assets/images/WBS.jpg" alt="wbs" width="550"/>

<br>

---

### 2. 📄 요구사항 정의서

🔗 [요구사항 정의서 링크](https://docs.google.com/spreadsheets/d/e/2PACX-1vS2PCFj3vhrSqJrXER0JPMTuIF6BAbb2YIH8Bzl6IEY3B6_UMLni89lNHIcSA1w8R8tclbtuDl1wDe2/pubhtml?gid=520517258&single=true)

<img src="assets/images/requirements.jpg" alt="requirements" width="550"/>

<br>

---

### 3. 🔄 플로우 차트 & UML

<img src="assets/images/flowchart.png" alt="flowchart" width="450"/>

<br>

<img src="assets/images/UML.png" alt="UML" width="750"/>

<br>

---

### 4. 📦 DDD

🔗 [DDD 링크](https://miro.com/app/board/uXjVIwveahs=/?share_link_id=57339969023)

<img src="assets/images/DDD_1.jpg" alt="DDD"/>

<br>

<img src="assets/images/DDD_2.jpg" alt="DDD" width="550"/>

<br>

---

### 5. 📌 ERD & 테이블 정의서

<img src="assets/images/physical_erd.png" alt="ERD"/>

<br>
<br>


<img src="assets/images/logic_erd.png" alt="ERD" width="550"/>

<br>

🔗 [테이블 정의서 링크](https://docs.google.com/spreadsheets/d/e/2PACX-1vS2PCFj3vhrSqJrXER0JPMTuIF6BAbb2YIH8Bzl6IEY3B6_UMLni89lNHIcSA1w8R8tclbtuDl1wDe2/pubhtml?gid=941831741&single=true)

<br>

---

### 6. 🧩 기능 정의서

🔗 [기능 정의서 링크](https://docs.google.com/spreadsheets/d/e/2PACX-1vS2PCFj3vhrSqJrXER0JPMTuIF6BAbb2YIH8Bzl6IEY3B6_UMLni89lNHIcSA1w8R8tclbtuDl1wDe2/pubhtml?gid=797049621&single=true)

<img src="assets/images/functionalDefinition.jpg" alt="functionalDefinition" width="400"/>

<br>

---

### 7. 📐 시스템 아키텍처 구조도

<img src="assets/images/architecture.png" alt="architecture"/>

<br>

---

### 8. 💻 화면 설계서

🔗 [화면 설계서 (Figma) 링크](https://www.figma.com/design/F3jnZYqEHHwzUqGBeQSRJW/DAO?node-id=0-1&t=pQC5VJ1zsJBlAHc7-1)

<img src="assets/images/figma.png" alt="figma"/>

<br>

---

### 9. 🧾 테스트 케이스 정의서

🔗 [테스트 케이스 정의서 링크](https://docs.google.com/spreadsheets/d/e/2PACX-1vS2PCFj3vhrSqJrXER0JPMTuIF6BAbb2YIH8Bzl6IEY3B6_UMLni89lNHIcSA1w8R8tclbtuDl1wDe2/pubhtml?gid=830549809&single=true)

<img src="assets/images/testcase.jpg" alt="testcase"/>

<br>

---

### 10. 📚 SWAGGER 문서

[swagger_api.pdf](assets/api-docs/Swagger_API.pdf)

<img src="assets/images/swagger.jpg" alt="swagger"/>

<br>

---


### 11. 🧪 테스트 케이스 상세

- 정식 검사 진행 테스트

<img src="assets/testcases/test/test_formal.gif" alt="회원 검사 진행"/>

<br>

<details>
<summary>회원</summary>

- 회원가입 테스트

<img src="assets/testcases/user/user_signup.gif" alt="회원가입"/>

- 비밀번호 찾기 테스트

<img src="assets/testcases/user/user_pwd.gif" alt="비밀번호찾기"/>

- 로그인 테스트

<img src="assets/testcases/user/user_login.gif" alt="로그인"/>

- 로그인 테스트

<img src="assets/testcases/user/user_logout.gif" alt="로그아웃"/>

- 회원 탈퇴 테스트

<img src="assets/testcases/user/user_withdraw.gif" alt="회원 탈퇴"/>

</details>

<br>

<details>
<summary>검사</summary>

- 맛보기 검사 진행 테스트

<img src="assets/testcases/test/test_informal.gif" alt="비회원 검사 진행"/>

- 정식 검사 진행 테스트

<img src="assets/testcases/test/test_formal.gif" alt="회원 검사 진행"/>

</details>

<br>

<details>
<summary>학습</summary>

- 학습 진행 테스트  

<img src="assets/testcases/study/study_01.gif" alt="학습 진행"/>


- 학습 결과 조회 테스트

<img src="assets/testcases/study/study_02.gif" alt="학습 결과"/>

</details>

<br>

<details>
<summary>마이페이지</summary>

<br>

<details>
<summary> &emsp;검사 결과</summary>

- 검사 결과 목록 조회 테스트  

<img src="assets/testcases/mypage/mypage_test_01.gif" alt="검사 결과 목록 조회"/>

- 검사 결과 상세 조회 테스트

<img src="assets/testcases/mypage/mypage_test_02.gif" alt="검사 결과 상세"/>

</details>

<br>

<details>
<summary> &emsp;학습 결과</summary>

- 학습 결과 목록 조회 테스트

<img src="assets/testcases/mypage/mypage_study_01.gif" alt="학습 결과 목록 조회"/>

- 학습 결과 상세 조회 테스트

<img src="assets/testcases/mypage/mypage_study_02.gif" alt="학습 결과 상세 조회"/>

</details>

<br>

<details>
<summary> &emsp;이의 제기</summary>

- 이의 제기 신청 테스트

<img src="assets/testcases/mypage/mypage_obj_01.gif" alt="이의 제기 신청"/>

- 이의 제기 내역 조회 테스트

<img src="assets/testcases/mypage/mypage_obj_02.gif" alt="이의 제기 내역 조회"/>

- 이의 제기 상세 조회 테스트

<img src="assets/testcases/mypage/mypage_obj_03.gif" alt="이의 제기 상세 조회"/>

</details>

<br>

<details>
<summary> &emsp;회원 정보</summary>

- 회원 정보 조회 테스트

<img src="assets/testcases/mypage/mypage_userinfo.png" alt="회원 정보 조회"/>

</details>

<br>

</details>

<br>

<details>
<summary>관리자 </summary>

<details>
<summary> &emsp;회원 관리 </summary>

- 회원 관리 테스트  

<img src="assets/testcases/admin/admin_user.gif" alt="회원 관리"/>

- 검사 결과 관리 테스트

<img src="assets/testcases/admin/admin_test.gif" alt="검사 결과 관리"/>

- 학습 결과 관리 테스트

<img src="assets/testcases/admin/admin_study.gif" alt="학습 결과 관리"/>

</details>

<br>

<details>
<summary> &emsp;문제 관리 </summary>

- 문제 목록 조회 테스트

<img src="assets/testcases/admin/admin_problem_list.gif" alt="문제 목록 조회"/>

- 문제 등록 테스트

<img src="assets/testcases/admin/admin_problem_create.gif" alt="문제 등록"/>

- 문제 상세 조회 테스트

<img src="assets/testcases/admin/admin_problem_details.gif" alt="문제 상세 조회"/>

- 문제 수정 테스트

<img src="assets/testcases/admin/admin_problem_update.gif" alt="문제 수정"/>

- 문제 삭제 테스트

<img src="assets/testcases/admin/admin_problem_delete.gif" alt="문제 삭제"/>

</details>

<br>

<details>
<summary> &emsp;이의 제기 관리 </summary>

- 이의 제기 목록 조회 테스트

<img src="assets/testcases/admin/admin_objection_list.gif" alt="이의 제기 목록 조회"/>

- 이의 제기 상세 조회 테스트

<img src="assets/testcases/admin/admin_objection_details.gif" alt="이의 제기 상세 조회"/>

- 이의 제기 처리 테스트

<img src="assets/testcases/admin/admin_objection_update.gif" alt="이의 제기 처리"/>

</details>

</details>



<br>

---

### 12. 🗄️ 빌드 및 배포


<details>
<summary>1. Docker File</summary>

<details>
<summary> &emsp;backend Dockerfile</summary>

  ```dockerfile
FROM openjdk:17-alpine
WORKDIR /app
ARG JAR_FILE=build/libs/*.jar

COPY ${JAR_FILE} app.jar

ENTRYPOINT ["java","-jar","/app/app.jar"]
```
</details>

<details>
<summary> &emsp;backend Docker-Compose</summary>

  ```yml
services:
  db:
    image: mariadb
    container_name: mariadb
    environment:
      - MYSQL_DATABASE=${MYSQL_DATABASE}
      - MYSQL_USER=${MYSQL_USER}
      - MYSQL_PASSWORD=${MYSQL_PASSWORD}
      - MYSQL_ROOT_PASSWORD=${MYSQL_ROOT_PASSWORD}
    ports:
      - "${DB_PORT}:3306"

  redis:
    image: redis
    container_name: redis
    ports:
      - "6379:6379"

  elasticsearch:
    image: docker.elastic.co/elasticsearch/elasticsearch:7.17.0
    container_name: elasticsearch
    environment:
      - discovery.type=single-node
      - ES_JAVA_OPTS=-Xms512m -Xmx512m
    ports:
      - "9200:9200"

  logstash:
    image: docker.elastic.co/logstash/logstash:7.17.0
    container_name: logstash
    volumes:
      - ./docker/logstash/logstash.conf:/usr/share/logstash/pipeline/logstash.conf
    ports:
      - "5000:5000"
    depends_on:
      - elasticsearch

  kibana:
    image: docker.elastic.co/kibana/kibana:7.17.0
    container_name: kibana
    ports:
      - "5601:5601"
    depends_on:
      - elasticsearch
```
</details>

<details>
<summary> &emsp;frontend Dockerfile</summary>

  ```dockerfile
FROM node:lts-alpine AS build-stage
WORKDIR /app
COPY package*.json ./
RUN npm install
COPY . .
RUN npm run build

FROM nginx:stable-alpine AS production-stage
COPY --from=build-stage /app/dist /usr/share/nginx/html

COPY ./nginx.conf /etc/nginx/conf.d/default.conf
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
```
</details>

<details>
<summary> &emsp;logstash.conf</summary>

  ```dockerfile
input {
  tcp {
    port => 5000
    codec => json
  }
}

output {
  elasticsearch {
    hosts => ["elasticsearch:9200"]
    index => "nbti-%{+YYYY.MM.dd}"
  }
}
```
</details>

</details>

<br>

<details>
<summary>2. Kubernetes manifest</summary>

<details>
<summary> &emsp;ingress</summary>

  ```yml
apiVersion: networking.k8s.io/v1
kind: Ingress
metadata:
  name: ingress
  annotations:
    nginx.ingress.kubernetes.io/ssl-redirect: "false"
    nginx.ingress.kubernetes.io/rewrite-target: /$2
spec:
  ingressClassName: nginx
  rules:
    - http:
        paths:
          - path: /()(.*)
            pathType: ImplementationSpecific
            backend:
              service:
                name: frontend
                port:
                  number: 8000
          - path: /boot(/|$)(.*)
            pathType: ImplementationSpecific
            backend:
              service:
                name: backend
                port:
                  number: 8001
---
apiVersion: networking.k8s.io/v1
kind: Ingress
metadata:
  name: kibana-ingress
  annotations:
    nginx.ingress.kubernetes.io/ssl-redirect: "false"
spec:
  ingressClassName: nginx
  rules:
    - http:
        paths:
          - path: /kibana
            pathType: Prefix
            backend:
              service:
                name: kibana
                port:
                  number: 5601
```
</details>

<details>
<summary> &emsp;backend</summary>

  ```yml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: backend
spec:
  replicas: 3
  selector:
    matchLabels:
      app: backend
  template:
    metadata:
      labels:
        app: backend
    spec:
      containers:
        - name: backend
          image: splguyjr/backend
          ports:
            - containerPort: 8080
          readinessProbe:
            httpGet:
              path: /actuator/health/readiness
              port: 8080
            initialDelaySeconds: 10
            periodSeconds: 5
          envFrom:
            - configMapRef:
                name: service-config
            - secretRef:
                name: db-secret
            - secretRef:
                name: redis-secret
            - secretRef:
                name: jwt-secret
            - secretRef:
                name: gpt-secret
            - secretRef:
                name: s3-secret
---
apiVersion: v1
kind: Service
metadata:
  name: backend
spec:
  selector:
    app: backend
  ports:
    - port: 8001
      targetPort: 8080
```
</details>

<details>
<summary> &emsp;configmap</summary>

  ```yml
apiVersion: v1
kind: ConfigMap
metadata:
  name: service-config
data:
  MYSQL_DATABASE: "nbti"
  DB_PORT: "3308"
  DB_URL: "jdbc:mariadb://db:3306/nbti"

  REDIS_HOST: "redis"
  REDIS_PORT: "6379"

  JWT_EXPIRATION: "18000000"
  JWT_REFRESH_EXPIRATION: "604800000"

  env.js: |
    window.env = {
        VITE_API_BASE_URL: "/boot"
    }
---
apiVersion: v1
kind: ConfigMap
metadata:
  name: logstash-config
data:
  logstash.conf: |
    input {
      tcp {
        host => "0.0.0.0"
        port => 5000
        codec => json_lines
      }
    }
    output {
      elasticsearch {
        hosts => ["http://elasticsearch:9200"]
        index => "nbti-%{+YYYY.MM.dd}"
        ilm_enabled => false
      }
    }
---
apiVersion: v1
kind: ConfigMap
metadata:
  name: kibana-config
data:
  kibana.yml: |
    server.host: "0.0.0.0"
    server.basePath: "/kibana"
    server.rewriteBasePath: true
    elasticsearch.hosts: [ "http://elasticsearch:9200" ]
```
</details>

<details>
<summary> &emsp;frontend</summary>

  ```yml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: frontend
spec:
  replicas: 2
  selector:
    matchLabels:
      app: frontend
  template:
    metadata:
      labels:
        app: frontend
    spec:
      containers:
        - name: frontend
          image: splguyjr/frontend
          ports:
            - containerPort: 80
          volumeMounts:
            - name: env-volume
              mountPath: /usr/share/nginx/html/env.js
              subPath: env.js
      volumes:
        - name: env-volume
          configMap:
            name: service-config
---
apiVersion: v1
kind: Service
metadata:
  name: frontend
spec:
  selector:
    app: frontend
  ports:
    - port: 8000
      targetPort: 80
```
</details>

<details>
<summary> &emsp;db</summary>

  ```yml
apiVersion: v1
kind: PersistentVolumeClaim
metadata:
  name: db-pvc
spec:
  accessModes:
    - ReadWriteOnce
  resources:
    requests:
      storage: 1Gi
---
apiVersion: apps/v1
kind: Deployment
metadata:
  name: db
spec:
  replicas: 1
  selector:
    matchLabels:
      app: db
  template:
    metadata:
      labels:
        app: db
    spec:
      containers:
        - name: db
          image: mariadb
          ports:
            - containerPort: 3306
          envFrom:
            - configMapRef:
                name: service-config
            - secretRef:
                name: db-secret
          volumeMounts:
            - name: db-storage
              mountPath: /var/lib/mysql
      volumes:
        - name: db-storage
          persistentVolumeClaim:
            claimName: db-pvc
---
apiVersion: v1
kind: Service
metadata:
  name: db
spec:
  selector:
    app: db
  ports:
    - port: 3306
      targetPort: 3306
```
</details>

<details>
<summary> &emsp;logstash</summary>

  ```yml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: logstash
spec:
  replicas: 1
  selector:
    matchLabels:
      app: logstash
  template:
    metadata:
      labels:
        app: logstash
    spec:
      containers:
        - name: logstash
          image: docker.elastic.co/logstash/logstash:7.17.0
          ports:
            - containerPort: 5000
          #          readinessProbe:
          #            tcpSocket:
          #              port: 5000
          #            initialDelaySeconds: 30
          #            periodSeconds: 5
          #            failureThreshold: 10
          volumeMounts:
            - name: logstash-storage
              mountPath: /usr/share/logstash/pipeline/
      volumes:
        - name: logstash-storage
          configMap:
            name: logstash-config
---
apiVersion: v1
kind: Service
metadata:
  name: logstash
spec:
  selector:
    app: logstash
  ports:
    - port: 5000
      targetPort: 5000
```
</details>

<details>
<summary> &emsp;redis</summary>

  ```yml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: redis
spec:
  replicas: 1
  selector:
    matchLabels:
      app: redis
  template:
    metadata:
      labels:
        app: redis
    spec:
      containers:
        - name: redis
          image: redis
          ports:
            - containerPort: 6379
          envFrom:
            - secretRef:
                name: redis-secret
---
apiVersion: v1
kind: Service
metadata:
  name: redis
spec:
  selector:
    app: redis
  ports:
    - port: 6379
      targetPort: 6379
```
</details>

<details>
<summary> &emsp;elasticsearch</summary>

  ```yml
apiVersion: v1
kind: PersistentVolumeClaim
metadata:
  name: es-pvc
spec:
  accessModes:
    - ReadWriteOnce
  resources:
    requests:
      storage: 1Gi
---
apiVersion: apps/v1
kind: Deployment
metadata:
  name: elasticsearch
spec:
  replicas: 1
  selector:
    matchLabels:
      app: elasticsearch
  template:
    metadata:
      labels:
        app: elasticsearch
    spec:
      containers:
        - name: elasticsearch
          image: docker.elastic.co/elasticsearch/elasticsearch:7.17.0
          ports:
            - containerPort: 9200
          env:
            - name: discovery.type
              value: single-node
            - name: ES_JAVA_OPTS
              value: -Xms512m -Xmx512m
          volumeMounts:
            - name: es-storage
              mountPath: /usr/share/elasticsearch/data
      volumes:
        - name: es-storage
          persistentVolumeClaim:
            claimName: es-pvc
---
apiVersion: v1
kind: Service
metadata:
  name: elasticsearch
spec:
  selector:
    app: elasticsearch
  ports:
    - port: 9200
      targetPort: 9200
```
</details>

<details>
<summary> &emsp;kibana</summary>

  ```yml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: kibana
spec:
  replicas: 1
  selector:
    matchLabels:
      app: kibana
  template:
    metadata:
      labels:
        app: kibana
    spec:
      containers:
        - name: kibana
          image: docker.elastic.co/kibana/kibana:7.17.0
          ports:
            - containerPort: 5601
          volumeMounts:
            - name: kibana-config
              mountPath: /usr/share/kibana/config/
      volumes:
        - name: kibana-config
          configMap:
            name: kibana-config
---
apiVersion: v1
kind: Service
metadata:
  name: kibana
spec:
  selector:
    app: kibana
  ports:
    - port: 5601
      targetPort: 5601
```
</details>

</details>

<br>

<details>
<summary>3. Jenkins Pipeline Script</summary>

<details>
<summary> &emsp;backend</summary>

  ```pipeline script
pipeline {
    agent any

    tools {
        gradle 'gradle'
        jdk 'openJDK17'
    }

    environment {
        DOCKERHUB_CREDENTIALS = credentials('DOCKERHUB_PASSWORD')
        SOURCE_GITHUB_URL = 'https://github.com/be15-4th-project/be15-4th-DAO-NBTI-backend'
        MANIFESTS_GITHUB_URL = 'https://github.com/splguyjr/k8s_manifests'
        GIT_USERNAME = 'splguyjr'
        GIT_EMAIL = 'splguyjr@naver.com'
    }

    stages {
        stage('Preparation') {
            steps {
                script {
                    if (isUnix()) {
                        sh 'docker --version'
                    } else {
                        bat 'docker --version'
                    }
                }
            }
        }

        stage('Clone Source') {
            steps {
                git branch: 'main', url: "${env.SOURCE_GITHUB_URL}"
            }
        }

       stage('Start Containers') {
    steps {
        withCredentials([file(credentialsId: 'env_backend', variable: 'ENV_FILE')]) {
            script {
                if (isUnix()) {
                    sh "docker-compose --env-file=${ENV_FILE} up -d"
                    sh "sleep 30"
                } else {
                    bat "docker-compose --env-file=%ENV_FILE% up -d"
                    bat "ping -n 31 127.0.0.1 > nul"
                }
            }
        }
    }
}


       stage('Source Build') {
    steps {
        withCredentials([file(credentialsId: 'env_backend_1', variable: 'ENV_FILE')]) {
            script {
                def envVars = readFile(ENV_FILE).split("\n")
                    .findAll { it.trim() && !it.startsWith('#') }
                    .collect { it.trim() }

                withEnv(envVars) {
                    if (isUnix()) {
                        sh "chmod +x NBTI/gradlew"
                        sh "cd NBTI && ./gradlew clean build"
                    } else {
                        bat "cd NBTI && gradlew.bat clean build"
                    }
                }
            }
        }
    }
}


        stage('Run Tests') {
            steps {
                withCredentials([file(credentialsId: 'env_backend_1', variable: 'ENV_FILE')]) {
                    script {
                        def envVars = readFile(ENV_FILE).split("\n")
                            .findAll { it.trim() && !it.startsWith('#') }
                            .collect { it.trim() }
        
                        withEnv(envVars) {
                            if (isUnix()) {
                                sh "cd NBTI && ./gradlew test"
                            } else {
                                bat "cd NBTI && gradlew.bat test"
                            }
                        }
                    }
                }
            }
            post {
                always {
                    junit 'NBTI/build/test-results/test/*.xml'
                }
            }
        }
        
        stage('Stop Containers') {
            steps {
                withCredentials([file(credentialsId: 'env_backend', variable: 'ENV_FILE')]) {
                    script {
                        if (isUnix()) {
                            sh "docker-compose --env-file=${ENV_FILE} down || true"
                        } else {
                            bat "docker-compose --env-file=%ENV_FILE% down || exit 0"
                        }
                    }
                }
            }
        }

        
        stage('Docker Build and Push') {
            steps {
                script {
                    withCredentials([usernamePassword(credentialsId: 'DOCKERHUB_PASSWORD', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
                        if (isUnix()) {
                            sh """
                                docker build -t ${DOCKER_USER}/backend:${currentBuild.number} ./NBTI
                                docker tag ${DOCKER_USER}/backend:${currentBuild.number} ${DOCKER_USER}/backend:latest
                                echo ${DOCKER_PASS} | docker login -u ${DOCKER_USER} --password-stdin
                                docker push ${DOCKER_USER}/backend:${currentBuild.number}
                                docker push ${DOCKER_USER}/backend:latest
                                docker logout
                            """
                        } else {
                            bat """
                                docker build -t %DOCKER_USER%/backend:${currentBuild.number} NBTI
                                docker tag %DOCKER_USER%/backend:${currentBuild.number} %DOCKER_USER%/backend:latest
                                docker login -u %DOCKER_USER% -p %DOCKER_PASS%
                                docker push %DOCKER_USER%/backend:${currentBuild.number}
                                docker push %DOCKER_USER%/backend:latest
                                docker logout
                            """
                        }
                    }
                }
            }
        }


        stage('K8S Manifest Update') {
            steps {
                git credentialsId: 'github',
                    url: "${env.MANIFESTS_GITHUB_URL}",
                    branch: 'main'

                script {
                    if (isUnix()) {
                        sh """
                            sed -i 's|image: splguyjr/backend.*|image: splguyjr/backend:${currentBuild.number}|' backend.yml
                            git add backend.yml
                            git config --global user.name '${env.GIT_USERNAME}'
                            git config --global user.email '${env.GIT_EMAIL}'
                            git commit -m '[UPDATE] ${currentBuild.number} image versioning' || echo 'No changes to commit'
                            git push -u origin main
                        """
                    } else {
                        bat "git remote -v"
                        bat """
                            powershell -Command "(Get-Content backend.yml) -replace 'image: splguyjr/backend.*', 'image: splguyjr/backend:${currentBuild.number}' | Set-Content backend.yml"
                            git add backend.yml
                            git config --global user.name '${env.GIT_USERNAME}'
                            git config --global user.email '${env.GIT_EMAIL}'
                            git commit -m \"[UPDATE] ${currentBuild.number} image versioning\" || echo 'No changes to commit'
                            git push -u origin main
                        """
                    }
                }
            }
        }
    }

    post {
        success {
            withCredentials([string(credentialsId: 'discord_be', variable: 'DISCORD')]) {
                discordSend(
                    description: """
                    **빌드 성공!** :tada:

                    **제목**: ${currentBuild.displayName}
                    **결과**: :white_check_mark: ${currentBuild.currentResult}
                    **실행 시간**: ${currentBuild.duration / 1000}s
                    **링크**: [빌드 결과 보기](${env.BUILD_URL})
                    """,
                    title: "${env.JOB_NAME} 빌드 성공!",
                    webhookURL: "$DISCORD"
                )
            }
        }
        failure {
            withCredentials([string(credentialsId: 'discord_be', variable: 'DISCORD')]) {
                discordSend(
                    description: """
                    **빌드 실패!** :x:

                    **제목**: ${currentBuild.displayName}
                    **결과**: :x: ${currentBuild.currentResult}
                    **실행 시간**: ${currentBuild.duration / 1000}s
                    **링크**: [빌드 결과 보기](${env.BUILD_URL})
                    """,
                    title: "${env.JOB_NAME} 빌드 실패!",
                    webhookURL: "$DISCORD"
                )
            }
        }
    }
}

```

</details>

<details>
<summary> &emsp;frontend</summary>

  ```pipeline script
pipeline {
    agent any

    environment {
        DOCKERHUB_CREDENTIALS = credentials('DOCKERHUB_PASSWORD')
        SOURCE_GITHUB_URL = 'https://github.com/be15-4th-project/be15-4th-DAO-NBTI-front'
        MANIFESTS_GITHUB_URL = 'https://github.com/splguyjr/k8s_manifests'
        GIT_USERNAME = 'splguyjr'
        GIT_EMAIL = 'splguyjr@naver.com'
    }

    stages {
        stage('Preparation') {
            steps {
                script {
                    if (isUnix()) {
                        sh 'docker --version'
                    } else {
                        bat 'docker --version'
                    }
                }
            }
        }

        stage('Clone Source') {
            steps {
                git branch: 'main', url: "${env.SOURCE_GITHUB_URL}"
            }
        }

        stage('Frontend Build') {
            steps {
                script {
                    if (isUnix()) {
                        sh """
                            cd be15-4th-dao-nbti-fe
                            npm ci
                            npm run build
                        """
                    } else {
                        bat """
                            cd be15-4th-dao-nbti-fe
                            npm ci
                            npm run build
                        """
                    }
                }
            }
        }

        stage('Docker Build and Push') {
            steps {
                script {
                    withCredentials([usernamePassword(credentialsId: 'DOCKERHUB_PASSWORD', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
                        if (isUnix()) {
                            sh """
                                docker build -t ${DOCKER_USER}/frontend:${currentBuild.number} ./be15-4th-dao-nbti-fe
                                docker tag ${DOCKER_USER}/frontend:${currentBuild.number} ${DOCKER_USER}/frontend:latest
                                echo ${DOCKER_PASS} | docker login -u ${DOCKER_USER} --password-stdin
                                docker push ${DOCKER_USER}/frontend:${currentBuild.number}
                                docker push ${DOCKER_USER}/frontend:latest
                                docker logout
                            """
                        } else {
                            bat """
                                docker build -t %DOCKER_USER%/frontend:${currentBuild.number} be15-4th-dao-nbti-fe
                                docker tag %DOCKER_USER%/frontend:${currentBuild.number} %DOCKER_USER%/frontend:latest
                                docker login -u %DOCKER_USER% -p %DOCKER_PASS%
                                docker push %DOCKER_USER%/frontend:${currentBuild.number}
                                docker push %DOCKER_USER%/frontend:latest
                                docker logout
                            """
                        }
                    }
                }
            }
        }

        stage('K8S Manifest Update') {
            steps {
                git credentialsId: 'github',
                    url: "${env.MANIFESTS_GITHUB_URL}",
                    branch: 'main'

                script {
                    if (isUnix()) {
                        sh """
                            sed -i 's|image: splguyjr/frontend.*|image: splguyjr/frontend:${currentBuild.number}|' frontend.yml
                            git add frontend.yml
                            git config --global user.name '${env.GIT_USERNAME}'
                            git config --global user.email '${env.GIT_EMAIL}'
                            git commit -m '[UPDATE] ${currentBuild.number} image versioning' || echo 'No changes to commit'
                            git push -u origin main
                        """
                    } else {
                        bat "git remote -v"
                        bat """
                            powershell -Command "(Get-Content frontend.yml) -replace 'image: splguyjr/frontend.*', 'image: splguyjr/frontend:${currentBuild.number}' | Set-Content frontend.yml"
                            git add frontend.yml
                            git config --global user.name '${env.GIT_USERNAME}'
                            git config --global user.email '${env.GIT_EMAIL}'
                            git commit -m \"[UPDATE] ${currentBuild.number} image versioning\" || echo 'No changes to commit'
                            git push -u origin main
                        """
                    }
                }
            }
        }
    }

    post {
        success {
            withCredentials([string(credentialsId: 'discord_be', variable: 'DISCORD')]) {
                discordSend(
                    description: """
                    **프론트엔드 빌드 성공!** :sparkles:

                    **제목**: ${currentBuild.displayName}
                    **결과**: ✅ ${currentBuild.currentResult}
                    **시간**: ${currentBuild.duration / 1000}s
                    **[빌드 확인](${env.BUILD_URL})**
                    """,
                    title: "Frontend Build Success",
                    webhookURL: "$DISCORD"
                )
            }
        }
        failure {
            withCredentials([string(credentialsId: 'discord_be', variable: 'DISCORD')]) {
                discordSend(
                    description: """
                    **프론트엔드 빌드 실패!** :x:

                    **제목**: ${currentBuild.displayName}
                    **결과**: ❌ ${currentBuild.currentResult}
                    **시간**: ${currentBuild.duration / 1000}s
                    **[빌드 확인](${env.BUILD_URL})**
                    """,
                    title: "Frontend Build Failed",
                    webhookURL: "$DISCORD"
                )
            }
        }
    }
}
```

</details>
</details>


<br>

<details>
<summary>4. Jenkins CI/CD 테스트 결과 화면</summary>

<img src="assets/images/jenkinstest.gif">

</details>

<br>

<br>

<br>

---

## 6. 🫂 팀원 회고



|**곽진웅**|
|------|

> 짧은 기간동안 백엔드, 프론트엔드 개발에 배포까지 완료해야 하는 프로젝트여서 걱정이 많았지만, 무사히 마무리할 수 있었습니다. 이전 프로젝트에서는 맡은 기능을 구현하기 바빠서 코드 리뷰를 충분히 하지 못했지만, 이번에는 PR 리뷰와 개발을 병행하며 진행할 수 있었던 걸 보면 개발 프로세스에 어느 정도 익숙해진 듯합니다.<br> 다른 팀원의 코드를 참고하며 개발하다 보니 유사한 기능이 필요할 때 큰 도움이 되어, 개발 시간을 많이 단축할 수 있었습니다. 이를 통해 코드 리뷰의 중요성을 실감할 수 있었습니다. 또한, 서로가 놓친 부분을 함께 보완하며 완성도를 높여가는 과정을 통해 협업의 중요성 또한 배울 수 있었습니다. <br> 프로젝트 배포 과정에서 예상보다 어려움이 많았는데, 사소한 실수 하나가 배포 실패로 이어지는 것을 경험하였습니다. 수십번의 실패 끝에 배포에 성공할 수 있었는데, 서버 간 연결 및 통신에 대한 이해도가 높아야 함을 알게 되었습니다. <br> 실력 면에서나 성격 면에서나 모두 좋은 팀원들을 만나 즐겁게 프로젝트 진행할 수 있었고, 이어서 함께 할 최종 프로젝트도 기대가 됩니다. 최종 프로젝트에서는 더 많은 부분에 기여할 수 있도록 열심히 임해보려 합니다.

<br>



| **김여진** |
|---------|

> 이번 DevOps 프로젝트는 기획부터 백엔드, 프론트엔드, 그리고 CI/CD까지 모든 과정을 짧은 기간 내에 통합적으로 진행해야 했던 프로젝트였습니다. 그만큼 시간적 여유가 부족했고, 동시에 체력적으로도 부담이 컸던 경험이었습니다.<br> 기획 단계에서는 아이디어 도출부터 세부 기능 정의까지 모든 디테일을 빠르게 정리해야 했는데, 여전히 기획의 방향성을 잡는 부분과 프로젝트의 전체적인 구조를 설계하는 부분은 가장 어렵고 부담스러운 단계였습니다.<br> 또한 백엔드와 프론트엔드 개발에서는 지난 프로젝트들과는 다른 설정과 폴더 구조를 사용하게 되면서 새로운 환경에 적응하는 데 시간이 필요했습니다. 하지만 이전 프로젝트에서 유사한 작업들을 반복적으로 경험했던 덕분에, 놓치기 쉬운 포인트나 사전에 고려해야 할 요소들을 더 명확하게 인지할 수 있었고, 실수 없이 보다 효율적으로 작업을 진행할 수 있었습니다.<br> 특히 이번 프로젝트에서는 산출물 정리 및 문서화 작업을 주도적으로 맡게 되었는데, 예상외로 이 과정에서 코드 작성보다 정리 능력과 문서화 역량이 더 많이 향상된 것을 느낄 수 있었습니다. <br> 또한, 예상치 못하게 가장 큰 난관은 코드 구현이 아니라 ‘문제 데이터 생성’이었습니다. 6개의 인지 분야와 각 분야별 난이도(Level 1~3)에 따라 문제를 기획하고, 시각적 자산(이미지, JSON, SVG 등)을 함께 구성하여 실제 서비스에 적용하는 데 상당한 시간과 반복적인 수정 작업이 요구되었습니다. 이 경험을 통해 기능 구현 외에도 데이터 설계와 콘텐츠 준비가 프로젝트 성공의 핵심 요소임을 깊이 체감하게 되었습니다.<br> 팀원들 모두가 자신의 파트에 최선을 다해 기능을 구현했고, 작업이 끝난 팀원은 다른 팀원의 업무를 자연스럽게 이어받아 도와주는 협업 구조가 자연스럽게 형성되었습니다. 또한, 자신의 역할에만 집중하지 않고 타인의 영역까지 이해하려고 노력하며 전체 프로젝트의 완성도를 높이려는 자세를 갖추게 되었습니다. 덕분에 사소한 버그나 문제들을 빠르게 찾아 수정할 수 있었고, 팀워크의 중요성을 다시금 느낄 수 있는 프로젝트였습니다.

<br>



| **김태인** |
|---------|

> 2주라는 짧은 기간 동안 프로젝트의 기획부터 백엔드, 프론트엔드 개발까지 수행하면서 "이 모든 작업을 기한 내에 마칠 수 있을까?"라는 걱정이 앞섰습니다. 하지만 팀원 모두가 적극적으로 맡은 역할을 수행해 주었고, 서로 긴밀히 협력한 덕분에 정해진 시간 안에 프로젝트를 성공적으로 마무리할 수 있었습니다.<br> 이번 DevOps 프로젝트를 진행하기 전까지는 쿠버네티스와 젠킨스에 대해 이론적인 수준에서만 알고 있었고, 실질적인 감각은 부족했습니다. 수업을 통해 각 요소의 개념과 역할은 이해하고 있었지만, 이를 실제로 어떻게 구성하고 운영하는지는 와닿지 않았습니다.<br> 그러나 프로젝트를 통해 도커, 쿠버네티스, 젠킨스를 직접 다루고, 팀원들과 함께 스크립트를 작성하고 요소를 하나하나 설정해 나가면서 개념적인 이해가 구체적인 기술 역량으로 전환되는 경험을 할 수 있었습니다. 특히 문제 상황에 직면했을 때 팀원들과 함께 해결 방안을 고민하고 시도하는 과정을 통해 DevOps 도구에 대한 실전 감각을 기를 수 있었고, 이는 저에게 큰 성장의 기회가 되었습니다.<br> 또한 프로젝트 기간 동안 세 번의 팀 변경을 겪으면서, 각 팀마다 다른 개발 컨벤션이나 작업 방식이 존재한다는 것을 체감했습니다. 그 과정에서 프로젝트를 수행하는 데 있어 정답은 하나가 아니라는 것을 느꼈고, 다양한 방식 중에서 더 나은 방법을 유연하게 받아들이고 자신의 것으로 만드는 능력이 중요하다는 것을 배웠습니다.<br> 이번 DevOps 프로젝트는 단순한 개발을 넘어, 협업의 중요성과 도구의 실질적인 활용 능력, 그리고 유연한 사고방식을 함께 키울 수 있었던 값진 경험이었습니다.

<br>


| **김운경** |
|---------|

> 이번 DevOps 프로젝트에서 **Docker**, **Jenkins, Kubernetes, Argo CD**를 중심으로 한 인프라 및 배포 자동화 파트를 메인으로 맡아 진행했습니다. <br> 우선, 약 2주라는 짧은 기간 동안 **기획부터 개발, 그리고 배포 파이프라인 구축까지 책임감 있게 완수해준 팀원들에게 감사의 말을 전하고 싶습니다.** <br> 이번 프로젝트는 단순한 기능 개발을 넘어, **서비스에 필요한 문제 데이터를 직접 제작하고 검수하는 작업**까지 포함되어 있어 생각보다 훨씬 많은 시간이 요구됐습니다.  <br>  그럼에도 불구하고, 주어진 시간 안에 모든 작업을 마무리할 수 있었던 것은 각자 맡은 역할에 충실했던 팀원들 덕분이었습니다. <br> 개인적으로는 **Jenkins, Kubernetes, Argo CD 같은 DevOps 자동화 도구들을 실제 프로젝트에 적용해 본 것은 처음이었기 때문에** 많은 도전이 있었습니다. <br> 설정 과정에서 크고 작은 문제들이 계속해서 발생했고, 그때마다 로그를 분석하고 원인을 하나씩 파악하며 해결해 나갔습니다. <br>  **시간이 지날수록 문제를 빠르게 진단하고 대응하는 능력이 눈에 띄게 향상**된 것을 느낄 수 있었고, 인프라와 배포 자동화에 대한 자신감도 함께 키울 수 있었습니다. <br> 짧은 시간이었지만, 실무와 유사한 환경에서 DevOps의 전반적인 흐름을 경험할 수 있었던 값진 프로젝트였습니다.

<br>


| **정유진** |
|---------|

> 처음에는 2주 동안 frontend와 backend부터 배포 까지 진행할 수 있을지에 대해 의문이 있었습니다.  설계를 제외하곤 사실상 개발 기간이 1주일 뿐이었기 때문에 이 모든 기능을 완성할 수 있을까 했지만 다행히 완성할 수 있었습니다. 우선 조원들 모두가 열심히 해준 덕분에 완성할 수 있었고 만족스러운 결과를 얻을 수 있던 것 같습니다. 이 프로젝트를 진행하면서 프론트와 백엔드를 더 잘 이해할 수 있는 기회가 됐고, 데브옵스에 관해서도 배울 수 있는 기회가 됐습니다. 다만, 데브옵스의 경우 아직 쿠버네티스와 젠킨스에 대한 설정과 이해가 부족한 것 같아 이 부분에서는 학습을 더 진행해야 겠다고 생각했습니다. 특히 아직 이 개념들을 완벽하게 이해하고 있지 못한 것 같아서 이런 부분은 더 보완 해야겠다고 생각했습니다. <br> 이번 프로젝트에서 지난번에 사용하지 못했던 ai를 직접 사용해 볼 수 있었어서 재밌었고, 최종 프로젝트 때는 이러한 경험을 바탕으로 더 좋은 결과물을 낼 수 있게 노력할 것입니다. 마지막으로 좋은 팀원들을 만나서 너무 좋고, 마지막 프로젝트 까지 팀원들과 함께 잘 진행해보도록 하겠습니다.

<br>
