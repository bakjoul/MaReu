package com.bakjoul.mareu.utils;

import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import android.view.View;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.test.espresso.PerformException;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.util.HumanReadables;
import androidx.test.espresso.util.TreeIterables;

import org.hamcrest.Matcher;

import java.util.concurrent.TimeoutException;

public class WaitForId implements ViewAction {

    @IdRes
    private final int viewId;

    private final long millis;

    public WaitForId(int viewId, long millis) {
        this.viewId = viewId;
        this.millis = millis;
    }

    @Override
    public Matcher<View> getConstraints() {
        return isRoot();
    }

    @Override
    public String getDescription() {
        return "wait for a specific view with id <" + viewId + "> during " + millis + " millis.";
    }

    @Override
    public void perform(@NonNull UiController uiController, View view) {
        uiController.loopMainThreadUntilIdle();
        final long startTime = System.currentTimeMillis();
        final long endTime = startTime + millis;
        final Matcher<View> viewMatcher = withId(viewId);

        do {
            for (View child : TreeIterables.breadthFirstViewTraversal(view)) {
                // found view with required ID
                if (viewMatcher.matches(child)) {
                    return;
                }
            }

            uiController.loopMainThreadForAtLeast(50);
        }
        while (System.currentTimeMillis() < endTime);

        // timeout happens
        throw new PerformException.Builder()
                .withActionDescription(this.getDescription())
                .withViewDescription(HumanReadables.describe(view))
                .withCause(new TimeoutException())
                .build();
    }
}
