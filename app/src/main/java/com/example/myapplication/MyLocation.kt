package com.example.myapplication


data class MyLocation(
    var id:String="",
    var lati: Double?=null,
    var long: Double?=null,
    var name: String="",
    var image: String,
    var description:String="",
    var category:String="",
    var zanimljivost:Int?=null,
    var pristupacnost:Int?=null,
    var zanimljivostBrojOcjena: Int?=null,
    var pristupacnostBrojOcjena: Int?=null,
    var avgZanimljivost:Double?=null,
    var avgPristupacnost:Double?=null,

){
    constructor(): this("", null,null,"","","","")
}