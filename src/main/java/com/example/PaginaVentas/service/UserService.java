package com.example.PaginaVentas.service;

import com.example.PaginaVentas.exception.OurException;
import com.example.PaginaVentas.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import java.util.List;
import com.example.PaginaVentas.repository.UserRepository;
import com.example.PaginaVentas.utils.JwtTokenUtil;
import java.util.Optional;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
public class UserService implements UserServiceInterface {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    @Transactional
    public List<User> findAll() {
        return (List<User>) userRepository.findAll();
    }

    @Override
    @Transactional
    public User save(User user) {

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public User findById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public void delete(User user) {
        userRepository.delete(user);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void createAdminUser() {
        // Crear un nuevo usuario admin
        User admin = new User();
        admin.setUsername("admin");
        admin.setPassword(passwordEncoder.encode("admin"));  // Codificar la contraseña
        admin.setRole("ADMIN");
        admin.setPhone(Integer.valueOf("12345"));
        // Guardar el usuario admin en la base de datos
        userRepository.save(admin);
    }
}

interface UserServiceInterface {

    public List<User> findAll();

    public User save(User user);

    public User findById(Long idUser);

    public void delete(User user);

    Optional<User> findByUsername(String username);

}
