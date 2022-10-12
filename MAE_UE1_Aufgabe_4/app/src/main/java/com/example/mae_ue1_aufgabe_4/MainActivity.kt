package com.example.mae_ue1_aufgabe_4

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val textViewFormula = findViewById<View>(R.id.textViewFormula) as TextView
        val textViewResult = findViewById<View>(R.id.textViewResult) as TextView
        val button0 = findViewById<View>(R.id.button0) as Button
        val button1 = findViewById<View>(R.id.button1) as Button
        val button2 = findViewById<View>(R.id.button2) as Button
        val button3 = findViewById<View>(R.id.button3) as Button
        val button4 = findViewById<View>(R.id.button4) as Button
        val button5 = findViewById<View>(R.id.button5) as Button
        val button6 = findViewById<View>(R.id.button6) as Button
        val button7 = findViewById<View>(R.id.button7) as Button
        val button8 = findViewById<View>(R.id.button8) as Button
        val button9 = findViewById<View>(R.id.button9) as Button
        val buttonDecimal = findViewById<View>(R.id.buttonDecimal) as Button
        val buttonPlus = findViewById<View>(R.id.buttonPlus) as Button
        val buttonMinus = findViewById<View>(R.id.buttonMinus) as Button
        val buttonMulti = findViewById<View>(R.id.buttonMulti) as Button
        val buttonDivision = findViewById<View>(R.id.buttonDivision) as Button
        val buttonPleft = findViewById<View>(R.id.buttonPleft) as Button
        val buttonPright = findViewById<View>(R.id.buttonPright) as Button
        val buttonCalc = findViewById<View>(R.id.buttonCalc) as Button
        val buttonDel= findViewById<View>(R.id.buttonDel) as Button
        val buttonClr = findViewById<View>(R.id.buttonClr) as Button

        // variables for calculation
        var lastCharOperator: Boolean = false   // true if last char was operator, false if number
        var calculated: Boolean = false         // true if last activity was a calculation
        val numbers: ArrayList<Double> = ArrayList() // stores numbers entered for calculations
        val operators: ArrayList<Char> = ArrayList() // stores operators entered for calculations
        var numString: String = ""              // stores current number until entering operator
        var error: Boolean = false              // true if math error occured

        // clear
        fun clr() {
            textViewFormula.setText("")
            textViewResult.setText("")
            numString = ""
            lastCharOperator = false
            numbers.clear()
            operators.clear()
            error = false
        }

        // using to enter a number
        fun enterNum(c: Char) {
            textViewFormula.append(c.toString())
            numString += c.toString()
            lastCharOperator = false
        }

        // pushing a number to the numbers list
        fun pushNum() {
            if(calculated) {
                clr()
            }
            numbers.add(numString.toDouble())
            numString = ""
        }

        // pop a number in case of del after operator got removed
        fun popNum() {
            numString = numbers[numbers.size-1].toString()
            numbers.dropLast(1)
        }

        // push operator
        fun pushOp(c: Char) {
            if(calculated) {
                // push last result for next calculations and clear cache
                numbers.clear()
                operators.clear()
                textViewFormula.setText("")
                numString = textViewResult.text.toString()
                textViewFormula.setText(textViewResult.text)
                textViewResult.setText("")
                calculated = false
            }
            if(!lastCharOperator && numString != "") {
                pushNum()
                operators.add(c)
                lastCharOperator = true
                textViewFormula.append(" $c ")
            }
        }

        // pop operator
        fun popOp() {
            lastCharOperator = false
            textViewFormula.setText(textViewFormula.text.dropLast(3))
            popNum()
        }

        // calc numbers with operator
        fun calcNum(a: Double, b: Double, c: Char): Double {
            when (c) {
                '+' -> return a + b
                '-' -> return a - b
                'x' -> return a * b
                '/' -> if(b != 0.0) {
                        return a / b
                    }
                    else {
                        textViewResult.setText("error: div 0")
                        error = true
                    }
            }
            // if no operator specified (fallback)
            return 0.0
        }

        // calculate result
        fun calculate(): Double {
            // push last number
            if(numString != "") {
                pushNum()
            }

            if(numbers.size > 1) {
                var n = 0.0
                for (i in 1 until numbers.size) {
                    if(operators[i-1] == 'x' || operators[i-1] == '/') {
                        if(n == 0.0) n = numbers[i-1]
                        n = calcNum(n, numbers[i], operators[i - 1])
                        operators.drop(i-1)
                        numbers.drop(i)
                        numbers[i-1] = n
                    }
                }
                for (i in 1 until numbers.size) {
                    if(operators[i-1] == '+' || operators[i-1] == '-') {
                        if(n == 0.0) n = numbers[i-1]
                        n = calcNum(n, numbers[i], operators[i - 1])
                    }
                }
                return n
            }
            else {
                return numbers[0]
            }
        }

        button0.setOnClickListener {
            enterNum('0')
        }

        button1.setOnClickListener {
            enterNum('1')
        }

        button2.setOnClickListener {
            enterNum('2')
        }

        button3.setOnClickListener {
            enterNum('3')
        }

        button4.setOnClickListener {
            enterNum('4')
        }

        button5.setOnClickListener {
            enterNum('5')
        }

        button6.setOnClickListener {
            enterNum('6')
        }

        button7.setOnClickListener {
            enterNum('7')
        }

        button8.setOnClickListener {
            enterNum('8')
        }

        button9.setOnClickListener {
            enterNum('9')
        }

        buttonDecimal.setOnClickListener {
            enterNum('.')
        }

        buttonPlus.setOnClickListener {
            pushOp('+')
        }

        buttonMinus.setOnClickListener {
            pushOp('-')
        }

        buttonMulti.setOnClickListener {
            pushOp('x')
        }

        buttonDivision.setOnClickListener {
            pushOp('/')
        }

        buttonCalc.setOnClickListener {
            // ignore if last char was operator
            if(!lastCharOperator && !textViewFormula.text.trim().isEmpty()) {
                // calculate result
                var res = calculate().toString()
                if(!error) {
                    var s = res.split('.')
                    if(s.size > 1 && s[1] != "0") {
                        textViewResult.setText(res)
                    }
                    else {
                        textViewResult.setText(s[0])
                    }
                    calculated = true
                }
            }
            else {
                textViewResult.setText("")
            }
        }

        buttonDel.setOnClickListener {
            if(calculated || error) {
                clr()
            }
            if (lastCharOperator) {
                // delete last operator
                popOp()
            }
            else {
                // delete last digit
                numString.dropLast(1)
                textViewFormula.setText(textViewFormula.text.trim().dropLast(1))
            }
        }

        buttonClr.setOnClickListener {
            clr()
        }
    }
}