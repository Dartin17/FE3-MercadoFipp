<template>
    <div>
        <div v-if="usuario">
            <div class="bg-gray-300/30 flex justify-center p-10">
                <div class="container flex flex-col gap-8 rounded-xl bg-slate-50 border border-gray-400/30 p-10 min-h-screen">
                    <article class="grid gap-16">
                        <h2 class="text-xl font-semibold">Lista de Anúncios</h2>
                        <div class="grid grid-cols-2 gap-10">
                            <div class="flex gap-3">
                                <div class="flex items-center w-full">
                                    <span class="absolute pl-2">🔍</span>
                                    <input v-model="filtroTexto" class="py-1.5 pl-9 border border-gray-400/30 rounded-md w-full" placeholder="Buscar por texto..." />
                                </div>
                                <button class="px-5 bg-blue-500 rounded-md text-white font-medium" @click="buscarPorTexto">Buscar</button>
                            </div>
                            <div class="flex gap-3">
                                <div class="flex items-center w-full">
                                    <span class="absolute pl-2">🔍</span>
                                    <input v-model.number="filtroUsuario" class="py-1.5 pl-9 border border-gray-400/30 rounded-md w-full" placeholder="Buscar pelo nome do vendedor..." />
                                </div>
                                <button class="px-5 bg-blue-500 rounded-md text-white font-medium" @click="buscarPorUsuario">Buscar</button>
                            </div>
                        </div>
                        <div class="flex gap-4 items-end justify-between">
                            <div class="flex gap-4 font-medium">
                                <strong class="text-lg">Ordenar por:</strong>
                                <button class="bg-amber-400 border border-zinc-500/20 cursor-pointer hover:bg-amber-500 px-4 py-1 rounded-md" @click="ordenar('titulo')">Título</button>
                                <button class="bg-amber-400 border border-zinc-500/20 cursor-pointer hover:bg-amber-500 px-4 py-1 rounded-md" @click="ordenar('descricao')">Descrição</button>
                                <button class="bg-amber-400 border border-zinc-500/20 cursor-pointer hover:bg-amber-500 px-4 py-1 rounded-md" @click="ordenar('preco')">Preço</button>
                                <button class="bg-amber-400 border border-zinc-500/20 cursor-pointer hover:bg-amber-500 px-4 py-1 rounded-md" @click="ordenar('categoria')">Categoria</button>

                                <button class="px-5 py-2 bg-zinc-950 text-white font-medium rounded-md" @click="carregarAnuncios">Limpar Filtros</button>
                            </div>
                        </div>
                    </article>

                    <article>
                        <div v-if="anuncios.length" class="anuncios-preview">
                            <div class="anuncios-grid">
                                <div v-for="anuncio in anuncios" :key="anuncio.id" class="anuncio-card" @click="verAnuncio(anuncio.id)" style="cursor: pointer">
                                    <h3 class="anuncio-title truncate">{{ anuncio.titulo }}</h3>

                                    <div v-if="anuncio.fotos && anuncio.fotos.length > 0" class="anuncio-image-container">
                                        <img :src="anuncio.fotos[0].img64" alt="Imagem do anúncio" class="anuncio-image" />
                                    </div>
                                    <div v-else class="py-5">
                                        <span>Sem imagens.</span>
                                    </div>

                                    <p class="anuncio-price">Preço: {{ anuncio.preco.toLocaleString("pt-BR", { style: "currency", currency: "BRL" }) }}</p>
                                    <p class="anuncio-seller">Categoria: {{ anuncio.categoria.nome }}</p>
                                    <p class="anuncio-seller">Vendedor: {{ anuncio.usuario.nome }}</p>
                                    <p class="anuncio-seller">Data Publicação: {{ anuncio?.data?.replace(/(\d{4})-(\d{2})-(\d{2})/, '$3/$2/$1') ||'-' }}</p>
                                </div>
                            </div>
                        </div>
                        <div v-else class="col-span-2 mt-4">
                            <div class="w-full bg-slate-300 rounded-md py-10 border border-gray-300/50 text-center">
                                <span class="font-medium text-2xl">Nenhum anuncio encontrado.</span>
                            </div>
                        </div>
                    </article>

                    <div class="modal-backdrop" v-if="modalAberto" @click="fecharModal">
                        <img :src="fotoModal" class="modal-img" />
                    </div>
                </div>
            </div>
        </div>

        <div v-else>
            <div class="home-container">
                <article class="content-wrapper">
                    <div class="welcome-box">
                        <h1 class="display-5">Bem-vindo ao <span class="highlight">Mercado FIPP</span></h1>
                        <p class="lead">Use o menu acima para navegar entre as funcionalidades da plataforma.</p>
                    </div>
                    <div v-if="anuncios.length" class="anuncios-preview">
                        <h2 class="section-title">Últimos anúncios</h2>
                        <div class="anuncios-grid">
                            <div v-for="anuncio in anuncios" :key="anuncio.id" class="anuncio-card">
                                <h3 class="anuncio-title truncate">{{ anuncio.titulo }}</h3>
                                <div v-if="anuncio.fotos && anuncio.fotos.length > 0" class="anuncio-image-container" @click="abrirModal(anuncio.fotos[0].img64)">
                                    <img :src="anuncio.fotos[0].img64" alt="Imagem do anúncio" class="anuncio-image" />
                                </div>
                                <p class="anuncio-price">Preço: {{ anuncio?.preco?.toLocaleString("pt-BR", { style: "currency", currency: "BRL" }) || "-" }}</p>
                                <p class="anuncio-seller">Categoria: {{ anuncio.categoria.nome }}</p>
                                <p class="anuncio-seller">Vendedor: {{ anuncio.usuario.nome }}</p>
                                <p class="anuncio-seller">Data Publicação: {{ anuncio?.data?.replace(/(\d{4})-(\d{2})-(\d{2})/, '$3/$2/$1') ||'-' }}</p>
                            </div>
                        </div>
                    </div>
                </article>
            </div>
        </div>
    </div>
</template>

<script>
import axios from "axios";

export default {
    name: "ListaAnuncios",
    data() {
        return {
            anuncios: [],
            usuario: null,
            filtroTexto: "",
            filtroId: "",
            filtroUsuario: "",
            ordemOriginal: [],
            ordenacaoAtual: null,
            modalAberto: false,
            fotoModal: "",
        };
    },
    methods: {
        isAutor(anuncio) {
            return this.usuario && anuncio.usuario.id === this.usuario.id;
        },
        abrirModal(foto) {
            this.fotoModal = foto;
            this.modalAberto = true;
        },
        fecharModal() {
            this.modalAberto = false;
        },
        verAnuncio(anuncioId) {
            if (this.usuario) {
                this.$router.push(`/anuncioDetalhe/${anuncioId}`);
            }
        },
        async carregarAnuncios() {
            try {
                this.ordenacaoAtual = null;
                this.filtroTexto = "";
                this.filtroUsuario = "";
                const resposta = await axios.get("http://localhost:8080/apis/anuncio");
                this.tratarAnuncios(resposta.data);
            } catch (err) {
                console.error("Erro ao carregar anúncios:", err);
            }
        },
        async buscarPorTexto() {
            try {
                const resposta = await axios.get(`http://localhost:8080/apis/anuncio/get-anuncios/${this.filtroTexto}`);
                this.tratarAnuncios(resposta.data);
            } catch (err) {
                console.error("Erro ao buscar por texto:", err);
            }
        },
        async buscarPorUsuario() {
            try {
                const resposta = await axios.get(`http://localhost:8080/apis/anuncio/get-por-usuario-nome/${encodeURIComponent(this.filtroUsuario)}`);
                this.tratarAnuncios(resposta.data);
            } catch (err) {
                console.error("Erro ao buscar por usuário:", err);
            }
        },
        tratarAnuncios(lista) {
            if (!this.usuario) {
                lista = lista.slice(-5); // mostra os 5 ultimos
            }
            this.anuncios = lista;
            this.ordemOriginal = [...lista];
        },

        async fazerPergunta(anuncio) {
            if (!anuncio.novaPergunta) return;
            try {
                await axios.post(
                    `http://localhost:8080/apis/anuncio/add-pergunta/${this.anuncio.id}`,
                    {
                        pergunta: anuncio.novaPergunta,
                    },
                    {
                        headers: {
                            Authorization: this.usuario?.token,
                        },
                    }
                );
                anuncio.novaPergunta = "";
                this.carregarAnuncios();
            } catch (err) {
                console.error("Erro ao enviar pergunta:", err);
            }
        },
        async responderPergunta(pergunta) {
            if (!pergunta.novaResposta) return;
            try {
                const idPergunta = Number(pergunta.id);
                await axios.post(
                    `http://localhost:8080/apis/anuncio/add-resposta/${idPergunta}`,
                    {
                        resposta: pergunta.novaResposta,
                    },
                    {
                        headers: {
                            Authorization: this.usuario?.token,
                        },
                    }
                );
                pergunta.novaResposta = "";
                this.carregarAnuncios();
            } catch (err) {
                console.error("Erro ao responder pergunta:", err);
            }
        },
        ordenar(campo) {
            this.ordenacaoAtual = campo;
            this.anuncios.sort((a, b) => {
                if (campo === "preco") {
                    return parseFloat(a.preco) - parseFloat(b.preco);
                } else if (campo === "categoria") {
                    return (a.categoria?.nome || "").localeCompare(b.categoria?.nome || "");
                } else {
                    return (a[campo] || "").toString().localeCompare((b[campo] || "").toString());
                }
            });
        },
        resetarOrdenacao() {
            this.anuncios = [...this.ordemOriginal];
            this.ordenacaoAtual = null;
        },
    },
    mounted() {
        this.usuario = JSON.parse(localStorage.getItem("usuario"));
        this.carregarAnuncios();
    },
};
</script>

<style scoped>
.container {
    box-shadow: 0 8px 20px rgba(0, 0, 0, 0.06);
}

h2 {
    text-align: center;
    font-weight: 700;
    margin-bottom: 2rem;
    color: #343a40;
    font-size: 2.5rem;
}

.perguntas ul {
    margin-top: 1rem;
}

.perguntas li {
    margin-bottom: 0.75rem;
    background: #eef3f7;
    padding: 0.75rem;
    border-left: 4px solid #007bff;
    border-radius: 6px;
}

.pergunta-item {
    background: #eef3f7;
    padding: 0.75rem;
    border-left: 4px solid #007bff;
    border-radius: 6px;
    margin-bottom: 1rem;
}

.texto-pergunta {
    margin: 0;
    font-weight: 500;
    color: #333;
}

.texto-resposta {
    margin: 0.5rem 0 0 0;
    padding-left: 1rem;
    border-left: 3px solid #28a745;
    background-color: #f6fdf7;
    border-radius: 4px;
    padding: 0.5rem;
    color: #155724;
}

ul {
    padding-left: 0;
    list-style: none;
    margin-top: 1rem;
}

ul li {
    background: #f8f9fa;
    border-left: 4px solid #007bff;
    padding: 0.75rem 1rem;
    border-radius: 6px;
    margin-bottom: 1rem;
}

ul li span {
    display: block;
    margin-top: 0.5rem;
}
</style>

<style>
.modal-backdrop {
    position: fixed;
    top: 0;
    left: 0;
    width: 100vw;
    height: 100vh;
    background-color: rgba(0, 0, 0, 0.8);
    display: flex;
    align-items: center;
    justify-content: center;
    z-index: 9999;
}

.modal-img {
    max-width: 90vw;
    max-height: 90vh;
    border-radius: 10px;
    box-shadow: 0 0 20px rgba(255, 255, 255, 0.3);
}

.home-container {
    display: flex;
    justify-content: center;
    min-height: calc(100vh - 4rem);
    padding: 2rem;
    background: linear-gradient(135deg, #f0f4f8, #ffffff);
}

.content-wrapper {
    width: 100%;
    max-width: 1200px;
    display: flex;
    flex-direction: column;
    align-items: center;
}

.welcome-box {
    background-color: #ffffff;
    padding: 2rem;
    border-radius: 12px;
    box-shadow: 0 10px 25px rgba(0, 0, 0, 0.08);
    width: 100%;
    text-align: center;
    margin-bottom: 3rem;
    animation: fadeIn 0.6s ease;
}

h1 {
    font-size: 2.2rem;
    margin-bottom: 1rem;
    color: #333;
}

.highlight {
    color: #007bff;
}

.lead {
    font-size: 1.25rem;
    color: #555;
    margin-bottom: 1rem;
}

.section-title {
    font-size: 1.5rem;
    font-weight: 600;
    margin-bottom: 1.5rem;
    text-align: center;
    color: #333;
}

.anuncios-preview {
    width: 100%;
}

.anuncios-grid {
    display: grid;
    grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
    gap: 1.5rem;
    width: 100%;
}

.anuncio-card {
    background: white;
    border-radius: 8px;
    padding: 1rem;
    box-shadow: 0 4px 6px rgba(0, 0, 0, 0.05);
    transition: transform 0.2s ease, box-shadow 0.2s ease;
}

.anuncio-card:hover {
    transform: translateY(-2px);
    box-shadow: 0 6px 12px rgba(0, 0, 0, 0.1);
}

.anuncio-title {
    font-size: 1.1rem;
    font-weight: 600;
    margin-bottom: 0.5rem;
    color: #333;
}

.anuncio-image-container {
    width: 100%;
    margin-bottom: 0.75rem;
    cursor: pointer;
    display: flex;
    justify-content: center;
}

.anuncio-image {
    max-width: 100%;
    height: auto;
    max-height: 180px;
    border-radius: 4px;
    object-fit: contain;
}

.anuncio-price {
    font-size: 0.95rem;
    color: #2e7d32;
    font-weight: 500;
    margin-bottom: 0.25rem;
}

.anuncio-seller {
    font-size: 0.85rem;
    color: #666;
}

@keyframes fadeIn {
    from {
        opacity: 0;
        transform: translateY(20px);
    }
    to {
        opacity: 1;
        transform: translateY(0);
    }
}

@media (max-width: 768px) {
    .home-container {
        padding: 1rem;
    }

    .welcome-box {
        padding: 1.5rem;
    }

    h1 {
        font-size: 1.8rem;
    }

    .lead {
        font-size: 1rem;
    }
}
</style>
