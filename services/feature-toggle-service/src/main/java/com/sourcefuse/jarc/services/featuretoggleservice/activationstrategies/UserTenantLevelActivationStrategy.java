package com.sourcefuse.jarc.services.featuretoggleservice.activationstrategies;

import java.util.List;

import org.springframework.security.core.context.SecurityContextHolder;
import org.togglz.core.activation.Parameter;
import org.togglz.core.activation.ParameterBuilder;
import org.togglz.core.repository.FeatureState;
import org.togglz.core.spi.ActivationStrategy;
import org.togglz.core.user.FeatureUser;
import org.togglz.core.util.Strings;

import com.sourcefuse.jarc.core.models.session.CurrentUser;
import com.sourcefuse.jarc.services.featuretoggleservice.enums.StrategyEnums;

//Strategy that will check if the particular userTenant has access to the provided feature

public class UserTenantLevelActivationStrategy implements ActivationStrategy {

	private static final StrategyEnums ID = StrategyEnums.UserTenant;
	private static final StrategyEnums NAME = StrategyEnums.UserTenant;
	private static final String USER_TENANT_PARAMETER = StrategyEnums.UserTenant.toString();

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
		String userTenantIds = featureState.getParameter(USER_TENANT_PARAMETER);

		if (Strings.isNotBlank(userTenantIds)) {

			List<String> userTenantList = Strings.splitAndTrim(userTenantIds, ",");

			CurrentUser currUser = (CurrentUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

			if (currUser != null && Strings.isNotBlank(currUser.getUserTenantId().toString())) {
				String currUserUTenantId = currUser.getUserTenantId().toString();

				return userTenantList.contains(currUserUTenantId) ? true : false;
			}

		}
		return false;
	}

	@Override
	public Parameter[] getParameters() {
		// TODO Auto-generated method stub
		return new Parameter[] { ParameterBuilder.create(USER_TENANT_PARAMETER).label("UserTenants").largeText()
				.description("List of user tenant for which the fetaure is active") };
	}

}
