package com.example.plantapp

import android.content.Intent
import android.nfc.Tag
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser


class loginActivity : AppCompatActivity() {

    lateinit var emailID: EditText
    lateinit var password: EditText
    lateinit var buttonSignIn: TextView

    //google stuff

    lateinit var googleBtn : SignInButton
    lateinit var googleSignIN: GoogleSignInClient


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        lateinit var authent: FirebaseAuth
        lateinit var listenerState: FirebaseAuth.AuthStateListener


        authent = FirebaseAuth.getInstance()
        emailID = findViewById(R.id.editText3)
        password = findViewById(R.id.editText6)
        buttonSignIn = findViewById(R.id.button)


        var RC_SIGN_IN =  1;

        listenerState = FirebaseAuth.AuthStateListener() {

         fun onAuthStateChanged( fireAuth: FirebaseAuth )  {

                var user: FirebaseUser? = authent.getCurrentUser()
                if(user!=null){
                    Toast.makeText(this@loginActivity, "You are logged in!", Toast.LENGTH_SHORT).show();

                }
                else{
                    Toast.makeText(this@loginActivity, "Please login!", Toast.LENGTH_SHORT).show();

                }

            }

        };

        buttonSignIn.setOnClickListener( View.OnClickListener { it: View? ->

           // Toast.makeText(this@loginActivity, "Just clicked", Toast.LENGTH_SHORT).show()


                    var email : String = emailID.text.toString()
                    var pwd :  String = password.text.toString()

                    if(email.isEmpty()){
                        emailID.setError("Please enter email id");
                        emailID.requestFocus();
                    }
                    else if (pwd.isEmpty()){

                        password.setError("Please enter your password");
                        password.requestFocus();

                    }

                    else if(email.isEmpty() && pwd.isEmpty()){
                        Toast.makeText(this@loginActivity, "Fields are empty", Toast.LENGTH_SHORT).show()

                    }
                    else if(!(email.isEmpty() && pwd.isEmpty())){

                        authent.signInWithEmailAndPassword(email, pwd)
                            .addOnCompleteListener(this){
                                task->
                                    if(task.isSuccessful){
                                        Log.d("logare", "logat cu success!")
                                        Toast.makeText(this@loginActivity, "Login succesfully ...", Toast.LENGTH_SHORT).show()
                                        val user = authent.currentUser;
                                        //updateUI(user);
                                    }
                                else{
                                        Toast.makeText(this@loginActivity, "Authentication failed!", Toast.LENGTH_SHORT).show()
                                    }
                            }

                    }



        })



        // google sign in

//        googleBtn = findViewById(R.id.sign_in_button)
//        authent = FirebaseAuth.getInstance()
//
//        var gso  : GoogleSignInOptions;
//        gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//            .requestIdToken(getString(R.string.default_web_client_id))
//            .requestEmail()
//            .build()
//
//        googleSignIN = GoogleSignIn.getClient(this, gso)
//
//
//        googleBtn.setOnClickListener(View.OnClickListener {
//
//                signIn()
//
//        });

    }




}
