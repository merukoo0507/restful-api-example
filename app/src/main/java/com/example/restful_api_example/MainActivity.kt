package com.example.restful_api_example

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.commit
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.restful_api_example.ui.main.ShareViewModel
import com.example.restful_api_example.ui.profile.ProfileFragment
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

        var shareViewModel = ViewModelProvider(this).get(ShareViewModel::class.java)
        customLayout.setShareViewModel(shareViewModel)
        shareViewModel.navigationDirection.observe(this, Observer {
            when (it) {
                "Right" -> {
                    if (supportFragmentManager.backStackEntryCount > 0)
                        supportFragmentManager.popBackStack()
                    else if (!drawer_layout.isDrawerOpen(GravityCompat.START))
                        drawer_layout.openDrawer(GravityCompat.START)
                }
                "Left" -> {
                    //預設已有實作closeDrawer
                    if (drawer_layout.isDrawerOpen(GravityCompat.START))
                        drawer_layout.closeDrawer(GravityCompat.START)
                    if (supportFragmentManager.findFragmentById(R.id.nav_host_fragment_container) is ProfileFragment) {
                        return@Observer
                    }
                    supportFragmentManager.commit {
                        add(R.id.nav_host_fragment_container, ProfileFragment::class.java, null)
                        addToBackStack("profile")
                    }
                }
            }
        })
    }

//    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        menuInflater.inflate(R.menu.bottom_nav_menu, menu)
//        return true
//    }
//
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        return item.onNavDestinationSelected(navController) || super.onOptionsItemSelected(item)
//    }
    // 上面的按鈕委託給NavController.navigateUp
    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
}