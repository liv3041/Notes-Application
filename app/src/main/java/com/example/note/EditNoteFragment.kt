package com.example.note

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import java.text.SimpleDateFormat
import java.util.Date


class EditNoteFragment : Fragment() {
    private lateinit var noteTitleEt:EditText
    private lateinit var noteDescriptionEt:EditText
    private lateinit var saveButton:Button
    private lateinit var userViewModel: UserViewModel
    private lateinit var dbManager: DatabaseManager
    var noteId = -1.0


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_edit_note, container, false)
        noteTitleEt = view.findViewById(R.id.editNoteTitle)
        noteDescriptionEt = view.findViewById(R.id.editNoteDescription)
        saveButton = view.findViewById(R.id.updateButton)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val args = arguments
        Log.d("EditNoteFragment", "Bundle arguments: ${args?.toString()}")

        val noteType = args?.getString("noteType")
        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)

        dbManager = DatabaseManager(requireContext())

        if(noteType == "Edit"){
            Log.d("EditNoteFragment","$noteType")
            val noteTitle = args?.getString("noteTitle")
            val noteDescription = args?.getString("noteDescription")
            noteId = args?.getDouble("noteId")?:-1.0
            saveButton.setText("Update Note")
            noteTitleEt.setText(noteTitle)
            noteDescriptionEt.setText(noteDescription)

        }else{
            saveButton.setText("Save Note")
        }


        saveButton.setOnClickListener {

            val noteTitle = noteTitleEt.text.toString().trim()
            val noteDescription = noteDescriptionEt.text.toString().trim()

//            if(noteType.equals("Edit")){
//                if(noteTitle.isNotEmpty() && noteDescription.isNotEmpty()){
//                    val sdf = SimpleDateFormat("dd MMM, yyyy - HH:mm")
//                    val currentDate:String = sdf.format(Date())
//                    dbManager.updateNote(noteId,noteTitle,noteDescription,currentDate)
//
//
//                }
//            }else{
//                if(noteTitle.isNotEmpty()&& noteDescription.isNotEmpty()){
//                    val sdf = SimpleDateFormat("dd MMM, yyyy - HH:mm")
//                    val currentDate:String = sdf.format(Date())
//                    val userId = dbManager.getUserIdByEmail(FirebaseAuth.getInstance().currentUser?.email!!)
//
//
//                    dbManager.addNote(noteTitle,noteDescription,userId!!,currentDate)
//
//
//                }
//                findNavController().navigate(R.id.action_editNoteFragment_to_notesFragment)
//            }
            if (noteTitle.isNotEmpty() && noteDescription.isNotEmpty()) {
                val sdf = SimpleDateFormat("dd MMM, yyyy - HH:mm")
                val currentDate: String = sdf.format(Date())
                if (noteType == "Edit") {
                    dbManager.updateNote(noteId, noteTitle, noteDescription, currentDate)
                    findNavController().navigate(R.id.action_editNoteFragment_to_notesFragment)
                } else {
                    val userId = FirebaseAuth.getInstance().currentUser?.email?.let { dbManager.getUserIdByEmail(it) }
                    if (userId != null) {
                        dbManager.addNote(noteTitle, noteDescription, userId, currentDate)
                        findNavController().navigate(R.id.action_editNoteFragment_to_notesFragment)
                    } else {
                        Log.e("EditNoteFragment", "User ID is null")
                    }
                }
            } else {
                Toast.makeText(requireContext(), "Please fill out all fields", Toast.LENGTH_SHORT).show()
            }
        }
        }


    }


