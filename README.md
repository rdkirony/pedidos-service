# 🚀 Projeto  para gerencimento de pedidos

## 📦 Stack

* **Java 21**
* **Spring Boot**
* **MySQL 8.4**
* **Maven 3.9+**

---

## ✅ Pré‑requisitos

* **JDK 21** instalado (obrigatório)
* **Maven 3.9+** instalado (obrigatório)
* **Docker + Docker Compose** (opcional, para MySQL)

### Validar versão do Java

```bash
java -version
```

Saída esperada:

```text
openjdk version "21"
```

Se não for **21**, ajuste o `JAVA_HOME` para apontar para o JDK correto.

#### Checar JAVA\_HOME

* **Linux / MacOS**

```
echo $JAVA_HOME
```

* **Windows (PowerShell)**

```powershell
echo $env:JAVA_HOME
```

### Validar instalação do Maven

```
mvn -version
```

Saída esperada:

```text
Apache Maven 3.9.x
Java version: 21
```

---

## ⚙️ Variáveis de Ambiente

```env
SERVER_PORT=8585
DB_URL=jdbc:mysql://localhost:3306/pedidos
DB_USERNAME=pedidos
DB_PASSWORD=pedidos
ENVIRONMENT=dev
```

---

## 🐬 Docker Compose (MySQL)

```yaml
version: "3.9"
services:
  mysql:
    image: mysql:8.4
    container_name: mysql-pedidos
    command: >
      --character-set-server=utf8mb4
      --collation-server=utf8mb4_unicode_ci
      --default-time-zone=America/Sao_Paulo
      --max-connections=200
      --sql-mode=STRICT_TRANS_TABLES,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION
    environment:
      MYSQL_ROOT_PASSWORD: ${MYSQL_ROOT_PASSWORD:-root}
      MYSQL_DATABASE: ${MYSQL_DATABASE:-pedidos}
      MYSQL_USER: ${MYSQL_USER:-pedidos}
      MYSQL_PASSWORD: ${MYSQL_PASSWORD:-pedidos}
      TZ: ${TZ:-America/Sao_Paulo}
    ports:
      - "3306:3306"
    volumes:
      - mysql_data:/var/lib/mysql
      - ./initdb:/docker-entrypoint-initdb.d
    healthcheck:
      test: ["CMD-SHELL", "mysqladmin ping -h 127.0.0.1 -uroot -p$$MYSQL_ROOT_PASSWORD || exit 1"]
      interval: 10s
      timeout: 5s
      retries: 10
      start_period: 30s
    restart: unless-stopped
volumes:
  mysql_data:
```
## ⚙️ Variáveis de do banco MYSQL

```env
MYSQL_ROOT_PASSWORD=supersegredo
MYSQL_DATABASE=pedidos
MYSQL_USER=pedidos
MYSQL_PASSWORD=pedidos
TZ=America/Sao_Paulo
```

---


## ▶️ Executando Localmente (Maven)

### 1. Subir o MySQL (opcional, se não tiver instalado)

```
docker compose up -d mysql
```

### 2. Rodar aplicação

```
./mvnw spring-boot:run ou mvn spring-boot:run
```

---

A aplicação estará disponível em:

* **App:** `http://localhost:8585`
* **Swagger UI:** `http://localhost:8585/swagger-ui.html`