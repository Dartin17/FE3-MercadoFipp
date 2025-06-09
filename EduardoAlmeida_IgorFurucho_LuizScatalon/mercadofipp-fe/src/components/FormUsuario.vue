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
                            placeholder="Nome do Usuário..."
                            class="border border-gray-300 rounded px-3 py-2 focus:outline-none focus:ring-2 focus:ring-blue-500"
                        />
                    </div>

                    <div class="flex flex-col">
                        <label for="senha" class="mb-1 text-sm font-medium text-gray-700">Senha</label>
                        <input
                            type="password"
                            id="senha"
                            v-model="senha"
                            placeholder="Senha do Usuário..."
                            class="border border-gray-300 rounded px-3 py-2 focus:outline-none focus:ring-2 focus:ring-blue-500"
                        />
                    </div>

                    <div class="flex flex-col">
                        <label for="nivel" class="mb-1 text-sm font-medium text-gray-700">Nível</label>
                        <select name="nivel" id="nivel" v-model="nivel" class="border border-gray-300 rounded px-3 py-2">
                            <option value="" disabled selected>Selecione o nível de usuário</option>
                            <option value="0">Administrador</option>
                            <option value="1">Usuário</option>
                        </select>
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
                    <h1 class="text-center font-medium text-5xl">Lista de Usuários</h1>
                </article>
                <article class="space-y-8">
                    <nav class="flex justify-between">
                        <div class="space-x-6">
                            <button class="bg-orange-400 px-4 py-2 rounded-md font-medium" @click="this.mostrarForm(true)">Novo Usuario</button>
                            <button class="px-4 py-2 bg-black text-white rounded-md font-medium" @click="carregarDados">Limpar Filtros</button>
                        </div>
                        <div class="space-x-4">
                            <input
                                v-model="filtroTexto"
                                class="py-2 rounded-md border border-gray-300/70 bg-slate-50 pl-2 font-medium placeholder-gray-600/70 sm:w-64 md:w-96"
                                placeholder="Buscar por nome..."
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
                                    <th>Senha</th>
                                    <th>Nivel</th>
                                    <th class="rounded-tr-lg px-4">Ações</th>
                                </tr>
                            </thead>
                            <tbody>
                                <tr v-for="usr in this.usuarios" class="font-medium bg-green-200/30 border border-gray-300">
                                    <td class="p-4">{{ usr.id }}</td>
                                    <td>{{ usr.nome }}</td>
                                    <td>{{ usr.senha }}</td>
                                    <td>{{ usr.nivel }}</td>
                                    <td class="w-[1%] px-4">
                                        <div class="flex gap-10">
                                            <button @click="this.alterar(usr.id, usr.nome)" class="px-4 py-1.5 bg-yellow-300 rounded-md">Alterar</button>
                                            <button @click="this.apagar(usr.id, usr.nome)" class="px-4 py-1.5 bg-red-500 rounded-md">Excluir</button>
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
    name: "FormUsuario",
    props: {
        msg: String,
    },
    data() {
        return {
            id: 0,
            nome: "",
            senha: "",
            nivel: "",
            formOn: false,
            alterando: false,
            filtroTexto: "",
            usuarios: [],
        };
    },
    methods: {
        mostrarForm(flag) {
            this.formOn = flag;
        },
        cancelar() {
            this.id = 0;
            this.nome = "";
            this.senha = "";
            this.nivel = "";
            this.formOn = false;
        },
        gravar() {
            const url = "http://localhost:8080/apis/user";
            const data = {
                id: this.id,
                nome: this.nome,
                senha: this.senha,
                nivel: this.nivel,
            };
            axios
                .post(url, data)
                .then((response) => {
                    this.id = 0;
                    this.nome = "";
                    this.senha = "";
                    this.nivel = "";
                    this.carregarDados();
                    alert(this.alterando ? "Usuário atualizado com sucesso!" : "Usuário cadastrado com sucesso!");
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
                    .delete("http://localhost:8080/apis/user/" + id)
                    .then((result) => {
                        this.carregarDados();
                        alert("Usuário deletado com sucesso!");
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
                .get("http://localhost:8080/apis/user/" + id)
                .then((result) => {
                    const usuario = result.data;
                    this.id = usuario.id;
                    this.nome = usuario.nome;
                    this.senha = usuario.senha;
                    this.nivel = usuario.nivel;
                })
                .catch((error) => {
                    alert(error?.response?.data || "Erro desconhecido");
                });
            alert("Alterando: " + nome);
        },
        carregarDados() {
            this.filtroTexto = "";
            axios
                .get("http://localhost:8080/apis/user")
                .then((result) => {
                    this.usuarios = result.data;
                })
                .catch((error) => {
                    alert(error?.response?.data || "Erro desconhecido");
                });
        },
        async buscarPorTexto() {
            try {
                const resposta = await axios.get(`http://localhost:8080/apis/user/get-usuarios/${this.filtroTexto}`);
                this.tratarUsuarios(resposta.data);
            } catch (err) {
                console.error("Erro ao buscar usuários por texto:", err);
                this.categorias = [];
            }
        },

        tratarUsuarios(lista) {
            const tratadas = lista.map((u) => ({
                ...u,
                nomeNormalizado: u.nome.trim().toLowerCase(),
            }));
            this.usuarios = tratadas;
        },
        async carregarUsuarios() {
            try {
                const resposta = await axios.get("http://localhost:8080/apis/user");
                this.tratarUsuarios(resposta.data);
                this.filtroTexto = "";
            } catch (err) {
                console.error("Erro ao carregar usuários:", err);
            }
        },
    },
    mounted() {
        this.carregarDados();
    },
};
</script>
