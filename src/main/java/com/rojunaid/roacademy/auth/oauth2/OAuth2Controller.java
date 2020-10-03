package com.rojunaid.roacademy.auth.oauth2;

import com.rojunaid.roacademy.models.AuthProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/oauth2")
public class OAuth2Controller {

  @Autowired private Auth authProvider;

  @GetMapping("/authorize/box")
  public void authorizeBox(HttpServletRequest request, HttpServletResponse response) {
    authProvider.authorize(AuthProvider.box, request, response);
  }

  @GetMapping("/callback/box")
  public void boxCallback(HttpServletRequest request, HttpServletResponse response) {
    authProvider.getAccessToken(AuthProvider.box, request, response);
  }

  @GetMapping("/authorize/imgur")
  public void authorizeImgur(HttpServletRequest request, HttpServletResponse response) {
    authProvider.authorize(AuthProvider.imgur, request, response);
  }

  @GetMapping("/callback/imgur")
  public void imgurCallback(HttpServletRequest request, HttpServletResponse response) {
    authProvider.getAccessToken(AuthProvider.imgur, request, response);
  }

  @GetMapping("/authorize/youtube")
  public void authorizeYoutube(HttpServletRequest request, HttpServletResponse response) {
    authProvider.authorize(AuthProvider.youtube, request, response);
  }

  @GetMapping("/callback/youtube")
  public void getAccessToken(HttpServletRequest request, HttpServletResponse response) {
    authProvider.getAccessToken(AuthProvider.youtube, request, response);
  }
}
