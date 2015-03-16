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

import static org.hamcrest.Matchers.emptyCollectionOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.common.base.Stopwatch;
import com.google.common.collect.ImmutableList;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Arrays;
import java.util.Optional;
import java.util.Random;

/**
 *
 * @author Olivier Grégoire &lt;fror@users.noreply.github.com&gt;
 */
public class RulerTest {

  private static final Random RANDOM = new Random();

  public RulerTest() {
  }

  @BeforeClass
  public static void setUpClass() {
  }

  @AfterClass
  public static void tearDownClass() {
  }

  @Before
  public void setUp() {
  }

  @After
  public void tearDown() {
  }

  /**
   * Test of validatePassword method, of class Ruler.
   */
  @Test
  public void testValidate() {
    Rule rule1 = mock(Rule.class);
    Rule rule2 = mock(Rule.class);
    Ruler instance = Ruler.createRuler(ImmutableList.of(rule1));
    RuleResult result;
    ImmutableList<Failure> failures;

    { // Verify that rule1.validatePassword() is called.
      when(rule1.validate(any())).thenReturn(RuleResult.ok());
      instance.validatePassword("abc");
      verify(rule1).validate(new Password("abc"));
    }

    { // A single rule, returning ok.
      when(rule1.validate(any())).thenReturn(RuleResult.ok());
      result = instance.validatePassword("abc");
      assertThat(result.isValid(), is(true));
      assertThat(result.getFailures(), is(emptyCollectionOf(Failure.class)));
    }

    { // A single rule, failing
      when(rule1.validate(any())).thenReturn(RuleResult.failed("failed"));
      result = instance.validatePassword("abc");
      failures = result.getFailures();
      Optional<Failure> failure = failures.stream().findFirst();
      assertThat(result.isValid(), is(false));
      assertThat(failures, hasSize(1));
      assertThat(failure.get().getReason(), is(equalTo("failed")));
      assertThat(failure.get().getParameters().size(), is(0));
    }
  }

  @Test
  public void testGenerate() {
    Ruler ruler = Ruler.createRuler(Arrays.asList(
        Rule.asciiLowercaseLetters(1),
        Rule.asciiUppercaseLetters(1),
        Rule.asciiDigits(1),
        Rule.asciiSymbols(1)
    ));

    for (int i = 0; i < 20; i++) {
      String password = ruler.generatePassword(8, RANDOM);
      assertThat(ruler.validatePassword(password), is(RuleResult.ok()));
    }
  }

}
