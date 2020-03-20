package com.keya.flexoffice.exposition;

import com.keya.flexoffice.domain.Profile;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
@Aspect
public class Player {



    @Pointcut("execution(* com.apress.cems.*.*PersonRepo+.findBy*(..))")
    public void repoFind() {}

    @Pointcut ("execution (* com.apress.cems.aop.service.*Service+.findBy*(..)))")
    public void serviceFind() {}

    /* TODO 23. Declare this method as an AfterReturning advice and create a pointcut expression that matches any method
   with the name starting with "save" that is defined in a class with the name containing "Service" */

    //@AfterReturning("*+.*Service+**.save+*()")
    @AfterReturning(pointcut = "com.xyz.myapp.SystemArchitecture.dataAccessOperation()",returning ="profile")

    // @AfterReturning("com.xyz.myapp.SystemArchitecture.dataAccessOperation()")

    public void afterServiceSave(JoinPoint joinPoint, Profile result) {


    }

    @Pointcut("execution(public * *(..))")
    private void anyPublicOperation() {}

    @Pointcut("execution(com.keya.flexoffice.domain.SiteServiceImpl.update**(..))")
    private void updatePublicOperation() {}
    @Before("serviceFind()")
    public void beforeFind(JoinPoint joinPoint) {

    }
    @After("repoFind() && serviceFind()")
    public void afterFind(JoinPoint joinPoint) {

    }



        public static void main(String args[]) {
        Scanner in = new Scanner(System.in);
        List<Integer> ints = new ArrayList<>();


        // game loop
        while (true) {
            for (int i = 0; i < 8; i++) {
                int mountainH = in.nextInt(); // represents the height of one mountain.
                ints.add(mountainH);
            }

            int result = ints.stream().max(Integer::compareTo).get();
            int index = ints.indexOf(result);
            ints = new ArrayList<>();

            // Write an action using System.out.println()
            // To debug: System.err.println("Debug messages...");

            System.out.println(String.valueOf(index)); // The index of the mountain to fire on.
        }
    }
}
