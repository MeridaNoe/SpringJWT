package mx.edu.utez.sda.springjwt.service;

import mx.edu.utez.sda.springjwt.model.UserInfo;
import mx.edu.utez.sda.springjwt.repository.UserInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserInfoService implements UserDetailsService {

    @Autowired
    private UserInfoRepository repository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserInfo> userDatail = repository.findByUsername(username);

        return userDatail.map(UserInfoDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado" + username)
        );
    }

    public String guardarUser(UserInfo userInfo) {
        userInfo.setPassword(passwordEncoder.encode(userInfo.getPassword()));
        userInfo.setNonLocked(true);
        repository.save(userInfo);
        return "Usuario guardado correctamente";

    }
}
