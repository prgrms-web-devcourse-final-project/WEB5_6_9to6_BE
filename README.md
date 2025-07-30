<div id="top"></div>

<div align='center'>

<h1><b>Studium</b></h1>
<h3><b>Studium(창의력이 참담해서 생각이 안나네요 변경해주시에요)</b></h3>

Notion: [프로젝트 노션 링크](https://)

</div>

<br>

## 0. 목차

1.  [팀원 소개](#1)
2.  [시작 가이드](#2)
3.  [브랜치 및 디렉토리 구조](#3)
4.  [Color 적용 방법](#4)
5.  [커밋 컨벤션](#5)
6.  [Package_yarn](#6)
7.  [기타](#7)

<br >

## <span id="1">🏃 1. 팀원 소개</span>

<div align="center">

| <img src="https://img.shields.io/badge/BE_Team_member-4CAF50" /> | <img src="https://img.shields.io/badge/BE_Team_member-4CAF50" /> | <img src="https://img.shields.io/badge/BE_Team_member-4CAF50" /> | <img src="https://img.shields.io/badge/BE_Team_member-4CAF50" /> | <img src="https://img.shields.io/badge/BE_Team_Leader-0073B7" /> |
| :--------------------------------------------------------------: | :--------------------------------------------------------------: | :--------------------------------------------------------------: | :--------------------------------------------------------------------------: | :-----------------------------------------------------------: |
|      <img src="https://avatars.githubusercontent.com/u/155226507?s=400&v=4" width="120px;" alt=""/>      |       <img src="https://avatars.githubusercontent.com/u/128045455?v=4" width="120px;" alt=""/>      |      <img src="https://avatars.githubusercontent.com/u/203406555?v=4" width="120px;" alt=""/>      |            <img src="https://avatars.githubusercontent.com/u/165629851?v=4" width="120px;" alt=""/>            |    <img src="https://avatars.githubusercontent.com/u/107977530?v=4" width="120px;" alt=""/>     |
|           [김준형](https://github.com/NoviceWyatt19)           |           [이인선](https://github.com/Inseoni)           |                 [최종우](https://github.com/lnvisibledragon)                 |                  [황영준](https://github.com/youngjun222)                 |         [남다빈](https://github.com/namdragonkiller)          |
|                            기능1 설명                            |                            기능2 설명                            |                                  기능3 설명                                  |                                  기능4 설명                                  |                          기능4 설명                           |

</div>

<br>

<!-- Top Button -->
<p style='background: black; width: 32px; height: 32px; border-radius: 50%; display: flex; justify-content: center; align-items: center; margin-left: auto;'><a href="#top" style='color: white; '>▲</a></p>

<br>

## <span id="2">🛠️ 2. 시작 가이드</span>

### 2.1 Installation

```shell
# 1. 클론하기
$ git clone https://github.com/CAREER-For-Me/Career-web.git .

# 2. 의존성 설치하기
$ yarn

# 3. 개발 서버 실행하기
$ yarn dev
```

<br>

### 2.2 브랜치 생성 가이드

① 브랜치 생성

- `feature/`로 시작하고 기능명을 붙이세요.
- e.g., `feature/login`, `feature/activityRecommend`

<br>

② 본인 브랜치에 push 후 `develop` 브랜치에 PR 보낸 후 카톡 남겨주세요.

<br>

③ PR 후 `develop` 브랜치에서 pull

<br>

④ main 브랜치는 건들지 말기

<br>

> 브랜치 활용 명령어

```shell
# 브랜치 생성
git branch feature/<기능명>

# 브랜치 이동
git switch feature/<기능명>

# 생성과 동시에 이동
git switch -c feature/<기능명>

# 브랜치 리스트 확인
git branch

# 브랜치 삭제 (삭제를 하기 위해선 다른 브랜치로 전환 후 삭제하셔야합니다.)
git branch -d <브랜치명>   # 안전 삭제
git branch -D <브랜치명>   # 강제 삭제
```

<br>

<!-- Top Button -->
<p style='background: black; width: 32px; height: 32px; border-radius: 50%; display: flex; justify-content: center; align-items: center; margin-left: auto;'><a href="#top" style='color: white; '>▲</a></p>

<br>

## 3. <span id="3">🗂️ 3. 브랜치 및 디렉토리 구조</span>

> 브랜치

- `main`
- `dev`
- `featuer/login`

<br>

> 디렉토리 구조

```shell

```

<br>

<!-- Top Button -->
<p style='background: black; width: 32px; height: 32px; border-radius: 50%; display: flex; justify-content: center; align-items: center; margin-left: auto;'><a href="#top" style='color: white; '>▲</a></p>

<br>

# 4. <span id="4">🎨 4. Color 적용 방법</span>

`tailwind.config.js` 파일에 프로젝트에서 사용할 색상을 정의해 뒀습니다.

```ts
extend: {
  colors: {
    careerForMe: {
      main: "#6D72FF",
      mainMedium: "#979AED",
      red: "#FF4238",
      redMedium: "#FF8E88",
      gray01: "#F4F4F4",
    },
    gray: {
      dark: "#737373",
      medium: "#B2B2B2",
      light: "#D2D2D2",
    },
  },
}
```

<br>

Tailwind에서 추가한 색상을 아래와 같이 사용할 수 있습니다.

```tsx
<p class="text-careerForMe-main">커리어 포미 색</p>
```

```tsx
<div class="bg-careerForMe-red">배경 색</div>
```

```tsx
<button class="border border-gray-medium">테두리 색</button>
```

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

## <span id="6">⚙️ 6. Package_yarn</span>

다음은 이 프로젝트에서 사용 중인 주요 라이브러리 목록입니다.

이미 설치되어 있으므로, 별도로 추가할 필요는 없습니다.

```shell
yarn create next-app
```

```shell
yarn add axios
```

<br>

<!-- Top Button -->
<p style='background: black; width: 32px; height: 32px; border-radius: 50%; display: flex; justify-content: center; align-items: center; margin-left: auto;'><a href="#top" style='color: white; '>▲</a></p>

<br>

## <span id="7">7. 기타</span>

<br>

<!-- Top Button -->
<p style='background: black; width: 32px; height: 32px; border-radius: 50%; display: flex; justify-content: center; align-items: center; margin-left: auto;'><a href="#top" style='color: white; '>▲</a></p>

<br>



🚀 프로젝트 완성 후Permalink
프로젝트가 완성된 후, 프로젝트를 쉽게 이해하도록 돕기 위한 README.md 작성 가이드입니다.

<div id="top"></div>

<div align='center'>

<h1><b>프로젝트 제목</b></h1>
<h3><b>프로젝트 부제목</b></h3>

🔗 [배포 링크](https://)

<img src="" alt="intro title image"/>

</div>

<br>

## 0. 목차

1. [프로젝트 소개](#1)
2. [팀원 소개](#2)
3. [개발 일정](#3)
4. [기술 스택](#4)
5. [라이브러리 사용 이유](#5)
6. [컨벤션](#6)
7. [브랜치 및 디렉토리 구조](#7)
8. [주요 기능 소개](#8)
9. [상세 담당 업무](#9)
10. [주요 코드 ](#10)
11. [트러블 슈팅](#11)
12. [프로젝트 회고](#12)
13. [시작 가이드](#13)

<br />

## <span id="1">🚩 1. 프로젝트 소개</span>

Notion: [프로젝트 노션 링크](https://)

프로젝트에 대한 전반적인 소개를 여기에 적어주세요.

<br>

<!-- Top Button -->
<p style='background: black; width: 32px; height: 32px; border-radius: 50%; display: flex; justify-content: center; align-items: center; margin-left: auto;'><a href="#top" style='color: white; '>▲</a></p>

<br>

## <span id="2">🏃 2. 팀원 소개</span>

<div align="center">

| <img src="https://img.shields.io/badge/Project_Leader-FF5733" /> | <img src="https://img.shields.io/badge/Tech_Leader-%2300264B" /> | <img src="https://img.shields.io/badge/Documentation_Leader-%2310069F%20" /> | <img src="https://img.shields.io/badge/Design_Leader-blue" /> |
| :--------------------------------------------------------------: | :--------------------------------------------------------------: | :--------------------------------------------------------------------------: | :-----------------------------------------------------------: |
|      <img src="https://github.com/" width="120px;" alt=""/>      |      <img src="https://github.com/" width="120px;" alt=""/>      |            <img src="https://github.com/" width="120px;" alt=""/>            |    <img src="https://github.com/" width="120px;" alt=""/>     |
|           [팀원1 이름](https://github.com/팀원1아이디)           |           [팀원2 이름](https://github.com/팀원2아이디)           |                 [팀원3 이름](https://github.com/팀원3아이디)                 |         [팀원4 이름](https://github.com/팀원4아이디)          |
|                            기능1 설명                            |                            기능2 설명                            |                                  기능3 설명                                  |                          기능4 설명                           |

</div>

<br>

<!-- Top Button -->
<p style='background: black; width: 32px; height: 32px; border-radius: 50%; display: flex; justify-content: center; align-items: center; margin-left: auto;'><a href="#top" style='color: white; '>▲</a></p>

<br>

## <span id="3">📅 3. 개발 일정</span>

> 프로젝트 개발 기간: 202n.00.00 - 202n.00.00 (n일)

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
<!-- | CI/CD | ![GitHub Actions](https://img.shields.io/badge/github_actions-%232671E5.svg?style=for-the-badge&logo=githubactions&logoColor=white) | -->

<br>

<!-- Top Button -->
<p style='background: black; width: 32px; height: 32px; border-radius: 50%; display: flex; justify-content: center; align-items: center; margin-left: auto;'><a href="#top" style='color: white; '>▲</a></p>

<br>

## <span id="5">❓ 5. 라이브러리 사용 이유</span>

각 라이브러리의 사용 이유를 설명해주세요.

> React

<br>

> Redux

<br>

> Axios

<br>

> Styled-Components

<br>

<!-- Top Button -->
<p style='background: black; width: 32px; height: 32px; border-radius: 50%; display: flex; justify-content: center; align-items: center; margin-left: auto;'><a href="#top" style='color: white; '>▲</a></p>

<br>

## <span id="6">🤝 6. 컨벤션</span>

### prettier

```json
{
  "printWidth": 80,
  "tabWidth": 2,
  "singleQuote": true,
  "trailingComma": "all",
  "semi": false
}
```

### 커밋 컨벤션



<br>

<!-- Top Button -->
<p style='background: black; width: 32px; height: 32px; border-radius: 50%; display: flex; justify-content: center; align-items: center; margin-left: auto;'><a href="#top" style='color: white; '>▲</a></p>

<br>

## 7.<span id="7"> 🗂️ 브랜치 및 디렉토리 구조</span>

> 브랜치

- `main`:
- `dev`:
-

<br>

> 디렉토리 구조

```
📂 App
├── 📂 src
│   ├── 📂 components      # 컴포넌트 관련 파일
│   ├── 📂 pages           # 페이지 파일
│   ├── 📂 redux           # Redux 상태 관리 파일
│   ├── 📂 utils           # 유틸리티 파일
│   └── 📄 App.js          # 메인 App 컴포넌트
├── 📂 public
│   ├── 📄 index.html      # HTML 엔트리 파일
│   └── 📄 favicon.ico     # 사이트 아이콘
└── 📄 package.json        # 프로젝트 종속성 및 설정 파일

```

<br>

<!-- Top Button -->
<p style='background: black; width: 32px; height: 32px; border-radius: 50%; display: flex; justify-content: center; align-items: center; margin-left: auto;'><a href="#top" style='color: white; '>▲</a></p>

<br>

## <span id="8">8. 💻 주요 기능 소개</span>

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

## <span id="9">9. 📄 상세 담당 업무</span>

### 1) 팀원1 이름

- **🎨 디자인**

  - 로고 디자인 및 이미지 제작

- **💻 화면 개발**

  - 로그인 화면
  - 검색 화면
  - 채팅 화면

- **🧑‍💻 구현 기능**

  - 로딩 페이지
    - 회원가입 후 로그인 모달이 올라오는 로딩페이지
  - 팔로워 목록 및 팔로워 취소&팔로우
    - 팔로워 목록을 getFollowerList로 서버에 요청하여 리스트 출력

- **♻️ 리팩토링**
  - 관련 설명

### 2) 팀원2 이름

- **🎨 디자인**

  - 전체적인 UI 디자인

- **💻 화면 개발**

  - 공통 헤더 네브바
  - 공통 푸터 네브바
  - 삭제 / 신고 모달창

- **👩‍💻 구현 기능**

  - 라우터 초기 셋팅
  - 게시물 등록
    - 토글 Open, Close에 따라 인풋창 높이 자동 조절
    - api 전송 한계로 인해 한 공간에 저장하여 보낼 수 있게, 데이터를 연산자로 구분하여 한줄로 전송
      이미지 추가 및 삭제 가능
  - 게시글 삭제 / 신고
    - userId를 통해 유저를 구별하여 타인의 경우 신고 기능, 본인일 경우 삭제 기능 구현

- **♻️ 리팩토링**
  - 관련 설명

<br>

<!-- Top Button -->
<p style='background: black; width: 32px; height: 32px; border-radius: 50%; display: flex; justify-content: center; align-items: center; margin-left: auto;'><a href="#top" style='color: white; '>▲</a></p>

<br>

## <span id="10">✨ 10. 주요 코드</span>

<details>
<summary> 주요 코드에 대한 설명을 입력하세요. </summary>

<div>
설명

```jsx

```

</div>
</details>

<br>

<details>
<summary> 주요 코드에 대한 설명을 입력하세요. </summary>

<div>
설명

```jsx

```

</div>
</details>

<br>

<!-- Top Button -->
<p style='background: black; width: 32px; height: 32px; border-radius: 50%; display: flex; justify-content: center; align-items: center; margin-left: auto;'><a href="#top" style='color: white; '>▲</a></p>

<br>

## <span id="11">🚦 11. 트러블 슈팅</span>

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

## <span id="12">12. 📝 프로젝트 회고</span>

프로젝트 진행 후 느낀 점과 개선할 점을 적어주세요. 블로그에 작성하셨다면 블로그 링크를 첨부해주세요.

<br>

<!-- Top Button -->
<p style='background: black; width: 32px; height: 32px; border-radius: 50%; display: flex; justify-content: center; align-items: center; margin-left: auto;'><a href="#top" style='color: white; '>▲</a></p>

<br>


<!-- Top Button -->
<p style='background: black; width: 32px; height: 32px; border-radius: 50%; display: flex; justify-content: center; align-items: center; margin-left: auto;'><a href="#top" style='color: white; '>▲</a></p>
