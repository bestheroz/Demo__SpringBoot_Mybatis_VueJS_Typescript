package com.github.bestheroz.demo.api.sign.in;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TokenDTO {
  private String accessToken;
  private String refreshToken;
}
