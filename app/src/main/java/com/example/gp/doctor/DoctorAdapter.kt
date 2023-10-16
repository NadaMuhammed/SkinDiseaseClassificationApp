package com.example.gp.doctor

import android.content.Context
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.example.gp.R
import com.example.gp.chat.User
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.storage.FileDownloadTask
import com.google.firebase.storage.FirebaseStorage
import java.io.File
import java.lang.Exception

class DoctorAdapter (val context: Context, val userList: ArrayList<User>, val navController: NavController) : RecyclerView.Adapter<DoctorAdapter.DoctorViewHolder>() {
    class DoctorViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val userName = itemView.findViewById<TextView>(R.id.userName)
        val userPic = itemView.findViewById<de.hdodenhof.circleimageview.CircleImageView>(R.id.userPic)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DoctorViewHolder {
        return DoctorViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.user_layout2, parent, false))
    }

    override fun onBindViewHolder(holder: DoctorViewHolder, position: Int) {
        val currentUser = userList[position]
        holder.userName.text = currentUser.name.toString()
        val storageReference = FirebaseStorage.getInstance().getReference("users/"+currentUser.uid.toString())
        try {
            val localFile = File.createTempFile("tempfile",".jpg")
            storageReference.getFile(localFile).addOnSuccessListener(object :
                OnSuccessListener<FileDownloadTask.TaskSnapshot> {
                override fun onSuccess(p0: FileDownloadTask.TaskSnapshot?) {
                    val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)
                    holder.userPic.setImageBitmap(bitmap)
                }

            })
        } catch (e: Exception){
            e.printStackTrace()
        }

        holder.itemView.setOnClickListener {
            navController.navigate(DrChatFragmentDirections.actionDrChatFragmentToMessagesFragment2(currentUser.name.toString(), currentUser.uid.toString()))
        }
    }

    override fun getItemCount(): Int {
        return userList.size
    }
}