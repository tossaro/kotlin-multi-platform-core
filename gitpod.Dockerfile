FROM gitpod/workspace-full-vnc
SHELL ["/bin/bash", "-c"]

USER root
RUN add-apt-repository ppa:maarten-fonville/android-studio
RUN curl -fsSL https://pkgs.tailscale.com/stable/ubuntu/jammy.gpg | sudo apt-key add - \
     && curl -fsSL https://pkgs.tailscale.com/stable/ubuntu/jammy.list | sudo tee /etc/apt/sources.list.d/tailscale.list \
     && apt-get update \
     && apt-get install -y tailscale openjdk-11-jdk android-studio
RUN update-alternatives --set ip6tables /usr/sbin/ip6tables-nft

USER gitpod
ENV ANDROID_HOME="$HOME/Android/Sdk"
ENV PATH="$ANDROID_HOME/tools:$ANDROID_HOME/cmdline-tools/latest/bin:$ANDROID_HOME/platform-tools:$PATH"=