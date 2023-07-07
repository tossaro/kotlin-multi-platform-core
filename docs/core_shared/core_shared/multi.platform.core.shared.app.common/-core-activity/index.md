//[core_shared](../../../index.md)/[multi.platform.core.shared.app.common](../index.md)/[CoreActivity](index.md)

# CoreActivity

[android]\
public abstract class [CoreActivity](index.md) extends [AppCompatActivity](https://developer.android.com/reference/kotlin/androidx/appcompat/app/AppCompatActivity.html) implements KoinComponent

## Constructors

| | |
|---|---|
| [CoreActivity](-core-activity.md) | [android]<br>public [CoreActivity](index.md)[CoreActivity](-core-activity.md)() |

## Functions

| Name | Summary |
|---|---|
| [actionBarExpandedAutoCompleteHint](action-bar-expanded-auto-complete-hint.md) | [android]<br>public [Integer](https://developer.android.com/reference/kotlin/java/lang/Integer.html)[actionBarExpandedAutoCompleteHint](action-bar-expanded-auto-complete-hint.md)()<br>Open function for override action bar auto complete hint Default: null |
| [actionBarSearchHint](action-bar-search-hint.md) | [android]<br>public [Integer](https://developer.android.com/reference/kotlin/java/lang/Integer.html)[actionBarSearchHint](action-bar-search-hint.md)()<br>Open function for override action bar search hint Default: null |
| [appVersion](app-version.md) | [android]<br>public [String](https://developer.android.com/reference/kotlin/java/lang/String.html)[appVersion](app-version.md)()<br>Open function for version of application |
| [bottomNavBarMenu](bottom-nav-bar-menu.md) | [android]<br>public [Integer](https://developer.android.com/reference/kotlin/java/lang/Integer.html)[bottomNavBarMenu](bottom-nav-bar-menu.md)()<br>Open function for override bottom navigation menu Default: 0 |
| [getAppBarConfiguration](get-app-bar-configuration.md) | [android]<br>public final [AppBarConfiguration](https://developer.android.com/reference/kotlin/androidx/navigation/ui/AppBarConfiguration.html)[getAppBarConfiguration](get-app-bar-configuration.md)() |
| [isDebugNavStack](is-debug-nav-stack.md) | [android]<br>public [Boolean](https://developer.android.com/reference/kotlin/java/lang/Boolean.html)[isDebugNavStack](is-debug-nav-stack.md)()<br>Open function for override debug navigation stack Default: false |
| [isInternetAvailable](is-internet-available.md) | [android]<br>public final [Boolean](https://developer.android.com/reference/kotlin/java/lang/Boolean.html)[isInternetAvailable](is-internet-available.md)()<br>Function for check internet availability |
| [navGraph](nav-graph.md) | [android]<br>public [Integer](https://developer.android.com/reference/kotlin/java/lang/Integer.html)[navGraph](nav-graph.md)()<br>Open function for override navigation graph Default: null |
| [nightMode](night-mode.md) | [android]<br>public [Integer](https://developer.android.com/reference/kotlin/java/lang/Integer.html)[nightMode](night-mode.md)()<br>Open function for override night mode Default: MODE_NIGHT_NO |
| [onSupportNavigateUp](on-support-navigate-up.md) | [android]<br>public [Boolean](https://developer.android.com/reference/kotlin/java/lang/Boolean.html)[onSupportNavigateUp](on-support-navigate-up.md)() |
| [orientation](orientation.md) | [android]<br>public [Integer](https://developer.android.com/reference/kotlin/java/lang/Integer.html)[orientation](orientation.md)()<br>Open function for application orientation Default: ActivityInfo.SCREEN_ORIENTATION_PORTRAIT |
| [setAppBarConfiguration](set-app-bar-configuration.md) | [android]<br>public final [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)[setAppBarConfiguration](set-app-bar-configuration.md)([AppBarConfiguration](https://developer.android.com/reference/kotlin/androidx/navigation/ui/AppBarConfiguration.html)appBarConfiguration) |
| [topLevelDestinations](top-level-destinations.md) | [android]<br>public [Set](https://developer.android.com/reference/kotlin/java/util/Set.html)&lt;[Integer](https://developer.android.com/reference/kotlin/java/lang/Integer.html)&gt;[topLevelDestinations](top-level-destinations.md)()<br>Open function for override default top level app route config Default: no top route |

## Properties

| Name | Summary |
|---|---|
| [appBarConfiguration](index.md#-1105016925%2FProperties%2F-2121679934) | [android]<br>private [AppBarConfiguration](https://developer.android.com/reference/kotlin/androidx/navigation/ui/AppBarConfiguration.html)[appBarConfiguration](index.md#-1105016925%2FProperties%2F-2121679934) |
