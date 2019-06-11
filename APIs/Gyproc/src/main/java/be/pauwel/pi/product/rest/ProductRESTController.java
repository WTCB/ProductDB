package be.pauwel.pi.product.rest;

import be.pauwel.pi.product.services.ProductService;
import be.pauwel.pi.product.viewmodel.data.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE, value = "/Gyproc/ws/products")
public class ProductRESTController {

    @Autowired
    private ProductService productService;

    @RequestMapping(method = RequestMethod.GET)
    List<Product> getAllProducts() {
        return productService.getAllProducts();
    }
}






