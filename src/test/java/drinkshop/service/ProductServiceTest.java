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
    private Validator<Product> validator;
    private ProductService productService;

    @BeforeEach
    @SuppressWarnings("unchecked")
    void setUp() {
        mockRepo = mock(Repository.class);
        productService = new ProductService(mockRepo);
    }

    @Test
    void testAddProduct_ValidProduct_SavesToRepo() {
        Product validProduct = new Product(1, "Fanta", 15.0, CategorieBautura.MILK_COFFEE, TipBautura.DAIRY);
        
        productService.addProduct(validProduct);
        
        verify(mockRepo, times(1)).save(validProduct);
    }

    @Test
    void testAddProduct_InvalidName_ThrowsException() {
        // Nume gol
        Product invalidProduct = new Product(1, "", 15.0, CategorieBautura.MILK_COFFEE, TipBautura.DAIRY);
        
        assertThrows(ValidationException.class, () -> productService.addProduct(invalidProduct));
        
        // Verificăm că NU s-a apelat metoda save (a picat la validare)
        verify(mockRepo, never()).save(any());
    }

    @Test
    void testAddProduct_InvalidPrice_ThrowsException() {
        // Preț negativ sau zero
        Product invalidProduct = new Product(1, "Fanta", -1.0, CategorieBautura.MILK_COFFEE, TipBautura.DAIRY);
        
        assertThrows(ValidationException.class, () -> productService.addProduct(invalidProduct));
        verify(mockRepo, never()).save(any());
    }
}