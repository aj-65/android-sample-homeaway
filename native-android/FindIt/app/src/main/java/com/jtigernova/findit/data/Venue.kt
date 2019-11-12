package com.jtigernova.findit.data

import android.os.Parcel
import android.os.Parcelable

data class Venue(val id: String?, val name: String?, val url: String?,
                 val categories: List<VenueCategory?>, val location: VenueLocation?) : Parcelable {
    constructor(source: Parcel) : this(
            source.readString(),
            source.readString(),
            source.readString(),
            source.createTypedArrayList(VenueCategory.CREATOR)!!,
            source.readParcelable<VenueLocation>(VenueLocation::class.java.classLoader)
    )

    override fun describeContents() = 0

    val mainCategory = categories.firstOrNull { it?.primary == true }

    val mainCategoryName = mainCategory?.name

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(id)
        writeString(name)
        writeString(url)
        writeTypedList(categories)
        writeParcelable(location, 0)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Venue> = object : Parcelable.Creator<Venue> {
            override fun createFromParcel(source: Parcel): Venue = Venue(source)
            override fun newArray(size: Int): Array<Venue?> = arrayOfNulls(size)
        }
    }
}

data class VenueLocation(val address: String?, val lat: Double, val lng: Double,
                         val postalCode: String?, val cc: String?, val city: String?,
                         val state: String?, val country: String?,
                         val formattedAddress: List<String?>) : Parcelable {
    constructor(source: Parcel) : this(
            source.readString(),
            source.readDouble(),
            source.readDouble(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            ArrayList<String?>().apply { source.readList(this as List<*>, String::class.java.classLoader) }
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(address)
        writeDouble(lat)
        writeDouble(lng)
        writeString(postalCode)
        writeString(cc)
        writeString(city)
        writeString(state)
        writeString(country)
        writeList(formattedAddress)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<VenueLocation> = object : Parcelable.Creator<VenueLocation> {
            override fun createFromParcel(source: Parcel): VenueLocation = VenueLocation(source)
            override fun newArray(size: Int): Array<VenueLocation?> = arrayOfNulls(size)
        }
    }
}

data class VenueCategory(val id: String?, val name: String?, val shortName: String?,
                         val pluralName: String?, val primary: Boolean, val icon: VenueCategoryIcon?) : Parcelable {
    constructor(source: Parcel) : this(
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            1 == source.readInt(),
            source.readParcelable<VenueCategoryIcon>(VenueCategoryIcon::class.java.classLoader)
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(id)
        writeString(name)
        writeString(shortName)
        writeString(pluralName)
        writeInt((if (primary) 1 else 0))
        writeParcelable(icon, 0)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<VenueCategory> = object : Parcelable.Creator<VenueCategory> {
            override fun createFromParcel(source: Parcel): VenueCategory = VenueCategory(source)
            override fun newArray(size: Int): Array<VenueCategory?> = arrayOfNulls(size)
        }
    }
}

data class VenueCategoryIcon(val prefix: String?, val suffix: String?) : Parcelable {
    constructor(source: Parcel) : this(
            source.readString(),
            source.readString()
    )

    val fullPath = prefix + suffix

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(prefix)
        writeString(suffix)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<VenueCategoryIcon> = object : Parcelable.Creator<VenueCategoryIcon> {
            override fun createFromParcel(source: Parcel): VenueCategoryIcon = VenueCategoryIcon(source)
            override fun newArray(size: Int): Array<VenueCategoryIcon?> = arrayOfNulls(size)
        }
    }
}