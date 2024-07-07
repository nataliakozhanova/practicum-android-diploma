package ru.practicum.android.diploma.common.domain

abstract class ErrorType()

class Success : ErrorType()

class ServerInternalError : ErrorType()

class NoInternetError : ErrorType()

class BadRequestError : ErrorType()








