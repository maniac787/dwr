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
package uk.ltd.getahead.dwr;

import java.util.HashMap;
import java.util.Map;

/**
 * We need to keep track of stuff while we are converting on the way out to
 * prevent recurrsion.
 * This class helps track the conversion process.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public final class OutboundContext
{
    /**
     * The prefix for outbound variable names the we generate
     */
    public static final String OUTBOUND_VARIABLE_PREFIX = "s"; //$NON-NLS-1$

    /**
     * Have we already converted an object?
     * @param object The object to check
     * @return How it was converted last time or null if we've not seen it before
     */
    public OutboundVariable get(Object object)
    {
        return (OutboundVariable) map.get(object);
    }

    /**
     * @param object We have converted a new object, remember it
     * @param ss How the object was converted
     */
    public void put(Object object, OutboundVariable ss)
    {
        map.put(object, ss);
    }

    /**
     * Create a new variable name to keep everything we declare separate
     * @return A new unique variable name
     */
    public String getNextVariableName()
    {
        String varName = OUTBOUND_VARIABLE_PREFIX + nextVarIndex;
        nextVarIndex++;

        return varName;
    }

    /**
     * Create a new OutboundVariable along with a variable name and remembered
     * to avoid recursion.
     * @param data The data to be converted
     * @return A new OutboundVariable
     * @see OutboundContext#put(Object, OutboundVariable)
     * @see OutboundContext#getNextVariableName()
     */
    public OutboundVariable createOutboundVariable(Object data)
    {
        OutboundVariable ov = new OutboundVariable();
        ov.setAssignCode(getNextVariableName());
        put(data, ov);
        return ov;
    }

    /**
     * The map of objects to how we converted them last time
     */
    private final Map map = new HashMap();

    /**
     * What index do we tack on the next variable name that we generate
     */
    private int nextVarIndex = 0;
}
