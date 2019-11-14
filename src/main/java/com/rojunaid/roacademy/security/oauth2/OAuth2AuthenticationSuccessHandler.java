package com.rojunaid.roacademy.security.oauth2;

import com.rojunaid.roacademy.configs.AppProperties;
import com.rojunaid.roacademy.exception.BadRequestException;
import com.rojunaid.roacademy.security.JwtTokenProvider;
import com.rojunaid.roacademy.util.CookieUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.util.Optional;

@Component
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
  private JwtTokenProvider tokenProvider;
  private AppProperties appProperties;

  private HttpCookieOAuth2AuthorizationRequestRepository
      httpCookieOAuth2AuthorizationRequestRepository;

  @Autowired
  OAuth2AuthenticationSuccessHandler(
      JwtTokenProvider tokenProvider,
      AppProperties appProperties,
      HttpCookieOAuth2AuthorizationRequestRepository
          httpCookieOAuth2AuthorizationRequestRepository) {
    this.tokenProvider = tokenProvider;
    this.appProperties = appProperties;
    this.httpCookieOAuth2AuthorizationRequestRepository =
        httpCookieOAuth2AuthorizationRequestRepository;
  }

  @Override
  public void onAuthenticationSuccess(
      HttpServletRequest request, HttpServletResponse response, Authentication authentication)
      throws IOException, ServletException {
    String targetUrl = determineTargetUrl(request, response, authentication);

    if (response.isCommitted()) {
      logger.debug("Response has already been committed. Unable to redirect to " + targetUrl);
      return;
    }

    clearAuthenticationAttributes(request, response);
    getRedirectStrategy().sendRedirect(request, response, targetUrl);
  }

  protected String determineTargetUrl(
      HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
    Optional<String> redirectUri =
        CookieUtils.getCookie(
                request,
                HttpCookieOAuth2AuthorizationRequestRepository.REDIRECT_URI_PARAM_COOKIE_NAME)
            .map(Cookie::getValue);

    if (redirectUri.isPresent() && !isAuthorizedRedirecUri(redirectUri.get())) {
      throw new BadRequestException(
          "Sorry! We've got an Unauthorized URI an cannot proceed with the authentication");
    }

    String targetUrl = redirectUri.orElse(getDefaultTargetUrl());
    String token = tokenProvider.generateToken(authentication);
    return UriComponentsBuilder.fromUriString(targetUrl)
        .queryParam("token", token)
        .build()
        .toUriString();
  }

  protected void clearAuthenticationAttributes(
      HttpServletRequest request, HttpServletResponse response) {
    super.clearAuthenticationAttributes(request);
    httpCookieOAuth2AuthorizationRequestRepository.removeAuthorizationRequestCookie(
        request, response);
  }

  private boolean isAuthorizedRedirecUri(String uri) {
    URI clientRedirectUri = URI.create(uri);

    return appProperties.getOauth2().getAuthorizedRedirectUris().stream()
        .anyMatch(
            authorizedRedirectUri -> {
              URI authorizedURI = URI.create(authorizedRedirectUri);
              if (authorizedURI.getHost().equalsIgnoreCase(clientRedirectUri.getHost())
                  && authorizedURI.getPort() == clientRedirectUri.getPort()) {
                return true;
              }
              return false;
            });
  }
}
