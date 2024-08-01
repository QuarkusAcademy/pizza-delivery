# docker build -f pizza-api/src/main/docker/Dockerfile --no-cache --progress=plain -t caravanacloud/pizza-api:latest .
# docker run caravanacloud/pizza-api:latest
# docker images caravanacloud/pizza-api:latest --format "{{.Size}}"
# docker login
# docker push caravanacloud/pizza-api:latest

# Build Stage
FROM fedora:40 as build

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

# Runtime Stage
FROM fedora:40

USER root

ARG USERNAME=somebody
ARG USER_UID=1000
ARG USER_GID=$USER_UID
RUN groupadd --gid $USER_GID $USERNAME 
RUN useradd --uid $USER_UID --gid $USER_GID -m $USERNAME -d "/home/$USERNAME"

# Download GraalVM
ARG GRAALVM_URL="https://github.com/graalvm/graalvm-ce-builds/releases/download/jdk-21.0.2/graalvm-community-jdk-21.0.2_linux-x64_bin.tar.gz"
RUN curl -L $GRAALVM_URL | tar -xz -C /usr/local

USER $USERNAME

# Set JAVA_HOME and add java to PATH
ENV JAVA_HOME=/usr/local/graalvm-community-openjdk-21.0.2+13.1
ENV PATH="$JAVA_HOME/bin:$PATH"

# Copy binaries
ARG CP_FROM="/home/somebody/quarkus-app/pizza-api/target/quarkus-app"
ARG CP_TO="/home/somebody/quarkus-app/pizza-api/target/quarkus-app"
COPY --chown=$USER_UID --from=build $CP_FROM $CP_TO

# Network Settings
EXPOSE 8080

ENTRYPOINT [ "java", "-jar", "/home/somebody/quarkus-app/pizza-api/target/quarkus-app/quarkus-run.jar" ]
