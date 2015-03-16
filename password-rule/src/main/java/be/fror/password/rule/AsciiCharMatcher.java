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

import com.google.common.base.Ascii;
import com.google.common.base.CharMatcher;

/**
 *
 * @author Olivier Grégoire &lt;fror@users.noreply.github.com&gt;
 */
class AsciiCharMatcher extends CharMatcher {

  private static final int ARRAY_SIZE = Ascii.MAX + 1;

  static AsciiCharMatcher anyOfAscii(CharSequence characters) {
    boolean[] matchingCharacters = new boolean[ARRAY_SIZE];
    for (int i = 0, l = characters.length(); i < l; i++) {
      matchingCharacters[characters.charAt(i)] = true;
    }
    return new AsciiCharMatcher(matchingCharacters);
  }

  private final boolean[] matchingCharacters;

  private AsciiCharMatcher(boolean[] matchingCharacters) {
    this.matchingCharacters = matchingCharacters;
  }

  @Override
  public boolean matches(char c) {
    return c <= Ascii.MAX && matchingCharacters[c];
  }

  AsciiCharMatcher or(AsciiCharMatcher other) {
    boolean[] newMatchingCharacters = new boolean[ARRAY_SIZE];
    for (int i = 0; i < ARRAY_SIZE; i++) {
      newMatchingCharacters[i] = this.matchingCharacters[i] | other.matchingCharacters[i];
    }
    return new AsciiCharMatcher(newMatchingCharacters);
  }

  AsciiCharMatcher and(AsciiCharMatcher other) {
    boolean[] newMatchingCharacters = new boolean[ARRAY_SIZE];
    for (int i = 0; i < ARRAY_SIZE; i++) {
      newMatchingCharacters[i] = this.matchingCharacters[i] & other.matchingCharacters[i];
    }
    return new AsciiCharMatcher(newMatchingCharacters);
  }
  
}
