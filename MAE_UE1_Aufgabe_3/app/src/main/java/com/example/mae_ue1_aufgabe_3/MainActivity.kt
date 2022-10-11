package com.example.mae_ue1_aufgabe_3

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.google.android.material.textfield.TextInputEditText

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val textInputTemperature: TextInputEditText = findViewById<TextInputEditText>(R.id.textInputTemperature)
        val textViewResult: TextView = findViewById<TextView>(R.id.textViewResult)
        val celsius_button: Button = findViewById<Button>(R.id.celsius_button)
        val fahrenheit_button: Button = findViewById<Button>(R.id.fahrenheit_button)

        celsius_button.setOnClickListener {
            // convert to celsius
            val celsius = convertToCelsius(textInputTemperature.text.toString().toDouble())
            textViewResult.setText(celsius.toString() + " °C")
        }

        fahrenheit_button.setOnClickListener {
            // convert to fahrenheit
            val fahrenheit = convertToFahrenheit(textInputTemperature.text.toString().toDouble())
            textViewResult.setText(fahrenheit.toString() + " °F")
        }
    }

    fun convertToCelsius(f: Double): Double {
        return (f - 32.0) * (5.0 / 9.0)
    }

    fun convertToFahrenheit(c: Double): Double {
        return c * (9.0 / 5.0) + 32.0
    }
}