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
    private lateinit var buttonAces: ImageView
    private lateinit var buttonTwos: ImageView
    private lateinit var buttonThrees: ImageView
    private lateinit var buttonFours: ImageView
    private lateinit var buttonFives: ImageView
    private lateinit var buttonSixes: ImageView
    private lateinit var buttonThreeOfKind: ImageView
    private lateinit var buttonFourOfKind: ImageView
    private lateinit var buttonFullHouse: ImageView
    private lateinit var buttonSmallStraight: ImageView
    private lateinit var buttonLargeStraight: ImageView
    private lateinit var buttonFiveOfKind: ImageView
    private lateinit var buttonChance: ImageView
    private lateinit var textViewScoreAces: TextView
    private lateinit var textViewScoreTwos: TextView
    private lateinit var textViewScoreThrees: TextView
    private lateinit var textViewScoreFours: TextView
    private lateinit var textViewScoreFives: TextView
    private lateinit var textViewScoreSixes: TextView
    private lateinit var textViewScoreThreeOfKind: TextView
    private lateinit var textViewScoreFourOfKind: TextView
    private lateinit var textViewScoreFullHouse: TextView
    private lateinit var textViewScoreSmallStraight: TextView
    private lateinit var textViewScoreLargeStraight: TextView
    private lateinit var textViewScoreFiveOfKind: TextView
    private lateinit var textViewScoreChance: TextView

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

        buttonAces = findViewById<ImageView>(R.id.imageViewAces)
        buttonTwos = findViewById<ImageView>(R.id.imageViewTwos)
        buttonThrees = findViewById<ImageView>(R.id.imageViewThrees)
        buttonFours = findViewById<ImageView>(R.id.imageViewFours)
        buttonFives = findViewById<ImageView>(R.id.imageViewFives)
        buttonSixes = findViewById<ImageView>(R.id.imageViewSixes)
        buttonThreeOfKind = findViewById<ImageView>(R.id.imageViewThreeOfKind)
        buttonFourOfKind = findViewById<ImageView>(R.id.imageViewFourOfKind)
        buttonFullHouse = findViewById<ImageView>(R.id.imageViewFullHouse)
        buttonSmallStraight = findViewById<ImageView>(R.id.imageViewSmallStraight)
        buttonLargeStraight = findViewById<ImageView>(R.id.imageViewLargeStraight)
        buttonFiveOfKind = findViewById<ImageView>(R.id.imageViewFiveOfKind)
        buttonChance = findViewById<ImageView>(R.id.imageViewChance)
        textViewScoreAces = findViewById<TextView>(R.id.scoreAces)
        textViewScoreTwos = findViewById<TextView>(R.id.scoreTwos)
        textViewScoreThrees = findViewById<TextView>(R.id.scoreThrees)
        textViewScoreFours = findViewById<TextView>(R.id.scoreFours)
        textViewScoreFives = findViewById<TextView>(R.id.scoreFives)
        textViewScoreSixes = findViewById<TextView>(R.id.scoreSixes)
        textViewScoreThreeOfKind = findViewById<TextView>(R.id.scoreThreeOfKind)
        textViewScoreFourOfKind = findViewById<TextView>(R.id.scoreFourOfKind)
        textViewScoreFullHouse = findViewById<TextView>(R.id.scoreFullHouse)
        textViewScoreSmallStraight = findViewById<TextView>(R.id.scoreSmallStraight)
        textViewScoreLargeStraight = findViewById<TextView>(R.id.scoreLargeStraight)
        textViewScoreFiveOfKind = findViewById<TextView>(R.id.scoreFiveOfKind)
        textViewScoreChance = findViewById<TextView>(R.id.scoreChance)

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
            val calcResults = CalculateResults()
            println(calcResults.sumNofKind(1, diceValues))
            buttonAces.setImageResource(R.drawable.button_pressed)
            Handler(Looper.getMainLooper()).postDelayed({ buttonAces.setImageResource(R.drawable.button_normal) }, 500)
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

    fun optionChosen() {
        roundCounter++
        diceRollCounter = 0
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