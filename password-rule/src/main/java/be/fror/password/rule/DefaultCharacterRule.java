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

import com.google.common.base.CharMatcher;

/**
 *
 * @author Olivier Grégoire &lt;fror@users.noreply.github.com&gt;
 */
class DefaultCharacterRule implements CharacterRule {

  private final CharMatcher matcher;
  private final String characters;
  private final int numberOfCharacters;
  private final String errorCode;

  DefaultCharacterRule(String characters, CharMatcher matcher, int numberOfCharacters, String errorCode) {
    this.characters = characters;
    this.numberOfCharacters = numberOfCharacters;
    this.matcher = matcher;
    this.errorCode = errorCode;
  }

  @Override
  public int getNumberOfCharacters() {
    return this.numberOfCharacters;
  }

  @Override
  public String getValidCharacters() {
    return this.characters;
  }

  @Override
  public RuleResult validate(Password password) {
    if (this.matcher.retainFrom(password.getPassword()).length() >= this.numberOfCharacters) {
      return RuleResult.ok();
    } else {
      return RuleResult.failed(this.errorCode,
          "characters", this.characters,
          "numberOfCharacters", this.numberOfCharacters
      );
    }
  }
}
