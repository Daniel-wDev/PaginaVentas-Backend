package com.example.PaginaVentas.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.PaginaVentas.model.User;
import com.example.PaginaVentas.service.UserService;
import io.jsonwebtoken.lang.Supplier;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.web.server.csrf.CsrfToken;

@RestController
@RequestMapping("/api/v1")
public class UserController {

    @Autowired
    private UserService usuarioService;

    @GetMapping(value = "/csrf-token")
    public CsrfToken getCsrfToken(HttpServletRequest request) {
        Object csrfObject = request.getAttribute(CsrfToken.class.getName());

        if (csrfObject instanceof Supplier) {
            // Si el objeto es un Supplier, obtenemos el token desde el Supplier
            return ((Supplier<CsrfToken>) csrfObject).get();
        } else if (csrfObject instanceof CsrfToken csrfToken) {
            // Si ya es un CsrfToken, lo devolvemos directamente
            return csrfToken;
        }
        return null; // Manejo en caso de que no haya token
    }

    @GetMapping(value = "/usuarios")
    public ResponseEntity<Object> get() {
        Map<String, Object> map = new HashMap<>();
        try {
            List<User> list = usuarioService.findAll();
            return new ResponseEntity<>(list, HttpStatus.OK);
        } catch (Exception e) {
            map.put("message", e.getMessage());
            return new ResponseEntity<>(map, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/usuarios/{id}")
    public ResponseEntity<Object> getById(@PathVariable Long id) {
        try {
            User data = usuarioService.findById(id);
            return new ResponseEntity<>(data, HttpStatus.OK);
        } catch (Exception e) {
            Map<String, Object> map = new HashMap<>();
            map.put("message", e.getMessage());
            return new ResponseEntity<>(map, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = "/usuarios")
    public ResponseEntity<Object> create(@RequestBody User usuario) {
        Map<String, Object> map = new HashMap<>();
        try {
            User res = usuarioService.save(usuario);
            return new ResponseEntity<>(res, HttpStatus.OK);
        } catch (Exception e) {
            map.put("message", e.getMessage());
            return new ResponseEntity<>(map, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/usuarios/{id}")
    public ResponseEntity<Object> update(@RequestBody User user, @PathVariable Long id) {
        Map<String, Object> map = new HashMap<>();
        try {
            User currentUser = usuarioService.findById(id);
            currentUser.setUsername(user.getUsername());
            currentUser.setPassword(user.getPassword());
            currentUser.setRole(user.getRole());
            currentUser.setPhone(user.getPhone());

            User res = usuarioService.save(user);
            return new ResponseEntity<>(res, HttpStatus.OK);
        } catch (Exception e) {
            map.put("message", e.getMessage());
            return new ResponseEntity<>(map, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/usuarios/{id}")
    public ResponseEntity<Object> delete(@PathVariable Long id) {
        Map<String, Object> map = new HashMap<>();
        try {
            User currentUsuario = usuarioService.findById(id);
            usuarioService.delete(currentUsuario);
            map.put("deleted", true);
            return new ResponseEntity<>(map, HttpStatus.OK);
        } catch (Exception e) {
            map.put("message", e.getMessage());
            return new ResponseEntity<>(map, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
