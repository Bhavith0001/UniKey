package com.unikey.android.ui

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.core.content.getSystemService
import androidx.navigation.ui.AppBarConfiguration
import com.unikey.android.DefaultValue
import com.unikey.android.REG_NO
import com.unikey.android.USharedPref
import com.unikey.android.database.cloud.CloudStorage
import com.unikey.android.databinding.ActivityMainBinding
import com.unikey.android.getUnikeySharedPref

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



//        WindowCompat.setDecorFitsSystemWindows(window, false)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val datStore = CloudStorage()

        val sharedPref = getUnikeySharedPref()
        with(sharedPref){
            val regNo = getLong(REG_NO, DefaultValue)
            Log.d("JJJJ", "onCreate: $regNo")
            datStore.updateRegNo(regNo)
        }

        val requestPermissionLauncher =
            registerForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) { isGranted: Boolean ->
                if (isGranted) {
                    // Permission is granted. Continue the action or workflow in your
                    // app.
                    Log.d("PERMISION", "onViewCreated: Granted")
                } else {
                    // Explain to the user that the feature is unavailable because the
                    // features requires a permission that the user has denied. At the
                    // same time, respect the user's decision. Don't link to system
                    // settings in an effort to convince the user to change their
                    // decision.
                }
            }

        if(ContextCompat.checkSelfPermission(
                this.applicationContext,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED ){
            // You can use the API that requires the permission.
        }
        else  {
            // You can directly ask for the permission.
            // The registered ActivityResultCallback gets the result of this request.
            requestPermissionLauncher.launch(
                Manifest.permission.READ_EXTERNAL_STORAGE)
        }

    }


}