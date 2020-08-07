package com.rojunaid.roacademy.configs;

import com.rojunaid.roacademy.security.CustomUserDetailService;
import com.rojunaid.roacademy.security.JwtAuthenticationFilter;
import com.rojunaid.roacademy.security.RestAuthenticationEntryPoint;
import com.rojunaid.roacademy.security.oauth2.CustomOAuth2UserService;
import com.rojunaid.roacademy.security.oauth2.HttpCookieOAuth2AuthorizationRequestRepository;
import com.rojunaid.roacademy.security.oauth2.OAuth2AuthenticationFailureHandler;
import com.rojunaid.roacademy.security.oauth2.OAuth2AuthenticationSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  @Autowired private RestAuthenticationEntryPoint authenticationEntryPoint;

  @Autowired private CustomUserDetailService customUserDetailService;

  @Autowired private CustomOAuth2UserService customOAuth2UserService;

  @Autowired private OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;

  @Autowired private OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler;

  @Bean
  public HttpCookieOAuth2AuthorizationRequestRepository cookieAuthorizationRequestRepository() {
    return new HttpCookieOAuth2AuthorizationRequestRepository();
  }

  @Bean
  public JwtAuthenticationFilter jwtAuthenticationFilter() {
    return new JwtAuthenticationFilter();
  }

  @Override
  public void configure(AuthenticationManagerBuilder builder) throws Exception {
    builder.userDetailsService(customUserDetailService).passwordEncoder(passwordEncoder());
  }

  @Bean(BeanIds.AUTHENTICATION_MANAGER)
  @Override
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Override
  public void configure(HttpSecurity httpSecurity) throws Exception {
    httpSecurity
        .cors()
        .and()
        .csrf()
        .disable()
        .formLogin()
        .disable()
        .httpBasic()
        .disable()
        .exceptionHandling()
        .authenticationEntryPoint(authenticationEntryPoint)
        .and()
        .sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
        .authorizeRequests()
        .antMatchers(
            "/",
            "/favicon.ico",
            "/**/*.png",
            "/**/*.gif",
            "/**/*.svg",
            "/**/*.jpg",
            "/**/*.html",
            "/**/*.css",
            "/**/*.js")
        .permitAll()
        .antMatchers("/api/auth/**")
        .permitAll()
        .antMatchers("/api/oauth2/**")
        .permitAll()
        .antMatchers(HttpMethod.GET, "/api/categories/**")
        .permitAll()
        .antMatchers(HttpMethod.GET, "/api/grades/**")
        .permitAll()
        .antMatchers(HttpMethod.GET, "/api/courses/**")
        .permitAll()
        .antMatchers(HttpMethod.GET, "/api/lectures/**")
        .permitAll()
        .antMatchers("/api/search/**")
        .permitAll()
        .antMatchers(HttpMethod.GET, "/resources/{\\w+}/{\\d+}/{\\w+}")
        .permitAll()
        .anyRequest()
        .authenticated()
        .and()
        .oauth2Login()
        .authorizationEndpoint()
        .baseUri("/oauth2/authorize")
        .authorizationRequestRepository(cookieAuthorizationRequestRepository())
        .and()
        .redirectionEndpoint()
        .baseUri("/oauth2/callback/*")
        .and()
        .userInfoEndpoint()
        .userService(customOAuth2UserService)
        .and()
        .successHandler(oAuth2AuthenticationSuccessHandler)
        .failureHandler(oAuth2AuthenticationFailureHandler);
    httpSecurity.addFilterBefore(
        jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
  }
}
