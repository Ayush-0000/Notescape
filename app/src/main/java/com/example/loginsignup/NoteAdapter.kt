package com.example.loginsignup

import android.icu.text.CaseMap.Title
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import androidx.recyclerview.widget.RecyclerView
import com.example.loginsignup.databinding.NotesItemBinding

class NoteAdapter(private val notes: List<NoteItem>,private val itemClickListener:OnItemClickListener ):
    RecyclerView.Adapter<NoteAdapter.NoteViewHolder> (){
interface OnItemClickListener{
    fun onDeleteClick(noteId:String)
    fun onUpdateClick(noteId:String,title: String,description: String)
}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
     val binding= NotesItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
     return NoteViewHolder(binding)
    }



    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
      val note= notes[position]
        holder.bind(note)
        holder.binding.UpdateButton.setOnClickListener {
           itemClickListener.onUpdateClick(note.noteId,note.title,note.description)
           }
        holder.binding.DeleteButton23.setOnClickListener {
           itemClickListener.onDeleteClick(note.noteId)
           }
    }
    override fun getItemCount(): Int {
return notes.size
    }
    class NoteViewHolder(val binding: NotesItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(note: NoteItem) {
           binding.titleTextview.text= note.title
           binding.descriptionTextview.text=note.description
        }

    }
}