package drinkshop.service;

import drinkshop.domain.*;
import drinkshop.repository.Repository;
import drinkshop.service.validator.ProductValidator;
import drinkshop.service.validator.ValidationException;
import drinkshop.service.validator.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
class ProductServiceTest {

    private Repository<Integer, Product> mockRepo;
    private ProductService productService;

    @BeforeEach
    @SuppressWarnings("unchecked")
    void setUp() {
        mockRepo = mock(Repository.class);
        productService = new ProductService(mockRepo);
    }

    @Test
    void testTC1_ECP_ValidProduct() {
        Product p = new Product(1, "Latte", 15.5, CategorieBautura.MILK_COFFEE, TipBautura.DAIRY);
        productService.addProduct(p);
        verify(mockRepo, times(1)).save(p);
    }

    @Test
    void testTC2_ECP_NullName() {
        Product p = new Product(2, null, 15.5, CategorieBautura.MILK_COFFEE, TipBautura.DAIRY);
        assertThrows(ValidationException.class, () -> productService.addProduct(p));
    }

    @Test
    void testTC4_ECP_NegativePrice() {
        Product p = new Product(4, "Latte", -1.0, CategorieBautura.MILK_COFFEE, TipBautura.DAIRY);
        assertThrows(ValidationException.class, () -> productService.addProduct(p));
    }

    @Test
    void testTC5_ECP_NullCategory() {
        Product p = new Product(5, "Latte", 15.5, null, TipBautura.DAIRY);
        assertThrows(ValidationException.class, () -> productService.addProduct(p));
    }

    // --- BVA TESTS ---

    @Test
    void testTC3_BVA_NameLength1() {
        Product p = new Product(10, "M", 15.5, CategorieBautura.MILK_COFFEE, TipBautura.DAIRY);
        productService.addProduct(p);
        verify(mockRepo).save(p);
    }

    @Test
    void testTC5_BVA_NameLength255() {
        String longName = "a".repeat(255);
        Product p = new Product(12, longName, 15.5, CategorieBautura.MILK_COFFEE, TipBautura.DAIRY);
        productService.addProduct(p);
        verify(mockRepo).save(p);
    }

    @Test
    void testTC6_BVA_NameLength256_Invalid() {
        String tooLongName = "a".repeat(256);
        Product p = new Product(13, tooLongName, 15.5, CategorieBautura.MILK_COFFEE, TipBautura.DAIRY);
        assertThrows(ValidationException.class, () -> productService.addProduct(p));
    }

    @Test
    void testTC7_BVA_PriceZero_Invalid() {
        Product p = new Product(14, "Latte", 0.0, CategorieBautura.MILK_COFFEE, TipBautura.DAIRY);
        assertThrows(ValidationException.class, () -> productService.addProduct(p));
    }

    @Test
    void testTC8_BVA_PriceMinimumValid() {
        Product p = new Product(15, "Latte", 0.01, CategorieBautura.MILK_COFFEE, TipBautura.DAIRY);
        productService.addProduct(p);
        verify(mockRepo).save(p);
    }

    @Test
    void testTC9_BVA_PriceNegativeBoundary() {
        Product p = new Product(16, "Latte", -0.01, CategorieBautura.MILK_COFFEE, TipBautura.DAIRY);
        assertThrows(ValidationException.class, () -> productService.addProduct(p));
    }
}