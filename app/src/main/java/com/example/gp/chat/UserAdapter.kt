package com.example.gp.chat

import android.content.Context
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.example.gp.R
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.storage.FileDownloadTask
import com.google.firebase.storage.FirebaseStorage
import java.io.File
import java.lang.Exception

class UserAdapter(val context: Context, val userList: ArrayList<User>, val navController: NavController) : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {
    class UserViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val userName = itemView.findViewById<TextView>(R.id.userName)
        val userPic = itemView.findViewById<de.hdodenhof.circleimageview.CircleImageView>(R.id.userPic)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        return UserViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.user_layout, parent, false))
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val currentUser = userList[position]
        holder.userName.text = currentUser.name.toString()
        val storageReference = FirebaseStorage.getInstance().getReference("users/"+currentUser.uid.toString())
        try {
            val localFile = File.createTempFile("tempfile",".jpg")
            storageReference.getFile(localFile).addOnSuccessListener(object : OnSuccessListener<FileDownloadTask.TaskSnapshot>{
                override fun onSuccess(p0: FileDownloadTask.TaskSnapshot?) {
                    val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)
                    holder.userPic.setImageBitmap(bitmap)
                }

            })
        } catch (e: Exception){
            e.printStackTrace()
        }

        holder.itemView.setOnClickListener {
            navController.navigate(ChatFragmentDirections.actionChatFragmentToMessagesFragment(currentUser.name.toString(), currentUser.uid.toString()))
        }
    }

    override fun getItemCount(): Int {
        return userList.size
    }
}