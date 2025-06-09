package unoeste.fipp.mercadofipp.repositories;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import unoeste.fipp.mercadofipp.entities.Anuncio;

import java.util.List;

public interface AnuncioRepository extends JpaRepository<Anuncio,Long> {
    @Modifying
    @Transactional
    @Query(value = "INSERT INTO pergunta_anuncio (per_text, anu_id) VALUES (:texto, :id_anuncio)", nativeQuery = true)
    public void addPergunta(
            @Param("texto") String texto,
            @Param("id_anuncio") Long id_anuncio
    );

    @Modifying
    @Transactional
    @Query(value = "UPDATE pergunta_anuncio SET per_resp = :resposta WHERE per_id = :id_pergunta", nativeQuery = true)
    public void addResposta(
            @Param("resposta") String resposta,
            @Param("id_pergunta") Long id_pergunta
    );

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO foto_anuncio (fot_file, anu_id, fot_ext) VALUES (:foto, :id_anuncio, :extensao)", nativeQuery = true)
    public void addFoto(
            @Param("foto") byte[] foto,
            @Param("id_anuncio") Long id_anuncio,
            @Param("extensao") String extensao
    );

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM foto_anuncio WHERE anu_id = :id_anuncio", nativeQuery = true)
    public void delFoto(
            @Param("id_anuncio") Long id_anuncio
    );

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM pergunta_anuncio WHERE anu_id = :id_anuncio", nativeQuery = true)
    public void delPergunta(
            @Param("id_anuncio") Long id_anuncio
    );

    @Query(value = "SELECT a.* FROM anuncio a " +
            "JOIN categoria c ON a.cat_id = c.cat_id " +
            "WHERE a.anu_title ILIKE :filtro " +
            "OR CAST(a.anu_date AS TEXT) ILIKE :filtro " +
            "OR a.anu_desc ILIKE :filtro " +
            "OR CAST(a.anu_price AS TEXT) ILIKE :filtro " +
            "OR c.cat_name ILIKE :filtro", nativeQuery = true)
    List<Anuncio> findByFilter(@Param("filtro") String filtro);
}
