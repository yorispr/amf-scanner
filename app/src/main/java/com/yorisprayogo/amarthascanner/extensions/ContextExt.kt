package com.yorisprayogo.amarthascanner.extensions

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import com.yorisprayogo.amarthascanner.R

/**
 * Created by subkhansarif on 13/09/18
 **/

inline val Context.isStoragePermissionGranted: Boolean
    get() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
    }

fun Context.stringRes(stringId: Int): String {
    return resources.getString(stringId)
}

fun Context.showSimpleDialog(message: String, cancelable: Boolean = true): Boolean {
    showDialog(message = message, isCancelable = cancelable, positiveButtonText = "OK")
    return true
}

fun Context.showDialog(
        title: String? = null,
        message: String? = null,
        positiveButtonText: String? = null,
        negativeButtonText: String? = null,
        isCancelable: Boolean? = true,
        customLayout: Int? = null,
        customLayoutHandler: ((View) -> Unit)? = null,
        positiveCallback: (() -> Unit)? = null,
        negativeCallback: (() -> Unit)? = null): AlertDialog {

    val builder = AlertDialog.Builder(this)
    title?.also { builder.setTitle(it) }
    message?.also { builder.setMessage(it) }
    builder.setPositiveButton(positiveButtonText, { _, _ -> positiveCallback?.invoke() })
    builder.setNegativeButton(negativeButtonText, { _, _ -> negativeCallback?.invoke() })
    builder.setCancelable(isCancelable ?: true)
    customLayout?.also {
        val view = LayoutInflater.from(this).inflate(it, null)
        builder.setView(view)
        customLayoutHandler?.invoke(view)
    }
    val dialog = builder.show()
    // setTypeFace(dialog)
    return dialog
}

fun Activity.requestPermission(permission: Array<String>, requestCode: Int, rationaleMessage: String): Boolean {
    if (ContextCompat.checkSelfPermission(this, permission[0]) != PackageManager.PERMISSION_GRANTED) {
        // request new permission
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission[0])) {
            showDialog(message = rationaleMessage,
                    positiveButtonText = "OK",
                    positiveCallback = {
                        ActivityCompat.requestPermissions(this, permission, requestCode)
                    })
        } else {
            ActivityCompat.requestPermissions(this, permission, requestCode)
        }
        // not granted
        return false
    } else {
        // already granted
        return true
    }
}

fun Activity.isPermissionGranted(permission: String): Boolean {
    return ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED
}