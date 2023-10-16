package com.example.gp.doctor

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.gp.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class DoctorActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_doctor)
        val access_token = intent.getStringExtra("access_token")
        val uid = intent.getStringExtra("uid")
        saveToSharedPreference(access_token,uid)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        val navController = findNavController(R.id.fragmentContainerView)
        bottomNavigationView.setupWithNavController(navController)
        val appBarConfig = AppBarConfiguration(setOf(R.id.drChatFragment, R.id.settingsFragment2))
        setupActionBarWithNavController(navController, appBarConfig)
    }

    fun saveToSharedPreference(access_token: String?, uid: String?) {
        val sharedPreference =
            this.getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE)
        var editor = sharedPreference?.edit()
        if (editor != null) {
            editor.putString("access_token", access_token)
            editor.putString("uid", uid)
            editor.apply()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return super.onSupportNavigateUp() || findNavController(R.id.fragmentContainerView).navigateUp()
    }
}