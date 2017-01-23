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

package org.lappsgrid.serialization.aas
/**
 * @author Keith Suderman
 * @deprecated
 *
 */
class Authorization {
    String username
    String resourceId
    Token token;
    List<License> restrictions

//    public Authorization() {
//    }
//
//
//    public Authorization(Authorization auth) {
//        this()
//        assignFrom(auth)
//    }
//
//    private void assignFrom(Authorization proxy) {
//        this.username = proxy.username
//        this.resourceId = proxy.resourceId
//        this.restrictions = []
//        this.restrictions.addAll(proxy.restrictions)
//    }
//
//    String toJson() {
//        objectMapper.disable(SerializationFeature.INDENT_OUTPUT)
//        return objectMapper.writeValueAsString(this)
//    }
//
//    String toPrettyJson() {
//        objectMapper.enable(SerializationFeature.INDENT_OUTPUT)
//        return objectMapper.writeValueAsString(this)
//    }
//
//    /** Calls toPrettyJson() */
//    String toString() {
//        return toJson()
//    }
//
}
