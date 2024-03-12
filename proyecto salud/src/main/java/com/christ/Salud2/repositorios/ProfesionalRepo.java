package com.christ.Salud2.repositorios;


import com.christ.Salud2.entidades.Profesional;
import com.christ.Salud2.enumeradores.Especialidad;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfesionalRepo extends JpaRepository<Profesional, String> {

    @Query("SELECT p FROM Profesional p WHERE p.especialidad = :especialidad")
    List<Profesional> findByEspecialidad(Especialidad especialidad);

    @Query("SELECT p FROM  Profesional p WHERE  p.nombre = :nombre")
    public Profesional buscarPorNombre(@Param("nombre") String nombre);

    @Query("SELECT p.id FROM Profesional p WHERE p.nombre = :nombre")
    String obtenerIdPorNombre(@Param("nombre") String nombre);

    @Query("SELECT p FROM Profesional p")
    List<Profesional> findAllProfesionales();
    
     @Query("SELECT p FROM  Profesional p WHERE  p.email = :email")
    public Profesional buscarPorEmail(@Param("email")String email);
    
}
