package cn.byteout.adminx.modules.system.service.impl;

import cn.byteout.adminx.config.GithubConfig;
import cn.byteout.adminx.modules.system.dto.response.GithubUserInfo;
import cn.byteout.adminx.modules.system.service.GithubAuthService;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import java.util.*;

/**
 * @author antfl
 * @since 2025/8/16
 */
@RequiredArgsConstructor
@Service
public class GithubAuthServiceImpl implements GithubAuthService {

    private static final Logger LOGGER = LoggerFactory.getLogger(GithubAuthServiceImpl.class);
    private final RestTemplate restTemplate;
    private final GithubConfig githubConfig;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String getAccessToken(String code) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("client_id", githubConfig.getClientId());
        params.add("client_secret", githubConfig.getClientSecret());
        params.add("code", code);
        params.add("redirect_uri", githubConfig.getRedirectUri());

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        try {
            LOGGER.debug("Requesting GitHub access token: {}", params);

            ResponseEntity<String> response = restTemplate.postForEntity(
                    githubConfig.getTokenUri(),
                    request,
                    String.class
            );

            LOGGER.debug("GitHub access token response: {}", response.getBody());

            if (response.getStatusCode() == HttpStatus.OK) {
                String json = response.getBody();
                GithubToken githubToken = objectMapper.readValue(json, GithubToken.class);
                return githubToken.getAccessToken();
            } else {
                LOGGER.error("GitHub token API error: {} - {}", response.getStatusCode(), response.getBody());
            }
        } catch (Exception e) {
            LOGGER.error("Error getting GitHub access token", e);
        }
        return null;
    }

    @Override
    public GithubUserInfo getUserInfo(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            LOGGER.debug("Requesting GitHub user info with token: {}", accessToken);

            ResponseEntity<String> response = restTemplate.exchange(
                    githubConfig.getUserInfoUri(),
                    HttpMethod.GET,
                    entity,
                    String.class
            );

            LOGGER.debug("GitHub user info response: {}", response.getBody());

            if (response.getStatusCode() == HttpStatus.OK) {
                String json = response.getBody();
                return objectMapper.readValue(json, GithubUserInfo.class);
            } else {
                LOGGER.error("GitHub user API error: {} - {}", response.getStatusCode(), response.getBody());
            }
        } catch (Exception e) {
            LOGGER.error("Error getting GitHub user info", e);
        }
        return null;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Data
    public static class GithubToken {

        @JsonProperty("access_token")
        private String accessToken;
    }
}
