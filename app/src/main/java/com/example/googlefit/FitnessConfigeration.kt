package com.example.googlefit

import android.app.Activity
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.fitness.FitnessOptions
import com.google.android.gms.fitness.data.DataType

class FitnessConfigeration(val activity: Activity) {

    lateinit var fitnessOptions: FitnessOptions
    lateinit var account: GoogleSignInAccount
    companion object{
        val GOOGLE_FIT_PERMISSIONS_REQUEST_CODE = 1
        val ALREADY_GRANTED = "already granted"
        val GRANTED_SUCCESS = "granted success"
    }
    init {
        fitnessOptions= FitnessOptions.builder()
            .addDataType(DataType.TYPE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
            .addDataType(DataType.TYPE_CALORIES_EXPENDED, FitnessOptions.ACCESS_READ)
            .addDataType(DataType.AGGREGATE_CALORIES_EXPENDED)
            .addDataType(DataType.AGGREGATE_STEP_COUNT_DELTA)
            .build()
        account = GoogleSignIn.getAccountForExtension(activity, fitnessOptions)
    }
    fun grantPermissions() : String{
        var currently_Signed = GoogleSignIn.getLastSignedInAccount(activity)
        if(!GoogleSignIn.hasPermissions(account) || currently_Signed == null)
        {
            GoogleSignIn.requestPermissions(
                activity, // your activity
                FitnessConfigeration.GOOGLE_FIT_PERMISSIONS_REQUEST_CODE, // e.g. 1
                account,
                fitnessOptions)
            return GRANTED_SUCCESS
        }
        return ALREADY_GRANTED
    }
}