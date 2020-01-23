package com.revolut.androidtest.domain

import com.revolut.androidtest.domain.model.Rates
import io.reactivex.Single

interface RateRepository {
    fun getRates(): Single<Rates>
}