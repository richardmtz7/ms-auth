package com.auth.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.auth.entities.RoleEntity;
import com.auth.entities.UserEntity;
import com.auth.entities.DTO.AuthCreateUserRequest;
import com.auth.entities.DTO.AuthLoginRequest;
import com.auth.entities.DTO.AuthResponse;
import com.auth.repository.IRoleRepository;
import com.auth.repository.IUserRepository;
import com.auth.utils.JwtUtils;

import jakarta.transaction.Transactional;

@Service
public class UserDetailServiceImpl implements UserDetailsService {
	@Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private IRoleRepository roleRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		UserEntity userEntity = userRepository.findByEmail(username);
		
		List<SimpleGrantedAuthority> authorityList = new ArrayList<>();
		
		userEntity.getRoles().forEach(role -> authorityList.add(new SimpleGrantedAuthority("ROLE_".concat(role.getRoleEnum().name()))));
		userEntity.getRoles().stream().flatMap(role -> role.getPermissionList().stream()).forEach(permission -> authorityList.add(new SimpleGrantedAuthority(permission.getName())));

		return new User(userEntity.getName(), userEntity.getPassword(), authorityList);
	}
	
	@Transactional
	public String createUser(AuthCreateUserRequest createRoleRequest) {

        String email = createRoleRequest.email();
        String password = createRoleRequest.password();
        String name = createRoleRequest.name();
        List<String> rolesRequest = createRoleRequest.roleRequest().roleListName();

        Set<RoleEntity> roleEntityList = roleRepository.findRoleEntitiesByRoleEnumIn(rolesRequest).stream().collect(Collectors.toSet());

        if (roleEntityList.isEmpty()) {
            throw new IllegalArgumentException("The roles specified does not exist.");
        }

        UserEntity userEntity = UserEntity.builder()
        		.name(name)
        		.password(passwordEncoder.encode(password))
        		.email(email)
        		.roleId(rolesRequest.get(0))
        		.roles(roleEntityList)
        		.status(1)
        		.build();

        userRepository.save(userEntity);
        
        return "User created successfully";
    }
	
	public AuthResponse loginUser(AuthLoginRequest authLoginRequest) {

        String email = authLoginRequest.email();
        String password = authLoginRequest.password();
        Authentication authentication = this.authenticate(email, password);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String accessToken = jwtUtils.createToken(authentication);
        UserEntity userLogin = userRepository.findByEmail(email);
        int userId = userLogin.getUserId();
        String role = userLogin.getRoleId();
        AuthResponse authResponse = new AuthResponse(userId, email, "User loged succesfully", role , true, accessToken);
     
        return authResponse;
    }

	public Authentication authenticate(String username, String password) {
        UserDetails userDetails = this.loadUserByUsername(username);
        if (userDetails == null) {
            throw new BadCredentialsException(String.format("Invalid username or password"));
        }
        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new BadCredentialsException("Incorrect Password");
        }

        return new UsernamePasswordAuthenticationToken(username, password, userDetails.getAuthorities());
    }
    
    
}
