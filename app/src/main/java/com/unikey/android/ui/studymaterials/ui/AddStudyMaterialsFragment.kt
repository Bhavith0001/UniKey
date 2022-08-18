package com.unikey.android.ui.studymaterials.ui

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.transition.MaterialSharedAxis
import com.unikey.android.SELECT_PDF_COLOR
import com.unikey.android.databinding.FragmentAddStudyMaterilasBinding
import com.unikey.android.setTargetSharedAxisAnim
import com.unikey.android.setUp
import com.unikey.android.ui.studymaterials.StudyMaterialsViewModel

class AddStudyMaterialsFragment : Fragment() {

    private val REQUEST_CODE = 99
    private var pdfUri: Uri? = null

    private lateinit var binding: FragmentAddStudyMaterilasBinding
    private val viewModel: StudyMaterialsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTargetSharedAxisAnim(MaterialSharedAxis.X)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAddStudyMaterilasBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.appBar.toolBar.setUp(this, "Add Study material")
        binding.selectPdf.setOnClickListener {
            getPdfFile()
        }
        binding.uploadPdfBtn.isEnabled = false

        binding.clsName.onTextChanged()
        binding.subName.onTextChanged()
        binding.pdfFileName.onTextChanged()
    }

    private fun TextInputEditText.onTextChanged(){
        doOnTextChanged { text, _, _, _ ->
            val clsIsNotBlank = !binding.clsName.text.isNullOrBlank()
            val subIsNotBlank = !binding.subName.text.isNullOrBlank()
            val chapterIsNotBlank = !binding.pdfFileName.text.isNullOrBlank()
            val uriIsNotBlank = pdfUri != null
            binding.uploadPdfBtn.isEnabled = clsIsNotBlank && subIsNotBlank && chapterIsNotBlank && uriIsNotBlank
        }
    }

    private fun getPdfFile() {
        val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
            type = "application/pdf"
        }
        startActivityForResult(Intent.createChooser(intent,"Select pdf file"), REQUEST_CODE)
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK
            && data != null && data.data != null){

            pdfUri = data.data!!
            onResultReceived()
        }
    }

    private fun onResultReceived(){
        binding.selectPdf.apply {
            text = "PDF file elected"
            setTextColor(Color.GREEN)
        }
        binding.imgSelected.visibility = View.VISIBLE

            with(binding){
                uploadPdfBtn.setOnClickListener {
                    viewModel.uploadPdfFile(
                        clsName = clsName.text.toString(),
                        subName = subName.text.toString(),
                        pdfName = pdfFileName.text.toString(),
                        uri = pdfUri
                    ).observe(viewLifecycleOwner){
                        setUploadingStatus(it)
                    }
                }

                if (clsName.text!!.isNotBlank() && subName.text!!.isNotBlank() && pdfFileName.text!!.isNotBlank())
                    this.uploadPdfBtn.isEnabled = true

            }
    }

    private fun setUploadingStatus(status: UploadStatus){
        with(binding) {
            when (status) {
                UploadStatus.UPLOADING -> uploadingProgress.visibility = View.VISIBLE
                UploadStatus.FAILED -> {
                    uploadingProgress.visibility = View.GONE
                    Toast.makeText(requireActivity(), "UploadFailed", Toast.LENGTH_LONG).show()
                }
                UploadStatus.UPLOADED -> {
                    uploadingProgress.visibility = View.GONE
                    Toast.makeText(requireActivity(), "Uploaded", Toast.LENGTH_LONG).show()
                    clearTexts()
                }
            }
        }
    }

    private fun clearTexts(){
        binding.apply {
            clsName.text = null
            subName.text = null
            pdfFileName.text = null
            selectPdf.apply {
                text = "Select Pdf file"
                setTextColor(SELECT_PDF_COLOR)
                imgSelected.visibility = View.GONE
            }
        }
    }
}

enum class UploadStatus{
    UPLOADED, FAILED, UPLOADING
}