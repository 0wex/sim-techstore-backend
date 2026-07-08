package com.cs2031.techstore.user.domain;

import com.cs2031.techstore.auth.domain.AuthService;
import com.cs2031.techstore.auth.domain.User;
import com.cs2031.techstore.auth.infrastructure.UserRepository;
import com.cs2031.techstore.product.domain.Product;
import com.cs2031.techstore.product.domain.ProductService;
import com.cs2031.techstore.user.infrastructure.CartItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final AuthService authService;
    private final ProductService productService;
    private final UserRepository userRepository;
    private final CartItemRepository cartItemRepository;

    public UserService(AuthService authService,
                       ProductService productService,
                       UserRepository userRepository,
                       CartItemRepository cartItemRepository) {
        this.authService = authService;
        this.productService = productService;
        this.userRepository = userRepository;
        this.cartItemRepository = cartItemRepository;
    }

    @Transactional
    public void addWishlist(String email, Long productId) {
        User user = authService.findByEmail(email);
        Product product = productService.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found: " + productId));
        user.getWishlist().add(product);
        userRepository.save(user);
    }

    @Transactional
    public void removeWishlist(String email, Long productId) {
        User user = authService.findByEmail(email);
        user.getWishlist().removeIf(p -> p.getId().equals(productId));
        userRepository.save(user);
    }

    @Transactional
    public void addToCart(String email, Long productId, Integer quantity) {
        if (quantity == null || quantity < 1) {
            throw new IllegalArgumentException("Quantity must be at least 1");
        }
        User user = authService.findByEmail(email);
        Product product = productService.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found: " + productId));

        cartItemRepository.findByUserIdAndProductId(user.getId(), productId)
                .ifPresentOrElse(
                    item -> item.setQuantity(quantity),
                    () -> cartItemRepository.save(new CartItem(user, product, quantity))
                );
    }

    @Transactional
    public void removeFromCart(String email, Long productId) {
        User user = authService.findByEmail(email);
        cartItemRepository.deleteByUserIdAndProductId(user.getId(), productId);
    }
}
