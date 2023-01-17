package com.example.dicegame

import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.graphics.drawable.toDrawable
import java.util.*
import kotlin.collections.ArrayList
import kotlin.random.Random.Default.nextInt

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val list: MutableList<Int> = ArrayList()
        var counter = 0
        list.add(R.drawable.dice_one)
        list.add(R.drawable.dice_two)
        list.add(R.drawable.dice_three)
        list.add(R.drawable.dice_four)
        list.add(R.drawable.dice_five)
        list.add(R.drawable.dice_six)
        val buttonRollDice = findViewById<Button>(R.id.buttonRollDice)
        val buttonResetScore = findViewById<Button>(R.id.buttonResetScore)
        val roundIndicatior = findViewById<ImageView>(R.id.roundIndicatorImg)
        val diceOneSelection = findViewById<ImageView>(R.id.diceOneIndicatorImg)
        val diceTwoSelection = findViewById<ImageView>(R.id.diceTwoIndicatorImg)
        val diceThreeSelection = findViewById<ImageView>(R.id.diceThreeIndicatorImg)
        val diceFourSelection = findViewById<ImageView>(R.id.diceFourIndicatorImg)
        val diceFiveSelection = findViewById<ImageView>(R.id.diceFiveIndicatorImg)
        buttonRollDice.setOnClickListener() {

            counter = counter + 1

            if(counter >= 3) {
                buttonRollDice.isEnabled = false
            }

            rollDice(diceOneSelection, diceTwoSelection, diceThreeSelection, diceFourSelection, diceFiveSelection, list)

            if(counter == 1) {
                roundIndicatior.setImageResource(list[0])
            }
            if(counter == 2) {
                roundIndicatior.setImageResource(list[1])
            }
            if(counter == 3) {
                roundIndicatior.setImageResource(list[2])
            }
        }

        buttonResetScore.setOnClickListener() {
            buttonRollDice.isEnabled = true
            counter = 0
            roundIndicatior.setImageResource(0)
            diceOneSelection.setImageResource(0)
            diceTwoSelection.setImageResource(0)
            diceThreeSelection.setImageResource(0)
            diceFourSelection.setImageResource(0)
            diceFiveSelection.setImageResource(0)
        }
    }

    fun rollDice(iv1: ImageView, iv2: ImageView, iv3: ImageView, iv4: ImageView, iv5: ImageView, list: MutableList<Int>) {
        for (i in 1..750) {
            if(i % 7 == 0) {
                Handler(Looper.getMainLooper()).postDelayed({ iv1.setImageResource(list.random()) },
                    i.toLong()
                )
                Handler(Looper.getMainLooper()).postDelayed({ iv2.setImageResource(list.random()) },
                    i.toLong()
                )
                Handler(Looper.getMainLooper()).postDelayed({ iv3.setImageResource(list.random()) },
                    i.toLong()
                )
                Handler(Looper.getMainLooper()).postDelayed({ iv4.setImageResource(list.random()) },
                    i.toLong()
                )
                Handler(Looper.getMainLooper()).postDelayed({ iv5.setImageResource(list.random()) },
                    i.toLong()
                )
            }
        }
    }

}