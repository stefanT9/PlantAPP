/*package com.example.plantapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.android.synthetic.main.activity_login.*

class loginActivity : AppCompatActivity() {

    lateinit var emailID: EditText
    lateinit var password: EditText
    lateinit var buttonSignIn: TextView
    lateinit var googleClientSignIN: GoogleSignInClient
    lateinit var googleBtn: SignInButton
    lateinit var auth: FirebaseAuth
    var TAG = "thisLOGIN"
    // google stuff

    val RC_SIGN_IN: Int = 1

    // ------------

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        lateinit var authent: FirebaseAuth
        lateinit var listenerState: FirebaseAuth.AuthStateListener

        authent = FirebaseAuth.getInstance()
        emailID = findViewById(R.id.editText3)
        password = findViewById(R.id.editText6)
        buttonSignIn = findViewById(R.id.button)
        googleBtn = findViewById(R.id.sign_in_button)
        auth = FirebaseAuth.getInstance()

        listenerState = FirebaseAuth.AuthStateListener() {

            fun onAuthStateChanged(fireAuth: FirebaseAuth) {

                var user: FirebaseUser? = authent.getCurrentUser()
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
                emailID.setError("Please enter email id")
                emailID.requestFocus()
            } else if (pwd.isEmpty()) {

                password.setError("Please enter your password")
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
                                "Login succesfully ...",
                                Toast.LENGTH_SHORT
                            ).show()
                            val user = authent.currentUser
                            // updateUI(user);
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

        // ---GOOGLE SIGN IN ----

        // Configure Google Sign In
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
        val credential: AuthCredential = GoogleAuthProvider.getCredential(acct.idToken, null)
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
*/