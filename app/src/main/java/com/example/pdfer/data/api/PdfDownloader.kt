package com.example.pdfer.data.api

import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Streaming
import retrofit2.http.Url

/**
 *  Making HTTP request to download pdf file from network
 */


interface PdfDownloader {
    @Streaming
    @GET
    suspend fun downloadPdf(@Url url: String) : Response<ResponseBody>

    companion object{
        val instance: PdfDownloader by lazy {

            val logging = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }

            val client = OkHttpClient.Builder().addInterceptor(logging).build()

            //baseurl is changing at run time
            Retrofit.Builder()
                .client(client)
                .baseUrl("http://localhost/")
                .build()
                .create(PdfDownloader::class.java)

        }
    }

}