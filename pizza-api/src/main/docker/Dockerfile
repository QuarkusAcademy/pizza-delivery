# docker build -f pizza-api/src/main/docker/Dockerfile --no-cache --progress=plain -t caravanacloud/pizza-api:latest .
# docker run pizza-api:latest
# docker login
# docker push caravanacloud/pizza-api:latest

FROM fedora:40

USER root
RUN bash -c "dnf install -y zip unzip"

ARG USERNAME=somebody
ARG USER_UID=1000
ARG USER_GID=$USER_UID
RUN groupadd --gid $USER_GID $USERNAME 
RUN useradd --uid $USER_UID --gid $USER_GID -m $USERNAME -d "/home/$USERNAME"

USER $USERNAME
ARG JAVA_SDK="21.0.2-graal"
RUN curl -s "https://get.sdkman.io" | bash 
RUN bash -c ". $HOME/.sdkman/bin/sdkman-init.sh \
    && sdk install java $JAVA_SDK \
    && sdk default java $JAVA_SDK \
    && sdk install quarkus \
    && sdk install maven \
    "
RUN mkdir -p "/home/$USERNAME/quarkus-app"
WORKDIR "/home/$USERNAME/quarkus-app"
COPY --chown=$USERNAME . .

RUN bash -c ". $HOME/.sdkman/bin/sdkman-init.sh \
    && mvn install -DskipTests"

ENTRYPOINT [ "/home/somebody/.sdkman/candidates/java/current/bin/java", "-jar", "/home/somebody/quarkus-app/pizza-api/target/quarkus-app/quarkus-run.jar" ]
