# Traveler Core

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT) [![pipeline status](https://gitlab.com/tossaro/kotlin-multi-platform-core/badges/main/pipeline.svg)](https://gitlab.com/tossaro/kotlin-multi-platform-core/-/commits/main) [![coverage report](https://gitlab.com/tossaro/kotlin-multi-platform-core/badges/main/coverage.svg)](https://gitlab.com/tossaro/kotlin-multi-platform-core/-/commits/main) [![Latest Release](https://gitlab.com/tossaro/kotlin-multi-platform-core/-/badges/release.svg)](https://gitlab.com/tossaro/kotlin-multi-platform-core/-/releases)

Provide base constructor / abstract for simplify code structure.
Powered by KOIN for dependency injection and using MVVM pattern with clean architecture.

## Contents

- [Requirements](#requirements)
- [Architectural Pattern](#architectural-pattern)
- [Project Structure](#project-structure)
- [Getting started](#getting-started)
- [Usage](#usage)
- [Example](https://gitlab.com/tossaro/kotlin-multi-platform-core/tree/main/example)
- [Documentation](https://gitlab.com/tossaro/kotlin-multi-platform-core/tree/main/docs)
- [Development](#development)

## Requirements

1. Minimum Android/SDK Version: Nougat/23
2. Deployment Target iOS/SDK Version: 14.1
3. Target Android/SDK Version: Snow Cone/32
4. Compile Android/SDK Version: Snow Cone/32
5. This project is built using Android Studio version 2022.1.1 and Android Gradle 7.5

## Architectural Pattern

This project implement
Clean [Architecture by Fernando Cejas](https://github.com/android10/Android-CleanArchitecture)

### Clean architecture

![Image Clean architecture](/resources/clean_architecture.png)

### Architectural approach

![Image Architectural approach](/resources/clean_architecture_layers.png)

### Architectural reactive approach

![Image Architectural reactive approach](/resources/clean_architecture_layers_details.png)

## Project Structure

For the high level hierarchy, the project separate into 3 main modules, which are :

### App

> This module represent the Presentation layer. Consists of activities, fragments, views, etc. The
> classes are separated based on features, for examples : auth, booking, easyride, etc.

### Domain

> This module represent the Domain layer. Consists of interactors/use cases and models and doesn’t
> know anything outside.

### Data

> This module represent the Data layer. Consists of data sources; can be network call, mock data,
> disk data, and cache data.

## Getting started

1. Fork this repository to your account
2. Copy forked repository ID, paste for step 4
3. Create new `Private Token`
   -> [Tutorial](https://docs.gitlab.com/ee/user/project/private_tokens/index.html)
4. Check for `read_package_registry` role, then save your token
5. Create `properties.gradle` in your root folder, add this content:

```groovy
ext {
    gitlab = [
            consumeToken: "<Generated Private Token>"
    ]
}
```

4. Edit settings.gradle in your root folder:

```groovy
dependencyResolutionManagement {
    repositories {
        //...
        maven {
            name = "Core"
            url = uri("https://gitlab.com/api/v4/projects/<FORKED_REPOSITORY_ID>/packages/maven")
            credentials(HttpHeaderCredentials) {
                name = 'Private-Token'
                value = gitlab.consumeToken
            }
            authentication {
                header(HttpHeaderAuthentication)
            }
        }
    }
}
```

5. Last, add 'implementation "multi.platform.core:${buildType}:${version}"' inside tag
   dependencies { . . . } of build.gradle app

## Usage

1. Example on Application:

```kotlin
//...
import multi.platform.core.external.utils.NetworkUtil
import multi.platform.core.CoreApplication

class ExampleApplication : CoreApplication() {
    //...
    override fun module() = module {
        single { NetworkUtil.buildClient(get()) }
        single { NetworkUtil.buildService<ExampleServiceV1>(BuildConfig.SERVER, get()) }

        singleOf(::ExampleRepositoryImpl) { bind<ExampleRepository>() }
        singleOf(::GetExamplesUseCase)

        viewModelOf(::ExampleViewModel)
    }
    //...
} 
```

2. Example on Activity:

```kotlin
//...
import multi.platform.core.app.common.BaseActivity

class ExampleActivity : BaseActivity() {
    //...
    override fun navHostFragment(): FragmentContainerView =
        your - content - fragment - view - binding
    override fun getNavGraphResource(): Int = R.navigation.navigation
    //...
} 
```

3. Example on Fragment:

```kotlin
//...
import multi.platform.core.app.common.BaseFragment

class ExampleFragment : BaseFragment<ExampleFragmentBinding>(
    R.layout.example_fragment
) {
    //...
    private val viewModel: ExampleViewModel by viewModel()
    override fun bind() {
        super.bind()
        binding.viewModel = viewModel.also {
            it.loadingIndicator.observe(this, ::loadingIndicator)
            it.alertMessage.observe(this, ::showAlert)
            //...
        }
        //...
    }
    //...
} 
```

4. Example on View Model :

```kotlin
//...
import multi.platform.core.app.common.BaseViewModel

class ExampleViewModel : BaseViewModel() {
    //...
} 
```

## Development

Here are some useful gradle/adb commands for executing this example:

* ./gradlew clean build - Build the entire project and execute unit tests
* ./gradlew clean sonarqube - Execute sonarqube coverage report
* ./gradlew dokkaGfm - Generate documentation
* ./gradlew test[flavor][buildType]UnitTest - Execute unit tests e.g., testDevDebugUnitTest
* ./gradlew test[flavor][buildType]UnitTest create[flavor][buildType]CoverageReport - Execute unit
  tests and create coverage report e.g., createDevDebugCoverageReport
* ./gradlew assemble[flavor][buildType] - Create apk file e.g., assembleDevDebug