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
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.layoutInflater.inflate(R.layout.activity_register, mainLayout)

        mAuth = FirebaseAuth.getInstance()
        val db = FirebaseFirestore.getInstance()
        listenerState = FirebaseAuth.AuthStateListener() {

            fun onAuthStateChanged(fireAuth: FirebaseAuth) {

                val user: FirebaseUser? = mAuth.currentUser
                if (user != null) {
                    username.setText(FirebaseAuth.getInstance().currentUser?.displayName.toString())
                    mail.setText(FirebaseAuth.getInstance().currentUser?.email.toString())
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
            val ussname: String = editText.text.toString()
            if(ussname.isEmpty())
            {
                regEmail.error = "Username Required";
                regEmail.requestFocus();
            }
            if (email.isEmpty()) {
                regEmail.error = "Please enter email id";
                regEmail.requestFocus();
            }
            else if ( !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                regEmail.error = "Please enter a valid email"
                regEmail.requestFocus()
            }
            else if (pass.isEmpty()) {
                regPassword.error = "Please enter your password";
                regPassword.requestFocus();
            }
            else if ( pass.length < 5) {
                regPassword.error = "Password must have at least 5 characters";
                regPassword.requestFocus();
            }
            else if ( pass == pass.toLowerCase()) {
                regPassword.error = "Password should have at least 1 uppercase";
                regPassword.requestFocus();
            }
            else if ( pass == pass.toUpperCase()) {
                regPassword.error = "Password should have at least 1 lowercase";
                regPassword.requestFocus();
            }
            else if( Pattern.compile("(.)*(\\\\d)(.)*").matcher(pass).matches() ) {
                regPassword.error = "Password should have at least 1 digit";
                regPassword.requestFocus();
            }
            else if( !Pattern.compile("(.)*([\$&+,:;=\\\\\\\\?@#|/'<>.^*()%!-])(.)*").matcher(pass).matches() ){
                regPassword.error = "Password should have at least 1 special character";
                regPassword.requestFocus();
            }
            else {
                /// TODO: Update firebase username after the register is complete ( Alexandra Ciocoiu )
                mAuth.createUserWithEmailAndPassword(email, pass)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            Log.d("create", "createUserWithEmail:success")
                            val userr: MutableMap<String, Any> =
                                HashMap()
                            userr.put("username",ussname)


// Add a new document with a generated ID

// Add a new document with a generated ID
                            db.collection("users") 
                                .add(userr)
                                .addOnSuccessListener(OnSuccessListener<DocumentReference> { documentReference ->
                                    Log.d(
                                        tag,
                                        "DocumentSnapshot added with ID: " + documentReference.id
                                    )
                                })
                                .addOnFailureListener(OnFailureListener { e ->
                                    Log.w(
                                        tag,
                                        "Error adding document",
                                        e
                                    )
                                })
                            val intent = Intent(this, HomeActivity::class.java)
                            startActivity(intent)
                            val user = mAuth.currentUser

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
}










