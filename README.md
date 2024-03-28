## LEXAILEC store

LEXAILEC Inc. has decided to open an online shopping store

This store will then be backed by an online shopping application that will provide the following
features:

- A user can browse through the products.
- A user can add/remove/update the products in the cart.
- A user can place an order.
- A user can modify the shipping address.

LEXAILEC store is implemented using the Hexagonal Architecture and the Domain-Driven Design principles.
Meaning some business principles will be enforced, like:

- The number of products added to a cart must be at least one
- Once a product is added to the cart, it's total quantity in the cart must not exceed the maximum number of the product
  still available in the warehouse

This implementation is freely inspired by this blog
post : [Hexagonal Architecture With Java](https://www.happycoders.eu/software-craftsmanship/hexagonal-architecture-java/)

## Requirements

- [Adoptium Temurin 21](https://adoptium.net/)
- [Gradle 8.6+](https://gradle.org/install/)