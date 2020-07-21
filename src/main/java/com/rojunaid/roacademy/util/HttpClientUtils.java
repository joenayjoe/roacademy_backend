package com.rojunaid.roacademy.util;

import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.json.jackson2.JacksonFactory;

public class HttpClientUtils {

  public static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
  public static final JsonFactory JSON_FACTORY = new JacksonFactory();

  public static HttpRequestFactory getRequestFactory() {
    return HTTP_TRANSPORT.createRequestFactory(
        (HttpRequest request) -> {
          request.setParser(new JsonObjectParser(JSON_FACTORY));
        });
  }
}
