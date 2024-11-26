<h1 align="center">
  Projeto Mytt
</h1>


<p align="center">
  <a href="#page_with_curl-sobre">Sobre</a>&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;
  <a href="#hammer-tecnologias">Tecnologias</a>
  &nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;
  <a href="#rocket-iniciando">Iniciando</a>
</p>


## :page_with_curl: Sobre

Este repositório é um projeto de estudo feito como uma cópia da rede social X(Twitter). Feito em Java com Spring, afim de entender um pouco melhor como funciona camadas de segurança e autenticação.

Uma das minha intenções era entender melhor como funciona o protocolo OAuth 2.0 através do keycloak, bem como entender melhor como funciona a lógica para privação de rotas no spring, caputrando as roles no keycloak.

Para consumir essa API construi uma aplicação simples em Angular 18, sendo essa a minha primeria experiência com o framework.


## :hammer: Tecnologias

- Java 23
- Maven
- Lombok
- Spring (Boot, Web, Security, Validation, JPA)
- Angular 18
- Typescript
- Keycloak
- PostgreSQL
- Docker

## :rocket: Iniciando
``` bash
  # Clonar o projeto:
  $ git clone git@github.com:davifariasp/mytt-fullstack.git

  # Entrar no diretório:
  $ cd mytt-fullstack

  # Iniciar back
  $ cd back
  $ sh up.sh
  $ mvn spring-boot:run

  #Iniciar front
  $ npm start
```