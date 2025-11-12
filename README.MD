# 💱 FX Deals — Data Warehouse Service

### Description
**FX Deals** est une application Spring Boot conçue pour gérer et importer des données de transactions financières (Foreign Exchange Deals).  
Elle permet de **sauvegarder**, **consulter** et **importer en masse** des transactions dans une base de données MySQL, tout en garantissant la **validation** et la **cohérence** des données.

---

## 🚀 Fonctionnalités principales

- ✅ Enregistrement d’un deal unique (`POST /api/v1/deals`)
- 📄 Récupération d’un deal par ID (`GET /api/v1/deals/{id}`)
- 📦 Importation de plusieurs deals en une seule requête (`POST /api/v1/deals/batch`)
- ⚠️ Gestion des erreurs :
    - Deal déjà existant (`DuplicateDealException`)
    - Données invalides (`InvalidDealDataException`)
    - Deal non trouvé (`DealNotFoundException`)
- 🧩 Validation des données avec **Jakarta Validation**
- 🗄️ Persistance avec **Spring Data JPA** et **MySQL**
- 🐳 Support complet de **Docker & Docker Compose**

---

## 🧠 Architecture du projet

```text
src/
 ├── controller/
 │   └── DealController.java       → Gère les endpoints REST
 ├── service/
 │   ├── DealServiceImpl.java      → Contient la logique métier
 │   └── IDealService.java         → Interface du service
 ├── entity/
 │   └── Deal.java                 → Représente la table `deals`
 ├── repository/
 │   └── DealRepository.java       → Interface JPA pour accéder à la base
 ├── dto/
 │   ├── DealRequestDto.java       → Données reçues depuis l’API
 │   └── DealResponseDto.java      → Données renvoyées à l’utilisateur
 ├── mapper/
 │   └── DealMapper.java           → Conversion entre DTO et entité
 ├── exception/
 │   ├── DuplicateDealException.java
 │   ├── InvalidDealDataException.java
 │   ├── DealNotFoundException.java
 │   └── GlobalExceptionHandler.java
 └── FxDealsApplication.java       → Point d’entrée de l’application
# 🧰 Installation et exécution

## 1️⃣ Cloner le projet
```bash
git clone https://github.com/lakrounehamza/fx-deals.git
cd fx-deals
```

## 2️⃣ Configurer les variables de la base de données
Dans `src/main/resources/application.yml` :
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/fx_deals_db
    username: root
    password: root
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
```

## 3️⃣ Lancer avec Docker
Assure-toi que **Docker** et **Docker Compose** sont installés, puis exécute :
```bash
docker-compose up --build
```

Cela démarrera :
- un conteneur **MySQL** (`fx_deals_mysql`)
- un conteneur **Spring Boot App** (`fx_deals_app`)

---

# 🧪 Exemples d’appels API

## ➕ Ajouter un deal
**POST** `/api/v1/deals`
```json
{
  "id": "FX123456",
  "fromCurrency": "USD",
  "toCurrency": "EUR",
  "dealTimestamp": "2025-11-12T09:30:00",
  "dealAmount": 12000.50
}
```

### Réponse :
```json
{
  "id": "FX123456",
  "fromCurrency": "USD",
  "toCurrency": "EUR",
  "dealTimestamp": "2025-11-12T09:30:00",
  "dealAmount": 12000.5
}
```

## 📄 Récupérer un deal
**GET** `/api/v1/deals/FX123456`
```json
{
  "id": "FX123456",
  "fromCurrency": "USD",
  "toCurrency": "EUR",
  "dealTimestamp": "2025-11-12T09:30:00",
  "dealAmount": 12000.5
}
```

## 📦 Importer plusieurs deals
**POST** `/api/v1/deals/batch`
```json
[
  {
    "id": "FX1001",
    "fromCurrency": "GBP",
    "toCurrency": "USD",
    "dealTimestamp": "2025-11-12T11:00:00",
    "dealAmount": 5000
  },
  {
    "id": "FX1002",
    "fromCurrency": "EUR",
    "toCurrency": "JPY",
    "dealTimestamp": "2025-11-12T11:05:00",
    "dealAmount": 10000
  }
]
```

---

# ⚠️ Gestion des exceptions

| Exception | Cause | Code HTTP |
|------------|--------|-----------|
| `DuplicateDealException` | L’ID du deal existe déjà | `400 Bad Request` |
| `InvalidDealDataException` | FromCurrency = ToCurrency | `400 Bad Request` |
| `DealNotFoundException` | Aucun deal trouvé avec cet ID | `404 Not Found` |

---

# 🧑‍💻 Auteur
**👨‍💻 Hamza Lakrouine**  
📍 Étudiant en développement Full Stack à YouCode  
🔗 [GitHub](https://github.com/lakrounehamza)

---

# 🏁 Objectif du projet
Ce projet a été réalisé dans le cadre d’un test technique pour le poste de **Java Developer** chez **ProgressSoft Corporation**, portant sur la création d’un **Clustered Data Warehouse** pour l’analyse des FX Deals.
