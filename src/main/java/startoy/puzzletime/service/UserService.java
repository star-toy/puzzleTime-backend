package startoy.puzzletime.service;

import startoy.puzzletime.domain.User;
import startoy.puzzletime.repository.UserRepository;

import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    public String getUserId(OAuth2AuthenticationToken authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            return authentication.getPrincipal().getAttribute("sub");
        }else{
            return "";
        }
    }
}
