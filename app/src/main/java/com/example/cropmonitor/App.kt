package com.example.cropmonitor

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModelProvider
import com.example.cropmonitor.auth.TokenManager
import com.example.cropmonitor.data.api.CultivosApiService
import com.example.cropmonitor.data.repository.CultivosRepository
import com.example.cropmonitor.data.api.ModulosApiService
import com.example.cropmonitor.data.repository.ModulosRepository
import com.example.cropmonitor.data.repository.UsuariosRepository
import com.example.cropmonitor.data.api.UsuariosApiService // <-- Nueva importación
import com.example.cropmonitor.network.AuthApiService
import com.example.cropmonitor.viewmodels.ModulosViewModelFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.security.SecureRandom
import java.security.cert.X509Certificate
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager
import com.example.cropmonitor.data.repository.NetworkRecetasRepository
import com.example.cropmonitor.data.repository.RecetasRepository
import com.example.cropmonitor.data.api.RecetasApiService
import com.example.cropmonitor.viewmodels.RecetasViewModelFactory

/**
 * Interfaz que define el contenedor de dependencias de la aplicación.
 */
interface AppContainer {
    val modulosRepository: ModulosRepository
    val authApiService: AuthApiService
    val modulosApiService: ModulosApiService
    val modulosViewModelFactory: ViewModelProvider.Factory

    // Agregado: Dependencias para la funcionalidad de cultivos
    val cultivosApiService: CultivosApiService
    val cultivosRepository: CultivosRepository

    val usuariosApiService: UsuariosApiService // <-- Nuevo servicio
    val usuariosRepository: UsuariosRepository // <-- Nuevo repositorio

    val recetasRepository: RecetasRepository
    val recetasViewModelFactory: RecetasViewModelFactory
}


/**
 * Implementación de [AppContainer] que crea las instancias de las dependencias.
 */
class DefaultAppContainer(private val context: Context) : AppContainer {
    private val BASE_URL = "https://10.0.2.2:7016/api/"

    private val client: OkHttpClient by lazy {
        val logging = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }

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

        OkHttpClient.Builder()
            .sslSocketFactory(sslSocketFactory, trustAllCerts[0] as X509TrustManager)
            .hostnameVerifier { _, _ -> true }
            .addInterceptor { chain ->
                val token = TokenManager(context).getToken()
                val originalRequest = chain.request()
                val newRequest = originalRequest.newBuilder().apply {
                    if (token != null) {
                        header("Authorization", "Bearer $token")
                    }
                }.build()
                chain.proceed(newRequest)
            }
            .addInterceptor(logging)
            .build()
    }

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // Servicio para la API de módulos.
    override val modulosApiService: ModulosApiService by lazy {
        retrofit.create(ModulosApiService::class.java)
    }

    // Servicio para la API de autenticación.
    override val authApiService: AuthApiService by lazy {
        retrofit.create(AuthApiService::class.java)
    }

    // Repositorio para los módulos.
    override val modulosRepository: ModulosRepository by lazy {
        ModulosRepository(modulosApiService)
    }

    // Agregado: Servicio para la API de cultivos
    override val cultivosApiService: CultivosApiService by lazy {
        retrofit.create(CultivosApiService::class.java)
    }

    // Agregado: Repositorio para cultivos
    override val cultivosRepository: CultivosRepository by lazy {
        CultivosRepository(cultivosApiService)
    }

    // <-- NUEVA IMPLEMENTACIÓN DE USUARIOS REPOSITORY
    // Servicio para la API de usuarios.
    override val usuariosApiService: UsuariosApiService by lazy {
        retrofit.create(UsuariosApiService::class.java)
    }

    // Repositorio para los usuarios.
    override val usuariosRepository: UsuariosRepository by lazy {
        UsuariosRepository(usuariosApiService)
    }

    // Fábrica de ViewModels.
    override val modulosViewModelFactory: ViewModelProvider.Factory by lazy {
        ModulosViewModelFactory(
            modulosRepository = modulosRepository,
            authService = authApiService,
            tokenManager = TokenManager(context),
            cultivosRepository = cultivosRepository,
            usuariosRepository = usuariosRepository // <-- Ahora esta dependencia ya está disponible
        )
    }

    // Agrega el servicio y el repositorio de recetas
    private val recetasApiService: RecetasApiService by lazy {
        retrofit.create(RecetasApiService::class.java)
    }

    override val recetasRepository: RecetasRepository by lazy {
        NetworkRecetasRepository(recetasApiService)
    }

    // Agrega la factoría del ViewModel de recetas
    override val recetasViewModelFactory: RecetasViewModelFactory by lazy {
        RecetasViewModelFactory(recetasRepository)
    }
}

/**
 * Clase principal de la aplicación.
 * Esta clase hereda de [Application] y es el punto de entrada para inicializar
 * el contenedor de dependencias, asegurando que esté disponible en toda la app.
 */
class App : Application() {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer(this)
    }
}