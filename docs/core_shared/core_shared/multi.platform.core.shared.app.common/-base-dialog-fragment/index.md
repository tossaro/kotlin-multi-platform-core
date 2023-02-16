//[core_shared](../../../index.md)/[multi.platform.core.shared.app.common](../index.md)/[BaseDialogFragment](index.md)

# BaseDialogFragment

[android]\
public class [BaseDialogFragment](index.md)&lt;B extends ViewDataBinding&gt; extends [DialogFragment](https://developer.android.com/reference/kotlin/androidx/fragment/app/DialogFragment.html) implements KoinComponent

## Constructors

| | |
|---|---|
| [BaseDialogFragment](-base-dialog-fragment.md) | [android]<br>public [BaseDialogFragment](index.md)&lt;[B](index.md)&gt;[BaseDialogFragment](-base-dialog-fragment.md)(@[LayoutRes](https://developer.android.com/reference/kotlin/androidx/annotation/LayoutRes.html)()[Integer](https://docs.oracle.com/javase/8/docs/api/java/lang/Integer.html)layoutResId) |

## Functions

| Name | Summary |
|---|---|
| [getBinding](get-binding.md) | [android]<br>public final [B](index.md)[getBinding](get-binding.md)() |
| [getLayoutResId](get-layout-res-id.md) | [android]<br>public final [Integer](https://docs.oracle.com/javase/8/docs/api/java/lang/Integer.html)[getLayoutResId](get-layout-res-id.md)() |
| [onAttach](on-attach.md) | [android]<br>public [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)[onAttach](on-attach.md)([Context](https://developer.android.com/reference/kotlin/android/content/Context.html)context) |
| [onCreateDialog](on-create-dialog.md) | [android]<br>public [Dialog](https://developer.android.com/reference/kotlin/android/app/Dialog.html)[onCreateDialog](on-create-dialog.md)([Bundle](https://developer.android.com/reference/kotlin/android/os/Bundle.html)savedInstanceState) |
| [onCreateView](on-create-view.md) | [android]<br>public [View](https://developer.android.com/reference/kotlin/android/view/View.html)[onCreateView](on-create-view.md)([LayoutInflater](https://developer.android.com/reference/kotlin/android/view/LayoutInflater.html)inflater, [ViewGroup](https://developer.android.com/reference/kotlin/android/view/ViewGroup.html)container, [Bundle](https://developer.android.com/reference/kotlin/android/os/Bundle.html)savedInstanceState) |
| [onDestroyView](on-destroy-view.md) | [android]<br>public [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)[onDestroyView](on-destroy-view.md)() |
| [onStart](on-start.md) | [android]<br>public [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)[onStart](on-start.md)() |
| [setBinding](set-binding.md) | [android]<br>public final [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)[setBinding](set-binding.md)([B](index.md)binding) |

## Properties

| Name | Summary |
|---|---|
| [binding](index.md#-1162874201%2FProperties%2F-2121679934) | [android]<br>private [B](index.md)[binding](index.md#-1162874201%2FProperties%2F-2121679934) |
| [layoutResId](index.md#-1609927077%2FProperties%2F-2121679934) | [android]<br>private final [Integer](https://docs.oracle.com/javase/8/docs/api/java/lang/Integer.html)[layoutResId](index.md#-1609927077%2FProperties%2F-2121679934) |

## Inheritors

| Name |
|---|
| [ErrorConnectionDialogFragment](../../multi.platform.core.shared.app.errorconnectiondialog/-error-connection-dialog-fragment/index.md) |
