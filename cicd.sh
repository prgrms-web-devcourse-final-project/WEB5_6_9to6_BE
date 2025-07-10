# !!배포하기전에 이거 실행해야함
# 1. 빌드
.\gradlew.bat build -x test

# 2. JAR 복사
copy .\build\libs\*.jar .\app.jar

# 3. 도커 빌드
docker build -t my-app .

# 4. 실행
docker run -d -p 8080:8080 --name my-app-container my-app
