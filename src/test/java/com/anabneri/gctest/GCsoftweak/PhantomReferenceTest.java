package com.anabneri.gctest.GCsoftweak;

import com.antkorwin.commonutils.gc.GcUtils;
import com.antkorwin.commonutils.gc.LeakDetector;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.lang.ref.PhantomReference;
import java.lang.ref.ReferenceQueue;

public class PhantomReferenceTest {
    @Test
    public void testQueuePollAfterFinalizationGC() throws InterruptedException {

        Foo foo = new Foo();
        ReferenceQueue<Foo> queue = new ReferenceQueue<>();
        PhantomReference<Foo> ref = new PhantomReference<>(foo, queue);


        foo = null;
        int gcPass = GcUtils.fullFinalization();

        Assertions.assertThat(ref.isEnqueued()).isTrue();
        Assertions.assertThat(gcPass).isLessThanOrEqualTo(3); //GcUtils
        Assertions.assertThat(queue.poll()).isEqualTo(ref);
    }

    @Test
    public void testWithoutFinalize() {

        Object instance = new Object();
        ReferenceQueue<Object> queue = new ReferenceQueue<>();
        PhantomReference<Object> ref = new PhantomReference<>(instance, queue);


        instance = null;

        GcUtils.fullFinalization();

        Assertions.assertThat(ref.isEnqueued()).isTrue();
        Assertions.assertThat(queue.poll()).isEqualTo(ref);
    }


    @Test
    public void testWithoutLeaks() {

        Foo foo = new Foo();
        LeakDetector leakDetector = new LeakDetector(foo);

        foo = null;

        leakDetector.assertMemoryLeaksNotExist();
    }

    @Test
    public void testWithLeak() {

        Foo foo = new Foo();
        Foo bar = foo;
        LeakDetector leakDetector = new LeakDetector(foo);

        foo = null;

        leakDetector.assertMemoryLeaksExist();
    }


    private class Foo {
        @Override
        protected void finalize() throws Throwable {
            System.out.println("FINISHED!");
            super.finalize();
        }
    }
}
