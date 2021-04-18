package com.tsayun.offices.data.common

abstract class ApplicationException(
    message: String? = null,
    cause: Throwable? = null,
) : RuntimeException(message, cause)