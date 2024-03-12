package com.christ.Salud2.repositorios;


import com.christ.Salud2.entidades.Turno;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TurnoRepo extends JpaRepository<Turno, Long> {

@Query("SELECT p.id AS id_profesional, hd.dia, t.horario FROM Profesional p "
     + "INNER JOIN p.horariosDisponibles hd "
     + "INNER JOIN hd.turnos t "
     + "WHERE p.id = :profesionalId "
     + "ORDER BY hd.dia, t.horario")
    List<Object[]> obtenerDatosTurnoPorProfesionalId(@Param("profesionalId") String profesionalId);

}
