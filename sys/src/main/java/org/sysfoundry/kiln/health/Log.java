/*
 * Copyright 2019 Sysfoundry (www.sysfoundry.org)
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

package org.sysfoundry.kiln.health;

import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import static org.sysfoundry.kiln.Constants.LOG_NAME_PATTERN;


public class Log {

    public static Logger get(@NonNull String name){
        Logger requestedLogger = LoggerFactory.getLogger(name);
        if(!name.matches(LOG_NAME_PATTERN)){
            requestedLogger.warn("Invalid logger name: {}. Only numbers, lowercase letters and - are allowed!",name);
        }
        return requestedLogger;
    }
}
