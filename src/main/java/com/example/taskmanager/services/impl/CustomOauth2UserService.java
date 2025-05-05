package com.example.taskmanager.services.impl;

import com.example.taskmanager.entities.User;
import com.example.taskmanager.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Slf4j
@Service
public class CustomOauth2UserService extends DefaultOAuth2UserService{

    private final DefaultOAuth2UserService delegate = new DefaultOAuth2UserService();
    private final UserRepository userRepository;

    public CustomOauth2UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = delegate.loadUser(userRequest);
        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        String email = null;

        Map<String, Object> attributes = new HashMap<>(oAuth2User.getAttributes());

        if ("google".equals(registrationId)) {
            email = (String) attributes.get("email");
            String name = (String) attributes.get("name");

            System.out.println("Google user: " + name + ", Email: " + email);
        } else if ("github".equals(registrationId)) {
            email = fetchGithubPrimaryEmail(userRequest);
            attributes.put("email", email);
            System.out.println("GitHub user, email fetched: " + email);
        } else if ("facebook".equals(registrationId)) {
            email = (String) attributes.get("email");
            String name = (String) attributes.get("name");
            System.out.println("Facebook user: " + name + ", Email: " + email);
        }

        Optional<User> user = userRepository.findByEmail(email);
        if(!user.get().isEmailVerified()) {
            throw new IllegalStateException("Email is not verified");
        }
        System.out.println("Attributes from Google: " + attributes.keySet());

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")),
                attributes,
                "email"
        );
    }

    private String fetchGithubPrimaryEmail(OAuth2UserRequest userRequest) {
        OAuth2AccessToken accessToken = userRequest.getAccessToken();
        String token = accessToken.getTokenValue();

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        HttpEntity<Void> request = new HttpEntity<>(headers);
        ResponseEntity<List<Map<String, Object>>> response = restTemplate.exchange(
                "https://api.github.com/user/emails",
                HttpMethod.GET,
                request,
                new ParameterizedTypeReference<>() {}
        );

        return Objects.requireNonNull(response.getBody()).stream()
                .filter(emailEntry -> Boolean.TRUE.equals(emailEntry.get("primary")))
                .findFirst()
                .map(emailEntry -> (String) emailEntry.get("email"))
                .orElse(null);
    }
}
