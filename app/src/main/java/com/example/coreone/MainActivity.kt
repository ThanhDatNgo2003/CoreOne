package com.example.coreone

import android.net.wifi.rtt.CivicLocationKeys.STATE
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.ComponentActivity

class MainActivity : ComponentActivity() {

    private val STATE = "State"

    private lateinit var roll: Button
    private lateinit var result: TextView
    private lateinit var add: Button
    private lateinit var subtract: Button
    private lateinit var dice: ImageView
    private lateinit var reset: Button

    private var diceValue: Int = 1
    private var state: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main);

        roll = findViewById(R.id.roll)
        add = findViewById(R.id.add)
        subtract = findViewById(R.id.subtract)
        dice = findViewById(R.id.dice)
        result = findViewById(R.id.result)
        reset = findViewById(R.id.reset)

        roll.isEnabled = true
        add.isEnabled = false
        subtract.isEnabled = false

        roll.setOnClickListener {
            // Generate a random dice value and update diceValue
            state = 1
            diceValue = (1..6).random()
            rollDice(dice, diceValue)
            updateButtonStates(result.text.toString().toInt(), state, roll, add, subtract, reset, diceValue)
        }

        add.setOnClickListener {
            // Get the current value from the result TextView
            val currentValue = result.text.toString().toInt()

            // Calculate the new value by adding the current value and the dice value
            val newValue = add(currentValue, diceValue)
            state = 0
            // Set the new value to the result TextView
            updateScoreText(newValue, result)
            updateButtonStates(newValue, state, roll, add, subtract, reset, diceValue)
        }
        subtract.setOnClickListener {
            // Get the current result value from the TextView
            val currentValue = result.text.toString().toInt()

            // Calculate the new result value by subtracting the dice value from the current result
            val newValue = subtract(currentValue, diceValue)
            state = 0
            // Update the result TextView with the new value
            updateScoreText(newValue, result)
            updateButtonStates(newValue, state, roll, add, subtract, reset, diceValue)
        }
        reset.setOnClickListener {
            dice.setImageResource(R.drawable.dice_1)
            result.text = "0"
            result.setTextColor(result.context.resources.getColor(android.R.color.black))
            updateButtonStates(result.text.toString().toInt(), state, roll, add, subtract, reset, diceValue)
        }
    }

    override fun onResume() {
        super.onResume()
        Log.e(STATE.toString(), "onResume")
    }

    override fun onStop() {
        super.onStop()
        Log.e(STATE.toString(), "onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.e(STATE.toString(), "onDestroy")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt("dice_value", diceValue)
        outState.putInt("state", state)
        outState.putString("my_score", result.text.toString())
        super.onSaveInstanceState(outState)
        Log.e(STATE.toString(), "onSaveInstanceState")
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        Log.e(STATE.toString(), "onRestoreInstanceState")
        result.text = savedInstanceState.getString("my_score")
        diceValue = savedInstanceState.getInt("dice_value")
        state = savedInstanceState.getInt("state")
        rollDice(dice, diceValue)
        updateButtonStates(result.text.toString().toInt(), state, roll, add, subtract, reset, diceValue)
    }

}

private fun rollDice(dice: ImageView, diceValue: Int) {
    when (diceValue) {
        1 -> dice.setImageResource(R.drawable.dice_1)
        2 -> dice.setImageResource(R.drawable.dice_2)
        3 -> dice.setImageResource(R.drawable.dice_3)
        4 -> dice.setImageResource(R.drawable.dice_4)
        5 -> dice.setImageResource(R.drawable.dice_5)
        6 -> dice.setImageResource(R.drawable.dice_6)
    }
}

private fun updateScoreText(score: Int, result: TextView) {
    result.text = score.toString()
    val color = when {
        score < 20 -> android.R.color.black
        score > 20 -> android.R.color.holo_red_dark
        else -> android.R.color.holo_green_light
    }
    result.setTextColor(result.context.resources.getColor(color))
}

private fun updateButtonStates(score: Int, state: Int, roll: Button, add: Button, subtract: Button, reset: Button, diceValue: Int) {
    if (state == 1) {
        roll.isEnabled = false
        add.isEnabled = true
        subtract.isEnabled = true
    } else {
        roll.isEnabled = true
        add.isEnabled = false
        subtract.isEnabled = false
    }
}

private fun add(result: Int, diceValue: Int) = result + diceValue
private fun subtract(result: Int, diceValue: Int): Int {
    val newResult = result - diceValue
    return maxOf(0, newResult)
}



