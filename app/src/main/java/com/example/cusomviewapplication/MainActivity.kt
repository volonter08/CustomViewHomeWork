package com.example.cusomviewapplication

import android.animation.ObjectAnimator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.LinearInterpolator
import java.util.Objects

class MainActivity : AppCompatActivity(R.layout.activity_main) {
    lateinit var myCustomView:MyCustomView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        myCustomView = findViewById<MyCustomView>(R.id.stats).apply {
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