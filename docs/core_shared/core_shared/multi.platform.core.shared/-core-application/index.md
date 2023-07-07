//[core_shared](../../../index.md)/[multi.platform.core.shared](../index.md)/[CoreApplication](index.md)

# CoreApplication

[android]\
public abstract class [CoreApplication](index.md) extends [Application](https://developer.android.com/reference/kotlin/android/app/Application.html)

## Constructors

| | |
|---|---|
| [CoreApplication](-core-application.md) | [android]<br>public [CoreApplication](index.md)[CoreApplication](-core-application.md)() |

## Functions

| Name | Summary |
|---|---|
| [appVersion](app-version.md) | [android]<br>public abstract [String](https://developer.android.com/reference/kotlin/java/lang/String.html)[appVersion](app-version.md)()<br>Abstract function must override app version |
| [deviceId](device-id.md) | [android]<br>public abstract [String](https://developer.android.com/reference/kotlin/java/lang/String.html)[deviceId](device-id.md)()<br>Abstract function must override device id |
| [host](host.md) | [android]<br>public abstract [String](https://developer.android.com/reference/kotlin/java/lang/String.html)[host](host.md)()<br>Abstract function must override server host |
| [koinModule](koin-module.md) | [android]<br>public abstract Module[koinModule](koin-module.md)()<br>Abstract function must override KOIN module |
| [onConfigurationChanged](on-configuration-changed.md) | [android]<br>public [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)[onConfigurationChanged](on-configuration-changed.md)([Configuration](https://developer.android.com/reference/kotlin/android/content/res/Configuration.html)newConfig) |
| [onCreate](on-create.md) | [android]<br>public [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)[onCreate](on-create.md)() |
| [protocol](protocol.md) | [android]<br>public abstract URLProtocol[protocol](protocol.md)()<br>Abstract function must override server protocol |
| [sharedPrefsName](shared-prefs-name.md) | [android]<br>public abstract [String](https://developer.android.com/reference/kotlin/java/lang/String.html)[sharedPrefsName](shared-prefs-name.md)()<br>Abstract function must override shared preferences name |
