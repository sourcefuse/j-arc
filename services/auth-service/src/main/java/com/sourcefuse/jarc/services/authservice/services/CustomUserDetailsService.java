package com.sourcefuse.jarc.services.authservice.services;

import java.util.ArrayList;
import java.util.List;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.sourcefuse.jarc.services.authservice.models.Role;
import com.sourcefuse.jarc.services.authservice.models.User;
import com.sourcefuse.jarc.services.authservice.models.UserTenant;
import com.sourcefuse.jarc.services.authservice.repositories.RoleRepository;
import com.sourcefuse.jarc.services.authservice.repositories.UserCredentialRepository;
import com.sourcefuse.jarc.services.authservice.repositories.UserRepository;
import com.sourcefuse.jarc.services.authservice.repositories.UserTenantRepository;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class CustomUserDetailsService implements UserDetailsService {

        private final UserRepository userRepository;
        private final UserTenantRepository userTenantRepository;
        private final UserCredentialRepository userCredentialRepository;
        private final RoleRepository roleRepository;

        @Override
        public UserDetails loadUserByUsername(String usernameOrEmail)
                        throws UsernameNotFoundException {
                User user = userRepository
                                .findFirstUserByUsernameOrEmail(usernameOrEmail)
                                .orElseThrow(() -> new UsernameNotFoundException(
                                                "User not found with username or email: " + usernameOrEmail));
                UserTenant userTenant = userTenantRepository.findUserTenantByUserId(
                                user.getId()).orElseThrow(
                                                () -> new UsernameNotFoundException(
                                                                "User tenant not found with username or email: "
                                                                                + usernameOrEmail));
                userCredentialRepository.findByUserId(
                                user.getId()).orElseThrow(
                                                () -> new UsernameNotFoundException(
                                                                "User Credentials not found with username or email: "
                                                                                + usernameOrEmail));
                Role role = roleRepository
                                .findById(userTenant.getRoleId())
                                .orElseThrow(() -> new UsernameNotFoundException(
                                                "Role not found by role id: " + userTenant.getRoleId()));

                GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(role.getName());
                List<GrantedAuthority> listAuthorities = new ArrayList<GrantedAuthority>();
                listAuthorities.add(grantedAuthority);
                return new org.springframework.security.core.userdetails.User(
                                user.getEmail(),
                                "",
                                listAuthorities);
        }
}
