package com.example.plantapp

import android.content.Intent
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.*
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_top_nav.*

class LoginActivity : TopNavViewActivity() {

    lateinit var emailID: EditText
    lateinit var password: EditText
    lateinit var buttonSignIn: TextView
    lateinit var googleBtn: Button
    lateinit var auth: FirebaseAuth

    //Google Login Request Code
    private val RC_SIGN_IN = 7

    //Google Sign In Client
    private lateinit var mGoogleSignInClient: GoogleSignInClient

    //Firebase Auth
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        this.layoutInflater.inflate(R.layout.activity_login, mainLayout)

        //setColor to SIGN IN
        val stringText: String = getString(R.string.don_t_have_an_account_sign_up)
        val mSpannableString = SpannableString(stringText)
        mSpannableString.setSpan(
            "@color/sign_in_green_color",
            23,
            30,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        Sign_up_textview.text = mSpannableString


        mAuth = FirebaseAuth.getInstance()
        emailID = EmailId
        password = PasswordId
        buttonSignIn = btn_login
        auth = FirebaseAuth.getInstance()

        Sign_up_textview.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        buttonSignIn.setOnClickListener(View.OnClickListener {

            // Toast.makeText(this@loginActivity, "Just clicked", Toast.LENGTH_SHORT).show()
            // pentru test, nu incurca
            val email: String = emailID.text.toString()
            val pwd: String = password.text.toString()

            if (email.isEmpty()) {
                emailID.error = "Please enter email"
                emailID.requestFocus()

            }
            else if ( !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                emailID.error = "Please enter a valid email"
                emailID.requestFocus()
            }
            else if (pwd.isEmpty()) {
                password.error = "Please enter your password"
                password.requestFocus()
            } else if (email.isEmpty() && pwd.isEmpty()) {
                Toast.makeText(this@LoginActivity, "Fields are empty", Toast.LENGTH_SHORT).show()

            } else if (!(email.isEmpty() && pwd.isEmpty())) {
                message.text = getString(R.string.signed_in_successfully)
                message2.text = getString(R.string.auth_failed)
                mAuth.signInWithEmailAndPassword(email, pwd)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            val intent = Intent(this, HomeActivity::class.java)
                            startActivity(intent)
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

        /// TODO: Test/Repair register with google(Stefan Tomsa)
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
        sign_in_button.setOnClickListener(View.OnClickListener {
            signIn()
        })
    }


    private fun signIn() {
        val signInIntent: Intent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w("Login", "Google sign in failed", e)
                // ...
            }

        }

    }

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        Log.d("Login", "firebaseAuthWithGoogle:" + acct.id!!)

        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d("Login", "signInWithCredential:success")
                } else {
                    Log.w("Login", "signInWithCredential:failure", task.exception)
                    Toast.makeText(this,"Auth Failed",Toast.LENGTH_LONG).show()
                }
            }
    }
}


