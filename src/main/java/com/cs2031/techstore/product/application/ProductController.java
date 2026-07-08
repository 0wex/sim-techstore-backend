package com.cs2031.techstore.product.application;

import com.cs2031.techstore.product.domain.Product;
import com.cs2031.techstore.product.domain.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    record ProductResponse(Long id, String name, String brand, Double price,
                           String category, String imageUrl) {}

    @GetMapping("/search")
    public ResponseEntity<List<ProductResponse>> search(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice) {
        List<ProductResponse> result = productService.search(name, brand, minPrice, maxPrice).stream()
                .map(ProductController::toResponse)
                .toList();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(toResponse(productService.getById(id)));
    }

    private static ProductResponse toResponse(Product p) {
        return new ProductResponse(p.getId(), p.getName(), p.getBrand(),
                p.getPrice(), p.getCategory(), p.getImageUrl());
    }
}
