FROM debian:12

ARG ANDROID_COMPILE_SDK
ARG ANDROID_BUILD_TOOLS
ARG ANDROID_SDK_TOOLS

RUN apt-get --quiet update --yes
RUN apt-get --quiet install --yes git wget unzip ca-certificates dpkg curl jq python3 libpulse0 libglu1-mesa libnss3 libxcomposite1 libxcursor1 libasound2 libgl1 libqt5gui5 adb
RUN wget https://download.oracle.com/java/21/latest/jdk-21_linux-x64_bin.deb; \
    dpkg -i jdk-21_linux-x64_bin.deb || apt-get --quiet install --yes -f

# Install official GitLab CLI (glab) for securefile download and other GitLab operations
RUN set -eux; \
    GLAB_VERSION=$(curl -sSL "https://gitlab.com/api/v4/projects/gitlab-org%2Fcli/releases" | jq -r '.[0].tag_name | ltrimstr("v")'); \
    GLAB_DEB="glab_${GLAB_VERSION}_linux_amd64.deb"; \
    wget --quiet --output-document="/tmp/${GLAB_DEB}" "https://gitlab.com/gitlab-org/cli/-/releases/v${GLAB_VERSION}/downloads/${GLAB_DEB}"; \
    dpkg --install "/tmp/${GLAB_DEB}" || apt-get --quiet install --yes -f

# Setup path as android_home for moving/exporting the downloaded sdk into it
ENV ANDROID_HOME=/android-sdk-root
# Create a new directory at specified location
RUN install -d "$ANDROID_HOME"
# Here we are installing androidSDK tools from official source,
# (the key thing here is the url from where you are downloading these sdk tool for command line, so please do note this url pattern there and here as well)
# after that unzipping those tools and
# then running a series of SDK manager commands to install necessary android SDK packages that'll allow the app to build
RUN wget --no-verbose --output-document=$ANDROID_HOME/cmdline-tools.zip https://dl.google.com/android/repository/commandlinetools-linux-${ANDROID_SDK_TOOLS}_latest.zip
RUN unzip -q -d "$ANDROID_HOME/cmdline-tools" "$ANDROID_HOME/cmdline-tools.zip"
RUN mv -T "$ANDROID_HOME/cmdline-tools/cmdline-tools" "$ANDROID_HOME/cmdline-tools/tools"
ENV PATH="$PATH:$ANDROID_HOME/cmdline-tools/latest/bin:$ANDROID_HOME/cmdline-tools/tools/bin"

# Nothing fancy here, just checking sdkManager version
RUN sdkmanager --version

# use yes to accept all licenses
RUN yes | sdkmanager --licenses > /dev/null || true
RUN sdkmanager "platforms;android-${ANDROID_COMPILE_SDK}"
RUN sdkmanager "platform-tools"
RUN sdkmanager "build-tools;${ANDROID_BUILD_TOOLS}"
RUN sdkmanager "emulator"
RUN sdkmanager "system-images;android-30;aosp_atd;x86_64"