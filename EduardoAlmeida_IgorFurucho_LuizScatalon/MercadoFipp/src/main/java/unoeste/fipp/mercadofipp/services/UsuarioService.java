package unoeste.fipp.mercadofipp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import unoeste.fipp.mercadofipp.entities.Usuario;
import unoeste.fipp.mercadofipp.repositories.UsuarioRepository;
import unoeste.fipp.mercadofipp.security.JWTTokenProvider;

import java.util.List;

@Service
public class UsuarioService {
    @Autowired
    private UsuarioRepository usuarioRepo;
    public List<Usuario> getAll() {

        return usuarioRepo.findAll();
    }

    public Usuario getById(Long id){

        return usuarioRepo.findById(id).orElse(null);
    }

    public Usuario save(Usuario usuario)
    {
        try {
            Usuario novoUsuario = usuarioRepo.save(usuario);
            return novoUsuario;
        }catch (Exception e) {
            return null;
        }
    }
    public boolean delete(Long id){
        Usuario usuario=usuarioRepo.findById(id).orElse(null);
        try {
            usuarioRepo.delete(usuario);
            return true;
        }
        catch (Exception e){
            return false;
        }
    }

    private Usuario getAutenticar(String nome, String senha) {
        Usuario usuario = usuarioRepo.findByNome(nome);
        if (usuario != null)
            if (usuario.getSenha().equals(senha))
                return usuario;
        return null;
    }

    public String autenticar(String nome, String senha) {
        Usuario consultado = getAutenticar(nome, senha);
        if (consultado != null)
            return JWTTokenProvider.getToken(consultado.getNome(), consultado.getId(), consultado.getNome(), "" + consultado.getNivel());
        return null;
    }
}
