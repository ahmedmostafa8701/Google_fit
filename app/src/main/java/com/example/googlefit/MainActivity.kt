package com.example.googlefit

import android.app.Activity
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import com.example.googlefit.databinding.ActivityMainBinding
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.fitness.data.DataSet
import com.google.android.gms.fitness.data.Field
import com.google.android.gms.fitness.result.DataReadResponse
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() , RecieveHistoricalData{
    val binding:ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    lateinit var account:GoogleSignInAccount
    lateinit var myFitness:MyFitness
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        GlobalScope.launch {
            requestDataSet()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun requestDataSet() {
        myFitness = MyFitness(this, this)
        val fitnessConfigeration = FitnessConfigeration(this)
        account = fitnessConfigeration.account
        val operationStatus = fitnessConfigeration.grantPermissions()
        if(operationStatus == FitnessConfigeration.ALREADY_GRANTED){
            myFitness.accessGoogleFit(account)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (resultCode) {
            Activity.RESULT_OK -> when (requestCode) {
                FitnessConfigeration.GOOGLE_FIT_PERMISSIONS_REQUEST_CODE -> {
                    myFitness.accessGoogleFit(account)
                }
            }
        }
    }

    override fun onRecieve(response: DataReadResponse) {
        if(response == null){
            return
        }
        for (dataSet in response.buckets.flatMap { it.dataSets }) {
            dumpDataSet(dataSet)
        }
        binding.loading.visibility = View.INVISIBLE
    }
    fun dumpDataSet(dataSet: DataSet) {
        for (dp in dataSet.dataPoints) {
            for (field in dp.dataType.fields) {
                when(field.name){
                    Field.FIELD_STEPS.name-> binding.steps.text = dp.getValue(Field.FIELD_STEPS).asInt().toString() + " steps"
                    Field.FIELD_CALORIES.name -> binding.calories.text = dp.getValue(Field.FIELD_CALORIES).asFloat().toString() + " calories"
                }
            }
        }
    }
}