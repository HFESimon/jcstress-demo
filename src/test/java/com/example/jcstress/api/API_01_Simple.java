package com.example.jcstress.api;

import org.openjdk.jcstress.annotations.*;
import org.openjdk.jcstress.infra.results.II_Result;

import static org.openjdk.jcstress.annotations.Expect.ACCEPTABLE;
import static org.openjdk.jcstress.annotations.Expect.ACCEPTABLE_INTERESTING;

/**
 * @author wenjing.zsm
 * @version 1.0
 * @title:
 * @description:
 * @since 2022/6/21 8:39 下午
 */

/*
    This is our first concurrency test. It is deliberately simplistic to show
    testing approaches, introduce JCStress APIs, etc.

    Suppose we want to see if the field increment is atomic. We can make test
    with two actors, both actors incrementing the field and recording what
    value they observed into the result object. As JCStress runs, it will
    invoke these methods on the objects holding the field once per each actor
    and instance, and record what results are coming from there.

    Done enough times, we will get the history of observed results, and that
    would tell us something about the concurrent behavior.

    How to run this test:
       $ java -jar jcstress-samples/target/jcstress.jar -t API_01_Simple

       ...

        .......... [OK] org.openjdk.jcstress.samples.api.API_01_Simple

          Scheduling class:
            actor1: package group 0, core group 0
            actor2: package group 0, core group 0

          CPU allocation:
            actor1: CPU #3, package #0, core #3
            actor2: CPU #35, package #0, core #3

          Compilation: split
            actor1: C2
            actor2: C2

          JVM args: []

          RESULT      SAMPLES    FREQ       EXPECT  DESCRIPTION
            1, 1   46,946,789   10.1%  Interesting  Both actors came up with the same value: atomicity failure.
            1, 2  110,240,149   23.8%   Acceptable  actor1 incremented, then actor2.
            2, 1  306,529,420   66.1%   Acceptable  actor2 incremented, then actor1.
 */

// Mark the class as JCStress test.
@JCStressTest(value = Mode.Continuous)

// These are the test outcomes.
@Outcome(id = {"1, 1"}, expect = ACCEPTABLE_INTERESTING, desc = "Both actors came up with the same value: atomicity failure.")
@Outcome(id = {"1, 2"}, expect = ACCEPTABLE, desc = "actor1 incremented, then actor2.")
@Outcome(id = {"2, 1"}, expect = ACCEPTABLE, desc = "actor2 incremented, then actor1.")

// This is a state object
@State
public class API_01_Simple {

    public API_01_Simple(){

    }

    int v;

    @Actor
    public void actor1(II_Result r) {
        r.r1 = ++v; // record result from actor1 to field r1
    }

    @Actor
    public void actor2(II_Result r) {
        r.r2 = ++v; // record result from actor2 to field r2
    }
}
