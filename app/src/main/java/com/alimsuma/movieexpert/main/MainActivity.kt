package com.alimsuma.movieexpert.main

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.core.view.OnApplyWindowInsetsListener
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.alimsuma.movieexpert.R
import com.alimsuma.movieexpert.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!
    private var windowInsetsListener: OnApplyWindowInsetsListener? = null

    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        applyTheme()
        windowInsetsListener = OnApplyWindowInsetsListener { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        ViewCompat.setOnApplyWindowInsetsListener(binding.main, windowInsetsListener)

        val navView: BottomNavigationView = binding.navView
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
        val navController = navHostFragment.navController

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home,
                R.id.favorite_nav,
                R.id.navigation_setting,
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navController.let {
            setupActionBarWithNavController(it, appBarConfiguration)
            navView.setupWithNavController(it)
        }
    }

    private fun applyTheme() {
        mainViewModel.getThemeSetting().observe(this) { isDarkModeActive ->
            AppCompatDelegate.setDefaultNightMode(
                if (isDarkModeActive) AppCompatDelegate.MODE_NIGHT_YES
                else AppCompatDelegate.MODE_NIGHT_NO
            )
            updateStatusBarColor(isDarkModeActive)
        }
    }

    private fun updateStatusBarColor(isDarkModeActive: Boolean) {
        val colorRes = if (isDarkModeActive) R.color.primaryColorNight else R.color.primaryColor
        window.statusBarColor = ContextCompat.getColor(this, colorRes)
    }

    override fun onDestroy() {
        super.onDestroy()
        windowInsetsListener?.let {
            ViewCompat.setOnApplyWindowInsetsListener(binding.main, null)
        }
        windowInsetsListener = null
        _binding = null
    }
}