package com.tukholko.assistant.app.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.tukholko.assistant.R
import com.tukholko.assistant.app.fragments.dialog.LogoutAlertDialog
import com.tukholko.assistant.app.service.NetworkService
import com.tukholko.assistant.model.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Profile : Fragment() {
    val TAG = "PROFILE"
    lateinit var email: TextView
    lateinit var firstName: TextView
    lateinit var secondName: TextView
    val auth: FirebaseAuth = FirebaseAuth.getInstance()
    lateinit var user: User

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        getUser(auth.currentUser?.email.toString())
        email = view.findViewById(R.id.profileEmail)
        firstName = view.findViewById(R.id.profileName)
        secondName = view.findViewById(R.id.profileSecondName)

        view.findViewById<Button>(R.id.signOut).setOnClickListener {
            logout()
        }
        super.onViewCreated(view, savedInstanceState)
    }

    private fun fillProfile(user: User) {
        email.text = user.email
        firstName.text = user.firstName
        secondName.text = user.secondName
    }

    private fun getUser(email: String) {
        NetworkService.getInstance()
                .userAPI
                .getUserByEmail(email).enqueue(object : Callback<User> {
                    override fun onResponse(call: Call<User>, response: Response<User>) {
                        user = response.body() as User
                        Log.i(TAG, "user from db: $user")
                        fillProfile(user)
                    }

                    override fun onFailure(call: Call<User>, t: Throwable) {
                        user = User(email, "", "")
                        Log.e(TAG, "user read error")
                    }
                })
    }


    private fun logout() {
        fragmentManager?.let { LogoutAlertDialog().show(it, "LogoutDialog") }
    }

    companion object {
        fun newInstance(): Profile = Profile()
    }
}
