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

import javax.servlet.http.HttpSession;
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
    public String viewDriversView(String firstName,
                                  String lastName,
                                  String age,
                                  String experienceAge, Model model, HttpSession session)
    {

        User loggedUser = (User) session.getAttribute("loggedUser");
        Boolean driverIsRegistered = Boolean.FALSE;

        if (loggedUser != null && loggedUser.getDriver() != null ) {

            driverIsRegistered = Boolean.TRUE;

        }

        List<OroErrors> errorsList = new ArrayList<>();

        Boolean success = Boolean.FALSE;

        if (loggedUser != null)
        {
            model.addAttribute("loggedUser", loggedUser);
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

        if (StringUtils.isNullOrEmpty(age) || Integer.valueOf(age) < 18)
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
            driverIsRegistered =  Boolean.TRUE;

            driverService.saveDriver(newDrivre);
            userService.saveUSer(loggedUser);
            success = Boolean.TRUE;
        }

        model.addAttribute("driverIsRegistered", driverIsRegistered);
        model.addAttribute("success", success);

        return "drivers";
    }
}
