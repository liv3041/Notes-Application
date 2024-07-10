package com.example.note

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class NoteRVAdapter(
    private val context: Context,
    val noteClickInterface:NoteClickInterface,
    val noteClickDeleteInterface:NoteClickDeleteInterface,
    val allNote: ArrayList<Note>

):RecyclerView.Adapter<NoteRVAdapter.ViewHolder> (){
//    private val allNote:ArrayList<Note> = ArrayList()
   inner class ViewHolder(itemView:View):RecyclerView.ViewHolder(itemView) {
       val noteTitle = itemView.findViewById<TextView>(R.id.notesRVTitle)
       val timestamp = itemView.findViewById<TextView>(R.id.timeStamp)
       val deleteButton = itemView.findViewById<ImageView>(R.id.deletebtn)

       fun bind(note: Note){
           noteTitle.text = note.title
           timestamp.text = note.timestamp

       }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.notes_recyclerview_item,parent,false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return allNote.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val singleNote = allNote[position]
        holder.bind(singleNote)

        holder.deleteButton.setOnClickListener {
            noteClickDeleteInterface.onDeleteIconClick(singleNote)
        }

        holder.itemView.setOnClickListener{
            noteClickInterface.onNoteClick(singleNote)
        }
    }

    fun updateList(newList: List<Note>) {
        allNote.clear()
        allNote.addAll(newList)
        notifyDataSetChanged()
    }

}