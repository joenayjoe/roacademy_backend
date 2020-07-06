package com.rojunaid.roacademy.auth.oauth2;

import com.nimbusds.oauth2.sdk.util.StringUtils;
import com.rojunaid.roacademy.configs.AppProperties;
import com.rojunaid.roacademy.exception.BadRequestException;
import com.rojunaid.roacademy.exception.OAuth2AuthenticationProcessingException;
import com.rojunaid.roacademy.models.AuthProvider;
import com.rojunaid.roacademy.models.OAuth2Credential;
import com.rojunaid.roacademy.security.oauth2.HttpCookieOAuth2AuthorizationRequestRepository;
import com.rojunaid.roacademy.util.CookieUtils;
import com.rojunaid.roacademy.util.OAuth2Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@Component
public class Auth {
  @Autowired private AppProperties appProperties;
  @Autowired private OAuth2CredentialService oAuth2CredentialService;

  public void authorize(
      AuthProvider authProvider, HttpServletRequest request, HttpServletResponse response) {
    String clientId = this.getClientId(authProvider);
    String authorizationUri = this.getAuthorizationUri(authProvider);
    String authorizationUrl =
        String.format("%s?client_id=%s&response_type=code", authorizationUri, clientId);

    String redirectUriAfterLogin =
        request.getParameter(
            HttpCookieOAuth2AuthorizationRequestRepository.REDIRECT_URI_PARAM_COOKIE_NAME);
    if (StringUtils.isNotBlank(redirectUriAfterLogin)) {
      CookieUtils.addCookie(
          response,
          HttpCookieOAuth2AuthorizationRequestRepository.REDIRECT_URI_PARAM_COOKIE_NAME,
          redirectUriAfterLogin,
          HttpCookieOAuth2AuthorizationRequestRepository.COOKIE_EXPIRATION_IN_SEC);
    }

    try {
      response.sendRedirect(authorizationUrl);
    } catch (Exception e) {
      throw new BadRequestException(e.getLocalizedMessage());
    }
  }

  public void getAccessToken(
      AuthProvider authProvider,
      HttpServletRequest servletRequest,
      HttpServletResponse servletResponse) {

    String code = servletRequest.getParameter("code");
    if (code != null) {
      try {
        TokenResponse tokenResponse =
            OAuth2Utils.getOrRefreshAccessToken(
                this.getClientId(authProvider),
                this.getClientSecret(authProvider),
                "authorization_code",
                code,
                this.getTokenUri(authProvider));

        // save token to db
        OAuth2Credential credential = this.getOAuth2Credential(authProvider).orElse(null);

        if (credential == null) {
          credential = new OAuth2Credential();
        }

        credential.setAccessToken(tokenResponse.getAccessToken());
        credential.setRefreshToken(tokenResponse.getRefreshToken());
        Long currentTimeInSec = System.currentTimeMillis() / 1000;
        credential.setExpiresInSeconds(currentTimeInSec + tokenResponse.getExpiresInSeconds());
        credential.setProvider(authProvider.name());
        oAuth2CredentialService.createOrUpdateCredential(credential);

        Optional<String> redirectUri =
            CookieUtils.getCookie(
                    servletRequest,
                    HttpCookieOAuth2AuthorizationRequestRepository.REDIRECT_URI_PARAM_COOKIE_NAME)
                .map(Cookie::getValue);

        CookieUtils.deleteCookie(
            servletRequest,
            servletResponse,
            HttpCookieOAuth2AuthorizationRequestRepository.REDIRECT_URI_PARAM_COOKIE_NAME);

        if (redirectUri.isPresent()) {
          servletResponse.sendRedirect(redirectUri.get());
        } else {
          throw new BadRequestException(
              "Sorry! We cannot determine the redirect URL. Please refresh the page.");
        }

      } catch (IOException e) {
        throw new OAuth2AuthenticationProcessingException(e.getLocalizedMessage());
      }
    }
  }

  public String getClientId(AuthProvider authProvider) {
    String clientId = null;
    if (authProvider.equals(AuthProvider.box)) {
      clientId = appProperties.getOauth2().getBox().getClientId();
    } else if (authProvider.equals(AuthProvider.imgur)) {
      clientId = appProperties.getOauth2().getImgur().getClientId();
    } else if (authProvider.equals(AuthProvider.youtube)) {
      clientId = appProperties.getOauth2().getYoutube().getClientId();
    }
    return clientId;
  }

  public String getClientSecret(AuthProvider authProvider) {
    String clientSecret = null;
    if (authProvider.equals(AuthProvider.box)) {
      clientSecret = appProperties.getOauth2().getBox().getClientSecret();
    } else if (authProvider.equals(AuthProvider.imgur)) {
      clientSecret = appProperties.getOauth2().getImgur().getClientSecret();
    } else if (authProvider.equals(AuthProvider.youtube)) {
      clientSecret = appProperties.getOauth2().getYoutube().getClientSecret();
    }
    return clientSecret;
  }

  public String getTokenUri(AuthProvider authProvider) {
    String tokenUrl = null;
    if (authProvider.equals(AuthProvider.box)) {
      tokenUrl = appProperties.getOauth2().getBox().getTokenUri();
    } else if (authProvider.equals(AuthProvider.imgur)) {
      tokenUrl = appProperties.getOauth2().getImgur().getTokenUri();
    } else if (authProvider.equals(AuthProvider.youtube)) {
      tokenUrl = appProperties.getOauth2().getYoutube().getTokenUri();
    }
    return tokenUrl;
  }

  public String getAuthorizationUri(AuthProvider authProvider) {
    String authorizationUrl = null;
    if (authProvider.equals(AuthProvider.box)) {
      authorizationUrl = appProperties.getOauth2().getBox().getAuthorizationUri();
    } else if (authProvider.equals(AuthProvider.imgur)) {
      authorizationUrl = appProperties.getOauth2().getImgur().getAuthorizationUri();
    } else if (authProvider.equals(AuthProvider.youtube)) {
      authorizationUrl = appProperties.getOauth2().getYoutube().getAuthorizationUri();
    }
    return authorizationUrl;
  }

  public Optional<OAuth2Credential> getOAuth2Credential(AuthProvider provider) {
    return oAuth2CredentialService.getCredentialByProvider(provider.name());
  }

  public OAuth2Credential updateOAuth2Credential(OAuth2Credential credential) {
    return oAuth2CredentialService.createOrUpdateCredential(credential);
  }
}
