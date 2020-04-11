package com.example.plantapp

import android.content.Intent
import android.nfc.Tag
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_register.*


class RegisterActivity : AppCompatActivity() {

    lateinit var mAuth: FirebaseAuth
    lateinit var listenerState: FirebaseAuth.AuthStateListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        mAuth = FirebaseAuth.getInstance()




        listenerState = FirebaseAuth.AuthStateListener() {

            fun onAuthStateChanged( fireAuth: FirebaseAuth )  {

                var user: FirebaseUser? = mAuth.getCurrentUser()
                if(user!=null){
                    Toast.makeText(this@RegisterActivity, "You are auth!", Toast.LENGTH_SHORT).show();

                }
                else{
                    Toast.makeText(this@RegisterActivity, "Please auth!", Toast.LENGTH_SHORT).show();

                }

            }

        };
        btn_Register.setOnClickListener( View.OnClickListener { it: View? ->


            var email : String = regEmail.text.toString()
            var pass :  String = regPassword.text.toString()


            if(email.isEmpty()){
                regEmail.setError("Please enter email id");
                regEmail.requestFocus();
            }
            else if (pass.isEmpty()){

                regPassword.setError("Please enter your password");
                regPassword.requestFocus();

            }
            else {
                mAuth.createUserWithEmailAndPassword(email, pass)

                    .addOnCompleteListener(this) { task ->

                        if (task.isSuccessful) {

                            // Sign in success, update UI with the signed-in user's information

                            Log.d("create", "createUserWithEmail:success")
                            Toast.makeText(this@RegisterActivity, "You are auth!", Toast.LENGTH_SHORT).show();
                            val user  = mAuth.currentUser

                            // updateUI(user)

                        } else {

                            // If sign in fails, display a message to the user.

                            Log.w("failed", "createUserWithEmail:failure", task.exception)

                            Toast.makeText(this@RegisterActivity, "Authentication failed.",

                                Toast.LENGTH_SHORT).show()

                            // updateUI(null)

                        }




                    }



            }
        })


    }}







