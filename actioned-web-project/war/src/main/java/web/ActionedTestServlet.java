/*******************************************************************************
 * Copyright (c) 2019 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package web;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.UserTransaction;

/**
 * 
 * The ActionedTestServlet is intended for complicated scenarios where it is desirable to 
 * step-by-step demonstrate a problem with navigable steps (actions) that can be selected
 * by browser interaction.
 * 
 * For simple, straightforward issues, the SimpleTestServlet is recommended.
 *
 */
@SuppressWarnings("serial")
@WebServlet(urlPatterns = "/ActionedTestServlet")
public class ActionedTestServlet extends HttpServlet {
    private final static String sname = "WebAccess: ";

    private final static Class<?> sCls = ActionedTestServlet.class;
    private final static Set<String> actionSet = new HashSet<String>();
    static {
        // Actions are listed at the top of the generated web page.  Actions can be added, modified,
        // renamed, or removed.  The names of all actions must be added to the actionSet Set,
        // and there must exist methods on this servlet class with a method name equal to the
        // name of the action added to actionSet; these methods must take HttpServletRequest and
        // HttpServletResponse (in that order) as arguments, and throw ServletException and IOException.
        //
        // The base example defines "populate" and "query" as two actions.
        actionSet.add("populate");
        actionSet.add("query");
    }

    @PersistenceContext(unitName = "TestPU")
    private EntityManager em;

    @Resource
    private UserTransaction tx;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        final int maxActionsPerRow = 5;
        
        final PrintWriter pw = response.getWriter();
        pw.println("<h1>JPA Test Case</h1>");
        try {
            // If the file "TestExplanation" exists in the war module, open it and copy its contents
            // to the response output.
            URL testExplanHtmlURL = sCls.getClassLoader().getResource("TestExplanation.html");
            if (testExplanHtmlURL != null) {
                try (InputStream is = testExplanHtmlURL.openStream()) {
                    pw.println("<hr>");
                    BufferedReader br = new BufferedReader(new InputStreamReader(is));
                    String text = null;
                    while ((text = br.readLine()) != null) {
                        pw.println(text);
                    }
                }
            }
        } catch (Throwable t) {
            // Swallow
        }

        pw.println("<hr>");
        pw.println("<H3>Actions (Choose One):</H3>");
        pw.println("<table width=\"100%\">");
        pw.println("<tr>");
        int rowCount = 0;
        for (String a : actionSet) {
            pw.println("<td><b><A HREF=\"?action=" + a + "\">" + a + "</a></b></td>");
            if (++rowCount >= maxActionsPerRow) {
                pw.println("</tr>");
                rowCount = 0;
            }
        }
        if (rowCount > 0) {
            pw.println("<td colspan=" + (maxActionsPerRow - rowCount) + "></td>");
        }
        pw.println("</tr>");
        pw.println("</table>");
        pw.println("<hr>");
        String action = null;
        try {
            action = request.getParameter("action");
            if (actionSet.contains(action)) {
                long startTime = System.nanoTime();
                try {
                    Method m = sCls.getMethod(action, HttpServletRequest.class, HttpServletResponse.class);
                    System.out.println(sname + "Executing action: " + action + " ...");
                    pw.println("<br>Executing action: " + action + " ...<br>");
                    m.invoke(this, request, response);
                } finally {
                    long endTime = System.nanoTime();
                    pw.println("<hr>");
                    pw.println((new java.util.Date()) + ", operation executed in " + (endTime - startTime) + " ns");
                }
                
            }
        } catch (Exception e) {
            e.printStackTrace();
            e.printStackTrace(pw);
            pw.println("<br>");
        } finally {
            System.out.println(sname + "Action " + action + " is done.");
        }
    }

    /*
     * Customize with what you need to do to populate the database to demonstrate
     * the issue.
     */
    @SuppressWarnings("unused")
    public void populate(HttpServletRequest request, HttpServletResponse response) throws Exception {
        // The code that performs the create operations have been moved to CommonTestCode in order
        // to share it with SimpleTestServlet.  You can continue with that pattern, or place all of
        // your action's logic here.
        CommonTestCode.populate(request, response, em, tx);
    }

    /*
     * One sample action to demonstrate a problem.
     */
    @SuppressWarnings("unused")
    public void query(HttpServletRequest request, HttpServletResponse response) throws Exception {
        // The code that performs the create operations have been moved to CommonTestCode in order
        // to share it with SimpleTestServlet.  You can continue with that pattern, or place all of
        // your action's logic here.
        CommonTestCode.query(request, response, em, tx);
    }
}
