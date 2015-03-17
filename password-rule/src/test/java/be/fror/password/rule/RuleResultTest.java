/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.fror.password.rule;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import com.google.common.collect.ImmutableList;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Olivier Grégoire <https://github.com/fror>
 */
public class RuleResultTest {

  public RuleResultTest() {
  }

  /**
   *
   */
  @BeforeClass
  public static void setUpClass() {
  }

  /**
   *
   */
  @AfterClass
  public static void tearDownClass() {
  }

  /**
   *
   */
  @Before
  public void setUp() {
  }

  /**
   *
   */
  @After
  public void tearDown() {
  }

  /**
   * Test of isValid method, of class RuleResult.
   */
  @Test
  public void testIsValid() {
  }

  /**
   * Test of getFailures method, of class RuleResult.
   */
  @Test
  public void testGetFailures() {
  }

  /**
   * Test of ok method, of class RuleResult.
   */
  @Test
  public void testOk() {
    RuleResult result = RuleResult.ok();

    assertThat(result.isValid(), is(true));
    assertThat(result.getFailures(), is(equalTo(ImmutableList.of())));
  }

  /**
   * Test of failed method, of class RuleResult.
   */
  @Test
  public void testFailed_0args() {
    RuleResult result = RuleResult.failed();

    assertThat(result.isValid(), is(false));
    assertThat(result.getFailures(), is(equalTo(ImmutableList.of())));
  }

  /**
   * Test of failed method, of class RuleResult.
   */
  @Test
  public void testFailed_String() {
  }

  /**
   * Test of failed method, of class RuleResult.
   */
  @Test
  public void testFailed_3args() {
  }

  /**
   * Test of failed method, of class RuleResult.
   */
  @Test
  public void testFailed_5args() {
  }

  /**
   * Test of failed method, of class RuleResult.
   */
  @Test
  public void testFailed_7args() {
  }

  /**
   * Test of failed method, of class RuleResult.
   */
  @Test
  public void testFailed_String_Map() {
  }

}
