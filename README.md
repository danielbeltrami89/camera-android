# camera-android

Camera criada para funcionar como custom camera em aplicações que exigem fotos tiradas do celular do usuário.

Usa a API Camera 1 do Android.

Na versão atual não é possível finalizar a tela de Preview, ou seja, o aplicativo irá ficar travado a não ser que o usuário
volte para CameraActivity, e após isso voltar a tela anterior, na qual foi chamada. 

Nela é possivel: 
  - Tirar fotos e salvar no SD
  - Pode excluir fotos recem tiradas caso deseje
  - Visualizar estilo lightbox
  - Abrir galeria e escolher as imagens desejadas
  
Para abrir a camera após configurada a biblioteca, basta criar uma nova Intent 

startActivity(new Intent(this, CameraActivity.class));
