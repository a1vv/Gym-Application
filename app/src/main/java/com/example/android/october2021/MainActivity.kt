package com.example.android.october2021

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.android.october2021.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}