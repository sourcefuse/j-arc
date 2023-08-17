package com.sourcefuse.jarc.services.featuretoggleservice.controller;

import com.sourcefuse.jarc.core.utils.CommonUtils;
import com.sourcefuse.jarc.services.featuretoggleservice.model.FeatureToggle;
import com.sourcefuse.jarc.services.featuretoggleservice.repositories.FeatureToggleRepository;
import jakarta.validation.Valid;
import java.util.Optional;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@AllArgsConstructor
@RequestMapping("/feature-toggle")
public class FeatureToggleController {

  @Autowired
  FeatureToggleRepository featureToggleRepo;

  @GetMapping
  @PreAuthorize("isAuthenticated()")
  public ResponseEntity<Iterable<FeatureToggle>> find() {
    return new ResponseEntity<>(
      this.featureToggleRepo.findAll(),
      HttpStatus.OK
    );
  }

  @GetMapping("/{id}")
  @PreAuthorize("isAuthenticated()")
  public ResponseEntity<Optional<FeatureToggle>> findById(
    @PathVariable UUID id
  ) {
    return new ResponseEntity<>(
      this.featureToggleRepo.findById(id),
      HttpStatus.OK
    );
  }

  @GetMapping("/count")
  @PreAuthorize("isAuthenticated()")
  public ResponseEntity<Long> count() {
    return new ResponseEntity<>(this.featureToggleRepo.count(), HttpStatus.OK);
  }

  @PostMapping
  @PreAuthorize("isAuthenticated()")
  public ResponseEntity<FeatureToggle> create(
    @Valid @RequestBody FeatureToggle featureToggle
  ) {
    return new ResponseEntity<>(
      this.featureToggleRepo.save(featureToggle),
      HttpStatus.CREATED
    );
  }

  @DeleteMapping("/{id}")
  @PreAuthorize("isAuthenticated()")
  public ResponseEntity<String> delete(@PathVariable("id") UUID id) {
    this.featureToggleRepo.deleteById(id);
    return new ResponseEntity<>("Delete success", HttpStatus.OK);
  }

  @PatchMapping("/{id}")
  @PreAuthorize("isAuthenticated()")
  public ResponseEntity<String> updateById(
    @PathVariable("id") UUID id,
    @Valid @RequestBody FeatureToggle featureToggle
  ) {
    FeatureToggle targetFeatureToggle =
      this.featureToggleRepo.findById(id)
        .orElseThrow(() ->
          new ResponseStatusException(
            HttpStatus.NOT_FOUND,
            "No group is present against given value"
          )
        );

    BeanUtils.copyProperties(
      featureToggle,
      targetFeatureToggle,
      CommonUtils.getNullPropertyNames(featureToggle)
    );

    this.featureToggleRepo.save(targetFeatureToggle);
    return new ResponseEntity<>("Feature PATCH Success", HttpStatus.NO_CONTENT);
  }
}
