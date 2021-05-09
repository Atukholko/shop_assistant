package com.tukholko.assistant.auth


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.rengwuxian.materialedittext.MaterialEditText
import com.tukholko.assistant.R
import com.tukholko.assistant.app.service.NetworkService
import com.tukholko.assistant.auth.validator.AuthValidator
import com.tukholko.assistant.model.Shop
import com.tukholko.assistant.model.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class RegisterActivity : AppCompatActivity() {
    val TAG = "REGISTRATION"

    lateinit var authorizationLayout: RelativeLayout
    lateinit var emailField: MaterialEditText
    lateinit var passwordField: MaterialEditText
    lateinit var firstNameField: MaterialEditText
    lateinit var secondNameField: MaterialEditText
    lateinit var progressBar: ProgressBar
    val auth: FirebaseAuth = FirebaseAuth.getInstance()
    val validator: AuthValidator = AuthValidator()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        authorizationLayout = findViewById(R.id.register_layout)
        progressBar = findViewById(R.id.progressBar)

        emailField = findViewById(R.id.emailFieldRegister)
        passwordField = findViewById(R.id.passwordFieldRegister)
        firstNameField = findViewById(R.id.nameFieldRegister)
        secondNameField = findViewById(R.id.secondNameFieldRegister)

        findViewById<Button>(R.id.registerButton).setOnClickListener {
            createAccount()
        }
    }

    private fun createAccount() {
        if (!validateForm(emailField, passwordField, firstNameField, secondNameField)) {
            return
        }
        Log.d(TAG, "createAccount:" + emailField.text.toString())
        progressBar.visibility = ProgressBar.VISIBLE
        auth.createUserWithEmailAndPassword(emailField.text.toString(), passwordField.text.toString())
                .addOnSuccessListener {
                    Log.d(TAG, "createUserWithEmail:success")
                    var user = User(emailField.text.toString(), firstNameField.text.toString(), secondNameField.text.toString())
                    addToDataBase(user)
                    Snackbar.make(authorizationLayout, "Регистрация завершена", Snackbar.LENGTH_SHORT).show()
                    progressBar.visibility = ProgressBar.INVISIBLE
                    auth.signOut()
                    val intent = Intent()
                    intent.putExtra("email", emailField.text.toString())
                    intent.putExtra("password", passwordField.text.toString())
                    setResult(RESULT_OK, intent)
                    finish()
                }
                .addOnFailureListener {
                    Log.e(TAG, "createUserWithEmail:failure(Указана неверная/зарегистрированная почта)")
                    emailField.setText("")
                    Snackbar.make(authorizationLayout, "Указана неверная/зарегистрированная почта", Snackbar.LENGTH_SHORT).show()
                    progressBar.visibility = ProgressBar.INVISIBLE
                }
    }

    private fun addToDataBase(user: User) {
        NetworkService.getInstance()
                .userAPI
                .postData(user).enqueue(object : Callback<User> {
                    override fun onResponse(call: Call<User>, response: Response<User>) {
                        Log.i(TAG, "user is wrote into db")
                    }

                    override fun onFailure(call: Call<User>, t: Throwable) {
                        Log.i(TAG, "user is not wrote into db")
                    }

                })
    }

    private fun validateForm(emailField: MaterialEditText, passwordField: MaterialEditText, firstNameField: MaterialEditText, secondNameField: MaterialEditText): Boolean {
        var result = true
        val email = emailField.text.toString()
        val password = passwordField.text.toString()
        val name = firstNameField.text.toString()
        val phone = secondNameField.text.toString()
        if (!validator.emailValid(email)) {
            emailField.error = "Неверная почта"
            result = false
        }
        if (!validator.passwordValid(password)) {
            passwordField.error = "Пароль должен содержать более 5 символов"
            result = false
        }
        if (!validator.nameValid(name)) {
            firstNameField.error = "Заполните имя и фамилию"
            result = false
        }
        if (!validator.nameValid(phone)) {
            secondNameField.error = "Неверный номер"
            result = false
        }
        return result
    }
}
