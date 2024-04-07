package com.github.kagokla.store.model.product;

import com.github.kagokla.store.model.BaseEntity;
import com.github.kagokla.store.model.utils.IdGeneratorUtils;
import com.github.kagokla.store.model.utils.ValidatorUtils;
import lombok.Getter;
import lombok.Setter;
import org.javamoney.moneta.Money;

@Getter
public class Product extends BaseEntity {

    private String name;
    @Setter
    private String description;
    private Money price;
    private int stock;

    public Product(final String name, final String description, final Money price, final int stock) {
        super(IdGeneratorUtils.generateRandomProductId());

        setName(name);
        this.description = description;
        setPrice(price);
        setStock(stock);
    }

    public void setName(final String name) {
        ValidatorUtils.requireNonBlank(name, "name");
        this.name = name;
    }

    public void setPrice(final Money price) {
        ValidatorUtils.requireNonNull(price, "price");
        this.price = price;
    }

    public void setStock(final int stock) {
        ValidatorUtils.requireNonNegative(stock, "stock");
        this.stock = stock;
    }

    public String toString() {
        return "Product(Id=" + id + ", name=" + name + ", price=" + price + ")";
    }
}