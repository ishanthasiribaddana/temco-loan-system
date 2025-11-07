/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import Service.OrgAccessService;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

/**
 *
 * @author USER
 */
@Path("/java-institute")
@Stateless
public class OrgAccessController {

    @Inject
    OrgAccessService orgAccessService;

    @POST
    @Path("/send-offer-email")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public jakarta.ws.rs.core.Response sendLoanOfferEmailTo(
            @FormParam("nic") String nic,
            @FormParam("gupid") String gupId,
            @FormParam("firstName") String firstName,
            @FormParam("lastName") String lastName,
            @FormParam("email") String email,
            @FormParam("mobileNo") String mobileNo,
            @FormParam("scholarship") String scholarship,
            @FormParam("totalDue") String totalDue,
            @FormParam("verificationToken") String verificationToken,
            @FormParam("intakeId") String intakeId,
            @FormParam("branchName") String branchName,
            @FormParam("intakeName") String intakeName,
            @FormParam("address1") String address1,
            @FormParam("address2") String address2,
            @FormParam("address3") String address3,
            @FormParam("profileCreatedDate") String profileCreatedDate) {
        System.out.println("request gotted");
        return orgAccessService.sendLoanOfferEmailTo(
                nic,
                gupId,
                firstName,
                lastName,
                email,
                mobileNo,
                scholarship,
                totalDue,
                verificationToken,
                branchName,
                intakeId,
                intakeName,
                address1,
                address2,
                address3,
                profileCreatedDate);
    }

    @POST
    @Path("/transfer-up-due-student")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public jakarta.ws.rs.core.Response sendUniversityFeeDueStudentDetails(
            @FormParam("nic") String nic,
            @FormParam("gupid") String gupId,
            @FormParam("firstName") String firstName,
            @FormParam("lastName") String lastName,
            @FormParam("email") String email,
            @FormParam("mobileNo") String mobileNo,
            @FormParam("scholarship") String scholarship,
            @FormParam("totalDue") String totalDue,
            @FormParam("verificationToken") String verificationToken,
            @FormParam("intakeId") String intakeId,
            @FormParam("branchName") String branchName,
            @FormParam("intakeName") String intakeName,
            @FormParam("address1") String address1,
            @FormParam("address2") String address2,
            @FormParam("address3") String address3,
            @FormParam("profileCreatedDate") String profileCreatedDate) {
        System.out.println("request gotted");
        return orgAccessService.sendLoanOfferEmailTo(
                nic,
                gupId,
                firstName,
                lastName,
                email,
                mobileNo,
                scholarship,
                totalDue,
                verificationToken,
                branchName,
                intakeId,
                intakeName,
                address1,
                address2,
                address3,
                profileCreatedDate);
    }

}
