package com.christ.Salud2.repositorios;

import com.christ.Salud2.entidades.Ficha;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface FichaRepo extends JpaRepository<Ficha, String> {

    @Query("SELECT f FROM Ficha f WHERE f.id = :id")
    public Ficha buscarPorId(@Param("id") String id);

    @Query("SELECT f FROM Ficha f WHERE f.usuario.nombre = :nombre")
    List<Ficha> buscarPorNombre(@Param("nombre") String nombre);
}
