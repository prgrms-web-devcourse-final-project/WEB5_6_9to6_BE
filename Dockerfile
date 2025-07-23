# 베이스 이미지를 openjdk:21-jdk-slim으로 설정합니다.
FROM openjdk:21-jdk-slim

# 빌드 인자(JAR_FILE)를 선언합니다.
# 이 ARG 선언은 FROM 명령어 뒤에 위치해야 해당 빌드 단계에서 사용할 수 있습니다.
ARG JAR_FILE

# 작업 디렉토리를 /app으로 설정합니다.
WORKDIR /app

# --build-arg로 전달된 JAR_FILE 경로의 파일을 컨테이너의 app.jar로 복사합니다.
# Dockerfile 외부에서 전달된 변수를 사용하기 위해 ${} 문법을 사용합니다.
COPY ${JAR_FILE} app.jar

# 컨테이너가 시작될 때 실행할 명령어를 설정합니다.
ENTRYPOINT ["java", "-jar", "app.jar"]