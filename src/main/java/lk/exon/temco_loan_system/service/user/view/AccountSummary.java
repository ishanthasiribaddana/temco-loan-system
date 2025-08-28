/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB40/StatelessEjbClass.java to edit this template
 */
package lk.exon.temco_loan_system.service.user.view;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.LocalBean;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;
import java.io.Serializable;

/**
 *
 * @author USER
 */
@SessionScoped
@LocalBean
@Named
public class AccountSummary implements Serializable {

    @PostConstruct
    public void init() {

    }
}
