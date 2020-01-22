package com.revolut.androidtest.domain

import io.reactivex.Single

interface RateRepository {
    fun getRates(): Single<Rates>
}