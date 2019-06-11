package be.pauwel.pi.product.rest;

import be.pauwel.pi.product.services.CompanyService;
import be.pauwel.pi.product.viewmodel.data.Company;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE, value = "/TechDat/ws/companies")
public class CompanyRESTController {

    @Autowired
    private CompanyService companyService;

    @RequestMapping(method = RequestMethod.GET)
    List<Company> getAllCompanies() {
        return companyService.getAllCompanies();
    }
}






