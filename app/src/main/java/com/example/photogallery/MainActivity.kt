package com.example.photogallery

import android.content.pm.PackageManager
import android.database.Cursor
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    // Creating variables for our array list, recycler view, and adapter class.
    private val PERMISSION_REQUEST_CODE = 200
    private lateinit var imagePaths: ArrayList<String>
    private lateinit var imagesRV: RecyclerView
    private lateinit var imageRVAdapter: RecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initializing our array list and recycler view.
        imagePaths = ArrayList()
        imagesRV = findViewById(R.id.idRVImages)

        // Request permissions to read external storage.
        requestPermissions()

        // Prepare the recycler view.
        prepareRecyclerView()
    }

    private fun checkPermission(): Boolean {
        // Checking if the permissions are granted or not.
        val result = ContextCompat.checkSelfPermission(applicationContext, android.Manifest.permission.READ_EXTERNAL_STORAGE)
        return result == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermissions() {
        if (checkPermission()) {
            // If permissions are already granted, get all images from external storage.
            Toast.makeText(this, "Permissions granted.", Toast.LENGTH_SHORT).show()
            getImagePath()
        } else {
            // If permissions are not granted, request them.
            requestPermission()
        }
    }

    private fun requestPermission() {
        // Requesting read external storage permissions.
        ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_MEDIA_IMAGES, android.Manifest.permission.READ_MEDIA_VIDEO, android.Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED), PERMISSION_REQUEST_CODE)
    }

    private fun prepareRecyclerView() {
        // Preparing the recycler view.
        imageRVAdapter = RecyclerViewAdapter(this, imagePaths)
        val manager = GridLayoutManager(this, 4)
        imagesRV.layoutManager = manager
        imagesRV.adapter = imageRVAdapter
    }

    private fun getImagePath() {
        // Adding all image paths to the array list.
        val isSDPresent = android.os.Environment.getExternalStorageState() == android.os.Environment.MEDIA_MOUNTED

        if (isSDPresent) {
            val columns = arrayOf(MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID)
            val orderBy = MediaStore.Images.Media._ID
            val cursor: Cursor? = contentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null, null, orderBy)

            cursor?.let {
                val count = it.count
                for (i in 0 until count) {
                    it.moveToPosition(i)
                    val dataColumnIndex = it.getColumnIndex(MediaStore.Images.Media.DATA)
                    imagePaths.add(it.getString(dataColumnIndex))
                }
                imageRVAdapter.notifyDataSetChanged()
                it.close()
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        // Called after permissions have been granted.
        when (requestCode) {
            PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty()) {
                    val storageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED
                    if (storageAccepted) {
                        Toast.makeText(this, "Permissions Granted.", Toast.LENGTH_SHORT).show()
                        getImagePath()
                    } else {
                        Toast.makeText(this, "Permissions denied, permissions are required to use the app.", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}
