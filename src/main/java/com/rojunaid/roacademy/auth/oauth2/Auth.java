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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class Auth {
  @Autowired private AppProperties appProperties;
  @Autowired private OAuth2CredentialService oAuth2CredentialService;

  private static final Logger LOGGER = LoggerFactory.getLogger(Auth.class);

  public void authorize(
      AuthProvider authProvider, HttpServletRequest request, HttpServletResponse response) {
    String clientId = this.getClientId(authProvider);
    String authorizationUri = this.getAuthorizationUri(authProvider);

    List<String> scopes = this.getScopes(authProvider);
    String redirectUri = this.getRedirectUri(authProvider);

    String authorizationUrl =
        String.format("%s?client_id=%s&response_type=code", authorizationUri, clientId);

    if (scopes != null && scopes.size() > 0) {
      String sc = scopes.stream().collect(Collectors.joining(" "));
      authorizationUrl = String.format("%s&scope=%s", authorizationUrl, sc);
    }

    if (redirectUri != null) {
      authorizationUrl = String.format("%s&redirect_uri=%s", authorizationUrl, redirectUri);
    }

    if (authProvider.equals(AuthProvider.youtube)) {
      authorizationUrl =
          String.format("%s&access_type=offline&include_granted_scopes=true", authorizationUrl);
    }

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
      OAuth2RequestParams params = new OAuth2RequestParams();
      params.setClientId(this.getClientId(authProvider));
      params.setClientSecret(this.getClientSecret(authProvider));
      params.setGrantType("authorization_code");
      params.setCodeOrToken(code);
      params.setAuthOrTokenUrl(this.getTokenUri(authProvider));
      params.setRedirectUrl(this.getRedirectUri(authProvider));
      try {
        TokenResponse tokenResponse = OAuth2Utils.getOrRefreshAccessToken(params);

        System.out.println(("##### OAUTH2 Response ######"));
        System.out.println(("##### Access Token = "+ tokenResponse.getAccessToken()));
        System.out.println(("##### Refresh Token = "+ tokenResponse.getRefreshToken()));
        System.out.println("######  END ######");
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

  public List<String> getScopes(AuthProvider authProvider) {
    if (authProvider.equals(AuthProvider.youtube)) {
      return appProperties.getOauth2().getYoutube().getScopes();
    }
    return null;
  }

  public String getRedirectUri(AuthProvider authProvider) {
    if (authProvider.equals(AuthProvider.youtube)) {
      return appProperties.getOauth2().getYoutube().getRedirectUri();
    }
    return null;
  }

  public Optional<OAuth2Credential> getOAuth2Credential(AuthProvider provider) {
    return oAuth2CredentialService.getCredentialByProvider(provider.name());
  }

  public OAuth2Credential updateOAuth2Credential(OAuth2Credential credential) {
    return oAuth2CredentialService.createOrUpdateCredential(credential);
  }
}
