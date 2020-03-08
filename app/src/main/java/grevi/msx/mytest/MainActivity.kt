package grevi.msx.mytest

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    val RC_SIGN = 1
    private lateinit var mGoogleSignClient : GoogleSignInClient
    private lateinit var mGoogleSignOptions : GoogleSignInOptions
    private lateinit var firebaseAuth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        configureSignSettings()
        setupButton()
        firebaseAuth = FirebaseAuth.getInstance()
    }

    private fun configureSignSettings() {
        mGoogleSignOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build()
        mGoogleSignClient = GoogleSignIn.getClient(this, mGoogleSignOptions)
    }

    private fun setupButton() {
        btn_sign_in.setOnClickListener{
            signIn()
        }
    }

    private fun signIn() {
        val signIntent : Intent = mGoogleSignClient.signInIntent
        startActivityForResult(signIntent, RC_SIGN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN) {
            val task : Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val akun = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(akun)
            } catch (e : ApiException) {

            }
        }
    }

    private fun firebaseAuthWithGoogle(akun: GoogleSignInAccount?) {
        val credential = GoogleAuthProvider.getCredential(akun?.idToken, null)
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener{
            if (it.isSuccessful) {
                val mIntent = Intent(this, HomeActivity::class.java).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                }
                startActivity(mIntent)
            } else {
                Toast.makeText(this, "Gagal Masuk Dengan Google", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            val mIntent = Intent(this, HomeActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            }
            startActivity(mIntent)
            finish()
        }
    }
}
