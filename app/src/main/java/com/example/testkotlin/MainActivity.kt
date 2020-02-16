package com.example.testkotlin

import android.app.Activity
import android.os.Bundle
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.TableRow
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    var gameSize: Int? = 3
    var activePlayer = 1
//    player 1: X, player 2: O
    private var map = Array(gameSize!!) { i ->
        Array(gameSize!!) { j ->
            "the String at position $i, $j" // provide some initial value based on i and j
        }
    }

    private fun hideKeyboard(){
        val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if(currentFocus != null) {
            hideKeyboard()
        }
        return super.dispatchTouchEvent(ev)
    }

    private fun createPlayground() {
        gameScreen.removeAllViews()
        for(i in 0 until gameSize!!) {
            val newRow = TableRow(this)
            for(j in 0 until gameSize!!) {

                var newCellButton = Button(applicationContext)
                newCellButton.setOnClickListener {
                    if (activePlayer == 1) {
                        map[i][j] = "X"
                        newCellButton.text = "X"
                        activePlayer = 2
                    }else if (activePlayer == 2) {
                        map[i][j] = "O"
                        newCellButton.text = "O"
                        activePlayer = 1
                    }
                    newCellButton?.isEnabled = false
                }
                newRow.addView(newCellButton)
            }
            gameScreen.addView(newRow)
        }
    }

    private fun createGame(){
        if (gameSizeInput.text.toString().trim() != "") {
            var size = gameSizeInput.text.toString().toInt()
            if (size >= 3) gameSize = size
        }

//        original
//        map = Array(gameSize!!) { i ->
//            Array(gameSize!!) { j ->
//                "the String at position $i, $j" // provide some initial value based on i and j
//            }
//        }

        map = Array(gameSize!!) {
            Array(gameSize!!) {
                "0"
            }
        }
        createPlayground()

        Toast.makeText(applicationContext, "Size: $gameSize", Toast.LENGTH_LONG).show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        startBtn.setOnClickListener {
            createGame()
        }
    }



}
