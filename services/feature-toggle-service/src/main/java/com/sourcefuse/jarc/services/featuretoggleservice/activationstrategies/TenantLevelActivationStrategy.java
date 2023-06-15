package com.sourcefuse.jarc.services.featuretoggleservice.activationstrategies;

import java.util.List;

import org.springframework.security.core.context.SecurityContextHolder;
import org.togglz.core.activation.Parameter;
import org.togglz.core.activation.ParameterBuilder;
import org.togglz.core.repository.FeatureState;
import org.togglz.core.spi.ActivationStrategy;
import org.togglz.core.user.FeatureUser;
import org.togglz.core.util.Strings;

import com.sourcefuse.jarc.services.authservice.session.CurrentUser;
import com.sourcefuse.jarc.services.featuretoggleservice.enums.StrategyEnums;

public class TenantLevelActivationStrategy implements ActivationStrategy {

	private static final StrategyEnums ID = StrategyEnums.Tenant;
	private static final StrategyEnums NAME = StrategyEnums.Tenant;
	private static final String TENANT_PARAMETER = StrategyEnums.Tenant.toString();

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return ID.toString();
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return NAME.toString();
	}

	@Override
	public boolean isActive(FeatureState featureState, FeatureUser user) {
		// TODO Auto-generated method stub
		String tenantIds = featureState.getParameter(TENANT_PARAMETER);

		if (Strings.isNotBlank(tenantIds)) {

			List<String> tenantList = Strings.splitAndTrim(tenantIds, ",");

			CurrentUser currUser = (CurrentUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

			if (currUser != null && Strings.isNotBlank(currUser.getUserTenant().getTenantId().toString())) {
				String currUserTenantId = currUser.getUserTenant().getTenantId().toString();
				System.out.println("Current User Tenant ----" + currUserTenantId);

				return tenantList.contains(currUserTenantId) ? true : false;
			}

		}
		return false;
	}

	@Override
	public Parameter[] getParameters() {
		// TODO Auto-generated method stub
		return new Parameter[] { ParameterBuilder.create(TENANT_PARAMETER).label("Tenants").largeText()
				.description("List of tenants for which the fetaure is active") };
	}

}
