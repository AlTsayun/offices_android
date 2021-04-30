package com.tsayun.offices.ui.item.itemEdit

data class ItemEditFormState(
    val usernameError: Int? = null,
    val passwordError: Int? = null,
    val isDataValid: Boolean = false
)