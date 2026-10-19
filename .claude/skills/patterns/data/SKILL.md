---
name: patterns/data
description: Data layer patterns — repository impl, data sources, DTOs, Ktor, fakes. Read before editing feature/*/data.
---

# Data

```
Repository (domain interface) <- RepositoryImpl -> DataSource (interface)
                                                    ├ FakeXDataSource   (default, hackathon)
                                                    └ RemoteXDataSource (Ktor)
```

## DataSource
```kotlin
class RemoteProductDataSource(private val client: HttpClient) : ProductDataSource {
    override suspend fun getProducts(): List<ProductDto> = safeApiCall { client.get("products").body() }
}
```
- Every network call in `safeApiCall { }`. It maps Ktor errors to `NetworkException`.
- No try/catch, no `runCatching`.
- Base URL: `API_BASE_URL` in `app/build.gradle.kts`.
- Switch fake -> remote: change one binding in `<feature>DataModule`.

## DTO
- `@Serializable` DTO lives in data. Never leaks out.
- `@SerialName("snake_case")` for JSON names.
- Mappers both ways: `ProductDto.toDomain()`, `Product.toDto()`.

## Repository
- `internal class XRepositoryImpl(...) : XRepository`.
- Maps DTO -> domain. Throws domain exceptions (e.g. `ProductNotFound`).
- Shared state: hold `MutableStateFlow`, expose `StateFlow`. Bind `singleOf(...)`.
- Inject DataSource, never `HttpClient` directly.

## Koin
```kotlin
val catalogDataModule = module {
    singleOf(::FakeProductDataSource) bind ProductDataSource::class
    singleOf(::ProductRepositoryImpl) bind ProductRepository::class
}
```
