# OpenBread

## Despliegue (Docker)

Esta arquitectura está completamente desacoplada y automatizada. Tanto el **Frontend (Nginx + React/Vite)** como el **Backend (Kotlin/Spring Boot)** se compilan, testean y empaquetan en la nube mediante un pipeline de **GitHub Actions (CI/CD)**, quedando publicados en GitHub Packages (GHCR).

### Requisitos Previos
* [Docker Desktop](https://www.docker.com/products/docker-desktop/).

### 📦 Instrucciones de Instalación

1. **Descarga los archivos de configuración**
   Crea una carpeta vacía en tu máquina e incluye en ella los dos archivos principales de la raíz de este repositorio:
   * `docker-compose.yml` 
   * `nginx.conf` 

2. **Levanta el entorno con Docker Compose**
   Abre una terminal en esa carpeta y ejecuta el siguiente comando:
   ```bash
   docker compose up -d
   
Este comando descargará automáticamente las últimas imágenes públicas de GitHub Packages, configurará la red interna aislada y levantará la base de datos PostgreSQL junto a la aplicación.

Abre tu navegador web e ingresa a: http://localhost (Puerto 80)

### Características de Producción Incluidas

* **Seguridad de Red:** El backend (`8080`) y la base de datos (`5432`) corren dentro de una red interna aislada de Docker. Solo el puerto `80` de Nginx está expuesto al exterior, actuando como **Proxy Inverso** y blindando la API de accesos directos no deseados desde internet.
* **Persistencia de Datos:** Incluye volúmenes nombrados de Docker (`pgdata` y `openbread-uploads`) para asegurar que ni la base de datos ni las imágenes de perfil o archivos subidos por los usuarios se pierdan al reiniciar, detener o actualizar los contenedores.
* **Gestión de Permisos Nativa:** El almacenamiento de archivos se gestiona mediante usuarios sin privilegios del sistema (`springuser`) configurados directamente desde el `Dockerfile`.
