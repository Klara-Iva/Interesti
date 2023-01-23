package com.example.myapplication
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.text.DecimalFormat

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
        var pristupacnost: Double = 1.0
        var pristupacnostBrojOcjena: Double = 1.0
        var zanimljivost: Double = 1.0
        var zanimljivostBrojOcjena: Double = 1.0



        backButton.setOnClickListener {
            finish()
        }

        val docRef = db.collection("places").document(id!!)
        docRef.get()
            .addOnSuccessListener { document ->
                name.text = document?.data!!["name"].toString()
                description.text = document?.data!!["description"].toString()
                Glide.with(this).load(document?.data!!["image"]).into(image)

                pristupacnost= document?.data!!["pristupacnost"].toString().toDouble()
                pristupacnostBrojOcjena= document?.data!!["pristupacnostBrojOcjena"].toString().toDouble()
                zanimljivost = document?.data!!["zanimljivost"].toString().toDouble()
                zanimljivostBrojOcjena = document?.data!!["zanimljivostBrojOcjena"].toString().toDouble()


                prosjekpristupacnost.text = DecimalFormat("#.00").format(document?.data!!["avgPristupacnost"])+" ⭐"
                prosjekzanimljivost.text = DecimalFormat("#.00").format(document?.data!!["avgZanimljivost"])+" ⭐"


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
                    pristupacnost += rBar2.rating.toDouble()
                    pristupacnostBrojOcjena+=1
                    zanimljivost += rBar.rating.toDouble()
                    zanimljivostBrojOcjena += 1
                    var averagePristupacnost: Double = pristupacnost/ pristupacnostBrojOcjena
                    var averageZanimljivost: Double = zanimljivost/ zanimljivostBrojOcjena
                    prosjekpristupacnost.text = DecimalFormat("#.00").format(averagePristupacnost)
                    prosjekzanimljivost.text = DecimalFormat("#.00").format(averageZanimljivost)

                    db.collection("places")
                        .document(id)
                        .update(
                            "avgPristupacnost", averagePristupacnost,
                            "avgZanimljivost", averageZanimljivost,
                            "pristupacnost", pristupacnost,
                            "pristupacnostBrojOcjena", pristupacnostBrojOcjena,
                            "zanimljivost", zanimljivost,
                            "zanimljivostBrojOcjena", zanimljivostBrojOcjena
                        )

                   /* val docRef = db.collection("places").document(id)
                    docRef.get()
                        .addOnSuccessListener { document ->
                            var pristupacnost = document?.data!!["pristupacnost"].toString().toDouble()
                            var pristupacnostBrojOcjena =
                                document?.data!!["pristupacnostBrojOcjena"].toString().toDouble()
                            var averagePristupacnost: Double = pristupacnost+rBar2.rating.toDouble() / pristupacnostBrojOcjena+1
                            var zanimljivost = document?.data!!["zanimljivost"].toString().toDouble()
                            var zanimljivostBrojOcjena =
                                document?.data!!["zanimljivostBrojOcjena"].toString().toDouble()
                            var averageZanimljivost: Double = zanimljivost+rBar.rating.toDouble() / zanimljivostBrojOcjena+1

                            zanimljivost += rBar.rating.toInt()
                            db.collection("places")
                                .document(id).update("zanimljivost", zanimljivost)
                            zanimljivostBrojOcjena += 1
                            db.collection("places")
                                .document(id).update("zanimljivostBrojOcjena", zanimljivostBrojOcjena)
                            db.collection("places")
                                .document(id).update("avgZanimljivost", averageZanimljivost)

                            pristupacnost += rBar2.rating.toInt()
                            db.collection("places")
                                .document(id).update("pristupacnost", pristupacnost)
                            pristupacnostBrojOcjena += 1
                            db.collection("places")
                                .document(id).update("pristupacnostBrojOcjena", pristupacnostBrojOcjena)
                            db.collection("places")
                                .document(id).update("avgPristupacnost", averagePristupacnost)

                            prosjekpristupacnost.text = averagePristupacnost.toString()

                            prosjekzanimljivost.text =averageZanimljivost.toString()
                        }*/
                    }
                }
            }
        }
    }






