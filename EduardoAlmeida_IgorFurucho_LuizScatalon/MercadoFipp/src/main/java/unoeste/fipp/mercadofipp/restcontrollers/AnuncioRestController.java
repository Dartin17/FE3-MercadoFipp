package unoeste.fipp.mercadofipp.restcontrollers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import unoeste.fipp.mercadofipp.entities.Anuncio;
import unoeste.fipp.mercadofipp.entities.Erro;
import unoeste.fipp.mercadofipp.services.AnuncioService;

import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("apis/anuncio")
public class AnuncioRestController {
    @Autowired
    private AnuncioService anuncioService;

    @GetMapping
    public ResponseEntity<Object> getAll(){
        List<Anuncio> anuncios = anuncioService.getAll();
        return ResponseEntity.ok(anuncios);
    }

    //GET/{id}
    @GetMapping(value = "/{id}")
    public ResponseEntity<Object> getAnuncioId(@PathVariable(name = "id") Long id) {
        Anuncio anuncio = anuncioService.getById(id);
        if (anuncio != null)
            return ResponseEntity.ok(anuncio);
        return ResponseEntity.badRequest().body(new Erro("Anúncio não encontrado!"));
    }

    //GET/get-por-usuario/{id}
    @GetMapping(value = "/get-por-usuario/{id}")
    public ResponseEntity<Object> getAnuncioByUser(@PathVariable(name = "id") Long id) {
        List<Anuncio> anuncios = anuncioService.getByUser(id);
        if (anuncios != null)
            return ResponseEntity.ok(anuncios);
        return ResponseEntity.badRequest().body(new Erro("Anúncios não encontrados!"));
    }

    //GET/get-anuncios/{filtro}
    @GetMapping(value = "get-anuncios/{filtro}")
    public ResponseEntity<Object> getAnunciosByFilter(@PathVariable(name = "filtro") String filtro) {
        List<Anuncio> anuncios = anuncioService.getByFilter("%" + filtro + "%");

        if (anuncios != null)
            return ResponseEntity.ok(anuncios);
        return ResponseEntity.badRequest().body(new Erro("Anúncios não encontrados!"));
    }

    //POST
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Object> save(@RequestPart("anuncio") Anuncio anuncio, @RequestPart("fotos") MultipartFile[] fotos)
    {
        Anuncio novoAnuncio = anuncioService.add(anuncio, fotos);
        if(novoAnuncio!=null)
            return ResponseEntity.ok(novoAnuncio);
        else
            return ResponseEntity.badRequest().body(new Erro("Erro ao gravar"));
    }

    //POST/add-pergunta/{id}/{texto}
    @PostMapping(value = "add-pergunta/{id}")
    public ResponseEntity<Object> addPergunta(@PathVariable(name = "id") Long idAnuncio, @RequestBody Map dados) {
        if (anuncioService.addPergunta(idAnuncio, (String) dados.get("pergunta")))
            return ResponseEntity.noContent().build();
        return ResponseEntity.badRequest().body(new Erro("Erro ao adicionar a pergunta!"));
    }

    //POST/add-resposta/{id}/{resposta}
    @PostMapping(value = "add-resposta/{id}")
    public ResponseEntity<Object> addResposta(@PathVariable(name = "id") Long id, @RequestBody Map dados) {
        if (anuncioService.addResposta(id, (String) dados.get("resposta")))
            return ResponseEntity.noContent().build();
        return ResponseEntity.badRequest().body(new Erro("Erro ao adicionar resposta!"));
    }

    //DELETE/{id}
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Object> delete(@PathVariable Long id) {
        if (anuncioService.delete(id)) {
            return ResponseEntity.noContent().build();
        }
        else{
            return ResponseEntity.badRequest().body(new Erro("Erro ao apagar anuncio!"));
        }
    }

}
