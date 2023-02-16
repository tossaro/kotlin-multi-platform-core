//[example_lib](../../../index.md)/[multi.platform.example_lib.shared.domain.stock](../index.md)/[StockRepository](index.md)

# StockRepository

[common]\
public interface [StockRepository](index.md)

## Functions

| Name | Summary |
|---|---|
| [getStocks](get-stocks.md) | [common]<br>public abstract [StockResponse](../../multi.platform.example_lib.shared.data.stock.network.response/-stock-response/index.md)[getStocks](get-stocks.md)([Integer](https://developer.android.com/reference/kotlin/java/lang/Integer.html)limit, [Integer](https://developer.android.com/reference/kotlin/java/lang/Integer.html)page) |
| [getStocksLocal](get-stocks-local.md) | [common]<br>public abstract [List](https://developer.android.com/reference/kotlin/java/util/List.html)&lt;[Stock](../../multi.platform.example_lib.shared.domain.stock.entity/-stock/index.md)&gt;[getStocksLocal](get-stocks-local.md)([Integer](https://developer.android.com/reference/kotlin/java/lang/Integer.html)offset, [Integer](https://developer.android.com/reference/kotlin/java/lang/Integer.html)limit) |
| [setStocksLocal](set-stocks-local.md) | [common]<br>public abstract [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)[setStocksLocal](set-stocks-local.md)([List](https://developer.android.com/reference/kotlin/java/util/List.html)&lt;[Stock](../../multi.platform.example_lib.shared.domain.stock.entity/-stock/index.md)&gt;stocks) |

## Inheritors

| Name |
|---|
| [StockRepositoryImpl](../../multi.platform.example_lib.shared.data.stock/-stock-repository-impl/index.md) |
