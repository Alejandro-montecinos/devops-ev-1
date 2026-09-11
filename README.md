
# Proyecto CI/CD - Microservicio Pedidos

Este proyecto corresponde a la implementación de un flujo **CI/CD** para el despliegue de un microservicio desarrollado con **Spring Boot**.

## Tecnologías

* Java 21
* Spring Boot
* Maven
* Git / GitHub
* GitHub Actions
* AWS EC2
* Swagger / OpenAPI

## Organización de ramas

Para organizar el desarrollo utilizamos las siguientes ramas:

```text
main
└── develops
    └── feature/endpointAgregar
```

* **main:** contiene la versión estable del proyecto.
* **develops:** rama utilizada para integrar los cambios durante el desarrollo.
* **feature/endpointAgregar:** rama utilizada para desarrollar nuevas funcionalidades o endpoints.

Una vez terminada una funcionalidad, se integra desde `feature` hacia `develops` y posteriormente hacia `main`.

## CI/CD

El proyecto utiliza **GitHub Actions** para automatizar el proceso de compilación y despliegue.

El flujo consiste en:

```text
GitHub
   ↓
GitHub Actions
   ↓
Maven Build
   ↓
Archivo .jar
   ↓
AWS EC2
   ↓
Spring Boot
```

El microservicio se encuentra desplegado en una instancia EC2 utilizando el puerto **2020**.

## Swagger

La API cuenta con documentación mediante Swagger:

```text
http://184.192.139.90:2020/swagger-ui/index.html
```

## API

La ruta principal para los productos es:

```text
/api/v1/productos
```

URL:

```text
http://184.192.139.90:2020/api/v1/productos
```

## Objetivo

El objetivo del proyecto es aplicar conceptos de **CI/CD**, control de versiones, desarrollo mediante ramas y despliegue automatizado de un microservicio Spring Boot utilizando **GitHub Actions y AWS EC2**.
