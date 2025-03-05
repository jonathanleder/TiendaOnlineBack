
# TiendaOnline - API Backend

![TiendaOnline Logo](https://img.shields.io/badge/Project-TiendaOnline-blue.svg)
![Java Version](https://img.shields.io/badge/Java-11%2B-blue.svg)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-2.5.4-green.svg)
![MySQL](https://img.shields.io/badge/MySQL-8.0-blue.svg)
![JWT](https://img.shields.io/badge/JWT-Authentication-yellow.svg)
![License](https://img.shields.io/badge/License-MIT-yellowgreen.svg)

## Descripción

**TiendaOnline** es una API backend creada con **Spring Boot** que gestiona las operaciones de una tienda online. Permite gestionar productos, pedidos, usuarios y más, proporcionando una interfaz RESTful para interactuar con el sistema. La API utiliza **JWT** para la autenticación de usuarios y está conectada a una base de datos **MySQL**.

## Tecnologías utilizadas

- **Spring Boot** ![Spring Boot](https://img.shields.io/badge/Spring_Boot-2.5.4-green.svg)
- **JPA (Java Persistence API)** 
- **Hibernate**
- **Lombok** 
- **JWT (JSON Web Token)** ![JWT](https://img.shields.io/badge/JWT-Authentication-yellow.svg)
- **MySQL** ![MySQL](https://img.shields.io/badge/MySQL-8.0-blue.svg)

## Funcionalidades principales

- **Gestión de productos**: CRUD para manejar productos de la tienda (crear, leer, actualizar y eliminar).
- **Gestión de pedidos**: Crear y gestionar los pedidos realizados por los usuarios.
- **Gestión de usuarios**: Registrar, autenticar y autorizar usuarios mediante JWT.
- **Autenticación**: Los usuarios pueden acceder a la API con un token JWT.
  
## Endpoints principales

- **POST** `/api/auth/login`: Iniciar sesión y obtener un token JWT.
- **GET** `/api/products`: Obtener todos los productos.
- **POST** `/api/products`: Crear un nuevo producto.
- **GET** `/api/products/{id}`: Obtener un producto específico por ID.
- **PUT** `/api/products/{id}`: Actualizar un producto por ID.
- **DELETE** `/api/products/{id}`: Eliminar un producto por ID.
- **GET** `/api/orders`: Obtener todos los pedidos.
- **POST** `/api/orders`: Crear un nuevo pedido.

## Autenticación y autorización

La autenticación en la API se realiza utilizando **JSON Web Tokens (JWT)**. Al iniciar sesión con las credenciales correctas, el servidor devuelve un token que debe ser enviado en los encabezados de las solicitudes posteriores para acceder a los recursos protegidos.

Ejemplo de uso:

- Realiza una solicitud `POST /api/auth/login` con las credenciales (usuario y contraseña).
- Guarda el token JWT devuelto.
- En solicitudes posteriores, incluye el token en el encabezado `Authorization` como:  
  `Authorization: Bearer <tu_token_jwt>`.

## Base de datos

La aplicación está conectada a una base de datos **MySQL**. Asegúrate de configurar correctamente las credenciales y el nombre de la base de datos en el archivo `application.properties` o `application.yml`.

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/tiendaonline
spring.datasource.username=root
spring.datasource.password=tu_contraseña
spring.jpa.hibernate.ddl-auto=update
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect
```

## Instrucciones de instalación y dependencias

### 1. Clonar el repositorio

Clona el repositorio en tu máquina local:

```bash
git clone https://github.com/tu_usuario/tiendaonline.git
```

### 2. Requisitos previos

- **Java 11 o superior**: Asegúrate de tener Java instalado en tu máquina. Puedes verificarlo ejecutando:

  ```bash
  java -version
  ```

- **MySQL**: Necesitas tener MySQL instalado y en funcionamiento. Crea una base de datos llamada `tiendaonline`.

- **Maven**: La aplicación usa Maven para la gestión de dependencias. Si no tienes Maven, puedes descargarlo desde [aquí](https://maven.apache.org/).

### 3. Configuración de la base de datos

Asegúrate de que la base de datos MySQL esté configurada correctamente. En el archivo `src/main/resources/application.properties`, configura los datos de la conexión a la base de datos:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/tiendaonline
spring.datasource.username=root
spring.datasource.password=tu_contraseña
spring.jpa.hibernate.ddl-auto=update
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect
```

### 4. Instalar dependencias

En la raíz del proyecto, ejecuta el siguiente comando para descargar todas las dependencias de Maven:

```bash
mvn clean install
```

### 5. Ejecutar la aplicación

Una vez que hayas configurado todo, puedes ejecutar la aplicación con el siguiente comando:

```bash
mvn spring-boot:run
```

La API debería estar corriendo en `http://localhost:8080`.

## Pruebas y cobertura con JUnit

Este proyecto incluye pruebas unitarias con **JUnit**. Las pruebas están ubicadas en el directorio `src/test/java`.

Para ejecutar las pruebas, puedes usar el siguiente comando de Maven:

```bash
mvn test
```

Este comando ejecutará todas las pruebas definidas en el proyecto. Para ver los resultados y la cobertura de las pruebas, puedes integrar herramientas como **JaCoCo** para generar un informe de cobertura. Si no lo tienes configurado, puedes agregar JaCoCo al archivo `pom.xml` en la sección de plugins:

```xml
<build>
    <plugins>
        <plugin>
            <groupId>org.jacoco</groupId>
            <artifactId>jacoco-maven-plugin</artifactId>
            <version>0.8.7</version>
            <executions>
                <execution>
                    <goals>
                        <goal>prepare-agent</goal>
                        <goal>report</goal>
                    </goals>
                </execution>
            </executions>
        </plugin>
    </plugins>
</build>
```

Con esta configuración, al ejecutar `mvn test`, también se generará un informe de cobertura en el directorio `target/site/jacoco/index.html`.

## Contribución

Si deseas contribuir al proyecto, siéntete libre de hacer un fork y enviar un pull request. Asegúrate de seguir las buenas prácticas y realizar las pruebas adecuadas antes de enviar cualquier contribución.

## Licencia

Este proyecto está licenciado bajo la **MIT License**. ![MIT License](https://img.shields.io/badge/License-MIT-yellowgreen.svg)

---
