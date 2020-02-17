package com.example.testkotlin

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity : AppCompatActivity() {
    private var mAuth: FirebaseAuth? = null
    private var database = FirebaseDatabase.getInstance()
    private var myRef = database.reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        mAuth = FirebaseAuth.getInstance()
        myRef.child("Users").child("TESTID").setValue("TEST EMail")

    }

    override fun onStart() {
        super.onStart()
        loadMain()
    }

    fun loginEvent(view: View) {
        loginToFireBase(editTextEmail.text.toString(), editTextPassword.text.toString())
    }

    private fun loginToFireBase(email: String, password: String) {
        mAuth!!.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) {task ->
                if (task.isSuccessful) {
                    Toast.makeText(applicationContext, "Login done", Toast.LENGTH_LONG).show()
                    loadMain()
                }
                else {

                    Toast.makeText(applicationContext, task.exception.toString(), Toast.LENGTH_LONG).show()
                }
            }
    }

    fun loadMain() {
        var currentUser = mAuth!!.currentUser
        if (currentUser != null) {
            // Save in db
            var intent = Intent(this, MainActivity::class.java)
            intent.putExtra("email", currentUser.email)
            intent.putExtra("uid", currentUser.uid)

            startActivity(intent)
        }
    }

}
