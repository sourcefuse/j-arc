package com.sourcefuse.jarc.services.featuretoggleservice.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sourcefuse.jarc.services.featuretoggleservice.model.FeatureList;
import com.sourcefuse.jarc.services.featuretoggleservice.repositories.FeatureListRepository;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/feature")
public class FeatureController {

	@Autowired
	FeatureListRepository featureListRepo;

	@GetMapping
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<Iterable<FeatureList>> find() {
		return new ResponseEntity<>(this.featureListRepo.findAll(), HttpStatus.OK);
	}

	@GetMapping("/count")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<Long> count() {
		return new ResponseEntity<>(this.featureListRepo.count(), HttpStatus.OK);
	}

	@PostMapping
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<FeatureList> create(@Valid @RequestBody FeatureList feature) {
		return new ResponseEntity<>(this.featureListRepo.save(feature), HttpStatus.CREATED);
	}

	@DeleteMapping("/delete")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<String> delete(@PathVariable("id") UUID id) {
		this.featureListRepo.deleteById(id);
		return new ResponseEntity<>("Delete success", HttpStatus.OK);
	}

}
