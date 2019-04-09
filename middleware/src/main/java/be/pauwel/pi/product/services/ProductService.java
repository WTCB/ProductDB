package be.pauwel.pi.product.services;

import be.pauwel.pi.product.viewmodel.data.Product;

import java.util.List;


public interface ProductService {
    /**
     * Get all companies available
     *
     * @return {@link Product}
     */
    List<Product> getAllProducts();

}