package cn.byteout.adminx.config;

import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

/**
 * 处理本地调试接口绕过 SSL 校验
 *
 * @author antfl
 * @since 2025/8/16
 */
@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplate() throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        // 创建一个信任所有证书的策略
        TrustStrategy acceptingTrustStrategy = (X509Certificate[] chain, String authType) -> true;

        // 创建 SSL 上下文
        SSLContext sslContext = org.apache.http.ssl.SSLContexts.custom()
                .loadTrustMaterial(null, acceptingTrustStrategy)
                .build();

        // 创建 SSL 连接工厂
        SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(sslContext);

        // 创建 HTTP 客户端
        CloseableHttpClient httpClient = HttpClients.custom()
                .setSSLSocketFactory(csf)
                .build();

        // 创建请求工厂
        HttpComponentsClientHttpRequestFactory requestFactory =
                new HttpComponentsClientHttpRequestFactory();
        requestFactory.setHttpClient(httpClient);

        // 返回配置好的 RestTemplate
        return new RestTemplate(requestFactory);

    }
}
