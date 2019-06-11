package be.pauwel.pi.product.services.impl;

import be.pauwel.pi.product.services.MenuService;
import be.pauwel.pi.product.viewmodel.ui.MenuItem;
import be.pauwel.pi.product.viewmodel.ui.SubItem;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MenuServiceImpl implements MenuService {

    @Override
    public List<MenuItem> getMenuItems() {

        MenuItem mi = new MenuItem();
        mi.setId(0);
        mi.setLabel("Geometrische Data");
        mi.subItems.add(new SubItem(0,"Dikte","range","cm",0, 100));
        mi.subItems.add(new SubItem(1,"Lengte","range","cm",0, 5));
        mi.subItems.add(new SubItem(2,"Breedte","range","cm",0, 300));

        MenuItem mi1 = new MenuItem();
        mi1.setId(1);
        mi1.setLabel("Thermische prestaties");
        mi1.subItems.add(new SubItem(10,"Warmtegeleidingscoëfficiënt","range","W/(m.K)",0, 0.2));
        mi1.subItems.add(new SubItem(11,"Thermische weerstand","range","m².K/W",0, 200));

        return MenuItem.menuItemList;
    }
}




