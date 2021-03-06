package grevi.msx.mytest

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {

    private lateinit var firebaseAuth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        firebaseAuth = FirebaseAuth.getInstance()
        setupButton()

    }

    private fun setupButton() {
        val users = firebaseAuth.currentUser

        tv_name.text = users?.uid
        tv_email.text = users?.email
        tv_dName.text = users?.displayName
        tv_phone.text = users?.phoneNumber

        btn_logout.setOnClickListener{
            signOut()
        }
    }

    private fun signOut() {
        val mIntent = Intent(this, MainActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        }
        startActivity(mIntent)
        FirebaseAuth.getInstance().signOut()
    }
}
