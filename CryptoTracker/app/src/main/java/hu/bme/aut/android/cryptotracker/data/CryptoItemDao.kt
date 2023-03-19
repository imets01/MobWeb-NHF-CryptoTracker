package hu.bme.aut.android.cryptotracker.data

import androidx.room.*

@Dao
interface CryptoItemDao {
    @Query("SELECT * FROM cryptoitem")
    fun getAll(): List<CryptoItem>

    @Insert
    fun insert(cryptoItems: CryptoItem): Long

    @Update
    fun update(cryptoItems: CryptoItem)

    @Delete
    fun deleteItem(cryptoItems: CryptoItem)
}