<template>
    <div class="bg-gray-200">
        <h1>{{ msg }}</h1>
        <main v-if="formOn" class="px-20 pt-10">
            <section class="p-10 rounded-xl flex justify-center border-gray-400/30 border bg-slate-50">
                <form @submit.prevent="this.gravar()" class="grid gap-6 bg-white w-full max-w-lg border-gray-400/30 border p-6 rounded-xl shadow-md/50">
                    <div class="flex flex-col">
                        <label for="name" class="mb-1 text-sm font-medium text-gray-700">Nome</label>
                        <input
                            type="text"
                            id="name"
                            v-model="nome"
                            placeholder="Nome da Categoria..."
                            class="border border-gray-300 rounded px-3 py-2 focus:outline-none focus:ring-2 focus:ring-blue-500"
                        />
                    </div>

                    <button
                        type="submit"
                        :class="`${this.alterando == true ? 'bg-yellow-400 hover:bg-yellow-700 text-black' : 'bg-blue-600 hover:bg-blue-700'} text-white font-medium py-2 px-4 rounded transition`"
                    >
                        {{ this.alterando ? "Alterar" : "Cadastrar" }}
                    </button>
                    <button type="button" @click="this.cancelar()" class="bg-red-600 text-white font-medium py-2 px-4 rounded hover:bg-red-700 transition">Cancelar</button>
                </form>
            </section>
        </main>

        <main class="px-20 py-10">
            <section class="p-10 space-y-20 rounded-xl min-h-screen border-gray-400/30 border bg-slate-50">
                <article>
                    <h1 class="text-center font-medium text-5xl">Lista de Categorias</h1>
                </article>
                <article class="space-y-8">
                    <nav class="flex justify-between">
                        <div class="space-x-6">
                            <button class="bg-orange-400 px-4 py-2 rounded-md font-medium" @click="this.mostrarForm(true)">Novo Categoria</button>
                            <button class="px-4 py-2 bg-black text-white rounded-md font-medium" @click="carregarCategorias">Limpar Filtros</button>
                        </div>
                        <div class="space-x-4">
                            <input
                                v-model="filtroTexto"
                                class="py-2 rounded-md border border-gray-300/70 bg-slate-50 pl-2 font-medium placeholder-gray-600/70 sm:w-64 md:w-96"
                                placeholder="Buscar categoria por nome..."
                            />
                            <button class="px-5 py-2 bg-blue-500 font-medium text-white rounded-md" @click="buscarPorTexto">Filtrar</button>
                        </div>
                    </nav>

                    <div>
                        <table class="w-full text-left">
                            <thead>
                                <tr class="bg-green-500/90 text-lg border-gray-300">
                                    <th class="p-4 rounded-tl-lg">Id</th>
                                    <th>Nome</th>
                                    <th class="rounded-tr-lg px-4">Ações</th>
                                </tr>
                            </thead>
                            <tbody>
                                <tr v-for="cat in this.categorias" class="font-medium bg-green-200/30 border border-gray-300">
                                    <td class="p-4">{{ cat.id }}</td>
                                    <td>{{ cat.nome }}</td>
                                    <td class="w-[1%] px-4">
                                        <div class="flex gap-10">
                                            <button @click="this.alterar(cat.id, cat.nome)" class="px-4 py-1.5 bg-yellow-300 rounded-md">Alterar</button>
                                            <button @click="this.apagar(cat.id, cat.nome)" class="px-4 py-1.5 bg-red-500 rounded-md">Excluir</button>
                                        </div>
                                    </td>
                                </tr>
                            </tbody>
                        </table>
                    </div>
                </article>
            </section>
        </main>
    </div>
</template>

<script>
import axios from "axios";

export default {
    name: "FormCategoria",
    props: {
        msg: String,
    },
    data() {
        return {
            id: 0,
            nome: "",
            formOn: false,
            alterando: false,
            filtroTexto: "",
            categorias: [],
        };
    },
    methods: {
        mostrarForm(flag) {
            this.formOn = flag;
        },
        cancelar() {
            this.id = 0;
            this.nome = "";
            this.formOn = false;
        },
        gravar() {
            const url = "http://localhost:8080/apis/categoria";
            const data = {
                id: this.id,
                nome: this.nome,
            };
            axios
                .post(url, data)
                .then((response) => {
                    this.id = 0;
                    this.nome = "";
                    this.senha = "";
                    this.nivel = "";
                    this.carregarDados();
                    alert(this.alterando ? "Categoria atualizado com sucesso!" : "Categoria cadastrado com sucesso!");
                })
                .catch((error) => {
                    alert(error?.response?.data || "Erro desconhecido");
                });
            this.mostrarForm(false);
        },
        apagar(id, nome) {
            const response = confirm("Deseja realmente excluir: " + nome);
            if (response) {
                axios
                    .delete("http://localhost:8080/apis/categoria/" + id)
                    .then((result) => {
                        this.carregarDados();
                        alert("Categoria deletada com sucesso!");
                    })
                    .catch((error) => {
                        alert(error?.response?.data || "Erro desconhecido");
                    });
            }
        },
        alterar(id, nome) {
            this.alterando = true;
            this.formOn = true;
            axios
                .get("http://localhost:8080/apis/categoria/" + id)
                .then((result) => {
                    const categoria = result.data;
                    this.id = categoria.id;
                    this.nome = categoria.nome;
                })
                .catch((error) => {
                    alert(error?.response?.data || "Erro desconhecido");
                });
            alert("Alterando: " + nome);
        },
        carregarDados() {
            this.filtroTexto = "";
            axios
                .get("http://localhost:8080/apis/categoria")
                .then((result) => {
                    this.categorias = result.data;
                })
                .catch((error) => {
                    alert(error?.response?.data || "Erro desconhecido");
                });
        },
        async buscarPorTexto() {
            try {
                const resposta = await axios.get(`http://localhost:8080/apis/categoria/get-categorias/${this.filtroTexto}`);
                this.tratarCategorias(resposta.data);
            } catch (err) {
                console.error("Erro ao buscar categorias por texto:", err);
                this.categorias = [];
            }
        },

        tratarCategorias(lista) {
            const tratadas = lista.map((c) => ({
                ...c,
                nomeNormalizado: c.nome.trim().toLowerCase(),
            }));
            this.categorias = tratadas;
        },
        async carregarCategorias() {
            try {
                const resposta = await axios.get("http://localhost:8080/apis/categoria");
                this.tratarCategorias(resposta.data);
                this.filtroTexto = "";
            } catch (err) {
                console.error("Erro ao carregar categorias:", err);
            }
        },
    },
    mounted() {
        this.carregarDados();
    },
};
</script>
