/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.PaginaVentas.controller;

/**
 *
 * @author elfau
 */
import com.example.PaginaVentas.model.User;
import com.example.PaginaVentas.utils.JwtTokenUtil;
import com.example.PaginaVentas.service.UserService;
import java.time.Instant;
import java.util.AbstractMap;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.core.HeaderLinksResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;
    private final UserService userService;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager, JwtTokenUtil jwtTokenUtil, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {

        try {
            var authenticationToken = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
            var authentication = authenticationManager.authenticate(authenticationToken);
            var jwt = jwtTokenUtil.generateToken(authentication.getName());
            Date expirationTime = jwtTokenUtil.getExpirationDateFromToken(jwt);

            System.out.println("Token Generado: " + jwt);

            // Crear un mapa para el cuerpo de la respuesta JSON
            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("status", "SUCCESS");
            responseBody.put("token", jwt);
            responseBody.put("expirationTime", expirationTime.toString());

            // Retornar el ResponseEntity con el cuerpo y el estado OK
            return ResponseEntity.ok(responseBody);

        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }

}
