import { createApp } from "vue";
import { createRouter, createWebHistory } from "vue-router";
import axios from "axios";
import App from "./App.vue";
import "./assets/globals.css";
import FormCategoria from "./components/FormCategoria.vue";
import FormUsuario from "./components/FormUsuario.vue";
import FormAnuncio from "./components/FormAnuncio.vue";
import Login from "./components/Login.vue";
import CadAnuncio from "./components/CadAnuncio.vue";
import CadUsuario from "./components/CadUsuario.vue";
import Home from "./components/Home.vue";
import AnuncioDetalhe from "./components/AnuncioDetalhe.vue"

axios.interceptors.request.use((config) => {
    const usuario = JSON.parse(localStorage.getItem("usuario"));
    // console.log(usuario);
    if (usuario && usuario?.token) {
        config.headers.Authorization = usuario?.token;
    }
    return config;
});

const router = createRouter({
    history: createWebHistory(),
    routes: [
        { path: "/", component: Home },
        { path: "/login", component: Login },
        { path: "/cadastro", component: CadUsuario },
        { path: "/anuncioDetalhe/:id", component: AnuncioDetalhe, meta: { requiresAuth: true } },
        { path: "/cadastrar-anuncio", component: CadAnuncio, meta: {requiresAuth: true} },
        { path: "/admin/categorias", component: FormCategoria, meta: { requiresAdmin: true } },
        { path: "/admin/usuarios", component: FormUsuario, meta: { requiresAdmin: true } },
        { path: "/admin/anuncios", component: FormAnuncio, meta: { requiresAdmin: true } },
    ],
});

router.beforeEach((to, from, next) => {
    const usuario = JSON.parse(localStorage.getItem("usuario"));

    if (to.meta.requiresAuth && !usuario) {
        return next("/login");
    }

    if (to.meta.requiresAdmin && (!usuario || Number(usuario?.nivel) !== 0)) {
        return next("/");
    }

    next();
});

const app = createApp(App);
app.use(router);
app.mount("#app");
