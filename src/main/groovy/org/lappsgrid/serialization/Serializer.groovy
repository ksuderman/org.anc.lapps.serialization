/*
 * Copyright 2014 The Language Application Grid
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
 *
 */

package org.lappsgrid.serialization

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature

/**
 * Serializes objects to/from JSON.
 * <p>
 * The `Serializer` class is a thin wrapper around the Jackson ObjectMapper.  While
 * it is primarily intended to serialize LIF objects, there is nothing that prevents
 * it from serializing arbitrary Java objects.
 * <p>
 * Since the Jackson ObjectMapper is thread-safe the Serializer class is also thread-safe.
 *
 * @author Keith Suderman
 */
class Serializer {
    private static ObjectMapper mapper;
    private static ObjectMapper prettyPrinter;

    static {
        mapper = new ObjectMapper()
        mapper.disable(SerializationFeature.INDENT_OUTPUT)
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL)
        prettyPrinter = new ObjectMapper()
        prettyPrinter.enable(SerializationFeature.INDENT_OUTPUT)
        prettyPrinter.setSerializationInclusion(JsonInclude.Include.NON_NULL)
    }
    private Serializer() {}

    /**
     * Parses a JSON string and creates an instance of the specified class.
     */
    public static <T> T parse(String json, Class<T> theClass) {
        // Bug #? : Log the error if you must, but just dumping a stack trace
        // simply confuses things.
//        T result = null
//        try {
//            result = (T) mapper.readValue(json, theClass)
//        }
//        catch(Exception e)
//        {
//            e.printStackTrace()
//            // Ignored. We return null to indicate an error.
//        }
        return (T) mapper.reader(json, theClass)
    }

    /**
     * Returns a JSON representation of the object.
     */
    public static String toJson(Object object)
    {
        try {
            return mapper.writeValueAsString(object)
        }
        catch (Exception e)
        {
            return null;
        }
    }

    /** Returns a pretty-printed JSON representation of the object. */
    public static String toPrettyJson(Object object)
    {
        try {
            return prettyPrinter.writeValueAsString(object)
        }
        catch (Exception e)
        {
            return null;
        }
    }
}
