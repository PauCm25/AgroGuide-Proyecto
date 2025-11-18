package com.agroguide.auth.domain.usecase;

import com.agroguide.auth.domain.model.Usuario;
import com.agroguide.auth.domain.model.gateway.EncrypterGateway;
import com.agroguide.auth.domain.model.gateway.UsuarioGateway;
import lombok.RequiredArgsConstructor;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
//@Slf4j //inyecta log por medio de Loombok
public class UsuarioUseCase {
    private final UsuarioGateway usuarioGateway;
    private final EncrypterGateway encrypterGateway;

    public Usuario guardarUsuario(Usuario usuario) {
        try{
            validacionCamposUsuario(usuario);
            listaErrores(usuario);
            Usuario usarioExistente=usuarioGateway.buscarPorEmail(usuario.getEmail());
            if(usarioExistente!=null && usarioExistente.getId()!=usuario.getId()){
                throw new IllegalArgumentException("El usuario existe en el sistema");
            }
            String passwordEncriptado= encrypterGateway.encrypt(usuario.getPassword());
            usuario.setPassword(passwordEncriptado);
            return usuarioGateway.guardar(usuario);

        } catch (IllegalArgumentException e) {
            //CORREGIR ESTO DEBE MANDAR UN MENSAJE O ALGO
            throw new IllegalArgumentException("Error validación al guardar usuario: "+e.getMessage());
        }catch (Exception e){
            throw new RuntimeException("Error al guardar el usuario"+e.getMessage());
        }

    }

    private void listaErrores(Usuario usuario) {
        List<String> errores =new ArrayList<>();
        if (usuario.getNombre()==null|| usuario.getNombre().isBlank()){
            errores.add("El nombre es requerido");
        }
        if (usuario.getEmail()==null || usuario.getEmail().isBlank()){
            errores.add("El email es obligatorio");
            //Contains=Verdadero o falso
        }else if(!usuario.getEmail().contains(("@"))){
            errores.add("El email debe tener '@'");
        }
        if (usuario.getPassword()==null || usuario.getPassword().isBlank()){
            errores.add("La contraseña no puede estar vacía");
        }
        if(usuario.getEdad()==null|| usuario.getEdad()<15){
            errores.add("Debe ser mayor de 15 años para registrarse");
        }
        if(!esPasswordSegura(usuario.getPassword())){
           errores.add("La contraseña debe contener 8 caracteres, 1 mayuscula y 1 simbolo");
        }

        if (usuario.getTipoUsuario() != null) {

            String rol = usuario.getTipoUsuario().trim().toUpperCase();
            if (!(rol.equals("AGRICULTOR") || rol.equals("TÉCNICO")
                    || rol.equals("TECNICO") || rol.equals("ADMINISTRADOR"))) {
                errores.add("El rol debe ser AGRICULTOR, TÉCNICO o ADMINISTRADOR");
            }
        }

        if (!errores.isEmpty()) {
            throw new IllegalArgumentException(String.join(" | ", errores));
        }
    }

    private void validacionCamposUsuario(Usuario usuario) {
        if(
                //ISBLANK= verifica si un valor esta nulo o con espacios en blanco
                usuario.getNombre()==null|| usuario.getNombre().isBlank()||
                usuario.getEmail()==null|| usuario.getEmail().isBlank()||
                usuario.getPassword()==null || usuario.getPassword().isBlank()||
                usuario.getUbicacion()==null|| usuario.getUbicacion().isBlank()||
                usuario.getTipoUsuario()==null|| usuario.getTipoUsuario().isBlank()||
                usuario.getEdad()==null){
            throw new IllegalArgumentException("Todos los datos deben ser completados");
        }
    }

    //ESTUDIAR PORQUE ESTATICA
    private boolean esPasswordSegura(String password) {
        return password != null &&
                password.length() >= 8 &&
                password.matches(".*[A-Z].*") &&        // al menos una mayúscula
                password.matches(".*[a-z].*") &&        // al menos una minúscula
                password.matches(".*\\d.*") &&          // al menos un número
                password.matches(".*[!@#$%^&*()_+=<>?/{},\\[\\]\\-].*");  // al menos un símbolo
    }
    public Usuario loginUsuario (String email, String password) {
        Usuario usuario = buscarPorEmail(email);
        if (usuario == null || usuario.getEmail() == null||usuario.getPassword()==null) {
            throw new IllegalArgumentException("Usuario no encontrado");
        }
        boolean passOk=encrypterGateway.checkPass(password, usuario.getPassword());
        if (!passOk){
            throw new IllegalArgumentException("Contraseña incorrecta");
        }
        return usuario;
    }

    public Usuario actualizarUsuario (Usuario usuario) {
        validacionCamposUsuario(usuario);
        listaErrores(usuario);
        if (usuario.getId()==null){
            throw new IllegalArgumentException("Es necesario el ID para actualizar");
        }
        Usuario usuarioExiste;
        try {
            usuarioExiste= buscarPorId(usuario.getId());
            if(usuarioExiste==null||usuarioExiste.getId()==null){
                throw new IllegalArgumentException("Usuario con ID"+usuario.getId()+"no encontrado");
            }
        }catch (Exception e){
            throw new IllegalArgumentException("Usuario con ID "+usuario.getId()+" no encontrado");
        }
        if (usuario.getPassword()==null||usuario.getPassword().isEmpty()){
            throw new IllegalArgumentException("La contraseña es requerida");
        }
        usuario.setPassword(encrypterGateway.encrypt(usuario.getPassword()));
        return usuarioGateway.actualizarUsuario(usuario);
    }
    public void eliminarPorIdUsuario(Long id) {
        try {
            usuarioGateway.elimnarPorID(id);
        } catch (Exception e) {
            throw  new RuntimeException("No se elimino el usuario: ");
        }
    }
    public Usuario buscarPorId(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("El id es invalido");
        }
        try {
            return usuarioGateway.buscarPorID(id);
        }catch (Exception e) {
            throw new RuntimeException("Error al consultar usuario por ID "+ e.getMessage());
        }
    }
    public Usuario buscarPorEmail(String email) {
        try {
            return usuarioGateway.buscarPorEmail(email);
        }catch(Exception e){
            throw new IllegalArgumentException("No existe usuario con el email " + email);
        }
    }

}

