package com.example.photogallery

import android.os.Bundle
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.Picasso
import java.io.File

class ImageDetailActivity : AppCompatActivity() {

    // Creating a string variable, image view variable, and a variable for our scale gesture detector class.
    private lateinit var imgPath: String
    private lateinit var imageView: ImageView
    private lateinit var scaleGestureDetector: ScaleGestureDetector

    // Defining our scale factor.
    private var mScaleFactor = 1.0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_detail)

        // Getting data which we have passed from our adapter class.
        imgPath = intent.getStringExtra("imgPath") ?: ""

        // Initializing our image view.
        imageView = findViewById(R.id.idIVImage)

        // Initializing our scale gesture detector for zoom in and out for our image.
        scaleGestureDetector = ScaleGestureDetector(this, ScaleListener())

        // Getting our image file from its path.
        val imgFile = File(imgPath)

        // If the file exists then we are loading that image in our image view.
        if (imgFile.exists()) {
            Picasso.get().load(imgFile).placeholder(R.drawable.ic_launcher_background).into(imageView)
        }
    }

    override fun onTouchEvent(motionEvent: MotionEvent): Boolean {
        // Inside on touch event method we are calling on
        // touch event method and passing our motion event to it.
        scaleGestureDetector.onTouchEvent(motionEvent)
        return true
    }

    private inner class ScaleListener : ScaleGestureDetector.SimpleOnScaleGestureListener() {
        // Creating a class for our scale listener and extending it with gesture listener.
        override fun onScale(scaleGestureDetector: ScaleGestureDetector): Boolean {
            // Inside on scale method we are setting scale
            // for our image in our image view.
            mScaleFactor *= scaleGestureDetector.scaleFactor
            mScaleFactor = Math.max(0.1f, Math.min(mScaleFactor, 10.0f))

            // Setting scale x and scale y to our image view.
            imageView.scaleX = mScaleFactor
            imageView.scaleY = mScaleFactor
            return true
        }
    }
}
