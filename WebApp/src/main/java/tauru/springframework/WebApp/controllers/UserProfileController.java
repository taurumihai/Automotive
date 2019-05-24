package tauru.springframework.WebApp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import tauru.springframework.WebApp.entities.Address;
import tauru.springframework.WebApp.entities.User;
import tauru.springframework.WebApp.repositories.AddressRepository;
import tauru.springframework.WebApp.services.AddressService;
import tauru.springframework.WebApp.services.UserService;
import tauru.springframework.WebApp.utilitare.OroErrors;
import tauru.springframework.WebApp.utilitare.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Controller
public class UserProfileController {

    @Autowired
    private UserService userService;

    @Autowired
    private AddressService addressService;

    @RequestMapping("/profile")
    public String viewProfile() {

        return "profile";
    }

    @RequestMapping("/changePassword")
    public String changePasswordView(String username, String password, String newPassword, Model model) {

        User currentUser = null;
        Boolean success;
        List<OroErrors> errorsList = new ArrayList<>();

        if (username != null && password != null) {

            currentUser  = userService.findUSerByUSernameAndPassword(username, password);
        }

        if (currentUser != null && newPassword != null) {

            if (newPassword.equals(password)) {

                errorsList.add(new OroErrors("Noua parola nu poate coincide cu cea veche"));

            } else {

                success = Boolean.TRUE;
                currentUser.setPassword(newPassword);
                model.addAttribute("passwordChanged", success);
                userService.saveUSer(currentUser);
            }
        }
        model.addAttribute("errorList", errorsList);

        return "changePassword";
    }

    @RequestMapping("/userAddresses")
    public String viewAddressForm(String county, String street, String streetNumber, String zipCode, Boolean deliveryAddress, Boolean billingAddress, User username, Model model) {

        User loggedUser = userService.findUserByUserName("Tauru Mihai");

        if (loggedUser != null) {

            model.addAttribute("loggedUser", loggedUser);
        }

        List<OroErrors> errors = new ArrayList<>();

        if (StringUtils.isNullOrEmpty(county)) {

            String error = "Completati campul Judet";
            errors.add(new OroErrors(error));

        }

        if (StringUtils.isNullOrEmpty(street)) {

            String error = "Completati campul Strada";
            errors.add(new OroErrors(error));

        }

        if (StringUtils.isNullOrEmpty(streetNumber)) {

            String error = "Completati campul Numar Strada";
            errors.add(new OroErrors(error));

        }

        if (StringUtils.isNullOrEmpty(zipCode)) {

            String error = "Completati campul Cod Postal";
            errors.add(new OroErrors(error));

        }

        if ((deliveryAddress == null && billingAddress == null )) {

            String error = "Va rugam selecati daca doriti ca adresa introdusa sa fie de livrare/ facturare sau ambele";
            errors.add(new OroErrors(error));
        }

        model.addAttribute("errorList", errors);
        if (errors.isEmpty() && !loggedUser.getHasChangedAddressInfos()) {

            if (loggedUser.getAddressList() != null && loggedUser.getAddressList().isEmpty()) {

                loggedUser.setHasChangedAddressInfos(Boolean.TRUE);
                Address userAddress = new Address();

                userAddress.setCounty(county);
                userAddress.setStreetNumber(streetNumber);
                userAddress.setStreet(street);
                userAddress.setZipCode(zipCode);
                userAddress.setUser(loggedUser);
                userAddress.setDeliveryAddress(deliveryAddress);
                userAddress.setBillingAddress(billingAddress);

                loggedUser.getAddressList().add(userAddress);
                addressService.saveAddress(userAddress);
                userService.saveUSer(loggedUser);
            }

        } else if (errors.isEmpty() && loggedUser.getHasChangedAddressInfos()) {

            if (!StringUtils.isNullOrEmpty(county)  && !StringUtils.isNullOrEmpty(street) && !StringUtils.isNullOrEmpty(streetNumber)
                 && !StringUtils.isNullOrEmpty(zipCode))
            {
                Address userAddress = new Address();

                userAddress.setCounty(county);
                userAddress.setStreetNumber(streetNumber);
                userAddress.setStreet(street);
                userAddress.setZipCode(zipCode);
                userAddress.setUser(loggedUser);
                userAddress.setDeliveryAddress(deliveryAddress);
                userAddress.setBillingAddress(billingAddress);

                loggedUser.getAddressList().add(userAddress);

                addressService.saveAddress(userAddress);
                userService.saveUSer(loggedUser);
            }
        }

        if (loggedUser.getAddressList() != null && !loggedUser.getAddressList().isEmpty()) {

            model.addAttribute("addressesList", loggedUser.getAddressList());
        }

        return "userAddresses";
    }
}
