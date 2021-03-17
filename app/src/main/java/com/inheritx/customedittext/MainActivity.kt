package com.inheritx.customedittext

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.inheritx.standardedittext.CustomFrameLayout


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        setupErrorButton()
    }

    private fun setupErrorButton() {
        val errorButton: Button = findViewById(R.id.error_button)
        val errorField: CustomFrameLayout = findViewById(R.id.customLayout2)
        errorButton.setOnClickListener { errorField.setError("Invalid code.", false) }
    }
}