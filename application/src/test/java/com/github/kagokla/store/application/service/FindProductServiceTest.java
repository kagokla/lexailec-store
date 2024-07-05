package com.github.kagokla.store.application.service;

import com.github.kagokla.store.application.port.out.persistence.ProductRepository;
import com.github.kagokla.store.model.TestModelFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FindProductServiceTest extends TestModelFactory {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private FindProductService findProductService;

    @Test
    void shouldFailWhenTryingToFindProductWithNullQuery() {
        assertThatIllegalArgumentException().isThrownBy(() -> findProductService.findByNameOrDescription(null));
    }

    @Test
    void shouldFailWhenTryingToFindProductWithQueryTooShort() {
        assertThatIllegalArgumentException().isThrownBy(() -> findProductService.findByNameOrDescription(""));
        assertThatIllegalArgumentException().isThrownBy(() -> findProductService.findByNameOrDescription("a"));
    }

    @Test
    void shouldSucceedWhenTryingToFindOneProductByDescription() {
        final var product = buildRandomProductWithPriceUSD();

        when(productRepository.findByNameOrDescription(product.description())).thenReturn(List.of(product));

        assertThat(findProductService.findByNameOrDescription(product.description()))
                .containsExactly(product);
    }

    @Test
    void shouldSucceedWhenTryingToFindOneProductByName() {
        final var product = buildRandomProductWithPriceUSD();

        when(productRepository.findByNameOrDescription(product.name())).thenReturn(List.of(product));

        assertThat(findProductService.findByNameOrDescription(product.name())).containsExactly(product);
    }

    @Test
    void shouldSucceedWhenTryingToFindSeveralProductsByDescription() {
        final var firstProduct = buildRandomProductWithPriceUSD();
        final var secondProduct = buildRandomProductWithPriceUSD();

        when(productRepository.findByNameOrDescription("fs")).thenReturn(List.of(firstProduct, secondProduct));

        assertThat(findProductService.findByNameOrDescription("fs"))
                .hasSize(2)
                .containsExactly(firstProduct, secondProduct);
    }
}