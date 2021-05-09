package com.tukholko.assistant.app.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.tukholko.assistant.R
import com.tukholko.assistant.app.AppActivity
import com.tukholko.assistant.model.User

class Profile : Fragment() {
    lateinit var email: TextView
    lateinit var firstName: TextView
    lateinit var secondName: TextView
    val auth: FirebaseAuth = FirebaseAuth.getInstance()
    lateinit var user: User

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        email = view.findViewById(R.id.profileEmail)
        email.text = auth.currentUser?.email ?: "Error"
        view.findViewById<Button>(R.id.signOut).setOnClickListener {
            (activity as AppActivity?)!!.signOut()
        }
        super.onViewCreated(view, savedInstanceState)
    }

    private fun getUser(email: String) {
        //TODO make reading from db
    }

    companion object {
        fun newInstance(): Profile = Profile()
    }
}
