package com.example.note

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider

class AuthViewModel: ViewModel() {

     private val TAG = "AuthViewModel"
     private val _navigateToNotes = MutableLiveData<Boolean>()
     val navigateToNotes:LiveData<Boolean> get() = _navigateToNotes
     lateinit var googleSignInClient: GoogleSignInClient
     private lateinit var firebaseAuth: FirebaseAuth
     private lateinit var account:GoogleSignInAccount

    fun initGoogleSignIn(activity: Activity, context: Context){
        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(context.getString(R.string.WEB_CLIENT_ID))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(context,googleSignInOptions)
        firebaseAuth = FirebaseAuth.getInstance()

        val firebaseUser: FirebaseUser? = firebaseAuth.currentUser

        if(firebaseUser!= null){
            _navigateToNotes.value = true
        }



    }

    fun signInWithGoogle(activity: Activity) {

        val intent: Intent = googleSignInClient.signInIntent
        activity.startActivityForResult(intent, 100)
    }

    fun handleSignInResult(data: Intent?, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        val task = GoogleSignIn.getSignedInAccountFromIntent(data)
        try {
            val account = task.getResult(ApiException::class.java)!!
            firebaseAuthWithGoogle(account, onSuccess, onFailure)
//            firebaseAuthWithGoogle(account.idToken!!)
//            onSuccess
        } catch (e: ApiException) {
            Log.w(TAG, "Google sign in failed", e)
            onFailure("Google sign in failed: ${e.message}")
        }
    }

    fun signOut(onSuccess: () -> Unit, onError: (String) -> Unit) {
        firebaseAuth = FirebaseAuth.getInstance()

        firebaseAuth.signOut()
        googleSignInClient.signOut().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                onSuccess()
            } else {
                onError("Sign out failed")
            }
        }
    }

    private fun firebaseAuthWithGoogle(account: GoogleSignInAccount, onSuccess: () -> Unit, onFailure: (String) -> Unit) {

        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onSuccess()
                } else {
                    onFailure("Firebase Authentication failed.")
                }
            }


    }

    }



