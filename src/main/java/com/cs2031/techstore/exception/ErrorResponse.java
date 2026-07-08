package com.cs2031.techstore.exception;

import io.swagger.v3.oas.annotations.media.Schema;

public record ErrorResponse(
        @Schema(description = "Mensaje de error legible", example = "Product not found: 99")
        String error) {}
