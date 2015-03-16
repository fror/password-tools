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

import java.util.Objects;

/**
 *
 * @author Olivier Grégoire &lt;fror@users.noreply.github.com&gt;
 */
public final class Password {

  private final String password;

  public Password(final String password) {
    checkNotNull(password);
    this.password = password;
  }

  public String getPassword() {
    return password;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null || this.getClass() != obj.getClass()) {
      return false;
    }
    Password other = (Password) obj;
    return Objects.equals(this.password, other.password);
  }

  @Override
  public int hashCode() {
    return Objects.hash(getClass(), this.password);
  }
}
