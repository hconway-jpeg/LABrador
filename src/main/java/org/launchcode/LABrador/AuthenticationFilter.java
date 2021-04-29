package org.launchcode.LABrador;

import org.launchcode.LABrador.controllers.AuthenticationController;
import org.launchcode.LABrador.data.UserRepository;
import org.launchcode.LABrador.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.List;

public class AuthenticationFilter extends HandlerInterceptorAdapter {

    @Autowired
    UserRepository userRepository;

    @Autowired
    AuthenticationController authenticationController;

    private static final List<String> whitelist = Arrays.asList("/login", "/register", "/logout", "/css");

    //checking if this page is whitelisted
    private static boolean isWhitelisted(String route){
        for(String routeRoot : whitelist){
            if(route.startsWith(routeRoot)){
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        //whitelisted pages don't require a login
        if(isWhitelisted(request.getRequestURI())){
            return true;
        }

        HttpSession session = request.getSession();
        User user = authenticationController.getUserFromSession(session);

        // the user is logged in!
        if(user != null){
            return true;
        }

        // the user isn't logged in
        response.sendRedirect("/login");
        return false;
    }
}
