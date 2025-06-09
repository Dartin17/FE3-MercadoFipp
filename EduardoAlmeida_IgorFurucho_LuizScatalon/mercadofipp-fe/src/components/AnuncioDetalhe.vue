<template>
    <section class="bg-gray-300 flex justify-center p-10">
        <div class="bg-slate-50 rounded-xl w-[85dvw] border shadow-md border-gray-400/50 p-10">
            <div class="anuncio-header">
                <h1 class="truncate">{{ anuncio.titulo }}</h1>
                <p class="pt-2 text-lg font-medium">Categoria: {{ anuncio.categoria?.nome }}</p>
                <p class="pt-2 text-lg font-medium">Vendedor: {{ anuncio.usuario?.nome }}</p>
                <p class="pt-2 text-lg font-medium">Data Publicação: {{ anuncio?.data?.replace(/(\d{4})-(\d{2})-(\d{2})/, '$3/$2/$1') ||'-' }}</p>
                <p class="price">{{ anuncio?.preco?.toLocaleString("pt-BR", { style: "currency", currency: "BRL" }) || "-" }}</p>
            </div>

            <div class="my-8 h-px w-full bg-gradient-to-r from-transparent via-zinc-700 to-transparent" />

            <div class="grid grid-cols-5 gap-10">
                <!-- Seção do carrossel de imagens (esquerda) -->
                <div class="image-section col-span-3">
                    <div class="carrossel-container">
                        {{ console.log(anuncio) }}
                        <div class="main-image">
                            <img v-if="imagemAtual" :src="imagemAtual" :alt="`Imagem do anúncio ${anuncio.titulo}`" />
                            <div v-else class="no-image">Sem imagem</div>
                        </div>
                        <div class="thumbnail-container">
                            <div v-for="(foto, index) in anuncio.fotos" :key="index" class="thumbnail" :class="{ active: index === imagemIndex }" @click="mudarImagem(index)">
                                <img :src="foto.img64" :alt="`Miniatura ${index + 1}`" />
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Seção de perguntas/respostas (direita) -->
                <div class="bg-white p-5 rounded-xl border border-gray-400/30 shadow-sm col-span-2">
                    <div class="questions-container">
                        <h2 class="text-lg">Perguntas e Respostas</h2>

                        <div class="my-4 h-px w-full bg-gradient-to-r from-transparent via-zinc-700 to-transparent" />

                        <!-- Lista de perguntas -->
                        <div class="perguntas-list">
                            <div v-for="(pergunta, index) in anuncio.perguntas" :key="index" class="pergunta-item">
                                <div class="texto-pergunta"><strong>❓:</strong> {{ pergunta.texto }}</div>
                                <div v-if="pergunta.resposta" class="texto-resposta"><strong>💬:</strong> {{ pergunta.resposta }}</div>
                                <div v-else-if="isVendedor" class="responder-form">
                                    <textarea v-model="pergunta.novaResposta" placeholder="Digite sua resposta"></textarea>
                                    <button @click="responderPergunta(pergunta)">Responder</button>
                                </div>
                            </div>
                        </div>

                        <!-- Formulário para nova pergunta -->
                        <div v-if="auth.isLogado() && !isVendedor" class="nova-pergunta">
                            <h3>Faça uma pergunta</h3>
                            <textarea v-model="anuncio.novaPergunta" placeholder="Digite sua pergunta sobre o produto"></textarea>
                            <button @click="fazerPergunta">Enviar Pergunta</button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </section>
</template>

<script>
import axios from "axios";
import auth from "@/auth";

export default {
    name: "AnuncioDetalhe",
    data() {
        return {
            auth,
            anuncio: {
                titulo: "",
                preco: 0,
                fotos: [],
                perguntas: [],
                usuario: {},
                novaPergunta: "", // Movido para dentro do anuncio
            },
            imagemIndex: 0,
            respostasTemp: [],
        };
    },
    computed: {
        imagemAtual() {
            return this.anuncio.fotos[this.imagemIndex]?.img64 || "";
        },
        isVendedor() {
            return this.auth.isLogado() && this.auth.usuario?.id === this.anuncio.usuario?.id;
        },
        usuario() {
            return this.auth.usuario; // Acessa o usuário do auth
        },
    },
    async created() {
        await this.carregarAnuncio();
    },
    methods: {
        async carregarAnuncio() {
            try {
                const response = await axios.get(`http://localhost:8080/apis/anuncio/${this.$route.params.id}`);
                console.log(response?.data)
                this.tratarAnuncio(response.data);
            } catch (error) {
                console.error("Erro ao carregar anúncio:", error);
            }
        },
        mudarImagem(index) {
            this.imagemIndex = index;
        },
        async fazerPergunta() {
            if (!this.anuncio.novaPergunta.trim()) return;

            try {
                await axios.post(
                    `http://localhost:8080/apis/anuncio/add-pergunta/${this.anuncio.id}`,
                    { pergunta: this.anuncio.novaPergunta },
                    {
                        headers: {
                            Authorization: `Bearer ${this.usuario?.token}`,
                        },
                    }
                );
                this.anuncio.novaPergunta = "";
                await this.carregarAnuncio();
            } catch (err) {
                console.error("Erro ao enviar pergunta:", err);
            }
        },
        async responderPergunta(pergunta) {
            if (!pergunta.novaResposta.trim()) return;

            try {
                const idPergunta = Number(pergunta.id);
                await axios.post(
                    `http://localhost:8080/apis/anuncio/add-resposta/${idPergunta}`,
                    {
                        resposta: pergunta.novaResposta,
                    },
                    {
                        headers: {
                            Authorization: `Bearer ${this.usuario?.token}`,
                        },
                    }
                );
                pergunta.novaResposta = "";
                await this.carregarAnuncio();
            } catch (err) {
                console.error("Erro ao responder pergunta:", err);
            }
        },
        tratarAnuncio(anuncioData) {
            this.anuncio = {
                ...anuncioData,
                novaPergunta: "",
                perguntas:
                    anuncioData.perguntas?.map((p) => ({
                        ...p,
                        novaResposta: "",
                        // Garanta que estas propriedades existam
                        id: p.id || null,
                        texto: p.texto || p.pergunta || "", // Adapte conforme seu backend
                        resposta: p.resposta || p.textoResposta || null,
                        data: p.data || new Date().toISOString(),
                        dataResposta: p.dataResposta || null,
                    })) || [],
            };
        },
    },
};
</script>

<style scoped>
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

.anuncio-header {
    margin-bottom: 30px;
}

.anuncio-header h1 {
    font-size: 28px;
    margin: 0;
}

.price {
    font-size: 24px;
    color: #2e7d32;
    font-weight: bold;
    margin: 10px 0 0;
}

.image-section {
    background: #f9f9f9;
    border-radius: 8px;
}

.carrossel-container {
    display: flex;
    flex-direction: column;
    gap: 15px;
}

.main-image {
    width: 100%;
    height: 400px;
    display: flex;
    justify-content: center;
    align-items: center;
    background: #fff;
    border: 1px solid #ddd;
    border-radius: 8px;
    overflow: hidden;
}

.main-image img {
    max-width: 100%;
    max-height: 100%;
    object-fit: contain;
}

.thumbnail-container {
    display: flex;
    gap: 10px;
    overflow-x: auto;
    padding-bottom: 10px;
}

.thumbnail {
    width: 80px;
    height: 80px;
    border: 2px solid transparent;
    border-radius: 4px;
    cursor: pointer;
    flex-shrink: 0;
}

.thumbnail.active {
    border-color: #007bff;
}

.thumbnail img {
    width: 100%;
    height: 100%;
    object-fit: cover;
}

.questions-container {
    height: 100%;
    display: flex;
    flex-direction: column;
}

.perguntas-list {
    flex-grow: 1;
    overflow-y: auto;
    max-height: 500px;
    padding-right: 10px;
}

.pergunta-item {
    margin-bottom: 20px;
    padding-bottom: 15px;
    border-bottom: 1px solid #eee;
}

.pergunta,
.resposta {
    margin-bottom: 10px;
    padding: 10px;
    background: #fff;
    border-radius: 4px;
}

.resposta {
    background: #f0f8ff;
    margin-left: 20px;
}

.date {
    display: block;
    font-size: 12px;
    color: #666;
    margin-top: 5px;
}

textarea {
    width: 100%;
    padding: 10px;
    border: 1px solid #ddd;
    border-radius: 4px;
    margin-bottom: 10px;
    resize: vertical;
    min-height: 80px;
}

button {
    background: #007bff;
    color: white;
    border: none;
    padding: 8px 15px;
    border-radius: 4px;
    cursor: pointer;
}

button:hover {
    background: #0056b3;
}

.responder-form {
    margin-top: 10px;
}

.nova-pergunta {
    margin-top: 20px;
    padding-top: 20px;
    border-top: 1px solid #ddd;
}

@media (max-width: 768px) {
    .anuncio-content {
        grid-template-columns: 1fr;
    }

    .main-image {
        height: 300px;
    }
}
</style>
