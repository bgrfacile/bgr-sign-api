package com.bgrfacile.bgrsignapi.service;

import com.bgrfacile.bgrsignapi.model.CustomUserDetails;
import com.bgrfacile.bgrsignapi.model.User;
import com.bgrfacile.bgrsignapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public CustomUserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé avec le username : " + email));
        return CustomUserDetails.create(user);
    }

    // Méthode supplémentaire pour charger par ID
    public CustomUserDetails loadUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé avec l'id : " + id));
        return CustomUserDetails.create(user);
    }
}
