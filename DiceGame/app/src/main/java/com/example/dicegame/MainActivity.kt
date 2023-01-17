package com.example.dicegame

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.ImageView
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {
    private val diceNormalIcons: MutableList<Int> = ArrayList()
    private val diceChosenIcons: MutableList<Int> = ArrayList()
    private var diceFaces: MutableList<ImageView> = ArrayList()
    private val diceValues = Array(5) { 0 }
    private val diceChosen = Array(5) { false }
    // 3 rounds per phase
    private var roundCounter = 0
    // 0: roll dice and select, 1: pick scoring, 2: game over
    private var phase = 0
    private lateinit var buttonRollDice: Button
    private lateinit var buttonResetScore: Button
    private lateinit var roundIndicator: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        diceNormalIcons.add(R.drawable.dice_one)
        diceNormalIcons.add(R.drawable.dice_two)
        diceNormalIcons.add(R.drawable.dice_three)
        diceNormalIcons.add(R.drawable.dice_four)
        diceNormalIcons.add(R.drawable.dice_five)
        diceNormalIcons.add(R.drawable.dice_six)
        diceChosenIcons.add(R.drawable.dice_one_chosen)
        diceChosenIcons.add(R.drawable.dice_two_chosen)
        diceChosenIcons.add(R.drawable.dice_three_chosen)
        diceChosenIcons.add(R.drawable.dice_four_chosen)
        diceChosenIcons.add(R.drawable.dice_five_chosen)
        diceChosenIcons.add(R.drawable.dice_six_chosen)

        buttonRollDice = findViewById<Button>(R.id.buttonRollDice)
        buttonResetScore = findViewById<Button>(R.id.buttonResetScore)
        roundIndicator = findViewById<ImageView>(R.id.roundIndicatorImg)

        val imageViewOne = findViewById<ImageView>(R.id.diceOneIndicatorImg)
        val imageViewTwo = findViewById<ImageView>(R.id.diceTwoIndicatorImg)
        val imageViewThree = findViewById<ImageView>(R.id.diceThreeIndicatorImg)
        val imageViewFour = findViewById<ImageView>(R.id.diceFourIndicatorImg)
        val imageViewFive = findViewById<ImageView>(R.id.diceFiveIndicatorImg)

        diceFaces.add(imageViewOne)
        diceFaces.add(imageViewTwo)
        diceFaces.add(imageViewThree)
        diceFaces.add(imageViewFour)
        diceFaces.add(imageViewFive)

        imageViewOne.setOnClickListener() {
            toggleDiceSelection(0)
        }

        imageViewTwo.setOnClickListener() {
            toggleDiceSelection(1)
        }

        imageViewThree.setOnClickListener() {
            toggleDiceSelection(2)
        }

        imageViewFour.setOnClickListener() {
            toggleDiceSelection(3)
        }

        imageViewFive.setOnClickListener() {
            toggleDiceSelection(4)
        }

        buttonRollDice.setOnClickListener() {
            rollDice()
        }

        buttonResetScore.setOnClickListener() {
            buttonRollDice.isEnabled = true
            roundCounter = 0
            roundIndicator.setImageResource(0)
            // clear dice faces
            for(i in 0 .. 4) {
                diceChosen[i] = false
                diceFaces[i].setImageResource(0)
            }
        }
    }

    fun toggleDiceSelection(i: Int) {
        if(roundCounter > 0) {
            if (!diceChosen[i]) {
                // select dice hold
                diceFaces[i].setImageResource(diceChosenIcons[diceValues[i]-1])
                diceChosen[i] = true
            } else {
                // unselect dice hold
                diceFaces[i].setImageResource(diceNormalIcons[diceValues[i]-1])
                diceChosen[i] = false
            }
        }
    }

    fun updateDice() {
        for(i in 0..4) {
            if(!diceChosen[i]) {
                // only get new random number if not chosen to hold
                diceValues[i] = (1..6).random()
                diceFaces[i].setImageResource(diceNormalIcons[diceValues[i]-1])
            }
        }
        println("Dice 1: " + diceValues[0] + "\nDice 2: " + diceValues[1] + "\nDice 3: " + diceValues[2] + "\nDice 4: " + diceValues[3] + "\nDice 5: " + diceValues[4])
    }

    fun rollDice() {
        roundCounter++

        if(roundCounter >= 3) {
            buttonRollDice.isEnabled = false
        }

        // animateDice()
        updateDice()

        if(roundCounter == 1) {
            roundIndicator.setImageResource(diceNormalIcons[0])
        }
        if(roundCounter == 2) {
            roundIndicator.setImageResource(diceNormalIcons[1])
        }
        if(roundCounter == 3) {
            roundIndicator.setImageResource(diceNormalIcons[2])
        }
    }

    fun animateDice() {
        for (i in 1..750) {
            if(i % 75 == 0) {
                for(j in 0..4) {
                    if(!diceChosen[j]) {
                        Handler(Looper.getMainLooper()).postDelayed({ diceFaces[j].setImageResource(diceNormalIcons[(0..5).random()]) }, i.toLong())
                    }
                }
            }
        }
    }

}