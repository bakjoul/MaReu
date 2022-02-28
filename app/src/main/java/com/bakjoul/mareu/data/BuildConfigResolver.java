package com.bakjoul.mareu.data;

import com.bakjoul.mareu.BuildConfig;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class BuildConfigResolver {

    @Inject
    public BuildConfigResolver() {
    }

    public Boolean isDebug() {
        return BuildConfig.DEBUG;
    }

}
