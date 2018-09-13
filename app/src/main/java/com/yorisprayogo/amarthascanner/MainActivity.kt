package com.yorisprayogo.amarthascanner

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.scanlibrary.ScanActivity
import com.scanlibrary.ScanConstants
import com.yorisprayogo.amarthascanner.extensions.isPermissionGranted
import com.yorisprayogo.amarthascanner.extensions.requestPermission
import com.yorisprayogo.amarthascanner.extensions.stringRes
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.net.URI


class MainActivity : AppCompatActivity() {

    val REQUEST_CODE = 99
    var isWithResult = false

    companion object {
        const val REQ_PERMISSIONS_CODE = 2121
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (!isPermissionGranted(Manifest.permission.READ_EXTERNAL_STORAGE)
                || !isPermissionGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                || !isPermissionGranted(Manifest.permission.CAMERA)
        ) {
            requestPermission(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA),
                    REQ_PERMISSIONS_CODE,
                    stringRes(R.string.lbl_permissions_rationale))
        } else {
            startCamera()
        }
    }

    private fun startCamera() {
        val preference = intent?.getIntExtra("amf-req-type", 4)

        if (intent?.hasExtra("amf-req-type") == true) {
            isWithResult = true
        }

        val intent = Intent(this, ScanActivity::class.java)
        intent.putExtra(ScanConstants.OPEN_INTENT_PREFERENCE, preference)
        startActivityForResult(intent, REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            REQ_PERMISSIONS_CODE -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    // Do the business
                    startCamera()
                } else {
                    Log.e("RequestPermissionResult", "Denied!")
                    Toast.makeText(this, stringRes(R.string.lbl_permissions_denied), Toast.LENGTH_SHORT).show()
                }
                return
            }

            else -> {
                // Ignore all other requests.
            }
        }
    }

    fun getPathFromUri(uri: Uri?): File {
        val path = uri?.path
        return File(URI(path))
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            if (isWithResult) {
                setResult(Activity.RESULT_OK, data)
            } else {
                val uri = data?.extras?.getParcelable<Uri>("scannedResult")
                try {
                    val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)
                    contentResolver.delete(uri, null, null)
                    imageView.setImageBitmap(bitmap)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

        if (isWithResult) {
            finish()
        }
    }

}
