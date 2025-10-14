package com.ecruz.resell_ecommerce.product;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    private Product testProduct;

    private Rating testRating;

    @BeforeEach
    void setUp() {
        testRating = new Rating();
        testRating.setCount(10);
        testRating.setRate(4.5);
        
        testProduct = new Product();
        testProduct.setId(1);
        testProduct.setTitle("Test Product");
        testProduct.setPrice(99.99);
        testProduct.setDescription("Test Description");
        testProduct.setCategory("Electronics");
        testProduct.setImage("test-image.jpg");
        testProduct.setRating(testRating);
    }

    /**
     * Test that getAllProducts returns all products from the repository
     * Verifies the service correctly delegates to repository and returns the result
     */
    @Test
    void getAllProducts_ReturnsAllProducts() {
        List<Product> products = Arrays.asList(testProduct);
        when(productRepository.findAll()).thenReturn(products);

        List<Product> result = productService.getAllProducts();

        assertEquals(1, result.size());
        assertEquals("Test Product", result.get(0).getTitle());
        verify(productRepository).findAll();
    }

    /**
     * Test that getProductById returns a product when it exists in the repository
     * Verifies successful product retrieval by ID
     */
    @Test
    void getProductById_Found() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));

        Product result = productService.getProductById(1L);

        assertNotNull(result);
        assertEquals("Test Product", result.getTitle());
        verify(productRepository).findById(1L);
    }

    /**
     * Test that getProductById returns null when product doesn't exist
     * Verifies proper handling of non-existent product IDs
     */
    @Test
    void getProductById_NotFound() {
        when(productRepository.findById(999L)).thenReturn(Optional.empty());

        Product result = productService.getProductById(999L);

        assertNull(result);
        verify(productRepository).findById(999L);
    }

    /**
     * Test that createProduct successfully saves a new product
     * Verifies the service delegates to repository save method
     */
    @Test
    void createProduct_Success() {
        when(productRepository.save(any(Product.class))).thenReturn(testProduct);

        Product result = productService.createProduct(testProduct);

        assertNotNull(result);
        assertEquals("Test Product", result.getTitle());
        verify(productRepository).save(testProduct);
    }

    /**
     * Test that updateProductPrice successfully updates an existing product
     * Verifies the service finds, updates, and saves the product
     */
    @Test
    void updateProductPrice_Success() {
        Product updatedProduct = new Product();
        updatedProduct.setTitle("Updated Product");
        updatedProduct.setPrice(149.99);
        
        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));
        when(productRepository.save(any(Product.class))).thenReturn(testProduct);

        Product result = productService.updateProductPrice(1L, updatedProduct);

        assertNotNull(result);
        verify(productRepository).findById(1L);
        verify(productRepository).save(testProduct);
    }

    /**
     * Test that updateProductPrice returns null when product doesn't exist
     * Verifies proper handling of update attempts on non-existent products
     */
    @Test
    void updateProductPrice_NotFound() {
        Product updatedProduct = new Product();
        updatedProduct.setTitle("Updated Product");
        
        when(productRepository.findById(999L)).thenReturn(Optional.empty());

        Product result = productService.updateProductPrice(999L, updatedProduct);

        assertNull(result);
        verify(productRepository).findById(999L);
        verify(productRepository, never()).save(any(Product.class));
    }

    /**
     * Test that deleteProduct successfully deletes a product by ID
     * Verifies the service delegates to repository deleteById method
     */
    @Test
    void deleteProduct_Success() {
        doNothing().when(productRepository).deleteById(1L);

        productService.deleteProduct(1L);

        verify(productRepository).deleteById(1L);
    }
}