package com.gautamastudios.currencyexchange.services;

import android.content.Context;
import android.util.Log;

import com.path.android.jobqueue.BaseJob;
import com.path.android.jobqueue.Job;
import com.path.android.jobqueue.JobManager;
import com.path.android.jobqueue.config.Configuration;
import com.path.android.jobqueue.di.DependencyInjector;
import com.path.android.jobqueue.log.CustomLogger;

import com.gautamastudios.currencyexchange.CurrencyExchangeApplication;
import com.gautamastudios.currencyexchange.services.jobs.BaseInjectorJob;

public class PathJobQueue {

    private final Context mContext;
    private JobManager mJobManager;
    private static final String TAG = "PathJobQueue";

    private void configureJobManager() {
        Configuration configuration = new Configuration.Builder(mContext)
                .injector(new DependencyInjector() {
                    //Using Deprecated class BaseInjectorJob Here, as the DependencyInjector has not
                    //been updated. Jobs created will use the new Job class which extends BaseInjectorJob still
                    @Override
                    public void inject(BaseJob baseJob) {
                        try {
                            ((CurrencyExchangeApplication) mContext).applicationComponent().inject((BaseInjectorJob) baseJob);
                        } catch (Exception ex) {
                            Log.e(TAG, ex.getMessage(), ex);
                        }
                    }
                })
                .customLogger(new CustomLogger() {
                    private static final String TAG = "JOBS";

                    @Override
                    public boolean isDebugEnabled() {
                        return true;
                    }

                    @Override
                    public void d(String text, Object... args) {
                        Log.d(TAG, String.format(text, args));
                    }

                    @Override
                    public void e(Throwable t, String text, Object... args) {
                        Log.e(TAG, String.format(text, args), t);
                    }

                    @Override
                    public void e(String text, Object... args) {
                        Log.e(TAG, String.format(text, args));
                    }
                })
                .minConsumerCount(1)//always keep at least one consumer alive
                .maxConsumerCount(3)//up to 3 consumers at a time
                .loadFactor(3)//3 jobs per consumer
                .consumerKeepAlive(120)//wait 2 minute
                .build();
        mJobManager = new JobManager(mContext, configuration);
    }

    public PathJobQueue(Context context) {
        mContext = context;
        configureJobManager();
        mJobManager.start();
    }

    public void addJobInBackground(Job job) {
        Log.d(TAG, String.format("Journal: Job Posted %s", job.getClass().getCanonicalName()));
        mJobManager.addJobInBackground(job);
    }
}
