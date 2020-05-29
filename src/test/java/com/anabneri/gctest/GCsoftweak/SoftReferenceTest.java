package com.anabneri.gctest.GCsoftweak;

import com.antkorwin.commonutils.gc.GcUtils;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.lang.ref.SoftReference;


@Slf4j
public class SoftReferenceTest {

    @Test
    public void productName() {

        String instance = new String("89829382");
        SoftReference<String> softReference = new SoftReference<>(instance);
        instance = null;
        Assertions.assertThat(softReference).isNotNull();
        Assertions.assertThat(softReference.get()).isNotNull();

        GcUtils.tryToAllocateAllAvailableMemory();

        Assertions.assertThat(softReference.get()).isNull();
    }
}
