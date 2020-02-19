package com.example.testkotlin

import android.app.Activity
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.TableRow
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Exception


class MainActivity : AppCompatActivity() {

    private var database = FirebaseDatabase.getInstance()
    private var myRef = database.reference
    private var mFirebaseAnalytics: FirebaseAnalytics? = null
    var myEmail: String ?= null

    var gameSize: Int? = 3
    var activePlayer = 1
    private val winCondition = 3
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
                    newCellButton.isEnabled = false
                    checkWin(i, j)
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
//        Toast.makeText(applicationContext, "Size: $gameSize", Toast.LENGTH_LONG).show()
    }

    private fun checkWin(x: Int, y: Int) {
        var winCount = 1
        var player = map[x][y]

        // Check col ------------------------------
        winCount = 1
        // Check col go down
        for(i in 1 .. winCondition) {
            if (x + i >= gameSize!!) break
            if (map[x + i][y] == player) winCount ++
            else break
        }
        // Check col go up
        for(i in 1 .. winCondition) {
            if (x - i < 0) break
            if (map[x - i][y] == player) winCount ++

            else break
        }
        // ----------------------------------------
        if (winCount >= winCondition) {
            Toast.makeText(this, "Player $player win", Toast.LENGTH_LONG).show()
            return
        }
        // Check row ------------------------------
        winCount = 1
        // Check row right
        for(i in 1 .. winCondition) {
            if (y + i >= gameSize!!) break
            if (map[x][y + i] == player) winCount++
            else break
        }
        // Check row left
        for(i in 1 .. winCondition) {
            if (y - i < 0) break
            if (map[x][y - i] == player) winCount++
            else break
        }
        // ----------------------------------------
        if (winCount >= winCondition) {
            Toast.makeText(this, "Player $player win", Toast.LENGTH_LONG).show()
            return
        }
        // Check dig left to right ----------------
        winCount = 1
        // go down
        for(i in 1 .. winCondition) {
            if (y + i >= gameSize!!) break
            if (x + i >= gameSize!!) break
            if (map[x + i][y + i] == player) winCount++
            else break
        }
        // go up
        for(i in 1 .. winCondition) {
            if (y - i < 0) break
            if (x - i < 0) break
            if (map[x - i][y - i] == player) winCount++
            else break
        }
        if (winCount >= winCondition) {
            Toast.makeText(this, "Player $player win", Toast.LENGTH_LONG).show()
            return
        }
        // ----------------------------------------

        // Check dig right to left ----------------
        winCount = 1
        // go down
        for(i in 1 .. winCondition) {
            if (y - i < 0) break
            if (x + i >= gameSize!!) break
            if (map[x + i][y - i] == player) winCount++
            else break
        }
        // go up
        for(i in 1 .. winCondition) {
            if (y + i >= gameSize!!) break
            if (x - i < 0) break
            if (map[x - i][y +  i] == player) winCount++
            else break
        }
        if (winCount >= winCondition) {
            Toast.makeText(this, "Player $player win", Toast.LENGTH_LONG).show()
            return
        }
        // ----------------------------------------
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        startBtn.setOnClickListener {
            createGame()
        }
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this)
        var bundle: Bundle? = intent.extras
        myEmail = bundle!!.getString("email")
        incommingCall()

    }

    fun requestEvent(view: View){
        var userEmail = editTextEmail.text.toString()
        Toast.makeText(this, "Im in", Toast.LENGTH_LONG).show()
        myRef.child("Users").child(splitString(userEmail)).child("Request").push().setValue(myEmail)
    }

    fun acceptEvent(view: View){
        var userEmail = editTextEmail.text.toString()
        Toast.makeText(this, "Im in", Toast.LENGTH_LONG).show()
        myRef.child("Users").child(splitString(userEmail)).child("Request").push().setValue(myEmail)

    }

    fun incommingCall(){
        myRef.child("Users").child(splitString(myEmail.toString())).child("Request")
            .addValueEventListener(object: ValueEventListener{
                override fun onCancelled(p0: DatabaseError) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onDataChange(p0: DataSnapshot) {
                    try {
                        val td = p0.value as HashMap<String, Any>
                        if (td != null) {
                            var value: String
                            for (key in td.keys) {
                                value = td[key] as String
                                editTextEmail.setText(value)

                                myRef.child("Users").child(myEmail!!).child("Request").setValue(true)
                                break
                            }
                        }
                    }   catch (ex:Exception) {
                        ex.printStackTrace()
                    }
                }
            })
    }

    fun splitString(str: String): String {
        var split = str.split("@")
        return split[0]
    }
    

}
