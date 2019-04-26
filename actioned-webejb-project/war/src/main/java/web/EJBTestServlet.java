package web;

import java.io.IOException;
import java.io.PrintWriter;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ejb.TestEJBLocal;

/**
 * 
 * The EJBTestServlet is a servlet that serves as a vehicle for an EJB-driven test.
 * 
 */
@SuppressWarnings("serial")
@WebServlet(urlPatterns = "/EJBTestServlet")
public class EJBTestServlet extends HttpServlet {
    @EJB
    private TestEJBLocal testEjb;
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        final PrintWriter pw = response.getWriter();
        pw.println("<h1>JPA+EJB Test Case</h1>");
        pw.println("<hr>");
        
        try {
            testEjb.executeTest(pw);
        } catch (Exception e) {
            e.printStackTrace();
            e.printStackTrace(pw);
            pw.println("<br>");
        }        
    }
}
