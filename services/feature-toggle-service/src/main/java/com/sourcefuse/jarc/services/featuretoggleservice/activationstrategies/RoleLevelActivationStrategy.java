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

public class RoleLevelActivationStrategy implements ActivationStrategy {

	private static final String ID = "role-level";
	private static final String NAME = "Role level";
	private static final String ROLE_PARAMETER = "role";
	// private static final String END_TIME_PARAMETER = "end";

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return ID;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return NAME;
	}

	@Override
	public boolean isActive(FeatureState featureState, FeatureUser user) {
		// TODO Auto-generated method stub
		String rolesAsString = featureState.getParameter(ROLE_PARAMETER);

		if (Strings.isNotBlank(rolesAsString)) {

			List<String> rolesList = Strings.splitAndTrim(rolesAsString, ",");

			CurrentUser currUser = (CurrentUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

			if (currUser != null && Strings.isNotBlank(currUser.getRole().getName())) {
				String currUserRole = currUser.getRole().getName();

				return rolesList.contains(currUserRole) ? true : false;
			}

		}
		return false;
	}

	@Override
	public Parameter[] getParameters() {
		// TODO Auto-generated method stub
		return new Parameter[] { ParameterBuilder.create(ROLE_PARAMETER).label("Roles").largeText()
				.description("List of roles for which the fetaure is active") };
	}

}
