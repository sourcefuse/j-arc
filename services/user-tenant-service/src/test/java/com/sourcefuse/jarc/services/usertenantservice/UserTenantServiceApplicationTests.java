package com.sourcefuse.jarc.services.usertenantservice;

import com.sourcefuse.jarc.authlib.utils.JwtUtils;
import com.sourcefuse.jarc.core.models.session.CurrentUser;
import com.sourcefuse.jarc.services.usertenantservice.constant.TestConstants;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class UserTenantServiceApplicationTests {

  @Test
  void contextLoads() {
    assertTrue(true);
  }

  public static void main(String args[]){
    CurrentUser mockCurrentUser = TestConstants.createMockCurrentUser();

    mockCurrentUser.setTenantId(UUID.fromString("debb0722-c49c-49be-8850-35c5eb6687cf"));
    mockCurrentUser.setUserTenantId(UUID.fromString("a13366b3-f938-4c32-aa87-1f1b76d0c0c1"));

    String token= JwtUtils.generateAccessToken("daf66e01593f61a15b857cf433aae03a005812b31234e149036bcc8dee755dbb",
            21600000L, mockCurrentUser);
    System.out.println(token);
  }
}
