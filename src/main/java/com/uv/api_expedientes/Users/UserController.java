package com.uv.api_expedientes.Users;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.RestController;

import com.uv.api_expedientes.Auth.AuthService;
import com.uv.api_expedientes.Auth.dtos.RegisterUserDto;
import com.uv.api_expedientes.Users.dtos.AllUsersDto;
import com.uv.api_expedientes.Users.dtos.MatriculaDto;
import com.uv.api_expedientes.Users.dtos.UserDto;
import com.uv.api_expedientes.Users.dtos.UserEditDto;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final AuthService authService;

    @GetMapping("/Ver")
    public ResponseEntity<List<AllUsersDto>> getAllActiveUsers() {
        return ResponseEntity.ok(userService.getUsers());
    }

    @GetMapping("/Ver/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable("id") int id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @PostMapping("/Crear")
    public ResponseEntity<MatriculaDto> register(@RequestBody RegisterUserDto registerUserDto) {
        return ResponseEntity.ok(authService.register(registerUserDto));
    }

    @DeleteMapping("/Eliminar/{id}")
    public ResponseEntity<Map<String, String>> deactivateUser(@PathVariable("id") int id) {
        Map<String, String> response = new HashMap<>();
        response.put("mensaje", userService.deactivateUser(id));
        return ResponseEntity.ok(response);
    }

    @PutMapping("Editar/{id}")
    public ResponseEntity<Map<String, String>> putMethodName(@PathVariable Integer id,
            @RequestBody UserEditDto userEditDto) {
        Map<String, String> response = new HashMap<>();
        response.put("mensaje", userService.UpdateUser(id, userEditDto));
        return ResponseEntity.ok(response);
    }

    @PostMapping("/Reactivar/{id}")
    public ResponseEntity<String> reactivateUser(@PathVariable("id") int id) {
        return ResponseEntity.ok(userService.reactivateUser(id));
    }

}
