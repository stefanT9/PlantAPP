package com.example.plantapp

import android.content.Intent
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_top_nav.*

class LoginActivity : TopNavViewActivity() {

    lateinit var emailID: EditText
    lateinit var password: EditText
    lateinit var buttonSignIn: TextView
    lateinit var googleClientSignIN: GoogleSignInClient
    lateinit var googleBtn: Button
    lateinit var auth:FirebaseAuth
    var TAG="thisLOGIN"
    //LOGIN_ACTIVITY

    val RC_SIGN_IN:Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        this.layoutInflater.inflate(R.layout.activity_login,mainLayout)


        lateinit var listenerState: FirebaseAuth.AuthStateListener

        //setColor to SIGN IN
        val mSpannableString = SpannableString(getString(R.string.sign_up_text))
        /// What does the next line do?
        // mSpannableString.setSpan("@color/sign_in_green_color", 23, 30, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        sign_up_textView.text = mSpannableString


        val authent: FirebaseAuth = FirebaseAuth.getInstance()
        emailID = email_textView
        password = password_editText
        buttonSignIn = email_login_button
        googleBtn = sign_in_button
        auth = FirebaseAuth.getInstance()

        sign_up_textView.setOnClickListener{
            val intent = Intent(this,RegisterActivity::class.java)
            startActivity(intent)
        }

        listenerState = FirebaseAuth.AuthStateListener {

            fun onAuthStateChanged(fireAuth: FirebaseAuth) {

                val user: FirebaseUser? = authent.currentUser
                if (user != null) {
                    Toast.makeText(this@LoginActivity, "You are logged in!", Toast.LENGTH_SHORT)
                        .show()

                } else {
                    Toast.makeText(this@LoginActivity, "Please login!", Toast.LENGTH_SHORT).show()
                }

            }

        }

        buttonSignIn.setOnClickListener(View.OnClickListener {

            // Toast.makeText(this@loginActivity, "Just clicked", Toast.LENGTH_SHORT).show()

             // pentru test, nu incurca
            val email: String = emailID.text.toString()
            val pwd: String = password.text.toString()

            if (email.isEmpty()) {
                emailID.error = "Please enter email id"
                emailID.requestFocus()
            } else if (pwd.isEmpty()) {

                password.error = "Please enter your password"
                password.requestFocus()

            } else if (email.isEmpty() && pwd.isEmpty()) {
                Toast.makeText(this@LoginActivity, "Fields are empty", Toast.LENGTH_SHORT).show()

            } else if (!(email.isEmpty() && pwd.isEmpty())) {
                message.text= getString(R.string.logged_in_successfully)
                message2.text= getString(R.string.auth_failed)
                authent.signInWithEmailAndPassword(email, pwd)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            Log.d("logare", "logat cu success!")
                            /// TODO: Replace toasts with activity transfers to home activity(Alex Barsan)
                            Toast.makeText(
                                this@LoginActivity,
                                "Logged in successfully",
                                Toast.LENGTH_SHORT
                            ).show()
                            val user = authent.currentUser
                            //updateUI(user);
                        } else {
                            Toast.makeText(
                                this@LoginActivity,
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
                    this@LoginActivity,
                    "Signed in with Google Successfully",
                    Toast.LENGTH_SHORT
                ).show()
                firebaseAuthWithGoogle(account!!)
            } catch (e: ApiException) {
                Log.w(TAG, "signInWithCredential:failure", task.exception)
                Toast.makeText(this@LoginActivity, "Authentication failed", Toast.LENGTH_SHORT)
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
                        this@LoginActivity,
                        "Signed in with Google Successfully",
                        Toast.LENGTH_SHORT
                    ).show()
                    val user = auth.currentUser
                    updateUI(user!!)

                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    Toast.makeText(
                        this@LoginActivity,
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
                val personName = account.displayName
                val personEmail = account.email

                Toast.makeText(this@LoginActivity, personName + personEmail, Toast.LENGTH_SHORT)
                    .show()
            }

        }

}


