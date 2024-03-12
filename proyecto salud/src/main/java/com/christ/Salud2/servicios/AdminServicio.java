package com.christ.Salud2.servicios;

import com.christ.Salud2.entidades.Admin;
import com.christ.Salud2.repositorios.AdminRepo;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
@Service
public class AdminServicio implements UserDetailsService{
    
     @Autowired
    private AdminRepo adminRepo;
     
         @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        
        Admin admin = adminRepo.buscarPorEmail(email);

        if (admin != null) {

            List<GrantedAuthority> permisos = new ArrayList();

            GrantedAuthority p = new SimpleGrantedAuthority("ROLE_" + admin.getRol().toString());

            permisos.add(p);

            ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();

            HttpSession session = attr.getRequest().getSession(true);

            session.setAttribute("usuariosession", admin);

            return User.builder()
                    .username(admin.getNombre())
                    .password(admin.getPassword())
                    .authorities(permisos)
                    .build();
        } else {
            throw new UsernameNotFoundException("Profesional no encontrado: " + email);
        }

    }


  

}
