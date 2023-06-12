# Multi Platform Core  | [![Open in Gitpod](https://gitpod.io/button/open-in-gitpod.svg)](https://gitpod.io/#https://gitlab.com/tossaro/kotlin-multi-platform-core/tree/develop)

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT) [![pipeline status](https://gitlab.com/tossaro/kotlin-multi-platform-core/badges/main/pipeline.svg)](https://gitlab.com/tossaro/kotlin-multi-platform-core/-/commits/main) [![coverage report](https://gitlab.com/tossaro/kotlin-multi-platform-core/badges/main/coverage.svg)](https://gitlab.com/tossaro/kotlin-multi-platform-core/-/commits/main) [![Latest Release](https://gitlab.com/tossaro/kotlin-multi-platform-core/-/badges/release.svg)](https://gitlab.com/tossaro/kotlin-multi-platform-core/-/releases)

## Contents

- [Documentation](https://gitlab.com/tossaro/kotlin-multi-platform-core/tree/main/docs)
- [Features](#features)
- [Requirements](#requirements)
- [Usage](#usage)
- [Project Structure](#project-structure)
- [Architecture](#architecture)
- [Commands](#commands)

## Features

- Provide base constructor / abstract for simplify code structure.
- Powered by KOIN for dependency injection and using MVVM pattern with clean architecture.

## Requirements

1. Minimum Android/SDK Version: Nougat/23
2. Deployment Target iOS/SDK Version: 14.1
3. Target Android/SDK Version: Snow Cone/32
4. Compile Android/SDK Version: Snow Cone/32
5. This project is built using Android Studio version 2022.1.1 and Android Gradle 7.5
6. For iOS, please install [COCOAPODS](https://cocoapods.org/)
7. Create `properties.gradle` in your root folder, add this content:
```groovy
ext {
    gitlab = [
            publishToken: "$yourPublishToken",
            consumeToken: "$yourConsumeToken"
    ]

    deeplink = "android-app://example.app.id"

    //example app purpose
    server = [
            dev  : "\"$yourAuthServer\"",
            stage: "\"$yourAuthServer\"",
            beta : "\"$yourAuthServer\"",
            prod : "\"$yourAuthServer\"",
    ]
    //example app purpose
    socket = "\"wss://streamer.cryptocompare.com/v2?api_key=$yourCryptoCompareKey\""
}
```
> ***note:***
>- replace ***$yourPublishToken*** with your private token if you forked to your private project, otherwise leave it blank if only wanted to run.
>- replace ***$yourConsumeToken*** with your private token if you forked to your private project, otherwise leave it blank if only wanted to run.
>- replace ***$yourAuthServer*** with your auth server if you want to trial oauth api communication, otherwise leave it blank if only wanted to run.
>- replace ***$yourCryptoCompareKey*** with your cryptocompare key if you show list data on example app, otherwise leave it blank if only wanted to run.

## Usage

1. Edit settings.gradle in your root folder:

```groovy
dependencyResolutionManagement {
    repositories {
        //...
        maven { url 'https://gitlab.com/api/v4/projects/<FORKED_REPOSITORY_ID>/packages/maven' }
    }
}
```

2. Last, add 'implementation "multi.platform.core:${platform}:${version}"' inside tag
   dependencies { . . . } of build.gradle app

## Project Structure

```plantuml
:core_shared;
fork
    :example_lib;
    :example_android;
fork again
    :core_ios;
    :example_ios;
end fork
end
```
For the high level hierarchy, the project separate into 4 main modules, which are :

### 1. [Example Android](https://gitlab.com/tossaro/kotlin-multi-platform-core/tree/main/example_lib)

This module contains the android library's example usage of this project.

### 2. [Example Android](https://gitlab.com/tossaro/kotlin-multi-platform-core/tree/main/example_android)

This module contains the android application's example usage of this project.

### 3. [Example iOS](https://gitlab.com/tossaro/kotlin-multi-platform-core/tree/main/example_ios)

This module contains the iOS application's example usage of this project.

### 4. [Core iOS](https://gitlab.com/tossaro/kotlin-multi-platform-core/tree/main/core_ios)
This module contains iOS code that holds the iOS library, that can be injected to iOS app.

### 5. [Core Shared](https://gitlab.com/tossaro/kotlin-multi-platform-core/tree/main/core_shared)
This module contains shared code that holds the domain and data layers and some part of the presentation logic ie.shared viewmodels.

## Architecture

This project implement
Clean [Architecture by Fernando Cejas](https://github.com/android10/Android-CleanArchitecture)

### Clean architecture

![Image Clean architecture](/resources/clean_architecture.png)

### Architectural approach

![Image Architectural approach](/resources/clean_architecture_layers.png)

### Architectural reactive approach

![Image Architectural reactive approach](/resources/clean_architecture_layers_details.png)

## Commands

Here are some useful gradle/adb commands for executing this example:

* ./gradlew clean build - Build the entire project and execute unit tests
* ./gradlew clean sonarqube - Execute sonarqube coverage report
* ./gradlew dokkaGfm - Generate documentation
* ./gradlew test[flavor][buildType]UnitTest - Execute unit tests e.g., testDevDebugUnitTest
* ./gradlew test[flavor][buildType]UnitTest create[flavor][buildType]CoverageReport - Execute unit
  tests and create coverage report e.g., createDevDebugCoverageReport
* ./gradlew assemble[flavor][buildType] - Create apk file e.g., assembleDevDebug
* ./gradlew :core_shared:assembleXCFramework - Generate XCFramework for iOS
* ./gradlew publish - Publish - Publish to repository packages (MAVEN)