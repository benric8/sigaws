# Consulta Siga Ws Rest

Web service que permite consultar Expedientes Judiciales de las cortes superiores y de la corte suprema

### Requisitos Iniciales

| Herramienta     | Version                           |
|:----------------|:----------------------------------|
| Java            | 1.8.0_231                         |
| Springboot      | 2.5.15                            |
| Maven           | 3.9.8                             |
| Lombok          | 1.18.26                           |

- Configurar Java, Maven y Lombok en una ruta local del espacio de trabajo.

### Contexto del servicio

- /consulta-siga-ws

### arquitectura

```
consulta-siga-ws-rest/
│
├── consulta-siga-ws-domain/
│   ├── src/main/java
│   │   │
│   │   ├── pe/gob/pj/[nombrecorto]/domain/model/                   # Modelos y DTOs.
│   │   │   ├── negocio/
│   │   │   ├── [basedatos]/
│   │   │   ├── cliente/[servicioconsumir]/
│   │   │   │
│   │   │
│   │   ├── pe/gob/pj/[nombrecorto]/domain/port/                    # Interfaces de la funcionalidad a utilizar.
│   │   │   ├── client/[servicioconsumir]/                          # Servicios externos consumidos.
│   │   │   ├── persistence/                                        # Manejo de datos en BD.
│   │   │   ├── usecase/                                            # Casos de uso.
│   │   │   │
│   │   │
│   │   ├── pe/gob/pj/[nombrecorto]/domain/exceptions/              # Package donde va todas las excepciones personalizadas.
│   │   │   ├── TokenException.java
│   │   │   ├── CaptchaException.java
│   │   │   ├── ...
│   │   │   │
│   |   ├── pe/gob/pj/[nombrecorto]/domain/utils/                   # Package donde va todos los metodos utilitarias.
│   │   │   ├── CaptchaUtils.java
│   │   │   ├── EncryptUtils.java
│   │   │   ├── ...
│   │   │   │
│   └── pom.xml                                                     # Archivo domain de configuración maven
│
├── pconsulta-siga-ws-usecase/
│   ├── src/main/java
│   │   ├── pe/gob/pj/[nombrecorto]/usecase/                        # Package donde van las clases que implementan ports usecase
│   │   │   ├── SeguridadUseCaseAdapter.java                        # Clase que implementa el caso de uso para seguridad
│   │   │   ├── ...
│   │   │   │
│   │
│   └── pom.xml                                                     # Archivo usecase de configuración maven               
│
├── consulta-siga-ws-infrastructure/
│   ├── src/main/java  
│   │   ├── pe/gob/pj/[nombrecorto]/infrastructure/client/          # Package donde va todo referente al consumo de servicios
│   │   │   ├── RestTemplateConfig.java                             # Clase de configuración para resttemplate.
│   │   │   ├── ...
│   │   │   │
│   │   │
│   │   ├── pe/gob/pj/[nombrecorto]/infrastructure/db/              # Package donde va todo referente al consumo de base de datos
│   │   │   ├── [basedatos]Config.java                              # Clase de configuración a bd a manejar Ej: SeguridadConfig.java
│   │   │   ├── [basedatos]/entities/                               # Package donde va las clases entidades
│   │   │   ├── [basedatos]/repositories/                           # Package donde va los objetos reposiotry y dsl de las entidades
│   │   │   ├── persistence/                                        # Package donde va las clases que implementan ports persistence
│   │   │   ├── ...
│   │   │   
│   │   ├── pe/gob/pj/[nombrecorto]/infrastructure/doc/             # Package donde va todo referente a documentación de endpoints
│   │   │   ├── SwaggerConfig.java                                  # Clase de configuración de swagger
│   │   │   ├── ...
│   │   │   │
|   |   |
│   │   ├── pe/gob/pj/[nombrecorto]/infrastructure/rest/            # Package donde va todo referente a los endpoints.
│   │   │   ├── exceptions/                                         # Package donde  va la excepxion generica para los endpoints
│   │   │   ├── requests/                                           # Package donde va las clases request de las peticiones.
│   │   │   ├── responses/                                          # Package donde va las clases response de las peticiones.
│   │   │   ├── ...
│   │   │   │
│   │   │
│   │   ├── pe/gob/pj/[nombrecorto]/infrastructure/security/        # Package donde va todo referente a la seguridad.
│   │   │   ├── SecurityConfig.java                                 # Clase de configuración de spring security
│   │   │   ├── adapters/
│   │   │   │   └── UserDetailsServiceAdapter.java
│   │   │   └── filters/                                            # Package donde van los filtros de seguridad
│   │   │       ├── JwtAuthenticationFilter.java                    # Clase encargada de validar la autenticación de los parámetros
│   │   │       └── JwtAuthorizationFilter.java                     # Clase encargada de validar la autorización a los endpoints
│   │   │   
│   │   ├── ...
│   │
│   └── pom.xml                                                     # Archivo infraestructure de configuración maven
│
└── pom.xml                                                         # Archivo general de configuración de Maven.

```

### Base de Datos

| Entorno     | Tipo de BD        | Servidor        |Puerto|BD                      |Usuario del Servicio             |
|:------------|:------------------|:----------------|:-----|:-----------------------|:--------------------------------|
| Desarrollo  | Oracle            |172.19.211.91    |7520  |BDPRUEBAS               |UC_CONSIGA_SIGA                  |
| Desarrollo  | PostgreSQL        |172.18.11.241    |39969 |SEGURIDAD               |uc_consiga_seguridad             |


### Recursos 

+ Los recursos corresponden a los archivo propeties y logs, y se debe ubicar en la carpeta modules/pe/gob/pj/consulta-siga del servidor donde se despliega el proyecto.
```
https://desagit.pj.gob.pe/sdsi/recurso-servicios-in-modules/consulta-siga
```
+ Para obtener los recursos con git bash ejecutar el siguiente comando: 
```
git clone https://desagit.pj.gob.pe/sdsi/consulta-siga-csiga/consulta-siga-ws-rest.git
```

### URLs del servicio                         

| Nombre        | Link Url                                                       |
|:--------------|:---------------------------------------------------------------|
|Url Base       |http://172.18.13.173:8193/consulta-siga-ws                      |
|Disponibilidad |http://172.18.13.173:8193/consulta-siga-ws/healthcheck          |

