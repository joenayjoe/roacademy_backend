package com.rojunaid.roacademy.dto;

import lombok.Data;

@Data
public class JwtAuthenticationResponseDTO {

  private String accessToken;

  private String tokenType = "Bearer";
}
