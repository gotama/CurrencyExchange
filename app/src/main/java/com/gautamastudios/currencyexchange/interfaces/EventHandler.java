package com.gautamastudios.currencyexchange.interfaces;

import android.content.Context;

public interface EventHandler {
    void register(Context context);

    void bindContext(Context context);

    void unbindContext();

    void unregister();
}
