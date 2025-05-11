package com.pkcoder.authorization_server;

import com.pkcoder.authorization_server.repository.CredentialRepository;
import com.pkcoder.authorization_server.repository.RoleRepository;
import com.pkcoder.authorization_server.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;

@Slf4j
@EnableJpaAuditing
@EnableDiscoveryClient
@SpringBootApplication
public class AuthorizationServerApplication {
	@Value(value = "${ui.app.url}")
	private String uiAppUrl;

	public static void main(String[] args) {
		SpringApplication.run(AuthorizationServerApplication.class, args);
	}

	@Bean
	public ApplicationRunner init(
			RegisteredClientRepository registeredClientRepository,
			RoleRepository roleRepository,
			UserRepository userRepository,
			CredentialRepository credentialRepository,
			PasswordEncoder passwordEncoder
	) {
		return args -> {
//			if (registeredClientRepository.findByClientId("react-client") == null) {
//				try {
//					RegisteredClient reactClient = RegisteredClient.withId(UUID.randomUUID().toString())
//							.clientId("react-client").clientSecret("react-secret")
//							.clientName("React Client")
//							.clientAuthenticationMethod(ClientAuthenticationMethod.NONE)
//							.authorizationGrantTypes(authorizationGrantTypes -> {
//								authorizationGrantTypes.add(AuthorizationGrantType.AUTHORIZATION_CODE);
//								authorizationGrantTypes.add(AuthorizationGrantType.REFRESH_TOKEN);
//							})
//							.scopes(authorizationScopes -> {
//								authorizationScopes.add(OidcScopes.OPENID);
//								authorizationScopes.add(OidcScopes.PROFILE);
//								authorizationScopes.add(OidcScopes.EMAIL);
//							})
//							.redirectUri(uiAppUrl)
//							.postLogoutRedirectUri("http://localhost:8080")
//							.clientSettings(ClientSettings.builder()
//									.requireAuthorizationConsent(false)
//									.requireProofKey(true)
//									.build()
//							)
//							.tokenSettings(TokenSettings.builder()
//									.accessTokenTimeToLive(Duration.ofDays(1))
//									.refreshTokenTimeToLive(Duration.ofDays(2))
//									.build()
//							)
//							.build();
//
//					registeredClientRepository.save(reactClient);
//					log.info("Registered client successfully {}", reactClient.getClientId());
//				} catch (Exception exception) {
//					log.error(exception.getMessage());
//				}
//			}
//
//			try {
//				if (roleRepository.findByNameIgnoreCase(Authority.USER.name()).isEmpty()) {
//					var userRole = new RoleEntity();
//					userRole.setName(Authority.USER.name());
//					userRole.setAuthorities(Authority.USER);
//					roleRepository.save(userRole);
//
//					var adminRole = new RoleEntity();
//					adminRole.setName(Authority.ADMIN.name());
//					adminRole.setAuthorities(Authority.ADMIN);
//					roleRepository.save(adminRole);
//
//					var superAdminRole = new RoleEntity();
//					superAdminRole.setName(Authority.SUPER_ADMIN.name());
//					superAdminRole.setAuthorities(Authority.SUPER_ADMIN);
//					roleRepository.save(superAdminRole);
//
//					var managerRole = new RoleEntity();
//					managerRole.setName(Authority.MANAGER.name());
//					managerRole.setAuthorities(Authority.MANAGER);
//					roleRepository.save(managerRole);
//				}
//			} catch (Exception exception) {
//				log.error(exception.getMessage());
//			}
//
//			try {
//				if (userRepository.findByEmailIgnoreCase("pk@gmail.com").isEmpty()) {
//					UserEntity userEntity = UserEntity.builder()
//							.userUuid(UUID.randomUUID().toString())
//							.firstName("Pratik")
//							.lastName("Khedkar")
//							.email("pk@gmail.com")
//							.phone("1234567890")
//							.bio("Software Developer")
//							.address("[IND] India")
//							.imageUrl("https://cdn-icons-png.flaticon.com/512/149/149071.png")
//							.qrCodeSecret(EMPTY)
//							.qrCodeImageUri(EMPTY)
//							.loginAttempts(0)
//							.lastLogin(LocalDateTime.now())
//							.accountNonExpired(true)
//							.accountNonLocked(true)
//							.enabled(true)
//							.mfa(false)
//							.role(roleRepository.findByNameIgnoreCase(Authority.ADMIN.name()).orElse(null))
//							.build();
//					UserEntity savedUserEntity = userRepository.save(userEntity);
//					CredentialEntity credentialEntity = new CredentialEntity(savedUserEntity, passwordEncoder.encode("letmein"));
//					credentialRepository.save(credentialEntity);
//				}
//			} catch (Exception exception) {
//				log.error(exception.getMessage());
//			}

		};
	}

}
