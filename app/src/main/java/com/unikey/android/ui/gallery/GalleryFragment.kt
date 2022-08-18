package com.unikey.android.ui.gallery

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.transition.MaterialSharedAxis
import com.unikey.android.database.local.TestInter
import com.unikey.android.databinding.FragmentGalleryBinding
import com.unikey.android.setTargetSharedAxisAnim
import com.unikey.android.setUp
import kotlinx.coroutines.launch

class GalleryFragment : Fragment() {

    private lateinit var binding: FragmentGalleryBinding
    private val viewModel : GalleryViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTargetSharedAxisAnim(MaterialSharedAxis.X)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentGalleryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.appBar.toolBar.setUp(this, "Gallery")

        val adapter = GalleryAdapter(this)
        binding.imagesRecyclerView.adapter = adapter

        viewModel.imagesList.observe(viewLifecycleOwner){
            adapter.submitList(it)
            Log.d("LLLL", "getImages: Inside fragment $it")
        }
    }

}