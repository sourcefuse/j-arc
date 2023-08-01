package com.sourcefuse.jarc.services.usertenantservice.specifications;

import com.sourcefuse.jarc.services.usertenantservice.dto.UserCredentials;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import java.util.UUID;

public final class UserCredentialsSpecification {
    private UserCredentialsSpecification(){}

    public static Specification<UserCredentials> byUserId(UUID userId) {
        return (
                Root<UserCredentials> root,
                CriteriaQuery<?> query,
                CriteriaBuilder builder
        ) ->
                builder.equal(root.get("userId").get("id"), userId);
    }
}
