package io.rendecano.stox

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

// Add navigation component to bottom nav bar
        val navController = findNavController(R.id.mainNavigationFragment)
//        navController.addOnDestinationChangedListener { _, destination, _ ->
//            when (destination.id) {
//                R.id.homeFragment -> toolbar_title.text = "Home"
//                R.id.teamFragment -> toolbar_title.text = "Team"
//                R.id.venueFragment -> toolbar_title.text = "Venues"
//                R.id.seasonFragment -> toolbar_title.text = "Season"
//            }
//        }
        NavigationUI.setupWithNavController(bottom_navigation, navController)
    }

    override fun onSupportNavigateUp() = findNavController(R.id.mainNavigationFragment).navigateUp()
}
