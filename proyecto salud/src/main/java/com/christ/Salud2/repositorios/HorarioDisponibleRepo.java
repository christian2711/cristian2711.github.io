package com.christ.Salud2.repositorios;

import com.christ.Salud2.entidades.HorarioDisponible;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;


public interface HorarioDisponibleRepo extends JpaRepository<HorarioDisponible, String> {

   List<HorarioDisponible> findAllByProfesionalId(String id);


}
