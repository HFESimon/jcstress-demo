package com.example.jcstress;

import org.openjdk.jcstress.annotations.*;

import static org.openjdk.jcstress.annotations.Expect.ACCEPTABLE;
import static org.openjdk.jcstress.annotations.Expect.ACCEPTABLE_INTERESTING;

/**
 * @author wenjing.zsm
 * @version 1.0
 * @title:
 * @description:
 * @since 2022/6/20 4:29 下午
 */

public class BasicJMM_04_Progress {

        /*
        How to run this test:
            $ java -jar jcstress-samples/target/jcstress.jar -t BasicJMM_04_Progress[.SubTestName]
     */

    /*
      ----------------------------------------------------------------------------------------------------------

        One naively can expect that writes to variables are eventually visible. However, under Java Memory Model,
        this does not apply to plain reads and writes. The usual example is the busy loop in plain field.`
        The optimizing compiler is allowed to check the field once, and if it is "false", reduce the rest of
        the loop into "while(true)", infinite version.

        Indeed, running this on just about any platform yields:

              RESULT  SAMPLES     FREQ       EXPECT  DESCRIPTION
               STALE        4   50.00%  Interesting  Test is stuck
          TERMINATED        4   50.00%   Acceptable  Gracefully finished
      */

    @JCStressTest(Mode.Termination)
    @Outcome(id = "TERMINATED", expect = ACCEPTABLE,             desc = "Gracefully finished")
    @Outcome(id = "STALE",      expect = ACCEPTABLE_INTERESTING, desc = "Test is stuck")
    @State
    public static class PlainSpin {
        boolean ready;

        @Actor
        public void actor1() {
            while (!ready); // spin
        }

        @Signal
        public void signal() {
            ready = true;
        }
    }
}
