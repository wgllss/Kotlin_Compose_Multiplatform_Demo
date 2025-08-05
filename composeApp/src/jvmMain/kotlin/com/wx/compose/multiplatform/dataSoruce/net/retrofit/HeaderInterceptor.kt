package com.wx.compose.multiplatform.dataSoruce.net.retrofit

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

class HeaderInterceptor : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request: Request = chain.request()
        val authorised = request
            .newBuilder()
            .addHeader("Connection", "keep-alive") //
            .addHeader("Accept-Language", "zh-CN,zh;q=0.9") //
            .addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8") //
//            .addHeader("Content-Type", "application/json;charset=utf-8") //
//            .addHeader("Accept-Encoding", "gzip, deflate, br, zstd") //
//            .addHeader("Accept-Language", "zh-CN,zh;q=0.8,zh-TW;q=0.7,zh-HK;q=0.5,en-US;q=0.3,en;q=0.2") //
//            .addHeader("Host", "baike.baidu.com") //
//            .addHeader("Pragma", "no-cache") //
//            .addHeader("Referer", "https://baike.baidu.com/item/%E5%98%89%E5%B3%AA%E5%85%B3/31160") //
//            .addHeader(
//                "Authorization","Bearer eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJtZXJjaGFudElkIjo1NTcsInVzZXJfbmFtZSI6IjE1NzMxNTU0MjY1Iiwib3BlbmlkIjpudWxsLCJzY29wZSI6WyJhbGwiXSwiYWRtaW5JZCI6bnVsbCwiaWQiOjYzOSwiY2xpZW50X3NvdXJjZSI6bnVsbCwiZXhwIjoxNzQ5MTk5MDAzLCJhdXRob3JpdGllcyI6WyJyYmFjXzlf5Yac6LS45ZWG5a62Il0sImp0aSI6ImQ2NmNmNzRmLWZmMDctNDQ1OS1iNzY5LWE2MjI0M2RkOTk3ZSIsImNsaWVudF9pZCI6ImN1c3RvbWVyLWFwcCIsInJlYWxuYW1lIjpudWxsfQ.TNUIo7qEtqJIQZJwfcDa9cqLgsznEpECnJ7UiWPtdzoj5AdClrlC70KC2ANAqjcAFyby9WPZ3hyqxkOSNclGpV0u_94b5QtrnYGv4Agi6zPIcrmcbkHsqRs20RKPzG3nAjcLrZb724zDanEuAm5aYRe7Q8xFDR-jQuZ9AMciYrE"
//            ) //
//            .addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8") //
//            .addHeader("Accept", "application/json, text/plain, */*") //
//            .addHeader("Sec-Fetch-Dest", "document") //
//            .addHeader("Referer", " https://wwz.lanzouj.com/ihT5x20ulecf") //https
//            .addHeader("Upgrade-insecure-Requests", "1") //
            .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/96.0.4664.45 Safari/537.36") //
//        if (request.header("User-Agent") == null) {
//        authorised.addHeader(
//            "Cookie",
//            "BDORZ=FFFB88E999055A3F8A630C64834BD6D0; BAIDUID=3C42F54EC7024F1D1900FBFE390E112F:FG=1; BIDUPSID=B39704A732BE596B95348A18C735CAE0; PSTM=1739969725; H_PS_PSSID=60273_61027_62043_62079_62055_62063_62081_62108_62111_62148_62167_62185_62187_62180_62195; ZFY=485DjNT5CEF:A0gtreTthLzGCX8AnaI3vgqyIMctQsxs:C; H_WISE_SIDS=60273_61027_62079_62055_62063_62111_62148_62167_62185_62187_62180_62195; BA_HECTOR=81208g8gagag0kak2g8l2k018qrcol1jreem71v; baikeVisitId=44ecde47-2bf6-4f47-a8b8-63764a8178e3; BDRCVFR[gZhL2P1o08b]=mk3SLVN4HKm; delPer=0; PSINO=7; BCLID=10779696362298539042; BCLID_BFESS=10779696362298539042; BDSFRCVID=uBAOJexroGWn9AJJRcF9TxntUmKK0gOTDYrEOwXPsp3LGJLVdGYsEG0Pt_NclZAhb5gzogKKXgOTHw0F_2uxOjjg8UtVJeC6EG0Ptf8g0M5; BDSFRCVID_BFESS=uBAOJexroGWn9AJJRcF9TxntUmKK0gOTDYrEOwXPsp3LGJLVdGYsEG0Pt_NclZAhb5gzogKKXgOTHw0F_2uxOjjg8UtVJeC6EG0Ptf8g0M5; H_BDCLCKID_SF=JR-H_K_htK_3H48k-4QEbbQH-UnLqKrnbgOZ04n-ah3RDCTqyx6C-UK13h7X-pQLW20j0h7m3UTKsq76Wh35K5tTQP6rLt3xy674KKJxbU7FHnRLLt7Djt-hhUJiBhvMBan7LKJIXKohJh7FM4tW3J0ZyxomtfQxtNRJ0DnjtpChbCK9D6tKDjJXeU5eetjK2CntsJOOaCvhEljOy4oWK4413UI83xTZLD7lVxbyatblDqvoD-Jc3M04K4o9-hvT-54e2p3FBUQfsUTRQft20b0mXxbQXfo3KJTMXJ7jWhviep72ybt2QlRX5q79atTMfNTJ-qcH0KQpsIJM5-DWbT8IjH62btt_tR-qVI3P; H_BDCLCKID_SF_BFESS=JR-H_K_htK_3H48k-4QEbbQH-UnLqKrnbgOZ04n-ah3RDCTqyx6C-UK13h7X-pQLW20j0h7m3UTKsq76Wh35K5tTQP6rLt3xy674KKJxbU7FHnRLLt7Djt-hhUJiBhvMBan7LKJIXKohJh7FM4tW3J0ZyxomtfQxtNRJ0DnjtpChbCK9D6tKDjJXeU5eetjK2CntsJOOaCvhEljOy4oWK4413UI83xTZLD7lVxbyatblDqvoD-Jc3M04K4o9-hvT-54e2p3FBUQfsUTRQft20b0mXxbQXfo3KJTMXJ7jWhviep72ybt2QlRX5q79atTMfNTJ-qcH0KQpsIJM5-DWbT8IjH62btt_tR-qVI3P; ab_sr=1.0.1_ZTMzNDM0M2VhYTQ0ZDA5YTAwMTBlNDVlNjYxYmE5YzgzY2NkZDYxOTQxNjdlNjgxZWQ4YmYwYTI5ZDQ5MmI2ZDA1MGJjZTYxNTE4NWE1Y2M5MWY3NTU4OGVkOGVkOGRlZDI0ZjI5ZTUyOWE5YjM5YTUyN2I0YTExNTExMmQ4OWNkZGM3ODY2ODFlZjVhMzc4YzdiMjM4MzE3YWViMTVkOQ==; __ymg_scsc=1_a477b1537cdf57444dc0e7439e2e59e0352c8aa1_067b7d7fb"
//        )
//        authorised.addHeader(
//            "User-Agent",
//            "apifox/1.0.0 (https://www.apifox.cn)"
//        ) //
//        }
        return chain.proceed(authorised.build())
//        return chain.proceed(authorised.build())
    }
}