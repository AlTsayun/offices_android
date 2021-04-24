package com.tsayun.offices.data.common

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.tsayun.offices.data.authentication.login.LoginDataSource
import com.tsayun.offices.data.common.model.Result
import com.tsayun.offices.data.authentication.login.model.LoggedInUser
import com.tsayun.offices.data.office.dataSource.DataSourceRetrievingException
import com.tsayun.offices.data.authentication.common.model.Account
import com.tsayun.offices.data.authentication.signup.SignUpDataSource
import com.tsayun.offices.data.authentication.signup.model.SignedUpUser
import com.tsayun.offices.data.office.OfficeDataSource
import com.tsayun.offices.data.office.models.OfficeDetails
import com.tsayun.offices.data.office.models.OfficePreview
import java.io.IOException
import java.util.*

interface CommonDataSource: LoginDataSource, OfficeDataSource, SignUpDataSource

class FirebaseRealtimeDatabaseDataSource(private val databaseRef: DatabaseReference) : CommonDataSource {
    private var accounts: MutableMap<String, Account> = mutableMapOf()
    private var officesMap:  MutableMap<String, OfficeDetails> = mutableMapOf()

    private val _offices = MutableLiveData<MutableMap<String, OfficeDetails>>()
    override val offices: LiveData<MutableMap<String, OfficeDetails>> = _offices

    init {
        _offices.value = officesMap

        databaseRef.child("accounts").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach { childSnapshot ->
                    accounts[childSnapshot.key ?: throw DataSourceRetrievingException()] = Account(
                        UUID.fromString(childSnapshot.key),
                        childSnapshot.child("name").getValue(String::class.java) ?: throw DataSourceRetrievingException(),
                        childSnapshot.child("password").getValue(String::class.java) ?: throw DataSourceRetrievingException(),
                        childSnapshot.child("email").getValue(String::class.java) ?: throw DataSourceRetrievingException()
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
                        OfficeDetails(
                            UUID.fromString(childSnapshot.key),
                            childSnapshot.child("name").getValue(String::class.java) ?: throw DataSourceRetrievingException(),
                            childSnapshot.child("area").getValue(Double::class.java) ?: throw DataSourceRetrievingException(),
                            childSnapshot.child("address").getValue(String::class.java) ?: throw DataSourceRetrievingException(),
                            childSnapshot.child("roomCount").getValue(Int::class.java) ?: throw DataSourceRetrievingException()
                        )
                    _offices.value = officesMap

                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })


//        databaseRef.child("message").setValue("hello")

//        val taskMap: Map<String, String> = mapOf("name" to "name2", "password" to "password")
//        databaseRef.child("office2").updateChildren(taskMap)

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

    override fun getOfficeByIdOrNull(id: UUID): OfficeDetails? = _offices.value?.get(id.toString())


    override fun getAllOfficePreviews(): List<OfficePreview> =
        _offices.value?.values?.map { OfficePreview(it.id, it.name, it.area, it.address, it.roomCount) } ?: listOf()

    override fun signup(
        displayName: String,
        username: String,
        password: String
    ): Result<SignedUpUser> {
        //todo: fix concurrent
        if (accounts.filterValues { it.email == username || it.name == displayName }.isEmpty()) {
            val id = UUID.randomUUID()
            val user = mapOf<String, String>("email" to username, "name" to displayName, "password" to password)
            databaseRef.child("accounts").child(id.toString()).setValue(user)
            return Result.Success(SignedUpUser(id, displayName))
        } else {
            return Result.Error(IOException("Error singing up"))
        }
    }

}