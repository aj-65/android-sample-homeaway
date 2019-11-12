package com.jtigernova.findit.data

import android.os.Parcel
import android.os.Parcelable

data class Venue(val id: String?, val name: String?, val url: String?,
                 val categories: Array<VenueCategory?>, val location: VenueLocation?) : Parcelable {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Venue) return false

        if (id != other.id) return false
        if (name != other.name) return false
        if (url != other.url) return false
        if (!categories.contentEquals(other.categories)) return false
        if (location != other.location) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + url.hashCode()
        result = 31 * result + categories.contentHashCode()
        result = 31 * result + location.hashCode()
        return result
    }

    constructor(source: Parcel) : this(
            source.readString(),
            source.readString(),
            source.readString(),
            source.readParcelableArray(VenueCategory::class.java.classLoader) as Array<VenueCategory?>,
            source.readParcelable<VenueLocation>(VenueLocation::class.java.classLoader)
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(id)
        writeString(name)
        writeString(url)
        writeParcelableArray(categories, 0)
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
                         val formattedAddress: Array<String?>) : Parcelable {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is VenueLocation) return false

        if (address != other.address) return false
        if (lat != other.lat) return false
        if (lng != other.lng) return false
        if (postalCode != other.postalCode) return false
        if (cc != other.cc) return false
        if (city != other.city) return false
        if (state != other.state) return false
        if (country != other.country) return false
        if (!formattedAddress.contentEquals(other.formattedAddress)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = address.hashCode()
        result = 31 * result + lat.hashCode()
        result = 31 * result + lng.hashCode()
        result = 31 * result + postalCode.hashCode()
        result = 31 * result + cc.hashCode()
        result = 31 * result + city.hashCode()
        result = 31 * result + state.hashCode()
        result = 31 * result + country.hashCode()
        result = 31 * result + formattedAddress.contentHashCode()
        return result
    }

    constructor(source: Parcel) : this(
            source.readString(),
            source.readDouble(),
            source.readDouble(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readArray(String::class.java.classLoader) as Array<String?>
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
        writeStringArray(formattedAddress)
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