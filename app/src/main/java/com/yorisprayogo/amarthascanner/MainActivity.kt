package com.yorisprayogo.amarthascanner

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import com.scanlibrary.ScanConstants
import com.scanlibrary.ScanActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.net.URI


class MainActivity : AppCompatActivity() {

    val REQUEST_CODE = 99
    var isWithResult = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val preference = intent?.getIntExtra("amf-req-type", 4)

        if(intent?.hasExtra("amf-req-type") == true) {
            isWithResult = true
        }

        val intent = Intent(this, ScanActivity::class.java)
        intent.putExtra(ScanConstants.OPEN_INTENT_PREFERENCE, preference)
        startActivityForResult(intent, REQUEST_CODE)
    }

    fun getPathFromUri(uri: Uri?): File{
        val path = uri?.path
        return File(URI(path))
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            if(isWithResult) {
                setResult(Activity.RESULT_OK, data)
            }else{
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

        if(isWithResult){
            finish()
        }
    }

}
