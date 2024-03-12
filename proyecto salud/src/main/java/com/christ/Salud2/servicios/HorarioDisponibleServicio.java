package com.christ.Salud2.servicios;

import com.christ.Salud2.entidades.HorarioDisponible;
import com.christ.Salud2.entidades.Profesional;
import com.christ.Salud2.entidades.Turno;
import com.christ.Salud2.repositorios.HorarioDisponibleRepo;
import com.christ.Salud2.repositorios.ProfesionalRepo;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class HorarioDisponibleServicio {

    @Autowired
    private ProfesionalRepo profesionalRepo;

    @Autowired
    private HorarioDisponibleRepo horarioDisponibleRepo;

    @Transactional
    public void crearHorario(String id, String dia, List<String> turnos) {
        Optional<Profesional> respuesta = profesionalRepo.findById(id);

        if (respuesta.isPresent()) {
            Profesional profesional = respuesta.get();

            HorarioDisponible horarioDisponible = new HorarioDisponible();
            horarioDisponible.setProfesional(profesional);
            horarioDisponible.setDia(dia);

            List<Turno> tur = new ArrayList<>();
            for (String horario : turnos) {
                Turno turno = new Turno();
                turno.setHorario(horario);
                tur.add(turno);
            }

            horarioDisponible.setTurnos(tur);
            horarioDisponibleRepo.save(horarioDisponible);
        }
    }

}
