package com.christ.Salud2.controladores;

import com.christ.Salud2.entidades.Usuario;
import com.christ.Salud2.excepciones.MiException;
import com.christ.Salud2.repositorios.ProfesionalRepo;
import com.christ.Salud2.repositorios.UsuarioRepo;
import com.christ.Salud2.servicios.UsuarioServicio;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/")
public class PortalControlador {

    @Autowired
    private UsuarioServicio usuarioServicio;

    @Autowired
    private ProfesionalRepo profesionalRepo;

    @Autowired
    private UsuarioRepo usuarioRepo;

    @GetMapping("/")
    public String index() {

        return "index.html";

    }

    @GetMapping("/registrar")
    public String registrar() {
        return "registro.html";
    }

    @PostMapping("/registro")
    public String registro(@RequestParam String nombre, @RequestParam String email, @RequestParam String dni, @RequestParam Boolean obraSocial, @RequestParam String password,
            String password2, ModelMap modelo, MultipartFile archivo) {

        try {
            usuarioServicio.registrarPaciente(nombre, email, dni, archivo, password, password2, obraSocial, obraSocial);

            modelo.put("exito", "Usuario registrado correctamente!");

            return "index.html";
        } catch (MiException ex) {

            modelo.put("error", ex.getMessage());
            modelo.put("nombre", nombre);
            modelo.put("email", email);
            modelo.put("dni", dni);
            modelo.put("obraSocial", obraSocial);

            return "registro.html";
        }

    }

    @GetMapping("/login")
    public String login(@RequestParam(required = false) String error, ModelMap modelo) {
        if (error != null) {
            modelo.put("error", "usuario o contreaseña invalida intente nuevamente");
        }

        return "login.html";
    }

    @PreAuthorize("hasAnyRole('ROLE_PACIENTE', 'ROLE_ADMIN','ROLE_PROFESIONAL')")
    @GetMapping("/inicio")
    public String inicio(HttpSession session, ModelMap modelo) {

        Usuario logueado = (Usuario) session.getAttribute("usuarioSession");
        if (logueado.getRol().toString().equals("ADMIN")) {
            return "redirect:/admin/dashboard";
        }

        if (logueado.getRol().toString().equals("PROFESIONAL")) {
            return "redirect:/profesional/inicio";
        }
        return "inicio_paciente.html";
    }

   @PreAuthorize("hasAnyRole('PACIENTE')")
    @GetMapping("/perfil")
    public String perfil(ModelMap modelo, HttpSession session
    ) {
        Usuario usuario = (Usuario) session.getAttribute("usuariosession");
        modelo.put("usuario", usuario);
        return "actualizar_paciente.html";
    }

    @PreAuthorize("hasAnyRole('PACIENTE')")
    @PostMapping("/modificar/{id}")
    public String actualizar(MultipartFile archivo, @PathVariable String id, @RequestParam String nombre, @RequestParam String dni, @RequestParam String email,
            @RequestParam String password, @RequestParam String password2, ModelMap modelo
    ) {

        try {
            usuarioServicio.actualizar(archivo, dni, id, nombre, email, true, password, password2);

            modelo.put("exito", "Usuario actualizado correctamente!");

            return "index.html";
        } catch (MiException ex) {

            modelo.put("error", ex.getMessage());
            modelo.put("nombre", nombre);
            modelo.put("email", email);
            modelo.put("dni", dni);

            return "actualizar_paciente.html";
        }

    }
}
