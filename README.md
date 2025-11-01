DevOps Exercise – Java + Docker + Podman + GitHub Actions
📖 Descripción

Este proyecto implementa un microservicio REST en Java Spring Boot que cumple los siguientes requisitos:

Endpoint único: /DevOps

Solo acepta método POST con el siguiente JSON:

{
  "message": "This is a test",
  "to": "Juan Perez",
  "from": "Rita Asturia",
  "timeToLifeSec": 45
}


Responde con:

{
  "message": "Hello Juan Perez your message will be sent"
}


Cualquier otro método HTTP → devuelve ERROR.

Protegido por API Key (2f5ae96c-b558-4c7b-a590-a501ae1c3f6c) y un JWT único por transacción.

El proyecto está completamente containerizado con Podman, incluye balanceo de carga con NGINX y un pipeline CI/CD automatizado en GitHub Actions.

🧱 Estructura del proyecto
devopsExercise/
├── src/                            # Código fuente Java
├── pom.xml                         # Configuración Maven + plugins (Checkstyle, SpotBugs, JaCoCo)
├── Dockerfile                      # Build multi-stage (sin secretos)
├── docker-compose.yml              # Define app1, app2 y balanceador NGINX
├── .env                            # Variables runtime (JWT_SECRET_B64, IMAGE_TAG)
├── .github/
│   └── workflows/publish.yml       # Pipeline CI/CD
└── README.md                       # Este archivo

⚙️ Requisitos locales
🔸 Software necesario
Herramienta	Versión mínima	Instalación
Java JDK	17 o 21	Adoptium Temurin

Maven o mvnw	3.8+	Incluido (./mvnw)
Podman	5.x	Podman Windows

podman-compose	1.5.0	pip install podman-compose
OpenSSL	–	Para generar el secreto JWT (openssl rand -base64 32)

🧩 Ejecución local
1️⃣ Generar el secreto
export JWT_SECRET_B64="$(openssl rand -base64 32)"
echo "JWT_SECRET_B64=$JWT_SECRET_B64" > .env

2️⃣ Construir y levantar
python -m podman_compose up -d --build

Esto levantará:

app1 → puerto interno 8080

app2 → puerto interno 8080

nginx (load balancer) → expuesto en localhost:8080

🌐 Prueba del endpoint
1️⃣ Generar JWT (MintJwtRunner)
export JWT="$(./mvnw -q -Dexec.mainClass=com.diegog.devopsExercise.MintJwtRunner exec:java | tr -d '\r\n')"
echo $JWT

2️⃣ Probar
curl -v -X POST \
 -H "X-Parse-REST-API-Key: 2f5ae96c-b558-4c7b-a590-a501ae1c3f6c" \
 -H "X-JWT-KWY: $JWT" \
 -H "Content-Type: application/json" \
 --data-raw '{ "message":"This is a test", "to":"Juan Perez", "from":"Rita Asturia", "timeToLifeSec":45 }' \
 http://localhost:8080/DevOps


✅ Respuesta esperada:

{"message":"Hello Juan Perez your message will be sent"}

⚖️ Validar balanceo

Ejecuta varias veces el comando anterior:

for i in {1..10}; do
  curl -s -o /dev/null -w "%{http_code}\n" \
  -H "X-Parse-REST-API-Key: 2f5ae96c-b558-4c7b-a590-a501ae1c3f6c" \
  -H "X-JWT-KWY: $JWT" \
  -H "Content-Type: application/json" \
  --data-raw '{"message":"t","to":"t","from":"f","timeToLifeSec":10}' \
  http://localhost:8080/DevOps;
done

🧪 Pruebas automáticas

El proyecto usa JUnit + SpringBootTest y se ejecutan automáticamente al construir:

./mvnw clean verify

Esto incluye:

Test de endpoint (DevopsExerciseApplicationTests)

Validación de API Key y JWT

Análisis de cobertura con JaCoCo

Análisis estático con Checkstyle + SpotBugs

🚀 Pipeline CI/CD (GitHub Actions)
Estructura

.github/workflows/publish.yml

Etapas
Etapa	Descripción
build-test	Compila, ejecuta pruebas unitarias, Checkstyle, SpotBugs
build-image	Construye la imagen Docker y la publica en GitHub Container Registry (GHCR)
deploy	Despliega automáticamente en el runner self-hosted (Windows + Podman)
Variables requeridas
Variable	Dónde se define	Descripción
DEVOPS_PWD	Repository → Settings → Secrets → Actions	Token de acceso a GHCR
JWT_SECRET_B64	Secrets	Clave base64 para firmar JWT
Runner self-hosted

Para solventar el error:
"Waiting for a runner to pick up this job..."

Significa que el runner no está en ejecución.

Solución:

Abrir PowerShell y navega al directorio del runner:
en mi caso: 
cd .\devopsExercise\actions-runner
.\run.cmd

mantenerlo abierto mientras se ejecuta el job deploy.

📊 Cobertura y calidad de código

Los reportes se generan automáticamente en:

target/site/jacoco/index.html
target/site/spotbugs.html
target/site/checkstyle.html
