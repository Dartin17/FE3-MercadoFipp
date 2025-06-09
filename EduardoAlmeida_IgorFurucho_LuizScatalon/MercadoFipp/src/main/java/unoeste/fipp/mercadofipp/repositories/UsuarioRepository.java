package unoeste.fipp.mercadofipp.repositories;

import org.springframework.stereotype.Repository;
import unoeste.fipp.mercadofipp.entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario,Long> {
    public Usuario findByNome(String nome);
}
