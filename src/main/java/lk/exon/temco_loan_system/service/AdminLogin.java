/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB40/StatelessEjbClass.java to edit this template
 */
package lk.exon.temco_loan_system.service;

import com.google.gson.Gson;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.core.Context;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import lk.exon.temco.security.Security;
import lk.exon.temco_loan_system.common.ComPath;
import lk.exon.temco_loan_system.common.UniDBLocal;
import lk.exon.temco_loan_system.entity.GeneralUserProfile;
import lk.exon.temco_loan_system.entity.LoginSession;
import lk.exon.temco_loan_system.entity.UserLoginGroup;
import lk.exon.temco_loan_system.model.ResponseModel;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

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

    public void verifyLogin() throws Exception {
        try {
            if (username != null && password != null) {
                System.out.println("username " + username);
                if (password != null) {
                    System.out.println("username " + password);

                    String quary = "SELECT u FROM UserLoginGroup u WHERE u.userLoginId.username='" + username + "'";
                    System.out.println(quary);
                    List<UserLoginGroup> ul_list = uni.searchByQuery(quary);
                    if (ul_list.size() > 0) {
                        UserLoginGroup ulg = ul_list.get(0);
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
                                ls.setGeneralOrganizationProfile(ulg.getGeneralOrganizationProfileId());
                                ls.setUserLogin(ulg.getUserLoginId());
                                ls.setUserLoginGroupId(ulg);
                                ls.setStartTime(new Date());
                                ls.setIp(ip);
                                uni.create(ls);

                                FacesContext facesContext = FacesContext.getCurrentInstance();
                                ExternalContext externalContext = facesContext.getExternalContext();
                                externalContext.redirect(externalContext.getRequestContextPath() + "/view/dashboard.xhtml");
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
