const express = require('express');
const path = require('path');

const app = express();
// serve arquivos estáticos da pasta "public"
app.use(express.static(path.join(__dirname, 'public')));

const PORT = 3000;
app.listen(PORT, () => {
  console.log(`Frontend rodando em: http://localhost:${PORT}`);
});
