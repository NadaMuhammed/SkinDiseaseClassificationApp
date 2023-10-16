package com.example.gp.login

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.gp.doctor.DoctorActivity
import com.example.gp.MainActivity2
import com.example.gp.R
import com.example.gp.model.LoginRequest
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import java.lang.Exception

class PatientLoginFragment : Fragment() {
    lateinit var mainViewModel: LoginViewModel
    lateinit var mAuth: FirebaseAuth
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_patient_login, container, false)
        mainViewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        mAuth = FirebaseAuth.getInstance()

//        (activity as AppCompatActivity).supportActionBar!!.hide()
        val email = view.findViewById<EditText>(R.id.email_ett)
        val username = view.findViewById<EditText>(R.id.username_et)
        val password = view.findViewById<EditText>(R.id.password_et)


        val hardUserName = "nourhanshaban@gmail.com"
        val hardPassword = "1234567890"

//        val map : HashMap<String,String> = HashMap()
//        map["email"] = hardUserName
//        map["password"] = hardPassword

        view.findViewById<Button>(R.id.Login_btn).setOnClickListener {
            if (username.text.isEmpty() || password.text.isEmpty()) {
                Snackbar.make(
                    view.findViewById(R.id.myCoordinatorLayout),
                    "Please fill all the fields",
                    Snackbar.LENGTH_SHORT
                ).show()
            } else {
                try {
                    mainViewModel.loginRequest(
                        LoginRequest(
                            email.text.toString(),
                            username.text.toString(),
                            password.text.toString()
                        )
                    )
//                mainViewModel.loginRequest(map)
                } catch (e: Exception) {
                    Log.v("PatientLoginFragment", e.message.toString())
                }
            }
            Log.v("PatientLoginFragment", "Button CLiked")
        }
        mainViewModel.loginResponse.observe(viewLifecycleOwner) {

            Log.v("PatientLoginFragment", it.code().toString())
            Log.v("PatientLoginFragment", it.message())
            val response = it.body()

            if (it.isSuccessful) {
                ProgressDialog.show(context, "Loading", "Wait while logging you in...")
                try {
                    mAuth.signInWithEmailAndPassword(
                        email.text.toString(),
                        password.text.toString()
                    ).addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                if (it.body()!!.type[0] == "patient") {
                                    saveToSharedPreference(response!!.access_token)
                                    val intent = Intent(activity, MainActivity2::class.java)
                                    intent.putExtra("access_token", response.access_token)
                                    intent.putExtra("uid", mAuth.currentUser?.uid.toString())
                                    startActivity(intent)
                                } else{
                                    val intent = Intent(activity, DoctorActivity::class.java)
                                    intent.putExtra("access_token", response!!.access_token)
                                    intent.putExtra("uid", mAuth.currentUser?.uid.toString())
                                    startActivity(intent)
                                }
                            } else {
                                Snackbar.make(
                                    view.findViewById(R.id.myCoordinatorLayout),
                                    "Some error occurred",
                                    Snackbar.LENGTH_SHORT
                                ).show()
                            }
                        }

                } catch (e: Exception) {
                    Log.v("PatientLoginFragment", e.message.toString())
                }
            } else {
                Snackbar.make(
                    view.findViewById(R.id.myCoordinatorLayout),
                    "This user isn't registered",
                    Snackbar.LENGTH_SHORT
                ).show()
            }

        }

        view.findViewById<TextView>(R.id.signUp_txt).setOnClickListener {
            findNavController().navigate(R.id.action_patientLoginFragment_to_registerFragment)
        }
        return view
    }

    fun saveToSharedPreference(access_token: String?) {
        val sharedPreference =
            activity?.getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE)
        var editor = sharedPreference?.edit()
        if (editor != null) {
            editor.putString("access_token", access_token)
            editor.commit()
        }
    }
}