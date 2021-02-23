package com.radiostudies.main.client

import okhttp3.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RadioStudiesApiClient(okHttp: OkHttpClient.Builder) : Interceptor, Authenticator {

    private var retrofit: Retrofit

    init {
        okHttp.addInterceptor(this)
        okHttp.authenticator(this)
        retrofit = Retrofit.Builder()
            .client(okHttp.build())
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("http://apiservices.phoenixkm.net/api/")
            .build()
    }

    /**
     *  method to intercept request.
     *  @param chain interceptor.
     */
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()

        request = request.newBuilder()
            .addHeader(HEADER_CONTENT_TYPE, HEADER_VALUE_APP_JSON)
            .addHeader(HEADER_ACCEPT, HEADER_VALUE_APP_JSON)
            .build()
        return chain.proceed(request)
    }

    /**
     * Request authenticator
     * @param route
     * @param response
     */
    override fun authenticate(route: Route?, response: Response): Request? {
        return response.request().newBuilder().build()
    }

    /**
     * Instance call for the retrofit service.
     */
    fun getRadioStudiesResponse(): RadioStudiesApiService {
        return retrofit.create(RadioStudiesApiService::class.java)
    }

    companion object {
        const val HEADER_CONTENT_TYPE = "Content-Type"
        const val HEADER_ACCEPT = "Accept"
        const val HEADER_VALUE_APP_JSON = "application/json"
    }
}
