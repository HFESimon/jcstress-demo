package com.example.jcstress;

import org.openjdk.jcstress.annotations.Actor;
import org.openjdk.jcstress.annotations.JCStressTest;
import org.openjdk.jcstress.annotations.Outcome;
import org.openjdk.jcstress.annotations.State;
import org.openjdk.jcstress.infra.results.L_Result;

import static org.openjdk.jcstress.annotations.Expect.ACCEPTABLE;
import static org.openjdk.jcstress.annotations.Expect.FORBIDDEN;

/**
 * @author wenjing.zsm
 * @version 1.0
 * @title:
 * @description:
 * @since 2022/6/21 2:40 下午
 */

@JCStressTest
@Outcome(id = "null",                   expect = ACCEPTABLE, desc = "Not seeing the object yet")
@Outcome(id = "class java.lang.Object", expect = ACCEPTABLE, desc = "Seeing the object, valid class")
@Outcome(                               expect = FORBIDDEN,  desc = "Other cases are illegal")
@State
public class BasicJMM_01_DataRaces {

    /*
        How to run this test:
            $ java -jar jcstress-samples/target/jcstress.jar -t BasicJMM_01_DataRaces
     */

    /*
      ----------------------------------------------------------------------------------------------------------

        This is our first case: data race. There is a conflict: writer and reader access
        the same location without any synchronization. That is a _data race_, by definition.

        This test might look trivial, but it actually highlights a rather strong Java property:
        even in the presence of data races, the behavior can be reasonable. Notably, data races
        do not break the JVM. Here, we are publishing the object through the race, but even
        then the object has all metadata set, so we can ask the classes, call the methods,
        access the fields.

        On all platforms, this test yields:

                      RESULT        SAMPLES     FREQ      EXPECT  DESCRIPTION
      class java.lang.Object  3,619,439,149   51.74%  Acceptable  Seeing the object, valid class
                        null  3,376,358,355   48.26%  Acceptable  Not seeing the object yet
    */

    Object o;

    @Actor
    public void writer() {
        o = new Object();
    }

    @Actor
    public void reader(L_Result r) {
        Object lo = o;
        if (lo != null) {
            try {
                r.r1 = lo.getClass();
            } catch (NullPointerException npe) {
                r.r1 = npe;
            }
        } else {
            r.r1 = null;
        }
    }
}
