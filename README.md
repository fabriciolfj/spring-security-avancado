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
