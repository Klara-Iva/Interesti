package com.example.myapplication
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class InfoActivity : AppCompatActivity() {
    private val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_info)
        val id=intent.getStringExtra("id")
        var prosjekzanimljivost=findViewById<TextView>(R.id.prosjekzanimljivost)
        var prosjekpristupacnost=findViewById<TextView>(R.id.prosjekpristupacnost)


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

                var pristupacnost= document?.data!!["pristupacnost"].toString().toInt()
                var pristupacnostBrojOcjena= document?.data!!["pristupacnostBrojOcjena"].toString().toInt()
                var zanimljivost = document?.data!!["zanimljivost"].toString().toInt()
                var zanimljivostBrojOcjena = document?.data!!["zanimljivostBrojOcjena"].toString().toInt()



                        prosjekpristupacnost.text =String.format("%.2f",pristupacnost / pristupacnostBrojOcjena.toFloat())

                prosjekzanimljivost.text =String.format("%.2f",zanimljivost / zanimljivostBrojOcjena.toFloat())


            }


        val rBar = findViewById<RatingBar>(R.id.ratingBar)
        val rBar2 = findViewById<RatingBar>(R.id.ratingBar2)
        if (rBar != null) {
            val button = findViewById<Button>(R.id.save)
            button?.setOnClickListener {
                //TODO unos ocjena u bazu
                if(rBar.rating.toString() == "0.0" ||  rBar.rating.toString()=="0.0"){
                    Toast.makeText(
                        this@InfoActivity,
                        "Cmon, rate it atleast", Toast.LENGTH_SHORT
                    ).show()
                }
                else {



                    val docRef = db.collection("places").document(id)
                    docRef.get()
                        .addOnSuccessListener { document ->
                            var pristupacnost = document?.data!!["pristupacnost"].toString().toInt()
                            var pristupacnostBrojOcjena =
                                document?.data!!["pristupacnostBrojOcjena"].toString().toInt()
                             var zanimljivost = document?.data!!["zanimljivost"].toString().toInt()
                            var zanimljivostBrojOcjena =
                                document?.data!!["zanimljivostBrojOcjena"].toString().toInt()

                            zanimljivost += rBar.rating.toInt()
                            db.collection("places")
                                .document(id).update("zanimljivost", zanimljivost)

                            zanimljivostBrojOcjena += 1
                            db.collection("places")
                                .document(id).update("zanimljivostBrojOcjena", zanimljivostBrojOcjena)
                            pristupacnost += rBar2.rating.toInt()
                            db.collection("places")
                                .document(id).update("pristupacnost", pristupacnost)
                            pristupacnostBrojOcjena += 1
                            db.collection("places")
                                .document(id).update("pristupacnostBrojOcjena", pristupacnostBrojOcjena)
                            prosjekpristupacnost.text =String.format("%.2f",pristupacnost / pristupacnostBrojOcjena.toFloat())

                            prosjekzanimljivost.text =String.format("%.2f",zanimljivost / zanimljivostBrojOcjena.toFloat())


                        }
                }
                }
            }
        }
    }






