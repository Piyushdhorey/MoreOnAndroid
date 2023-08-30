package com.example.googleauthentication

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.example.googleauthentication.databinding.ActivityMainBinding
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.FacebookSdk
import com.facebook.appevents.AppEventsLogger
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthCredential
import com.google.firebase.auth.GoogleAuthProvider

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var callbackManager: CallbackManager
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        FacebookSdk.sdkInitialize(applicationContext)

        val googleSignOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, googleSignOptions)

        findViewById<Button>(R.id.signInBtn).setOnClickListener {
            signInGoogle()
        }

        callbackManager = CallbackManager.Factory.create()

        binding.FbSignInBtn.setPermissions("email", "public_profile")
        binding.FbSignInBtn.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) {
                Log.d("Facebook on success", "facebook:onSuccess:$loginResult")
                handleFacebookAccessToken(loginResult.accessToken)
            }

            override fun onCancel() {
                Log.d("FB Cancel", "facebook:onCancel")
                // ...
            }

            override fun onError(error: FacebookException) {
                Log.d("FB Error", "facebook:onError", error)
                // ...
            }
        })
        // ...
        fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
            super.onActivityResult(requestCode, resultCode, data)

            // Pass the activity result back to the Facebook SDK
            callbackManager.onActivityResult(requestCode, resultCode, data)
        }

    }

    private fun handleFacebookAccessToken(token: AccessToken) {
        Log.d("Facebook Access Token", "handleFacebookAccessToken:$token")

        val credential = FacebookAuthProvider.getCredential(token.token)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("Facebook Success", "signInWithCredential:success")
                    val user = auth.currentUser
//                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("Facebook Failure", "signInWithCredential:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
//                    updateUI(null)
                }
            }
    }


    private fun signInGoogle() {
        val signInIntent = googleSignInClient.signInIntent
        launcher.launch(signInIntent)
    }

    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        result ->
                if (result.resultCode == RESULT_OK){
                    val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                    handleResults(task)
                }
    }
    private fun handleResults(task: Task<GoogleSignInAccount>){
        if(task.isSuccessful){
            val account: GoogleSignInAccount? = task.result
            if(account!=null){
                updateUI(account)
            }
        }else{
            Toast.makeText(this, task.exception.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateUI(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        auth.signInWithCredential(credential).addOnCompleteListener{
            if(it.isSuccessful){
                val intent = Intent(this, HomeActivity::class.java)
                intent.putExtra("email", account.email)
                intent.putExtra("name", account.displayName)
                startActivity(intent)
            }else{
                Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
            }
        }

    }

}
