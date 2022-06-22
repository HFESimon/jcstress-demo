package com.example.jcstress.api;

import org.openjdk.jcstress.annotations.*;
import org.openjdk.jcstress.infra.results.I_Result;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author wenjing.zsm
 * @version 1.0
 * @title:
 * @description:
 * @since 2022/6/21 9:08 下午
 */

@JCStressTest
@Outcome(id = "1", expect = Expect.FORBIDDEN,  desc = "One update lost.")
@Outcome(id = "2", expect = Expect.ACCEPTABLE, desc = "Both updates.")
@State
public class AtomicIncrementTest {

    public AtomicIncrementTest(){

    }

    AtomicInteger ai = new AtomicInteger();

    @Actor
    public void actor1() {
        ai.incrementAndGet();
    }

    @Actor
    public void actor2() {
        ai.incrementAndGet();
    }

    @Arbiter
    public void arbiter(I_Result r) {
        r.r1 = ai.get();
    }
}
