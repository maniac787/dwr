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
package uk.ltd.getahead.dwr.convert;

import junit.framework.TestCase;
import uk.ltd.getahead.dwr.InboundVariable;
import uk.ltd.getahead.dwr.InboundContext;
import uk.ltd.getahead.dwr.OutboundContext;
import uk.ltd.getahead.dwr.OutboundVariable;

/**
 * The tests for the <code>ConstructorConverter</code> class.
 * @see ConstructorConverter
 * @author Bram Smeets
 */
public class ConstructorConverterTests extends TestCase
{
    private ConstructorConverter converter = new ConstructorConverter();

    /**
     * 
     */
    public void testConvertInbound()
    {
        InboundContext ctx = new InboundContext();
        InboundVariable iv = new InboundVariable(ctx, "type", "value");

        Object result = converter.convertInbound(String.class, iv, ctx);

        assertNotNull(result);
        assertTrue(result instanceof String);
        assertEquals("value", result);
    }

    /**
     * 
     */
    public void testConvertOutbound()
    {
        OutboundContext ctx = new OutboundContext();

        OutboundVariable result = converter.convertOutbound("value", ctx);

        assertNotNull(result);
        assertEquals("'value'", result.getAssignCode());
        assertEquals("", result.getInitCode());
    }

    /**
     * 
     */
    public void testSetConverterManager()
    {
        converter.setConverterManager(null);
    }
}