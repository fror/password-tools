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

import static be.fror.password.rule.AsciiCharMatcher.anyOfAscii;

/**
 *
 * @author Olivier Grégoire &lt;fror@users.noreply.github.com&gt;
 */
class RuleConstants {

  static final String ASCII_LOWERCASE_LETTER_CHARACTERS = "abcdefghijklmnopqrstuvwxyz";
  static final AsciiCharMatcher ASCII_LOWERCASE_LETTER_MATCHER = anyOfAscii(ASCII_LOWERCASE_LETTER_CHARACTERS);

  static final String ASCII_UPPERCASE_LETTER_CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
  static final AsciiCharMatcher ASCII_UPPERCASE_LETTER_MATCHER = anyOfAscii(ASCII_UPPERCASE_LETTER_CHARACTERS);

  static final String ASCII_LETTER_CHARACTERS = ASCII_LOWERCASE_LETTER_CHARACTERS + ASCII_UPPERCASE_LETTER_CHARACTERS;
  static final AsciiCharMatcher ASCII_LETTER_MATCHER = ASCII_LOWERCASE_LETTER_MATCHER.or(ASCII_UPPERCASE_LETTER_MATCHER);

  static final String ASCII_DIGIT_CHARACTERS = "0123456789";
  static final AsciiCharMatcher ASCII_DIGIT_MATCHER = anyOfAscii(ASCII_DIGIT_CHARACTERS);

  static final String ASCII_ALPHANUMERIC_CHARACTERS = ASCII_LETTER_CHARACTERS + ASCII_DIGIT_CHARACTERS;
  static final AsciiCharMatcher ASCII_ALPHANUMERIC_MATCHER = ASCII_LETTER_MATCHER.or(ASCII_DIGIT_MATCHER);

  static final String ASCII_SYMBOL_CHARACTERS = ",.;:?!+-*/=[](){}_@&\"'`$#%^~|<>\\";
  static final AsciiCharMatcher ASCII_SYMBOL_MATCHER = anyOfAscii(ASCII_SYMBOL_CHARACTERS);
}
