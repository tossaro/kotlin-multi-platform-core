# Kotlin Android Core [![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
Provide base constructor / abstract for simplify code structure.
Powered by KOIN for dependency injection and using MVVM pattern with clean architecture.

## Contents
- [Getting started](#getting-started)
- [Usage](#usage)
- [Example](https://gitlab.com/tossaro/kotlin-android-core/tree/main/example)

## Getting started
1. Create New Personal Access Token -> [Tutorial](https://docs.gitlab.com/ee/user/profile/personal_access_tokens.html)
2. Check for api role, then save your token
3. Edit local.properties in your root folder, add this content:
    - gitlabToken=`<Generated Personal Access Token>`
4. Edit build.gradle in your root folder:
```groovy
allprojects {
   def localPropsFile = rootProject.file('local.properties')
   Properties localProps = new Properties()
   if (localPropsFile.canRead()) {
      localProps.load(new FileInputStream(localPropsFile))
   }
   repositories {
      //...
      maven {
         name = "GitLab"
         url = uri("https://gitlab.com/api/v4/projects/38836549/packages/maven")
         credentials(HttpHeaderCredentials) {
            name = 'Private-Token'
            value = localProps['gitlabToken']
         }
         authentication {
            header(HttpHeaderAuthentication)
         }
      }
   }
}
```
5. Last, add `implementation 'kotlin.android:core:1.1.0'` inside tag dependencies { . . . } of build.gradle app

## Usage
1. On Application:
```kotlin
//...
import android.core.external.utils.NetworkUtil
import android.core.CoreApplication
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
2. On Activity:
```kotlin
//...
import android.core.app.BaseActivity
class ExampleActivity : BaseActivity() {
   //...
   override fun navHostFragment(): FragmentContainerView = your-content-fragment-view-binding
   override fun getNavGraphResource(): Int = R.navigation.navigation
   //...
} 
```
3. On Fragment:
```kotlin
//...
import android.core.app.BaseFragment
class ExampleFragment: BaseFragment<ExampleFragmentBinding>(
    R.layout.example_fragment
) {
   //...
   val viewModel: ExampleViewModel by viewModel()
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
4. On View Model :
```kotlin
//...
import android.core.app.BaseViewModel
class ExampleViewModel : BaseViewModel() {
   //...
} 
```