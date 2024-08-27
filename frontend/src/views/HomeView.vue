<template>
  <div class="min-h-screen flex flex-col bg-gray-50">

    <header class="bg-white shadow-sm">
      <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-4">
        <div class="flex justify-between items-center">
          <div class="flex items-center flex-shrink-0">
            <img src="/assets/img/logo.png" alt="MeetSpace Logo" class="h-8 w-auto sm:h-10">
          </div>
          <div class="flex items-center space-x-4">
            <router-link to="/login" class="text-gray-600 hover:text-gray-800 px-3 py-2 rounded-md text-sm font-medium">Entrar</router-link>
            <router-link to="/register" class="bg-blue-600 text-white hover:bg-blue-700 px-4 py-2 rounded-md text-sm font-medium transition duration-300 ease-in-out">Cadastrar</router-link>
          </div>
        </div>
      </div>
    </header>


    <main class="flex-grow">
      <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-12">
        <div class="lg:flex lg:items-center lg:justify-between">

          <div class="lg:w-1/2 lg:pr-12">
            <h1 class="text-4xl font-bold text-gray-900 mb-4 lg:text-5xl lg:leading-tight">Bem-vindo ao MeetSpace</h1>
            <p class="text-xl text-gray-600 mb-8">Gerencie suas reservas de salas de reunião de forma fácil e eficiente.</p>
            <div class="flex flex-col sm:flex-row space-y-4 sm:space-y-0 sm:space-x-4">
              <button class="w-full sm:w-auto bg-blue-600 text-white px-6 py-3 rounded-md text-lg font-medium hover:bg-blue-700 transition duration-300">Ver Disponibilidade</button>
              <button class="w-full sm:w-auto border-2 border-blue-600 text-blue-600 px-6 py-3 rounded-md text-lg font-medium hover:bg-blue-50 transition duration-300">Gerenciar Reservas</button>
            </div>
          </div>


          <div class="mt-10 lg:mt-0 lg:w-1/2">
            <img src="/assets/img/placeholder.png?height=400&width=600" alt="Ilustração MeetSpace" class="w-full h-auto rounded-lg shadow-lg">
          </div>
        </div>


        <div class="mt-20">
          <h2 class="text-3xl font-semibold text-gray-900 mb-8 text-center">Recursos do Sistema</h2>
          <div class="grid grid-cols-1 md:grid-cols-3 gap-8">
            <div class="bg-white p-6 rounded-lg shadow-md">
              <div class="flex items-center mb-4">
                <svg class="h-6 w-6 text-blue-500 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z"></path></svg>
                <h3 class="text-xl font-semibold text-gray-900">Disponibilidade em tempo real</h3>
              </div>
              <p class="text-gray-600">Visualize a disponibilidade das salas instantaneamente e faça reservas com facilidade.</p>
            </div>
            <div class="bg-white p-6 rounded-lg shadow-md">
              <div class="flex items-center mb-4">
                <svg class="h-6 w-6 text-blue-500 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5H7a2 2 0 00-2 2v12a2 2 0 002 2h10a2 2 0 002-2V7a2 2 0 00-2-2h-2M9 5a2 2 0 002 2h2a2 2 0 002-2M9 5a2 2 0 012-2h2a2 2 0 012 2m-3 7h3m-3 4h3m-6-4h.01M9 16h.01"></path></svg>
                <h3 class="text-xl font-semibold text-gray-900">Gerenciamento de reservas</h3>
              </div>
              <p class="text-gray-600">Gerencie todas as suas reservas em um só lugar, com opções de edição e cancelamento.</p>
            </div>
            <div class="bg-white p-6 rounded-lg shadow-md">
              <div class="flex items-center mb-4">
                <svg class="h-6 w-6 text-blue-500 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 6V4m0 2a2 2 0 100 4m0-4a2 2 0 110 4m-6 8a2 2 0 100-4m0 4a2 2 0 110-4m0 4v2m0-6V4m6 6v10m6-2a2 2 0 100-4m0 4a2 2 0 110-4m0 4v2m0-6V4"></path></svg>
                <h3 class="text-xl font-semibold text-gray-900">Painel administrativo</h3>
              </div>
              <p class="text-gray-600">Acesse estatísticas e relatórios detalhados sobre o uso das salas de reunião.</p>
            </div>
          </div>
        </div>


        <div class="mt-20">
          <h2 class="text-3xl font-semibold text-gray-900 mb-8 text-center">Destaques</h2>
          <div class="grid grid-cols-1 md:grid-cols-2 gap-8">
            <div class="bg-white p-6 rounded-lg shadow-md">
              <h3 class="text-2xl font-semibold text-gray-900 mb-4">Sala de Reunião Premium</h3>
              <p class="text-gray-600 mb-4">Experimente nossa sala de reunião com recursos avançados, incluindo videoconferência em 4K e quadro interativo.</p>
              <button class="text-blue-600 hover:text-blue-800 font-medium">Saiba Mais →</button>
            </div>
            <div class="bg-white p-6 rounded-lg shadow-md">
              <h3 class="text-2xl font-semibold text-gray-900 mb-4">Planos Flexíveis</h3>
              <p class="text-gray-600 mb-4">Escolha o plano que melhor se adapta às suas necessidades, desde pequenas equipes até grandes corporações.</p>
              <button class="text-blue-600 hover:text-blue-800 font-medium">Conheça os Planos →</button>
            </div>
          </div>
        </div>
      </div>
    </main>


    <footer class="bg-gray-100 mt-20">
      <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-12">
        <div class="grid grid-cols-2 md:grid-cols-4 gap-8">
          <div>
            <h3 class="text-sm font-semibold text-gray-400 uppercase tracking-wider">Sobre</h3>
            <ul class="mt-4 space-y-2">
              <li><a href="#" class="text-base text-gray-500 hover:text-gray-900">Quem Somos</a></li>
              <li><a href="#" class="text-base text-gray-500 hover:text-gray-900">Contato</a></li>
              <li><a href="#" class="text-base text-gray-500 hover:text-gray-900">Imprensa</a></li>
            </ul>
          </div>
          <div>
            <h3 class="text-sm font-semibold text-gray-400 uppercase tracking-wider">Recursos</h3>
            <ul class="mt-4 space-y-2">
              <li><a href="#" class="text-base text-gray-500 hover:text-gray-900">Documentação</a></li>
              <li><a href="#" class="text-base text-gray-500 hover:text-gray-900">Tutoriais</a></li>
              <li><a href="#" class="text-base text-gray-500 hover:text-gray-900">Suporte</a></li>
            </ul>
          </div>
          <div>
            <h3 class="text-sm font-semibold text-gray-400 uppercase tracking-wider">Empresa</h3>
            <ul class="mt-4 space-y-2">
              <li><a href="#" class="text-base text-gray-500 hover:text-gray-900">Carreiras</a></li>
              <li><a href="#" class="text-base text-gray-500 hover:text-gray-900">Blog</a></li>
              <li><a href="#" class="text-base text-gray-500 hover:text-gray-900">Eventos</a></li>
            </ul>
          </div>
          <div>
            <h3 class="text-sm font-semibold text-gray-400 uppercase tracking-wider">Legal</h3>
            <ul class="mt-4 space-y-2">
              <li><a href="#" class="text-base text-gray-500 hover:text-gray-900">Termos de Uso</a></li>
              <li><a href="#" class="text-base text-gray-500 hover:text-gray-900">Política de Privacidade</a></li>
              <li><a href="#" class="text-base text-gray-500 hover:text-gray-900">Cookies</a></li>
            </ul>
          </div>
        </div>
        <div class="mt-12 border-t border-gray-200 pt-8 text-center">
          <p class="text-base text-gray-400">&copy; 2024 MeetSpace. Todos os direitos reservados.</p>
        </div>
      </div>
    </footer>
  </div>
</template>

<script setup>

</script>
