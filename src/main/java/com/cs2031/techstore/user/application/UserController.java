package com.cs2031.techstore.user.application;

import com.cs2031.techstore.user.domain.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    record ProductIdRequest(@NotNull Long productId) {}

    record CartRequest(@NotNull Long productId, @NotNull Integer quantity) {}

    @PostMapping("/wishlist")
    public ResponseEntity<Void> addWishlist(@AuthenticationPrincipal UserDetails principal,
                                            @Valid @RequestBody ProductIdRequest req) {
        userService.addWishlist(principal.getUsername(), req.productId());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/wishlist")
    public ResponseEntity<Void> removeWishlist(@AuthenticationPrincipal UserDetails principal,
                                               @Valid @RequestBody ProductIdRequest req) {
        userService.removeWishlist(principal.getUsername(), req.productId());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/cart")
    public ResponseEntity<Void> addToCart(@AuthenticationPrincipal UserDetails principal,
                                          @Valid @RequestBody CartRequest req) {
        userService.addToCart(principal.getUsername(), req.productId(), req.quantity());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/cart")
    public ResponseEntity<Void> removeFromCart(@AuthenticationPrincipal UserDetails principal,
                                               @Valid @RequestBody ProductIdRequest req) {
        userService.removeFromCart(principal.getUsername(), req.productId());
        return ResponseEntity.ok().build();
    }
}
