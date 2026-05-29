package com.am.lapcart.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.am.lapcart.activities.FirebaseLoginActivity
import com.am.lapcart.R
import com.am.lapcart.activities.InfoActivity
import com.google.android.material.bottomsheet.BottomSheetDialog

class ProfileFragment : Fragment() {
    private lateinit var imgProfile: ImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        val aboutTV = view.findViewById<LinearLayout>(R.id.aboutTV)
        val privacyTV = view.findViewById<LinearLayout>(R.id.privacyTV)
        val termTV = view.findViewById<LinearLayout>(R.id.termTV)
        val helpTV = view.findViewById<LinearLayout>(R.id.helpTV)

        val logoutTV = view.findViewById<LinearLayout>(R.id.logoutTV)
        val accountDeleteTV = view.findViewById<LinearLayout>(R.id.accountDeleteTV)


        val btnEditProfile = view.findViewById<ImageView>(R.id.btnEditProfile)

        imgProfile = view.findViewById(R.id.imgProfile)

        btnEditProfile.setOnClickListener {
            pickImageLauncher.launch("image/*")
        }

        logoutTV.setOnClickListener {
            showBottomSheet("logout")
        }
        accountDeleteTV.setOnClickListener {
            showBottomSheet("delete")
        }


        aboutTV.setOnClickListener {
            val intent = Intent(requireContext(), InfoActivity::class.java)
            intent.putExtra("page", "about")
            startActivity(intent)
        }
        privacyTV.setOnClickListener {
            val intent = Intent(requireContext(), InfoActivity::class.java)
            intent.putExtra("page", "privacy")
            startActivity(intent)
        }
        termTV.setOnClickListener {
            val intent = Intent(requireContext(), InfoActivity::class.java)
            intent.putExtra("page", "terms")
            startActivity(intent)
        }
        helpTV.setOnClickListener {
            val intent = Intent(requireContext(), InfoActivity::class.java)
            intent.putExtra("page", "help")
            startActivity(intent)


        }
        return view
    }


    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity).supportActionBar?.hide()

    }

    override fun onStop() {
        super.onStop()
        (activity as AppCompatActivity).supportActionBar?.show()

    }

    private fun showBottomSheet(type: String) {
         val dialog = BottomSheetDialog(requireContext())
        val view = layoutInflater.inflate(R.layout.bottom_sheet, null)

        val title = view.findViewById<TextView>(R.id.title)
        val actionBtn = view.findViewById<Button>(R.id.actionBtn)
        val confirmMessage = view.findViewById<TextView>(R.id.confirmation)



        if (type == "logout") {
            title.text = "Logout"
            confirmMessage.text = "Are you sure you want to logout?"
            actionBtn.text = "Confirm Logout"

            } else {
                title.text = "Delete Account"
                confirmMessage.text ="Once You Delete Your account you can not get recover again , your all data will be deleted"
                actionBtn.text = "Confirm"

        }
            actionBtn.setOnClickListener {
                if (type == "logout") {

                    val pref = requireActivity().getSharedPreferences(
                        "user",
                        Context.MODE_PRIVATE
                    )
                    val editor = pref.edit()
                    editor.clear()
                    editor.apply()

                    val intent = Intent(requireContext(), FirebaseLoginActivity::class.java)
                    startActivity(intent)
                    requireActivity().finish()
                }
                else {
                    AlertDialog.Builder(requireContext())
                        .setTitle("Delete Account")
                        .setMessage("Are you sure you want to delete your account?")
                        .setPositiveButton("Yes") { _, _ ->
                            Toast.makeText(
                                requireContext(),
                                "Account deleted successfully",
                                Toast.LENGTH_SHORT
                            ).show()
                            dialog.dismiss()

                            val pref = requireActivity().getSharedPreferences(
                                "user",
                                Context.MODE_PRIVATE
                            )
                            val editor = pref.edit()
                            editor.clear()
                            editor.apply()

                            val intent = Intent(requireContext(), FirebaseLoginActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(intent)
                        }
                        .setNegativeButton("Cancel", null)
                        .show()
                }
                }
                dialog.setContentView(view)
                dialog.show()
        }
    private val pickImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            imgProfile.setImageURI(it)
        }

    }
}