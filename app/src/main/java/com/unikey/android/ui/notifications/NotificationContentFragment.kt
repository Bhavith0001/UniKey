package com.unikey.android.ui.notifications

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.google.android.material.transition.MaterialSharedAxis
import com.unikey.android.databinding.FragmentNotificationContentBinding
import com.unikey.android.setTargetSharedAxisAnim
import com.unikey.android.setUp

class NotificationContentFragment : Fragment() {

    private lateinit var binding: FragmentNotificationContentBinding

    private val args : NotificationContentFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTargetSharedAxisAnim(MaterialSharedAxis.Z)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentNotificationContentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.run {
            appBar.toolBar.setUp(this@NotificationContentFragment, "Notification")
        }

        binding.contentTitle.text = args.notificationData.title
        binding.notifyContent.text = args.notificationData.content

    }
}