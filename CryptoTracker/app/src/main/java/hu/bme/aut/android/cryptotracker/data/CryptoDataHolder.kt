package hu.bme.aut.android.cryptotracker.data

import hu.bme.aut.android.cryptotracker.model.Data

interface CryptoDataHolder {
    fun getCryptoData(): Data?
}