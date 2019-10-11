package io.rendecano.stox.common.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import io.rendecano.stox.BuildConfig
import io.rendecano.stox.list.data.local.dao.StockDao
import io.rendecano.stox.list.data.local.model.StockEntity

@Database(
        entities = [StockEntity::class],
        version = BuildConfig.VERSION_CODE
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun stockDao(): StockDao
}