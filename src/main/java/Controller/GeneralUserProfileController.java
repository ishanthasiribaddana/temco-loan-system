package Controller;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import lk.exon.temco_loan_system.common.UniDBLocal;
import lk.exon.temco_loan_system.entity.GeneralUserProfile;
import org.json.JSONObject;

@Path("/v1/general-user-profile")
@Stateless
@Produces(MediaType.APPLICATION_JSON)
public class GeneralUserProfileController {

    @EJB
    private UniDBLocal uniDB;

    @GET
    @Path("/nic/{nic}")
    public Response getByNic(@PathParam("nic") String nic) {
        if (nic == null || nic.trim().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new JSONObject().put("message", "NIC is required").toString())
                    .build();
        }

        String nicNormalized = normalizeNic(nic);
        String nicEscaped = escapeSqlLiteral(nicNormalized);

        List results = uniDB.searchByQuery(
                "SELECT g FROM GeneralUserProfile g WHERE UPPER(g.nic)='" + nicEscaped + "'"
        );

        if (results == null || results.isEmpty() || !(results.get(0) instanceof GeneralUserProfile)) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(new JSONObject().put("message", "Profile not found").toString())
                    .build();
        }

        GeneralUserProfile g = (GeneralUserProfile) results.get(0);
        return Response.ok(toJson(g).toString()).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(String body) {
        if (body == null || body.trim().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new JSONObject().put("message", "Request body is required").toString())
                    .build();
        }

        JSONObject json;
        try {
            json = new JSONObject(body);
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new JSONObject().put("message", "Invalid JSON body").toString())
                    .build();
        }

        String nic = json.optString("nic", "");
        if (nic == null || nic.trim().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new JSONObject().put("message", "nic is required").toString())
                    .build();
        }

        String nicNormalized = normalizeNic(nic);
        String nicEscaped = escapeSqlLiteral(nicNormalized);

        List existing = uniDB.searchByQuery(
                "SELECT g FROM GeneralUserProfile g WHERE UPPER(g.nic)='" + nicEscaped + "'"
        );

        if (existing != null && !existing.isEmpty()) {
            return Response.status(Response.Status.CONFLICT)
                    .entity(new JSONObject().put("message", "Profile already exists for NIC").toString())
                    .build();
        }

        GeneralUserProfile gup = new GeneralUserProfile();
        gup.setNic(nicNormalized);
        gup.setFirstName(nullIfBlank(json.optString("firstName", null)));
        gup.setLastName(nullIfBlank(json.optString("lastName", null)));

        String fullName = null;
        if (gup.getFirstName() != null || gup.getLastName() != null) {
            String fn = gup.getFirstName() == null ? "" : gup.getFirstName().trim();
            String ln = gup.getLastName() == null ? "" : gup.getLastName().trim();
            String combined = (fn + " " + ln).trim();
            if (!combined.isEmpty()) {
                fullName = combined;
            }
        }
        if (fullName == null) {
            fullName = nullIfBlank(json.optString("fullName", null));
        }
        gup.setFullName(fullName);

        gup.setEmail(nullIfBlank(json.optString("email", null)));
        gup.setMobileNo(nullIfBlank(json.optString("mobileNo", null)));

        gup.setProfileCreatedDate(new Date());
        gup.setVerificationToken(UUID.randomUUID().toString());
        gup.setIsActive((short) 1);

        try {
            uniDB.create(gup);
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new JSONObject().put("message", "Failed to create profile").put("error", e.getMessage()).toString())
                    .build();
        }

        return Response.status(Response.Status.CREATED)
                .entity(toJson(gup).toString())
                .build();
    }

    private static JSONObject toJson(GeneralUserProfile g) {
        JSONObject o = new JSONObject();
        o.put("id", g.getId());
        o.put("nic", g.getNic());
        o.put("firstName", g.getFirstName());
        o.put("lastName", g.getLastName());
        o.put("fullName", g.getFullName());
        o.put("email", g.getEmail());
        o.put("mobileNo", g.getMobileNo());
        return o;
    }

    private static String normalizeNic(String nic) {
        return nic.trim().toUpperCase();
    }

    private static String escapeSqlLiteral(String v) {
        // UniDB only supports string JPQL, so we must escape single-quotes.
        return v.replace("'", "''");
    }

    private static String nullIfBlank(String v) {
        if (v == null) {
            return null;
        }
        String t = v.trim();
        return t.isEmpty() ? null : t;
    }
}
