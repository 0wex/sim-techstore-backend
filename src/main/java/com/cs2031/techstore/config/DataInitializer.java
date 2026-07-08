package com.cs2031.techstore.config;

import com.cs2031.techstore.product.domain.Product;
import com.cs2031.techstore.product.infrastructure.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private final ProductRepository productRepository;

    public DataInitializer(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public void run(String... args) {
        if (productRepository.count() > 0) return;

        List<Product> products = List.of(
            new Product("iPhone 15 Pro", "Apple", 5499.0, "Smartphones", "https://placehold.co/150"),
            new Product("Galaxy S24 Ultra", "Samsung", 5299.0, "Smartphones", "https://placehold.co/150"),
            new Product("MacBook Air M3", "Apple", 6299.0, "Laptops", "https://placehold.co/150"),
            new Product("Dell XPS 13", "Dell", 5799.0, "Laptops", "https://placehold.co/150"),
            new Product("Lenovo ThinkPad X1 Carbon", "Lenovo", 6999.0, "Laptops", "https://placehold.co/150"),
            new Product("iPad Air", "Apple", 2999.0, "Tablets", "https://placehold.co/150"),
            new Product("Kindle Paperwhite", "Amazon", 699.0, "Tablets", "https://placehold.co/150"),
            new Product("Sony WH-1000XM5", "Sony", 1499.0, "Audio", "https://placehold.co/150"),
            new Product("AirPods Pro 2", "Apple", 1099.0, "Audio", "https://placehold.co/150"),
            new Product("JBL Flip 6", "JBL", 549.0, "Audio", "https://placehold.co/150"),
            new Product("MX Master 3S", "Logitech", 449.0, "Accesorios", "https://placehold.co/150"),
            new Product("Keychron K2", "Keychron", 399.0, "Accesorios", "https://placehold.co/150"),
            new Product("Samsung T7 SSD 1TB", "Samsung", 499.0, "Almacenamiento", "https://placehold.co/150"),
            new Product("Anker PowerCore 20K", "Anker", 249.0, "Accesorios", "https://placehold.co/150"),
            new Product("LG UltraGear 27", "LG", 1299.0, "Monitores", "https://placehold.co/150"),
            new Product("Raspberry Pi 5", "Raspberry Pi", 449.0, "Electrónica", "https://placehold.co/150"),
            new Product("Nintendo Switch OLED", "Nintendo", 1599.0, "Gaming", "https://placehold.co/150"),
            new Product("PlayStation 5 Slim", "Sony", 2499.0, "Gaming", "https://placehold.co/150"),
            new Product("GoPro Hero 12", "GoPro", 1899.0, "Cámaras", "https://placehold.co/150"),
            new Product("Echo Dot 5", "Amazon", 249.0, "Smart Home", "https://placehold.co/150")
        );

        productRepository.saveAll(products);
    }
}
