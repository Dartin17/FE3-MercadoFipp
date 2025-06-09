<template>
    <div class="bg-gray-300/30 flex justify-center p-10">
        <div class="container border-gray-300/50 rounded-xl border">
            <h2 class="text-center mb-4">Cadastrar Anúncio</h2>

            <form @submit.prevent="gravarAnuncio" enctype="multipart/form-data">
                <div class="form-group mb-3">
                    <label>Título</label>
                    <input v-model="titulo" class="form-control" required />
                </div>

                <div class="form-group mb-3">
                    <label>Descrição</label>
                    <textarea v-model="descricao" class="form-control" rows="3" required></textarea>
                </div>

                <div class="form-group mb-3">
                    <label>Preço</label>
                    <input v-model="preco" type="number" step="0.01" class="form-control" required />
                </div>

                <div class="form-group mb-3">
                    <label>Categoria</label>
                    <select v-model="idCategoria" class="form-control" required>
                        <option value="" disabled>Selecione uma categoria</option>
                        <option v-for="cat in categorias" :key="cat.id" :value="cat.id">{{ cat.nome }}</option>
                    </select>
                </div>

                <div class="form-group mb-4">
                    <label>Fotos (até 3)</label>
                    <input type="file" multiple accept="image/*" @change="adicionarArquivos" class="form-control" />
                    <small class="text-muted">Selecionadas: {{ fotos.length }} de 3</small>
                </div>

                <div class="preview-container" v-if="fotos.length">
                    <div v-for="(foto, index) in fotos" :key="index" class="preview-box" @click="abrirModal(index)">
                        <img :src="getPreview(foto)" alt="Prévia" />
                        <button type="button" class="remove-btn" @click.stop="removerFoto(index)">✖</button>
                    </div>
                </div>

                <button type="submit" class="btn btn-success w-100 mt-4">Cadastrar Anúncio</button>
            </form>

            <div class="modal-backdrop" v-if="modalAberto" @click="fecharModal">
                <img :src="getPreview(fotos[fotoSelecionada])" class="modal-img" />
            </div>
        </div>
    </div>
</template>

<script>
import axios from "axios";

export default {
    name: "CadAnuncio",
    data() {
        return {
            titulo: "",
            data: new Date().toISOString().slice(0, 10),
            descricao: "",
            preco: "",
            idCategoria: "",
            usuario: null,
            perguntas: [],
            fotos: [],
            categorias: [],
            modalAberto: false,
            fotoSelecionada: 0,
        };
    },
    methods: {
        buscarCategorias() {
            const url = "http://localhost:8080/apis/categoria";
            axios
                .get(url, {
                    headers: {
                        Authorization: JSON.parse(localStorage.getItem("usuario"))?.token,
                    },
                })
                .then((res) => {
                    this.categorias = res.data;
                })
                .catch(() => {
                    alert("Erro ao carregar categorias!");
                });
        },
        adicionarArquivos(event) {
            const novosArquivos = Array.from(event.target.files);
            const todos = [...this.fotos, ...novosArquivos].slice(0, 3);
            this.fotos = todos;
        },
        removerFoto(index) {
            this.fotos.splice(index, 1);
        },
        getPreview(file) {
            return URL.createObjectURL(file);
        },
        abrirModal(index) {
            this.fotoSelecionada = index;
            this.modalAberto = true;
        },
        fecharModal() {
            this.modalAberto = false;
        },
        gravarAnuncio() {
            const url = "http://localhost:8080/apis/anuncio";
            const formData = new FormData();

            const anuncio = {
                titulo: this.titulo,
                data: this.data,
                descricao: this.descricao,
                preco: this.preco,
                categoria: { id: this.idCategoria },
                usuario: this.usuario,
                perguntas: [],
            };

            formData.append("anuncio", new Blob([JSON.stringify(anuncio)], { type: "application/json" }));
            this.fotos.forEach((foto) => {
                formData.append("fotos", foto);
            });

            axios
                .post(url, formData, {
                    headers: {
                        "Content-Type": "multipart/form-data",
                        Authorization: JSON.parse(localStorage.getItem("usuario"))?.token,
                    },
                })
                .then(() => {
                    alert("Anúncio cadastrado com sucesso!");
                    this.resetarFormulario();
                })
                .catch(() => {
                    alert("Erro ao gravar anúncio!");
                });
        },
        resetarFormulario() {
            this.titulo = "";
            this.descricao = "";
            this.preco = "";
            this.idCategoria = "";
            this.fotos = [];
        },
    },
    mounted() {
        this.usuario = JSON.parse(localStorage.getItem("usuario"));
        if (!this.usuario) this.$router.push("/");
        this.buscarCategorias();
    },
};
</script>

<style scoped>
.container {
    max-width: 600px;
    background-color: #ffffff;
    padding: 2.5rem;
    box-shadow: 0 8px 20px rgba(0, 0, 0, 0.08);
}

h2 {
    text-align: center;
    font-size: 2rem;
    margin-bottom: 2rem;
    color: #343a40;
    font-weight: 700;
}

form {
    display: flex;
    flex-direction: column;
    gap: 1.5rem;
}

label {
    font-weight: 600;
    color: #444;
    margin-bottom: 0.5rem;
}

.form-group {
    display: flex;
    flex-direction: column;
}

input.form-control,
textarea.form-control,
select.form-control {
    border: 1px solid #ced4da;
    border-radius: 8px;
    padding: 0.65rem 0.75rem;
    font-size: 1rem;
    width: 100%;
    box-sizing: border-box;
    transition: border-color 0.3s, box-shadow 0.3s;
    background-color: #fdfdfd;
}

input.form-control:focus,
textarea.form-control:focus,
select.form-control:focus {
    border-color: #007bff;
    box-shadow: 0 0 0 0.15rem rgba(0, 123, 255, 0.2);
    outline: none;
}

button[type="submit"] {
    padding: 0.75rem;
    font-size: 1rem;
    font-weight: bold;
    border: none;
    border-radius: 8px;
    background-color: #28a745;
    transition: background-color 0.3s;
    color: #fff;
    width: 100%;
}

button[type="submit"]:hover {
    background-color: #218838;
}

.preview-container {
    display: flex;
    flex-wrap: wrap;
    gap: 1rem;
    margin-top: 1rem;
}

.preview-box {
    position: relative;
    width: 140px;
    height: 140px;
    border: 1px solid #ccc;
    border-radius: 8px;
    overflow: hidden;
    cursor: pointer;
}

.preview-box img {
    width: 100%;
    height: 100%;
    object-fit: cover;
}

.remove-btn {
    position: absolute;
    top: 4px;
    right: 4px;
    background-color: rgba(255, 0, 0, 0.85);
    color: white;
    border: none;
    border-radius: 50%;
    width: 24px;
    height: 24px;
    font-size: 1rem;
    line-height: 1;
    cursor: pointer;
}

.modal-backdrop {
    position: fixed;
    top: 0;
    left: 0;
    width: 100vw;
    height: 100vh;
    background-color: rgba(0, 0, 0, 0.75);
    display: flex;
    align-items: center;
    justify-content: center;
    z-index: 1050;
}

.modal-img {
    max-width: 90vw;
    max-height: 90vh;
    border-radius: 10px;
    box-shadow: 0 0 20px rgba(255, 255, 255, 0.2);
}

@media (max-width: 576px) {
    .container {
        padding: 1.5rem;
        margin: 30px 16px;
    }

    h2 {
        font-size: 1.5rem;
    }
}
</style>
