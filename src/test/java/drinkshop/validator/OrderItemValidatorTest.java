package drinkshop.validator;

import drinkshop.domain.OrderItem;
import drinkshop.domain.Product;
import drinkshop.service.validator.OrderItemValidator;
import drinkshop.service.validator.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class OrderItemValidatorTest {

    private OrderItemValidator validator;

    @BeforeEach
    public void setUp() {
        validator = new OrderItemValidator();
    }

    @Test
    public void testValidate_ValidItem_F02_P01() {
        Product validProduct = new Product(1, "Test Product", 10.0, null, null);
        OrderItem item = new OrderItem(validProduct, 5);

        assertDoesNotThrow(() -> validator.validate(item));
    }

    @Test
    public void testValidate_InvalidProductId_F02_P02() {
        Product invalidProduct = new Product(0, "Test Product", 10.0, null, null);
        OrderItem item = new OrderItem(invalidProduct, 2);

        ValidationException exception = assertThrows(ValidationException.class, () -> {
            validator.validate(item);
        });
        assertEquals("Product ID invalid!\n", exception.getMessage());
    }

    @Test
    public void testValidate_InvalidQuantity_F02_P03() {
        Product validProduct = new Product(2, "Test Product", 10.0, null, null);
        OrderItem item = new OrderItem(validProduct, -1);

        ValidationException exception = assertThrows(ValidationException.class, () -> {
            validator.validate(item);
        });
        assertEquals("Cantitate invalida!\n", exception.getMessage());
    }

    @Test
    public void testValidate_InvalidIdAndQuantity_F02_P04() {
        Product invalidProduct = new Product(-3, "Test Product", 10.0, null, null);
        OrderItem item = new OrderItem(invalidProduct, 0);

        ValidationException exception = assertThrows(ValidationException.class, () -> {
            validator.validate(item);
        });
        assertTrue(exception.getMessage().contains("Product ID invalid!"));
        assertTrue(exception.getMessage().contains("Cantitate invalida!"));
    }
}