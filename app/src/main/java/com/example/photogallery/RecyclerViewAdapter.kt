package com.example.photogallery

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import java.io.File

class RecyclerViewAdapter(
    private val context: Context,
    private val imagePathArrayList: ArrayList<String>
) : RecyclerView.Adapter<RecyclerViewAdapter.RecyclerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        // Inflate layout in this method which we have created.
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_layout, parent, false)
        return RecyclerViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        // On below line we are getting the file from the path which we have stored in our list.
        val imgFile = File(imagePathArrayList[position])

        // On below line we are checking if the file exists or not.
        if (imgFile.exists()) {
            // If the file exists, we are displaying that file in our ImageView using Picasso library.
            Picasso.get()
                .load(imgFile)
                .placeholder(R.drawable.ic_launcher_background)
                .into(holder.imageIV)

            // On below line we are adding click listener to our item of RecyclerView.
            holder.itemView.setOnClickListener {
                // Inside the onClick listener we are creating a new intent
                val intent = Intent(context, ImageDetailActivity::class.java)
//
                // On below line we are passing the image path to our new activity.
                intent.putExtra("imgPath", imagePathArrayList[position])
//
                // At last we are starting our activity.
                context.startActivity(intent)
            }
        }
    }

    override fun getItemCount(): Int {
        // This method returns the size of RecyclerView
        return imagePathArrayList.size
    }

    // View Holder Class to handle RecyclerView.
    class RecyclerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Creating variables for our views.
        val imageIV: ImageView = itemView.findViewById(R.id.idIVImage)
    }
}
