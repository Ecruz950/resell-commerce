package com.ecruz.resell_ecommerce.cart;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartServiceTest {

    @Mock
    private CartRepository cartRepository;

    @InjectMocks
    private CartService cartService;

    private Cart testCartItem;

    @BeforeEach
    void setUp() {
        testCartItem = new Cart();
        testCartItem.setId(1L);
        testCartItem.setuserId(1L);
        testCartItem.setproductId(1L);
    }

    /**
     * Test that getCartByUserId returns only cart items belonging to the specified user
     * Verifies filtering logic works correctly when fetching user's cart
     */
    @Test
    void getCartByUserId_ReturnsUserCart() {
        List<Cart> allCartItems = Arrays.asList(testCartItem);
        when(cartRepository.findAll()).thenReturn(allCartItems);

        List<Cart> result = cartService.getCartByUserId(1L);

        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getUserID());
        assertEquals(1L, result.get(0).getproductId());
        verify(cartRepository).findAll();
    }

    /**
     * Test that addProductToCart successfully saves a cart item to the repository
     * Verifies the service calls the repository save method with correct cart item
     */
    @Test
    void addProductToCart_Success() {
        when(cartRepository.save(any(Cart.class))).thenReturn(testCartItem);

        cartService.addProductToCart(testCartItem);

        verify(cartRepository).save(testCartItem);
    }

    /**
     * Test that removeProductFromCart successfully removes a specific product from user's cart
     * Verifies the service calls the repository delete method with correct user and product IDs
     */
    @Test
    void removeProductFromCart_Success() {
        doNothing().when(cartRepository).deleteByUserIdAndProductId(1L, 1L);

        cartService.removeProductFromCart(1L, 1L);

        verify(cartRepository).deleteByUserIdAndProductId(1L, 1L);
    }
}