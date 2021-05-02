package com.tukholko.assistant.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.rengwuxian.materialedittext.MaterialEditText
import com.tukholko.assistant.R
import com.tukholko.assistant.app.AppActivity
import com.tukholko.assistant.auth.validator.AuthValidator

class LoginActivity : AppCompatActivity() {
    val TAG = "LOGIN"
    lateinit var authorizationLayout: RelativeLayout
    lateinit var emailField: MaterialEditText
    lateinit var passwordField: MaterialEditText

    lateinit var auth: FirebaseAuth
    lateinit var progressBar: ProgressBar
    var validator: AuthValidator = AuthValidator()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        findViewById<Button>(R.id.signInButton).setOnClickListener {
            signIn()
        }

        findViewById<TextView>(R.id.moveToSignUp).setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivityForResult(intent, 1)
        }
        progressBar = findViewById(R.id.progressBarLogin)
        auth = FirebaseAuth.getInstance()
        emailField = findViewById(R.id.emailField)
        passwordField = findViewById(R.id.passwordField)
        authorizationLayout = findViewById(R.id.login_layout)
    }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        updateUI(currentUser)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data == null) {
            return
        }
        val email = data.getStringExtra("email")
        val password = data.getStringExtra("password")
        emailField.setText(email)
        passwordField.setText(password)
    }

    private fun signIn() {
        if (!validateForm(emailField, passwordField)) {
            return
        }
        Log.d(TAG, "signInAccount:" + emailField.text.toString())
        progressBar.visibility = ProgressBar.VISIBLE
        auth.signInWithEmailAndPassword(emailField.text.toString(), passwordField.text.toString())
                .addOnSuccessListener {
                    Log.d(TAG, "signInWithEmail:success")
                    val user = auth.currentUser
                    updateUI(user)
                    progressBar.visibility = ProgressBar.INVISIBLE
                }
                .addOnFailureListener {
                    Log.e(TAG, "signInUserWithEmail:failure(Не существует такой учетной записи)")
                    emailField.setText("")
                    passwordField.setText("")
                    Snackbar.make(
                            authorizationLayout,
                            "Не существует такой учетной записи",
                            Snackbar.LENGTH_SHORT
                    ).show()
                    progressBar.visibility = ProgressBar.INVISIBLE
                }
    }

    private fun validateForm(
            emailField: MaterialEditText,
            passwordField: MaterialEditText
    ): Boolean {
        var result = true
        val email = emailField.text.toString()
        val password = passwordField.text.toString()
        if (!validator.emailValid(email)) {
            emailField.error = "Неверная почта"
            result = false
        }
        if (!validator.passwordValid(password)) {
            passwordField.error = "Неверный пароль"
            result = false
        }
        return result
    }

    private fun updateUI(user: FirebaseUser?) {
        if (user != null) {
            finish()
            startActivity(Intent(this, AppActivity::class.java))
        } else {
            return
        }
    }
}
