/*
 * Copyright (C) 2017 The Language Applications Grid
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package org.lappsgrid.serialization

/**
 * @author Keith Suderman
 */
class SerializationException extends Exception {
    SerializationException() {
    }

    SerializationException(String message) {
        super(message)
    }

    SerializationException(String message, Throwable t) {

        super(message, t)
    }

    SerializationException(Throwable t) {
        super(t)
    }

    SerializationException(String message, Throwable t, boolean suppress, boolean stackTrace) {
        super(message, t, suppress, stackTrace)
    }
}
