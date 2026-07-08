package com.cs2031.techstore.user.application;

import com.cs2031.techstore.exception.ErrorResponse;
import com.cs2031.techstore.user.domain.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@Tag(name = "Usuario — Wishlist y Carrito",
     description = "Endpoints protegidos: requieren el header Authorization: Bearer <token>. Sin token devuelven 403.")
@SecurityRequirement(name = "bearerAuth")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    record ProductIdRequest(
            @Schema(description = "ID del producto", example = "1")
            @NotNull Long productId) {}

    record CartRequest(
            @Schema(description = "ID del producto", example = "1")
            @NotNull Long productId,
            @Schema(description = "Cantidad deseada (mínimo 1)", example = "2")
            @NotNull Integer quantity) {}

    @Operation(summary = "Agregar producto a la wishlist")
    @ApiResponse(responseCode = "200", description = "Agregado (sin cuerpo)")
    @ApiResponse(responseCode = "400", description = "El producto no existe",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class),
                    examples = @ExampleObject(value = "{\"error\": \"Product not found: 99\"}")))
    @ApiResponse(responseCode = "403", description = "Sin token o token inválido", content = @Content)
    @PostMapping("/wishlist")
    public ResponseEntity<Void> addWishlist(@AuthenticationPrincipal UserDetails principal,
                                            @Valid @RequestBody ProductIdRequest req) {
        userService.addWishlist(principal.getUsername(), req.productId());
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Remover producto de la wishlist")
    @ApiResponse(responseCode = "200", description = "Removido (sin cuerpo)")
    @ApiResponse(responseCode = "403", description = "Sin token o token inválido", content = @Content)
    @DeleteMapping("/wishlist")
    public ResponseEntity<Void> removeWishlist(@AuthenticationPrincipal UserDetails principal,
                                               @Valid @RequestBody ProductIdRequest req) {
        userService.removeWishlist(principal.getUsername(), req.productId());
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Agregar producto al carrito",
            description = "Si el producto ya está en el carrito, actualiza la cantidad (upsert).")
    @ApiResponse(responseCode = "200", description = "Agregado o actualizado (sin cuerpo)")
    @ApiResponse(responseCode = "400", description = "Cantidad menor a 1 o producto inexistente",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class),
                    examples = @ExampleObject(value = "{\"error\": \"Quantity must be at least 1\"}")))
    @ApiResponse(responseCode = "403", description = "Sin token o token inválido", content = @Content)
    @PostMapping("/cart")
    public ResponseEntity<Void> addToCart(@AuthenticationPrincipal UserDetails principal,
                                          @Valid @RequestBody CartRequest req) {
        userService.addToCart(principal.getUsername(), req.productId(), req.quantity());
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Remover producto del carrito")
    @ApiResponse(responseCode = "200", description = "Removido (sin cuerpo)")
    @ApiResponse(responseCode = "403", description = "Sin token o token inválido", content = @Content)
    @DeleteMapping("/cart")
    public ResponseEntity<Void> removeFromCart(@AuthenticationPrincipal UserDetails principal,
                                               @Valid @RequestBody ProductIdRequest req) {
        userService.removeFromCart(principal.getUsername(), req.productId());
        return ResponseEntity.ok().build();
    }
}
