package unoeste.fipp.pmsbackend.restcontrollers;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import unoeste.fipp.pmsbackend.entities.Erro;
import unoeste.fipp.pmsbackend.entities.Music;

import java.io.File;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(value = "apis")
public class MusicRestController {

    @Autowired
    private HttpServletRequest request;
    private final String UPLOAD_FOLDER = "src/main/resources/static/uploads/";

    @PostMapping(value = "add-music")
    public ResponseEntity<Object> addUsuario(@RequestParam("musica") MultipartFile file,
                                             @RequestParam("titulo") String titulo,
                                             @RequestParam("artista") String artista,
                                             @RequestParam("estilo") String estilo)
    {

        try {
            //cria uma pasta na área estática para acomodar os arquivos recebidos, caso não exista
            File uploadFolder = new File(UPLOAD_FOLDER);
            if (!uploadFolder.exists()) uploadFolder.mkdir();
            //criar um nome para no arquivo
            file.transferTo(new File(uploadFolder.getAbsolutePath() + "\\"+file.getOriginalFilename()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new Erro("Erro ao armazenar o arquivo:" + e.getMessage()));
        }
        return ResponseEntity.ok(new Music(titulo,artista,estilo,file.getOriginalFilename()));
    }
    @GetMapping(value = "find-musics")
    public ResponseEntity<Object> findMusics(){
        File uploadFolder = new File(UPLOAD_FOLDER);
        String[] files = uploadFolder.list();

        List<Music> musicList = new ArrayList<>();
        for
        if (files == null || files.length == 0) {
            return ResponseEntity.ok(musicList);
        }

        for (String f : files) {
            if (!f.endsWith(".mp4")) continue;

            String nomeSemExtensao = f.replace(".mp4", "");
            String[] partes = nomeSemExtensao.split("_");

            String titulo = partes.length > 0 ? partes[0] : "Desconhecido";
            String estilo = partes.length > 1 ? partes[1] : "Desconhecido";
            String artista = partes.length > 2 ? partes[2] : "Desconhecido";

            String url = getHostStatic() + f;
            musicList.add(new Music(titulo, artista, estilo, url));
        }

        return ResponseEntity.ok(musicList);
    }
    public String getHostStatic() {
        return "http://"+request.getServerName().toString()+":"+request.getServerPort()+"/uploads/";
    }
}
