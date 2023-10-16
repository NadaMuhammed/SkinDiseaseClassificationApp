package com.example.gp.settings

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.gp.R
import com.example.gp.chat.User
import com.example.gp.login.LoginViewModel
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FileDownloadTask
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import de.hdodenhof.circleimageview.CircleImageView
import java.io.File
import java.io.InputStream
import java.lang.Exception

class EditProfileFragment : Fragment() {
    lateinit var mainViewModel: LoginViewModel
    lateinit var mAuth: FirebaseAuth
    lateinit var bitmap: Bitmap
    lateinit var image: ImageView
    lateinit var filePath: Uri
    lateinit var uid: String
    lateinit var mDbRef: DatabaseReference
    lateinit var name: String
    lateinit var email: String
    var databaseId: Int = 0
     override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_edit_profile, container, false)
        uid = activity?.getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE)
            ?.getString("uid", "").toString()
        mainViewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        mAuth = FirebaseAuth.getInstance()
        image = view.findViewById(R.id.profilePic)
        mainViewModel.userProfile(
            "bearer " + activity?.getSharedPreferences(
                "PREFERENCE_NAME",
                Context.MODE_PRIVATE
            )?.getString("access_token", "").toString()
        )
        mainViewModel.profileResponse.observe(viewLifecycleOwner, Observer {
            name = it.body()!!.username
            email = it.body()!!.email
            databaseId = it.body()!!.id
            val storageReference = FirebaseStorage.getInstance().getReference(
                "users/$uid"
            )
            try {
                val localFile = File.createTempFile("tempfile", ".jpg")
                storageReference.getFile(localFile).addOnSuccessListener(object :
                    OnSuccessListener<FileDownloadTask.TaskSnapshot> {
                    override fun onSuccess(p0: FileDownloadTask.TaskSnapshot?) {
                        val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)
                        view.findViewById<CircleImageView>(R.id.profilePic).setImageBitmap(bitmap)
                    }

                })
            } catch (e: Exception) {
                e.printStackTrace()
            }
        })

//        if(filePath != null){
//            view.findViewById<androidx.appcompat.widget.AppCompatButton>(R.id.saveBtn).isEnabled = true
//        }

        view.findViewById<androidx.appcompat.widget.AppCompatButton>(R.id.saveBtn)
            .setOnClickListener {
                val newUsername =
                    view.findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.newUsername).text.toString()
                if (::filePath.isInitialized) {
                    val storageReference = FirebaseStorage.getInstance().getReference("users/$uid")
                    storageReference.putFile(filePath).addOnSuccessListener {
//            Toast.makeText(context, "Profile Success", Toast.LENGTH_SHORT).show()
                    }.addOnFailureListener {
//            Toast.makeText(context, "Profile Failure", Toast.LENGTH_SHORT).show()
                    }
                }
                if (newUsername!=null){
                    mDbRef = FirebaseDatabase.getInstance().reference
                    mDbRef.child("patient").child(uid).setValue(User(newUsername, email, uid))
                    mainViewModel.updateProfile("bearer " + activity?.getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE)?.getString("access_token", "").toString(), UpdateRequest(databaseId, newUsername))
                }
                Toast.makeText(context, "Profile Edited!", Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.action_editProfileFragment_to_settingsFragment)
            }

        view.findViewById<com.google.android.material.floatingactionbutton.FloatingActionButton>(R.id.floatingActionButton)
            .setOnClickListener {
                Dexter.withActivity(requireActivity())
                    .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    .withListener(object : PermissionListener {
                        override fun onPermissionGranted(p0: PermissionGrantedResponse?) {
                            val intent = Intent(Intent.ACTION_PICK)
                            intent.setType("image/*")
                            startActivityForResult(
                                Intent.createChooser(
                                    intent,
                                    "Select Image File"
                                ), 1
                            )
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
}