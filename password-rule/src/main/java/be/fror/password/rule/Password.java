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

import static com.google.common.base.CharMatcher.DIGIT;
import static com.google.common.base.CharMatcher.JAVA_LETTER;
import static com.google.common.base.CharMatcher.JAVA_LETTER_OR_DIGIT;
import static com.google.common.base.CharMatcher.WHITESPACE;
import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.base.CharMatcher;

import java.util.Objects;

/**
 *
 * @author Olivier Grégoire &lt;fror@users.noreply.github.com&gt;
 */
public final class Password {

  private static final CharMatcher UPPERCASE = new CharMatcher() {
    @Override
    public boolean matches(final char c) {
      return Character.isUpperCase(c);
    }
  };
  private static final CharMatcher LOWERCASE = new CharMatcher() {
    @Override
    public boolean matches(final char c) {
      return Character.isLowerCase(c);
    }
  };

  private final String password;
  private final String digit;
  private final String nonDigit;
  private final String alphabetical;
  private final String nonAlphabetical;
  private final String alphanumeric;
  private final String nonAlphanumeric;
  private final String uppercase;
  private final String lowercase;
  private final String whitespace;

  public Password(final String password) {
    checkNotNull(password);
    this.password = password;

    this.digit = DIGIT.retainFrom(password);
    this.nonDigit = DIGIT.removeFrom(password);
    this.alphabetical = JAVA_LETTER.retainFrom(password);
    this.nonAlphabetical = JAVA_LETTER.removeFrom(password);
    this.alphanumeric = JAVA_LETTER_OR_DIGIT.retainFrom(password);
    this.nonAlphanumeric = JAVA_LETTER_OR_DIGIT.removeFrom(password);
    this.uppercase = UPPERCASE.retainFrom(this.alphabetical);
    this.lowercase = LOWERCASE.retainFrom(this.alphabetical);
    this.whitespace = WHITESPACE.retainFrom(this.nonAlphabetical);
  }

  public String getPassword() {
    return password;
  }

  public String getDigit() {
    return digit;
  }

  public String getNonDigit() {
    return nonDigit;
  }

  public String getAlphabetical() {
    return alphabetical;
  }

  public String getNonAlphabetical() {
    return nonAlphabetical;
  }

  public String getAlphanumeric() {
    return alphanumeric;
  }

  public String getNonAlphanumeric() {
    return nonAlphanumeric;
  }

  public String getUppercase() {
    return uppercase;
  }

  public String getLowercase() {
    return lowercase;
  }

  public String getWhitespace() {
    return whitespace;
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
