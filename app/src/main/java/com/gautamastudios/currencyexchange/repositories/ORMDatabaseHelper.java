package com.gautamastudios.currencyexchange.repositories;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.gautamastudios.currencyexchange.interfaces.Repository;
import com.gautamastudios.currencyexchange.models.RatesModel;
import com.gautamastudios.currencyexchange.models.SupportedCurrenciesModel;

public class ORMDatabaseHelper extends OrmLiteSqliteOpenHelper {

    private Dao<SupportedCurrenciesModel, Integer> mCurrencyDao;
    private Dao<RatesModel, Integer> mRatesDao;


    private static final String DATABASE_NAME = "currencyexchange.db";
    private static final int DATABASE_VERSION = 1;

    private List<Repository> mRepositories;
    private boolean mWasRecentlyUpgraded;

    public ORMDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mRepositories = new ArrayList<>();
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        for (Repository repository : mRepositories) {
            repository.onCreate();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        for (Repository repository : mRepositories) {
            repository.onUpgrade(oldVersion, newVersion);
        }

        mWasRecentlyUpgraded = true;
    }

    public void registerRepository(Repository repository) {
        mRepositories.add(repository);
    }

    public boolean getWasRecentlyUpgraded() {
        return mWasRecentlyUpgraded;
    }

    //TODO check difference between runtimedao and dao
    public Dao<SupportedCurrenciesModel, Integer> getCurrencyDao() throws SQLException {
        if (mCurrencyDao == null) {
            mCurrencyDao = getDao(SupportedCurrenciesModel.class);
        }
        return mCurrencyDao;
    }

    public Dao<RatesModel, Integer> getRatesDao() throws SQLException {
        if (mRatesDao == null) {
            mRatesDao = getDao(RatesModel.class);
        }
        return mRatesDao;
    }
}
