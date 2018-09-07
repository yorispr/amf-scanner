package com.yorisprayogo.amarthascanner

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import com.scanlibrary.ScanConstants
import com.scanlibrary.ScanActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.io.IOException


class MainActivity : AppCompatActivity() {

    val REQUEST_CODE = 99

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val preference = ScanConstants.OPEN_CAMERA
        val intent = Intent(this, ScanActivity::class.java)
        intent.putExtra(ScanConstants.OPEN_INTENT_PREFERENCE, preference)
        startActivityForResult(intent, REQUEST_CODE)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val uri = data?.extras!!.getParcelable<Uri>(ScanConstants.SCANNED_RESULT)
            var bitmap: Bitmap? = null
            try {
                bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)
                contentResolver.delete(uri!!, null, null)
                imageView.setImageBitmap(bitmap)
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }
    }

}
