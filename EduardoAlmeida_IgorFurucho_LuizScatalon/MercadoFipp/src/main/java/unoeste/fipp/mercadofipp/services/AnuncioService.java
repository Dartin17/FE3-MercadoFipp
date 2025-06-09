package unoeste.fipp.mercadofipp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import unoeste.fipp.mercadofipp.entities.Anuncio;
import unoeste.fipp.mercadofipp.entities.Foto;
import unoeste.fipp.mercadofipp.repositories.AnuncioRepository;
import unoeste.fipp.mercadofipp.repositories.FotoRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class AnuncioService {
    @Autowired
    private AnuncioRepository anuncioRepository;
    @Autowired
    private FotoRepository fotoRepository;

    public List<Anuncio> getAll(){
        return anuncioRepository.findAll();
    }

    public Anuncio getById(long id) {
        return anuncioRepository.findById(id).orElse(null);
    }

    public List<Anuncio> getByUser(long id) {
        List<Anuncio> anuncios = anuncioRepository.findAll();
        List<Anuncio> anunciosByUser = new ArrayList<>();
        for (Anuncio anuncio : anuncios)
            if (anuncio.getUsuario().getId() == id)
                anunciosByUser.add(anuncio);
        return anunciosByUser;
    }

    public List<Anuncio> getByFilter(String filtro) {
        return anuncioRepository.findByFilter(filtro);
    }


    public Anuncio add(Anuncio anuncio, MultipartFile[] fotos)
    {
     Anuncio novoAnuncio = anuncioRepository.save(anuncio);
     if(novoAnuncio != null)
         addFoto(fotos, novoAnuncio.getId());
     return novoAnuncio;
    }

    public boolean addPergunta(long id_anuncio, String texto) {
        try {
            anuncioRepository.addPergunta(texto, id_anuncio);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean addResposta(long id_pergunta, String texto) {
        try {
            anuncioRepository.addResposta(texto, id_pergunta);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean addFoto(MultipartFile[] fotos, long id_anuncio) {
        try {
            for (MultipartFile foto : fotos) {
                byte[] bytes = foto.getBytes();
                String nomeArq = foto.getOriginalFilename();
                String extensao;
                int pos = nomeArq.lastIndexOf(".");
                extensao = nomeArq.substring(pos + 1);
                anuncioRepository.addFoto(bytes, id_anuncio, extensao);
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean delete(Long id){
        Anuncio anuncio=anuncioRepository.findById(id).orElse(null);
        try {
            anuncioRepository.delFoto(anuncio.getId());
            anuncioRepository.delPergunta(anuncio.getId());
            anuncioRepository.delete(anuncio);
            return true;
        }
        catch (Exception e){
            return false;
        }
    }

    public List<Foto> getFotosDoAnuncio(Long idAnuncio) {
        return fotoRepository.findByAnuncioId(idAnuncio);
    }

}
