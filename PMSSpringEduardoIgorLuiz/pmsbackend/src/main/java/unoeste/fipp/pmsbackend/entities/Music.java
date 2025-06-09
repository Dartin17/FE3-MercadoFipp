package unoeste.fipp.pmsbackend.entities;

public class Music {
    private String titulo, artista, estilo, fileName;

    public Music(String titulo, String artista, String estilo, String fileName) {
        this.titulo = titulo;
        this.artista = artista;
        this.estilo = estilo;
        this.fileName = fileName;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getArtista() {
        return artista;
    }

    public void setArtista(String artista) {
        this.artista = artista;
    }

    public String getEstilo() {
        return estilo;
    }

    public void setEstilo(String estilo) {
        this.estilo = estilo;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
