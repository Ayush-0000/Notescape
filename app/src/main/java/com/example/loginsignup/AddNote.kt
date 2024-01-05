package com.example.loginsignup

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.loginsignup.databinding.ActivityAddNoteBinding
import com.example.loginsignup.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.lang.ref.PhantomReference

class AddNote : AppCompatActivity() {
    private val binding: ActivityAddNoteBinding by lazy {
        ActivityAddNoteBinding.inflate(layoutInflater)
    }
    private lateinit var databaseReference: DatabaseReference
    private lateinit var auth:FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        databaseReference = FirebaseDatabase.getInstance().reference
        auth = FirebaseAuth.getInstance()

        binding.SaveNoteButton.setOnClickListener {
            val title = binding.etTitle.text.toString()
            val description = binding.etDescription.text.toString()
            if (title.isEmpty() && description.isEmpty())
                Toast.makeText(this, "Fill the Title and Description", Toast.LENGTH_SHORT).show()
            else
            {
                val currentUser = auth.currentUser
                currentUser?.let { user ->
                    val noteKey =databaseReference.child("users").child(user.uid).child("notes").push().key
                    val noteItem = NoteItem(noteKey?:"",title,description,)
                    if (noteKey!=null)
                    {
                        databaseReference.child("users").child(user.uid).child("notes").child(noteKey).setValue(noteItem)
                            .addOnCompleteListener {
                                task ->
                                if (task.isSuccessful)
                                {
                                    Toast.makeText(this, "NoteSave Successful", Toast.LENGTH_SHORT).show()
                                    finish()
                                }
                                else
                                {
                                    Toast.makeText(this, " Failed to Save Note", Toast.LENGTH_SHORT).show()
                                }
                            }
                    }
                }
            }
        }
    }
}