# Spring security
Está relacionado a segurança nivel de aplicativo, ou seja, refere-se a tudo que o aplicativo deve fazer para proteger o ambiente que executa, bem como dados que processa e armazena.

##### Diferença entre autenticação e autorização.
- autenticação: identificação do usuário, quando ele foi identificado pelo sistema, ele foi "autenticado".
- autorização: após autenticado, o que o usuário pode fazer.
 
Conforme aumenta-se a complexidade do sistema, faz necessário implantar um serviço epecífico, relacionado a autenticação e autorização.

##### CSRF
- Falsificação de solicitação entre sites.

##### Vulnerabilidades
Abaixo uma listagem das vulnerabilidades mais comuns, aonde são exploradas no exemplo:

- Autenticação quebrada
  - quando um indivíduo com más intenções, de alguma forma ganha acesso a funcionalidade ou dados que não pertencem a ele.
- Fixação de sessão
  - permite que um invasor se passe por um usuário válido, reutilizando um id de sessão gerado anteriormente.
  - durante o processo de autenticação, o aplicativo web não atribuir um id de sessão exclusivo, isso pode potencialmente levar a reutilização dos ids de sessão anteriores.
- Scripting cross-site (XSS)
  - quando sites permitem a postagem de scripts.
- Falsificação de solicitações(CSRF)
- Injeção sensíveis d exposição de dados.
- Falta de controlede acesso ao método
- Usando dependência com vulnerabilidades conhecidas.

##### Arquitetura
Dependente da arquitetura implementada, a abordagem relacionada a segurança muda.
 - Arquitetura mvc sem separação entre backend e frontend: lidar com vulnerabilidades relacionadas a cors e csrf.
 - Arquitetura mvc com separação entre backend e frontend: problemas relacionados a cors (origem) são mais desafiadoras.

### Oauth2
Define 2 entidades separadas, o servdor de autorização e o servidor de recursos.
- Servidor de autorização: fornecer um token que especifique, entre outras coisas, um conjunto de privilégios que eles possam usar.
- Servidor de recursos: os privilégios que o usuario pode usar, diante autorização.

### Comunicação entre backends
- Usar chave estáticas no cabeçalho da requisição e response.
- Solicitações criptográfadas.
- Validando endereço de ip.

## Componentes pré-configurados (em ordem)
- Authentication filter -> delega a solicitação de autenticação ao gerenciador (authentication manager)
- Authentication manager -> usa o provedor de autenticação para processar a autenticação.
- Authentication provider -> implementa a lógica de autenticação; (recebe a solicitação vinda do authentication manager (contrato authentication) e delega em um userdetailsservice, verificando a senha de um passwordencoder)
- Password encoder -> implementa o gerenciamento de senhas, que o provedor de autenticação usa na lógica. (codifica a senha e verifica se ela corresponde a uma codificação existente).
- Security context -> mantem os dados de autenticação após o processo.
- Direciona para o filter de autorização.

##### Configuração
- Existem várias maneiras de realizar configurações na aplicação, mas devemos escolher e manter uma abordagem, para ajudar a tornar o código mais limpo e fácil de entender.

##### Mais componentes
- UserDetailsService: responsável por recuperar os dados do usuario.
- UserDetailsManager: extende userdetailsservice, adicionando mais funcionalidades, bem como: adicionar, modificar ou excluir o usuário.
- UserDetails: Interface que identifica a classe que o spring reconhecerá, para validar o usuário (autenticar).
- GrantedAuthority: Aonde ficam armazenradas as autorizações.
- JdbcUserDetailsManager: função igual ao userdetailsmanager, mas se conecta (independentemente de outra lib) a uma base de dados.
- DelegationPasswordEncoder: implementa a interface passwordencoder, mas delega a outra instância a implementação do contrato.

##### SSCM
Spring security crypto, que é parte do spring que cuida da criptografia, ou seja, gera criptografadores que são objetos utilitários que ajudam você a aplicar criptografia e descriptografia de dados.

##### Representação da solicitação
O contrato que representa a solicitação de autenticação/autorização, chama-se Authentication, que extende a classe Principal do api security do java. Contem os seguintes atributos:
- isAuthenticated() -> retorna true se o processo de autenticação terminou ou false se ainda está em andamento.
- getCredentials() -> retorna o password ou chave secreta usada no processo de autenticação.
- getAuthorities() -> retorna uma coleção de autorizações concedidas para a solicitação.

##### Detalhes do funcionamento do authentication provider
- authentication manager receber o contrato authentication
- verifica entre os authentication providers (seja personalizado ou a implementação padrão), se consegui validar esse authentication (suportar, tem um metodo que posso sobreescrever chamado suporte quando extendo o AuthenticationProvider).
- caso authentication suporte, ele executa a validação, caso não, retorna null.
- caso positivo, segue com a solicitação, caso negativo solta a exceção.

##### Spring context
Uma vez que o authentication manager conclui o processo de autenticação com sucesso, ele armazena a instância de autenticação para o restada solicitação.
- O spring security oferece três estratégias para gerenciar o spring context:
  - MODE_THREADLOCAL: cada thread armazena seus próprios detalhes no contexto (por solicitação).
  - MODE_INHERITABLETHREADLOCAL: similar ao mode threadlocal mas permite copiar o contexto para a próxima thread, em caso de metodo assíncrono.
  - MODE_GLOBAL:  faz com que todas as threads do aplicativo, vejam a mesma instância de contexto.
  - DelegatingSecurityContextRunnable/DelegatingSecurityContextCallable: quando possui novas threads, não gerenciadas pelo contexto (spring), mas gostaria de encaminhar o contexto de segurança para elas.

Uma outra alternativa é gerenciar o pool de threads, através do DelegatingSecurityContextExecutorService, afim de propagar o contexto de segurança.  


##### Basic authentication
É a autenticação padrão do spring security e mais simples, não indicado para ambiente produtivo.  Podemos personalizar o basic authentication, utilizando os pontos abaixo:
- formLogin: habilitar um formulário web padrão, com usuario e senha. 
- AuthenticationEntryPoint: personalizar a resposta de erro (caso login falhe).
- AuthenticationSuccessHandler: personalizar a lógica de autenticação (usado no fluxo do formlogin).
- AuthenticationFailureHandler: personalizar a lógica em caso de erro de autenticação (usado no fluxo do formlogin).

##### GrantedAuthority
- Representação das autorizações do usuário.
- Uma autorização é uma ação que o usuário pode executar com um recurso do sistema.
- Para aplicar o uso da autorização, existe os seguintes recursos: hasAuthority(), access() que recebe uma SPEL e hasAnyAuthority()
- Para aplicar roles, existe os seguintes recursos: hasRole(), hasAnyRole() e access().

##### Roles x Authorities
- Uma role pode ter várias authorities. Exemplo: ADMIN (role) pode (authorities) ler, escrever, apagar e etc.
- Ao atribuir uma role, inicie com o prefixo ROLE_, caso use o metodo authorities se for role, não necessita.

##### Restrições com uso de mvcMatchers
- Restringir o acesso a algum path ou verbo http, dependendo da autorização do usuário.
- Existem algumas configurações, tais como:
  - anyRequest() autoriza qualquer solicitação, para um critério especifico
  
  ```
          http.authorizeRequests()
                .anyRequest()
                .hasRole("ADMIN");
  
  ```
  - mvnMathers("/path") restringe o acesso a um determinado endpoint, com base no regex informado. obs: caso use apenas os mvnMathers, o caminho não especificado nele, ficará acessivel a todos os usuários, mesmo os não autenticados.
  ```
          http.authorizeRequests()
                .mvcMatchers("/hello")
                .hasRole("ADMIN")
                .mvcMatchers("/ciao")
                .hasRole("MANAGER");
  ```
 - Atenção ao uso de mathers, a ordem das regras deve ser particular para geral. Por isso o método anyRequest() não pode ser chamado antes do método matcher específico, como mvcMatchers().
 
 ##### Restrições com antMatchers
 - Existem 3 formas de usar o antMachers:
   - antMatchers(HttpMethod, String patterns): metodo http e caminho do endpoint
   - antMatchers(String): caminho do endpoint
   - antMatchers(HttpMethod): método http.
 - Diferença entre o mvcMatchers: mvcMatchers ele protege o caminho especificado e qualquer coisa adicionada acima. Exemplo: protegi o caminho /hello, mas adicionei /hello/test, ambos estão protegidos. No antMachers isso não ocorre, apenas /hello ficaria protegido.
 
 #### Restricos com regexMathers
  - Existem 2 formas de usar o regexMathers:
   - regexMathers(HttpMethod, String patterns): metodo http e regex
   - regexMathers(String): regex

##### Observação importante
- Caso você possua um endpoint exposto, autorizado para qualquer usuário, se fornecer usuário e senha válidos, será encaminhado para o recurso, caso não informe nenhum usuário, será encaminhado para o recuso, no entando se informar um usuário e senha inválido, receberá um codigo 401. Porquê? Lembre-se, o permiteAll se refere a autorização, que é executado após a autenticação, o filter intercepta a solicitação e a valida, caso ok, encaminha para o processo de autorização, caso negativo, retorna 401.
- Caso queria que todos os endpoints sejam acessíveis apenas por usuários autenticados, use o recurso anyRequest(0.authenticated().

## Filters
- Filtros http gerenciam cada responsabilidade que deve ser aplicada á solicitação.
- Os filtros formam uma cadeia de responsabilidades, ou seja, recebe uma solicitação, executa sua lógica e, eventualmente, delega a solicitação para o próximo filtro da cadeia.
- Para se criar filtros, implementa-se a interface Filter, a partir do pacote javax.servlet.
- Como parâmetros do método do contrato: ServletRequest: pegar detalhes da requisição; ServletResponse: altera a resposta antes de enviar ao cliente; FilterChain: representa a cadeia defiltros, utiliza-se para passar ao próximo filtro da cadeia a requisição.
- Podemos personalizar a ordem em que os filtros são executados, atráves da configuração http.addFilterBefore. Por exemplo: quero que meu filtro personalizado seja executado antes do filtro de autenticação: http.addFilterBefore(new Meufiltro(), BasicAuthenticationFilter.class).
- Existem alguns filtros padrões como: BasicAuthenticationFilter (autenticação, ja utilizada por default), CsrfFilter(requisição falsa), CorsFilter (origem cruzada).
- Podemos adicionar um filtro para localizar outro filtro, isso ocorre quando queremos reaproveitar alguma lógica já implementada.
- Podemos também adicionar um filtro na mesma ordem ou posição de outro, nesse caso o spring não garante a ordem de execução.
- Podemos tambem adicionar um filtro na posição do outro, sem declarar este: 
```
        http.addFilterAt(filter, BasicAuthenticationFilter.class) //colocar o filter na mesma posição, mas nao estou add o filtro BasicAuthentication, para isso preciso declarar httpBasic()
                .authorizeRequests()
                .anyRequest().permitAll();

```
#### Classes abstratas
- Existem alguma classes abstradas, que implementam filter, como: OncePerRequestFilter (garante que o filtro seja chamado apenas uma vez por solicitação (nao funciona para solicitação assincrona, precisa mudar o comportamento via shouldNotFilterAsyncDispatch), por padrão, o spring não garante tal comportamento, caso queira, extenda essa classe).

##### OncePerRequestFilter
- Existe 2 métodos que podemos sobreescrever ao extender essa classe:
  - shouldNotFilter: Colocamos uma condição para esse filtro ser executado, por exemplo: quero que seja executado para o endpoint /login
  - doFilterInternal: método que é chamado ao cair nesse filtro, a requisição.

## Filters relacionados a CORS e CSRF
- Relembrando a definição:
  - CSRF -> ataque de solicitação falsa, ou seja, o usuario se autenticou no aplicativo e o invasor engana o mesmo, fazendo efetuar ações indevidas. (por padrão, spring ativa a proteção csrf para endpoints post). 
  - Proteção CSRF -> Spring security utiliza seu mecanismo de token, para proteger os endpotins de ataques csrf.
  - Csrf ativado, apenas protege endpotins mutantes, como: post, put e delete.
- Proteção CSRF é indicado quando paginas web fazem requisições ao seu endpoint.

#### CsrfFilter
- Intercepta a solicitação e verifica se existe o token csrf no cabeçalho, para ações que não sejam GET, OPTIONS, TRACE E HEAD.
- Caso o token não exista, devolve um código http 403 (proibido).
- O token é gerado na primeira solicitação GET (armazena usando a sessão como chave, exemplo:  JSESSIONID=18F11F1AD09A4CC3557B7A1D9FB167D7, 00951ba4-ae47-4b8b-8285-b1f4fcc1ddf4
- Os tokens ficam armazenados na sessão HTTP, através do componente CsrfTokenRepository.
- Token em sua maioria das vezes, são UUIDS, mas podemos personalizar.
- A implementação do csrf (onde o csrffilter insere automaticamente no atributo _csrf da solicitação), onde o backend e frontend são juntos, é facil de desenvovler, mas em uma abordagem onde o front é separado, é um desafio.

#### Detalhes da proteção csrf do spring
- Por padrão, o token csrf gerado, fica armazenado na sessão http no servidor, dificultando a escalabilidade da aplicação.
- Podemos personalizar o gerenciamento/guarda do token no servidor, fazendo uso das interfaces csrftoken e csrftokenrepository.

#### Cors
- Por padrão navegadores não permitem solicitações feitas para qualquer domínio que não seja aquele de onde o site é carregado.
- Para permitir chamadas entre dominios diferentes, existem alguns cabeçalhos que podem nos fornecer mecanismos para isso:
  - Access-Control-Allow-Origini--Specifies: Insiro os dominios estrangeiros que poderam acessar meus recursos. (somente sse ponto, meus endpotins podem receber chamadas de dominios diferentes da minha aplicacao).
  - Access-Control-Allow-Methods--Lets: digo quais métodos http os dominios estrangeiros, podem requerer.
  - Access-Control-Allow-Headers--Adds: limito para quais cabeçalhos você pode usar em uma solicitação específica.
- Configurar o corsorigin dentro da aplicação, evita ataques como DDOS.

#### Tokens
- O que são tokens? São representados por uma string e devem ser reconhecidos pela sua aplicação. Funciona como um cartão de acesso, onde o cliente de posse do mesmo, pode acessar alguns endpoints e outros não.
- Qual a vantagem de utilizar token?
  - Token ajudam a você evitar o compartilhamento de credencias a cada solicitiação.
  - Podemos invalidar tokens sem invalidar credenciais.
  - Token também pode incluir autorizações.
  - Token ajuda a delegar a responsabilidade de autenticação para outro componente no sistema.
  - JWT (json web token): é uma implementação do token, que possui 3 partes (cabeçalho, corpo e assinatura digital)
