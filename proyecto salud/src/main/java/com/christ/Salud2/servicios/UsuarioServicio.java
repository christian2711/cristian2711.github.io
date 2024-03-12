package com.christ.Salud2.servicios;

import com.christ.Salud2.entidades.Ficha;
import com.christ.Salud2.entidades.Imagen;
import com.christ.Salud2.entidades.Usuario;
import com.christ.Salud2.enumeradores.Rol;
import com.christ.Salud2.excepciones.MiException;
import com.christ.Salud2.repositorios.FichaRepo;
import com.christ.Salud2.repositorios.ProfesionalRepo;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.security.core.userdetails.UserDetails;
import com.christ.Salud2.repositorios.UsuarioRepo;


@Service
public class UsuarioServicio implements UserDetailsService {

    @Autowired
    private UsuarioRepo usuarioRepo;

    @Autowired
    private ImagenServicio imagenServicio;

    @Autowired
    private FichaRepo fichaRepo;
    
     @Autowired
    private ProfesionalRepo profesionalRepo;


    @Transactional
    public void registrarPaciente(String nombre, String email, String dni, MultipartFile archivo, String password, String password2,Boolean alta, Boolean obraSocial) throws MiException {

        validar(nombre, email, dni, password, password2);

        Usuario usuario = new Usuario();

        usuario.setNombre(nombre);
        usuario.setEmail(email);
        usuario.setDni(dni);
        usuario.setObraSocial(obraSocial);
        usuario.setAlta(true);
        usuario.setRol(Rol.PACIENTE);
        usuario.setPassword(new BCryptPasswordEncoder().encode(password));
        
        Imagen imagen = imagenServicio.guardar(archivo);

        usuario.setImagen(imagen);

        // Crear una nueva ficha
        Ficha ficha = new Ficha();
        ficha.setUsuario(usuario);

        fichaRepo.save(ficha);
        
        usuarioRepo.save(usuario);

    }

    private void validar(String nombre, String email, String dni, String password, String password2) throws MiException {

        if (nombre.isEmpty() || nombre == null) {
            throw new MiException("el nombre no puede ser nulo o estar vacío");
        }
        if (email.isEmpty() || email == null) {
            throw new MiException("el email no puede ser nulo o estar vacio");
        }
        if (dni.isEmpty() || dni == null) {
            throw new MiException("el dni no puede ser nulo o estar vacio");
        }
        if (password.isEmpty() || password == null || password.length() <= 5) {
            throw new MiException("La contraseña no puede estar vacía, y debe tener más de 5 dígitos");
        }

        if (!password.equals(password2)) {
            throw new MiException("Las contraseñas ingresadas deben ser iguales");
        }

    }

    @Transactional(readOnly = true)
    public List<Usuario> listarUsuarios() {

        List<Usuario> usuarios = new ArrayList();

        usuarios = usuarioRepo.findAll();

        return usuarios;
    }
    
      @Transactional(readOnly = true)
    public List<Usuario> listarPacientes() {

        List<Usuario> usuarios = new ArrayList();

        usuarios = usuarioRepo.buscarPacientes();

        return usuarios;
    }


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Usuario usuario = usuarioRepo.buscarPorEmail(email);

        if (usuario != null) {
            List<GrantedAuthority> permisos = new ArrayList<>();
            GrantedAuthority p = new SimpleGrantedAuthority("ROLE_" + usuario.getRol().toString());
            permisos.add(p);
            ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
            HttpSession session = attr.getRequest().getSession(true);
            session.setAttribute("usuarioSession", usuario);
            return new User(usuario.getEmail(), usuario.getPassword(), permisos);
        } else {
            return null;
        }
    }

    

    @Transactional
    public void actualizar(MultipartFile archivo, String dni, String id, String nombre, String email, boolean obraSocial, String password, String password2) throws MiException {

        validar(nombre, email, dni, password, password2);

        Optional<Usuario> respuesta = usuarioRepo.findById(id);
        if (respuesta.isPresent()) {

            Usuario usuario = respuesta.get();
            usuario.setNombre(nombre);
            usuario.setEmail(email);
            usuario.setDni(dni);
            usuario.setObraSocial(obraSocial);
            usuario.setPassword(new BCryptPasswordEncoder().encode(password));

            usuario.setRol(Rol.PACIENTE);

            String idImagen = null;

            if (usuario.getImagen() != null) {
                idImagen = usuario.getImagen().getId();
            }

            Imagen imagen = (Imagen) imagenServicio.actualizar(archivo, idImagen);

            usuario.setImagen(imagen);

            usuarioRepo.save(usuario);
        }

    }

    public Usuario getOne(String id) {
        return usuarioRepo.getOne(id);
    }
    


 
}
