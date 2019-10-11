package io.rendecano.stox.list.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.DividerItemDecoration.VERTICAL
import androidx.recyclerview.widget.LinearLayoutManager
import io.rendecano.stox.R
import io.rendecano.stox.common.presentation.ui.BaseFragment
import io.rendecano.stox.list.domain.model.Stock
import io.rendecano.stox.list.presentation.adapter.StockListAdapter
import io.rendecano.stox.list.presentation.viewmodel.StocksListViewModel
import kotlinx.android.synthetic.main.fragment_stocks_list.*


class StocksListFragment : BaseFragment() {

    private lateinit var viewModel: StocksListViewModel
    private lateinit var mAdapter: StockListAdapter
    private var itemList = mutableListOf<Stock>()

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_stocks_list, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        mAdapter = StockListAdapter(activity!!, itemList)
        val mLayoutManager = LinearLayoutManager(activity)
        recyclerView.addItemDecoration(DividerItemDecoration(requireContext(), VERTICAL))
        recyclerView.layoutManager = mLayoutManager
        recyclerView.adapter = mAdapter

        initModel()
    }

    private fun initModel() {
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(StocksListViewModel::class.java)
        viewModel.action(StocksListViewModel.GetStockListAction.Init)

        viewModel.loading.observe {
            // TODO: Show loading
            if (it == true) {

            } else {

            }
        }

        viewModel.stockList.observe {
            itemList.addAll(it ?: listOf())
            mAdapter.notifyDataSetChanged()
        }

        viewModel.error.observe {
            // TODO: Show error message
        }
    }
}