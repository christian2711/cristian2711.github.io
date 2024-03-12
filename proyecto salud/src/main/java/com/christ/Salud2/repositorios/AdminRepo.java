package com.christ.Salud2.repositorios;

import com.christ.Salud2.entidades.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepo extends JpaRepository<Admin, String> {
    @Query("SELECT p FROM  Admin p WHERE  p.email = :email")
    public Admin buscarPorEmail(@Param("email")String email);
}
