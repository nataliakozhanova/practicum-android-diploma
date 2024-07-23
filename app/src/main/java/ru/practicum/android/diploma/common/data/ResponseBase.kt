package ru.practicum.android.diploma.common.data

import ru.practicum.android.diploma.common.domain.ErrorType
import ru.practicum.android.diploma.common.domain.Success

open class ResponseBase(val errorType: ErrorType = Success())
