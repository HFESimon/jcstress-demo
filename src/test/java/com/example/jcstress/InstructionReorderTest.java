package com.example.jcstress;

import org.openjdk.jcstress.annotations.*;
import org.openjdk.jcstress.infra.results.I_Result;

/**
 * @author wenjing.zsm
 * @version 1.0
 * @title:
 * @description:
 * @since 2022/6/17 2:46 下午
 */

@JCStressTest(value = Mode.Continuous)
@Outcome(id = {"0"}, expect = Expect.ACCEPTABLE_INTERESTING, desc = "wrong result") // 描述测试结果
@Outcome(id = {"-1", "5"}, expect = Expect.ACCEPTABLE, desc = "normal result") // 描述测试结果
@State
public class InstructionReorderTest {

    private boolean flag ;
    private int x = 0;

    public InstructionReorderTest() {

    }

    @Actor
    public void actor1(I_Result r) {
        if (flag) {
            r.r1 = x;
        } else {
            r.r1 = -1;
        }
    }

    @Actor
    public void actor2(I_Result r) {
        this.x = 5;
        flag = true;
    }

    @Arbiter
    public void actor3(I_Result r) {
        System.out.println("ha ha ha");
    }
}
