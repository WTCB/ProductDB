package be.pauwel.pi.product.services;

import be.pauwel.pi.product.viewmodel.ui.MenuItem;

import java.util.List;


public interface MenuService {
    /**
     * Get all companies available
     *
     * @return {@link MenuItem}
     */
    List<MenuItem> getMenuItems();

}
