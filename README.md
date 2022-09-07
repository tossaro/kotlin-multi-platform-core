# Kotlin Android Core 
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)[![pipeline status](https://gitlab.com/tossaro/kotlin-android-core/badges/develop/pipeline.svg)](https://gitlab.com/tossaro/kotlin-android-core/-/commits/develop) [![coverage report](https://gitlab.com/tossaro/kotlin-android-core/badges/development/coverage.svg)](https://gitlab.com/tossaro/kotlin-android-core/-/commits/develop) [![Latest Release](https://gitlab.com/tossaro/kotlin-android-core/-/badges/release.svg)](https://gitlab.com/tossaro/kotlin-android-core/-/releases)
Provide base constructor / abstract for simplify code structure.
Powered by KOIN for dependency injection and using MVVM pattern with clean architecture.

## Contents
- [Requirements](#requirements)
- [Architectural Pattern](#architectural-pattern)
- [Project Structure](#project-structure)
- [Getting started](#getting-started)
- [Usage](#usage)
- [Example](https://gitlab.com/tossaro/kotlin-android-core/tree/main/example)
- [Documentation](https://gitlab.com/tossaro/kotlin-android-core/tree/main/docs)
- [Development](#development)

## Requirements
1. Minimum Android/SDK Version: Nougat/23
2. Target Android/SDK Version: Snow Cone/32
3. Compile Android/SDK Version: Snow Cone/32
4. This project is built using Android Studio version 2022.1.1 and Android Gradle 7.5

## Architectural Pattern
This project implement Clean [Architecture by Fernando Cejas](https://github.com/android10/Android-CleanArchitecture)

### Clean architecture
![Image Clean architecture](/resources/clean_architecture.png)

### Architectural approach
![Image Architectural approach](/resources/clean_architecture_layers.png)

### Architectural reactive approach
![Image Architectural reactive approach](/resources/clean_architecture_layers_details.png)

## Project Structure
For the high level hierarchy, the project separate into 3 main modules, which are :

### App
> This module represent the Presentation layer. Consists of activities, fragments, views, etc. The classes are separated based on features, for examples : auth, booking, easyride, etc.

### Domain
> This module represent the Domain layer. Consists of interactors/use cases and models and doesn’t know anything outside.

### Data
> This module represent the Data layer. Consists of data sources; can be network call, mock data, disk data, and cache data.

## Getting started
1. Create New `Deploy Token` -> [Tutorial](https://docs.gitlab.com/ee/user/project/deploy_tokens/index.html)
2. Check for `read_package_registry` role, then save your token
3. Create `properties.gradle` in your root folder, add this content:
```groovy
ext {
   gitlab = [
        consumeToken: "<Generated Deploy Token>"
   ]
}
```
4. Edit settings.gradle in your root folder:
```groovy
dependencyResolutionManagement {
   repositories {
      //...
      maven {
         name = "Traveler Group"
         url = uri("https://gitlab.com/api/v4/projects/38836420/packages/maven")
         credentials(HttpHeaderCredentials) {
            name = 'Deploy-Token'
            value = gitlab.consumeToken
         }
         authentication {
            header(HttpHeaderAuthentication)
         }
      }
   }
}
```
5. Last, add 'implementation "tossaro.android.core:${variant}-${buildType}:${version}"' inside tag dependencies { . . . } of build.gradle app

## Usage
1. Example on Application:
```kotlin
//...
import tossaro.android.core.external.utils.NetworkUtil
import tossaro.android.core.CoreApplication
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
import tossaro.android.core.app.BaseActivity
class ExampleActivity : BaseActivity() {
   //...
   override fun navHostFragment(): FragmentContainerView = your-content-fragment-view-binding
   override fun getNavGraphResource(): Int = R.navigation.navigation
   //...
} 
```
3. Example on Fragment:
```kotlin
//...
import tossaro.android.core.app.BaseFragment
class ExampleFragment: BaseFragment<ExampleFragmentBinding>(
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
import tossaro.android.core.app.BaseViewModel
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
* ./gradlew test[flavor][buildType]UnitTest create[flavor][buildType]CoverageReport - Execute unit tests and create coverage report e.g., createDevDebugCoverageReport
* ./gradlew assemble[flavor][buildType] - Create apk file e.g., assembleDevDebug