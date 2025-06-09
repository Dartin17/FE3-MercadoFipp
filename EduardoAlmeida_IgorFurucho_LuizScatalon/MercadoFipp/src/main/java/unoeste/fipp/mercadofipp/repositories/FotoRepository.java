package unoeste.fipp.mercadofipp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import unoeste.fipp.mercadofipp.entities.Foto;

import java.util.List;

public interface FotoRepository extends JpaRepository<Foto, Long> {
    List<Foto> findByAnuncioId(Long anuncioId); // busca todas as fotos de um anúncio
}