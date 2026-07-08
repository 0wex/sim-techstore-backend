# TechStore — Backend API

Backend de la plataforma de e-commerce **TechStore**, una tienda de tecnología que vende smartphones, laptops, audio, gaming y más. Construido con Spring Boot 3.4 + JWT + H2 en memoria.

---

## Cómo ejecutar

```bash
./mvnw spring-boot:run
```

> No requiere variables de entorno ni base de datos externa. La base de datos H2 se crea en memoria al arrancar y se destruye al apagar.

El servidor corre en `http://localhost:8080`.

### Documentación de la API (Swagger)

📖 **Documentación en línea: [https://0wex.github.io/sim-techstore-backend/](https://0wex.github.io/sim-techstore-backend/)**

Ahí puedes consultar todos los endpoints y sus DTOs sin levantar nada. Con el backend corriendo también la tienes en `http://localhost:8080/swagger-ui.html` (JSON OpenAPI en `/v3/api-docs`). Usa el botón **Authorize** con el token de register/login para probar los endpoints protegidos.

### Consola H2 (opcional)

Disponible en `http://localhost:8080/h2-console`

| Campo    | Valor                     |
|----------|---------------------------|
| JDBC URL | `jdbc:h2:mem:techstoredb` |
| Usuario  | `sa`                      |
| Password | *(vacío)*                 |

---

## Datos precargados

Al arrancar, el backend carga automáticamente **20 productos**:

| ID | Producto                  | Marca        | Precio (S/) | Categoría      |
|----|---------------------------|--------------|-------------|----------------|
| 1  | iPhone 15 Pro             | Apple        | 5499.00     | Smartphones    |
| 2  | Galaxy S24 Ultra          | Samsung      | 5299.00     | Smartphones    |
| 3  | MacBook Air M3            | Apple        | 6299.00     | Laptops        |
| 4  | Dell XPS 13               | Dell         | 5799.00     | Laptops        |
| 5  | Lenovo ThinkPad X1 Carbon | Lenovo       | 6999.00     | Laptops        |
| 6  | iPad Air                  | Apple        | 2999.00     | Tablets        |
| 7  | Kindle Paperwhite         | Amazon       | 699.00      | Tablets        |
| 8  | Sony WH-1000XM5           | Sony         | 1499.00     | Audio          |
| 9  | AirPods Pro 2             | Apple        | 1099.00     | Audio          |
| 10 | JBL Flip 6                | JBL          | 549.00      | Audio          |
| 11 | MX Master 3S              | Logitech     | 449.00      | Accesorios     |
| 12 | Keychron K2               | Keychron     | 399.00      | Accesorios     |
| 13 | Samsung T7 SSD 1TB        | Samsung      | 499.00      | Almacenamiento |
| 14 | Anker PowerCore 20K       | Anker        | 249.00      | Accesorios     |
| 15 | LG UltraGear 27           | LG           | 1299.00     | Monitores      |
| 16 | Raspberry Pi 5            | Raspberry Pi | 449.00      | Electrónica    |
| 17 | Nintendo Switch OLED      | Nintendo     | 1599.00     | Gaming         |
| 18 | PlayStation 5 Slim        | Sony         | 2499.00     | Gaming         |
| 19 | GoPro Hero 12             | GoPro        | 1899.00     | Cámaras        |
| 20 | Echo Dot 5                | Amazon       | 249.00      | Smart Home     |

---

## Autenticación

El backend usa **JWT (Bearer Token)**. Los endpoints protegidos requieren el header:

```
Authorization: Bearer <token>
```

El token se obtiene al registrarse o iniciar sesión.

---

## Resumen de endpoints

| Método | Ruta                   | Acceso      | Descripción                                |
|--------|------------------------|-------------|---------------------------------------------|
| POST   | `/api/auth/register`   | Público     | Registrar nuevo usuario                     |
| POST   | `/api/auth/login`      | Público     | Iniciar sesión y obtener token              |
| GET    | `/api/products/search` | Público     | Buscar productos (filtros opcionales)       |
| GET    | `/api/products/{id}`   | Público     | Detalle de un producto                      |
| POST   | `/api/user/wishlist`   | Autenticado | Agregar producto a la wishlist              |
| DELETE | `/api/user/wishlist`   | Autenticado | Remover producto de la wishlist             |
| POST   | `/api/user/cart`       | Autenticado | Agregar producto al carrito (con cantidad)  |
| DELETE | `/api/user/cart`       | Autenticado | Remover producto del carrito                |

---

## Errores comunes

| Código | Causa                                                        |
|--------|--------------------------------------------------------------|
| `400`  | Campos requeridos faltantes, formato inválido o cantidad < 1 |
| `403`  | Endpoint protegido sin token o token inválido                |
| `500`  | Error interno del servidor                                   |
