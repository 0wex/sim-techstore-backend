package com.cs2031.techstore.product.application;

import com.cs2031.techstore.exception.ErrorResponse;
import com.cs2031.techstore.product.domain.Product;
import com.cs2031.techstore.product.domain.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@Tag(name = "Productos", description = "Catálogo público: búsqueda con filtros y detalle por ID.")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    record ProductResponse(
            @Schema(description = "Identificador único del producto", example = "1") Long id,
            @Schema(description = "Nombre del producto", example = "iPhone 15 Pro") String name,
            @Schema(description = "Marca del producto", example = "Apple") String brand,
            @Schema(description = "Precio en soles (S/)", example = "5499.0") Double price,
            @Schema(description = "Categoría del producto", example = "Smartphones") String category,
            @Schema(description = "URL de la imagen del producto", example = "https://placehold.co/150?text=Imagen+de+iPhone+15+Pro") String imageUrl) {}

    @Operation(summary = "Buscar productos con filtros",
            description = "Todos los filtros son opcionales y combinables. Sin parámetros devuelve los 20 productos.")
    @ApiResponse(responseCode = "200", description = "Lista de productos que cumplen los filtros")
    @GetMapping("/search")
    public ResponseEntity<List<ProductResponse>> search(
            @Parameter(description = "Busca por nombre (contiene, sin distinguir mayúsculas)", example = "pro")
            @RequestParam(required = false) String name,
            @Parameter(description = "Busca por marca (contiene, sin distinguir mayúsculas)", example = "Apple")
            @RequestParam(required = false) String brand,
            @Parameter(description = "Precio mínimo (inclusive)", example = "1000")
            @RequestParam(required = false) Double minPrice,
            @Parameter(description = "Precio máximo (inclusive)", example = "6000")
            @RequestParam(required = false) Double maxPrice) {
        List<ProductResponse> result = productService.search(name, brand, minPrice, maxPrice).stream()
                .map(ProductController::toResponse)
                .toList();
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "Detalle de un producto")
    @ApiResponse(responseCode = "200", description = "Producto encontrado")
    @ApiResponse(responseCode = "400", description = "El producto no existe",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class),
                    examples = @ExampleObject(value = "{\"error\": \"Product not found: 99\"}")))
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getById(
            @Parameter(description = "ID del producto", example = "1") @PathVariable Long id) {
        return ResponseEntity.ok(toResponse(productService.getById(id)));
    }

    private static ProductResponse toResponse(Product p) {
        return new ProductResponse(p.getId(), p.getName(), p.getBrand(),
                p.getPrice(), p.getCategory(), p.getImageUrl());
    }
}
