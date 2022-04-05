package com.ort.bacherreciclerview.Clases

import android.os.Parcel
import android.os.Parcelable
import android.widget.ImageButton


class Recetas (nombre: String, descripcion: String, urlImage: String, creatorID: String, firebaseID: String) :
        Parcelable {
    var nombre: String

    var descripcion: String

    var urlImage: String

    var firebaseID: String

    var creatorID: String

    constructor() : this("","","","","")


    init {
        this.nombre = nombre
        this.descripcion = descripcion
        this.urlImage = urlImage
        this.firebaseID = firebaseID
        this.creatorID = creatorID
    }

    constructor(source: Parcel) : this(
            source.readString()!!,
            source.readString()!!,
            source.readString()!!,
            source.readString()!!,
            source.readString()!!
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(nombre)
        writeString(descripcion)
        writeString(urlImage)
        writeString(firebaseID)
        writeString(creatorID)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Recetas> = object : Parcelable.Creator<Recetas> {
            override fun createFromParcel(source: Parcel): Recetas = Recetas(source)
            override fun newArray(size: Int): Array<Recetas?> = arrayOfNulls(size)
        }
    }
}