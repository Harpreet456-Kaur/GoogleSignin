package com.example.googlesignin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.googlesignin.databinding.ActivityMainBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
     lateinit var auth : FirebaseAuth
    lateinit var gso : GoogleSignInOptions
    lateinit var googleSignInClient : GoogleSignInClient
    var googleSigninResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        System.out.println("result ${it.toString()}")
        System.out.println("result data ${it.data.toString()}")
        val task = GoogleSignIn.getSignedInAccountFromIntent(it.data)
        try {
            // Google Sign In was successful, authenticate with Firebase
            val account = task.getResult(ApiException::class.java)!!
            firebaseAuthWithGoogle(account.idToken!!)
        } catch (e: ApiException) {
            // Google Sign In failed, update UI appropriately
            System.out.println("in exception $e")
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        auth= FirebaseAuth.getInstance()
        setContentView(binding.root)

        gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("74866074305-t7skri8hct03tkh3ui41na12o2v05vej.apps.googleusercontent.com")
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        binding.btnOk.setOnClickListener {
            val signInIntent: Intent = googleSignInClient.signInIntent
            googleSigninResult.launch(signInIntent)
        }
    }


    fun firebaseAuthWithGoogle(token: String){
        val credential = GoogleAuthProvider.getCredential(token, null)
        auth.signInWithCredential(credential).addOnSuccessListener {
            System.out.println("in successfully registered")
            Toast.makeText(this, "Successfully registered ", Toast.LENGTH_LONG).show()
        }.addOnFailureListener {
            System.out.println("${it.message}")
            Toast.makeText(this, "${it.message} ", Toast.LENGTH_LONG).show()

        }
    }

}