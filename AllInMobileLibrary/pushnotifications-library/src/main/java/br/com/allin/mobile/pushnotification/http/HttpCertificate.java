package br.com.allin.mobile.pushnotification.http;

import android.util.Log;

import java.security.SecureRandom;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * Class responsible for generating the certificate Https
 */
abstract class HttpCertificate {
    static void generateCertificate() {
        try {
            TrustManager[] trustManagerArray = new TrustManager[] {
                    new X509TrustManager() {

                        public X509Certificate[] getAcceptedIssuers() {
                            return new X509Certificate[0];
                        }

                        @Override
                        public void checkClientTrusted(X509Certificate[] certs, String authType) {
                            checkTrusted(certs, authType);
                        }

                        @Override
                        public void checkServerTrusted(X509Certificate[] certs, String authType) {
                            checkTrusted(certs, authType);
                        }

                        private void checkTrusted(X509Certificate[] chain, String authType) {
                            if (authType == null || authType.length() == 0) {
                                Log.e("AlliNCertificate", "Null or zero-length authentication type");
                            }

                            try {
                                chain[0].checkValidity();
                            } catch (Exception e) {
                                Log.e("AlliNCertificate", "Invalid certificate");
                            }
                        }
                    }
            };

            SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustManagerArray, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier((s, sslSession) -> true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
