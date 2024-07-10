package com.example.note

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser


class NotesFragment: Fragment(),NoteClickInterface,NoteClickDeleteInterface {

    private lateinit var notesRecyclerView: RecyclerView
    private lateinit var floatingActionButton: FloatingActionButton
    private lateinit var noteRVAdapter: NoteRVAdapter
    private lateinit var userViewModel:UserViewModel
    private lateinit var databaseManager: DatabaseManager
    private lateinit var list: ArrayList< Note>
    private lateinit var imageView: ImageView
    private lateinit var signOut:ImageView
    private lateinit var authViewModel: AuthViewModel






    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_notes, container, false)
        notesRecyclerView = view.findViewById(R.id.notesRecyclerView)
        floatingActionButton = view.findViewById(R.id.floatingBtn)
        imageView = view.findViewById(R.id.dummyImage)
        signOut = view.findViewById(R.id.signout)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val firebaseUser = FirebaseAuth.getInstance().currentUser

        databaseManager = DatabaseManager(requireContext())
        settingAdapter(view)

//        databaseManager.addUser(firebaseUser?.displayName!!,firebaseUser?.email!!)
        firebaseUser?.let {
            val userId = databaseManager.getUserIdByEmail(it.email!!)
            if (userId == null) {
                databaseManager.addUser(it.displayName!!, it.email!!)
            }
        }





        floatingActionButton.setOnClickListener {
            findNavController().navigate(R.id.action_noteFragment_to_EditNoteFragment)
        }

        signingOut()
    }

    private fun signingOut() {

        authViewModel = ViewModelProvider(this).get(AuthViewModel::class.java)
        authViewModel.initGoogleSignIn(requireActivity(),requireContext())
        signOut.setOnClickListener {
            authViewModel.signOut(
                onSuccess = {
                    Toast.makeText(requireContext(), "Sign out successful", Toast.LENGTH_SHORT).show()
                    findNavController().navigate(R.id.action_notesFragment_to_loginFragment)

                },
                onError = {
                    Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                }
            )

                }
    }




    private fun settingAdapter(view: View) {
        notesRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        list = ArrayList()
        if(list.isEmpty()){
            imageView.visibility = View.VISIBLE
            notesRecyclerView.visibility = View.GONE
        }
        loadNotes()
        if(list.isNotEmpty()){
            imageView.visibility = View.GONE
            notesRecyclerView.visibility = View.VISIBLE
        }
        noteRVAdapter = NoteRVAdapter(requireContext(),this,this,list)
        notesRecyclerView.adapter = noteRVAdapter

    }

    private fun loadNotes() {
        // Assuming the current user's email is known
        val firebaseUser = FirebaseAuth.getInstance().currentUser
        firebaseUser?.let {
            val userId = databaseManager.getUserIdByEmail(it.email!!)
            Log.d("NotesFragment", "User ID: $userId")

            if (userId != null) {
                val cursor = databaseManager.getAllNotesByUser(userId)
                list.clear()
                if (cursor.moveToFirst()) {
                    do {
                        val noteId = cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_NOTE_ID))
                        val title = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_NOTE_TITLE))
                        val content = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_NOTE_DESCRIPTION))
                        val timestamp = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TIMESTAMP)) // Assuming you have a timestamp column
                        list.add(
                            Note(title, content, timestamp, noteId))
                    } while (cursor.moveToNext())
                }
                cursor.close()
                Log.d("NotesFragment", "Loaded notes: $list")

//                noteRVAdapter.updateList(list)
            }
        }
    }

    override fun onNoteClick(note: Note) {
        val bundle = Bundle().apply {
            putString("noteType", "Edit")
            putString("noteTitle", note.title)
            putString("noteDescription", note.description)
            putDouble("noteId", note.noteId)
            putString("timestamp",note.timestamp)

        }
        Log.d("NotesFragment", "Navigating with bundle: $bundle")

//        list.add(Note(note.title,note.description,note.timestamp,note.noteId))
//        noteRVAdapter.updateList(list)
        findNavController().navigate(R.id.action_noteFragment_to_EditNoteFragment,bundle)

    }

    override fun onDeleteIconClick(note: Note) {
        Log.d("NotesFragment", "Attempting to delete note with ID: ${note.noteId}")
        val rowsDeleted = databaseManager.deleteNote(noteId = note.noteId)
        Log.d("NotesFragment", "Rows deleted: $rowsDeleted")
        loadNotes()
        noteRVAdapter.updateList(list)
        if(list.isEmpty()){
            imageView.visibility = View.VISIBLE
            notesRecyclerView.visibility = View.GONE
        }

    }


}