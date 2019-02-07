package be.pauwel.pi.product.services;

import be.pauwel.pi.product.viewmodel.Company;

import java.util.List;


public interface CompanyService {
    /**
     * Get all companies available
     *
     * @return {@link Company}
     */
    List<Company> getAllCompanies();

}