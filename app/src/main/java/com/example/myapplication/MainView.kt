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
import androidx.core.view.iterator
import androidx.recyclerview.widget.DividerItemDecoration
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

        recyclerView.addItemDecoration(
            DividerItemDecoration(
                recyclerView.context,
                DividerItemDecoration.VERTICAL
            )
        )


        val chipGroup = findViewById<ChipGroup>(R.id.chipGroup)

        for(chip in chipGroup){
            chip.setOnClickListener(){
                areChipsSelected(chipGroup)
            }
        }

        chipGroup.setOnClickListener {
            if (
                chipGroup.checkedChipIds.isEmpty()) {

                show(locationList)
            }
        }

         chipGroup.setOnCheckedStateChangeListener { group, _ ->
             val ids = group.checkedChipIds
             chipselected.clear()
             for (id in ids) {
                 val chip = group.findViewById<Chip>(id!!)
                 chipselected.add(chip.text.toString())
                 //Toast.makeText(this, chip.text, Toast.LENGTH_SHORT).show()
                 updateArrayWithChips()
             }
         }

    db.collection("places")
        .get()
        .addOnSuccessListener { result ->

            for (data in result.documents) {
                val onelocation = data.toObject(MyLocation::class.java)
                if (onelocation != null) {
                    onelocation.id = data.id
                    temporalLocationList.add(onelocation)
                }
                locationList = temporalLocationList
                opcalista = temporalLocationList
            }
            recyclerAdapter = LocationRecyclerAdapter(temporalLocationList)
            recyclerView.apply {
                layoutManager = LinearLayoutManager(this@MainView)
                adapter = recyclerAdapter

            }


}


        val backbutton = findViewById<ImageButton>(R.id.buttonback)
                backbutton.setOnClickListener {
                    finish()
                }//button za gasenje aktivnosti


        val kategorije = resources.getStringArray(R.array.Kategorije)
        val spinner = findViewById<Spinner>(R.id.spinner)
        if (spinner != null) {
            val adapter2 = ArrayAdapter(this, android.R.layout.simple_spinner_item, kategorije)

                    spinner.adapter = adapter2
                    adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(
                            parent: AdapterView<*>,
                            view: View, position: Int, id: Long
                        ) {
                            locationspinner=position
                            //ideja je znat u svakom trenutku
                            performSortAndShowIt()


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
        targetedLocationInput.setOnEditorActionListener(OnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                hideKeyboard()
                performSearch(temporalLocationList)
                return@OnEditorActionListener true
            }
            false
        })

    }

 fun updateArrayWithChips(){
  val lista:ArrayList<MyLocation> =  ArrayList()

        for(chip in chipselected){
            for(data in opcalista){
                if(data.category.contains(chip))
                    lista.add(data)
            }
        }
       locationList= lista
      performSortAndShowIt()

  }

    fun areChipsSelected(chipGroup: ChipGroup){
        if(chipGroup.checkedChipIds.size == 0){
            locationList = opcalista
            performSortAndShowIt()

        }
    }


    // <--TODO() velika slova imaju prednost nad malim slovima u sred imena
   fun performSortAndShowIt(){
        val ocjena = findViewById<TextView>(R.id.prikazavgzanimljivost)
        val position=locationspinner
        var sortedList: ArrayList<MyLocation> = ArrayList()
        when(position){
            1 -> {
                sortedList=
                    ArrayList(locationList.sortedWith(compareBy { it.name }))

            }
            2 -> {
                sortedList=
                    ArrayList(locationList.sortedWith(compareByDescending{
                            it.avgZanimljivost
                    }))

            }
            3 -> {
                sortedList=
                    ArrayList(locationList.sortedWith(compareByDescending{
                            it.avgPristupacnost
                    }))

            }
            else -> {
                sortedList = locationList
            }
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



    // fun Fragment.hideKeyboard() {
    //    view?.let { activity?.hideKeyboard(it) }
    //}

    fun Activity.hideKeyboard() {
        hideKeyboard(currentFocus ?: View(this))
    }

    fun Context.hideKeyboard(view: View) {
        val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }
}