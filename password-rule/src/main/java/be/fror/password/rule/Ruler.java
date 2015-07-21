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

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import be.fror.common.function.MoreCollectors;
import be.fror.common.function.Suppliers;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.ImmutableSet;
import com.google.common.primitives.Chars;

import java.util.Arrays;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Supplier;

import javax.annotation.concurrent.Immutable;
import javax.annotation.concurrent.NotThreadSafe;
import javax.annotation.concurrent.ThreadSafe;

/**
 *
 * @author Olivier Grégoire &lt;fror@users.noreply.github.com&gt;
 */
@ThreadSafe
@Immutable
public final class Ruler {

  /**
   * Factory method to easily create a new <tt>Ruler</tt> with all the rules given as parameter.
   *
   * @param rules the rules to create the ruler with.
   * @return a new <tt>Ruler</tt> using each <tt>Rule</tt> of <tt>rules</tt>.
   */
  public static Ruler createFromRules(Iterable<Rule> rules) {
    return new Builder().addRules(rules).build();
  }

  @VisibleForTesting
  final ImmutableSet<Rule> rules;

  private final Supplier<Generator> generator;

  @VisibleForTesting
  Ruler(Builder builder) {
    this.rules = builder.rules.build();
    this.generator = Suppliers.memoize(this::createGenerator);
  }

  /**
   * Validates the password according to the defined rules and aggregates their result in a
   * <tt>{@link RuleResult}</tt>.
   *
   * @param password the password to validatePassword
   * @return the result of the validation
   */
  public RuleResult validatePassword(final String password) {
    checkNotNull(password, "password must not be null");
    final Password pwd = new Password(password);
    final RuleResult.FailedResult failedResult = RuleResult.failed();
    boolean failed = false;
    for (Rule rule : this.rules) {
      final RuleResult result = rule.validate(pwd);
      if (!result.isValid()) {
        failed = true;
        failedResult.addFailures(result.getFailures());
      }
    }
    if (failed) {
      return failedResult;
    } else {
      return RuleResult.ok();
    }
  }

  /**
   * Generates a password of length <tt>length</tt> using <tt>random</tt>.
   *
   * @param length the length of the password to generate
   * @param random the random number generator to use to generate the password
   * @return A password valid according the provided character rules.
   * @throws IllegalArgumentException if <tt>length &lt;= 0</tt>
   * @throws IllegalStateException if no <tt>CharacterRule</tt> were provided when creating this
   * ruler.
   */
  public String generatePassword(final int length, final Random random) {
    final Generator gen = this.generator.get();
    checkArgument(length >= gen.minLength, "length (%s) must be greater than %s given the CharacterRules provided", length, gen.minLength);
    checkNotNull(random, "random must not be null");
    return gen.generate(length, random);
  }

  private Generator createGenerator() {
    final ImmutableSet<CharacterRule> charRules = rules.stream()
        .filter(e -> e instanceof CharacterRule)
        .map(e -> (CharacterRule) e)
        .collect(MoreCollectors.toImmutableSet());
    checkState(!charRules.isEmpty(), "No CharacterRule were added to this Ruler");
    Set<Character> uniqueChars = new TreeSet<>();
    int minLen = 0;
    for (CharacterRule rule : charRules) {
      for (char c : rule.getValidCharacters().toCharArray()) {
        uniqueChars.add(c);
      }
      minLen += rule.getNumberOfCharacters();
    }
    return new Generator(charRules, Chars.toArray(uniqueChars), minLen);
  }

  class Generator {

    final ImmutableSet<CharacterRule> rules;
    final char[] allChars;
    final int minLength;

    Generator(ImmutableSet<CharacterRule> rules, char[] allChars, int minLength) {
      this.rules = rules;
      this.allChars = allChars;
      this.minLength = minLength;
    }

    String generate(int length, Random random) {
      final char[] password = new char[length];
      int offset = 0;

      // Add mandatory characters
      for (CharacterRule rule : rules) {
        final String source = rule.getValidCharacters();
        for (int i = 0, l = rule.getNumberOfCharacters(); i < l; i++) {
          password[offset++] = source.charAt(random.nextInt(source.length()));
        }
      }

      // Add to match length
      while (offset < length) {
        password[offset++] = allChars[random.nextInt(allChars.length)];
      }

      // Shuffle the charaters // What? No Arrays.shuffle??
      for (int i = password.length - 1; i > 0; i--) {
        final int pos = random.nextInt(i + 1);
        final char swap = password[pos];
        password[pos] = password[i];
        password[i] = swap;
      }

      return new String(password);
    }
  }

  /**
   * The builder class for Ruler.
   *
   * <p>
   * The rules of the <tt>Ruler</tt> are ordered in the order they were added in this Builder.
   *
   * <p>
   * The methods are chained, meaning that it is possible to create a
   * <tt>Ruler</tt> like this:
   *
   * <pre><code>
   * Ruler ruler = new Ruler.Builder()
   *   .addRule(firstRule)
   *   .addRule(secondRule)
   *   .build();
   * </code></pre>
   */
  @NotThreadSafe
  public static class Builder {

    private boolean nonEmpty = false;
    private final ImmutableSet.Builder<Rule> rules = ImmutableSet.builder();

    /**
     * Instantiates a new <tt>Rule</tt> builder.
     */
    public Builder() {
    }

    /**
     * Adds a rule to the <tt>Ruler</tt> being built.
     *
     * <p>
     * If two identical rules are added, only the first is retained and the other one is silently
     * not added.
     *
     * @param rule the <tt>Rule</tt> to add.
     * @return <tt>this</tt>
     */
    public Builder addRule(Rule rule) {
      this.rules.add(rule);
      this.nonEmpty = true;
      return this;
    }

    /**
     * Adds rules to the <tt>Ruler</tt> being built.
     *
     * <p>
     * If two identical rules are added, only the first is retained and the other one is silently
     * not added.
     *
     * @param rules the <tt>Rule</tt> to add.
     * @return <tt>this</tt>
     */
    public Builder addRules(Iterable<Rule> rules) {
      boolean nonEmptyRules = rules.iterator().hasNext();
      this.rules.addAll(rules);
      this.nonEmpty |= nonEmptyRules;
      return this;
    }

    /**
     * Creates a new <tt>Ruler</tt> with all the rules that were add to
     * <tt>this</tt>.
     *
     * @return a new ruler containing the rules
     * @throws IllegalStateException if no rule were added to <tt>this</tt>.
     */
    public Ruler build() {
      checkState(this.nonEmpty, "No rules were added to this builder");
      return new Ruler(this);
    }
  }
}
