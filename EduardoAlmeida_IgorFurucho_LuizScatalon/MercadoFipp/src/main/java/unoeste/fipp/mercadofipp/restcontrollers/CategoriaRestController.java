package unoeste.fipp.mercadofipp.restcontrollers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import unoeste.fipp.mercadofipp.entities.Categoria;
import unoeste.fipp.mercadofipp.entities.Erro;
import unoeste.fipp.mercadofipp.services.CategoriaService;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("apis/categoria")
public class CategoriaRestController {
    @Autowired
    private CategoriaService categoriaService;

    //GET
    @GetMapping
    public ResponseEntity<Object> getAll() {
        List<Categoria> categoriaList = categoriaService.getAll();
        if (categoriaList != null && !categoriaList.isEmpty())
            return ResponseEntity.ok(categoriaList);
        return ResponseEntity.badRequest().body(new Erro("categorias não encontradas"));
    }

    //POST
    @PostMapping
    public ResponseEntity<Object> save(@RequestBody Categoria categoria) {
        Categoria novaCategoria = categoriaService.save(categoria);
        if (categoria != null)
            return ResponseEntity.ok(novaCategoria);
        else
            return ResponseEntity.badRequest().body("Erro ao cadastrar a categoria");
    }

    //PUT
    @PutMapping
    public ResponseEntity<Object> updCategoria(@RequestBody Categoria categoria) {
        Categoria novaCategoria = categoriaService.save(categoria);
        if (novaCategoria != null)
            return ResponseEntity.ok(novaCategoria);
        else
            return ResponseEntity.badRequest().body("erro ao alterar a categoria");
    }

    //GET/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Object> getCategoriaId(@PathVariable(name = "id") Long id) {
        Categoria categoria = categoriaService.getById(id);
        if (categoria == null)
            return ResponseEntity.badRequest().body("Categoria não encontrada");
        else
            return ResponseEntity.ok(categoria);
    }

    //DELETE/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delCategoria(@PathVariable Long id) {
        if (categoriaService.delete(id))
            return ResponseEntity.noContent().build();
        else
            return ResponseEntity.badRequest().body("Erro ao apagar a categoria");
    }
}
