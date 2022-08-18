package com.unikey.android.ui.notifications

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.chip.Chip
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.transition.MaterialSharedAxis
import com.unikey.android.TAG
import com.unikey.android.databinding.FragmentAddNotificationBinding
import com.unikey.android.setTargetSharedAxisAnim

class AddNotificationFragment : Fragment() {

    private val viewModel : NotificationViewModel by viewModels()

    private lateinit var binding : FragmentAddNotificationBinding
    private var categoryList: Array<String> = arrayOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTargetSharedAxisAnim(MaterialSharedAxis.Z)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAddNotificationBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getClsNamesList()
        viewModel.categoryList.observe(viewLifecycleOwner){
            categoryList = it.toTypedArray()
        }
        setRecipients()     //sets recipients chips in chip group
        with(binding){
            addRecipient.setOnClickListener {
                showRecipientDialog()
            }

            closeBtn.setOnClickListener {
                findNavController().navigateUp()
            }

            sendNotificationBtn.setOnClickListener {
                senNotification()
            }
        }
//        binding.addRecipient.setOnClickListener {
//            showRecipientDialog()
//        }


//        binding.closeBtn.setOnClickListener {
//            findNavController().navigateUp()
//        }



        binding.run {
            enableOrDisableSendButton()

            nTitleTxt.doOnTextChanged { _, _, _, _ ->
                enableOrDisableSendButton()
            }
            nDescription.doOnTextChanged { _, _, _, _ ->
                enableOrDisableSendButton()
            }
            nContentTxt.doOnTextChanged { _, _, _, _ ->
                enableOrDisableSendButton()
            }

        }

    }

    private fun enableOrDisableSendButton(){
        if (binding.inputFieldsNotEmpty()) {
            binding.sendNotificationBtn.apply {
                isEnabled = true
                alpha = 1.0f
            }
        } else {
            binding.sendNotificationBtn.apply {
                isEnabled = false
                alpha = 0.2f
            }
        }
    }

    private fun FragmentAddNotificationBinding.inputFieldsNotEmpty(): Boolean {
        return (nTitleTxt.text.isNotBlank()
                && nDescription.text.isNotBlank()
                && nContentTxt.text.isNotBlank()
                && viewModel.btnDoneEnabled.value == true)
    }
    /** user this function to create a new chip close chip function enabled  */
    private fun createChip(chipText: String, index: Int): Chip{

        val chip = Chip(requireContext())
        chip.isCloseIconVisible = true
        chip.text = chipText
        chip.setOnCloseIconClickListener {
            viewModel.selectedList.remove(index)
            viewModel.checkedList[index] = false
            binding.recipientChipGrp.removeView(it)
            viewModel.setBtnDoneEnabled()
            enableOrDisableSendButton()
        }
        return chip
    }


    private fun showRecipientDialog(){
        for (i in categoryList) {
            viewModel.checkedList.add(false)
        }


        val builder = MaterialAlertDialogBuilder(requireContext()).apply {
            setTitle("Choose recipients")
            setMultiChoiceItems(categoryList, viewModel.checkedList.toBooleanArray()) { _, which, checked ->
                viewModel.checkedList[which] = checked

                if (checked)
                    viewModel.selectedList.add(which)
                else
                    viewModel.selectedList.remove(which)

                Log.d(TAG, "showRecipientDialog: $viewModel.selectedList")
                viewModel.setBtnDoneEnabled()

            }
            setNegativeButton("Cancel") { dialog, _ ->
                dialog.cancel()
            }
            setPositiveButton("Done") { dialog, _ ->
                setRecipients()
                dialog.cancel()
            }
        }

        val dialog = builder.create()
        dialog.show()

        viewModel.btnDoneEnabled.observe(viewLifecycleOwner) {
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).isEnabled = it
        }
    }

    private fun setRecipients() {
        binding.recipientChipGrp.removeAllViews()
        enableOrDisableSendButton()

        for (i in viewModel.selectedList){
            val chip = createChip(categoryList[i], i)
            binding.recipientChipGrp.addView(chip)
        }
        Log.d(TAG, "showRecipientDialog: ${viewModel.selectedList}")
    }

    private fun senNotification(){
        viewModel.sendNotification(
            title = binding.nTitleTxt.text.toString(),
            description = binding.nDescription.text.toString(),
            content = binding.nContentTxt.text.toString()
        )
        findNavController().navigateUp()
    }

}

