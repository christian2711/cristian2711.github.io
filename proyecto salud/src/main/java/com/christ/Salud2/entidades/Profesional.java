package com.christ.Salud2.entidades;

import com.christ.Salud2.enumeradores.Especialidad;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.OneToMany;



@Entity
public class Profesional extends Usuario{

  
    private Integer honorario;
    private Integer calificacion;

    @Enumerated(EnumType.STRING)
    private Especialidad especialidad;
    
    
    @OneToMany(mappedBy = "profesional")
    private List<HorarioDisponible> horariosDisponibles;

    public Profesional() {
    }

    public Integer getHonorario() {
        return honorario;
    }

    public void setHonorario(Integer honorario) {
        this.honorario = honorario;
    }

    public Integer getCalificacion() {
        return calificacion;
    }

    public void setCalificacion(Integer calificacion) {
        this.calificacion = calificacion;
    }

    public Especialidad getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(Especialidad especialidad) {
        this.especialidad = especialidad;
    }

    public List<HorarioDisponible> getHorariosDisponibles() {
        return horariosDisponibles;
    }

    public void setHorariosDisponibles(List<HorarioDisponible> horariosDisponibles) {
        this.horariosDisponibles = horariosDisponibles;
    }



}
