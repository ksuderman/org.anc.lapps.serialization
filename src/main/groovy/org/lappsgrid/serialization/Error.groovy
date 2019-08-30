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

import groovy.transform.CompileStatic

import static org.lappsgrid.discriminator.Discriminators.Uri

/**
 * A Data object with the discriminator set to http://vocab.lappsgrid.org/ns/error
 *
 * The payload should contain the error message.
 *
 * @author Keith Suderman
 */
@CompileStatic
class Error extends Data<String> {

    public Error() {
        super(Uri.ERROR, "Unknown error.")
    }

    public Error(Map map) {
        this.discriminator = Uri.ERROR
        this.payload = map['payload']
    }

    public Error(String message) {
        this.discriminator = Uri.ERROR
        this.payload = message
    }
}
