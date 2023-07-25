package com.basic.example.authserviceexample.controller;

import com.basic.example.authserviceexample.dto.ValidateDto;
import com.basic.example.authserviceexample.model.Invitation;
import com.basic.example.authserviceexample.repository.InvitationRepository;
import com.sourcefuse.jarc.core.utils.CommonUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@RestController
@Slf4j
@RequestMapping("/invitation")
@RequiredArgsConstructor
public class InvitationExampleController {

    private final InvitationRepository invitationRepository;

    @PostMapping
    public ResponseEntity<Invitation> sendInvitationLink(@Valid @RequestBody Invitation invitation) {
        log.info("Called by Facade service to save invitation records  and send invitation ");
        return new ResponseEntity<>(invitationRepository.save(invitation), HttpStatus.CREATED);
    }

    @GetMapping("/validate/{id}")
    public ResponseEntity<ValidateDto> validateInvitationLink(@PathVariable UUID id) {
        Invitation invitation=invitationRepository.findById(id).orElseThrow(()->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Records does not exits against Id"+id));
        return new ResponseEntity<>(ValidateDto.builder().isValid
                (CommonUtils.calculateTimeDiff(invitation.getExpires())).build(), HttpStatus.CREATED);
    }
}