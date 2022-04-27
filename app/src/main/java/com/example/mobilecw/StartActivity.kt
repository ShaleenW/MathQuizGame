package com.example.mobilecw

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*
import kotlin.random.Random

var questionsCount = 0
var correctAnswersCount = 0
var incorrectAnswersCount = 0

class StartActivity : AppCompatActivity() {

    private var operatorsSet = "+-*/"
    var time  = mutableListOf<Long>(50000)
    var timer = mutableListOf<CountDownTimer>()
//first commit
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)

        val exp1View = findViewById<TextView>(R.id.expression1_textview) //expression on left
        val exp2View = findViewById<TextView>(R.id.expression2_textview) //expression on right

        val ansView = findViewById<TextView>(R.id.answer_textview)

        val timerView = findViewById<TextView>(R.id.timer_textview) //timer

        val greaterBtn = findViewById<Button>(R.id.greater_bt)
        val equalsBtn = findViewById<Button>(R.id.equals_bt)
        val lesserBtn = findViewById<Button>(R.id.lesser_bt)

        if (savedInstanceState == null){
            timer.add(countDownTimer(timerView,time))
        }

        expression(exp1View, exp2View, ansView, greaterBtn, equalsBtn, lesserBtn, timerView)

        //activity title
        val actionBar = supportActionBar
        actionBar!!.title = "Game Screen"
        actionBar.setDisplayHomeAsUpEnabled(true)
    }

    // timer code
    private fun countDownTimer(timerView:TextView, currentTime:MutableList<Long>):CountDownTimer{
        val timer = object : CountDownTimer(currentTime[0], 1000) {
            override fun onTick(milliSecUntilFinished: Long) {
                val f: NumberFormat = DecimalFormat("00")
                val minutes = milliSecUntilFinished / 60000 % 60
                val seconds = milliSecUntilFinished / 1000 % 60
                timerView.text =f.format(minutes)+":"+f.format(seconds)

                currentTime[0] = currentTime[0] - 1000 //since the timer begin from 50 seconds
            }

            override fun onFinish() {
//                val intent = Intent(this@StartActivity,Scorecard::class.java)
//                startActivity(intent)
                //activity title
                val actionBar = supportActionBar
                actionBar!!.title = "Scorecard"
                actionBar.setDisplayHomeAsUpEnabled(true)

                setContentView(R.layout.activity_scorecard)

                val correctScoreView = findViewById<TextView>(R.id.score_textview)
                val incorrectScoreView = findViewById<TextView>(R.id.incorrect_score)

                correctScoreView.append(correctAnswersCount.toString())
                incorrectScoreView.append(incorrectAnswersCount.toString())

            }
        }.start()
        return timer
    }

    private fun evaluateExpression(expression: String): Int? {
        val tokensList = expression.toCharArray()
        val valuesStack: Stack<Int> = Stack<Int>() //numbers stack
        val operatorsStack: Stack<Char> = Stack<Char>() //operators stack
        var i = 0
        while (i < tokensList.size) {
            if (tokensList[i] == ' ') { //whitespaces
                i++
                continue
            }
            if (tokensList[i] in '0'..'9') { //pushed to stacks (numbers)
                val stringBuffer = StringBuffer()
                //There may be more than one digit in a number
                while (i < tokensList.size && tokensList[i] >= '0' && tokensList[i] <= '9') stringBuffer.append(tokensList[i++])
                valuesStack.push(stringBuffer.toString().toInt())
                //since the while loop increases the i, 1 will have to be decremented from i
                i--
            } //the opening brace pushed to the operator
            else if (tokensList[i] == '(')
                operatorsStack.push(tokensList[i])
            //the closing brace pushed to the operator
            else if (tokensList[i] == ')') {
                while (operatorsStack.peek() != '(') valuesStack.push(
                    applyOperators(
                        operatorsStack.pop(),
                        valuesStack.pop(),
                        valuesStack.pop()
                    )
                )
                operatorsStack.pop()
            } //operators
            else if (tokensList[i] == '+' || tokensList[i] == '-' || tokensList[i] == '*' || tokensList[i] == '/') {
                //priority given for the operator with the higher precedence
                while (!operatorsStack.empty() &&
                    hasPrecedence(
                        tokensList[i],
                        operatorsStack.peek()
                    )
                ) valuesStack.push(
                    applyOperators(
                        operatorsStack.pop(),
                        valuesStack.pop(),
                        valuesStack.pop()
                    )
                )
                //the value pushed to the operator
                operatorsStack.push(tokensList[i])
            }
            i++
        }
        //entire expressing has been parsed, the remaining values will receive the remain operators
        while (!operatorsStack.empty()) valuesStack.push(
            applyOperators(
                operatorsStack.pop(),
                valuesStack.pop(),
                valuesStack.pop()
            )
        )
        //generates and returns the result
        return valuesStack.pop()
    }

    //if operator2 is higher returns true or equal or returns false if less
    private fun hasPrecedence(operator1: Char, operator2: Char): Boolean {
        if (operator2 == '(' || operator2 == ')') return false
        return !((operator1 == '*' || operator1 == '/') &&
                (operator2 == '+' || operator2 == '-'))
    }

    //to apply operators to the operands
    private fun applyOperators(op: Char, b: Int, a: Int): Int {
        when (op) {
            '+' -> return a + b
            '-' -> return a - b
            '*' -> return a * b
            '/' -> {
                if (a%b == 0)
                    return a / b
            }
        }
        return 0
    }

    fun operator(): Char {
        return operatorsSet[Random.nextInt(operatorsSet.length)]
    }

    //numbers 1 to 20 have been used in order to create the arithmetic expressions
    fun numbers(): Int {
        return Random.nextInt(1, 21)
    }

    //generating the arithmetic expressions
    private fun expression (exp1View: TextView, exp2View: TextView, ansView: TextView, greaterBtn: Button, equalsBtn: Button, lesserBtn: Button, timerView: TextView) {

        val numOfOperators = Random.nextInt(1, 5)

        var expression1 = ""
        var expression2 = ""

        for (i in 0 until numOfOperators) {
            if (numOfOperators > 1) {

                if (i == 0 && numOfOperators == 3) {
                    expression1 += "(" + "(" + numbers()
                } else if (i == 0 && numOfOperators == 4) {
                    expression1 += "(" + "(" + "(" + numbers()
                } else if (i == 0) {
                    expression1 += "(" + numbers()
                } else if (i == 2 || i == 4) {
                    expression1 += "" + numbers() + ")"
                } else {
                    expression1 += numbers()
                }

                if (i == 1 || i == 3) {
                    expression1 += ")" + operator()
                } else {
                    expression1 += operator()
                }

            } else {
                expression1 += numbers()
                expression1 += operator()
            }
        }

        expression1 += numbers()
        val x = evaluateExpression(expression1)
        if (x!! < 100.0 ){
            exp1View.text = ""
            exp1View.append(expression1)
        } else {
            expression(exp1View, exp2View, ansView, greaterBtn, equalsBtn, lesserBtn, timerView)
        }

        val numOfOperators2 = Random.nextInt(1, 5)

        for (i in 0 until numOfOperators2) {
            if (numOfOperators2 > 1) {
                if (i == 0 && numOfOperators2 == 3) {
                    expression2 += "(" + "(" + numbers()
                } else if (i == 0 && numOfOperators2 == 4) {
                    expression2 += "(" + "(" + "(" + numbers()
                } else if (i == 0) {
                    expression2 += "(" + numbers()
                } else if (i == 2 || i == 4) {
                    expression2 += "" + numbers() + ")"
                } else {
                    expression2 += numbers()
                }

                if (i == 1 || i == 3) {
                    expression2 += ")" + operator()
                } else {
                    expression2 += operator()
                }

            } else {
                expression2 += numbers()
                expression2 += operator()
            }
        }

        expression2 += numbers()
        val y = evaluateExpression(expression2)
        if (y!! < 100.0 ){
            exp2View.text = ""
            exp2View.append(expression2)
        } else {
            expression(exp1View, exp2View, ansView, greaterBtn, equalsBtn, lesserBtn, timerView)
        }

        var expr1 = evaluateExpression(expression1).toString()
        var expr2 = evaluateExpression(expression2).toString()
        lesserBtn.setOnClickListener {

            if (expr1 < expr2) {
                ansView.text = ""
                ansView.setTextColor(Color.GREEN) //if the answer is correct, indicated in green color
                ansView.append("Correct")
                correctAnswersCount ++
                //if 5 answers are correct, 10 seconds will be added to the timer
                if (correctAnswersCount%5 == 0) {
                    time[0] = time[0] + 10000
                    timer[0].cancel()
                    timer.clear()
                    timer.add(countDownTimer(timerView, time))
                }
            } else {
                ansView.text = ""
                ansView.setTextColor(Color.RED) //if the answer is incorrect, indicated in red color
                ansView.append("Wrong")
                incorrectAnswersCount ++
            }
            questionsCount ++
            expression(exp1View, exp2View, ansView, greaterBtn, equalsBtn, lesserBtn, timerView)
        }

        greaterBtn.setOnClickListener {
            if (expr1 > expr2) {
                ansView.text = ""
                ansView.setTextColor(Color.GREEN)
                ansView.append("Correct")
                correctAnswersCount ++
                if (correctAnswersCount%5 == 0) {
                    time[0] = time[0] + 10000
                    timer[0].cancel()
                    timer.clear()
                    timer.add(countDownTimer(timerView, time))
                }
            } else {
                ansView.text = ""
                ansView.setTextColor(Color.RED)
                ansView.append("Wrong")
                incorrectAnswersCount ++
            }
            questionsCount ++
            expression(exp1View, exp2View, ansView, greaterBtn, equalsBtn, lesserBtn, timerView)
        }
        equalsBtn.setOnClickListener {
            if (expr1 == expr2) {
                ansView.text = ""
                ansView.setTextColor(Color.GREEN)
                ansView.append("Correct")
                correctAnswersCount ++
                if (correctAnswersCount%5 == 0) {
                    time[0] = time[0] + 10000
                    timer[0].cancel()
                    timer.clear()
                    timer.add(countDownTimer(timerView, time))
                }
            } else {
                ansView.text = ""
                ansView.setTextColor(Color.RED)
                ansView.append("Wrong")
                incorrectAnswersCount ++
            }
            questionsCount ++
            expression(exp1View, exp2View, ansView, greaterBtn, equalsBtn, lesserBtn, timerView)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)


        val expression1View = findViewById<TextView>(R.id.expression1_textview) //left
        val expression2View = findViewById<TextView>(R.id.expression2_textview) //right
        val ansView = findViewById<TextView>(R.id.answer_textview)

        val restoreData1 : CharSequence = expression1View.text
        val restoreData2 : CharSequence = expression2View.text
        val restoreData3 : CharSequence = ansView.text

        val restoreData4 : Long = time[0]

        outState.putCharSequence ("Data restored1", restoreData1)
        outState.putCharSequence ("Data restored2", restoreData2)
        outState.putCharSequence ("Data restored3", restoreData3)
        outState.putLong("Countdown",restoreData4)
    }


    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        val expression1View = findViewById<TextView>(R.id.expression1_textview) //left
        val expression2View = findViewById<TextView>(R.id.expression2_textview) //right
        val ansView = findViewById<TextView>(R.id.answer_textview)
        val timerView = findViewById<TextView>(R.id.timer_textview)

        val restoreData1 : CharSequence = savedInstanceState.getCharSequence("Data restored1").toString()
        val restoreData2 : CharSequence = savedInstanceState.getCharSequence("Data restored2").toString()
        val restoreData3 : CharSequence = savedInstanceState.getCharSequence("Data restored3").toString()

        time [0] = savedInstanceState.getLong("Countdown")
        timer.add(countDownTimer(timerView,time))

        expression1View.text = restoreData1
        expression2View.text = restoreData2
        ansView.text = restoreData3
    }
}
