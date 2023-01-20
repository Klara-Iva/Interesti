package com.example.myapplication

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.*
import android.widget.TextView.OnEditorActionListener
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class MainView : AppCompatActivity(){

    private val db = Firebase.firestore
    private lateinit var recyclerAdapter: LocationRecyclerAdapter
    var locationList: ArrayList<MyLocation> = ArrayList()//konstantno se mijenja
    var opcalista:ArrayList<MyLocation> = ArrayList()//nju ne mijenjamo  ostaje pocetna
    var locationspinner:Int=0
    val chipselected:ArrayList<String> = ArrayList()


    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.recycledviewver)
        val recyclerView = findViewById<RecyclerView>(R.id.viewer)
        val temporalLocationList: ArrayList<MyLocation> = ArrayList()


        val chipGroup = findViewById<ChipGroup>(R.id.chipGroup)

         chipGroup.setOnCheckedStateChangeListener(ChipGroup.OnCheckedStateChangeListener { group, checkedIds ->
             val ids = group.checkedChipIds
                 chipselected.clear()
                 for (id in ids) {
                     val chip = group.findViewById<Chip>(id!!)
                     chipselected.add(chip.text.toString())
                     Toast.makeText(this, chip.text, Toast.LENGTH_SHORT).show()
                     updateArrayWithChips()
                 }

        }) //<--TODO ne radi kad se zadnji chip oznaci da vrati na pocetnu listu, aka nema funkcije da provjeri to





        db.collection("places")
            .get()
            .addOnSuccessListener { result ->

                for (data in result.documents) {
                    val onelocation = data.toObject(MyLocation::class.java)
                    if (onelocation != null) {
                        onelocation.id = data.id
                        temporalLocationList.add(onelocation)
                    }
                    locationList=temporalLocationList
                    opcalista=temporalLocationList
                }
                recyclerAdapter = LocationRecyclerAdapter(temporalLocationList)
                recyclerView.apply {
                layoutManager = LinearLayoutManager(this@MainView)
                adapter = recyclerAdapter
                //pocetno prikazivanje stavki koje nije sortirano
                }
            }

                val backbutton = findViewById<ImageButton>(R.id.buttonback)
                backbutton.setOnClickListener() {
                    finish()
                }//button za gasenje aktivnosti

                val languages = resources.getStringArray(R.array.Languages)
                val spinner = findViewById<Spinner>(R.id.spinner)
                if (spinner != null) {
                    val adapter2 =
                        ArrayAdapter(this, android.R.layout.simple_spinner_item, languages)

                    spinner.adapter = adapter2
                    adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(
                            parent: AdapterView<*>,
                            view: View, position: Int, id: Long
                        ) {
                            locationspinner=position
                            //ideja je znat u svakom trenutku
                            performSort(position)

                        }

                        override fun onNothingSelected(parent: AdapterView<*>) {
                            recyclerAdapter = LocationRecyclerAdapter(temporalLocationList)
                            recyclerView.apply {
                                layoutManager = LinearLayoutManager(this@MainView)
                                adapter = recyclerAdapter
                            }

                        }
                    }
                }


        val targetedLocationInput = findViewById<EditText>(R.id.TypeLocationName)
        targetedLocationInput.setOnEditorActionListener(OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                hideKeyboard()
                performSearch(temporalLocationList)


                return@OnEditorActionListener true
            }
            false
        })


    }

 fun updateArrayWithChips(){
  var lista:ArrayList<MyLocation> =  ArrayList()

        for(chip in chipselected){
            for(data in opcalista){
                if(data.category.contains(chip))
                    lista.add(data)
            }
        }
       locationList= lista
       show(locationList)
}



   // <--TODO() velika slova imaju prednost nad malim slovima u sred imena
fun performSort(position:Int){
    val recyclerView = findViewById<RecyclerView>(R.id.viewer)
    var sortedList: ArrayList<MyLocation> = ArrayList()
    if (position == 0) {
        sortedList=locationList

          }
       if (position == 1) {
           sortedList=
               ArrayList(locationList.sortedWith(compareBy { it.name }))
       }
    else if (position == 2) {
         sortedList =
            ArrayList(locationList.sortedWith(compareBy { it.description }))
          }
    else if (position == 3) {
         sortedList =
            ArrayList(locationList.sortedWith(compareBy { it.lati }))
           }
    else if (position == 4) {
         sortedList =
            ArrayList(locationList.sortedWith(compareBy { it.long }))
          }

    show(sortedList)

}

fun show(list:ArrayList<MyLocation>){
    val recyclerView = findViewById<RecyclerView>(R.id.viewer)
    recyclerAdapter = LocationRecyclerAdapter(list)
    recyclerView.apply {
        layoutManager = LinearLayoutManager(this@MainView)
        adapter = recyclerAdapter
    }
}
    fun performSearch(locallocationList: ArrayList<MyLocation>){
        val spinner = findViewById<Spinner>(R.id.spinner)
        spinner.setSelection(0)
        val recyclerView = findViewById<RecyclerView>(R.id.viewer)
        val sortedList2: ArrayList<MyLocation> = ArrayList()
        val unos = findViewById<EditText>(R.id.TypeLocationName)
        val unos2=unos.text.toString().replaceFirstChar { it.lowercase() }
        val unos3=unos.text.toString().replaceFirstChar { it.uppercase() }

        if (unos.text.toString() != "") {

            for (data in locallocationList) {
                if (data.name.contains(unos3) || data.name.contains(unos2) )
                    sortedList2.add(data)
            }
            if(sortedList2.isEmpty()){
                hideKeyboard()
                Toast.makeText(this,"No targeted location found",Toast.LENGTH_LONG).show()
            }
            else{
                locationList=sortedList2
                show(locationList)
            }
        }

        else{
            locationList=locallocationList
            show(locationList)

        }
    }



    fun Fragment.hideKeyboard() {
        view?.let { activity?.hideKeyboard(it) }
    }

    fun Activity.hideKeyboard() {
        hideKeyboard(currentFocus ?: View(this))
    }

    fun Context.hideKeyboard(view: View) {
        val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }
}