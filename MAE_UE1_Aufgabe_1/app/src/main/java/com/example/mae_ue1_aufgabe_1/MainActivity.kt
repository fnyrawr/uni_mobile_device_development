package com.example.mae_ue1_aufgabe_1

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val textView1 = findViewById<View>(R.id.textView1) as TextView
        textView1.setOnClickListener { Log.d("MainActivity", "Option 1 wurde ausgewählt") }
        val textView2 = findViewById<View>(R.id.textView2) as TextView
        textView2.setOnClickListener { Log.d("MainActivity", "Option 2 wurde ausgewählt") }
        val textView3 = findViewById<View>(R.id.textView3) as TextView
        textView3.setOnClickListener { Log.d("MainActivity", "Option 3 wurde ausgewählt") }
        val textView4 = findViewById<View>(R.id.textView4) as TextView
        textView4.setOnClickListener { Log.d("MainActivity", "Option 4 wurde ausgewählt") }
    }

}