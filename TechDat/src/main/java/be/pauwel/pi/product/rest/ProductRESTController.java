package be.pauwel.pi.product.rest;

import be.pauwel.pi.product.services.ProductService;
import be.pauwel.pi.product.viewmodel.data.Product;
import be.pauwel.pi.product.viewmodel.data.Property;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class ProductRESTController {

    @Autowired
    private ProductService productService;

    @RequestMapping(method = GET, params = "propsIncluded", value = "/TechDat/ws/products")
    List<Product> getAllProducts(@RequestParam("propsIncluded") boolean withProps) {
        if(withProps)
            return productService.getAllProductsWithProps();
        else
            return productService.getAllProductsWithoutProps();

        //return productService.getAllProducts();
    }

    @RequestMapping(value = "/TechDat/ws/products", params = "id", method = GET)
    @ResponseBody
    Product getProductWithId(@RequestParam("id") String id) {
        return productService.getProductWithId(id);
    }

    @RequestMapping(value = "/TechDat/ws/products", method = RequestMethod.POST)
    public @ResponseBody List<Product> getAllProductsWithProperties(@RequestBody List<Property> jsonString) {
        return productService.getAllProductsWithProperties(jsonString);
    }
}






