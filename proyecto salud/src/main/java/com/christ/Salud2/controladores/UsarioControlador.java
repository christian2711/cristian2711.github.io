package com.christ.Salud2.controladores;

import com.christ.Salud2.entidades.Profesional;
import com.christ.Salud2.entidades.Usuario;
import com.christ.Salud2.enumeradores.Especialidad;
import com.christ.Salud2.excepciones.MiException;
import com.christ.Salud2.repositorios.TurnoRepo;
import com.christ.Salud2.repositorios.UsuarioRepo;
import com.christ.Salud2.servicios.ProfesionalServicio;
import com.christ.Salud2.servicios.UsuarioServicio;
import java.util.List;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/usuario")
public class UsarioControlador {

    @Autowired
    private ProfesionalServicio profesionalServicio;

    @Autowired
    private TurnoRepo turnoRepo;

    @Autowired
    private UsuarioRepo usuarioRepo;

    @Autowired
    private UsuarioServicio usuarioServicio;

    @GetMapping("/listaProfesional")
    public String listarPorEspecialidad(@RequestParam("especialidad") Especialidad especialidad, ModelMap modelo) {

        List<Profesional> profesionales = profesionalServicio.listarProfesionalesPorEspecialidad(especialidad);

        modelo.addAttribute("especialidad", especialidad);
        modelo.addAttribute("profesionales", profesionales);

        return "profesional_list.html";
    }

    @GetMapping("/horarioProfesional/{id}")
    public String listarHorarioProfesionales(ModelMap modelo, @PathVariable String id) {

        List<Object[]> horarios = turnoRepo.obtenerDatosTurnoPorProfesionalId(id);

        modelo.addAttribute("horarios", horarios);

        return "horarios.html";
    }

 @GetMapping("/perfil")
    public String perfil(ModelMap modelo,HttpSession session){
        Usuario usuario = (Usuario) session.getAttribute("usuariosession");
         modelo.put("usuario", usuario);
        return "actualizar_paciente.html";
    }

    @PostMapping("/modificar/{id}")
    public String actualizar(MultipartFile archivo, @PathVariable String id, @RequestParam String nombre, @RequestParam String dni, @RequestParam boolean obraSocial, @RequestParam String email,
            @RequestParam String password, @RequestParam String password2, ModelMap modelo) {

        try {

            usuarioServicio.actualizar(archivo, dni, id, nombre, email, obraSocial, password, password2);

            modelo.put("exito", "Usuario actualizado correctamente!");

            return "inicio_paciente.html";
        } catch (MiException ex) {

            modelo.put("error", ex.getMessage());
            modelo.put("nombre", nombre);
            modelo.put("email", email);

            return "actualizar_paciente.html";
        }

    }
    
      @GetMapping("/inicio")
    public String inicioPaciente() {
        return "inicio.html";
    }
}
