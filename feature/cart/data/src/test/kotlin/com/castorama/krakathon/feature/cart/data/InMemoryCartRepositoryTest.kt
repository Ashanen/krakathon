package com.castorama.krakathon.feature.cart.data

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext

class InMemoryCartRepositoryTest : BehaviorSpec({

    Given("a cart") {
        val repository = InMemoryCartRepository()
        repository.add("a")
        repository.add("a")

        When("everything is taken") {
            val taken = repository.takeAll()

            Then("the snapshot is returned and the cart is empty") {
                taken shouldBe mapOf("a" to 2)
                repository.quantities.value shouldBe emptyMap()
            }
        }
    }

    Given("adds racing with checkouts on many threads") {
        val repository = InMemoryCartRepository()
        val adds = 10_000

        When("they all finish") {
            val taken = withContext(Dispatchers.Default) {
                val adders = List(adds) { async { repository.add("p${it % 7}") } }
                val takers = List(50) { async { repository.takeAll().values.sum() } }
                adders.awaitAll()
                takers.awaitAll().sum()
            }

            Then("no add is lost: taken + still in cart = all adds") {
                taken + repository.quantities.value.values.sum() shouldBe adds
            }
        }
    }
})
