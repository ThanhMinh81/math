package com.example.matheasyapp

// File: SharedPreferencesHelper.kt
import android.content.Context
import android.content.SharedPreferences

class SharedPreferencesHelper(context: Context) {

    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE)

    // Save a string value from
    fun saveValueFrom(key: String, valueFrom: String) {
        val editor = sharedPreferences.edit()
        editor.putString(key, valueFrom)
        editor.apply() // or editor.commit() for synchronous saving
    }

    // Save a string value to
    fun saveValueTo(key: String, valueTo: String) {
        val editor = sharedPreferences.edit()
        editor.putString(key, valueTo)
        editor.apply() // or editor.commit() for synchronous saving
    }

    // Retrieve a string value
    fun getString(key: String, defaultValue: String? = null): String? {
        return sharedPreferences.getString(key, defaultValue)
    }



}
