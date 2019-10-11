package io.rendecano.stox.list.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.rendecano.stox.list.data.local.model.StockEntity

@Dao
interface StockDao {

    @Query("Select * from StockEntity")
    fun getStocks(): List<StockEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(store: List<StockEntity>)

}