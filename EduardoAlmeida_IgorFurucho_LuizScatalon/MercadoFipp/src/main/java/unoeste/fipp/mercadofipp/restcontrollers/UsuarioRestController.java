package unoeste.fipp.mercadofipp.restcontrollers;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import unoeste.fipp.mercadofipp.entities.Erro;
import unoeste.fipp.mercadofipp.entities.Usuario;
import unoeste.fipp.mercadofipp.security.JWTTokenProvider;
import unoeste.fipp.mercadofipp.services.UsuarioService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("apis/user")
public class UsuarioRestController {
    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private HttpServletRequest httpServletRequest;

    @GetMapping
    public ResponseEntity<Object> getUsuarios() {
        List<Usuario> usuarioList = usuarioService.getAll();
        if (usuarioList.isEmpty()) {
            return ResponseEntity.badRequest().body("Sem dados");
        }
        return ResponseEntity.ok(usuarioList);
    }

    //GET/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Object> getUsuarioId(@PathVariable(name = "id") Long id){
        Usuario usuario=usuarioService.getById(id);
        if(usuario==null)
            return ResponseEntity.badRequest().body("Usuario não encontrado");
        else
            return ResponseEntity.ok(usuario);
    }


    //POST
    @PostMapping
    public ResponseEntity<Object> addUsuario(@RequestBody Usuario usuario){
        Usuario novoUsuario = usuarioService.save(usuario);
        if(novoUsuario!=null)
            return ResponseEntity.ok(novoUsuario);
        else
            return ResponseEntity.badRequest().body("erro ao cadastrar o novo usuario");
    }

    @PutMapping
    public ResponseEntity<Object> updUsuario(@RequestBody Usuario usuario){
        Usuario novoUsuario = usuarioService.save(usuario);
        if(novoUsuario!=null)
            return ResponseEntity.ok(novoUsuario);
        else
            return ResponseEntity.badRequest().body("erro ao alterar o novo usuario");
    }

    //POST/logar
    @PostMapping("/logar")
    public ResponseEntity<Object> logar(@RequestParam String nome, @RequestParam String senha) {
        String token = usuarioService.autenticar(nome, senha);
        if (token != null) {
            Claims claims = JWTTokenProvider.getAllClaimsFromToken(token);
            Map<String, Object> json = new HashMap<>();
            json.put("id", claims.get("id"));
            json.put("nome", claims.get("nome"));
            json.put("token", token);
            json.put("nivel", claims.get("nivel"));
            return ResponseEntity.ok(json);
        }
        return ResponseEntity.badRequest().body(new Erro("Não foi possível logar no sistema!!"));
    }

    //DELETE/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delUsuario(@PathVariable Long id){
        if(usuarioService.delete(id))
            return ResponseEntity.noContent().build();
        else
            return ResponseEntity.badRequest().body("Erro ao apagar o usuário");
    }
}
