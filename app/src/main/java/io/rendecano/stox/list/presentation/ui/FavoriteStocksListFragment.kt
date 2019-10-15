package io.rendecano.stox.list.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import io.rendecano.stox.R
import io.rendecano.stox.common.presentation.ui.BaseFragment
import io.rendecano.stox.databinding.FragmentStocksListBinding
import io.rendecano.stox.list.presentation.adapter.StockListAdapter
import io.rendecano.stox.list.presentation.viewmodel.FavoritesStocksListViewModel
import kotlinx.android.synthetic.main.fragment_stocks_list.*

class FavoriteStocksListFragment : BaseFragment() {

    private lateinit var viewModel: FavoritesStocksListViewModel
    private lateinit var mAdapter: StockListAdapter
    private lateinit var viewBinding: FragmentStocksListBinding

    private val favoriteListener by lazy {
        object : StockListAdapter.OnFavoriteListener {
            override fun onSelect(symbol: String, selected: Boolean) {
                viewModel.updateFavorite(symbol, selected).observe { list ->
                    list.let {
                        mAdapter.setData(ArrayList(it ?: listOf()))
                        mAdapter.notifyDataSetChanged()
                    }
                }
            }
        }
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        viewBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_stocks_list, container, false)
        return viewBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initModel()

        mAdapter = StockListAdapter(this, favoriteListener)
        val mLayoutManager = LinearLayoutManager(activity)
        recyclerView.addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
        recyclerView.layoutManager = mLayoutManager
        recyclerView.adapter = mAdapter

        viewBinding.txtTitle.text = "Favorites"
    }

    private fun initModel() {
        viewBinding.loading = true
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(FavoritesStocksListViewModel::class.java)

        viewModel.favoriteStockList.observe { list ->
            list.let {

                viewBinding.loading = list?.isNullOrEmpty()

                mAdapter.setData(ArrayList(it ?: listOf()))
                mAdapter.notifyDataSetChanged()
            }
        }

//        viewModel.error.observe {
//            // TODO: Show error message
//        }

        //        viewModel.loading.observe {
//            // TODO: Show loading
//            if (it == true) {
//
//            } else {
//
//            }
//        }
    }
}