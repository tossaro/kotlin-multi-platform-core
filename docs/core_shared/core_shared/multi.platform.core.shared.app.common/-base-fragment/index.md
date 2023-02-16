//[core_shared](../../../index.md)/[multi.platform.core.shared.app.common](../index.md)/[BaseFragment](index.md)

# BaseFragment

[android]\
public class [BaseFragment](index.md)&lt;B extends ViewDataBinding&gt; extends [Fragment](https://developer.android.com/reference/kotlin/androidx/fragment/app/Fragment.html) implements KoinComponent

## Constructors

| | |
|---|---|
| [BaseFragment](-base-fragment.md) | [android]<br>public [BaseFragment](index.md)&lt;[B](index.md)&gt;[BaseFragment](-base-fragment.md)(@[LayoutRes](https://developer.android.com/reference/kotlin/androidx/annotation/LayoutRes.html)()[Integer](https://docs.oracle.com/javase/8/docs/api/java/lang/Integer.html)layoutResId) |

## Functions

| Name | Summary |
|---|---|
| [expandActionBar](expand-action-bar.md) | [android]<br>public [Boolean](https://docs.oracle.com/javase/8/docs/api/java/lang/Boolean.html)[expandActionBar](expand-action-bar.md)()<br>Open function for override action bar expanded binding Default: false |
| [getBinding](get-binding.md) | [android]<br>public final [B](index.md)[getBinding](get-binding.md)() |
| [getLayoutResId](get-layout-res-id.md) | [android]<br>public final [Integer](https://docs.oracle.com/javase/8/docs/api/java/lang/Integer.html)[getLayoutResId](get-layout-res-id.md)() |
| [onAttach](on-attach.md) | [android]<br>public [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)[onAttach](on-attach.md)([Context](https://developer.android.com/reference/kotlin/android/content/Context.html)context) |
| [onCreateView](on-create-view.md) | [android]<br>public [View](https://developer.android.com/reference/kotlin/android/view/View.html)[onCreateView](on-create-view.md)([LayoutInflater](https://developer.android.com/reference/kotlin/android/view/LayoutInflater.html)inflater, [ViewGroup](https://developer.android.com/reference/kotlin/android/view/ViewGroup.html)container, [Bundle](https://developer.android.com/reference/kotlin/android/os/Bundle.html)savedInstanceState) |
| [onDestroy](on-destroy.md) | [android]<br>public [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)[onDestroy](on-destroy.md)() |
| [onResume](on-resume.md) | [android]<br>public [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)[onResume](on-resume.md)() |
| [onStop](on-stop.md) | [android]<br>public [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)[onStop](on-stop.md)() |
| [onViewCreated](on-view-created.md) | [android]<br>public [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)[onViewCreated](on-view-created.md)([View](https://developer.android.com/reference/kotlin/android/view/View.html)view, [Bundle](https://developer.android.com/reference/kotlin/android/os/Bundle.html)savedInstanceState) |
| [setBinding](set-binding.md) | [android]<br>public final [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)[setBinding](set-binding.md)([B](index.md)binding) |

## Properties

| Name | Summary |
|---|---|
| [binding](index.md#957309199%2FProperties%2F-2121679934) | [android]<br>private [B](index.md)[binding](index.md#957309199%2FProperties%2F-2121679934) |
| [layoutResId](index.md#-356749117%2FProperties%2F-2121679934) | [android]<br>private final [Integer](https://docs.oracle.com/javase/8/docs/api/java/lang/Integer.html)[layoutResId](index.md#-356749117%2FProperties%2F-2121679934) |

## Inheritors

| Name |
|---|
| [GalleryFragment](../../multi.platform.core.shared.app.gallery/-gallery-fragment/index.md) |
| [SplashFragment](../../multi.platform.core.shared.app.splash/-splash-fragment/index.md) |
| [WebViewFragment](../../multi.platform.core.shared.app.webview/-web-view-fragment/index.md) |
