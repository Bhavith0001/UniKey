package com.unikey.android.ui.profile

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.android.material.transition.MaterialSharedAxis
import com.google.android.material.transition.MaterialSharedAxis.X
import com.unikey.android.*
import com.unikey.android.databinding.FragmentProfileBinding
import com.unikey.android.databinding.ProfileBodyContentBinding
import com.unikey.android.objects.User

class ProfileFragment : Fragment() {
    private val REQUEST_CODE = 99

    private lateinit var binding: FragmentProfileBinding
    private  val viewModel: ProfileViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTargetSharedAxisAnim(X)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.user?.observe(viewLifecycleOwner){ user ->
            binding.run {
                userName.text = user.name
                regNo.text = "Reg.No: " + user.regNo

                Glide.with(this@ProfileFragment)
                    .load(user.profileUrl)
                    .placeholder(R.drawable.ic_account_24)
                    .into(profilePic)

                profileBody.run {
                    email.text = user.email
                    phoneNo.text = user.phoneNumber.toString()
                    dOB.text = user.dateOfBirth
                    academicYear.text = user.academicYear
                    fatherName.text = user.fatherName
                    motherName.text = user.motherName
                    motherName.text = user.motherName
                    address.text = user.address

                    if (user.isStudent == true){
                        setStudentMode(this, user)
                    } else{
                        setFacultyMode(this, user)
                    }
                }
            }
        }

        binding.uploadImg.setOnClickListener {

                val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
                    type = "image/jpeg"
                }
                startActivityForResult(Intent.createChooser(intent,"Select image"), REQUEST_CODE)
        }


        binding.btnSignOut.setOnClickListener {
            setHostSharedAxisAnim(X)
            val sharedPref = requireActivity().getUnikeySharedPref()
            with(sharedPref.edit()){
                putLong(REG_NO, DefaultValue)
            }
            viewModel.signOutUser()
            findNavController().navigate(ProfileFragmentDirections.actionGlobalLoginScreenFragment())
        }

        binding.run {
            appBar.toolBar.setUp(this@ProfileFragment, "Profile")
        }
    }

    private fun setFacultyMode(ref: ProfileBodyContentBinding, user: User) {
        ref.clsLabel.text = "Department"
        ref.cls.text = user.cls
        ref.semLabel.visibility = View.GONE
        ref.sem.visibility = View.GONE
        ref.dateOfAdmission.text = user.dateOfAdmission
        ref.dOALabel.text = "Date of Join"
    }

    private fun setStudentMode(ref: ProfileBodyContentBinding, user: User) {
        ref.cls.text = user.cls
        ref.sem.text = user.sem.toString()
        ref.dateOfAdmission.text = user.dateOfAdmission
    }


    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK
            && data != null && data.data != null){
            viewModel.uploadImage(data.data!!)
        }
    }

}