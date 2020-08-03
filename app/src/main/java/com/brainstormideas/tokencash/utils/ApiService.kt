package com.brainstormideas.tokencash.utils

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Url

interface ApiService {

    @GET("/v1.0.11/index.php?ACTION=PYXTER_TOKENCASH_EXT1_EXT:C5A.EXT.REGISTRO_SESION.OBTENER_LLAVE")
    fun getToken(): Call<Token>

}