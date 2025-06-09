<template>
    <div class="bg-gray-300/30 flex justify-center p-10">
        <div class="container flex flex-col gap-8 rounded-xl bg-slate-50 border border-gray-400/30 p-10 min-h-screen">
            <article class="grid gap-16">
                <h2 class="text-2xl font-bold text-center">Administração de Anúncios</h2>

                <!-- Filtros -->
                <div class="grid grid-cols-2 gap-10">
                    <div class="flex gap-3">
                        <div class="flex items-center w-full relative">
                            <span class="absolute pl-2">🔍</span>
                            <input v-model="filtroTexto" class="py-1.5 pl-9 border border-gray-400/30 rounded-md w-full" placeholder="Buscar por texto..." />
                        </div>
                        <button class="px-5 bg-blue-500 rounded-md text-white font-medium" @click="buscarPorTexto">Buscar</button>
                    </div>

                    <div class="flex gap-3">
                        <div class="flex items-center w-full relative">
                            <span class="absolute pl-2">🔍</span>
                            <input v-model.number="filtroUsuario" class="py-1.5 pl-9 border border-gray-400/30 rounded-md w-full" placeholder="Buscar por nome do vendedor..." />
                        </div>
                        <button class="px-5 bg-purple-500 rounded-md text-white font-medium" @click="buscarPorUsuario">Buscar</button>
                    </div>
                </div>

                <!-- Ordenação -->
                <div class="flex gap-4 items-end justify-between">
                    <div class="flex gap-4 font-medium">
                        <strong class="text-lg">Ordenar por:</strong>
                        <button class="bg-amber-400 border border-zinc-500/20 hover:bg-amber-500 px-4 py-1 rounded-md" @click="ordenar('titulo')">Título</button>
                        <button class="bg-amber-400 border border-zinc-500/20 hover:bg-amber-500 px-4 py-1 rounded-md" @click="ordenar('descricao')">Descrição</button>
                        <button class="bg-amber-400 border border-zinc-500/20 hover:bg-amber-500 px-4 py-1 rounded-md" @click="ordenar('preco')">Preço</button>
                        <button class="bg-amber-400 border border-zinc-500/20 hover:bg-amber-500 px-4 py-1 rounded-md" @click="ordenar('categoria')">Categoria</button>
                    </div>
                    <button class="px-5 py-2 bg-zinc-950 text-white font-medium rounded-md" @click="carregarAnuncios">Limpar Filtros</button>
                </div>
            </article>

            <!-- Lista de Anúncios -->
            <article class="grid grid-cols-2 gap-10">
                <div v-for="anuncio in anuncios" :key="anuncio.id" class="flex flex-col gap-6 p-6 bg-white border border-gray-300/50 shadow-xl rounded-xl">
                    <h4 class="text-center text-2xl font-medium truncate">{{ anuncio.titulo }}</h4>

                    <div class="grid gap-2">
                        <p class="truncate"><strong>Descrição:</strong> {{ anuncio.descricao }}</p>
                        <p><strong>Preço:</strong> R$ {{ anuncio.preco }}</p>
                        <p><strong>Usuário:</strong> {{ anuncio.usuario.nome }}</p>
                        <p><strong>Categoria:</strong> {{ anuncio.categoria?.nome }}</p>
                    </div>

                    <div v-if="anuncio.fotos?.length" class="mb-3 flex flex-wrap gap-2">
                        <img v-for="(foto, index) in anuncio.fotos" :key="index" :src="foto.img64" alt="Imagem do anúncio" class="rounded max-w-48 border" />
                    </div>

                    <div v-if="anuncio.perguntas?.length">
                        <h6 class="font-medium">Perguntas:</h6>
                        <ul>
                            <li v-for="(pergunta, index) in anuncio.perguntas" :key="index" class="pergunta-item">
                                <p class="texto-pergunta">❓ {{ pergunta.texto }}</p>
                                <p v-if="pergunta.resposta" class="texto-resposta">💬 <strong>Resposta:</strong> {{ pergunta.resposta }}</p>
                            </li>
                        </ul>
                    </div>

                    <div class="flex justify-end">
                        <button class="px-4 py-2 bg-red-600 text-white font-medium rounded-md hover:bg-red-700" @click="deletar(anuncio.id)">Apagar</button>
                    </div>
                </div>

                <div v-if="Array.isArray(!anuncios) || anuncios.length < 1" class="col-span-2 mt-4">
                    <div class="w-full bg-slate-300 rounded-md py-10 border border-gray-300/50 text-center">
                        <span class="font-medium text-2xl">Nenhum anúncio encontrado.</span>
                    </div>
                </div>
            </article>
        </div>
    </div>
</template>

<script>
import axios from "axios";
export default {
    name: "AdminAnuncios",
    data() {
        return {
            anuncios: [],
            filtroTexto: "",
            filtroUsuario: "",
            ordemOriginal: [],
            ordenacaoAtual: null,
        };
    },
    methods: {
        async carregarAnuncios() {
            this.ordenacaoAtual = null;
            this.filtroUsuario = "";
            this.filtroTexto = "";
            const resposta = await axios.get("http://localhost:8080/apis/anuncio");
            this.tratarAnuncios(resposta.data);
        },
        async buscarPorTexto() {
            const resposta = await axios.get(`http://localhost:8080/apis/anuncio/get-anuncios/${this.filtroTexto}`);
            this.tratarAnuncios(resposta.data);
        },
        async buscarPorUsuario() {
            const resposta = await axios.get(`http://localhost:8080/apis/anuncio/get-por-usuario-nome/${encodeURIComponent(this.filtroUsuario)}`);
            this.tratarAnuncios(resposta.data);
        },
        async deletar(id) {
            if (!confirm("Tem certeza que deseja apagar o anúncio?")) return;
            await axios.delete(`http://localhost:8080/apis/anuncio/${id}`);
            this.carregarAnuncios();
        },
        tratarAnuncios(lista) {
            this.anuncios = lista;
            this.ordemOriginal = [...lista];
        },
        ordenar(campo) {
            this.ordenacaoAtual = campo;
            this.anuncios.sort((a, b) => {
                if (campo === "preco") return parseFloat(a.preco) - parseFloat(b.preco);
                if (campo === "categoria") return (a.categoria?.nome || "").localeCompare(b.categoria?.nome || "");
                return (a[campo] || "").toString().localeCompare((b[campo] || "").toString());
            });
        },
        resetarOrdenacao() {
            this.anuncios = [...this.ordemOriginal];
            this.ordenacaoAtual = null;
        },
    },
    mounted() {
        this.carregarAnuncios();
    },
};
</script>

<style scoped>
.admin-container {
    max-width: 1100px;
    margin: 60px auto;
    background-color: #ffffff;
    padding: 2rem;
    border-radius: 12px;
    box-shadow: 0 8px 20px rgba(0, 0, 0, 0.08);
}

h2 {
    text-align: center;
    font-size: 1.75rem;
    margin-bottom: 2rem;
    color: #343a40;
    font-weight: 700;
}

.filtros {
    display: flex;
    flex-direction: column;
    gap: 1rem;
    margin-bottom: 2rem;
}

.input-group {
    display: flex;
    gap: 0.5rem;
}

.ordenacao {
    margin-bottom: 2rem;
    display: flex;
    flex-wrap: wrap;
    gap: 0.5rem;
    align-items: center;
}

.card-anuncio {
    background-color: #f9f9f9;
    border: 1px solid #ddd;
    border-radius: 10px;
    padding: 1.5rem;
    margin-bottom: 2rem;
    box-shadow: 0 4px 10px rgba(0, 0, 0, 0.04);
}

.card-anuncio h4 {
    font-size: 1.3rem;
    font-weight: bold;
    margin-bottom: 0.5rem;
}

.fotos {
    display: flex;
    flex-wrap: wrap;
    gap: 10px;
    margin-top: 1rem;
}

.foto-preview {
    max-width: 220px;
    border-radius: 8px;
    border: 1px solid #ccc;
}

.perguntas ul {
    padding-left: 1rem;
    margin-top: 1rem;
}

.perguntas li {
    margin-bottom: 0.75rem;
    background: #eef3f7;
    padding: 0.75rem;
    border-left: 4px solid #007bff;
    border-radius: 6px;
}

.acoes {
    margin-top: 1.25rem;
    display: flex;
    justify-content: flex-end;
}

.limpar {
    align-self: flex-start;
    max-width: 200px;
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

.resposta-conteudo {
    margin: 0.25rem 0 0 0.5rem;
    color: #333;
}
</style>
