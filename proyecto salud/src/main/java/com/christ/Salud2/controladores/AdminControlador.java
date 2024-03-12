package com.christ.Salud2.controladores;

import com.christ.Salud2.entidades.Usuario;
import com.christ.Salud2.excepciones.MiException;
import com.christ.Salud2.repositorios.UsuarioRepo;
import com.christ.Salud2.servicios.UsuarioServicio;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin")
public class AdminControlador {

    @Autowired
    private UsuarioServicio usuarioServicio;

    @Autowired
    private UsuarioRepo usuarioRepo;

    @GetMapping("/dashboard")
    public String panelAdministrativo() {
        return "panel_adm.html";
    }

    @GetMapping("/modificar/{id}")
    public String mostrarFormularioModificar(@PathVariable String id, ModelMap modelo) {
        Usuario usuario = usuarioServicio.getOne(id);
        modelo.addAttribute("usuario", usuario);
        return "paciente_modificar.html";
    }

    @PostMapping("/modificar/{id}")
    public String modificar(@PathVariable String id, @RequestParam String nombre, MultipartFile archivo, @RequestParam String dni,
            @RequestParam String email, @RequestParam boolean obraSocial, @RequestParam String password, @RequestParam String password2, ModelMap modelo, RedirectAttributes redirectAttributes) {
        if (nombre == null || nombre.trim().isEmpty()) {
            modelo.put("error", "Debe incluir el nombre.");
            modelo.put("error", "debe incluir dni");
            modelo.put("error", "debe incluir email");

            return "paciente_modificar.html";
        }

        try {
            usuarioServicio.actualizar(archivo, dni, id, nombre, email, obraSocial, password, password2);
            redirectAttributes.addFlashAttribute("exito", "Paciente modificado exitosamente");
            return "redirect:/admin/listaCompleta";
        } catch (MiException ex) {
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
            return "paciente_modificar.html";
        }
    }

    @GetMapping("/listaCompleta")
    public String listarCompleta(ModelMap modelo) {

        List<Usuario> usuarios = usuarioServicio.listarUsuarios();

        modelo.addAttribute("usuarios", usuarios);

        return "paciente_list.html";
    }

    @GetMapping("/listarPacientes")
    public String listarPacientes(ModelMap modelo) {

        List<Usuario> usuarios = usuarioRepo.buscarPacientes();

        modelo.addAttribute("usuarios", usuarios);

        return "paciente_list.html";
    }
}
