package com.example.jcstress;

import org.openjdk.jcstress.annotations.Actor;
import org.openjdk.jcstress.annotations.JCStressTest;
import org.openjdk.jcstress.annotations.Outcome;
import org.openjdk.jcstress.annotations.State;
import org.openjdk.jcstress.infra.results.IIII_Result;

import static org.openjdk.jcstress.annotations.Expect.ACCEPTABLE;
import static org.openjdk.jcstress.annotations.Expect.ACCEPTABLE_INTERESTING;

/**
 * @author wenjing.zsm
 * @version 1.0
 * @title:
 * @description:
 * @since 2022/6/21 3:39 下午
 */

@JCStressTest
@Outcome(id = "-1, -1, -1, -1", expect = ACCEPTABLE, desc = "Object is not seen yet.")
@Outcome(id = "1, 2, 3, 4", expect = ACCEPTABLE, desc = "Seen the complete object.")
@Outcome(expect = ACCEPTABLE_INTERESTING, desc = "Seeing partially constructed object.")
@State
public class BasicJMM_08_Finals {

        /*
        How to run this test:
            $ java -jar jcstress-samples/target/jcstress.jar -t BasicJMM_08_Finals[.SubTestName]
     */

    /*
      ----------------------------------------------------------------------------------------------------------

        Finals are another aspect of Java Memory Model. They allow surviving the publication
        via the race. In other words, they provide some basic inter-thread semantics, even in
        the absence of proper synchronization.

        x86_64:
                  RESULT      SAMPLES     FREQ       EXPECT  DESCRIPTION
          -1, -1, -1, -1  115,042,024   52.43%   Acceptable  Object is not seen yet.
              1, 0, 3, 0        3,892   <0.01%  Interesting  Seeing partially constructed object.
              1, 0, 3, 4       14,291   <0.01%  Interesting  Seeing partially constructed object.
              1, 2, 3, 0          123   <0.01%  Interesting  Seeing partially constructed object.
              1, 2, 3, 4  104,365,974   47.56%   Acceptable  Seen the complete object.

        AArch64:
                  RESULT      SAMPLES     FREQ       EXPECT  DESCRIPTION
          -1, -1, -1, -1  508,775,120   84.84%   Acceptable  Object is not seen yet.
              0, 0, 0, 0        1,725   <0.01%  Interesting  Seeing partially constructed object.
              0, 0, 0, 4          763   <0.01%  Interesting  Seeing partially constructed object.
              0, 0, 3, 0            9   <0.01%  Interesting  Seeing partially constructed object.
              0, 0, 3, 4           57   <0.01%  Interesting  Seeing partially constructed object.
              0, 2, 0, 0           21   <0.01%  Interesting  Seeing partially constructed object.
              0, 2, 0, 4        6,177   <0.01%  Interesting  Seeing partially constructed object.
              0, 2, 3, 0           17   <0.01%  Interesting  Seeing partially constructed object.
              0, 2, 3, 4        1,103   <0.01%  Interesting  Seeing partially constructed object.
              1, 0, 0, 0          101   <0.01%  Interesting  Seeing partially constructed object.
              1, 0, 0, 4           16   <0.01%  Interesting  Seeing partially constructed object.
              1, 0, 3, 0            1   <0.01%  Interesting  Seeing partially constructed object.
              1, 0, 3, 4          132   <0.01%  Interesting  Seeing partially constructed object.
              1, 2, 0, 0            7   <0.01%  Interesting  Seeing partially constructed object.
              1, 2, 0, 4          125   <0.01%  Interesting  Seeing partially constructed object.
              1, 2, 3, 0           91   <0.01%  Interesting  Seeing partially constructed object.
              1, 2, 3, 4   90,884,551   15.16%   Acceptable  Seen the complete object.
    */
    int v = 1;

    MyObject o;

    @Actor
    public void actor1() {
        o = new MyObject(v);
    }

    @Actor
    public void actor2(IIII_Result r) {
        MyObject o = this.o;
        if (o != null) {
            r.r1 = o.x1;
            r.r2 = o.x2;
            r.r3 = o.x3;
            r.r4 = o.x4;
        } else {
            r.r1 = -1;
            r.r2 = -1;
            r.r3 = -1;
            r.r4 = -1;
        }
    }

    public static class MyObject {
        int x1, x2, x3, x4;

        public MyObject(int v) {
            x1 = v;
            x2 = x1 + v;
            x3 = x2 + v;
            x4 = x3 + v;
        }
    }
}
