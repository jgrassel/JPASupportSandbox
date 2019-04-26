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
 * A servlet that serves as a vehicle for an EJB-driven test.
 * 
 */
@SuppressWarnings("serial")
@WebServlet(urlPatterns = "/SimpleTestServlet")
public class SimpleTestServlet extends HttpServlet {
    @EJB
    private TestEJBLocal testEjb;
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        final PrintWriter pw = response.getWriter();
        pw.println("<h1>JPA+EJB+Jar-Library Test Case</h1>");
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
