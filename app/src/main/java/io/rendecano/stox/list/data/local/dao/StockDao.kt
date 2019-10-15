package io.rendecano.stox.list.data.local.dao

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.*
import io.rendecano.stox.list.data.local.model.StockEntity

@Dao
interface StockDao {

    @Query("Select * from StockEntity")
    fun getStocks(): List<StockEntity>

    @Query("Select * from StockEntity where isFavorite = 1")
    fun getFavoriteStocks(): LiveData<List<StockEntity>>

    @Query("Select * from StockEntity where isFavorite = 1")
    fun getFavoriteStocksList(): List<StockEntity>

    @Query("Select * from StockEntity where symbol = :symbol")
    fun getStock(symbol: String): StockEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(store: List<StockEntity>)

    @Update
    fun updateStock(stockEntity: StockEntity)

    @Query("Select * from StockEntity")
    fun getStocksList(): LiveData<List<StockEntity>>
}