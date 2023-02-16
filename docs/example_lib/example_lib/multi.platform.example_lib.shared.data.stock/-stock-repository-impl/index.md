//[example_lib](../../../index.md)/[multi.platform.example_lib.shared.data.stock](../index.md)/[StockRepositoryImpl](index.md)

# StockRepositoryImpl

[common]\
public final class [StockRepositoryImpl](index.md) implements KoinComponent, [StockRepository](../../multi.platform.example_lib.shared.domain.stock/-stock-repository/index.md)

## Constructors

| | |
|---|---|
| [StockRepositoryImpl](-stock-repository-impl.md) | [common]<br>public [StockRepositoryImpl](index.md)[StockRepositoryImpl](-stock-repository-impl.md)() |

## Functions

| Name | Summary |
|---|---|
| [getStocks](get-stocks.md) | [common]<br>public [StockResponse](../../multi.platform.example_lib.shared.data.stock.network.response/-stock-response/index.md)[getStocks](get-stocks.md)([Integer](https://developer.android.com/reference/kotlin/java/lang/Integer.html)limit, [Integer](https://developer.android.com/reference/kotlin/java/lang/Integer.html)page) |
| [getStocksLocal](get-stocks-local.md) | [common]<br>public [List](https://developer.android.com/reference/kotlin/java/util/List.html)&lt;[Stock](../../multi.platform.example_lib.shared.domain.stock.entity/-stock/index.md)&gt;[getStocksLocal](get-stocks-local.md)([Integer](https://developer.android.com/reference/kotlin/java/lang/Integer.html)offset, [Integer](https://developer.android.com/reference/kotlin/java/lang/Integer.html)limit) |
| [setStocksLocal](set-stocks-local.md) | [common]<br>public [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)[setStocksLocal](set-stocks-local.md)([List](https://developer.android.com/reference/kotlin/java/util/List.html)&lt;[Stock](../../multi.platform.example_lib.shared.domain.stock.entity/-stock/index.md)&gt;stocks) |
