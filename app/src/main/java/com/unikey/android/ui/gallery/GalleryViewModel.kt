package com.unikey.android.ui.gallery

import androidx.lifecycle.ViewModel
import com.unikey.android.database.cloud.CloudStorage
import com.unikey.android.fakeImageDataList
import com.unikey.android.objects.GalleryImage

class GalleryViewModel : ViewModel() {

    private val dataStore = CloudStorage()

    val lis = fakeImageDataList

    val imagesList = dataStore.imagesList

    init {
        dataStore.getImages()
    }

}