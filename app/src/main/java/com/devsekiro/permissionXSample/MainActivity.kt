package com.devsekiro.permissionXSample

import android.Manifest
import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import com.devsekiro.permissionXSample.databinding.ActivityMainBinding
import com.devsekiro.permissionXSample.utilities.base.BaseActivity
import com.devsekiro.permissionx.entities.ItemPermissionX
import com.devsekiro.permissionx.PermissionXManager

class MainActivity : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {

    private val permissionManager = PermissionXManager(this@MainActivity)

    override fun onCreated() {
        initViews()
    }

    private fun initViews() {

        binding.mBtnSinglePermission.setOnClickListener {
            askSinglePermission()
        }

        binding.mBtnMultiPermission.setOnClickListener {
            askMultiplePermissions()
        }
    }

    // A single function to handle both single and multiple permissions
    private fun askPermissions(sdkPermissionsList: List<ItemPermissionX>) {
        permissionManager.requestPermissionsByDynamicSDK(
            sdkPermissionsList = sdkPermissionsList,
        ) { allGranted, deniedPermissions ->
            if (allGranted) {
                // All permissions granted for this SDK version
                Log.d("Permissions", "All granted")
            } else {
                // Handle denied permissions
                Log.d("Denied Permissions", deniedPermissions.toString())
            }
        }
    }

    // Sample usage for a single permission
    private fun askSinglePermission() {

        val sdkPermissionsList = listOf(
            ItemPermissionX(
                intRange = (Int.MIN_VALUE..Int.MAX_VALUE),
                permissionList = arrayListOf(Manifest.permission.CAMERA),
            )
        )
        askPermissions(sdkPermissionsList)
    }

    // Sample usage for multiple permissions with SDK-specific handling
    @SuppressLint("InlinedApi")
    private fun askMultiplePermissions() {
//        val sdkPermissionsList = listOf(
//            (Build.VERSION_CODES.TIRAMISU..Int.MAX_VALUE) to arrayOf(
//                android.Manifest.permission.CAMERA,
//                android.Manifest.permission.READ_MEDIA_IMAGES,
//                android.Manifest.permission.READ_MEDIA_VIDEO,
//                android.Manifest.permission.READ_MEDIA_AUDIO,
//                android.Manifest.permission.POST_NOTIFICATIONS
//            ),
//            (Build.VERSION_CODES.Q..Build.VERSION_CODES.S) to arrayOf(
//                android.Manifest.permission.CAMERA,
//                android.Manifest.permission.READ_EXTERNAL_STORAGE,
//                android.Manifest.permission.ACCESS_MEDIA_LOCATION
//            ),
//            (Int.MIN_VALUE..Build.VERSION_CODES.P) to arrayOf(
//                android.Manifest.permission.CAMERA,
//                android.Manifest.permission.READ_EXTERNAL_STORAGE
//            )
//        )

        val sdkPermissionsList = listOf(
            ItemPermissionX(
                intRange = (Build.VERSION_CODES.TIRAMISU..Int.MAX_VALUE),
                permissionList = arrayListOf(
                    Manifest.permission.CAMERA,
                    Manifest.permission.READ_MEDIA_IMAGES,
                    Manifest.permission.READ_MEDIA_VIDEO,
                    Manifest.permission.READ_MEDIA_AUDIO,
                    Manifest.permission.POST_NOTIFICATIONS
                ),

            ),
            ItemPermissionX(
                intRange = (Build.VERSION_CODES.Q..Build.VERSION_CODES.S),
                permissionList = arrayListOf(
                    Manifest.permission.CAMERA,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.ACCESS_MEDIA_LOCATION
                ),

            ),
            ItemPermissionX(
                intRange = (Int.MIN_VALUE..Build.VERSION_CODES.P),
                permissionList = arrayListOf(
                    Manifest.permission.CAMERA,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ),
            )
        )

        askPermissions(sdkPermissionsList)
    }
}
