package com.tsayun.offices.data.common

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.database.*
import com.tsayun.offices.data.authentication.login.LoginDataSource
import com.tsayun.offices.data.common.model.Result
import com.tsayun.offices.data.authentication.login.model.LoggedInUser
import com.tsayun.offices.data.office.dataSource.DataSourceRetrievingException
import com.tsayun.offices.data.authentication.common.model.Account
import com.tsayun.offices.data.authentication.signup.SignUpDataSource
import com.tsayun.offices.data.authentication.signup.model.SignedUpUser
import com.tsayun.offices.data.office.OfficeDataSource
import com.tsayun.offices.data.office.models.OfficeFull
import com.tsayun.offices.data.office.models.OfficeOnMap
import com.tsayun.offices.data.office.models.OfficePreview
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

interface CommonDataSource : LoginDataSource, OfficeDataSource, SignUpDataSource

class FirebaseRealtimeDatabaseDataSource(private val databaseRef: DatabaseReference) :
    CommonDataSource {
    private var accounts: MutableMap<String, Account> = mutableMapOf()
    private var officesMap: MutableMap<String, OfficeFull> = mutableMapOf()

    private val _offices = MutableLiveData<MutableMap<String, OfficeFull>>()
    override val offices: LiveData<MutableMap<String, OfficeFull>> = _offices

    private val dateFormatter = SimpleDateFormat("dd-MM-yyyy", Locale.ROOT)

    init {
        _offices.value = officesMap

        databaseRef.child("accounts").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach { childSnapshot ->
                    accounts[childSnapshot.key ?: throw DataSourceRetrievingException()] = Account(
                        UUID.fromString(childSnapshot.key),
                        childSnapshot.child("name").getValue(String::class.java)
                            ?: throw DataSourceRetrievingException(),
                        childSnapshot.child("password").getValue(String::class.java)
                            ?: throw DataSourceRetrievingException(),
                        childSnapshot.child("email").getValue(String::class.java)
                            ?: throw DataSourceRetrievingException()
                    )
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

        databaseRef.child("offices").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach { childSnapshot ->
                    officesMap[childSnapshot.key ?: throw DataSourceRetrievingException()] =
                        OfficeFull(
                            UUID.fromString(childSnapshot.key),
                            name = childSnapshot.child("name").getValue(String::class.java)
                                ?: throw DataSourceRetrievingException(),
                            area = childSnapshot.child("area").getValue(Double::class.java)
                                ?: throw DataSourceRetrievingException(),
                            address = childSnapshot.child("address").getValue(String::class.java)
                                ?: throw DataSourceRetrievingException(),
                            roomCount = childSnapshot.child("roomCount").getValue(Int::class.java)
                                ?: throw DataSourceRetrievingException(),
                            description = childSnapshot.child("description")
                                .getValue(String::class.java)
                                ?: throw DataSourceRetrievingException(),
                            floor = childSnapshot.child("floor").getValue(Int::class.java)
                                ?: throw DataSourceRetrievingException(),
                            numberOfFloors = childSnapshot.child("numberOfFloors")
                                .getValue(Int::class.java)
                                ?: throw DataSourceRetrievingException(),
                            hasBathroom = childSnapshot.child("hasBathroom")
                                .getValue(Boolean::class.java)
                                ?: throw DataSourceRetrievingException(),
                            lastRenovationDate = dateFormatter.parse(
                                childSnapshot.child("lastRenovationDate")
                                    .getValue(String::class.java)
                                    ?: throw DataSourceRetrievingException()
                            ) ?: throw DataSourceRetrievingException(),
                            coordinates = LatLng(
                                childSnapshot.child("coordinates").child("latitude")
                                    .getValue(Double::class.java)
                                    ?: throw DataSourceRetrievingException(),
                                childSnapshot.child("coordinates").child("longitude")
                                    .getValue(Double::class.java)
                                    ?: throw DataSourceRetrievingException()
                            ),
                            imagesUrls = childSnapshot.child("images").children.map {
                                it.getValue(
                                    String::class.java
                                ) ?: throw DataSourceRetrievingException()
                            }
                        )
                    _offices.value = officesMap

                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

    }

    override fun login(username: String, password: String): Result<LoggedInUser> {
        accounts.forEach { entry ->
            if (entry.value.email == username && entry.value.password == password) {
                return@login Result.Success(
                    LoggedInUser(
                        entry.value.id,
                        entry.value.name
                    )
                )
            }
        }
        return Result.Error(IOException("Error logging in"))
    }

    override fun logout() {
        // TODO: revoke authentication
    }

    override fun getOfficeByIdOrNull(id: UUID): OfficeFull? = _offices.value?.get(id.toString())


    override fun create(
        name: String,
        area: Double,
        address: String,
        roomCount: Int,
        description: String,
        floor: Int,
        numberOfFloors: Int,
        hasBathroom: Boolean,
        lastRenovationDate: Date,
        coordinates: LatLng,
        imagesUrls: List<String>
    ): UUID {
        val id = UUID.randomUUID()
        update(
            id,
            name,
            area,
            address,
            roomCount,
            description,
            floor,
            numberOfFloors,
            hasBathroom,
            lastRenovationDate,
            coordinates,
            imagesUrls
        )
        return id
    }

    override fun update(
        id: UUID,
        name: String,
        area: Double,
        address: String,
        roomCount: Int,
        description: String,
        floor: Int,
        numberOfFloors: Int,
        hasBathroom: Boolean,
        lastRenovationDate: Date,
        coordinates: LatLng,
        imagesUrls: List<String>
    ) {

        //todo: check if saves correctly to firebase realtime database
        val office = mapOf<String, Any>(
            "name" to name,
            "area" to area,
            "address" to address,
            "roomCount" to roomCount,
            "description" to description,
            "floor" to floor,
            "numberOfFloors" to numberOfFloors,
            "hasBathroom" to hasBathroom,
            "images" to imagesUrls,
            "coordinates" to coordinates.let {
                mapOf<String, Double>(
                    "latitude" to it.latitude,
                    "longitude" to it.longitude
                )
            },
            "lastRenovationDate" to dateFormatter.format(lastRenovationDate)
        )
        databaseRef.child("offices").child(id.toString()).setValue(office)
    }

    override fun remove(id: UUID) {
        TODO("Not yet implemented")
    }


    override fun signup(
        displayName: String,
        username: String,
        password: String
    ): Result<SignedUpUser> {
        //todo: fix concurrent
        if (accounts.filterValues { it.email == username || it.name == displayName }.isEmpty()) {
            val id = UUID.randomUUID()
            val user = mapOf<String, String>(
                "email" to username,
                "name" to displayName,
                "password" to password
            )
            databaseRef.child("accounts").child(id.toString()).setValue(user)
            return Result.Success(SignedUpUser(id, displayName))
        } else {
            return Result.Error(IOException("Error singing up"))
        }
    }


}