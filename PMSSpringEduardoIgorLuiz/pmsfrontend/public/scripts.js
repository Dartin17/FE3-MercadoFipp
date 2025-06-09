document.addEventListener("DOMContentLoaded", () => {
    const listaMusicas = document.getElementById("listaMusicas");
    const form = document.querySelector("form");
    const inputBusca = document.querySelector("input[name='busca']");

    async function carregarMusicas(filtro = "") {
        try {
            const resposta = await fetch('http://localhost:8080/apis/find-musics');

            if (!resposta.ok) {
                listaMusicas.innerHTML = `<p class="text-center text-danger">A pasta de músicas não foi encontrada.</p>`;
                return;
            }

            let musicas = await resposta.json();
            listaMusicas.innerHTML = ""; // Limpa o container

            if (filtro) {
                musicas = musicas.filter(m =>
                    m.titulo.toLowerCase().includes(filtro) ||
                    m.artista.toLowerCase().includes(filtro) ||
                    m.estilo.toLowerCase().includes(filtro)
                );
            }

            if (musicas.length === 0) {
                listaMusicas.innerHTML = filtro
                    ? `<p class="text-center text-muted">Nenhuma música encontrada para "${filtro}".</p>`
                    : `<p class="text-center text-muted">Nenhuma música disponível.</p>`;
                return;
            }

            musicas.forEach(m => {
                const col = document.createElement("div");
                col.className = "col";

                col.innerHTML = `
                    <div class="card shadow-sm h-100">
                        <div class="card-body d-flex flex-column">
                            <h5 class="card-title">${m.titulo}</h5>
                            <p class="card-text mb-1"><strong>Artista:</strong> ${m.artista}</p>
                            <p class="card-text mb-2"><strong>Estilo:</strong> ${m.estilo}</p>
                            <video controls class="mt-auto w-100">
                                <source src="${m.fileName}" type="video/mp4">
                                Seu navegador não suporta áudio.
                            </video>
                        </div>
                    </div>
                `;
                listaMusicas.appendChild(col);
            });

            // Pausar outras músicas ao dar play
            const players = listaMusicas.querySelectorAll("audio");
            players.forEach(player => {
                player.addEventListener("play", () => {
                    players.forEach(other => {
                        if (other !== player) {
                            other.pause();
                            other.currentTime = 0;
                        }
                    });
                });
            });

        } catch (erro) {
            console.error("Erro ao carregar músicas:", erro);
            listaMusicas.innerHTML = `<p class="text-center text-danger">A pasta de músicas não foi encontrada.</p>`;
        }
    }

    form.addEventListener("submit", (e) => {
        e.preventDefault(); // Evita o recarregamento da página
        const termoBusca = inputBusca.value.trim().toLowerCase();
        carregarMusicas(termoBusca);
    });

    carregarMusicas(); // Carrega todas as músicas inicialmente
});
