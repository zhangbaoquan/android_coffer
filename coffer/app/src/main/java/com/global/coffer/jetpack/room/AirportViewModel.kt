package com.global.coffer.jetpack.room

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.global.coffer.CofferApplication
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.*


class AirportViewModel : ViewModel() {

    private val database =
        AirportRoomDatabase.getInstance(CofferApplication.getInstance().baseContext)

    private val airportDao = database.getAirportDao()

    private val airportQueryData: MutableLiveData<AirportEntity?> =
        MutableLiveData<AirportEntity?>()

    fun insertAirport(data: AirportEntity) {
        viewModelScope.launch(viewModelScope.coroutineContext + Dispatchers.IO) {
            airportDao.insertAction(data)
        }
    }


    fun getAirportQueryByIdData(): LiveData<AirportEntity?> {
        return airportQueryData
    }

    // 使用协程
    fun queryByIdData(id: Int) {
        viewModelScope.launch(viewModelScope.coroutineContext + Dispatchers.IO) {
            airportQueryData.postValue(airportDao.queryAirportById(id))
        }
    }

    // 使用RxJava 实现
    @SuppressLint("CheckResult")
    fun queryByIdDataToRx(id: Int) {
        Observable.create { emitter ->
            airportDao.queryAirportByIdRx(id)?.let {
                Log.d("haha_tag", "发送 thread is :" + Thread.currentThread().getName());
                emitter.onNext(it)
                emitter.onComplete()
            }
        }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Consumer<AirportEntity?> {
                override fun accept(data: AirportEntity?) {
                    Log.d("haha_tag", "接收 thread is :" + Thread.currentThread().getName());
                    airportQueryData.value = data
                }
            })
    }

    fun deleteSingleData(data: AirportEntity) {
        viewModelScope.launch(viewModelScope.coroutineContext + Dispatchers.IO) {
            airportDao.deleteAction(data)
        }
    }

    fun deleteAllData() {
        viewModelScope.launch(viewModelScope.coroutineContext + Dispatchers.IO) {
            airportDao.deleteAllAction()
        }
    }

    fun update(data: AirportEntity) {
        viewModelScope.launch(viewModelScope.coroutineContext + Dispatchers.IO) {
            airportDao.updateAction(data)
        }
    }
}