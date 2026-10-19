package com.castorama.krakathon.feature.catalog.data.source

import kotlinx.coroutines.delay

/** In-memory data so the app runs with no backend. */
class FakeProductDataSource : ProductDataSource {
    override suspend fun getProducts(): List<ProductDto> {
        delay(FAKE_LATENCY_MS)
        return products
    }

    private companion object {
        const val FAKE_LATENCY_MS = 400L

        val products = listOf(
            ProductDto("p1", "Cordless drill 18V", "Tools", "Two-speed drill with two batteries and a case.", 34_900),
            ProductDto("p2", "Interior paint white 10 L", "Paint", "Matt, washable, covers up to 120 m².", 12_999),
            ProductDto("p3", "Oak laminate floor 1 m²", "Flooring", "AC4 class, click system, 8 mm.", 4_599),
            ProductDto("p4", "Aluminium ladder 3 x 8", "Tools", "Three-section ladder, max height 5.4 m.", 49_900),
            ProductDto("p5", "Wood screws set 500 pcs", "Hardware", "Assorted sizes in a sorting box.", 3_999),
            ProductDto("p6", "LED ceiling lamp", "Lighting", "24 W, neutral white, IP44.", 8_999),
            ProductDto("p7", "Garden hose 25 m", "Garden", "Anti-kink, with connectors.", 7_499),
            ProductDto("p8", "Toolbox 19\"", "Storage", "Plastic toolbox with metal locks.", 5_999),
        )
    }
}
