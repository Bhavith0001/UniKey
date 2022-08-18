package com.unikey.android.ui.homescreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.android.material.transition.MaterialElevationScale
import com.google.android.material.transition.MaterialSharedAxis
import com.google.android.material.transition.MaterialSharedAxis.X
import com.google.android.material.transition.MaterialSharedAxis.Z
import com.unikey.android.ANIM_DURATION
import com.unikey.android.R
import com.unikey.android.databinding.FragmentHomeScreenBinding
import com.unikey.android.objects.User
import com.unikey.android.setHostSharedAxisAnim
import com.unikey.android.setTargetSharedAxisAnim
import com.unikey.android.ui.profile.ProfileFragment

class HomeScreenFragment : Fragment() {

    private lateinit var binding: FragmentHomeScreenBinding

    //Instance of HomeScreen ViewModel
    private val viewModel: HomeScreenViewModel by viewModels()
    private lateinit var user: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTargetSharedAxisAnim(Z)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.user?.observe(viewLifecycleOwner){
            if (it != null) {
                viewModel.getAttendanceAndFees()
                user = it
                binding.run {
                    userName.text = it.name
                    clsInfo.text = it.cls
                    academicYear.text = it.academicYear
                }
                if (it.isStudent == true) {
                    binding.homeContent.feesCardView.visibility = View.VISIBLE
                    binding.homeContent.attendanceCardView.visibility = View.VISIBLE
                } else {
                    binding.homeContent.feesCardView.visibility = View.GONE
                    binding.homeContent.attendanceCardView.visibility = View.GONE
                }

                setProfilePic(user)
            }
        }

        viewModel.averageAttendance.observe(viewLifecycleOwner){
            binding.homeContent.attendancePercentTxtView.text = "$it%"
        }

        viewModel.pendingFees.observe(viewLifecycleOwner){
            binding.homeContent.feesAmountTxtView.text = "₹"+(it.total?.minus(it.paid!!)).toString()
        }

        //navigate to profile_fragment
        binding.profileIcon.setOnClickListener {
            openProfile()
        }

        binding.notIcon.notificationIcon.setOnClickListener {
            openNotifications()
        }

        binding.homeContent.run{
            attendanceCardView.setOnClickListener {
                val attendanceDetailTransitionName = getString(R.string.attendance_detail_transition_name)
                val extras =
                    FragmentNavigatorExtras(binding.homeContent.attendanceCardView to attendanceDetailTransitionName)
                findNavController().navigate(
                    HomeScreenFragmentDirections.actionHomeScreenFragmentToAttendanceFragment(),
                    extras
                )
                setScaleAnim()
            }

            feesCardView.setOnClickListener {
                val feesDetailTransitionName = getString(R.string.fees_detail_transition_name)
                val extras =
                    FragmentNavigatorExtras(binding.homeContent.feesCardView to feesDetailTransitionName)
                findNavController().navigate(
                    HomeScreenFragmentDirections.actionHomeScreenFragmentToFeesFragment(),
                    extras
                )
                setScaleAnim()
//                findNavController().navigate(HomeScreenFragmentDirections.actionHomeScreenFragmentToFeesFragment())
            }

            calender.setOnClickListener {
                setHostSharedAxisAnim(X)
                findNavController().navigate(HomeScreenFragmentDirections.actionHomeScreenFragmentToCalenderFragment())
            }

            materials.setOnClickListener {
                if (user.isStudent == true) {
                    setHostSharedAxisAnim(X)
                    findNavController().navigate(
                        HomeScreenFragmentDirections
                            .actionHomeScreenFragmentToSubjectsFragment()
                    )
                }
                else {
                    setHostSharedAxisAnim(X)
                    findNavController().navigate(
                        HomeScreenFragmentDirections
                            .actionHomeScreenFragmentToAddStudyMaterialsFragment()
                    )
                }
            }

            gallery.setOnClickListener {
                setHostSharedAxisAnim(X)
                findNavController().navigate(HomeScreenFragmentDirections.actionHomeScreenFragmentToGalleryFragment())
            }

            courses.setOnClickListener {
                setHostSharedAxisAnim(X)
                findNavController().navigate(HomeScreenFragmentDirections.actionHomeScreenFragmentToCoursesFragment())
            }

            jobAlerts.setOnClickListener {
                setHostSharedAxisAnim(X)
                findNavController().navigate(HomeScreenFragmentDirections.actionHomeScreenFragmentToJobsAletrsFragment())
            }

            about.setOnClickListener {
                setHostSharedAxisAnim(X)
                findNavController().navigate(HomeScreenFragmentDirections.actionHomeScreenFragmentToAboutFragment())
            }

//            timeTable.setOnClickListener {
//                findNavController().navigate(HomeScreenFragmentDirections.actionHomeScreenFragmentToTimeTableFragment())
//            }
//
//            results.setOnClickListener {
//                findNavController().navigate(HomeScreenFragmentDirections.actionHomeScreenFragmentToResultFragment())
//            }

        }

    }

    private fun setScaleAnim(){
        exitTransition = MaterialElevationScale(false).apply {
            duration = ANIM_DURATION
        }

        reenterTransition = MaterialElevationScale(true).apply {
            duration = ANIM_DURATION
        }
    }

    private fun setProfilePic(user: User?) {
        Glide.with(this)
            .load(user?.profileUrl)
            .placeholder(R.drawable.ic_account_24)
            .into(binding.profileIcon)
    }

    private fun openNotifications() {
        setHostSharedAxisAnim(Z)
        findNavController().navigate(HomeScreenFragmentDirections.actionHomeScreenFragmentToNotificationsFragment())
    }

//    private fun setSharedAxisAnim(axis: Int) {
//        exitTransition = MaterialSharedAxis(axis, true).apply {
//            duration = ANIM_DURATION
//        }
//
//        reenterTransition = MaterialSharedAxis(axis, false).apply {
//            duration = ANIM_DURATION
//        }
//    }

    override fun onStart() {
        super.onStart()
        if (!viewModel.isAuthenticated())
            findNavController().navigate(HomeScreenFragmentDirections.actionGlobalLoginScreenFragment())
    }

    /** Navigates to [ProfileFragment]*/
    private fun openProfile(){
        setHostSharedAxisAnim(X)
        findNavController().navigate(HomeScreenFragmentDirections.actionHomeScreenFragmentToProfileFragment())
    }

}