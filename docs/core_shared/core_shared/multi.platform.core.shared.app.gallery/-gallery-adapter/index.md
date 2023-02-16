//[core_shared](../../../index.md)/[multi.platform.core.shared.app.gallery](../index.md)/[GalleryAdapter](index.md)

# GalleryAdapter

[android]\
public final class [GalleryAdapter](index.md) extends [RecyclerView.Adapter](https://developer.android.com/reference/kotlin/androidx/recyclerview/widget/RecyclerView.Adapter.html)&lt;[GalleryAdapter.ViewHolder](-view-holder/index.md)&gt;

## Constructors

| | |
|---|---|
| [GalleryAdapter](-gallery-adapter.md) | [android]<br>public [GalleryAdapter](index.md)[GalleryAdapter](-gallery-adapter.md)() |

## Types

| Name | Summary |
|---|---|
| [ViewHolder](-view-holder/index.md) | [android]<br>public final class [ViewHolder](-view-holder/index.md) extends [RecyclerView.ViewHolder](https://developer.android.com/reference/kotlin/androidx/recyclerview/widget/RecyclerView.ViewHolder.html) |

## Functions

| Name | Summary |
|---|---|
| [clear](clear.md) | [android]<br>public final [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)[clear](clear.md)() |
| [getItemCount](get-item-count.md) | [android]<br>public [Integer](https://docs.oracle.com/javase/8/docs/api/java/lang/Integer.html)[getItemCount](get-item-count.md)() |
| [getItems](get-items.md) | [android]<br>public final [List](https://docs.oracle.com/javase/8/docs/api/java/util/List.html)&lt;[String](https://docs.oracle.com/javase/8/docs/api/java/lang/String.html)&gt;[getItems](get-items.md)() |
| [getOnClick](get-on-click.md) | [android]<br>public final Function2&lt;[View](https://developer.android.com/reference/kotlin/android/view/View.html), [String](https://docs.oracle.com/javase/8/docs/api/java/lang/String.html), [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)&gt;[getOnClick](get-on-click.md)() |
| [onBindViewHolder](on-bind-view-holder.md) | [android]<br>public [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)[onBindViewHolder](on-bind-view-holder.md)([GalleryAdapter.ViewHolder](-view-holder/index.md)viewHolder, [Integer](https://docs.oracle.com/javase/8/docs/api/java/lang/Integer.html)position) |
| [onCreateViewHolder](on-create-view-holder.md) | [android]<br>public [GalleryAdapter.ViewHolder](-view-holder/index.md)[onCreateViewHolder](on-create-view-holder.md)([ViewGroup](https://developer.android.com/reference/kotlin/android/view/ViewGroup.html)viewGroup, [Integer](https://docs.oracle.com/javase/8/docs/api/java/lang/Integer.html)viewType) |
| [setItems](set-items.md) | [android]<br>public final [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)[setItems](set-items.md)([List](https://docs.oracle.com/javase/8/docs/api/java/util/List.html)&lt;[String](https://docs.oracle.com/javase/8/docs/api/java/lang/String.html)&gt;items) |
| [setOnClick](set-on-click.md) | [android]<br>public final [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)[setOnClick](set-on-click.md)(Function2&lt;[View](https://developer.android.com/reference/kotlin/android/view/View.html), [String](https://docs.oracle.com/javase/8/docs/api/java/lang/String.html), [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)&gt;onClick) |

## Properties

| Name | Summary |
|---|---|
| [items](index.md#-1904200507%2FProperties%2F-2121679934) | [android]<br>private [List](https://docs.oracle.com/javase/8/docs/api/java/util/List.html)&lt;[String](https://docs.oracle.com/javase/8/docs/api/java/lang/String.html)&gt;[items](index.md#-1904200507%2FProperties%2F-2121679934) |
| [onClick](index.md#34807740%2FProperties%2F-2121679934) | [android]<br>private Function2&lt;[View](https://developer.android.com/reference/kotlin/android/view/View.html), [String](https://docs.oracle.com/javase/8/docs/api/java/lang/String.html), [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)&gt;[onClick](index.md#34807740%2FProperties%2F-2121679934) |
