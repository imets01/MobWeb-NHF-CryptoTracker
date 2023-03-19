package hu.bme.aut.android.cryptotracker.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cryptoitem")
data class CryptoItem(
    @ColumnInfo(name = "id") @PrimaryKey(autoGenerate = true) var id: Long? = null,
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "symbol") var symbol: String,
    @ColumnInfo(name = "priceWhenBought") var priceWhenBought: Double,
    @ColumnInfo(name = "amount") var amount: Double
) {

}
