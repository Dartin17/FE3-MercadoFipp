<template>
    <div class="flex justify-center items-center min-h-screen pb-20">
        <div class="bg-gray-200/50 border border-gray-300/50 shadow-md/50 p-8 rounded-lg max-w-lg w-full">
            <h2 class="mb-4 text-center">Crie sua conta</h2>

            <form @submit.prevent="cadastrar">
                <div class="form-group mb-3">
                    <label for="nome">Nome</label>
                    <input id="nome" v-model="nome" type="text" class="form-control" placeholder="Nome de usuário"
                        required />
                </div>

                <div class="form-group mb-3">
                    <label for="senha">Senha</label>
                    <input id="senha" v-model="senha" type="password" class="form-control" placeholder="Senha"
                        required />
                </div>
                <div class="mt-8">
                    <button type="submit" class="btn btn-primary w-full">Cadastrar</button>
                </div>
            </form>

            <p v-if="mensagem" class="mt-3 text-center text-success">{{ mensagem }}</p>
        </div>
    </div>
</template>

<script>
import axios from "axios";

export default {
    name: "CadastroUsuario",
    data() {
        return {
            id: 0,
            nome: "",
            senha: "",
            mensagem: ""
        };
    },
    methods: {
        async cadastrar() {
            try {
                await axios.post("http://localhost:8080/apis/user", {
                    id: 0,
                    nome: this.nome,
                    senha: this.senha,
                    nivel: 1
                });
                this.mensagem = "Cadastro realizado com sucesso!";
                this.nome = this.senha = "";
                localStorage.setItem("cadastroRealizado", "true");
                this.$router.push("/login");
            } catch (err) {
                this.mensagem = "Erro ao criar conta.";
            }
        }
    }
};
</script>

<style scoped>
.container {
    max-width: 480px;
    margin: 60px auto;
    background-color: #ffffff;
    padding: 2rem 2.5rem;
    border-radius: 12px;
    box-shadow: 0 8px 20px rgba(0, 0, 0, 0.08);
}

h2 {
    text-align: center;
    font-size: 2rem;
    font-weight: 600;
    margin-bottom: 1.5rem;
    color: #212529;
}

.form-group {
    display: flex;
    flex-direction: column;
    margin-bottom: 1.2rem;
}

label {
    font-weight: 600;
    color: #495057;
    margin-bottom: 0.5rem;
}

input.form-control {
    padding: 0.7rem 1rem;
    font-size: 1rem;
    border-radius: 8px;
    border: 1px solid #ced4da;
    transition: border-color 0.3s, box-shadow 0.3s;
}

input.form-control:focus {
    border-color: #007bff;
    box-shadow: 0 0 0 0.15rem rgba(0, 123, 255, 0.25);
    outline: none;
}

button.btn {
    font-weight: 500;
    border-radius: 6px;
    padding: 0.5rem;
    transition: background-color 0.3s ease-in-out;
    border: none;
}

button.btn-primary {
    background-color: #007bff;
    color: #fff;
}

button.btn-primary:hover {
    background-color: #0056b3;
}

.text-success {
    text-align: center;
    font-weight: 600;
    margin-top: 1rem;
    color: #28a745;
}
</style>