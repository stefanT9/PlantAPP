package com.example.plantapp

import android.content.Intent
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.*
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
import kotlinx.android.synthetic.main.activity_login.view.*

class LoginActivity : TopNavViewActivity() {

    lateinit var emailID: EditText
    lateinit var password: EditText
    lateinit var buttonSignIn: TextView
    lateinit var googleClientSignIN: GoogleSignInClient
    lateinit var googleBtn: Button
    lateinit var auth: FirebaseAuth
    var TAG = "thisLOGIN"
    //LOGIN_ACTIVITY

    val RC_SIGN_IN: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        this.layoutInflater.inflate(R.layout.activity_login, mainLayout)

        //setColor to SIGN IN
        val stringText: String = getString(R.string.don_t_have_an_account_sign_up)
        val mSpannableString = SpannableString(stringText)
        mSpannableString.setSpan("@color/sign_in_green_color", 23, 30, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        sign_up_textView.text = mSpannableString


        val authent: FirebaseAuth = FirebaseAuth.getInstance()
        emailID = email_textView
        password = password_editText
        buttonSignIn = email_login_button
        googleBtn = sign_in_button
        auth = FirebaseAuth.getInstance()

        sign_up_textView.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        buttonSignIn.setOnClickListener(View.OnClickListener {

            // Toast.makeText(this@loginActivity, "Just clicked", Toast.LENGTH_SHORT).show()

            // pentru test, nu incurca
            val email: String = emailID.text.toString()
            val pwd: String = password.text.toString()

            if (email.isEmpty()) {
                /// TODO: find what these 2 are and repair them
                // message_edit_text_email.text = getString(R.string.please_enter_email_id)
                emailID.error = "Please enter email id"
                emailID.requestFocus()
            } else if (pwd.isEmpty()) {
                // message_edit_text_password.text = getString(R.string.please_enter_your_password)
                password.error = "Please enter your password"
                password.requestFocus()


            } else if (email.isEmpty() && pwd.isEmpty()) {
                Toast.makeText(this@LoginActivity, "Fields are empty", Toast.LENGTH_SHORT).show()

            } else if (!(email.isEmpty() && pwd.isEmpty())) {
                message.text = getString(R.string.logged_in_successfully)
                message2.text = getString(R.string.auth_failed)
                authent.signInWithEmailAndPassword(email, pwd)
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

        /// TODO: Test/Repair register with google(Daniel Bicu)
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleClientSignIN = GoogleSignIn.getClient(this, gso)

        googleBtn.setOnClickListener(View.OnClickListener {
            signIn()
        })
    }


    private fun signIn() {
        var signInIntent: Intent = googleClientSignIN.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
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
            }
        }
    }


    fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {

        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.id!!)
        val credential: AuthCredential = GoogleAuthProvider.getCredential(acct.idToken, null)
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
                    val intent = Intent(this, HomeActivity::class.java)
                    startActivity(intent)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    Toast.makeText(
                        this@LoginActivity,
                        "Authentication failed",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }
}


