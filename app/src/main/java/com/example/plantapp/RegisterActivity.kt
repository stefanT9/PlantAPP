package com.example.plantapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.*
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.activity_top_nav.*
import kotlinx.android.synthetic.main.top_nav_login_fragment.*
import java.util.regex.Pattern


class RegisterActivity : TopNavViewActivity() {

    private lateinit var mAuth: FirebaseAuth
    private lateinit var listenerState: FirebaseAuth.AuthStateListener
    private val rcSignIn: Int = 1

    private var tag = "thisLOGIN"
    private var errorText = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.layoutInflater.inflate(R.layout.activity_register, mainLayout)
        foto.visibility = View.GONE
        mAuth = FirebaseAuth.getInstance()
        val db = FirebaseFirestore.getInstance()

        btn_Register.setOnClickListener(View.OnClickListener {
            val email: String = regEmail.text.toString()
            val pass: String = regPassword.text.toString()

            val ussname = editText.text.toString()
            if(ussname.isEmpty())
            {
                regEmail.error = "Username Required";
                regEmail.requestFocus();
            }
            if (!isValidEmail(email)) {
                regEmail.error = errorText
                regEmail.requestFocus();
            }
            else if (!isValidPassword(pass)){
                regPassword.error = errorText
                regPassword.requestFocus()
            }
            else {
                mAuth.createUserWithEmailAndPassword(email, pass)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            Log.d("create", "createUserWithEmail:success")

                            val profileUpdates =
                                UserProfileChangeRequest.Builder()
                                    .setDisplayName(ussname).build()

                            val user = mAuth.currentUser
                            user?.updateProfile(profileUpdates)

                            val intent = Intent(this, HomeActivity::class.java)
                            startActivity(intent)
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

        val gso =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build()

        // Build a GoogleSignInClient with the options specified by gso.
        val mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        sign_in_button.setOnClickListener {
            val signInIntent = mGoogleSignInClient.signInIntent
            startActivityForResult(signInIntent, rcSignIn)

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

                val intent = Intent(this, HomeActivity::class.java)
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
                    val intent = Intent(this, HomeActivity::class.java)
                    startActivity(intent)
                    val user = mAuth.currentUser
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

    private fun isValidPassword(pass: String? ) : Boolean{
        val digitRegex = "(.)*(\\\\d)(.)*"
        val specialCharRegex = "(.)*([\$&+,:;=\\\\\\\\?@#|/'<>.^*()%!-])(.)*"
        val passLength = 5;

        if (pass == null || pass.isEmpty()) {
            errorText = "Please enter your password"
            return false
        }

        if ( pass.length < passLength) {
            errorText = "Password must have at least $passLength characters"
            return false
        }

        if ( pass == pass.toLowerCase()) {
            errorText = "Password should have at least 1 uppercase"
            return false
        }

        if ( pass == pass.toUpperCase()) {
            errorText = "Password should have at least 1 lowercase"
            return false
        }

        if( Pattern.compile(digitRegex).matcher(pass).matches() ) {
            errorText = "Password should have at least 1 digit"
            return false
        }

        if( !Pattern.compile(specialCharRegex).matcher(pass).matches() ){
            errorText = "Password should have at least 1 special character"
            return false
        }

        return true
    }

    private fun isValidEmail(email: String?) : Boolean{
        if (email == null || email.isEmpty()) {
            errorText = "Please enter email id";
            return false
        }
        else if ( !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            errorText = "Please enter a valid email"
            return false
        }
        return true
    }
}










