package com.christ.Salud2.controladores;


import com.christ.Salud2.servicios.HorarioDisponibleServicio;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/horarios")
public class HorarioDisponibleControlador {

    @Autowired
    private HorarioDisponibleServicio horarioDisponibleServicio;

    @GetMapping("/crearHorario/{id}")
    public String mostrarFormularioRegistroHorarios(@PathVariable("id") String id, Model model) {
        model.addAttribute("id", id);
        System.out.println(id);

        return "registroHorarios.html";
    }

    @PostMapping("/crearTurnoPorDia/{id}")
    public String crearHorario(@PathVariable("id") String id,
            @RequestParam String dia, @RequestParam List<String>turnos) {
     
        horarioDisponibleServicio.crearHorario(id, dia, turnos);

        return "panel_adm.html";
    }

}
