package com.troy.keeper.hbz.helper;

import java.util.Stack;

/**
 * Created by leecheng on 2018/3/28.
 */
public class Stacks {

    private static ThreadLocal<Stack> stack = new ThreadLocal<>();

    public static void push(Object o) {
        Stack stack = Stacks.stack.get();
        if (stack == null) {
            stack = new Stack();
            Stacks.stack.set(stack);
        }
        stack.push(o);
    }

    public static <T> T pop() {
        Stack stack = Stacks.stack.get();
        return (T) stack.pop();
    }

}
