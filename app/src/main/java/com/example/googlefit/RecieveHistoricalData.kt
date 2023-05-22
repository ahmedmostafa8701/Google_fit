package com.example.googlefit

import com.google.android.gms.fitness.result.DataReadResponse

interface RecieveHistoricalData {
    fun onRecieve(response: DataReadResponse)
}