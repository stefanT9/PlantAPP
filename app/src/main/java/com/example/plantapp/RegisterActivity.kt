package com.example.plantapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_register.*
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.GoogleAuthProvider
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import kotlinx.android.synthetic.main.activity_top_nav.*

class RegisterActivity : TopNavViewActivity() {

    lateinit var mAuth: FirebaseAuth
    lateinit var listenerState: FirebaseAuth.AuthStateListener
    val RC_SIGN_IN: Int=1

    var TAG = "thisLOGIN"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.layoutInflater.inflate(R.layout.activity_register,mainLayout)

        mAuth = FirebaseAuth.getInstance()
        listenerState = FirebaseAuth.AuthStateListener() {

            fun onAuthStateChanged(fireAuth: FirebaseAuth) {

                var user: FirebaseUser? = mAuth.getCurrentUser()
                if (user != null) {
                    Toast.makeText(this@RegisterActivity, "You are auth!", Toast.LENGTH_SHORT)
                        .show();

                } else {
                    Toast.makeText(this@RegisterActivity, "Please auth!", Toast.LENGTH_SHORT)
                        .show();

                }

            }

        };
        btn_Register.setOnClickListener(View.OnClickListener { it: View? ->


            var email: String = regEmail.text.toString()
            var pass: String = regPassword.text.toString()


            if (email.isEmpty()) {
                regEmail.setError("Please enter email id");
                regEmail.requestFocus();
            } else if (pass.isEmpty()) {

                regPassword.setError("Please enter your password");
                regPassword.requestFocus();

            } else {
                mAuth.createUserWithEmailAndPassword(email, pass)

                    .addOnCompleteListener(this) { task ->

                        if (task.isSuccessful) {

                            // Sign in success, update UI with the signed-in user's information

                            Log.d("create", "createUserWithEmail:success")
                            Toast.makeText(
                                this@RegisterActivity,
                                "Signed up successfully!",
                                Toast.LENGTH_SHORT
                            ).show();
                            val user = mAuth.currentUser

                            // updateUI(user)

                        } else {

                            // If sign in fails, display a message to the user.

                            Log.w("failed", "createUserWithEmail:failure", task.exception)

                            Toast.makeText(
                                this@RegisterActivity, "Authentication failed.",

                                Toast.LENGTH_SHORT
                            ).show()

                            // updateUI(null)
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
       startActivityForResult(signInIntent,RC_SIGN_IN)

 }

    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase

                Log.d(TAG, "signInWithCredential:success")
                val account = task.getResult(ApiException::class.java)
                Toast.makeText(
                    this@RegisterActivity,
                    "Signed in with Google Successfully",
                    Toast.LENGTH_SHORT
                ).show()
                firebaseAuthWithGoogle(account!!)
            } catch (e: ApiException) {
                Log.w(TAG, "signInWithCredential:failure", task.exception)
                Toast.makeText(this@RegisterActivity, "Authetication failed", Toast.LENGTH_SHORT)
                    .show()
                //  firebaseAuthWithGoogle(null)
            }
        }
    }
    fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {

        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.id!!)
        val credential: AuthCredential = GoogleAuthProvider.getCredential(acct.idToken, null)
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")
                    Toast.makeText(
                        this@RegisterActivity,
                        "Signed in with Google Successfully",
                        Toast.LENGTH_SHORT
                    ).show()
                    val user = mAuth.currentUser
                    updateUI(user!!)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    Toast.makeText(
                        this@RegisterActivity,
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

            Toast.makeText(this@RegisterActivity, personName + personEmail, Toast.LENGTH_SHORT)
                .show()
        }
    }
}










