package com.example.cropmonitor.network

import com.example.cropmonitor.data.api.ModulosApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.security.SecureRandom
import java.security.cert.X509Certificate
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

object RetrofitClient {
    // Reemplaza esta URL por la dirección IP de tu máquina si no usas el emulador
    // Opcional: Para dispositivos físicos, usa https://<tu_ip_local>:7016
    // La URL base debe terminar en /api/ para que los endpoints de las interfaces se resuelvan correctamente.
    private const val BASE_URL = "https://10.0.2.2:7016/api/"

    private val logging = HttpLoggingInterceptor().apply {
        setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    // Código para aceptar certificados SSL no confiables (SOLO PARA DESARROLLO)
    private fun getUnsafeOkHttpClient(): OkHttpClient {
        try {
            val trustAllCerts = arrayOf<TrustManager>(
                object : X509TrustManager {
                    override fun checkClientTrusted(chain: Array<out X509Certificate>?, authType: String?) {}
                    override fun checkServerTrusted(chain: Array<out X509Certificate>?, authType: String?) {}
                    override fun getAcceptedIssuers(): Array<X509Certificate> = arrayOf()
                }
            )

            val sslContext = SSLContext.getInstance("SSL")
            sslContext.init(null, trustAllCerts, SecureRandom())

            val sslSocketFactory = sslContext.socketFactory
            return OkHttpClient.Builder()
                .sslSocketFactory(sslSocketFactory, trustAllCerts[0] as X509TrustManager)
                .hostnameVerifier { _, _ -> true }
                .addInterceptor(logging)
                .build()

        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(getUnsafeOkHttpClient())
        .build()

    // Servicio para la autenticación
    val authService: AuthApiService by lazy {
        retrofit.create(AuthApiService::class.java)
    }

    // Servicio para los módulos, que usa la misma instancia de Retrofit
    val modulosService: ModulosApiService by lazy {
        retrofit.create(ModulosApiService::class.java)
    }
}