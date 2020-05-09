package com.example.plantapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_register.*
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.GoogleAuthProvider
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import kotlinx.android.synthetic.main.activity_top_nav.*

class RegisterActivity : TopNavViewActivity() {

    private lateinit var mAuth: FirebaseAuth
    private lateinit var listenerState: FirebaseAuth.AuthStateListener
    private val rcSignIn: Int=1

    private var tag = "thisLOGIN"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.layoutInflater.inflate(R.layout.activity_register,mainLayout)

        mAuth = FirebaseAuth.getInstance()
        listenerState = FirebaseAuth.AuthStateListener() {

            fun onAuthStateChanged(fireAuth: FirebaseAuth) {

                val user: FirebaseUser? = mAuth.currentUser
                if (user != null) {
                    Toast.makeText(this@RegisterActivity, "You are auth!", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    Toast.makeText(this@RegisterActivity, "Please auth!", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
        btn_Register.setOnClickListener(View.OnClickListener {
            val email: String = regEmail.text.toString()
            val pass: String = regPassword.text.toString()

            if (email.isEmpty()) {
                regEmail.error = "Please enter email id";
                regEmail.requestFocus();
            } else if (pass.isEmpty()) {
                regPassword.error = "Please enter your password";
                regPassword.requestFocus();
            } else {
                mAuth.createUserWithEmailAndPassword(email, pass)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            Log.d("create", "createUserWithEmail:success")

                            val intent= Intent(this,HomeActivity::class.java)
                            startActivity(intent)

//                            Toast.makeText(
//                                this@RegisterActivity,
//                                "Signed up successfully!",
//                                Toast.LENGTH_SHORT
//                            ).show();

                            val user = mAuth.currentUser
                            // updateUI(user)
                        } else {

                            // If sign in fails, display a message to the user.

                            Log.w("failed", "createUserWithEmail:failure", task.exception)

                            Toast.makeText(
                                this@RegisterActivity, "Authentication failed.",

                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            }
        })

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        val gso =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build()

        // Build a GoogleSignInClient with the options specified by gso.
        val mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

          sign_in_button.setOnClickListener{
        val signInIntent=mGoogleSignInClient.signInIntent
       startActivityForResult(signInIntent,rcSignIn)

 }

    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == rcSignIn) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase

                Log.d(tag, "signInWithCredential:success")
                val account = task.getResult(ApiException::class.java)

                val intent= Intent(this,HomeActivity::class.java)
                startActivity(intent)

//                Toast.makeText(
//                    this@RegisterActivity,
//                    "Signed in with Google Successfully",
//                    Toast.LENGTH_SHORT
//                ).show()
                firebaseAuthWithGoogle(account!!)
            } catch (e: ApiException) {
                Log.w(tag, "signInWithCredential:failure", task.exception)
                Toast.makeText(this@RegisterActivity, "Authetication failed", Toast.LENGTH_SHORT)
                    .show()
                //  firebaseAuthWithGoogle(null)
            }
        }
    }
    fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {

        Log.d(tag, "firebaseAuthWithGoogle:" + acct.id!!)
        val credential: AuthCredential = GoogleAuthProvider.getCredential(acct.idToken, null)
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(tag, "signInWithCredential:success")
                    /// TODO: Replace toasts with activity transfers to home activity(Daniel Bicu)

                    val intent= Intent(this,HomeActivity::class.java)
                    startActivity(intent)

//                    Toast.makeText(
//                        this@RegisterActivity,
//                        "Signed in with Google Successfully",
//                        Toast.LENGTH_SHORT
//                    ).show()

                    val user = mAuth.currentUser
                    updateUI(user!!)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(tag, "signInWithCredential:failure", task.exception)
                    Toast.makeText(
                        this@RegisterActivity,
                        "Authentication failed",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }
    fun updateUI(fUser: FirebaseUser) {

        val account = GoogleSignIn.getLastSignedInAccount(applicationContext)

        if (account != null) {
            val personName = account.displayName
            val personEmail = account.email

            Toast.makeText(this@RegisterActivity, personName + personEmail, Toast.LENGTH_SHORT)
                .show()
        }
    }
}










