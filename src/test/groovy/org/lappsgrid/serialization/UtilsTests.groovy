/*
 * Copyright (C) 2018 The Language Applications Grid
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

import org.junit.Test

import java.time.ZonedDateTime

/**
 *
 */
class UtilsTests {

    @Test
    void timestampTests() {
        String t = Utils.timestamp()
        println t

        ZonedDateTime time = Utils.parseTime(t)
        assert t == time.toString()
    }
}
