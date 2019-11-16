package com.jtigernova.findit.model

import android.os.Parcel
import android.os.Parcelable
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.SphericalUtil
import java.math.RoundingMode
import java.text.DecimalFormat

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

    var mainCategory: VenueCategory? = null

    var mainCategoryName: String? = null

    fun determineCategory() {
        mainCategory = categories.firstOrNull { it?.primary == true }

        mainCategoryName = mainCategory?.name
    }

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
                         val formattedAddress: List<String?>,
                         private var metersFromCityCenter: Double = 0.0) : Parcelable {

    constructor(source: Parcel) : this(
            source.readString(),
            source.readDouble(),
            source.readDouble(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            ArrayList<String?>().apply {
                source.readList(this as List<*>,
                        String::class.java.classLoader)
            },
            source.readDouble()
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
        writeDouble(metersFromCityCenter)
    }

    fun calculateDistanceFromCityCenter(cityLatLng: LatLng) {
        metersFromCityCenter = SphericalUtil.computeDistanceBetween(cityLatLng, LatLng(lat, lng))
    }

    fun getDistanceFromCityCenterInMiles(): Double {
        return metersFromCityCenter / 1609.34
    }

    fun getDistanceFromCityCenterInMilesDisplay(): String {
        //convert from meters to miles and format to 2 decimal places, rounded up
        return DecimalFormat("#.##").apply {
            roundingMode = RoundingMode.CEILING
        }.format(getDistanceFromCityCenterInMiles())
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<VenueLocation> = object :
                Parcelable.Creator<VenueLocation> {
            override fun createFromParcel(source: Parcel): VenueLocation = VenueLocation(source)
            override fun newArray(size: Int): Array<VenueLocation?> = arrayOfNulls(size)
        }
    }
}

data class VenueCategory(val id: String?, val name: String?, val shortName: String?,
                         val pluralName: String?, val primary: Boolean,
                         val icon: VenueCategoryIcon?) : Parcelable {
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
        val CREATOR: Parcelable.Creator<VenueCategory> = object :
                Parcelable.Creator<VenueCategory> {
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

    fun getFullPath(): String? {
        return prefix + ICON_RES + suffix
    }

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(prefix)
        writeString(suffix)
    }


    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<VenueCategoryIcon> = object :
                Parcelable.Creator<VenueCategoryIcon> {
            override fun createFromParcel(source: Parcel): VenueCategoryIcon =
                    VenueCategoryIcon(source)

            override fun newArray(size: Int): Array<VenueCategoryIcon?> = arrayOfNulls(size)
        }

        //show icon 88x88 with a background
        const val ICON_RES = "bg_88"
    }
}