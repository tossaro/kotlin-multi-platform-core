//[core_shared](../../../index.md)/[multi.platform.core.shared.app.common](../index.md)/[GenericAdapter](index.md)

# GenericAdapter

[android]\
public final class [GenericAdapter](index.md) extends [RecyclerView.Adapter](https://developer.android.com/reference/kotlin/androidx/recyclerview/widget/RecyclerView.Adapter.html)&lt;[GenericAdapter.ViewHolder](-view-holder/index.md)&gt;

## Constructors

| | |
|---|---|
| [GenericAdapter](-generic-adapter.md) | [android]<br>public [GenericAdapter](index.md)[GenericAdapter](-generic-adapter.md)([Double](https://docs.oracle.com/javase/8/docs/api/java/lang/Double.html)widthRatio, [Integer](https://docs.oracle.com/javase/8/docs/api/java/lang/Integer.html)height, [Integer](https://docs.oracle.com/javase/8/docs/api/java/lang/Integer.html)radius, [Integer](https://docs.oracle.com/javase/8/docs/api/java/lang/Integer.html)elevation) |

## Types

| Name | Summary |
|---|---|
| [Listener](-listener/index.md) | [android]<br>public final class [Listener](-listener/index.md) extends [RecyclerView.OnScrollListener](https://developer.android.com/reference/kotlin/androidx/recyclerview/widget/RecyclerView.OnScrollListener.html) |
| [ViewHolder](-view-holder/index.md) | [android]<br>public final class [ViewHolder](-view-holder/index.md) extends [RecyclerView.ViewHolder](https://developer.android.com/reference/kotlin/androidx/recyclerview/widget/RecyclerView.ViewHolder.html) |

## Functions

| Name | Summary |
|---|---|
| [addItem](add-item.md) | [android]<br>public final [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)[addItem](add-item.md)(Function0&lt;[Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)&gt;process) |
| [clear](clear.md) | [android]<br>public final [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)[clear](clear.md)() |
| [getFetchData](get-fetch-data.md) | [android]<br>public final Function0&lt;[Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)&gt;[getFetchData](get-fetch-data.md)() |
| [getItemCount](get-item-count.md) | [android]<br>public [Integer](https://docs.oracle.com/javase/8/docs/api/java/lang/Integer.html)[getItemCount](get-item-count.md)() |
| [getItems](get-items.md) | [android]<br>public final [List](https://docs.oracle.com/javase/8/docs/api/java/util/List.html)&lt;GenericItem&gt;[getItems](get-items.md)() |
| [getItemsCache](get-items-cache.md) | [android]<br>public final [List](https://docs.oracle.com/javase/8/docs/api/java/util/List.html)&lt;GenericItem&gt;[getItemsCache](get-items-cache.md)() |
| [getItemViewType](get-item-view-type.md) | [android]<br>public [Integer](https://docs.oracle.com/javase/8/docs/api/java/lang/Integer.html)[getItemViewType](get-item-view-type.md)([Integer](https://docs.oracle.com/javase/8/docs/api/java/lang/Integer.html)position) |
| [getOnClickMore](get-on-click-more.md) | [android]<br>public final Function1&lt;GenericItem, [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)&gt;[getOnClickMore](get-on-click-more.md)() |
| [getOnSelected](get-on-selected.md) | [android]<br>public final Function1&lt;GenericItem, [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)&gt;[getOnSelected](get-on-selected.md)() |
| [getSkeleton](get-skeleton.md) | [android]<br>public final GenericItem[getSkeleton](get-skeleton.md)() |
| [isLastPage](is-last-page.md) | [android]<br>public final [Boolean](https://docs.oracle.com/javase/8/docs/api/java/lang/Boolean.html)[isLastPage](is-last-page.md)() |
| [onBindViewHolder](on-bind-view-holder.md) | [android]<br>public [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)[onBindViewHolder](on-bind-view-holder.md)([GenericAdapter.ViewHolder](-view-holder/index.md)viewHolder, [Integer](https://docs.oracle.com/javase/8/docs/api/java/lang/Integer.html)position) |
| [onCreateViewHolder](on-create-view-holder.md) | [android]<br>public [GenericAdapter.ViewHolder](-view-holder/index.md)[onCreateViewHolder](on-create-view-holder.md)([ViewGroup](https://developer.android.com/reference/kotlin/android/view/ViewGroup.html)viewGroup, [Integer](https://docs.oracle.com/javase/8/docs/api/java/lang/Integer.html)viewType) |
| [setFetchData](set-fetch-data.md) | [android]<br>public final [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)[setFetchData](set-fetch-data.md)(Function0&lt;[Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)&gt;fetchData) |
| [setItems](set-items.md) | [android]<br>public final [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)[setItems](set-items.md)([List](https://docs.oracle.com/javase/8/docs/api/java/util/List.html)&lt;GenericItem&gt;items) |
| [setItemsCache](set-items-cache.md) | [android]<br>public final [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)[setItemsCache](set-items-cache.md)([List](https://docs.oracle.com/javase/8/docs/api/java/util/List.html)&lt;GenericItem&gt;itemsCache) |
| [setLastPage](set-last-page.md) | [android]<br>public final [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)[setLastPage](set-last-page.md)([Boolean](https://docs.oracle.com/javase/8/docs/api/java/lang/Boolean.html)isLastPage) |
| [setOnClickMore](set-on-click-more.md) | [android]<br>public final [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)[setOnClickMore](set-on-click-more.md)(Function1&lt;GenericItem, [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)&gt;onClickMore) |
| [setOnSelected](set-on-selected.md) | [android]<br>public final [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)[setOnSelected](set-on-selected.md)(Function1&lt;GenericItem, [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)&gt;onSelected) |
| [setSkeleton](set-skeleton.md) | [android]<br>public final [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)[setSkeleton](set-skeleton.md)(GenericItemskeleton) |
| [showInfo](show-info.md) | [android]<br>public final [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)[showInfo](show-info.md)([String](https://docs.oracle.com/javase/8/docs/api/java/lang/String.html)desc, [String](https://docs.oracle.com/javase/8/docs/api/java/lang/String.html)button) |
| [showSkeletons](show-skeletons.md) | [android]<br>public final [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)[showSkeletons](show-skeletons.md)([Integer](https://docs.oracle.com/javase/8/docs/api/java/lang/Integer.html)size) |

## Properties

| Name | Summary |
|---|---|
| [fetchData](index.md#66272857%2FProperties%2F-2121679934) | [android]<br>private Function0&lt;[Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)&gt;[fetchData](index.md#66272857%2FProperties%2F-2121679934) |
| [isLastPage](is-last-page.md) | [android]<br>private [Boolean](https://docs.oracle.com/javase/8/docs/api/java/lang/Boolean.html)[isLastPage](is-last-page.md) |
| [items](index.md#1403412381%2FProperties%2F-2121679934) | [android]<br>private [List](https://docs.oracle.com/javase/8/docs/api/java/util/List.html)&lt;GenericItem&gt;[items](index.md#1403412381%2FProperties%2F-2121679934) |
| [itemsCache](index.md#-230930757%2FProperties%2F-2121679934) | [android]<br>private [List](https://docs.oracle.com/javase/8/docs/api/java/util/List.html)&lt;GenericItem&gt;[itemsCache](index.md#-230930757%2FProperties%2F-2121679934) |
| [onClickMore](index.md#-1861689537%2FProperties%2F-2121679934) | [android]<br>private Function1&lt;GenericItem, [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)&gt;[onClickMore](index.md#-1861689537%2FProperties%2F-2121679934) |
| [onSelected](index.md#-114620477%2FProperties%2F-2121679934) | [android]<br>private Function1&lt;GenericItem, [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)&gt;[onSelected](index.md#-114620477%2FProperties%2F-2121679934) |
| [skeleton](index.md#1822432368%2FProperties%2F-2121679934) | [android]<br>private GenericItem[skeleton](index.md#1822432368%2FProperties%2F-2121679934) |
