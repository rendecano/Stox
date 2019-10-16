package io.rendecano.stox.list.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import io.rendecano.stox.list.data.local.model.PriceHistoryEntity
import io.rendecano.stox.list.data.local.model.StockEntity

@Dao
interface PriceHistoryDao {

    @Query("Select * from PriceHistoryEntity where symbol = :symbol and date = :dateLabel order by date ASC")
    fun isPriceExisting(symbol: String, dateLabel: String): PriceHistoryEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(priceHistoryEntityList: List<PriceHistoryEntity>)

    @Query("Select * from PriceHistoryEntity where symbol = :symbol")
    fun getHistoricalPrice(symbol: String): LiveData<List<PriceHistoryEntity>>
}