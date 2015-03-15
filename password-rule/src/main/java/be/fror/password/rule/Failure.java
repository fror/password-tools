/*
 * Copyright 2015 Olivier Grégoire <fror@users.noreply.github.com>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package be.fror.password.rule;

import static com.google.common.base.Preconditions.*;
import com.google.common.collect.ImmutableMap;

import java.util.Map;

/**
 *
 * @author Olivier Grégoire &lt;fror@users.noreply.github.com&gt;
 */
public class Failure {

  private final String reason;
  private final ImmutableMap<String, Object> parameters;

  public Failure(final String reason, final Map<String, Object> parameters) {
    checkNotNull(reason, "reason must not be null");
    this.reason = reason;
    this.parameters = ImmutableMap.copyOf(parameters);
  }

  public String getReason() {
    return this.reason;
  }

  public ImmutableMap<String, Object> getParameters() {
    return parameters;
  }

}
