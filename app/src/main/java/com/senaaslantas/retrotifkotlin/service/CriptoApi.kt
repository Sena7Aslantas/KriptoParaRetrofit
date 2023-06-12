package com.senaaslantas.retrotifkotlin.service


import com.senaaslantas.retrotifkotlin.model.CriptoModel
import io.reactivex.Observable
import retrofit2.http.GET

interface CriptoApi {
    @GET("atilsamancioglu/K21-JSONDataSet/master/crypto.json")
    fun getData():Observable<List<CriptoModel>>


}