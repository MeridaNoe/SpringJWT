package mx.edu.utez.sda.springjwt.control;

import mx.edu.utez.sda.springjwt.model.AuthRequest;
import mx.edu.utez.sda.springjwt.model.UserInfo;
import mx.edu.utez.sda.springjwt.service.JwtService;
import mx.edu.utez.sda.springjwt.service.UserInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")

public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserInfoService service;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private AuthenticationManager authenticationManager;

    @GetMapping("/index")
    public String index() {

        logger.trace("Este es un mensaje de trace");
        logger.debug("Este es un mensaje de debug");
        logger.info("Este es un mensaje de info");
        logger.warn("Este es un mensaje de warning");
        logger.error("Este es un mensaje de error");

        return "Servicio Index";
    }

    @PostMapping("/registrame")
    public String registrar(@RequestBody UserInfo userInfo) {
        return service.guardarUser(userInfo);
    }

    @GetMapping("/admin/test")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String soloAdmin() {
        return "Este endpoint es solo para admin";
    }

    @GetMapping("/user/test")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN,ROLE_USER')")
    public String paraUser() {
        return "Este endpoint puede ser para User y Admin";
    }

    @PostMapping("/login")
    public String login(@RequestBody AuthRequest authRequest) {
        try {
            Authentication authentication =
                    authenticationManager.authenticate(
                            new UsernamePasswordAuthenticationToken(
                                    authRequest.getUsername(),
                                    authRequest.getPassword()
                            )
                    );
            if (authentication.isAuthenticated()) {
                return jwtService.generateToken(authRequest.getUsername());
            } else {
                System.out.println("No autenticado");
                throw new UsernameNotFoundException("Usuario invalido");
            }
        } catch (Exception e) {
            System.out.println("excepcion");
            throw new UsernameNotFoundException("Usuario invalido");
        }
    }
}
