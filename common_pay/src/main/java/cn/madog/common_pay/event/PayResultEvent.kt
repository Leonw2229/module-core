package com.hdfjy.hdf.event

data class PayResultEvent(
    var payStatus: Int,
    var resultMessage: String?
){
    companion object {
        const val RESULT_STATUS_CANCEL = 1
        const val RESULT_STATUS_SUCCESS = 2
        const val RESULT_STATUS_ERROR = 3
    }
}