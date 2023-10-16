package com.example.gp

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.ListFragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.example.gp.list.listFragment
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.navigation.fragment.NavHostFragment
import com.example.gp.datastore.SharedPreference


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navController = findNavController(R.id.fragmentContainerView2)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val access_token = intent.getStringExtra("access_token")
        val sharedPreference =
            this.getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE)
        var editor = sharedPreference?.edit()
        if (editor != null) {
            editor.putString("access_token", access_token)
            editor.commit()
        }
        Log.v("tokenIntent", access_token.toString())
        Log.v("tokenIntent", this.getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE)
            .getString("access_token", "").toString())

        Handler(Looper.getMainLooper()).postDelayed({
            if (this.getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE)
                    .getString("access_token", "") != "null"
            ) {
                Log.v("token",
                    this.getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE)
                        .getString("access_token", "").toString()
                )
//                val intent = Intent(this, MainActivity2::class.java)
//                startActivity(intent)
                navController.navigate(R.id.action_welcomeFragment_to_patientLoginFragment)
            } else{
                navController.navigate(R.id.action_welcomeFragment_to_patientLoginFragment)
            }

        }, 3000)

        supportActionBar?.hide()
    }

    override fun onSupportNavigateUp(): Boolean {
        return super.onSupportNavigateUp() || findNavController(R.id.fragmentContainerView2).navigateUp()
    }
}
