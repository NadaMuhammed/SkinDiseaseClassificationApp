package com.example.gp

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
//        supportActionBar?.hide()
        val access_token = intent.getStringExtra("access_token")
        val uid = intent.getStringExtra("uid")
        Log.v("uid",uid.toString())
        saveToSharedPreference(access_token,uid)
        Log.v("token2", this.getSharedPreferences("PREFERENCE_NAME",Context.MODE_PRIVATE).getString("access_token","").toString())
//        Log.v("type", this.getSharedPreferences("PREFERENCE_NAME",Context.MODE_PRIVATE).getString("type","").toString())

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        val navController = findNavController(R.id.fragmentContainerView)
        bottomNavigationView.setupWithNavController(navController)
        val appBarConfig = AppBarConfiguration(setOf(R.id.listFragment, R.id.newScanFragment, R.id.chatFragment, R.id.settingsFragment))
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