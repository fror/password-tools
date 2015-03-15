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

/**
 *
 * @author Olivier Grégoire &lt;fror@users.noreply.github.com&gt;
 */
class LengthRule implements Rule {

  private final int minimumLength;
  private final int maximumLength;

  LengthRule(final int minimumLength, final int maximumLength) {
    this.minimumLength = minimumLength;
    this.maximumLength = maximumLength;
  }

  @Override
  public RuleResult validate(final Password password) {
    checkNotNull(password);
    String pwd = password.getPassword();
    int length = pwd.length();
    if (length < this.minimumLength) {
      return RuleResult.failed("length.tooShort", "minimumLength", this.minimumLength);
    } else if (length > this.maximumLength) {
      return RuleResult.failed("length.tooLong", "maximumLength", this.maximumLength);
    } else {
      return RuleResult.ok();
    }
  }

  @Override
  public String toString() {
    if (this.minimumLength == this.maximumLength) {
      return new StringBuilder()
          .append("lengthIs")
          .append(this.minimumLength)
          .append(")")
          .toString();
    } else if (this.maximumLength == Integer.MAX_VALUE) {
      return new StringBuilder()
          .append("lengthIsGreaterThan(")
          .append(this.minimumLength)
          .append(")")
          .toString();
    } else {
      return new StringBuilder()
          .append("lengthIsBetween(")
          .append(this.minimumLength)
          .append(", ")
          .append(this.maximumLength)
          .append(")")
          .toString();
    }
  }
}
