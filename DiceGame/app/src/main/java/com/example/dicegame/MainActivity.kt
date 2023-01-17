package com.example.dicegame

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private val diceNormalIcons: MutableList<Int> = ArrayList()
    private val diceChosenIcons: MutableList<Int> = ArrayList()
    private var diceFaces: MutableList<ImageView> = ArrayList()
    private val diceValues = Array(5) { 0 }
    private val diceChosen = Array(5) { false }
    // 3 rounds per phase
    private var diceRollCounter = 0
    // 13 score possibilities
    private var roundCounter = 0
    // 0: roll dice and select, 1: pick scoring, 2: game over
    private var phase = 0
    private lateinit var buttonRollDice: Button
    private lateinit var buttonResetScore: Button
    private lateinit var roundIndicator: ImageView
    private lateinit var buttonAces: TextView
    private lateinit var buttonTwos: TextView
    private lateinit var buttonThrees: TextView
    private lateinit var buttonFours: TextView
    private lateinit var buttonFives: TextView
    private lateinit var buttonSixes: TextView
    private lateinit var buttonThreeOfKind: TextView
    private lateinit var buttonFourOfKind: TextView
    private lateinit var buttonFullHouse: TextView
    private lateinit var buttonSmallStraight: TextView
    private lateinit var buttonLargeStraight: TextView
    private lateinit var buttonFiveOfKind: TextView
    private lateinit var buttonChance: TextView

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

        buttonAces = findViewById<TextView>(R.id.textViewAces)
        buttonTwos = findViewById<TextView>(R.id.textViewTwos)
        buttonThrees = findViewById<TextView>(R.id.textViewThrees)
        buttonFours = findViewById<TextView>(R.id.textViewFours)
        buttonFives = findViewById<TextView>(R.id.textViewFives)
        buttonSixes = findViewById<TextView>(R.id.textViewSixes)
        buttonThreeOfKind = findViewById<TextView>(R.id.textViewThreeOfKind)
        buttonFourOfKind = findViewById<TextView>(R.id.textViewFourOfKind)
        buttonFullHouse = findViewById<TextView>(R.id.textViewFullHouse)
        buttonSmallStraight = findViewById<TextView>(R.id.textViewSmallStraight)
        buttonLargeStraight = findViewById<TextView>(R.id.textViewLargeStraight)
        buttonFiveOfKind = findViewById<TextView>(R.id.textViewFiveOfKind)
        buttonChance = findViewById<TextView>(R.id.textViewChance)

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
            diceRollCounter = 0
            roundIndicator.setImageResource(0)
            // clear dice faces
            for(i in 0 .. 4) {
                diceChosen[i] = false
                diceFaces[i].setImageResource(0)
            }
        }

        buttonAces.setOnClickListener() {
            print("clicked")
            val calcResults = CalculateResults()
            print(calcResults.sumNofKind(1, diceValues))
        }
    }

    fun toggleDiceSelection(i: Int) {
        if(diceRollCounter > 0) {
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
    }

    fun rollDice() {
        diceRollCounter++

        if(diceRollCounter >= 3) {
            buttonRollDice.isEnabled = false
        }

        // animateDice()
        updateDice()

        if(diceRollCounter == 1) {
            roundIndicator.setImageResource(diceChosenIcons[0])
        }
        if(diceRollCounter == 2) {
            roundIndicator.setImageResource(diceChosenIcons[1])
        }
        if(diceRollCounter == 3) {
            roundIndicator.setImageResource(diceChosenIcons[2])
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