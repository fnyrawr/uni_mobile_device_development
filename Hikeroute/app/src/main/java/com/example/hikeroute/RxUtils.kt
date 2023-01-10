package com.example.hikeroute

// from tutorial: https://medium.com/huawei-developers/room-database-with-kotlin-mvvm-architecture-477c3ad3c264

import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

fun subscribeOnBackground(function: () -> Unit) {
    Single.fromCallable {
        function()
    }
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe()
}