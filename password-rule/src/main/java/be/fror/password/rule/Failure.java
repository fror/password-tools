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

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.base.MoreObjects;
import com.google.common.collect.ImmutableMap;

import java.util.Map;
import java.util.Objects;

/**
 * Represents a failure
 *
 * @author Olivier Grégoire &lt;fror@users.noreply.github.com&gt;
 */
public class Failure {

  private final String reason;
  private final ImmutableMap<String, Object> parameters;

  /**
   *
   * @param reason
   * @param parameters
   */
  public Failure(final String reason, final Map<String, Object> parameters) {
    checkNotNull(reason, "reason must not be null");
    this.reason = reason;
    this.parameters = ImmutableMap.copyOf(parameters);
  }

  /**
   * 
   * @return the reason of this failure
   */
  public String getReason() {
    return this.reason;
  }

  /**
   * 
   * @return the parameters linked to this failure
   */
  public ImmutableMap<String, Object> getParameters() {
    return parameters;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null || this.getClass() != obj.getClass()) {
      return false;
    }
    Failure other = (Failure) obj;
    return Objects.equals(this.reason, other.reason)
        && Objects.equals(this.parameters, other.parameters);
  }

  @Override
  public int hashCode() {
    return Objects.hash(Failure.class, this.reason, this.parameters);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(Failure.class)
        .add("reason", this.reason)
        .add("parameters", this.parameters)
        .toString();
  }
}
