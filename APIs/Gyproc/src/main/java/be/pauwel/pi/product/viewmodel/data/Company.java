package be.pauwel.pi.product.viewmodel.data;

import be.pauwel.pi.product.viewmodel.servers.Server;

import java.util.ArrayList;

public class Company {

    public static ArrayList<Company> companyList = new ArrayList<Company>();

    private String name = "";
    private String logo = "";
    private String address = "";
    private String phone = "";
    private String mail = "";
    private String website = "";
    private Server server;

    public Company() {
        companyList.add(this);
    }

    public static void initiate() {
        Company.companyList.clear();

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
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public Server getServer() {
        return server;
    }

    public void setServer(Server server) {
        this.server = server;
    }

    public static Company getCompany(String name){
        for(Company c : companyList){
            if(c.name.equalsIgnoreCase(name))
                return c;
        }
        return null;
    }

}
