# Inventory API

API REST para la gestión de inventario de productos con funcionalidades avanzadas de filtrado, paginación y ordenamiento.

## 📋 Descripción

Sistema de gestión de inventario que permite:
- Crear productos individuales o en lote (bulk insert)
- Listar productos con paginación
- Filtrar productos por categoría, precio mínimo y nombre
- Ordenar productos por diferentes campos
- Gestión de stock y categorías

## 🚀 Tecnologías

- **Java 21**
- **Spring Boot 3.5.9**
- **Spring Data JPA**
- **H2 Database / MySQL** (soporta ambos)
- **Lombok 1.18.36** - Reducción de código boilerplate
- **MapStruct 1.6.3** - Mapeo entre DTOs y entidades
- **Bean Validation** - Validación de datos
- **Spring Data JPA Specifications** - Filtrado dinámico
- **Maven** - Gestión de dependencias

## 📦 Requisitos

- JDK 21 o superior
- Maven 3.6+
- MySQL (opcional, por defecto usa H2)

## 🔧 Instalación y Ejecución

### Clonar el repositorio
```bash
git clone <repository-url>
cd inventory-api
```

### Compilar el proyecto
```bash
mvnw clean install
```

### Ejecutar la aplicación
```bash
mvnw spring-boot:run
```

La aplicación estará disponible en: `http://localhost:8080`

## 🗄️ Base de Datos

El proyecto está configurado para usar **H2** (base de datos en memoria) por defecto. También soporta **MySQL**.

### Consola H2
Accede a la consola web de H2 en: `http://localhost:8080/h2-console`

**Credenciales H2:**
- JDBC URL: `jdbc:h2:mem:inventory`
- Username: `sa`
- Password: *(vacío)*

### Configurar MySQL (Opcional)
Para usar MySQL, actualiza `application.yaml`:
```yaml
datasource:
  url: jdbc:mysql://localhost:3306/inventory
  username: tu_usuario
  password: tu_password
  driverClassName: com.mysql.cj.jdbc.Driver

jpa:
  database-platform: org.hibernate.dialect.MySQLDialect
```

## 📡 Endpoints API

### Productos

#### Crear productos (Bulk Insert)
```http
POST /products
Content-Type: application/json

[
  {
    "sku": "PROD-001",
    "name": "Laptop Dell XPS 13",
    "description": "Laptop de alta gama con procesador Intel i7",
    "price": 1299.99,
    "stock": 15,
    "category": "ELECTRONICS"
  },
  {
    "sku": "PROD-002",
    "name": "Mesa de Comedor",
    "description": "Mesa de madera para 6 personas",
    "price": 450.00,
    "stock": 8,
    "category": "HOME"
  }
]
```

#### Listar productos con filtros y paginación
```http
GET /products?page=0&size=10&sort=price,desc&category=ELECTRONICS&minPrice=50&name=laptop
```

**Parámetros de Query:**
- `page` - Número de página (default: 0)
- `size` - Tamaño de página (default: 10)
- `sort` - Campo y dirección de ordenamiento (ej: `price,desc`, `name,asc`)
- `category` - Filtrar por categoría (`ELECTRONICS`, `HOME`, `TOYS`)
- `minPrice` - Precio mínimo
- `name` - Buscar por nombre (búsqueda parcial)

**Ejemplo de Respuesta:**
```json
{
  "content": [
    {
      "id": 1,
      "sku": "PROD-001",
      "name": "Laptop Dell XPS 13",
      "description": "Laptop de alta gama con procesador Intel i7",
      "price": 1299.99,
      "stock": 15,
      "category": "ELECTRONICS"
    }
  ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 10
  },
  "totalElements": 1,
  "totalPages": 1
}
```

## 📁 Estructura del Proyecto

```
src/main/java/com/warehouse/inventory_api/
├── controller/                # Controladores REST
│   └── ProductController.java
├── domain/                    # Entidades JPA
│   ├── Product.java
│   └── Category.java          # Enum de categorías
├── dto/                       # Data Transfer Objects
│   ├── ProductRequestDTO.java
│   └── ProductResponseDTO.java
├── mapper/                    # Mappers MapStruct
│   └── ProductMapper.java
├── repository/                # Repositorios Spring Data
│   ├── ProductRepository.java
│   └── spec/
│       └── ProductSpecifications.java  # JPA Specifications
└── service/                   # Lógica de negocio
    └── ProductService.java
```

## 🔍 Características Principales

### 1. **Paginación**
Usa Spring Data Pageable para paginar resultados eficientemente.

### 2. **Filtros Dinámicos con JPA Specifications**
Permite combinar múltiples filtros de forma dinámica:
- Filtrar por categoría
- Filtrar por precio mínimo
- Búsqueda por nombre (LIKE)

### 3. **Ordenamiento Flexible**
Ordena por cualquier campo: `price`, `name`, `stock`, `category`

### 4. **Bulk Insert**
Crea múltiples productos en una sola petición para facilitar pruebas y carga de datos.

### 5. **Validación de Datos**
Bean Validation en DTOs para garantizar integridad de datos.

## 🎯 Categorías Disponibles

- `ELECTRONICS` - Electrónica
- `HOME` - Hogar y muebles
- `TOYS` - Juguetes

## 📊 Ejemplos de Uso

### Ejemplo 1: Listar todos los productos electrónicos ordenados por precio descendente
```bash
GET /products?category=ELECTRONICS&sort=price,desc
```

### Ejemplo 2: Buscar productos con precio mayor a $100, paginado
```bash
GET /products?minPrice=100&page=0&size=20
```

### Ejemplo 3: Buscar productos por nombre que contengan "laptop"
```bash
GET /products?name=laptop
```

### Ejemplo 4: Combinación de filtros
```bash
GET /products?category=HOME&minPrice=200&name=mesa&sort=price,asc&page=0&size=5
```

## 🛠️ Características Técnicas

- **JPA Specifications**: Filtrado dinámico y composable
- **Paginación**: Manejo eficiente de grandes volúmenes de datos
- **DTOs**: Separación entre capa de presentación y dominio
- **MapStruct**: Mapeo automático y eficiente
- **Lombok**: Reducción de código boilerplate
- **Spring DevTools**: Hot reload en desarrollo
- **Actuator**: Endpoints de monitoreo y métricas

## 📝 Modelo de Datos

### Product
| Campo       | Tipo        | Descripción                    | Restricciones       |
|-------------|-------------|--------------------------------|---------------------|
| id          | Long        | ID único del producto          | PK, Auto-increment  |
| sku         | String      | Código SKU único               | UNIQUE, NOT NULL    |
| name        | String      | Nombre del producto            | NOT NULL            |
| description | String      | Descripción detallada          | -                   |
| price       | BigDecimal  | Precio del producto            | NOT NULL            |
| stock       | Integer     | Cantidad en inventario         | NOT NULL            |
| category    | Category    | Categoría del producto         | ENUM, NOT NULL      |

