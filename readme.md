[![](https://jitpack.io/v/hypersoftdev/PermissionX.svg)](https://jitpack.io/#hypersoftdev/PermissionX)

# PermissionX

PermissionX simplifies Android permission requests by handling version-specific checks, like "read external storage" for SDK 32 and below or "read media" for higher versions, and managing newer permissions like "post notification" effortlessly.

## Gradle Integration

### Step A: Add Maven Repository

In your project-level **build.gradle** or **settings.gradle** file, add the JitPack repository:
```
repositories {
    google()
    mavenCentral()
    maven { url "https://jitpack.io" }
}
```  

### Step B: Add Dependencies

In your app-level **build.gradle** file, add the library dependency. Use the latest version: [![](https://jitpack.io/v/hypersoftdev/PermissionX.svg)](https://jitpack.io/#hypersoftdev/PermissionX)

Groovy Version
```
 implementation 'com.github.hypersoftdev:PermissionX:x.x.x'
```
Kts Version
```
 implementation("com.github.hypersoftdev:PermissionX:x.x.x")
```

## How to Use


### For Single Permission
```
  val singlePermission = listOf(
            ItemPermissionX(
                intRange = (Int.MIN_VALUE..Int.MAX_VALUE),
                permissionList = arrayListOf(Manifest.permission.CAMERA),
            )
        )
```

### For Multiple Permission
```
val multiplePermissionsList = listOf(
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
```

We can give different sdk range for asking different permission for variety of sdk versions

```
 permissionManager.requestPermissionsByDynamicSDK(
            sdkPermissionsList = permissionsToAsk, //Replace permissionsToAsk with singlePermission or multiplePermissionsList
        ) { allGranted, deniedPermissions ->
            if (allGranted) {
                // All permissions granted for this SDK version
                Log.d("Permissions", "All granted")
            } else {
                // Handle denied permissions
                Log.d("Denied Permissions", deniedPermissions.toString())
            }
        }
```

# Acknowledgements

This work would not have been possible without the invaluable contributions of [Abdul Moiz](https://github.com/devsekiro). His expertise, dedication, and unwavering support have been instrumental in bringing this project to fruition.

We are deeply grateful for [AbdulMoiz](https://github.com/devsekiro) involvement and his belief in the importance of this work. His contributions have made a significant impact, and we are honored to have had the opportunity to collaborate with him.

# LICENSE

Copyright 2023 Hypersoft Inc

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
