package com.tsayun.offices.data.common.dataSource

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.tsayun.offices.data.login.LoginDataSource
import com.tsayun.offices.data.login.Result
import com.tsayun.offices.data.login.model.LoggedInUser
import com.tsayun.offices.data.common.model.Office
import java.io.IOException
import java.lang.Exception
import java.util.*

class OfficeDataSource(private val databaseRef: DatabaseReference) : LoginDataSource {

    private var offices: MutableMap<String, Office> = mutableMapOf()

    init {
        databaseRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach { childSnapshot ->
                    offices[childSnapshot.key ?: throw DataSourceRetrievingException()] = Office(
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

//        val taskMap: Map<String, String> = mapOf("name" to "name2", "password" to "password")
//        databaseRef.child("office2").updateChildren(taskMap)

    }

    override fun login(username: String, password: String): Result<LoggedInUser> {
        offices.forEach { entry ->
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
}