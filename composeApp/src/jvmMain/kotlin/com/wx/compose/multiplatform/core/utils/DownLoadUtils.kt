package com.wx.compose.multiplatform.core.utils

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import okio.buffer
import okio.sink
import okio.source
import java.io.File
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import java.security.SecureRandom
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.HttpsURLConnection
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager
import kotlin.system.measureTimeMillis
import kotlin.time.measureTime

class DownLoadUtils {


    companion object {

        val instance by lazy { DownLoadUtils() }

        private var mSSLSocketFactory: SSLSocketFactory? = null

        /**
         * 信任所有host
         */
        private val hnv = HostnameVerifier { _, _ -> true }

        /**
         * 设置https
         *
         * @author :Atar
         * @createTime:2015-9-17下午4:57:39
         * @version:1.0.0
         * @modifyTime:
         * @modifyAuthor:
         * @description:
         */
        private fun trustAllHosts() {
            try {
                val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
                    override fun getAcceptedIssuers(): Array<X509Certificate> {
                        return arrayOf()
                    }

                    @Throws(CertificateException::class)
                    override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {
                    }

                    @Throws(CertificateException::class)
                    override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {
                    }
                })
                val sc = SSLContext.getInstance("TLS")
                sc.init(null, trustAllCerts, SecureRandom())
                if (mSSLSocketFactory == null) {
                    mSSLSocketFactory = sc.socketFactory
                }
                HttpsURLConnection.setDefaultHostnameVerifier(hnv)
                HttpsURLConnection.setDefaultSSLSocketFactory(mSSLSocketFactory)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        private fun getHttpURLConnection(url: URL, connectTimeOut: Int): HttpURLConnection? {
            return try {
                if ("https" == url.protocol) {
                    trustAllHosts()
                    (url.openConnection() as HttpsURLConnection).apply {
                        HttpsURLConnection.setDefaultHostnameVerifier(hnv)
                        hostnameVerifier = hnv
                        HttpsURLConnection.setDefaultSSLSocketFactory(mSSLSocketFactory)
                        sslSocketFactory = mSSLSocketFactory
                        connectTimeout = 3 * connectTimeOut
                        readTimeout = 3 * connectTimeOut
                    }
                } else {
                    (url.openConnection() as HttpURLConnection).apply {
                        connectTimeout = connectTimeOut
                        readTimeout = connectTimeOut
                    }
                }
            } catch (e: Exception) {
                null
            }
        }

        fun setConHead(httpConnection: HttpURLConnection) {
            httpConnection.apply {
                setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
                setRequestProperty("Upgrade-Insecure-Requests", "1")
                setRequestProperty("User-Agent", "Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/113.0.0.0 Mobile Safari/537.36")
                setRequestProperty("Accept-Language", "en-us,en;q=0.7,zh-cn;q=0.3")
//            httpConnection.setRequestProperty("Accept-Encoding", "gzip, deflate, br")
                setRequestProperty("Accept-Charset", "ISO-8859-1,utf-8;q=0.7,*;q=0.7")
                setRequestProperty("Keep-Alive", "300")
                setRequestProperty("Connection", "keep-alive")
                setRequestProperty("Cache-Control", "max-age=0")
            }
        }
    }

    fun download(downUrl: String, file: File) {
        if (file.exists()) return
        var conn: HttpURLConnection? = null
        try {
            val url = URL(downUrl)
            conn = getHttpURLConnection(url, 5000)!!
            val inputStream = conn.inputStream
            if (inputStream != null) {
                val sinkBuffer = file.sink().buffer()
                val bufferedSource = inputStream.source().buffer()
                sinkBuffer.write(bufferedSource.readByteArray())
                sinkBuffer.close()
                bufferedSource.close()
                inputStream.close()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            conn?.disconnect()
        }
    }

    suspend fun WXDownload(downUrl: String, file: File) = coroutineScope {
        if (file.exists()) return@coroutineScope
//        if (file.exists()) return
        var conn: HttpURLConnection? = null
        try {
            println("WXDownload $downUrl ${Thread.currentThread().name}  开始 ")

            val url = URL(downUrl)
            conn = getHttpURLConnection(url, 5000)!!
            val inputStream = conn.inputStream
            if (inputStream != null) {
                val sinkBuffer = file.sink().buffer()
                val bufferedSource = inputStream.source().buffer()
                sinkBuffer.write(bufferedSource.readByteArray())
                sinkBuffer.close()
                bufferedSource.close()
                inputStream.close()
            }

        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            conn?.disconnect()
        }
    }

     fun WXDownload2(downUrl: String, file: File)  {
        if (file.exists()) return
        var conn: HttpURLConnection? = null
        try {
            val url = URL(downUrl)
            conn = getHttpURLConnection(url, 30000)!!
            val inputStream = conn.inputStream
            if (inputStream != null) {
                val sinkBuffer = file.sink().buffer()
                val bufferedSource = inputStream.source().buffer()
                sinkBuffer.write(bufferedSource.readByteArray())
                sinkBuffer.close()
                bufferedSource.close()
                inputStream.close()
            }

        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            conn?.disconnect()
        }
    }
}
