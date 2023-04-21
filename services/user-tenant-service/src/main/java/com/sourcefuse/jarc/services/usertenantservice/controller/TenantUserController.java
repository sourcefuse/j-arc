package com.sourcefuse.jarc.services.usertenantservice.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping(value = {"${api.tenants.user.context.url}"})
@RequiredArgsConstructor
public class TenantUserController {


}
