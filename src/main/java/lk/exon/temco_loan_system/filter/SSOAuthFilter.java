package lk.exon.temco_loan_system.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.naming.InitialContext;
import javax.sql.DataSource;

/**
 * SSO Authentication Filter for Super Admin
 * 
 * This filter validates the shared auth_token cookie against the com_session_token table
 * and allows Super Admin users to access protected resources without re-authentication.
 * 
 * Cookie Details:
 * - Name: auth_token
 * - Domain: .temcobank.com (shared across all subdomains)
 * - Set by: temco-api on login
 * 
 * Usage:
 * 1. Copy this filter to any JSF application
 * 2. Configure the JNDI datasource name
 * 3. Add @WebFilter annotation with URL patterns to protect
 */
@WebFilter(filterName = "SSOAuthFilter", urlPatterns = {"/admin/*"})
public class SSOAuthFilter implements Filter {

    private static final String AUTH_COOKIE_NAME = "auth_token";
    private static final int SUPER_ADMIN_ROLE_ID = 10;
    private static final String JNDI_DATASOURCE = "java:app/jdbc/temco_system";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("SSO Auth Filter initialized");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpSession session = httpRequest.getSession(false);

        // Check if already authenticated
        if (session != null && session.getAttribute("adminUser") != null) {
            chain.doFilter(request, response);
            return;
        }

        // Try SSO authentication via cookie
        String token = getAuthTokenFromCookie(httpRequest);
        if (token != null) {
            SSOUser ssoUser = validateSSOToken(token);
            if (ssoUser != null && ssoUser.isSuperAdmin()) {
                // Create session for authenticated Super Admin
                session = httpRequest.getSession(true);
                session.setAttribute("adminUser", ssoUser);
                session.setAttribute("ssoAuthenticated", true);
                System.out.println("SSO: Super Admin authenticated - " + ssoUser.getUsername());
                chain.doFilter(request, response);
                return;
            }
        }

        // Not authenticated - redirect to login
        String loginPage = httpRequest.getContextPath() + "/admin/login.xhtml";
        httpResponse.sendRedirect(loginPage);
    }

    private String getAuthTokenFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (AUTH_COOKIE_NAME.equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    private SSOUser validateSSOToken(String token) {
        String sql = "SELECT ul.id, ul.username, ul.user_role_id, ur.name as role_name " +
                     "FROM com_session_token cst " +
                     "JOIN user_login ul ON cst.user_login_id = ul.id " +
                     "JOIN user_role ur ON ul.user_role_id = ur.id " +
                     "WHERE cst.token_hash = ? " +
                     "AND cst.is_active = 1 " +
                     "AND cst.expires_at > NOW()";

        try {
            InitialContext ctx = new InitialContext();
            DataSource ds = (DataSource) ctx.lookup(JNDI_DATASOURCE);
            
            try (Connection conn = ds.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {
                
                stmt.setString(1, token);
                ResultSet rs = stmt.executeQuery();
                
                if (rs.next()) {
                    SSOUser user = new SSOUser();
                    user.setId(rs.getInt("id"));
                    user.setUsername(rs.getString("username"));
                    user.setRoleId(rs.getInt("user_role_id"));
                    user.setRoleName(rs.getString("role_name"));
                    return user;
                }
            }
        } catch (Exception e) {
            System.err.println("SSO Token validation error: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void destroy() {
        System.out.println("SSO Auth Filter destroyed");
    }

    /**
     * Simple POJO to hold SSO user information
     */
    public static class SSOUser implements java.io.Serializable {
        private static final long serialVersionUID = 1L;
        
        private int id;
        private String username;
        private int roleId;
        private String roleName;

        public int getId() { return id; }
        public void setId(int id) { this.id = id; }

        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }

        public int getRoleId() { return roleId; }
        public void setRoleId(int roleId) { this.roleId = roleId; }

        public String getRoleName() { return roleName; }
        public void setRoleName(String roleName) { this.roleName = roleName; }

        public boolean isSuperAdmin() {
            return roleId == SUPER_ADMIN_ROLE_ID || 
                   "Super Admin".equalsIgnoreCase(roleName);
        }
    }
}
