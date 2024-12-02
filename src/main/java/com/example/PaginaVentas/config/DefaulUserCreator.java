/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.PaginaVentas.config;

import com.example.PaginaVentas.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

/**
 *
 * @author elfau
 */
@Component
public class DefaulUserCreator implements CommandLineRunner {

    private final UserService userService;

    public DefaulUserCreator(@Lazy UserService userServiceImpl1) {
        this.userService = userServiceImpl1;
    }

    @Override
    public void run(String... args) throws Exception {
        // Verificar si el usuario "admin" ya existe, si no, crearlo
        if (userService.findByUsername("admin") == null) {
            userService.createAdminUser();
        }
    }
}
