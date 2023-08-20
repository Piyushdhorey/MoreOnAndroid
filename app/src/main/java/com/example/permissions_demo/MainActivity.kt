package com.example.permissions_demo

import android.Manifest
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog

class MainActivity : AppCompatActivity() {

    private var cameraResultLauncher: ActivityResultLauncher<String> =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()){
            isGranted ->
            if(isGranted){
                Toast.makeText(this, "Permission granted for camera", Toast.LENGTH_LONG).show()
            }else{
                Toast.makeText(this, "Permission denied for camera", Toast.LENGTH_LONG).show()
            }
        }


    private val cameraAndLocationResultLauncher: ActivityResultLauncher<Array<String>> =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()){
                permissions ->
            permissions.entries.forEach{
                val permissionName = it.key
                val isGranted = it.value
                if(isGranted){
                    if(permissionName == Manifest.permission.ACCESS_FINE_LOCATION){
                        Toast.makeText(this, "Permission granted for location", Toast.LENGTH_LONG).show()
                    }else{
                        Toast.makeText(this, "Permission granted for camera", Toast.LENGTH_LONG).show()
                    }
                }else{
                    if(permissionName == Manifest.permission.ACCESS_FINE_LOCATION){
                        Toast.makeText(this, "Permission denied for location", Toast.LENGTH_LONG).show()
                    }else{
                        Toast.makeText(this, "Permission denied for camera", Toast.LENGTH_LONG).show()
                    }
                }

            }

        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val cameraBtnPermission: Button = findViewById(R.id.camera_permissionBtn)
        cameraBtnPermission.setOnClickListener {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)){

                showRationaleDialog("Permission for camera required for app",
                    "Camera cannot be used because access denied")
            }
            else{
                cameraAndLocationResultLauncher.launch(
                    arrayOf(Manifest.permission.CAMERA, Manifest.permission.ACCESS_FINE_LOCATION)
                )
            }
        }

    }

    private fun showRationaleDialog(title: String, message: String){
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle(title)
            .setMessage(message)
            .setPositiveButton("Cancel"){dialog, _-> dialog.dismiss()}

        builder.create().show()
    }
}