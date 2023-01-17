package com.example.myapplication
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.bumptech.glide.Glide

class InfoActivity : AppCompatActivity() {
    private val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info)
        val id=intent.getStringExtra("id")

        var name=findViewById<TextView>(R.id.name)
        var description=findViewById<TextView>(R.id.description)
        var image=findViewById<ImageView>(R.id.image)
        val backButton=findViewById<ImageView>(R.id.button2)

        backButton.setOnClickListener {
            finish()
        }

        val docRef = db.collection("places").document(id!!)
        docRef.get()
            .addOnSuccessListener { document ->
                name.text = document?.data!!["name"].toString()
                description.text = document?.data!!["description"].toString()
                Glide.with(this).load(document?.data!!["image"]).into(image)

            }
    }

}




