package com.example.gp.register

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.view.isEmpty
import androidx.core.view.isNotEmpty
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.gp.R
import com.example.gp.chat.User
import com.example.gp.login.LoginViewModel
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import kotlinx.android.synthetic.main.fragment_register.*
import java.io.InputStream
import java.lang.Exception
import kotlin.random.Random

class RegisterFragment : Fragment() {
    lateinit var mainViewModel: LoginViewModel
    lateinit var mAuth: FirebaseAuth
    lateinit var mDbRef: DatabaseReference
    var gender: String = ""
    var type: String = ""
    lateinit var bitmap: Bitmap
    lateinit var image: ImageView
    lateinit var filePath: Uri

    @SuppressLint("CutPasteId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_register, container, false)
        mainViewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        mAuth = FirebaseAuth.getInstance()

        val firstName = view.findViewById<EditText>(R.id.firstname_et)
        val lastName = view.findViewById<EditText>(R.id.lastname_et)
        val radioGroup = view.findViewById<RadioGroup>(R.id.gender)
        val userName = view.findViewById<EditText>(R.id.username_et)
        val email = view.findViewById<EditText>(R.id.email_et)
        val password = view.findViewById<EditText>(R.id.password_et)
        val passwordConfirm = view.findViewById<EditText>(R.id.passwordConfirmation_et)
        image = view.findViewById(R.id.image)

        image.setOnClickListener {
            Dexter.withActivity(requireActivity())
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(object : PermissionListener {
                    override fun onPermissionGranted(p0: PermissionGrantedResponse?) {
                        val intent = Intent(Intent.ACTION_PICK)
                        intent.setType("image/*")
                        startActivityForResult(Intent.createChooser(intent, "Select Image File"), 1)
                    }

                    override fun onPermissionDenied(p0: PermissionDeniedResponse?) {
                        TODO("Not yet implemented")
                    }

                    override fun onPermissionRationaleShouldBeShown(
                        p0: PermissionRequest?,
                        p1: PermissionToken?
                    ) {
                        p1?.continuePermissionRequest()
                    }

                }).check()
        }

        view.findViewById<Button>(R.id.signUp_btn).setOnClickListener {

            val validFirstname = validateName(firstName)
            val validLastname = validateName(lastName)
            val validUsername = validateName(userName)
            val validEmail = validateEmail(email)
            val validPassword = validateName(password)
            val validConfirmation = validatePassword(password, passwordConfirm)
            gender =
                if (view.findViewById<RadioButton>(R.id.genderMale).isChecked) {
                    "male"
                } else {
                    "female"
                }
            type =
                if (view.findViewById<RadioButton>(R.id.typePatient).isChecked) {
                    "patient"
                } else {
                    "doctor"
                }
            if (validFirstname && validLastname && validUsername &&
                validEmail && validPassword &&
                validConfirmation
            ) {

                val newUser = RegisterRequest(
                    firstName.text.toString(),
                    lastName.text.toString(),
                    gender,
                    type,
                    userName.text.toString(),
                    email.text.toString(),
                    password.text.toString(),
                    passwordConfirm.text.toString(),
                )
                try {
                    mainViewModel.registerUser(newUser)

                } catch (e: Exception) {
                    Log.v("RegisterFragment", e.cause.toString())
                }
            } else {
                Snackbar.make(
                    view.findViewById(R.id.myCoordinatorLayout),
                    "Please fill all the fields",
                    Snackbar.LENGTH_SHORT
                ).show()

            }

        }

        mainViewModel.registerResponse.observe(viewLifecycleOwner) {
            if (it.isSuccessful) {
                mAuth.createUserWithEmailAndPassword(
                    email.text.toString(),
                    password.text.toString()
                ).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        if (type == "doctor") {
                            addDoctorToDatabase(
                                "Dr " + userName.text.toString(),
                                email.text.toString(),
                                mAuth.currentUser!!.uid
                            )
                            Snackbar.make(
                                view.findViewById(R.id.myCoordinatorLayout),
                                "you registered successfully",
                                Snackbar.LENGTH_SHORT
                            ).show()
                            findNavController().navigate(R.id.action_registerFragment_to_patientLoginFragment)
                        } else if (type == "patient") {
                            addPatientToDatabase(
                                userName.text.toString(),
                                email.text.toString(),
                                mAuth.currentUser!!.uid
                            )
                            Snackbar.make(
                                view.findViewById(R.id.myCoordinatorLayout),
                                "you registered successfully",
                                Snackbar.LENGTH_SHORT
                            ).show()
                            findNavController().navigate(R.id.action_registerFragment_to_patientLoginFragment)
                        }
//                        addUserToDatabase(
//                            userName.text.toString(),
//                            email.text.toString(),
//                            mAuth.currentUser!!.uid
//                        )
//                        Snackbar.make(
//                            view.findViewById(R.id.myCoordinatorLayout),
//                            "you registered successfully",
//                            Snackbar.LENGTH_SHORT
//                        ).show()
//                        findNavController().navigate(R.id.action_registerFragment_to_patientLoginFragment)
                    } else {

                        Snackbar.make(
                            view.findViewById(R.id.myCoordinatorLayout),
                            "Some error occurred",
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }
                }

            }
//            else if (it.code() == 401) {
//                Log.v("RegisterFragment", it.code().toString())
//            }
            else {
                Log.v("unauthorized",it.message().toString())
                Snackbar.make(
                    view.findViewById(R.id.myCoordinatorLayout),
                    "This user is already taken",
                    Snackbar.LENGTH_SHORT
                ).show()
//                val unAuthorized : RegisterResponseUnAuthorized = it.body().
                Log.v("RegisterFragment", it.body()?.message.toString())

            }
        }

        return view

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            filePath = data?.data!!
            try {
                val inputStream: InputStream? = activity?.contentResolver?.openInputStream(filePath)
                bitmap = BitmapFactory.decodeStream(inputStream)
                image.setImageBitmap(bitmap)
            } catch (e: Exception) {

            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun addUserToDatabase(name: String, email: String, uid: String) {
        mDbRef = FirebaseDatabase.getInstance().reference
        mDbRef.child("user").child(uid).setValue(User(name, email, uid))
        val storageReference = FirebaseStorage.getInstance().getReference("users/$uid")
        storageReference.putFile(filePath).addOnSuccessListener {
//            Toast.makeText(context, "Profile Success", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener {
//            Toast.makeText(context, "Profile Failure", Toast.LENGTH_SHORT).show()
        }
    }

    private fun addDoctorToDatabase(name: String, email: String, uid: String) {
        mDbRef = FirebaseDatabase.getInstance().reference
        mDbRef.child("doctor").child(uid).setValue(User(name, email, uid))
        val storageReference = FirebaseStorage.getInstance().getReference("users/$uid")
        storageReference.putFile(filePath).addOnSuccessListener {
//            Toast.makeText(context, "Profile Success", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener {
//            Toast.makeText(context, "Profile Failure", Toast.LENGTH_SHORT).show()
        }
    }

    private fun addPatientToDatabase(name: String, email: String, uid: String) {
        mDbRef = FirebaseDatabase.getInstance().reference
        mDbRef.child("patient").child(uid).setValue(User(name, email, uid))
        val storageReference = FirebaseStorage.getInstance().getReference("users/$uid")
        storageReference.putFile(filePath).addOnSuccessListener {
//            Toast.makeText(context, "Profile Success", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener {
//            Toast.makeText(context, "Profile Failure", Toast.LENGTH_SHORT).show()
        }
    }

    private fun validateName(name: EditText): Boolean {
        if (name.text.isEmpty()) {
            name.setError("This Field Cannot Be Empty")
            return false
        } else {
            return true
        }
    }

    private fun validateEmail(email: EditText): Boolean {
        if (email.text.isEmpty()) {
            email.setError("This Field Cannot Be Empty")
            return false
        } else if (android.util.Patterns.EMAIL_ADDRESS.matcher(email.text).matches()) {
            return true
        } else {
            email.setError("Enter A Valid Email")
            return false
        }
    }

    private fun validatePassword(password1: EditText, password2: EditText): Boolean {
        if (password2.text.isEmpty()) {
            password2.setError("This Field Cannot Be Empty")
            return false
        }
        if (password1.text.equals(password2.text)) {
            password2.setError("Passwords Don't Match")
            return false
        } else {
            return true
        }

    }
}