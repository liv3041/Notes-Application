package com.example.note

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatButton
import androidx.credentials.CredentialManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.fragment.findNavController

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser


class LoginFragment : Fragment() {


    private lateinit var activityContext: Context

    private lateinit var googleLoginButton: AppCompatButton

    private lateinit var  authViewModel: AuthViewModel
    private lateinit var auth: FirebaseAuth



    override fun onAttach(context: Context) {
        super.onAttach(context)
        activityContext = context


    }

    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        val view  = inflater.inflate(R.layout.fragment_login, container, false)

        googleLoginButton = view.findViewById(R.id.btn_google_login)
        


        return view


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        authViewModel = ViewModelProvider(this).get(AuthViewModel::class.java)

        authViewModel.initGoogleSignIn(requireActivity(),activityContext)
        googleLoginButton.setOnClickListener {
            authViewModel.signInWithGoogle(requireActivity())

//            authViewModel.navigateToNotes.observe(viewLifecycleOwner){
//                if(it){
//
//                }
//            }

        }
        registerForActivityResult()






    }

    private fun registerForActivityResult() {
        val signInResultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            authViewModel.handleSignInResult(result.data,
                { // On Success
                    Toast.makeText(requireContext(), "Sign in successful", Toast.LENGTH_SHORT).show()
                    // Navigate to next screen or handle success flow
                    findNavController().navigate(R.id.action_loginFragment_to_NotesFragment)


                },
                { error ->
                    Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
                    // Handle error case
                })
        }

//        // Call the launcher for sign-in when button is clicked
        googleLoginButton.setOnClickListener {
            val signInIntent = authViewModel.googleSignInClient.signInIntent
            signInResultLauncher.launch(signInIntent)
        }


    }






}




