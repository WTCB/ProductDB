package be.pauwel.pi.product.services.impl;

import be.pauwel.pi.product.services.CompanyService;
import be.pauwel.pi.product.viewmodel.Company;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompanyServiceImpl implements CompanyService {

    @Override
    public List<Company> getAllCompanies() {

        Company c = new Company();
        c.setName("Gyproc");
        c.setLogo("http://www.bricoleurgyproc.be/sites/all/themes/gyproc/images/svg/logo.svg");
        c.setAddress("Saint-Gobain Construction Products Belgium NV\n" +
                "Sint-Jansweg 9\n" +
                "Haven 1602\n" +
                "9130 Kallo\n" +
                "Belgium");
        c.setPhone("+32 (0)3 360.22.11");
        c.setMail("info@gyproc.be");
        c.setWebsite("http://www.gyproc.be");

        Company c1 = new Company();
        c1.setName("Renson");
        c1.setLogo("http://www.renson.be/");
        c1.setAddress("RensonAddress");
        c1.setPhone("RensonPhone");
        c1.setMail("info@renson.be");
        c1.setWebsite("http://www.renson.be");

        return Company.companyList;
    }
}




