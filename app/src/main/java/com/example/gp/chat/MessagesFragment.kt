package com.example.gp.chat

import android.app.Person
import android.content.Context
import android.content.pm.ShortcutInfo
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView

import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import at.favre.lib.crypto.bcrypt.BCrypt
import com.example.gp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.io.UnsupportedEncodingException
import java.nio.charset.Charset
import java.security.InvalidKeyException
import java.security.NoSuchAlgorithmException
import javax.crypto.BadPaddingException
import javax.crypto.Cipher
import javax.crypto.IllegalBlockSizeException
import javax.crypto.NoSuchPaddingException
import javax.crypto.spec.SecretKeySpec

class MessagesFragment : Fragment() {
    lateinit var chatRecyclerView: RecyclerView
    lateinit var messageBox: EditText
    lateinit var sendButton: ImageView
    lateinit var messageAdapter: MessageAdapter
    lateinit var messageList: ArrayList<Message>
    lateinit var mDbRef : DatabaseReference
    val args by navArgs<MessagesFragmentArgs>()
    var receiverRoom: String? = null
    var senderRoom: String? = null
    lateinit var cipher: Cipher
    lateinit var decipher: Cipher
    val encryptionKey = byteArrayOf(9, 115, 51, 86, 105, 4, -31, -23, -68, 88, 17, 20, 3, -105, 119, -53)
    lateinit var secretKeySpec: SecretKeySpec
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_messages, container, false)
        val receiverUid = args.uid
        val senderUid = FirebaseAuth.getInstance().currentUser?.uid
        senderRoom = receiverUid+senderUid
        receiverRoom = senderUid+receiverUid
        mDbRef = FirebaseDatabase.getInstance().reference
        try {
            cipher = Cipher.getInstance("AES")
            decipher = Cipher.getInstance("AES")
        } catch (e: NoSuchAlgorithmException){
            e.printStackTrace()
        } catch (e: NoSuchPaddingException){
            e.printStackTrace()
        }
        secretKeySpec = SecretKeySpec(encryptionKey, "AES")
        chatRecyclerView = view.findViewById(R.id.chatRecyclerView)
        messageBox = view.findViewById(R.id.messageBox)
        sendButton = view.findViewById(R.id.sendButton)
        (activity as AppCompatActivity?)!!.supportActionBar?.setTitle(args.name)
        messageList = ArrayList()
        messageAdapter = MessageAdapter(requireContext(), messageList)
        chatRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        chatRecyclerView.adapter = messageAdapter
        mDbRef.child("chats").child(senderRoom!!).child("messages").addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                messageList.clear()
                for (postSnapshot in snapshot.children){
                    val message = postSnapshot.getValue(Message::class.java)
                    message?.message = message?.message?.let { AESDecryptionMethod(it) }
                    messageList.add(message!!)
                }
                messageAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
        sendButton.setOnClickListener {
            val message = messageBox.text.toString()
//            val encryptedMessage = BCrypt.withDefaults().hashToString(12,message.toCharArray())
//            Log.v("encryption",encryptedMessage)
//            Log.v("encrypt", AESEncryptionMethod(message).toString())
            val messageObject = Message(AESEncryptionMethod(message), senderUid)
            mDbRef.child("chats").child(senderRoom!!).child("messages").push().setValue(messageObject).addOnSuccessListener {
                mDbRef.child("chats").child(receiverRoom!!).child("messages").push().setValue(messageObject)
            }
            messageBox.setText("")
        }
        return view
    }

    private fun AESEncryptionMethod(string: String): String? {
        val stringByte = string.toByteArray()
        var encryptedByte: ByteArray? = ByteArray(stringByte.size)
        try {
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec)
            encryptedByte = cipher.doFinal(stringByte)
        } catch (e: InvalidKeyException) {
            e.printStackTrace()
        } catch (e: BadPaddingException) {
            e.printStackTrace()
        } catch (e: IllegalBlockSizeException) {
            e.printStackTrace()
        }
        var returnString: String? = null
        try {
            returnString = String(encryptedByte!!, charset("ISO-8859-1"))
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        }
        return returnString
    }

    private fun AESDecryptionMethod(string: String): String?{
        val encryptedByte: ByteArray? = string.toByteArray(charset("ISO-8859-1"))
        var decryptedString : String = ""
        var decryption : ByteArray
        try {
            decipher.init(Cipher.DECRYPT_MODE, secretKeySpec)
            decryption = decipher.doFinal(encryptedByte)
            decryptedString = String(decryption)
        } catch (e: InvalidKeyException){
            e.printStackTrace()
        } catch (e: BadPaddingException){
            e.printStackTrace()
        } catch (e: IllegalBlockSizeException){
            e.printStackTrace()
        }
        return decryptedString
    }
}
