package com.example.plantapp

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



class RegisterActivity : AppCompatActivity() {


    lateinit var mRegEmail: EditText
    lateinit var mRegPass: EditText

    lateinit var mRegisterbtn: Button
    lateinit var mRegLoginBtn: TextView
    lateinit var mAuth: FirebaseAuth
    lateinit var listenerState: FirebaseAuth.AuthStateListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mAuth = FirebaseAuth.getInstance()

        mRegEmail=findViewById(R.id.regEmail)
       mRegLoginBtn=findViewById(R.id.regLogin)
        mRegEmail=findViewById(R.id.regEmail)
        mRegPass=findViewById(R.id.regPassword)

        mRegisterbtn=findViewById(R.id.buttonRegister)



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
        mRegisterbtn.setOnClickListener( View.OnClickListener { it: View? ->


            var email : String = mRegEmail.text.toString()
            var pwd :  String = mRegPass.text.toString()


            if(email.isEmpty()){
                mRegEmail.setError("Please enter email id");
                mRegEmail.requestFocus();
            }
            else if (pwd.isEmpty()){

                mRegPass.setError("Please enter your password");
                mRegPass.requestFocus();

            }

            else if(email.isEmpty() && pwd.isEmpty() ){
                Toast.makeText(this@RegisterActivity, "Fields are empty", Toast.LENGTH_SHORT).show()
    }
            else if(!(email.isEmpty() && pwd.isEmpty()) ){
                mAuth.createUserWithEmailAndPassword(email, pwd)

                    .addOnCompleteListener(this) { task ->

                        if (task.isSuccessful) {

                            // Sign in success, update UI with the signed-in user's information

                            Log.d("create", "createUserWithEmail:success")

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







