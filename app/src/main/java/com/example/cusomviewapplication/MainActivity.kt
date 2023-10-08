package com.example.cusomviewapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity(R.layout.activity_main) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        findViewById<MyCustomView>(R.id.stats).apply {
            cost = 2000F
            data = listOf(
                500F,
                500F,
                500F,
                500F,
            )
        }
    }
}