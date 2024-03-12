package com.christ.Salud2.servicios;

import com.christ.Salud2.entidades.Profesional;
import com.christ.Salud2.enumeradores.Especialidad;
import com.christ.Salud2.enumeradores.Rol;
import com.christ.Salud2.excepciones.MiException;
import com.christ.Salud2.repositorios.HorarioDisponibleRepo;
import com.christ.Salud2.repositorios.ProfesionalRepo;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Service
public class ProfesionalServicio implements UserDetailsService {

    @Autowired
    private ProfesionalRepo profesionalRepo;

    @Autowired
    private HorarioDisponibleRepo horarioDisponibleRepo;
    


     @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Profesional profesional= profesionalRepo.buscarPorEmail(email);
        System.out.println(profesional);

        if (profesional != null) {
            List<GrantedAuthority> permisos = new ArrayList<>();
            GrantedAuthority p = new SimpleGrantedAuthority("ROLE_" + profesional.getRol().toString());
            permisos.add(p);
            ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
            HttpSession session = attr.getRequest().getSession(true);
            session.setAttribute("usuarioSession", profesional);
            
            return new User(profesional.getEmail(), profesional.getPassword(), permisos);
            
        } else {
            throw new UsernameNotFoundException("No se encontró ningún médico con el correo electrónico: " + email);
        }
    }

    @Transactional
    public void registrarProfesional(String nombre, Integer honorario, String email,Integer calificacion,
            Especialidad especialidad,Rol rol, String password, String password2) throws MiException {

        Profesional profesional = new Profesional();
        profesional.setNombre(nombre);
        profesional.setHonorario(honorario);
        profesional.setEmail(email);
        profesional.setCalificacion(calificacion);
        profesional.setEspecialidad(especialidad);
        profesional.setRol(rol);
        profesional.setAlta(true);
        profesional.setPassword(new BCryptPasswordEncoder().encode(password));
        
        profesionalRepo.save(profesional);

    }

    @Transactional
    public void actualizarProfesional(String id, String nombre, Integer honorario,String email, Especialidad especialidad) throws MiException {

        Optional<Profesional> respuesta = profesionalRepo.findById(id);
        if (respuesta.isPresent()) {
            Profesional profesional = respuesta.get();
            profesional.setNombre(nombre);
            profesional.setHonorario(honorario);
            profesional.setEmail(email);
            profesional.setEspecialidad(especialidad);
            profesional.setRol(Rol.PROFESIONAL);
            
            profesionalRepo.save(profesional);
        } else {
            throw new MiException("No se encontró el profesional con el ID: " + id);
        }
    }

    @Transactional(readOnly = true)
    public List<Profesional> listarProfesionales() {
        return profesionalRepo.findAllProfesionales();
    }

  
    @Transactional(readOnly = true)
    public List<Profesional> listarProfesionalesPorEspecialidad(Especialidad especialidad) {
        List<Profesional> profesionales = new ArrayList<>();

        if (especialidad != null) {
            profesionales = profesionalRepo.findByEspecialidad(especialidad);
        }

        return profesionales;
    }
    
      public Profesional getOne(String id) {
        return profesionalRepo.getOne(id);
    }

    
}
