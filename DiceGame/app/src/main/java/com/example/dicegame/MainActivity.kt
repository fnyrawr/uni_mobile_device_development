package com.example.dicegame

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {
    // sensor
    private var sensorEnabled = true
    private val diceNormalIcons: MutableList<Int> = ArrayList()
    private val diceChosenIcons: MutableList<Int> = ArrayList()
    private var diceFaces: MutableList<ImageView> = ArrayList()
    private val diceValues = Array(5) { 0 }
    private val diceChosen = Array(5) { false }
    // 3 rounds per phase
    private var diceRollCounter = 0
    // 13 score possibilities
    private var roundCounter = 0
    // scores
    private var scoreArray = Array(13) { -1 }
    private var upperScore = 0
    private var bonus = 0
    private var lowerScore = 0
    private var total = 0
    // UI elements
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
    private lateinit var textViewScoreUpperTotal: TextView
    private lateinit var textViewScoreBonus: TextView
    private lateinit var textViewScoreLowerTotal: TextView
    private lateinit var textViewScoreTotal: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        val sensorShake = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        val sensorEventListener: SensorEventListener = object : SensorEventListener {
            override fun onSensorChanged(sensorEvent: SensorEvent) {
                if (sensorEvent != null) {
                    if(sensorEnabled) {
                        val x_accl = sensorEvent.values[0]
                        val y_accl = sensorEvent.values[1]
                        val z_accl = sensorEvent.values[2]
                        val floatSum = Math.abs(x_accl) + Math.abs(y_accl) + Math.abs(z_accl)
                        println(floatSum)
                        if (floatSum > 14) {
                            sensorEnabled = true
                            rollDice()
                        }
                    }
                }
            }

            override fun onAccuracyChanged(sensor: Sensor, i: Int) {
            }
        }
        sensorManager.registerListener(
            sensorEventListener,
            sensorShake,
            SensorManager.SENSOR_DELAY_NORMAL
        )

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
        textViewScoreUpperTotal = findViewById<TextView>(R.id.scoreUpperTotal)
        textViewScoreBonus = findViewById<TextView>(R.id.scoreBonus)
        textViewScoreLowerTotal = findViewById<TextView>(R.id.scoreLowerTotal)
        textViewScoreTotal = findViewById<TextView>(R.id.scoreTotal)

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
            resetScore()
        }

        buttonAces.setOnClickListener() {
            if(diceRollCounter > 0) {
                if (scoreArray[0] == -1) {
                    scoreArray[0] = CalculateResults().sumNofKind(1, diceValues)
                    textViewScoreAces.text = scoreArray[0].toString()
                    optionChosen()
                }
                buttonAces.setImageResource(R.drawable.button_pressed)
            }
        }

        buttonTwos.setOnClickListener() {
            if(diceRollCounter > 0) {
                if (scoreArray[1] == -1) {
                    scoreArray[1] = CalculateResults().sumNofKind(2, diceValues)
                    textViewScoreTwos.text = scoreArray[1].toString()
                    optionChosen()
                }
                buttonTwos.setImageResource(R.drawable.button_pressed)
            }
        }

        buttonThrees.setOnClickListener() {
            if(diceRollCounter > 0) {
                if (scoreArray[2] == -1) {
                    scoreArray[2] = CalculateResults().sumNofKind(3, diceValues)
                    textViewScoreThrees.text = scoreArray[2].toString()
                    optionChosen()
                }
                buttonThrees.setImageResource(R.drawable.button_pressed)
            }
        }

        buttonFours.setOnClickListener() {
            if(diceRollCounter > 0) {
                if (scoreArray[3] == -1) {
                    scoreArray[3] = CalculateResults().sumNofKind(4, diceValues)
                    textViewScoreFours.text = scoreArray[3].toString()
                    optionChosen()
                }
                buttonFours.setImageResource(R.drawable.button_pressed)
            }
        }

        buttonFives.setOnClickListener() {
            if(diceRollCounter > 0) {
                if (scoreArray[4] == -1) {
                    scoreArray[4] = CalculateResults().sumNofKind(5, diceValues)
                    textViewScoreFives.text = scoreArray[4].toString()
                    optionChosen()
                }
                buttonFives.setImageResource(R.drawable.button_pressed)
            }
        }

        buttonSixes.setOnClickListener() {
            if(diceRollCounter > 0) {
                if (scoreArray[5] == -1) {
                    scoreArray[5] = CalculateResults().sumNofKind(6, diceValues)
                    textViewScoreSixes.text = scoreArray[5].toString()
                    optionChosen()
                }
                buttonSixes.setImageResource(R.drawable.button_pressed)
            }
        }

        buttonThreeOfKind.setOnClickListener() {
            if(diceRollCounter > 0) {
                if (scoreArray[6] == -1) {
                    scoreArray[6] = CalculateResults().sumThreeOfAKind(diceValues)
                    textViewScoreThreeOfKind.text = scoreArray[6].toString()
                    optionChosen()
                }
                buttonThreeOfKind.setImageResource(R.drawable.button_pressed)
            }
        }

        buttonFourOfKind.setOnClickListener() {
            if(diceRollCounter > 0) {
                if (scoreArray[7] == -1) {
                    scoreArray[7] = CalculateResults().sumFourOfAKind(diceValues)
                    textViewScoreFourOfKind.text = scoreArray[7].toString()
                    optionChosen()
                }
                buttonFourOfKind.setImageResource(R.drawable.button_pressed)
            }
        }

        buttonFullHouse.setOnClickListener() {
            if(diceRollCounter > 0) {
                if (scoreArray[8] == -1) {
                    scoreArray[8] = CalculateResults().sumFullHouse(diceValues)
                    textViewScoreFullHouse.text = scoreArray[8].toString()
                    optionChosen()
                }
                buttonFullHouse.setImageResource(R.drawable.button_pressed)
            }
        }

        buttonSmallStraight.setOnClickListener() {
            if(diceRollCounter > 0) {
                if (scoreArray[9] == -1) {
                    scoreArray[9] = CalculateResults().sumSmallStraight(diceValues)
                    textViewScoreSmallStraight.text = scoreArray[9].toString()
                    optionChosen()
                }
                buttonSmallStraight.setImageResource(R.drawable.button_pressed)
            }
        }

        buttonLargeStraight.setOnClickListener() {
            if(diceRollCounter > 0) {
                if (scoreArray[10] == -1) {
                    scoreArray[10] = CalculateResults().sumLargeStraight(diceValues)
                    textViewScoreLargeStraight.text = scoreArray[10].toString()
                    optionChosen()
                }
                buttonLargeStraight.setImageResource(R.drawable.button_pressed)
            }
        }

        buttonFiveOfKind.setOnClickListener() {
            if(diceRollCounter > 0) {
                if (scoreArray[11] == -1) {
                    scoreArray[11] = CalculateResults().sumFiveOfAKind(diceValues)
                    textViewScoreFiveOfKind.text = scoreArray[11].toString()
                    optionChosen()
                }
                buttonFiveOfKind.setImageResource(R.drawable.button_pressed)
            }
        }

        buttonChance.setOnClickListener() {
            if(diceRollCounter > 0) {
                if (scoreArray[12] == -1) {
                    scoreArray[12] = CalculateResults().sumTotal(diceValues)
                    textViewScoreChance.text = scoreArray[12].toString()
                    optionChosen()
                }
                buttonChance.setImageResource(R.drawable.button_pressed)
            }
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
        updateScore()
        roundCounter++
        diceRollCounter = 0

        if(roundCounter < 13) {
            buttonRollDice.isEnabled = true
            // clear dice faces
            for (i in 0..4) {
                diceChosen[i] = false
            }
        }
    }

    fun resetScore() {
        sensorEnabled = true
        buttonRollDice.isEnabled = true
        diceRollCounter = 0
        roundIndicator.setImageResource(0)
        // clear dice faces
        for(i in 0 .. 4) {
            diceChosen[i] = false
            diceFaces[i].setImageResource(0)
        }
        // reset score
        for(i in 0..12) {
            scoreArray[i] = -1
        }
        upperScore = 0
        bonus = 0
        lowerScore = 0
        total = 0
        resetScoreboard()
    }

    fun rollDice() {
        diceRollCounter++

        if(diceRollCounter >= 3) {
            sensorEnabled = false
            buttonRollDice.isEnabled = false
        }
        else {
            sensorEnabled = false

            Handler(Looper.getMainLooper()).postDelayed({ sensorEnabled = true }, 3000)
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

    fun updateScore() {
        upperScore = 0
        lowerScore = 0
        for(i in 0..5) if(scoreArray[i] > 0) upperScore += scoreArray[i]
        if(upperScore > 62) bonus = 35 else 0
        for(i in 6..12) if(scoreArray[i] > 0) lowerScore += scoreArray[i]
        total = upperScore + bonus + lowerScore

        if(upperScore > 0) textViewScoreUpperTotal.text = upperScore.toString()
        if(bonus > 0) textViewScoreBonus.text = bonus.toString()
        if(lowerScore > 0) textViewScoreLowerTotal.text = lowerScore.toString()
        if(total > 0) textViewScoreTotal.text = total.toString()
    }

    fun resetScoreboard() {
        textViewScoreAces.text = ""
        textViewScoreTwos.text = ""
        textViewScoreThrees.text = ""
        textViewScoreFours.text = ""
        textViewScoreFives.text = ""
        textViewScoreSixes.text = ""
        textViewScoreThreeOfKind.text = ""
        textViewScoreFourOfKind.text = ""
        textViewScoreFullHouse.text = ""
        textViewScoreSmallStraight.text = ""
        textViewScoreLargeStraight.text = ""
        textViewScoreFiveOfKind.text = ""
        textViewScoreChance.text = ""

        textViewScoreUpperTotal.text = ""
        textViewScoreBonus.text = ""
        textViewScoreLowerTotal.text = ""
        textViewScoreTotal.text = ""

        buttonAces.setImageResource(R.drawable.button_normal)
        buttonTwos.setImageResource(R.drawable.button_normal)
        buttonThrees.setImageResource(R.drawable.button_normal)
        buttonFours.setImageResource(R.drawable.button_normal)
        buttonFives.setImageResource(R.drawable.button_normal)
        buttonSixes.setImageResource(R.drawable.button_normal)
        buttonThreeOfKind.setImageResource(R.drawable.button_normal)
        buttonFourOfKind.setImageResource(R.drawable.button_normal)
        buttonFullHouse.setImageResource(R.drawable.button_normal)
        buttonSmallStraight.setImageResource(R.drawable.button_normal)
        buttonLargeStraight.setImageResource(R.drawable.button_normal)
        buttonFiveOfKind.setImageResource(R.drawable.button_normal)
        buttonChance.setImageResource(R.drawable.button_normal)
    }
}