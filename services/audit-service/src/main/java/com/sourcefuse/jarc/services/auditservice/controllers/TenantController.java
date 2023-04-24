package com.sourcefuse.jarc.services.auditservice.controllers;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sourcefuse.jarc.services.auditservice.models.Tenant;
import com.sourcefuse.jarc.services.auditservice.repositories.TenantRepository;

@RestController
@RequestMapping("/api/tenant")
public class TenantController {

	@Autowired
	TenantRepository tenantRepository;

	@GetMapping("/save")
	public String saveTenant() {
		System.out.println("started to Save the Tenant");
		Tenant tenant = new Tenant();
		tenant.setName("Harshad" + new Date().getTime());
		tenant.setPermissons("Kadam");
		this.tenantRepository.save(tenant);
		System.out.println("Saved the user");
		return "Authorized to access!";
	}
	@GetMapping("/update")
	public String updateTenant() {
		System.out.println("started to Update the Tenant");
		Tenant tenant = this.tenantRepository.findAll().get(0);
		if(tenant!=null) {
			tenant.setName("Updated Name" + new Date().getTime());
			tenant.setPermissons("Tenant");
			this.tenantRepository.save(tenant);
		}
		System.out.println("updated the user");
		return "Authorized to access!";
	}

}
