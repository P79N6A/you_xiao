package com.runtoinfo.httpUtils.utils;

import android.content.Context;

import java.io.InputStream;
import java.security.KeyStore;
import java.util.Collections;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;

import okhttp3.OkHttpClient;
import okhttp3.Protocol;

/**
 * Created by QiaoJunChao on 2018/9/20.
 */
@SuppressWarnings("all")
public class HttpsSettings {

    /**
     * 设置ihttps证书验证
     */
    public static OkHttpClient setCertificates(Context context) {
        OkHttpClient.Builder mOkHttpClient = new OkHttpClient.Builder();
        try {
            //将ca证书导入输入流
            InputStream inputStream = context.getResources().getAssets().open("youxiao.pfx");

            //keystore添加证书内容和密码
            KeyStore keyStore = KeyStore.getInstance("PKCS12");
            keyStore.load(inputStream, "214973017970562".toCharArray());

//            //证书工厂类，生成证书
//            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
//            //生成证书，添加别名
//            keyStore.setCertificateEntry("youxiao", certificateFactory.generateCertificate(inputStream));

//            //信任管理器工厂
//            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
//            trustManagerFactory.init(keyStore);

            //key管理器工厂
            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            keyManagerFactory.init(keyStore, "214973017970562".toCharArray());


            //构建一个ssl上下文，加入ca证书格式，与后台保持一致
            SSLContext sslContext = SSLContext.getInstance("TLS");
            //参数，添加受信任证书和生成随机数
            sslContext.init(keyManagerFactory.getKeyManagers(), null, null);//new SecureRandom()

            //获得scoket工厂
            SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
            mOkHttpClient.sslSocketFactory(sslSocketFactory)
                    //将自定义SSLFactory加载到OKhttpClient,context对象就是Android 系统中常用的那个
                    .retryOnConnectionFailure(true)
                    .protocols(Collections.singletonList(Protocol.HTTP_1_1))
                    //此处将hostnameVerifier 验证关闭掉,会使SSL的安全性降低,如果想要使用这个验证,请不要使用私签证书,注释掉下面这段代码,运行体验一下
                    //设置ip授权认证：如果已经安装该证书，可以不设置，否则需要设置
                    .hostnameVerifier(new HostnameVerifier() {
                        @Override
                        public boolean verify(String hostname, SSLSession session) {
                            return true;
                        }
                    });

            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mOkHttpClient.build();
    }
}
