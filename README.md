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
- verifica entre os authentication providers (seja personalizado ou a implementação padrão), se consegui validar esse authentication (suportar).
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

##### Restrições com uso de matchers
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
   - antMatchers(String): método http.
 - Diferença entre o matchers: matchers ele protege o caminho especificado e qualquer coisa adicionada acima. Exemplo: protegi o caminho /hello, mas adicionei /heelo/test, ambos estão protegidos. No antMachers isso não ocorre, apenas /hello ficaria protegido.

##### Observação importante
- Caso você possua um endpoint exposto, autorizado para qualquer usuário, se fornecer usuário e senha válidos, será encaminhado para o recurso, caso não informe nenhum usuário, será encaminhado para o recuso, no entando se informar um usuário e senha inválido, receberá um codigo 401. Porquê? Lembre-se, o permiteAll se refere a autorização, que é executado após a autenticação, o filter intercepta a solicitação e a valida, caso ok, encaminha para o processo de autorização, caso negativo, retorna 401.
- Caso queria que todos os endpoints sejam acessíveis apenas por usuários autenticados, use o recurso anyRequest(0.authenticated().
