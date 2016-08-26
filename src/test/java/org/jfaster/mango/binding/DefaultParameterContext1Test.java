/*
 * Copyright 2014 mango.jfaster.org
 *
 * The Mango Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package org.jfaster.mango.binding;

import org.jfaster.mango.reflect.descriptor.ParameterDescriptor;
import org.jfaster.mango.reflect.TypeToken;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

/**
 * @author ash
 */
public class DefaultParameterContext1Test {

  @Test
  public void testSelectAndUpdate() throws Exception {
    List<Annotation> empty = Collections.emptyList();
    TypeToken<String> t0 = new TypeToken<String>() {
    };
    ParameterDescriptor p0 = ParameterDescriptor.create(0, t0.getType(), empty, "1");
    TypeToken<Integer> t1 = new TypeToken<Integer>() {
    };
    ParameterDescriptor p1 = ParameterDescriptor.create(1, t1.getType(), empty, "2");
    List<ParameterDescriptor> pds = Arrays.asList(p0, p1);

    ParameterContext ctx = DefaultParameterContext.create(pds);
    assertThat(ctx.getBindingParameterInvoker(BindingParameter.create("1", "")).getTargetType(), equalTo(t0.getType()));
    assertThat(ctx.getBindingParameterInvoker(BindingParameter.create("2", "")).getTargetType(), equalTo(t1.getType()));
  }

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  @Test
  public void testNotReadableParameterException() throws Exception {
    thrown.expect(BindingException.class);
    thrown.expectMessage("Parameter '3' not found, available root parameters are [1, 2]");

    List<Annotation> empty = Collections.emptyList();
    TypeToken<String> t = new TypeToken<String>() {
    };
    ParameterDescriptor p = ParameterDescriptor.create(0, t.getType(), empty, "1");
    ParameterDescriptor p2 = ParameterDescriptor.create(1, t.getType(), empty, "2");
    List<ParameterDescriptor> pds = Arrays.asList(p, p2);

    ParameterContext ctx = DefaultParameterContext.create(pds);
    ctx.getBindingParameterInvoker(BindingParameter.create("3", ""));
  }

  @Test
  public void testNotReadablePropertyException() throws Exception {
    thrown.expect(BindingException.class);
    thrown.expectMessage("Parameter ':1.id' can't be readable; caused by: There is no getter for property named 'id' in 'class java.lang.String'");

    List<Annotation> empty = Collections.emptyList();
    TypeToken<String> t = new TypeToken<String>() {
    };
    ParameterDescriptor p = ParameterDescriptor.create(0, t.getType(), empty, "1");
    List<ParameterDescriptor> pds = Arrays.asList(p);

    ParameterContext ctx = DefaultParameterContext.create(pds);
    ctx.getBindingParameterInvoker(BindingParameter.create("1", "id"));
  }

}
