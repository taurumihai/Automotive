package tauru.springframework.WebApp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import tauru.springframework.WebApp.entities.Driver;
import tauru.springframework.WebApp.entities.User;
import tauru.springframework.WebApp.services.DriverService;
import tauru.springframework.WebApp.services.UserService;
import tauru.springframework.WebApp.utilitare.OroErrors;
import tauru.springframework.WebApp.utilitare.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Controller
public class AutomotiveController {


    @Autowired
    private UserService userService;

    @Autowired
    private DriverService driverService;


    @RequestMapping("/automotive")
    public String viewAutomotive() {

        return "automotive";
    }

    @RequestMapping("/drivers")
    public String viewDriversView(String username,
                                  String firstName,
                                  String lastName,
                                  String age,
                                  String experienceAge, Model model)
    {

        List<OroErrors> errorsList = new ArrayList<>();
        if (StringUtils.isNullOrEmpty(username) && StringUtils.isNullOrEmpty(firstName)
            && StringUtils.isNullOrEmpty(lastName) && StringUtils.isNullOrEmpty(age) && StringUtils.isNullOrEmpty(experienceAge))
        {
            return "drivers";
        }

        User loggedUser = userService.findUserByUserName(username);
        Boolean success = Boolean.FALSE;

        if (loggedUser != null)
        {
            model.addAttribute("loggedUser", loggedUser);
        }

        if (StringUtils.isNullOrEmpty(username))
        {
            errorsList.add(new OroErrors("Completati numele de utilizator"));
        }
        if (StringUtils.isNullOrEmpty(firstName))
        {
            errorsList.add(new OroErrors("Completati numele dvs."));
        }
        if (StringUtils.isNullOrEmpty(lastName))
        {
            errorsList.add(new OroErrors("Completati prenumele"));
        }
        if (StringUtils.isNullOrEmpty(age))
        {
            errorsList.add(new OroErrors("Completati varsta dvs"));
        }

        if (Integer.valueOf(age) < 18)
        {
            errorsList.add(new OroErrors("Varsta minima este de 18 ani"));
        }
        if (StringUtils.isNullOrEmpty(experienceAge))
        {
            errorsList.add(new OroErrors("Completati numarul anilor de experienta"));
        }

        model.addAttribute("errorList", errorsList);

        if (errorsList.isEmpty())
        {
            loggedUser.setFirstName(firstName);
            loggedUser.setLastName(lastName);

            Driver newDrivre = new Driver();
            newDrivre.setAge(Integer.valueOf(age));
            newDrivre.setExperienceYears(Integer.valueOf(experienceAge));
            newDrivre.setIsRegistered(Boolean.TRUE);

            loggedUser.setDriver(newDrivre);

            driverService.saveDriver(newDrivre);
            userService.saveUSer(loggedUser);
            success = Boolean.TRUE;
            model.addAttribute("driver", newDrivre);
        }

        model.addAttribute("success", success);

        return "drivers";
    }
}
