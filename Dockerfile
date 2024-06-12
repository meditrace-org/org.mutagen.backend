FROM gradle:8.5-jdk17 as builder
WORKDIR /app
COPY --chown=gradle:gradle . /app
RUN gradle build -x test --no-daemon

FROM openjdk:17-jdk-slim

ENV TZ=Europe/Moscow

ARG HOST_USER_UID=1000
ARG HOST_USER_GID=1000

RUN groupadd -g $HOST_USER_GID notroot && \
    useradd -l -u $HOST_USER_UID -g $HOST_USER_GID notroot

RUN apt-get update && apt-get install -y ffmpeg

COPY --from=builder /app/build/libs /usr/src/app/build/libs
COPY . /usr/src/app
WORKDIR /usr/src/app

CMD ["java", "-jar", "build/libs/mutagen-backend.jar"]
