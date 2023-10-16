package com.example.gp.datastore

import android.content.Context

public final class SharedPreference {

    public final fun saveToSharedPreference(context: Context, access_token: String?) {
        val sharedPreference =
            context.getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE)
        var editor = sharedPreference?.edit()
        if (editor != null) {
            editor.putString("access_token", access_token)
            editor.commit()
        }
    }
}