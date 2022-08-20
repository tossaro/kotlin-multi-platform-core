# Kotlin Android Core [![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
Provide base constructor / abstract for simplify code structure.
Powered by KOIN for dependency injection and using MVVM pattern with clean architecture.

## Contents
- [Getting started](#getting-started)
- [Usage](#usage)
- [Example](https://github.com/tossaro/kotlin-android-core/tree/main/example)

## Getting started
1. Create New Personal Access Token -> [Tutorial](https://docs.github.com/en/enterprise-server@3.4/authentication/keeping-your-account-and-data-secure/creating-a-personal-access-token)
2. Check for read:packages role, then save your token
3. Create github.properties in your app folder with content:
    - USER=`<Your Github User ID>`
    - KEY=`<Generated Personal Access Token>`
4. Open build.gradle in your app folder, add this before tag android { . . . }
```groovy
def githubPropsFile = file('github.properties')
Properties githubProps = new Properties()
if (githubPropsFile.canRead()) {
    githubProps.load(new FileInputStream(githubPropsFile))
}
```
6. Then add maven repository for this package before tag dependencies { . . . }
```groovy
repositories {
    maven {
        url = uri("https://maven.pkg.github.com/tossaro/kotlin-android-core")
        credentials {
            username = githubProps['USER']
            password = githubProps['KEY']
        }
    }
}
```
7. Last, add `implementation 'kotlin.android:core:1.1.0'` inside tag dependencies { . . . }

## Usage
1. On Activity :
```kotlin
....
import android.core.app.BaseActivity
class ExampleActivity : BaseActivity() {
   ....
   override fun navHostFragment(): FragmentContainerView = your-content-fragment-view-binding
   override fun getNavGraphResource(): Int = R.navigation.navigation
   ....
} 
```
2. On Fragment :
```kotlin
....
import android.core.app.BaseFragment
class ExampleFragment: BaseFragment<ExampleFragmentBinding>(
    R.layout.example_fragment
) {
   ....
   val viewModel: ExampleViewModel by viewModel()
   override fun bind() {
        super.bind()
        binding.viewModel = viewModel.also {
            it.loadingIndicator.observe(this, ::loadingIndicator)
            it.alertMessage.observe(this, ::showAlert)
            ....
        }
        ....
   }
   ....
} 
```
3. On View Model :
```kotlin
....
import android.core.app.BaseViewModel
class SplashViewModel : BaseViewModel() {
   ....
} 
```