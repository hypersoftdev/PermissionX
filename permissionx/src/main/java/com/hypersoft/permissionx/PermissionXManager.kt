package com.hypersoft.permissionx

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.hypersoft.permissionx.entities.ItemPermissionX

class PermissionXManager(
    private val activity: FragmentActivity
) {
    private var permissionCallback: ((Boolean, List<String>) -> Unit)? = null

    private var permissionsToCheck = ArrayList<String>()

    private val PERMISSION_TAG = "CheckPermissionLog"


    private val permissionLauncher =
        activity.registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            val deniedList = permissions.filterValues { !it }.keys.toList()

            // Check if we should show rationale
            val shouldShowRationale = shouldShowRationale(deniedList)

            if (!shouldShowRationale) {

                showRationaleDialog("Please Enable Permission From Settings") {
                    // Request permissions after rationale
                    permissionsToCheck.clear()
                    permissionsToCheck.addAll(deniedList)
                    openAppPermissions(activity)
                }

            } else {
                // Request permissions directly (first time or user selected "Don't ask again")
            }


            permissionCallback?.let { callback ->
                callback(deniedList.isEmpty(), deniedList)
            }
        }


    private var permissionSettingsLauncher = activity.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        // Recheck permissions when the user returns from the settings screen
        checkPermissionsAfterSettings()
    }

    private fun checkPermissionsAfterSettings() {
        val deniedPermissions = permissionsToCheck.filter { permission ->
            ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED
        }

        if (deniedPermissions.isEmpty()) {
            // All permissions are granted
            onAllPermissionsGranted()
        } else {
            // Some permissions are still denied
            onPermissionsDenied(deniedPermissions)
        }
    }

    private fun onPermissionsDenied(deniedPermissions: List<String>) {
        Log.d(PERMISSION_TAG, "onPermissionsDenied: $deniedPermissions")
    }

    private fun onAllPermissionsGranted() {
        Log.d(PERMISSION_TAG, "onAllPermissionsGranted: ")
    }

    fun requestPermissionsByDynamicSDK(
        sdkPermissionsList: List<ItemPermissionX>,
        callback: (Boolean, List<String>) -> Unit
    ) {
        permissionCallback = callback

        // Get current SDK version
        val currentSdkVersion = Build.VERSION.SDK_INT

        // Find the appropriate permission set based on the current SDK version
        val matchingPermissions = sdkPermissionsList.firstOrNull { currentSdkVersion in it.intRange }?.permissionList

        val permissionsToRequest = when {
            !matchingPermissions.isNullOrEmpty() -> matchingPermissions
            else -> emptyList()
        }

        if (permissionsToRequest.isEmpty()) {
            // No permissions to request, return success
            callback(true, emptyList())
            return
        }

        // Filter out already granted permissions
        val deniedPermissions = permissionsToRequest.filter {
            ContextCompat.checkSelfPermission(activity, it) != PackageManager.PERMISSION_GRANTED
        }

        if (deniedPermissions.isEmpty()) {
            // All permissions already granted
            callback(true, emptyList())
        } else {
            permissionLauncher.launch(deniedPermissions.toTypedArray())
        }
    }

    // Helper to check if we should show rationale for any denied permissions
    private fun shouldShowRationale(permissions: List<String>): Boolean {
        val checkPermission = permissions.any { ActivityCompat.shouldShowRequestPermissionRationale(activity, it) }
        Log.d(PERMISSION_TAG, "shouldShowRationale: $checkPermission")
        return checkPermission
    }

    private fun openAppPermissions(activity: FragmentActivity?) {
        activity?.let {
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                data = Uri.fromParts("package", it.packageName, null)
            }
            permissionSettingsLauncher.launch(intent)
        }
    }

    // Helper to show rationale dialog
    private fun showRationaleDialog(message: String, onProceed: () -> Unit) {
        AlertDialog.Builder(activity)
            .setMessage(message)
            .setPositiveButton("OK") { _, _ -> onProceed() }
            .setNegativeButton("Cancel", null)
            .show()
    }
}
