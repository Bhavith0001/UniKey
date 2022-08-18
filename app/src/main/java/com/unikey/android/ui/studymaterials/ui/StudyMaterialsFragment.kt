package com.unikey.android.ui.studymaterials.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.google.android.material.transition.MaterialSharedAxis
import com.unikey.android.databinding.FragmentStudyMaterialsBinding
import com.unikey.android.setTargetSharedAxisAnim
import com.unikey.android.setUp
import com.unikey.android.ui.studymaterials.StudyMaterialsViewModel
import com.unikey.android.ui.studymaterials.adapters.StudyMaterialsLisAdapter
import java.io.File


class StudyMaterialsFragment : Fragment() {

    private lateinit var binding: FragmentStudyMaterialsBinding

    private val viewModel: StudyMaterialsViewModel by viewModels()

    private val args: StudyMaterialsFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTargetSharedAxisAnim(MaterialSharedAxis.X)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentStudyMaterialsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val title = args.subName
        binding.appBar.toolBar.setUp(this, title)

        val subjectName = args.subName
        viewModel.getChaptersList(subjectName)

        val adapter = StudyMaterialsLisAdapter(
            checkDownloadStatus = { viewModel.checkDownloadStatus(it) },
            downloadPdf = { pdf -> viewModel.downloadPdf(requireContext(), pdf, subjectName) },
            openPdf = { openPdf(subjectName, it) },
        )

        binding.materialsRecyclerView.adapter = adapter
        viewModel.chaptersList.observe(viewLifecycleOwner){
            adapter.submitList(it)
        }

    }

    private fun openPdf(subjectName: String, chapterName: String) {
        val path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).path + "/UniKey/$subjectName/$chapterName"
        val file = File(path)
        val uri = FileProvider.getUriForFile(requireContext(), "com.unikey.android.provider", file)
        Log.d("CCCC", "openPdf: $uri")

        val intent = Intent(Intent.ACTION_VIEW)
        with(intent){
            setDataAndType(uri, "application/pdf")
            flags  = Intent.FLAG_GRANT_READ_URI_PERMISSION
        }
        startActivity(intent)
    }

}