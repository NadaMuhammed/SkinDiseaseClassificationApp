package com.example.gp.settings

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_settings.view.*
import com.example.gp.MainActivity
import com.example.gp.R
import com.example.gp.login.LoginViewModel
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FileDownloadTask
import com.google.firebase.storage.FirebaseStorage
import java.io.File
import java.lang.Exception


class SettingsFragment : Fragment() {
    lateinit var mainViewModel: LoginViewModel
    lateinit var mAuth: FirebaseAuth
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_settings, container, false)
        mainViewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        mAuth = FirebaseAuth.getInstance()
        val username = view.findViewById<TextView>(R.id.username)
        val firstName = view.findViewById<TextView>(R.id.firstName)
        val lastName = view.findViewById<TextView>(R.id.lastName)
        val email = view.findViewById<TextView>(R.id.email)
        val type = view.findViewById<TextView>(R.id.type)

        mainViewModel.userProfile("bearer "+activity?.getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE)?.getString("access_token","").toString())

        mainViewModel.profileResponse.observe(viewLifecycleOwner, Observer {
            username.text = it.body()?.username.toString()
            firstName.text = it.body()?.fristname
            lastName.text = it.body()?.lastname
            email.text = it.body()?.email
            type.text = it.body()?.type
            val storageReference = FirebaseStorage.getInstance().getReference("users/"+activity?.getSharedPreferences("PREFERENCE_NAME",Context.MODE_PRIVATE)?.getString("uid","").toString())
            try {
                val localFile = File.createTempFile("tempfile",".jpg")
                storageReference.getFile(localFile).addOnSuccessListener(object :
                    OnSuccessListener<FileDownloadTask.TaskSnapshot> {
                    override fun onSuccess(p0: FileDownloadTask.TaskSnapshot?) {
                        val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)
                        view.findViewById<de.hdodenhof.circleimageview.CircleImageView>(R.id.profilePic).setImageBitmap(bitmap)
                    }

                })
            } catch (e: Exception){
                e.printStackTrace()
            }
        })


        Log.v("profile", mainViewModel.profileResponse.value?.body().toString())

        view.editBtn.setOnClickListener {
            findNavController().navigate(R.id.action_settingsFragment_to_editProfileFragment)
        }

        view.logoutBtn.setOnClickListener {
            Log.v("token2", activity?.getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE)?.getString("access_token","").toString())
//            val handler = Handler()
//
//            handler.postDelayed(Runnable { val dialog = ProgressDialog.show(
//                context, "",
//                "Loading. Please wait...", true
//            ) }, 3000)
            setSharedPreferenceToNull()
            mAuth.signOut()
            val intent = Intent(this.requireContext(), MainActivity::class.java)
            intent.putExtra("access_token","null")
            startActivity(intent)

        }

        return view

    }

    fun setSharedPreferenceToNull() {
        val sharedPreference =
            context?.getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE)
        var editor = sharedPreference?.edit()

        if (editor != null) {
            editor.putString("access_token", "null")
            editor.apply()
        }
    }
}