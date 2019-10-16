package io.rendecano.stox.detail.presentation.ui

import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import io.rendecano.stox.R
import io.rendecano.stox.common.di.Injectable
import io.rendecano.stox.databinding.ActivityStockDetailsBinding
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

        symbol = intent.getStringExtra("SYMBOL") ?: ""
        viewBinding.toolbarTitle.text = symbol

        initModel()
    }

    private fun initModel() {
//        viewBinding.loading = true
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(StocksDetailsViewModel::class.java)

        viewModel.stockDetails.observe { stock ->

            //            viewBinding.loading = list?.isNullOrEmpty()
            viewBinding.stock = stock
        }

        viewModel.historicalPriceDetails.observe { lineDataSet ->
            lineDataSet?.let {
                val formatter = object : ValueFormatter() {
                    override fun getAxisLabel(value: Float, axis: AxisBase?): String {
                        return lineDataSet.getEntryForIndex(value.toInt())?.data as String
                    }
                }

                val xAxis = viewBinding.chart.xAxis
                xAxis.valueFormatter = formatter
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

    fun <T> LiveData<T>.observe(observe: (T?) -> Unit) =
            observe(this@StockDetailsActivity, Observer { observe(it) })
}