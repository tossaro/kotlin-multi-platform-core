//[example_lib](../../../index.md)/[multi.platform.example_lib.shared.app.stocklist](../index.md)/[StockListViewModel](index.md)

# StockListViewModel

[common]\
public final class [StockListViewModel](index.md) extends BaseViewModel implements KoinComponent

## Constructors

| | |
|---|---|
| [StockListViewModel](-stock-list-view-model.md) | [common]<br>public [StockListViewModel](index.md)[StockListViewModel](-stock-list-view-model.md)() |

## Functions

| Name | Summary |
|---|---|
| [getNotif](get-notif.md) | [common]<br>public final MutableStateFlow&lt;[Integer](https://developer.android.com/reference/kotlin/java/lang/Integer.html)&gt;[getNotif](get-notif.md)() |
| [getPage](get-page.md) | [common]<br>public final [Integer](https://developer.android.com/reference/kotlin/java/lang/Integer.html)[getPage](get-page.md)() |
| [getPromo](get-promo.md) | [common]<br>public final MutableStateFlow&lt;[Integer](https://developer.android.com/reference/kotlin/java/lang/Integer.html)&gt;[getPromo](get-promo.md)() |
| [getStocks](get-stocks.md) | [common]<br>public final MutableStateFlow&lt;[List](https://developer.android.com/reference/kotlin/java/util/List.html)&lt;[Stock](../../multi.platform.example_lib.shared.domain.stock.entity/-stock/index.md)&gt;&gt;[getStocks](get-stocks.md)()<br>public final [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)[getStocks](get-stocks.md)() |
| [isFromNetwork](is-from-network.md) | [common]<br>public final [Boolean](https://developer.android.com/reference/kotlin/java/lang/Boolean.html)[isFromNetwork](is-from-network.md)() |
| [setFromNetwork](set-from-network.md) | [common]<br>public final [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)[setFromNetwork](set-from-network.md)([Boolean](https://developer.android.com/reference/kotlin/java/lang/Boolean.html)isFromNetwork) |
| [setNotif](set-notif.md) | [common]<br>public final [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)[setNotif](set-notif.md)(MutableStateFlow&lt;[Integer](https://developer.android.com/reference/kotlin/java/lang/Integer.html)&gt;notif) |
| [setPage](set-page.md) | [common]<br>public final [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)[setPage](set-page.md)([Integer](https://developer.android.com/reference/kotlin/java/lang/Integer.html)page) |
| [setPromo](set-promo.md) | [common]<br>public final [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)[setPromo](set-promo.md)(MutableStateFlow&lt;[Integer](https://developer.android.com/reference/kotlin/java/lang/Integer.html)&gt;promo) |
| [setStocks](set-stocks.md) | [common]<br>public final [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)[setStocks](set-stocks.md)(MutableStateFlow&lt;[List](https://developer.android.com/reference/kotlin/java/util/List.html)&lt;[Stock](../../multi.platform.example_lib.shared.domain.stock.entity/-stock/index.md)&gt;&gt;stocks) |

## Properties

| Name | Summary |
|---|---|
| [isFromNetwork](is-from-network.md) | [common]<br>private [Boolean](https://developer.android.com/reference/kotlin/java/lang/Boolean.html)[isFromNetwork](is-from-network.md) |
| [notif](index.md#851305087%2FProperties%2F-1932516659) | [common]<br>private MutableStateFlow&lt;[Integer](https://developer.android.com/reference/kotlin/java/lang/Integer.html)&gt;[notif](index.md#851305087%2FProperties%2F-1932516659) |
| [page](index.md#1461522908%2FProperties%2F-1932516659) | [common]<br>private [Integer](https://developer.android.com/reference/kotlin/java/lang/Integer.html)[page](index.md#1461522908%2FProperties%2F-1932516659) |
| [promo](index.md#-237375104%2FProperties%2F-1932516659) | [common]<br>private MutableStateFlow&lt;[Integer](https://developer.android.com/reference/kotlin/java/lang/Integer.html)&gt;[promo](index.md#-237375104%2FProperties%2F-1932516659) |
| [stocks](index.md#-1721464690%2FProperties%2F-1932516659) | [common]<br>private MutableStateFlow&lt;[List](https://developer.android.com/reference/kotlin/java/util/List.html)&lt;[Stock](../../multi.platform.example_lib.shared.domain.stock.entity/-stock/index.md)&gt;&gt;[stocks](index.md#-1721464690%2FProperties%2F-1932516659) |
