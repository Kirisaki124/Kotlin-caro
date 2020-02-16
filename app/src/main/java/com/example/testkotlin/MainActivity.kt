package com.example.testkotlin

import android.app.Activity
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.TableRow
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    var gameSize: Int? = 3
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

    private fun createGame(){
        if (!gameSizeInput.text.toString().trim().equals("")) {
            var size = gameSizeInput.text.toString().toInt()
            if (size >= 3) gameSize = size
        }
//        original
//        map = Array(gameSize!!) { i ->
//            Array(gameSize!!) { j ->
//                "the String at position $i, $j" // provide some initial value based on i and j
//            }
//        }
        map = Array(gameSize!!) { i ->
            Array(gameSize!!) { j ->
                "0"
            }
        }

        val newRow = TableRow(this)
        for(i in 0 until gameSize!!) {
            var newCell = TextView(this)
            newCell.text = i.toString()
            newRow.addView(newCell)
        }

        gameScreen!!.addView(newRow)
        Toast.makeText(applicationContext, "Size: $gameSize", Toast.LENGTH_LONG).show()
//        gameScreen.addView()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        startBtn.setOnClickListener {
            createGame()
        }
    }



}
