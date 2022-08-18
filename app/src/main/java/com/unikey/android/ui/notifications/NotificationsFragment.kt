package com.unikey.android.ui.notifications

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.transition.MaterialSharedAxis
import com.unikey.android.*
import com.unikey.android.databinding.FragmentNotificationsBinding

class NotificationsFragment : Fragment() {

    private lateinit var binding: FragmentNotificationsBinding
    private val viewModel : NotificationViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enterTransition = MaterialSharedAxis(MaterialSharedAxis.Z, true).apply {
            duration = ANIM_DURATION
        }

        returnTransition = MaterialSharedAxis(MaterialSharedAxis.Z, false).apply {
            duration = ANIM_DURATION
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.addNotificationFab.visibility = viewModel.fabEnableOrDisable()

        binding.run {
            appBar.toolBar.setUp(this@NotificationsFragment, "Notifications")
        }

        val notifyAdapter = NotificationViewAdapter(this)

        binding.notificationRecyclerView.adapter = notifyAdapter

        viewModel.notificationList.observe(viewLifecycleOwner){
            notifyAdapter.submitList(it)
        }

        binding.addNotificationFab.setOnClickListener {
            setHostSharedAxisAnim(MaterialSharedAxis.Z)
            findNavController().navigate(NotificationsFragmentDirections.actionNotificationsFragmentToAddNotificationFragment())
        }

    }

}