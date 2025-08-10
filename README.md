<div id="top"></div>

<div align='center'>

<h1>
  <b>Studium</b>
</h1>
<h3>
  <b>Studium(창의력이 참담해서 생각이 안나네요 변경해주시에요)</b>
</h3>

Notion: [프로젝트 노션 링크](https://)

</div>

<br>

## 0. 목차

1.  [팀원 소개](#1)
2. [브랜치 및 디렉토리 구조](#2)
3. [개발 일정](#3)
4. [기술 스택](#4)
5.  [커밋 컨벤션](#5)
6.  [주요 기능 소개](#6)
7.  [트러블 슈팅](#7)
8. [프로젝트 회고](#8)
9. [기타](#9)

<br >

## <span id="1">🏃 1. 팀원 소개</span>

<div align="center">

|                <img src="https://img.shields.io/badge/BE_Team_member-4CAF50" />                |             <img src="https://img.shields.io/badge/BE_Team_member-4CAF50" />             |             <img src="https://img.shields.io/badge/BE_Team_member-4CAF50" />             |             <img src="https://img.shields.io/badge/BE_Team_member-4CAF50" />             |             <img src="https://img.shields.io/badge/BE_Team_Leader-0073B7" />             |
|:----------------------------------------------------------------------------------------------:|:----------------------------------------------------------------------------------------:|:----------------------------------------------------------------------------------------:|:----------------------------------------------------------------------------------------:|:----------------------------------------------------------------------------------------:|
| <img src="https://avatars.githubusercontent.com/u/155226507?s=400&v=4" width="120px;" alt=""/> | <img src="https://avatars.githubusercontent.com/u/128045455?v=4" width="120px;" alt=""/> | <img src="https://avatars.githubusercontent.com/u/203406555?v=4" width="120px;" alt=""/> | <img src="https://avatars.githubusercontent.com/u/165629851?v=4" width="120px;" alt=""/> | <img src="https://avatars.githubusercontent.com/u/107977530?v=4" width="120px;" alt=""/> |
|                            [김준형](https://github.com/NoviceWyatt19)                             |                            [이인선](https://github.com/Inseoni)                             |                        [최종우](https://github.com/lnvisibledragon)                         |                          [황영준](https://github.com/youngjun222)                           |                        [남다빈](https://github.com/namdragonkiller)                         |
|                                        인증/인가, 타이머, 스터디                                         |                                          맴버, 알람                                          |                                       채팅, 리워드 아이템                                        |                                         퀴즈(LLM)                                          |                                         배포, 스터디                                          |

</div>

<br>

<!-- Top Button -->
<p style='background: black; width: 32px; height: 32px; border-radius: 50%; display: flex; justify-content: center; align-items: center; margin-left: auto;'><a href="#top" style='color: white; '>▲</a></p>

<br>

## 2. <span id="2">🗂️ 2. 브랜치 및 디렉토리 구조</span>

> 브랜치

- `main`
- `dev`
- `featuer/login`

<br>

> 디렉토리 구조

```shell
app
├── controller
│   ├── api
│   └── websocket
├── model
│   ├── alarm
│   │   ├── code
│   │   ├── dto
│   │   ├── entity
│   │   ├── repository
│   │   ├── service
│   │   └── sse
│   └── ...
infra
├── auth
├── config
└── ...
Application

```

<br>

<!-- Top Button -->
<p style='background: black; width: 32px; height: 32px; border-radius: 50%; display: flex; justify-content: center; align-items: center; margin-left: auto;'><a href="#top" style='color: white; '>▲</a></p>

<br>

## <span id="3">📅 3. 개발 일정</span>

> 프로젝트 개발 기간: 2025.06.27 - 2025.07.31 (35일)
> 
| 구분               | 기간                   | 활동                        | 비고            |
| ---------------- | -------------------- | ------------------------- | ------------- |
| **사전 기획**        | 06/27(금) \~ 07/01(화) | 프로젝트 기획 및 주제 선정, 기획안 작성   | 아이디어 선정       |
| **ERD 설계**       | 07/01(화) \~ 07/03(목) | ERD 설계, 기능 논의             |               |
| **와이어프레임 및 디자인** | 07/01(화) \~ 07/03(목) | 와이어프레임 작성                 |               |
| **목업 서버 배포**     | 07/04(금) \~ 07/09(수) | 인증 목업 서버 배포               |               |
| **디자인**          | 07/03(목) \~ 07/04(금) | 사용자 시나리오 기반 화면 설계         |               |
| **기능 정의**        | 07/04(금) \~ 07/06(일) | 페이지별 기능 정의, 기능정의서 작성      |               |
| **API 명세 작성**    | 07/04(금) \~ 07/09(수) | API 명세서 작성, 상세 기능 논의      |               |
| **퍼블리싱**         | 07/07(월) \~ 07/10(목) | 디자인 기반 페이지별 퍼블리싱          |               |
| **백엔드 기능 구현**    | 07/04(금) \~ 07/30(수) | 도메인별 기능 구현, 수정사항 반영       | 팀별 중간보고(7/16) |
| **프론트 기능 구현**    | 07/10(목) \~ 07/30(수) | 페이지별 기능 구현, 수정사항 반영       | 팀별 중간보고(7/16) |
| **최종 배포**        | 07/29(화) \~ 07/31(목) | 프론트 Vercel 배포, 백엔드 AWS 배포 | 최적화, 오류 수정    |


<br>

<!-- Top Button -->
<p style='background: black; width: 32px; height: 32px; border-radius: 50%; display: flex; justify-content: center; align-items: center; margin-left: auto;'><a href="#top" style='color: white; '>▲</a></p>

<br>

## <span id="4">📚 4. 기술 스택</span>

| 항목 | 기술 |
| --- | --- |
| 언어 | ![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white) |
| 프레임워크 및 라이브러리 | ![Spring Boot](https://img.shields.io/badge/spring_boot-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white) |
| ORM | ![JPA](https://img.shields.io/badge/JPA-6DB33F?style=for-the-badge) ![QueryDSL](https://img.shields.io/badge/QueryDSL-4A4A4A?style=for-the-badge) |
| 데이터베이스 | ![PostgreSQL](https://img.shields.io/badge/postgresql-%23316192.svg?style=for-the-badge&logo=postgresql&logoColor=white) ![Redis](https://img.shields.io/badge/redis-%23DD0031.svg?style=for-the-badge&logo=redis&logoColor=white) |
| 빌드 툴 | ![Gradle](https://img.shields.io/badge/gradle-02303A.svg?style=for-the-badge&logo=gradle&logoColor=white) |
| API | ![REST API](https://img.shields.io/badge/REST_API-000000?style=for-the-badge) ![Swagger](https://img.shields.io/badge/Swagger-85EA2D?style=for-the-badge&logo=Swagger&logoColor=black) ![Google Cloud](https://img.shields.io/badge/Google_Mail-4285F4?style=for-the-badge&logo=google-cloud&logoColor=white) |
| 인증/인가 | ![JWT](https://img.shields.io/badge/JWT-black?style=for-the-badge&logo=JSON%20web%20tokens) ![OAuth2](https://img.shields.io/badge/OAuth2-24292F?style=for-the-badge&logo=oauth&logoColor=white) ![Spring Security](https://img.shields.io/badge/Spring_Security-6DB33F?style=for-the-badge&logo=spring-security&logoColor=white) |
| 테스트 도구 | ![JUnit5](https://img.shields.io/badge/junit5-25A162?style=for-the-badge&logo=junit5&logoColor=white) ![Mockito](https://img.shields.io/badge/Mockito-373737?style=for-the-badge) ![JMeter](https://img.shields.io/badge/JMeter-D22128?style=for-the-badge&logo=apachejmeter&logoColor=white) |
| 배포 환경 | ![Amazon AWS](https://img.shields.io/badge/AWS-%23232F3E.svg?style=for-the-badge&logo=amazon-aws&logoColor=white) ![Google Cloud](https://img.shields.io/badge/GCP-%234285F4.svg?style=for-the-badge&logo=google-cloud&logoColor=white) ![Amazon EC2](https://img.shields.io/badge/EC2-FF9900?style=for-the-badge&logo=amazon-ec2&logoColor=white) ![Docker](https://img.shields.io/badge/docker-%230db7ed.svg?style=for-the-badge&logo=docker&logoColor=white) |

<br>

<!-- Top Button -->
<p style='background: black; width: 32px; height: 32px; border-radius: 50%; display: flex; justify-content: center; align-items: center; margin-left: auto;'><a href="#top" style='color: white; '>▲</a></p>

<br>

## <span id="5">🤝 5. 커밋 컨벤션</span>

- 커밋 메세지는 소문자 시작, PR 메세지는 대문자 시작으로 적어주세요.

| **타입** | **설명**                                          |
| -------- | ------------------------------------------------- |
| init | 초기설정 |
| feat     | 기능 구현 | 
| fix      | 버그 수정 | 
| rename   | 파일/폴더 이름 변경 및 이동 | 
| refactor | 코드 리팩토링 |
| chore    | 빌드 업무 수정, 패키지 매니저 설정 수정 및 그 외 기타 수정 |
| zap | 기능 수정 |
| docs     | 문서 수정                                         |
| style    | 코드 포맷팅, 세미콜론 누락, 코드 변경이 없는 경우 | 
| test     | 테스트 코드, 리팩토링 테스트 코드 추가            |
| comment | 주석 추가 및 변경 |
| rename | 파일 또는 폴더명을 수정하거나 옮기는 작업만 한 경우|
| remove | 파일을 삭제하는 작업만 수행한 경우 |
| BREAKING CHANGE | 커다란 API 변경의 경우 |
| HOTFIX | 급하게 치명적인 버그를 고쳐야 하는 경우|

<br>

<!-- Top Button -->
<p style='background: black; width: 32px; height: 32px; border-radius: 50%; display: flex; justify-content: center; align-items: center; margin-left: auto;'><a href="#top" style='color: white; '>▲</a></p>

<br>

## <span id="6">6. 💻 주요 기능 소개</span>

프로젝트의 주요 기능을 GIF를 첨부하여 설명해주세요.

### 1) 홈

| - 화면                                            | - 화면                                            | - 화면                                            |
| ------------------------------------------------- | ------------------------------------------------- | ------------------------------------------------- |
| <img src="" alt="-화면" width="288" height="608"> | <img src="" alt="-화면" width="288" height="608"> | <img src="" alt="-화면" width="288" height="608"> |

### 2) 게시글

| 상세페이지 화면                                   | 게시물 작성 - ??                                  | 게시물 작성 - ??                                  |
| ------------------------------------------------- | ------------------------------------------------- | ------------------------------------------------- |
| <img src="" alt="-화면" width="288" height="608"> | <img src="" alt="-화면" width="288" height="608"> | <img src="" alt="-화면" width="288" height="608"> |

### 3) 404 & 로딩 화면

| - 화면                                            | - 화면                                            | - 화면                                            |
| ------------------------------------------------- | ------------------------------------------------- | ------------------------------------------------- |
| <img src="" alt="-화면" width="288" height="608"> | <img src="" alt="-화면" width="288" height="608"> | <img src="" alt="-화면" width="288" height="608"> |

<br>

<!-- Top Button -->
<p style='background: black; width: 32px; height: 32px; border-radius: 50%; display: flex; justify-content: center; align-items: center; margin-left: auto;'><a href="#top" style='color: white; '>▲</a></p>

<br>

## <span id="7">🚦 7. 트러블 슈팅</span>

<details>
<summary> 트러블 슈팅을 입력하세요. </summary>

<div>

1. 문제 상황 <br />

2. 시도 <br />

3. 해결방안 <br />

</div>
</details>

<br>

<!-- Top Button -->
<p style='background: black; width: 32px; height: 32px; border-radius: 50%; display: flex; justify-content: center; align-items: center; margin-left: auto;'><a href="#top" style='color: white; '>▲</a></p>

<br>

## <span id="8">8. 📝 프로젝트 회고</span>

프로젝트 진행 후 느낀 점과 개선할 점을 적어주세요. 블로그에 작성하셨다면 블로그 링크를 첨부해주세요.

<br>

<!-- Top Button -->
<p style='background: black; width: 32px; height: 32px; border-radius: 50%; display: flex; justify-content: center; align-items: center; margin-left: auto;'><a href="#top" style='color: white; '>▲</a></p>

<br>


<!-- Top Button -->
<p style='background: black; width: 32px; height: 32px; border-radius: 50%; display: flex; justify-content: center; align-items: center; margin-left: auto;'><a href="#top" style='color: white; '>▲</a></p>


## <span id="9">9. 기타</span>

<br>

<!-- Top Button -->
<p style='background: black; width: 32px; height: 32px; border-radius: 50%; display: flex; justify-content: center; align-items: center; margin-left: auto;'><a href="#top" style='color: white; '>▲</a></p>

<br>




