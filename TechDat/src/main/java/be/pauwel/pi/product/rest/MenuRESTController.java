package be.pauwel.pi.product.rest;

import be.pauwel.pi.product.services.MenuService;
import be.pauwel.pi.product.viewmodel.ui.MenuItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE, value = "/TechDat/ws/menuItems")
public class MenuRESTController {

    @Autowired
    private MenuService menuService;

    @RequestMapping(method = RequestMethod.GET)
    List<MenuItem> getMenuItems() {
        return menuService.getMenuItems();
    }
}






