package com.example.loginsignup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.provider.ContactsContract.CommonDataKinds.Note
import android.view.LayoutInflater
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.loginsignup.databinding.ActivityMainBinding
import com.example.loginsignup.databinding.DialogUpdateNoteBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.ArrayList

class MainActivity : AppCompatActivity(),NoteAdapter.OnItemClickListener {
    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    private lateinit var databaseReference: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private lateinit var recyclerView: RecyclerView
//    private lateinit var SearchView: SearchView
//    var arrArray= mutableListOf<Note>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.createNoteButton.setOnClickListener {
            startActivity(Intent(this, AddNote::class.java))
        }
        recyclerView = binding.notesRecyclerView
        recyclerView.layoutManager = GridLayoutManager(this, 2)
        databaseReference = FirebaseDatabase.getInstance().reference
        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        currentUser?.let { user ->
            val noteReference = databaseReference.child("users").child(user.uid).child("notes")
            noteReference.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val noteList = mutableListOf<NoteItem>()
                    for (noteSnapshot in snapshot.children) {
                        val note = noteSnapshot.getValue(NoteItem::class.java)
                        note?.let {
                            noteList.add(it)

                        }
                    }
                    noteList.reverse()
//                   noteList.also { arrArray }
                    val adapter = NoteAdapter(noteList, this@MainActivity)
                    recyclerView.adapter = adapter
                }

                override fun onCancelled(error: DatabaseError) {

                }

            })
        }
//        SearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
//            override fun onQueryTextSubmit(query: String?): Boolean {
//             return true
//            }
//
//            override fun onQueryTextChange(newText: String?): Boolean {
//            var tempArray= mutableListOf<Note>()
//                for (arr in arrArray){
//                    if(arrArray.)
//                }
//                return true
//            }
//
//        })
    }

    override fun onDeleteClick(noteId: String) {
        val currentUser = auth.currentUser
        currentUser?.let { user ->
            val noteReference = databaseReference.child("users").child(user.uid).child("notes")
                noteReference.child(noteId).removeValue()
        }
    }

    override fun onUpdateClick(noteId: String, currentTitle: String, currentDescription: String) {
        val dialogBinding = DialogUpdateNoteBinding.inflate(LayoutInflater.from(this))
        val dialog= AlertDialog.Builder(this).setView(dialogBinding.root)
            .setTitle("Update Notes")
            .setPositiveButton("Update"){dialog,_ ->
                val newTitle=dialogBinding.updatenotetitle.text.toString()
                val newDescription=dialogBinding.UpdateNoteDescription.text.toString()
                updateNoteDatabase(noteId,newTitle,newDescription)
                dialog.dismiss()
            }
            .setNegativeButton("Cancel"){dialog,_ ->
                dialog.dismiss()
            }
            .create()
        dialogBinding.updatenotetitle.setText(currentTitle)
        dialogBinding.UpdateNoteDescription.setText(currentDescription)
        dialog.show()
    }

    private fun updateNoteDatabase(noteId: String, newTitle: String, newDescription: String) {

        val currentUser = auth.currentUser
        currentUser?.let { user ->
            val noteReference = databaseReference.child("users").child(user.uid).child("notes")
            val updateNote =NoteItem(noteId,newTitle,newDescription)
            noteReference.child(noteId).setValue(updateNote)
                .addOnCompleteListener{task ->
                    if (task.isSuccessful){
                        Toast.makeText(this, "Note Update Successful", Toast.LENGTH_SHORT).show()
                    }
                    else{
                        Toast.makeText(this, "Update Failed", Toast.LENGTH_SHORT).show()
                    }
                }



        }
    }
}
