/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB40/StatelessEjbClass.java to edit this template
 */
package lk.exon.temco_loan_system.service;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.core.Context;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import lk.exon.temco.security.Security;
import lk.exon.temco_loan_system.common.UniDBLocal;
import lk.exon.temco_loan_system.entity.LoginSession;
import lk.exon.temco_loan_system.entity.UserLoginGroup;

/**
 *
 * @author USER
 */
@Named
@RequestScoped
public class AdminLogin implements Serializable {

    private String username;
    private String password;

    @Inject
    private UniDBLocal uni;

    @Context
    HttpServletRequest httpRequest;

    @PostConstruct
    public void init() {
        httpRequest = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
    }

    private static final String AUTH_COOKIE_NAME = "auth_token";
    private static final int SUPER_ADMIN_ROLE_ID = 10;
    private static final String SUPER_ADMIN_ROLE_NAME = "Super Admin";

    public void checkSession() throws IOException {
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        Object adminUser = externalContext.getSessionMap().get("adminUser");
        
        // If no session, check for SSO cookie
        if (adminUser == null) {
            UserLoginGroup ssoUser = validateSSOCookie();
            if (ssoUser != null && isSuperAdmin(ssoUser)) {
                // SSO cookie valid AND user is Super Admin - create session
                externalContext.getSessionMap().put("adminUser", ssoUser);
                System.out.println("SSO: Super Admin authenticated via cookie - " + 
                        ssoUser.getUserLoginId().getUsername());
                return; // User authenticated via SSO
            }
            
            externalContext.redirect(externalContext.getRequestContextPath() + "/admin/login.xhtml");
            FacesContext.getCurrentInstance().responseComplete();
        }
    }

    private boolean isSuperAdmin(UserLoginGroup user) {
        if (user == null || user.getUserRoleId() == null) {
            return false;
        }
        Integer roleId = user.getUserRoleId().getId();
        String roleName = user.getUserRoleId().getName();
        
        // Check by role ID or role name
        return (roleId != null && roleId.equals(SUPER_ADMIN_ROLE_ID)) ||
               (roleName != null && roleName.equalsIgnoreCase(SUPER_ADMIN_ROLE_NAME));
    }

    private UserLoginGroup validateSSOCookie() {
        try {
            HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance()
                    .getExternalContext().getRequest();
            Cookie[] cookies = request.getCookies();
            
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if (AUTH_COOKIE_NAME.equals(cookie.getName())) {
                        String token = cookie.getValue();
                        if (token != null && !token.isEmpty()) {
                            // Validate token against com_session_token table using native query
                            String nativeQuery = "SELECT user_login_id FROM com_session_token " +
                                    "WHERE token_hash = '" + token + "' " +
                                    "AND is_active = 1 " +
                                    "AND expires_at > NOW()";
                            List<?> result = uni.searchByNativeQuery(nativeQuery);
                            
                            if (!result.isEmpty()) {
                                Object userLoginId = result.get(0);
                                // Fetch UserLoginGroup with role info
                                String jpqlQuery = "SELECT u FROM UserLoginGroup u " +
                                        "JOIN FETCH u.userRoleId " +
                                        "WHERE u.userLoginId.id = " + userLoginId;
                                List<UserLoginGroup> users = uni.searchByQuery(jpqlQuery);
                                if (!users.isEmpty()) {
                                    return users.get(0);
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("SSO Cookie validation error: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public void verifyLogin() throws Exception {
        try {
            if (username != null && password != null) {
                System.out.println("username " + username);
                if (password != null) {
//                    System.out.println("username " + password);
//                    String encryptpassword = Security.encrypt(password);
//                    System.out.println("encryptpassword " + encryptpassword);
                    String quary = "SELECT u FROM UserLoginGroup u WHERE u.userLoginId.username='" + username + "'";
                    System.out.println(quary);
                    List<UserLoginGroup> ul_list = uni.searchByQuery(quary);
                    if (ul_list.size() > 0) {
                        UserLoginGroup ulg = ul_list.get(0);
                        System.out.println("password :" + ulg.getUserLoginId().getPassword());
                        String decryptPassword = Security.decrypt(ulg.getUserLoginId().getPassword());

                        System.out.println(password + " : " + decryptPassword);
                        if (password.equals(decryptPassword)) {
                            if (ulg.getIsActive().equals((byte) 0)) {
                                FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Your account has suspended 1");
                                FacesContext.getCurrentInstance().addMessage("", msg);

                            } else {

                                String ip = httpRequest.getHeader("CF-Connecting-IP");
                                if (ip == null) {
                                    ip = httpRequest.getRemoteAddr();
                                }

                                LoginSession ls = new LoginSession();
                                ls.setUserLogin(ulg.getUserLoginId());
                                ls.setUserLoginGroupId(ulg);
                                ls.setStartTime(new Date());
                                ls.setIp(ip);
                                uni.create(ls);

                                FacesContext facesContext = FacesContext.getCurrentInstance();
                                ExternalContext externalContext = facesContext.getExternalContext();
                                externalContext.getSessionMap().put("adminUser", ulg);
                                externalContext.redirect(externalContext.getRequestContextPath() + "/admin/view/thirasara-loan.xhtml");
                                facesContext.responseComplete();

                            }
                        } else {
                            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Invalid Username or Password !");
                            FacesContext.getCurrentInstance().addMessage("", msg);
                        }
                    } else {
                        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Invalid Username or Password !");
                        FacesContext.getCurrentInstance().addMessage("", msg);
                    }
                } else {
                    FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Password Can not be empty");
                    FacesContext.getCurrentInstance().addMessage("", msg);
                }
            } else {
                FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Username or Password Can not be empty");
                FacesContext.getCurrentInstance().addMessage("", msg);
            }
        } catch (Exception e) {
            e.printStackTrace();
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", e.getMessage());
            FacesContext.getCurrentInstance().addMessage("", msg);
        }
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
