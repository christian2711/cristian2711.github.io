package com.christ.Salud2.controladores;

import com.christ.Salud2.entidades.Profesional;
import com.christ.Salud2.entidades.Usuario;
import com.christ.Salud2.enumeradores.Especialidad;
import com.christ.Salud2.enumeradores.Rol;
import com.christ.Salud2.excepciones.MiException;
import com.christ.Salud2.repositorios.UsuarioRepo;
import com.christ.Salud2.servicios.ProfesionalServicio;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/profesional")
public class ProfesionalControlador {

    @Autowired
    private ProfesionalServicio profesionalServicio;

    @Autowired
    private UsuarioRepo usuarioRepo;

    @GetMapping("/inicio")
    public String panelAdministrativo() {
        return "inicio_profesional.html";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/registrarProfesional")
    public String registrar() {

        return "profesional_form.html";

    }

    @PostMapping("/registroProfesional")
    public String registro(@RequestParam String nombre, @RequestParam Integer honorario, @RequestParam String email,
            @RequestParam Integer calificacion, @RequestParam Especialidad especialidad, @RequestParam Rol rol, @RequestParam String password,
            @RequestParam String password2, ModelMap modelo) throws MiException {

        profesionalServicio.registrarProfesional(nombre, honorario, email, calificacion, especialidad, rol, password, password2);

        modelo.put("exito", "El profesional fue cargado correctamente!");
        return "panel_adm.html";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/modificar/{id}")
    public String mostrarFormularioModificar(@PathVariable String id, ModelMap modelo) {
        Profesional profesional = profesionalServicio.getOne(id);
        modelo.addAttribute("profesional", profesional);
        return "profesional_modificar.html";
    }

    @PostMapping("/modificar/{id}")
    public String modificar(@PathVariable String id, @RequestParam String nombre, @RequestParam Integer honorario, @RequestParam String email, ModelMap modelo, @RequestParam Especialidad especialidad, RedirectAttributes redirectAttributes) {
        if (nombre == null || nombre.trim().isEmpty()) {
            modelo.put("error", "Debe incluir el nombre.");
            modelo.put("error", "debe incluir costo");
            modelo.put("error", "debe incluir email");
            modelo.put("error", "debe incluir especialidad");

            return "profesional_modificar.html";
        }

        try {
            profesionalServicio.actualizarProfesional(id, nombre, honorario, email, especialidad);
            redirectAttributes.addFlashAttribute("exito", "Profesional modificado exitosamente");
            return "redirect:/profesional/listaCompleta";
        } catch (MiException ex) {
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
            return "profesional_modificar.html";
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/listaCompleta")
    public String listarProfesionales(ModelMap modelo) {

        List<Profesional> profesionales = profesionalServicio.listarProfesionales();

        modelo.addAttribute("profesionales", profesionales);

        return "profesional_listaCompleta.html";
    }

    @GetMapping("/listarPacientes")
    public String listarPacientes(ModelMap modelo) {

        List<Usuario> usuarios = usuarioRepo.buscarPacientes();

        modelo.addAttribute("usuarios", usuarios);

        return "paciente_list.html";
    }
}
