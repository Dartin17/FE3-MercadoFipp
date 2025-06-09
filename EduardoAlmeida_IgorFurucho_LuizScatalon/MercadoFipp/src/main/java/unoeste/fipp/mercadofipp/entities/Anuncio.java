package unoeste.fipp.mercadofipp.entities;

import jakarta.persistence.*;

import javax.management.ObjectName;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "anuncio")
public class Anuncio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "anu_id")
    private Long id;
    @Column(name = "anu_title")
    private String titulo;
    @Column(name = "anu_date")
    private LocalDate data;
    @Column(name = "anu_desc")
    private String descricao;
    @Column(name = "anu_price")
    private Double preco;
    @ManyToOne
    @JoinColumn(name = "cat_id", nullable = false)
    private Categoria categoria;
    @ManyToOne
    @JoinColumn(name = "usr_id", nullable = false)
    private Usuario usuario;

    @OneToMany(mappedBy = "anuncio", cascade = CascadeType.ALL)
    private List<Pergunta> perguntas;
    @OneToMany(mappedBy = "anuncio", cascade = CascadeType.ALL)
    private List<Foto> fotos;

    public Anuncio() {
        this(0L,"",null,"",0.0,null,null);
    }

    public Anuncio(Long id, String titulo, LocalDate data, String descricao, Double preco, Categoria categoria, Usuario usuario) {
        this.id = id;
        this.titulo = titulo;
        this.data = data;
        this.descricao = descricao;
        this.preco = preco;
        this.categoria = categoria;
        this.usuario = usuario;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Double getPreco() {
        return preco;
    }

    public void setPreco(Double preco) {
        this.preco = preco;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public List<Pergunta> getPerguntas() {
        return perguntas;
    }

    public void setPerguntas(List<Pergunta> perguntas) {
        this.perguntas = perguntas;
    }

    public List<Foto> getFotos() {
        return fotos;
    }

    public void setFotos(List<Foto> fotos) {
        this.fotos = fotos;
    }
}
