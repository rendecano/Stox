package io.rendecano.stox.detail.presentation.ui

import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import io.rendecano.stox.R
import io.rendecano.stox.common.di.Injectable
import io.rendecano.stox.common.presentation.ui.SYMBOL
import io.rendecano.stox.databinding.ActivityStockDetailsBinding
import io.rendecano.stox.detail.domain.model.PriceRangeFilter
import io.rendecano.stox.detail.presentation.viewmodel.StocksDetailsViewModel
import javax.inject.Inject


class StockDetailsActivity : AppCompatActivity(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewBinding: ActivityStockDetailsBinding
    private lateinit var viewModel: StocksDetailsViewModel
    private lateinit var symbol: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = DataBindingUtil.setContentView(
                this, R.layout.activity_stock_details)

        setSupportActionBar(viewBinding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        viewBinding.toolbar.title = ""

        symbol = intent.getStringExtra(SYMBOL) ?: ""
        viewBinding.toolbarTitle.text = symbol

        viewBinding.chart.description.isEnabled = false
        viewBinding.chart.setBackgroundColor(ContextCompat.getColor(this, R.color.chartBackground))

        initModel()
    }

    private fun initModel() {
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(StocksDetailsViewModel::class.java)

        viewModel.stockDetails.observe { stock ->
            viewBinding.stock = stock
        }

        viewModel.historicalPriceDetails.observe { lineDataSet ->
            lineDataSet?.let {
                viewBinding.chart.data = LineData(arrayListOf<ILineDataSet>(it))
                viewBinding.chart.invalidate()
            }
        }

        viewModel.error.observe {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        }

        viewModel.getStockDetails(symbol)
        viewModel.getHistoricalPriceDetails(symbol)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.price_history_filter, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
            R.id.three_months -> viewModel.getHistoricalPriceDetails(symbol, PriceRangeFilter.QUARTER)
            R.id.six_months -> viewModel.getHistoricalPriceDetails(symbol, PriceRangeFilter.SEMI_ANNUAL)
            R.id.one_year -> viewModel.getHistoricalPriceDetails(symbol, PriceRangeFilter.ANNUAL)
            R.id.three_year -> viewModel.getHistoricalPriceDetails(symbol, PriceRangeFilter.THREE_YEARS)
        }
        return super.onOptionsItemSelected(item)
    }

    fun <T> LiveData<T>.observe(observe: (T?) -> Unit) =
            observe(this@StockDetailsActivity, Observer { observe(it) })
}