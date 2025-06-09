<template>
    <div class="flex justify-center items-center min-h-screen pb-20">
        <div class="bg-gray-200/50 border border-gray-300/50 shadow-md/50 p-8 rounded-lg max-w-lg w-full">
            <h1 class="mb-4 text-center">Acesse sua conta</h1>
            <form @submit.prevent="logar">
                <div class="form-group mb-3">
                    <label for="usuario">Usuário</label>
                    <input type="text" class="form-control" id="usuario" v-model="nome" placeholder="Informe o usuário"
                        required />
                </div>

                <div class="form-group mb-3">
                    <label for="senha">Senha</label>
                    <input type="password" class="form-control" id="senha" v-model="senha" placeholder="Senha"
                        required />
                </div>

                <div class="mt-8">
                    <button type="submit" class="btn w-full btn-warning">Entrar</button>
                </div>
            </form>

            <div class="text-center mt-4">
                <p>
                    Não tem uma conta?
                    <router-link to="/cadastro" class="btn-link">Crie sua conta aqui</router-link>
                </p>
            </div>
        </div>
    </div>
</template>

<script>
import axios from "axios";
import auth from "@/auth";

export default {
    name: "Login",
    data() {
        return {
            auth,
            nome: "",
            senha: ""
        };
    },
    methods: {
        async logar() {
            try {
                const url = "http://localhost:8080/apis/user/logar";
                const params = new URLSearchParams();
                params.append("nome", this.nome);
                params.append("senha", this.senha);

                const resposta = await axios.post(url, params);
                this.auth.login(resposta.data); // ✅ login via auth.js

                this.$router.push("/");

            } catch (erro) {
                alert("Erro ao tentar entrar, confira seu nome de usuário ou senha!");
            }
        }
    },
    mounted() {
        if (this.auth.usuario && this.auth.usuario?.token) {
            this.$router.push("/");
            return;
        }

        if (localStorage.getItem("cadastroRealizado")) {
            alert("Seu cadastro foi realizado com sucesso!");
            localStorage.removeItem("cadastroRealizado");
        }

        document.body.style.overflow = "hidden";
    },
    unmounted() {
        document.body.style.overflow = "auto";
    }
};
</script>

<style scoped>
.login-container {
    max-width: 400px;
    margin: 80px auto;
    padding: 2.5rem 2rem;
    background-color: #ffffff;
    border-radius: 12px;
    box-shadow: 0 8px 20px rgba(0, 0, 0, 0.08);
    border: 1px solid #e4e4e4;
}

h1 {
    text-align: center;
    font-size: 2rem;
    font-weight: 600;
    margin-bottom: 1.5rem;
    color: #212529;
}

.form-group label {
    font-weight: 500;
    color: #495057;
    margin-bottom: 0.5rem;
    display: block;
}

.form-control {
    padding: 0.6rem 0.75rem;
    font-size: 0.95rem;
    border-radius: 6px;
    border: 1px solid #ced4da;
    transition: border-color 0.2s;
    width: 100%;
}

.form-control:focus {
    border-color: #80bdff;
    outline: none;
}

.d-flex {
    display: flex;
    gap: 10px;
}

.w-48 {
    width: 48%;
}

.btn-warning {
    background-color: #ffc107;
    border: none;
    color: #212529;
    font-weight: 500;
    border-radius: 6px;
    padding: 0.5rem;
}

.btn-warning:hover {
    background-color: #e0a800;
}

.btn-secondary {
    background-color: #6c757d;
    border: none;
    color: #fff;
    font-weight: 500;
    border-radius: 6px;
    padding: 0.5rem;
    text-decoration: none;
}

.btn-secondary:hover {
    background-color: #5a6268;
}

.btn-link {
    font-weight: 500;
    color: #007bff;
    text-decoration: none;
}

.btn-link:hover {
    text-decoration: underline;
}

.text-center {
    text-align: center;
}

.mt-3 {
    margin-top: 1rem;
}

.mt-4 {
    margin-top: 1.5rem;
}
</style>