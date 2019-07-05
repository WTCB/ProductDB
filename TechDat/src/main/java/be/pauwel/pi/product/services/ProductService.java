package be.pauwel.pi.product.services;

import be.pauwel.pi.product.viewmodel.data.Product;
import be.pauwel.pi.product.viewmodel.data.Property;

import java.util.List;


public interface ProductService {
    /**
     * Get all products available, incl. properties
     *
     * @return {@link Product}
     */
    List<Product> getAllProductsWithProps();

    /**
     * Get all products available, excl. properties
     *
     * @return {@link Product}
     */
    List<Product> getAllProductsWithoutProps();

    /**
     * Get all products available with properties
     *
     * @return {@link Product}
     */
    List<Product> getAllProductsWithProperties(List<Property> properties);

    /**
     * Get all products available with ID
     *
     * @return {@link Product}
     */
    Product getProductWithId(String id);

}