// auth.js
import { reactive } from "vue";

const auth = reactive({
  usuario: JSON.parse(localStorage.getItem("usuario")) || null,

  login(usuario) {
    this.usuario = usuario;
    localStorage.setItem("usuario", JSON.stringify(usuario));
  },

  logout() {
    this.usuario = null;
    localStorage.removeItem("usuario");
  },

  isLogado() {
    return this.usuario !== null;
  },

  isAdmin() {
    return this.usuario && Number(this.usuario?.nivel) === 0;
  },

  isUsuario() {
    return this.usuario && Number(this.usuario?.nivel) === 1;
  }
});

export default auth; // Adicione esta linha