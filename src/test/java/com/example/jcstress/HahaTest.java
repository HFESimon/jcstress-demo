package com.example.jcstress;

import org.openjdk.jcstress.annotations.*;
import org.openjdk.jcstress.infra.results.C_Result;
import org.openjdk.jcstress.infra.results.S_Result;

/**
 * @author wenjing.zsm
 * @version 1.0
 * @title:
 * @description:
 * @since 2022/6/21 9:21 下午
 */

@JCStressTest(value = Mode.Continuous)
@Outcome(id = "a", expect = Expect.ACCEPTABLE, desc = "actor1")
@Outcome(id = "b", expect = Expect.ACCEPTABLE, desc = "actor2")
@State
public class HahaTest {

    @Actor
    public void actor1(C_Result c){
        c.r1 = 'a';
    }

    @Actor
    public void actor2(C_Result c){
        c.r1 = 'b';
    }
}
