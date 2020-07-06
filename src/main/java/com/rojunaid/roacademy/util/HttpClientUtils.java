package com.rojunaid.roacademy.util;

import com.google.api.client.http.*;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.http.json.JsonHttpContent;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.json.jackson2.JacksonFactory;

import java.io.IOException;
import java.util.Map;

public class HttpClientUtils {

  public static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
  public static final JsonFactory JSON_FACTORY = new JacksonFactory();

  public static HttpRequestFactory httpRequestFactory =
      HTTP_TRANSPORT.createRequestFactory(
          (HttpRequest request) -> {
            request.setParser(new JsonObjectParser(JSON_FACTORY));
          });

  public static HttpResponse post(String url, Map<String, Object> params, HttpHeaders headers)
      throws IOException {

    GenericUrl requestUrl = new GenericUrl(url);
    HttpContent content = new JsonHttpContent(JSON_FACTORY, params);

    HttpRequest request =
        httpRequestFactory.buildPostRequest(requestUrl, content).setHeaders(headers);
    HttpResponse response = request.execute();
    return response;
  }
}
