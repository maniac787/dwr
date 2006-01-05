/*
 * Copyright 2005 Joe Walker
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package uk.ltd.getahead.dwr.impl;

import junit.framework.TestCase;
import org.springframework.mock.web.MockHttpServletRequest;

import java.lang.reflect.Method;

import uk.ltd.getahead.dwr.create.NewCreator;
import uk.ltd.getahead.dwr.util.Messages;
import uk.ltd.getahead.dwr.Creator;

/**
 * @author Bram Smeets
 */
public class DefaultAccessControlTests extends TestCase
{
    private DefaultAccessControl accessControl = new DefaultAccessControl();

    private MockHttpServletRequest request;

    protected void setUp() throws Exception
    {
        super.setUp();

        request = new MockHttpServletRequest();
    }

    /**
     * @throws Exception
     */
    public void testReasonToNotDisplayDwrObject() throws Exception
    {
        NewCreator creator = new NewCreator();
        creator.setClass("uk.ltd.getahead.dwr.Messages");

        String result = accessControl.getReasonToNotDisplay(creator, "", getMethod());
        assertEquals(Messages.getString("ExecuteQuery.DeniedCoreDWR"), result);
    }

    /**
     * @throws Exception
     */
    public void testReasonToNotDisplay() throws Exception
    {
        NewCreator creator = new NewCreator();
        creator.setClass("java.lang.Object");

        String result = accessControl.getReasonToNotDisplay(creator, "", getMethod());
        assertNull(result);
    }

    /**
     * @throws Exception
     */
    public void testReasonToNotDisplayWithNonPublicMethod() throws Exception
    {
        String result = accessControl.getReasonToNotDisplay(null, null, getPrivateMethod());
        assertNotNull(result);
    }

    /**
     * @throws Exception
     */
    public void testReasonToNotDisplayWithNonExecutableMethod() throws Exception
    {
        accessControl.addExcludeRule("className", "someMethod");

        String result = accessControl.getReasonToNotDisplay(null, "className", getMethod());
        assertNotNull(result);
    }

    /**
     * @throws Exception
     */
    public void testReasonToNotDisplayWithMethodWithDwrParameter() throws Exception
    {
        NewCreator creator = new NewCreator();
        creator.setClass("java.lang.Object");

        String result = accessControl.getReasonToNotDisplay(creator, "className", getMethodWithDwrParameter());
        assertNotNull(result);
    }

    /**
     * @throws Exception
     */
    public void testReasonToNotDisplayWithObjectMethod() throws Exception
    {
        NewCreator creator = new NewCreator();
        creator.setClass("java.lang.Object");

        String result = accessControl.getReasonToNotDisplay(creator, "className", getHashCodeMethod());
        assertNotNull(result);
    }

    /**
     * @throws Exception
     */
    public void testReasonToNotExecute() throws Exception
    {
        NewCreator creator = new NewCreator();

        String result = accessControl.getReasonToNotExecute(creator, "className", getMethod());
        assertNull(result);

        accessControl.addRoleRestriction("className", "someMethod", "someRole");
        accessControl.addRoleRestriction("className", "someMethod", "someOtherRole");
        result = accessControl.getReasonToNotExecute(creator, "className", getMethod());
        assertNotNull(result);

        request.addUserRole("someRole");
        result = accessControl.getReasonToNotExecute(creator, "className", getMethod());
        assertNull(result);
    }

    /**
     * 
     */
    public void someMethod()
    {
        // do nothing
    }

    /**
     * @param someString
     * @param creator
     */
    public void someMethodWithDwrParameter(String someString, Creator creator)
    {
        Object ignore = someString;
        ignore =  creator;
        creator = (Creator) ignore;

        // do nothing
    }

    /**
     * 
     */
    private void somePrivateMethod()
    {
        // do nothing
    }

    private Method getMethod() throws NoSuchMethodException
    {
        return getClass().getMethod("someMethod", new Class[0]);
    }

    private Method getMethodWithDwrParameter() throws NoSuchMethodException
    {
        return getClass().getMethod("someMethodWithDwrParameter", new Class[]
        {
            String.class, Creator.class
        });
    }

    private Method getPrivateMethod() throws NoSuchMethodException
    {
        return getClass().getDeclaredMethod("somePrivateMethod", new Class[0]);
    }

    private Method getHashCodeMethod() throws NoSuchMethodException
    {
        return getClass().getMethod("hashCode", new Class[0]);
    }

    /**
     * Shuts lint up
     */
    protected void ignore()
    {
        somePrivateMethod();
    }
}