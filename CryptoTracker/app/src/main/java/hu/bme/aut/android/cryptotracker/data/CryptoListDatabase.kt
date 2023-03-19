package hu.bme.aut.android.cryptotracker.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [CryptoItem::class], version = 2)
abstract class CryptoListDatabase : RoomDatabase() {
    abstract fun cryptoItemDao(): CryptoItemDao

    companion object {
        fun getDatabase(applicationContext: Context): CryptoListDatabase {
            return Room.databaseBuilder(
                applicationContext,
                CryptoListDatabase::class.java,
                "crypto-list"
            ).fallbackToDestructiveMigration()
                .build();
        }
    }
}