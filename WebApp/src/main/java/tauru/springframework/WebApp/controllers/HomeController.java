package tauru.springframework.WebApp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import tauru.springframework.WebApp.entities.User;
import tauru.springframework.WebApp.services.UserService;
import tauru.springframework.WebApp.utilitare.OroErrors;
import tauru.springframework.WebApp.utilitare.StringUtils;


import java.util.ArrayList;
import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private UserService userService;

    @RequestMapping("/")
    public String viewHome(Model model){


        return "index";
    }

    @RequestMapping("/login")
    public String view (String username, String password, Model model) {

        List<OroErrors> oroErrorsList = new ArrayList<>();

        if (StringUtils.isNullOrEmpty(username) && StringUtils.isNullOrEmpty(password)) {

            return "login";
        }

        User loggedUser = userService.findUSerByUSernameAndPassword(username, password);

        if (loggedUser != null) {

            loggedUser.setUserIsLoggedIn(Boolean.TRUE);
            userService.saveUSer(loggedUser);
            return "welcome";

        } else {

            oroErrorsList.add(new OroErrors("Nu exista contul"));
            model.addAttribute("errorList", oroErrorsList);
            return "login";

        }
    }

    @RequestMapping("/register")
    public String viewRegister(String username, String password, String email, Model model){

        if (username == null || password == null || email == null) {

            return "register";
        }

        List<User> userList = userService.findAllUsers();
        List<String> errorList = new ArrayList<String>();

        for (User user : userList) {

            if (user.getUsername().equals(username)) {

                String error = "Please chose another username";
                errorList.add(error);

            }

            if (user.getEmail().equals(email)) {

                String error = "Please chose another email";
                errorList.add(error);

            }
        }

        if (StringUtils.isNullOrEmpty(password)) {

            String error = "Completati parola";
            errorList.add(error);
        }

        model.addAttribute("errorList", errorList);

        if (errorList.isEmpty()) {

            User user = new User(username, password, email);
            user.setUserIsLoggedIn(Boolean.TRUE);
            userService.saveUSer(user);
            return "welcome";
        }

        return "register";
    }
}
