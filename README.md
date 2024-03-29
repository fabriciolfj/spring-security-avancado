# Spring security
Está relacionado a segurança nivel de aplicativo, ou seja, refere-se a tudo que o aplicativo deve fazer para proteger o ambiente que executa, bem como dados que processa e armazena.

##### Visão geral
- DispatcherServlet é uma interface entre a requisição do cliente e o rest controller. Caso desejamos inserir alguma lógica de autenticação ou autorização, precisamos efetuar esse processo antes da chamada do dispatcherservlet.
- Spring security provem filtros que possa colocar essa lógica antes ou depóis da chamada a uma dispatcherservlet.

###### Abordagem
- autenticação via filter: devemos implementar um filtro antes do dispatcherservlet
- oauth2-resource-server: não precisa implementar um filtro personalizado, o mesmo utiliza uma classe chamada BearerTokenAutenthicationFilter para o processo.
- obs: para filtros personalizados, devem ser registrados na classe que extende o websecurityconfigureradapter 

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
- UserDetailsService: responsável por recuperar os dados do usuario, no momento de geração do token por exemplo.
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
- verifica entre os authentication providers (seja personalizado ou a implementação padrão), se consegui validar esse authentication (suportar, tem um metodo que posso sobreescrever chamado suporte quando extendo o AuthenticationProvider, exemplo: class OtpAuthentication que extende UsernamePasswordAuthenticationToken, no suporte colocaria o codigo abaixo, assim o manager authentication iria procurar um provider com base neste suporte, que tenha essa classe, para executar).
```
@Component
class OtpAuthenticationProvider : AuthenticationProvider {

    @Autowired
    private lateinit var proxy: AuthenticationServerProxy

    override fun authenticate(authenticate: Authentication?): Authentication {
        var username = authenticate!!.name
        var code = authenticate.credentials as String
        var result = proxy.sendOtp(username, code)

        if (result) {
            return OtpAuthentication(username, code)
        }

        throw BadCredentialsException("Bad credentials")
    }

    override fun supports(p0: Class<*>?): Boolean {
        return OtpAuthentication::class.java.isAssignableFrom(p0);
    }
}

```


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

#### UsernamePasswordAuthenticationToken
- Podemos extender a classe UsernamePasswordAuthenticationToken e fazer uso nos nossos providers authentication.
- Existem 2 construtores nessa classe, o primeiro com apenas 2 strings (username e password), quando apenas populado, não diz que o usuário está autenticado. Já o construtor com 3 parâmetros (2 strings e uma collection com os grantedAuthority), quando populado, demonstra ao spring que o usuário está autenticado.


## OAUTH2
### Componentes
 - Resource server: os recursos de hospedagem de aplicativos de propriedades dos usuários, recursos também podem ser dados dos usuários ou suas ações autorizadas.
 - User (também conhecido como resource owner) : indivíduo que possui recursos expostos pelo servidor de recursos.
 - Client: o aplicativo que acessa os recursos do usuário em seu nome. Ele usa sua identificação e secret para se identificar.
 - Authorization server: o aplicativo que autoriza o cliente a acessar os recursos do usuário, expostos pelo resource server.
 
 ### Grants
 - authorization code:

### Authorization server
- utiliza quase toda lógica abordada acima, ou seja: authentication filter -> authentication manager -> authentication provider, userdetails, passwordencode e etc.
- a diferença que a autenticação não fica no SecurityContext e sim no token store.
- authorization server concede privilégios o a cliente, em nome do usuário. O cliente que usa os recursos.

#### Registrando client no authorization server
- contrato que define o cliente ClientDetails
- contrato que define a busca do detalhes ClientDetailsService.
- para armazenar em memória InMemoryClientDetailsService

##### Authorization Code
- Exemplo de requisição:

```
http://localhost:8080/oauth/authorize?response_type=code&client_id=client1&scope=read para pegar o code
```

- Exemplo de uri para verificar o token
```
http://localhost:8080/oauth/check_token?token=cf0cd6ef-50ef-4912-b017-ab91d62b709d
```

### Resource Server
- Servidor aonde encontra-se os recursos que a aplicação cliente quer utilizar.

#### Forma que o resource server valida o token
- Ligando para o servidor de autorizaçao, para validar o token.
- Utiliza-se um banco de dados comum, onde o servidor de autorização armazena os tokens e em seguida, o servidor de recursos pode acessar.
- Utiliza-se assinaturas criptográficas, onde o servidor de autorização assina o token e o servidor de recursos valida a assinatura. (uso de jwtS)

### Chaves
- simétricas: chave de assinatura/validação do token, são os mesmos no resource server e authorization server
- assimétricas: chave de assinatura/validação do token, são diferentes no resource server e authorization server
 - 2 chaves, uma privada (usada pelo authorization server para assinar o token) e publica (usada pelo resource server para ler o token).
- o authorization server pode expor a chave publica, atráves da url: 

```
curl -u resourceserver:resourceserversecret  http://localhost:8080/oauth/token_key
```

### Global method security
- Podemos definir a restrição de acessos a níveis de métodos, ou seja, não apenas a pontos finais.
- Desta forma podemos dar privilégios a quem pode chamar determinado método (pre authorization) ou pegar o retorno de determinado método (pos authorization).
- Podemos filtrar quem pode ver determinado método (prefiltering) ou ver o retorno do método (posfiltering).
- Por trás, o spring uso aspectos para aplicar a segurança de métodos, ou seja, o oap intercepta a chamada do método e decide se continua ou não.
- Obs para o posauthorization, caso esteja em um @transacional e ocorre falha na autorização, o processo não é revertido, pois o aspecto intercepta após o commit na da informação.

#### Configuração
- Por padrão a proteção por método vem desabilitada e para habilitar, segue o codigo de exemplo abaixo:

```
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ProjectConfig {
}

```

#### Uso segurança por método
```
    @PreAuthorize("hasAuthority('write')")
    public String getName() {
        return "Fantastico";
    }
```
- hasAuthority(): tenha uma determinada autorização.
- hasAnyAuthority(): tenha ao menos uma dessas autorizações.
- hasRole(): tenha uma determinada função
- hasAnyRole(): tenha ao menos uma dessas funções.

##### Preauthorize exemplo
```
    @PreAuthorize("hasAuthority('write')")
    public String getName() {
        return "Fantastico";
    }
```

##### Posauthorize exemplo
```
    @PostAuthorize("returnObject.roles.contains('reader')")
    public Employee getBookDetails(String name) {
        return records.get(name);
    }
```    

##### Conceito de permissão
- Quando temos uma lógica longa para aplicar, não é recomendavel utilizar SPEL(expressão do spring) para descreve-la e sim, inserir em uma classe apartada.
- Conseguimos referenciar outra classe, usando hasPermission(), onde procurará quem implementa o contrato PermissionEvaluator.

#### @Secure @RolesAllowed
- não aconselho usar essas 2 anotações como substituição do @postauthorize/@preauthorize

#### Filtro como segurança de método
- Caso não queira impedir acesso ao método, mas obrigar os parâmetros se seguirem regras ou retornar apenas o necessário, podemos utilizar a abordagem de pré/posfiltro.
- prefiltering: filtra os valores dos parâmetros antes de chamar o método, podemos aplicar somente se o método receber como parâmetro uma coleção de objetos.
- posfiltering: filtra o valor retornado após a chamada do método, podemos aplicar somente se o método retornar uma coleção.
 
## Keycloak
- Primeiro cadastro de um client e seu scope
- criar os mapeadores no client
  - authorities: User Realm Role
  - username:  User Property
  - audience no client: define o destinatário pretendido do token de acesso. Audience
- Adicione usuarios e suas funções.

### jwk
- O servidor de autorização usa uma chave privada para assinar o token. Quando ele assina o token. o servidor de autorização também adiciona um ID do par de teclas no cabeçalho do token. Para validar o token, o servidor de recursos chama um ponto final do servidor de autorização e recebe a chave pública para o ID encontrado no cabeçalho do token. O servidor de recursos usa essa chave pública para validar a assinatura do token.

### SecurityExpressionHandler
- Para utilizar expressões (SPEL) sobre o token jwt, para autorizações a nível de método.
```
    @Bean
    public SecurityExpressionHandler<FilterInvocation> handler() {
        return new OAuth2WebSecurityExpressionHandler();// para conseguirmos usar expressoes para pegar dados do token jwt
    }
```    

## Security reactive
- Umas das principais mudanças está realizado ao contrato userdetails e a ausência de providers.
- Fluxo:
  - AuthenticationWebFilter intercepta a requisição e delega para o ReactiveAuthenticationManager
  - Não existe providers, o ReactiveAuthenticationManager implementa a logica de autenticação, ou seja, autentica e retorna o objeto ao filtro e o filtro armazena a instancia do objeto no SecurityContext.
  - O springsecuritycontext é gerenciado de forma diferente, pelo ReactiveAuthenticationManager.
