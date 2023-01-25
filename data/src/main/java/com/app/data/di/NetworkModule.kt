package com.app.data.di

import android.content.Context
import com.app.data.BuildConfig
import com.app.data.R
import com.app.data.base.network.interceptors.TokenManager
import com.app.data.features.service.MainService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.io.InputStream
import java.security.GeneralSecurityException
import java.security.KeyStore
import java.security.SecureRandom
import java.security.cert.Certificate
import java.security.cert.CertificateFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton
import javax.net.ssl.*

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val CERTIFICATION_TYPE = "X.509"

    private const val TIMEOUT_SEC = 60L

    //Key names
    private const val USER_AGENT_KEY = "User-Agent"
    private const val ACCEPT_KEY = "Accept"

    //Key values
    private const val USER_AGENT_VALUE = "Android"
    private const val ACCEPT_VALUE = "application/json"

    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BODY)
    }


    @Provides
    @Singleton
    fun provideOkHttpClient(
        logging: HttpLoggingInterceptor,
        sslContext: SSLContext,
        trustManager: TrustManagerFactory,
        tokenManager: TokenManager
    ): OkHttpClient {
        val okHttpClient = OkHttpClient.Builder()
            .readTimeout(TIMEOUT_SEC, TimeUnit.SECONDS)
            .connectTimeout(TIMEOUT_SEC, TimeUnit.SECONDS)
            .writeTimeout(TIMEOUT_SEC, TimeUnit.SECONDS)
            .addInterceptor { chain: Interceptor.Chain ->
                val original = chain.request()
                val request = original.newBuilder()
                    .header(USER_AGENT_KEY, USER_AGENT_VALUE)
                    .header(ACCEPT_KEY, ACCEPT_VALUE)
                    .method(original.method, original.body)
                    .build()
                chain.proceed(request)
            }.sslSocketFactory(
                sslContext.socketFactory,
                trustManager.trustManagers[0] as X509TrustManager
            )

        if (BuildConfig.DEBUG) {
            okHttpClient.addInterceptor(logging)
        }
        okHttpClient.addInterceptor(tokenManager)

        return okHttpClient.build()
    }


    @Provides
    @Singleton
    @Named("unsafe")
    fun provideUnsafeOkHttpClient(
        logging: HttpLoggingInterceptor,
    ): OkHttpClient {
        val okHttpClient = OkHttpClient.Builder()
            .readTimeout(TIMEOUT_SEC, TimeUnit.SECONDS)
            .connectTimeout(TIMEOUT_SEC, TimeUnit.SECONDS)
            .writeTimeout(TIMEOUT_SEC, TimeUnit.SECONDS)
            .addInterceptor { chain: Interceptor.Chain ->
                val original = chain.request()
                val request = original.newBuilder()
                    .header(ACCEPT_KEY, ACCEPT_VALUE)
                    .method(original.method, original.body)
                    .build()
                chain.proceed(request)
            }

        okHttpClient.hostnameVerifier(HostnameVerifier { hostname, session ->
            return@HostnameVerifier true
        })

        if (BuildConfig.DEBUG) {
            okHttpClient.addInterceptor(logging)
        }

        return okHttpClient.build()
    }



    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }



    @Provides
    @Singleton
    fun provideAuthService(retrofit: Retrofit): MainService =
        retrofit.create(MainService::class.java)


    @Provides
    @Singleton
    fun provideSSLContext(@ApplicationContext context: Context): SSLContext {
        val clientCert = context.resources.openRawResource(R.raw.test)
        return try {
            val certificateFactory: CertificateFactory = CertificateFactory.getInstance(
                CERTIFICATION_TYPE
            )
            val certificates: MutableCollection<out Certificate>? =
                certificateFactory.generateCertificates(clientCert)
//            require(
//                certificates?.isEmpty()?.not() == true
//            ) { "expected non-empty set of trusted certificates" }
            val password = "password".toCharArray()
            val keyStore: KeyStore = try {
                val keyStore = KeyStore.getInstance(KeyStore.getDefaultType())
                val `in`: InputStream? = null
                keyStore.load(`in`, password)
                keyStore
            } catch (e: IOException) {
                throw AssertionError(e)
            }
            var index = 0
            for (certificate in certificates!!) {
                val certificateAlias = (index++).toString()
                keyStore.setCertificateEntry(certificateAlias, certificate)
            }
            val keyManagerFactory = KeyManagerFactory.getInstance(
                KeyManagerFactory.getDefaultAlgorithm()
            )
            keyManagerFactory.init(keyStore, password)
            val trustManagerFactory = TrustManagerFactory.getInstance(
                TrustManagerFactory.getDefaultAlgorithm()
            )
            trustManagerFactory.init(keyStore)
            val sslContext = SSLContext.getInstance("TLS")
            sslContext.init(
                keyManagerFactory.keyManagers, trustManagerFactory.trustManagers,
                SecureRandom()
            )
            sslContext
        } catch (e: GeneralSecurityException) {
            throw RuntimeException(e)
        }
    }


    @Provides
    @Singleton
    fun provideTrustManager(@ApplicationContext context: Context): TrustManagerFactory {
        val clientCert = context.resources.openRawResource(R.raw.test)

        return try {
            val certificateFactory: CertificateFactory = CertificateFactory.getInstance("X.509")
            val certificates: MutableCollection<out Certificate>? =
                certificateFactory.generateCertificates(clientCert)
//            require(!certificates!!.isEmpty()) { "expected non-empty set of trusted certificates" }
            val password = "password".toCharArray()
            val keyStore: KeyStore = try {
                val keyStore = KeyStore.getInstance(KeyStore.getDefaultType())
                val `in`: InputStream? = null
                keyStore.load(`in`, password)
                keyStore
            } catch (e: IOException) {
                throw AssertionError(e)
            }
            var index = 0
            if (certificates != null) {
                for (certificate in certificates) {
                    val certificateAlias = (index++).toString()
                    keyStore.setCertificateEntry(certificateAlias, certificate)
                }
            }
            val keyManagerFactory = KeyManagerFactory.getInstance(
                KeyManagerFactory.getDefaultAlgorithm()
            )
            keyManagerFactory.init(keyStore, password)
            val trustManagerFactory = TrustManagerFactory.getInstance(
                TrustManagerFactory.getDefaultAlgorithm()
            )
            trustManagerFactory.init(keyStore)
            return trustManagerFactory
        } catch (e: GeneralSecurityException) {
            throw RuntimeException(e)
        }
    }


}