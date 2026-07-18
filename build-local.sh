#!/bin/bash
set -e

echo "🚀 Iniciando proceso de compilación local idéntico a Producción..."

# 1. Compilar Backend (Necesitamos el JAR para su Dockerfile)
echo "☕ Compilando Backend..."
cd openbread-backend/openbread
chmod +x gradlew
./gradlew clean bootJar
cd ../..

# 2. Construir la imagen del Backend en Docker
echo "🐳 Construyendo imagen Docker del Backend..."
docker build -t ghcr.io/eduardoabenante/openbread/openbread-backend:latest -f ./openbread-backend/openbread/Dockerfile ./openbread-backend/openbread

# 3. Construir la imagen del Frontend en Docker (Él mismo hace el npm run build por dentro)
echo "📦 Construyendo imagen Docker del Frontend (Nginx + React)..."
docker build -t ghcr.io/eduardoabenante/openbread/openbread-frontend:latest -f ./openbread-frontend/Dockerfile ./openbread-frontend

# 4. Reiniciar servicios
echo "🔄 Reiniciando todos los servicios..."
docker compose down
docker compose up -d

echo "✅ ¡Entorno idéntico a producción corriendo en http://localhost !"