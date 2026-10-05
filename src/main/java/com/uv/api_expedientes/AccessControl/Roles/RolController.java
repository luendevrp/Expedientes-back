package com.uv.api_expedientes.AccessControl.Roles;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uv.api_expedientes.AccessControl.Roles.dtos.IdRolDto;
import com.uv.api_expedientes.AccessControl.Roles.dtos.RolNamesResponseDTO;

import lombok.RequiredArgsConstructor;

import com.uv.api_expedientes.AccessControl.Roles.dtos.AllRolesDto;
import com.uv.api_expedientes.AccessControl.Roles.dtos.CreateRolDto;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
public class RolController {

    private final RolService rolService;

    @GetMapping("/Ver")
    public ResponseEntity<List<AllRolesDto>> getAllRoles() {
        return ResponseEntity.ok(rolService.getAllRoles());
    }

    @GetMapping("/Ver/{id}")
    public ResponseEntity<IdRolDto> getRolById(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(rolService.getRolById(id));
    }

    @GetMapping("/Ver/Nombres")
    public List<RolNamesResponseDTO> obtenerNombresRoles() {
        return rolService.obtenerNombresRoles();
    }

    @PostMapping("/Crear")
    public ResponseEntity<Void> createRol(@RequestBody CreateRolDto createRolDto) {
        return ResponseEntity.ok(rolService.createRol(createRolDto));
    }

    @DeleteMapping("/Eliminar/{id}")
    public ResponseEntity<Void> deleteRol(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(rolService.deleteRol(id));
    }

    @PutMapping("/Editar/{id}")
    public ResponseEntity<Void> updateRol(@PathVariable("id") Integer id, @RequestBody CreateRolDto createRolDto) {
        return ResponseEntity.ok(rolService.updateRol(id, createRolDto));
    }
}
