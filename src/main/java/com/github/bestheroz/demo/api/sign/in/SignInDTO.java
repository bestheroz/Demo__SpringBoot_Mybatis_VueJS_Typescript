package com.github.bestheroz.demo.api.sign.in;

import javax.validation.constraints.NotEmpty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SignInDTO {
  @NotEmpty private String adminId;
  @NotEmpty private String password;
}
