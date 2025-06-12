# 🐱 StarterPet 🐶

Seja bem-vindo(a) a nossa API de gerenciamento de pets composta por 3 microservices!
Este projeto foi desenvolvido com arquitetura de microservices para realizar o cadastro de pets,
gerenciamento de agendamentos de cuidados e envio de notificações por e-mail para os tutores.

Essa API foi desenvolvida com **Java 21**, **Spring Boot** e o banco de dados **MySQL**.
Ela consome a API externa TheDogAPI e TheCatAPI.

Se você deseja saber mais informações sobre elas, clique aqui! [TheDogAPI](https://thedogapi.com/) [TheCatAPI](https://thecatapi.com/)

## Tecnologias utilizadas

- [Spring Boot](https://spring.io/)
- [Spring Web](https://docs.spring.io/spring-boot/reference/web/index.html)
- [Spring Data JPA](https://spring.io/projects/spring-data-jpa)
- [MySQL Driver](https://dev.mysql.com/doc/connector-j/en/)
- [Spring Boot DevTools](https://docs.spring.io/spring-boot/reference/using/devtools.html)
- [Validation](https://docs.spring.io/spring-boot/reference/io/validation.html)
- [OpenAPI/Swagger](https://swagger.io/)
- [JUnit](https://junit.org/junit5/)
- [Mockito](https://site.mockito.org/)
- [RabbitMQ](https://www.rabbitmq.com/)
- [Lombok](https://projectlombok.org/)

## Como utilizar o projeto?

- *Realize um **git clone** no projeto*

- *Abra o projeto na IDE desejada*

- *Configure o application.properties de cada microservice com as informações do seu ambiente (banco de dados, e-mail, RabbitMQ)*

- *Será necessário colocar um e-mail (De preferência gmail) e uma senha de APP(contendo 16 dígitos) [Clique aqui](https://support.google.com/accounts/answer/13548313?hl=en#zippy=%2Ccreate-a-passkey) para informações de geração de passkey!*

- *Certifique-se de ter o MySQL instalado e configurado corretamente*

- *Caso seja necessário, configure o banco de dados através do **application.properties***

- *Inicie a aplicação*

- *Caso não possua o Postman instalado na sua máquina, baixe clicando neste [link](https://www.postman.com/downloads/)*

- *Importe o arquivo JSON para o **Postman** nomeado como "StarterPet_EndpointsPostman.json"*

- *Prontinho, sua aplicação está pronta para uso!*

## Endpoints da API

| Método   |                          Endpoint                           |                                                                  Descrição |
|:---------|:-----------------------------------------------------------:|---------------------------------------------------------------------------:|
| `POST`   |                         _api/pets_                          |                                         Adiciona um pet ao banco de dados. |
| `GET`    |                         _api/pets_                          |                                           Retorna a lista de pets criados. |
| `GET`    |                       _api/pets/dogs_                       |                                           Retorna a lista de cães criados. |
| `GET`    |                       _api/pets/cats_                       |                                          Retorna a lista de gatos criados. |
| `GET`    |            _api/pets/breed?breed={nome_da_raça}             |                          Retorna a lista de pets criados com base na raça. |
| `PUT`    |                   _api/pets/{id-do-pet}_                    |                                           Atualiza as informações do pets. |
| `DELETE` |                   _api/pets/{id-do-pet}_                    |                          Infelizmente, deleta um pet do banco de dados. 😭 |
| `GET`    |                   _api/pets/{id-do-pet}_                    |                           Retorna um pet criado do banco de dados pelo ID. |
| `GET`    |           _api/pets/breeds?species={dog ou cat}_            |                   Retorna a listagem completa de raças da **API Externa.** |
| `GET`    | _api/pets/images?species={dog ou cat}&breed={nome-da-raça}_ |        Retorna imagens de pets baseada no nome da raça na **API Externa.** |
| `POST`   |               _api/pets/appointments/manual_                |                                             Realiza um agendamento manual. | 
| `GET`    |                _api/pets/appointments/list_                 | Retorna a lista de agendamentos (automáticos e manuais) do banco de dados. |                                             |

## JSON para criação, atualização e deleção de pets

- {
- "name": "Exemplo-Nome",
- "breed": "Exemplo-Raça",
- "species": "Cão-ou-Gato",
- "tutorName": "Exemplo-Nome-Tutor",
- "tutorEmail": "Exemplo-Tutor-Email",
- "age": 0,
- "weight": 0.0,
- "color": "Exemplo-Cor",
- "description": Fornecida pela API automaticamente, se disponível,
- "urlImage": Fornecida pela API automaticamente, se disponível,
- "temperament": Fornecida pela API automaticamente, se disponível
- }

## Atenção! 
Acesso da documentação via Swagger:
- Pet Register Service: http://localhost:8080/swagger-ui.html
- Pet Agenda Service: http://localhost:8081/swagger-ui.html 
- A aplicação trabalha no localhost, nas portas **8080 (Pet Register)**, **8081 (Pet Agenda)** e **8082 (Notification Service)**
- O schema do MySQL deve ser criado com o nome **pet_register**
- Algumas raças das APIs externas podem conter informações que ultrapassem o limite de caracteres do banco de dados

## Autora

- [Luisa Coelho](https://git.gft.com/lico)



