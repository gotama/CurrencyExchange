package com.gautamastudios.currencyexchange.events;

import android.content.Context;
import android.util.Log;

import de.greenrobot.event.EventBus;

public class GreenRobotEventBus {
    private final static String TAG = "GreenRobotEventBus";
    private final EventBus mEventBus;
    private final Context mContext;

    public GreenRobotEventBus(Context context, de.greenrobot.event.EventBus eventBus) {
        mContext = context;
        mEventBus = eventBus;
    }

    public boolean isRegistered(Object object) {
        return mEventBus.isRegistered(object);
    }

    public void register(Object object) {
        if (!mEventBus.isRegistered(object)) {
            Log.d(TAG, String.format("Journal: Register %s", object.getClass().getCanonicalName()));
            mEventBus.register(object);
        }
    }

    public void unregister(Object subscriber) {
        Log.d(TAG, String.format("Journal: Unregister %s", subscriber.getClass().getCanonicalName()));
        mEventBus.unregister(subscriber);
    }

    public void post(Object object) {
        Log.d(TAG, String.format("Journal: Event Posted %s", object.getClass().getCanonicalName()));
        mEventBus.post(object);
    }
}
