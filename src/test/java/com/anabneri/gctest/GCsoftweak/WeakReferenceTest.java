package com.anabneri.gctest.GCsoftweak;

import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.lang.ref.WeakReference;

public class WeakReferenceTest {

    @Test
    public  void testeWeakAfterGC() {
        String instance = new String("2393829");
        WeakReference<String> reference = new WeakReference<>(instance);

        instance = null;
        System.gc();

        Assertions.assertThat(reference.get()).isNull();
    }
}
