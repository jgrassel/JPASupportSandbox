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

package ejb;

import java.io.PrintWriter;

import javax.annotation.Resource;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;

@Stateless
@Local(TestEJBLocal.class)
@TransactionManagement(javax.ejb.TransactionManagementType.BEAN)
public class TestEJB {
    @PersistenceContext(unitName = "TestPU")
    private EntityManager em;

    @Resource
    private UserTransaction tx;
    
    public void executeTest(PrintWriter pw) throws Exception {
        // The code that performs the create and query operations resides on CommonTestCode.  
        // You can continue with that pattern, or place all of your test's logic here.
        CommonTestCode.populate(pw, em, tx);
        CommonTestCode.query(pw, em, tx);
    }
}
