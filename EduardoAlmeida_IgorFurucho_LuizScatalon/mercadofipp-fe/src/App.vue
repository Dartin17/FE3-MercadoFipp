<template>
  <div>
    <nav class="navbar">
      <router-link to="/" class="nav-item">🏠 Home</router-link>
      <router-link to="/cadastrar-anuncio" class="nav-item">➕ Cadastrar Anúncio</router-link>

      <template v-if="auth.isLogado()">
        <template v-if="auth.isAdmin()">
          <router-link to="/admin/usuarios" class="nav-item">👥 Usuários</router-link>
          <router-link to="/admin/categorias" class="nav-item">🗂️ Categorias</router-link>
          <router-link to="/admin/anuncios" class="nav-item">🛠️ Gerenciamento de Anúncios</router-link>
        </template>
        <button @click="sair" class="btn-sair">🚪 Sair</button>
      </template>

      <template v-else>
        <router-link to="/login" class="nav-item">🔐 Login</router-link>
        <router-link to="/cadastro" class="nav-item">📝 Criar Conta</router-link>
      </template>
    </nav>

    <main>
      <router-view />
    </main>
  </div>
</template>

<script>
import auth from "@/auth";

export default {
  name: "App",
  setup() {
    return { auth };
  },
  methods: {
    sair() {
      auth.logout();
      this.$router.push("/login");
    }
  }
};
</script>

<style scoped>
/* Menu navbar */
.navbar {
  display: flex;
  flex-wrap: wrap;
  justify-content: center;
  gap: 1rem;
  padding: 1rem;
  background-color: #007bff;
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.1);
}

.nav-item {
  color: white;
  font-weight: bold;
  text-decoration: none;
  padding: 8px 16px;
  border-radius: 6px;
  transition: background-color 0.3s;
  background-color: rgba(255, 255, 255, 0.15);
}

.nav-item:hover {
  background-color: rgba(255, 255, 255, 0.3);
}

.btn-sair {
  background-color: #dc3545;
  color: white;
  font-weight: bold;
  border: none;
  padding: 8px 16px;
  border-radius: 6px;
  cursor: pointer;
  transition: background-color 0.3s;
}

.btn-sair:hover {
  background-color: #c82333;
}

</style>
