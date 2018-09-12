# amf-scanner

How to use :
```
    val intent = Intent()
    intent.component = ComponentName("com.yorisprayogo.amarthascanner", "com.yorisprayogo.amarthascanner.MainActivity")
    try {
        //Camera = 4, Gallery = 5
        intent.putExtra("amf-req-type", type)
        startActivityForResult(intent, REQ_CODE)
    } catch (e: ActivityNotFoundException) {
        Toast.makeText(this, "Amartha scanner belum terinstall!", Toast.LENGTH_SHORT).show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == REQ_CODE){
            //Image URI
            val uri = data?.extras?.getParcelable<Uri>("scannedResult")
            try {
                //Convert image URI to bitmap and do whatever with the bitmap
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)
                contentResolver.delete(uri, null, null)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


```
