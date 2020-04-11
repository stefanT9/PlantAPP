package com.example.plantapp

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import androidx.appcompat.widget.Toolbar
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.android.synthetic.main.activity_login.*
import java.lang.reflect.Field


class loginActivity : AppCompatActivity() {

    lateinit var emailID: EditText
    lateinit var password: EditText
    lateinit var buttonSignIn: TextView
    lateinit var googleClientSignIN: GoogleSignInClient
    lateinit var googleBtn: Button
    lateinit var auth:FirebaseAuth
     var TAG="thisLOGIN"
    //google stuff

    val RC_SIGN_IN:Int = 1


    // ------------




    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


        lateinit var authent: FirebaseAuth
        lateinit var listenerState: FirebaseAuth.AuthStateListener

        //setColor to SIGN IN
        val mText="Don't have  an account? Sign up"
        val mSpannableString = SpannableString(mText)
        val mGreen = ForegroundColorSpan(Color.parseColor("#1B5E20"))
        mSpannableString.setSpan(mGreen, 23, 31, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        textView.text = mSpannableString

        //set toolbar
        val toolbar = findViewById(R.id.toolbar) as Toolbar?
        setSupportActionBar(toolbar)
        toolbar?.title = "Log in"
        toolbar?.navigationIcon = ContextCompat.getDrawable(this,R.drawable.ic_menu_black_24dp)
        toolbar?.setNavigationOnClickListener {
            Toast.makeText(applicationContext,"Navigation icon was clicked",Toast.LENGTH_SHORT).show()
        }

        authent = FirebaseAuth.getInstance()
        emailID = findViewById(R.id.editText3)
        password = findViewById(R.id.editText6)
        buttonSignIn = findViewById(R.id.button)
        googleBtn = findViewById(R.id.sign_in_button)
        auth = FirebaseAuth.getInstance()


        listenerState = FirebaseAuth.AuthStateListener {

            fun onAuthStateChanged(fireAuth: FirebaseAuth) {

                var user: FirebaseUser? = authent.currentUser
                if (user != null) {
                    Toast.makeText(this@loginActivity, "You are logged in!", Toast.LENGTH_SHORT)
                        .show()

                } else {
                    Toast.makeText(this@loginActivity, "Please login!", Toast.LENGTH_SHORT).show()

                }

            }

        }

        buttonSignIn.setOnClickListener(View.OnClickListener { it: View? ->

            // Toast.makeText(this@loginActivity, "Just clicked", Toast.LENGTH_SHORT).show()


            var email: String = emailID.text.toString()
            var pwd: String = password.text.toString()

            if (email.isEmpty()) {
                emailID.error = "Please enter email id"
                emailID.requestFocus()
            } else if (pwd.isEmpty()) {

                password.error = "Please enter your password"
                password.requestFocus()

            } else if (email.isEmpty() && pwd.isEmpty()) {
                Toast.makeText(this@loginActivity, "Fields are empty", Toast.LENGTH_SHORT).show()

            } else if (!(email.isEmpty() && pwd.isEmpty())) {

                authent.signInWithEmailAndPassword(email, pwd)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            Log.d("logare", "logat cu success!")
                            Toast.makeText(
                                this@loginActivity,
                                "Logged in succesfully",
                                Toast.LENGTH_SHORT
                            ).show()
                            val user = authent.currentUser
                            //updateUI(user);
                        } else {
                            Toast.makeText(
                                this@loginActivity,
                                "Authentication failed!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

            }


        })


        //---GOOGLE SIGN IN ----

        // Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleClientSignIN = GoogleSignIn.getClient(this, gso)

        googleBtn.setOnClickListener(View.OnClickListener {

                signIn()
              //  FirebaseAuth.getInstance().signOut()
        })
    }


    private fun signIn() {
        var signInIntent:Intent  = googleClientSignIN.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    @SuppressLint("WrongViewCast")
    public override  fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                Log.d(TAG, "signInWithCredential:success")
                val account = task.getResult(ApiException::class.java)
                Toast.makeText(
                    this@loginActivity,
                    "Signed in with Google Successfully",
                    Toast.LENGTH_SHORT
                ).show()
                firebaseAuthWithGoogle(account!!)
            } catch (e: ApiException) {
                Log.w(TAG, "signInWithCredential:failure", task.exception)
                Toast.makeText(this@loginActivity, "Auth3tication failed", Toast.LENGTH_SHORT)
                    .show()
                //  firebaseAuthWithGoogle(null)
            }
        }
    }


    fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {

        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.id!!)
        val credential: AuthCredential  = GoogleAuthProvider.getCredential(acct.idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")
                    Toast.makeText(
                        this@loginActivity,
                        "Signed in with Google Successfully",
                        Toast.LENGTH_SHORT
                    ).show()
                    val user = auth.currentUser
                    updateUI(user!!)

                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    Toast.makeText(
                        this@loginActivity,
                        "Authentication failed",
                        Toast.LENGTH_SHORT
                    ).show()
                    //     updateUI(null)
                }

                // ...
            }
    }

        fun updateUI(fUser: FirebaseUser) {

            val account = GoogleSignIn.getLastSignedInAccount(applicationContext)

            if (account != null) {
                var personName = account.displayName
                var personGivenName = account.givenName
                var personFamily = account.familyName
                var personEmail = account.email
                var personId = account.id
                var personPhoto = account.photoUrl

                Toast.makeText(this@loginActivity, personName + personEmail, Toast.LENGTH_SHORT)
                    .show()
            }

        }

}


