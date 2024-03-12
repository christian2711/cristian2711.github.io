package com.christ.Salud2.repositorios;

import com.christ.Salud2.entidades.Usuario;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepo extends JpaRepository<Usuario, String> {

    @Query("SELECT u FROM  Usuario u WHERE  u.email = :email")
    public Usuario buscarPorEmail(@Param("email") String email);
    
     @Query("SELECT u.id FROM  Usuario u WHERE  u.email = :email")
    public Usuario buscarIdPorEmail(@Param("email") String email);

    @Query("SELECT u FROM Usuario u")
    List<Usuario> findAllPaciente();

    @Query("SELECT u FROM Usuario u WHERE u.rol = 'PACIENTE'")
    List<Usuario> buscarPacientes();

}
