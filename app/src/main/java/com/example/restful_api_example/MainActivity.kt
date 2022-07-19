package com.example.restful_api_example

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    lateinit var navController: NavController
    lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_container) as NavHostFragment
        navController = navHostFragment.findNavController()

        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.fragment_main),
            drawer_layout
        )

        setSupportActionBar(toolbar)
        setupActionBarWithNavController(navController, appBarConfiguration)
        nav_view.setupWithNavController(navController)
    }

//    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        menuInflater.inflate(R.menu.bottom_nav_menu, menu)
//        return true
//    }
//
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        return item.onNavDestinationSelected(navController) || super.onOptionsItemSelected(item)
//    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}