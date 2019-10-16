package io.rendecano.stox

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import io.rendecano.stox.common.presentation.ui.stringify
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navController = findNavController(R.id.mainNavigationFragment)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.stocksListFragment -> toolbar_title.text = R.string.stocks.stringify(resources)
                R.id.favoriteStocksListFragment -> toolbar_title.text = R.string.favorites.stringify(resources)
                R.id.settingsFragment -> toolbar_title.text = R.string.settings.stringify(resources)
            }
        }
        NavigationUI.setupWithNavController(bottom_navigation, navController)
    }

    override fun onSupportNavigateUp() = findNavController(R.id.mainNavigationFragment).navigateUp()
}
