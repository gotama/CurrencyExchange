package com.gautamastudios.currencyexchange.repositories;

import android.content.Context;
import android.util.Log;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import com.gautamastudios.currencyexchange.interfaces.Repository;
import com.gautamastudios.currencyexchange.models.RatesModel;
import com.gautamastudios.currencyexchange.models.SupportedCurrenciesModel;

public class SupportedCurrenciesRepository implements Repository {

    private static final String TAG = "Repository";

    private ORMDatabaseHelper mDatabaseHelper;
    private Context mContext;

    public SupportedCurrenciesRepository(Context context, ORMDatabaseHelper databaseHelper) {
        mContext = context;
        mDatabaseHelper = databaseHelper;
        mDatabaseHelper.registerRepository(this);
    }

    public ORMDatabaseHelper getHelper() {
        if (mDatabaseHelper == null || mDatabaseHelper.getConnectionSource() == null) {
            mDatabaseHelper =
                    OpenHelperManager.getHelper(mContext, ORMDatabaseHelper.class);
        }
        return mDatabaseHelper;
    }

    @Override
    public void onCreate() {
        try {
            TableUtils.createTable(getHelper().getConnectionSource(), SupportedCurrenciesModel.class);
            TableUtils.createTable(getHelper().getConnectionSource(), RatesModel.class);
        } catch (SQLException sqlex) {
            Log.e(TAG, String.format("OnCreate Error Code: %d", sqlex.getErrorCode()), sqlex);
        }
    }

    @Override
    public void onUpgrade(int oldVersion, int newVersion) {
        try {
            TableUtils.dropTable(getHelper().getConnectionSource(), SupportedCurrenciesModel.class, true);
            TableUtils.dropTable(getHelper().getConnectionSource(), RatesModel.class, true);
            onCreate();
        } catch (SQLException sqlex) {
            Log.e(TAG, String.format("OnUpgrade Error Code: %d", sqlex.getErrorCode()), sqlex);
        }
    }

    @Override
    public boolean getWasRecentlyUpgraded() {
        return getHelper().getWasRecentlyUpgraded();
    }

    public void saveSupportedCurrencies(List<SupportedCurrenciesModel> supportedCurrenciesModelList) throws
            SQLException {
        Dao<SupportedCurrenciesModel, Integer> currencyDao = getHelper().getCurrencyDao();
        for (SupportedCurrenciesModel model : supportedCurrenciesModelList) {
            QueryBuilder<SupportedCurrenciesModel, Integer> qb = currencyDao.queryBuilder();
            qb.where().eq("currencyCode", model.getCurrencyCode());
            if (qb.queryForFirst() == null) {
                currencyDao.create(model);
            } else {
                currencyDao.update(model);
            }
        }
    }

    public void saveSupportedRates(List<RatesModel> ratesModelList) throws SQLException {
        Dao<RatesModel, Integer> ratesDao = getHelper().getRatesDao();
        for (RatesModel model : ratesModelList) {
            QueryBuilder<RatesModel, Integer> qb = ratesDao.queryBuilder();
            qb.where().eq("currencyId", model.getCurrencyId()).and().eq("currencyCode", model.getCurrencyCode());
            if (qb.queryForFirst() == null) {
                ratesDao.create(model);
            } else {
                ratesDao.update(model);
            }
        }
    }

    public List<SupportedCurrenciesModel> getAllSupportedCurrencies() throws SQLException {
        Dao<SupportedCurrenciesModel, Integer> currencyDao = getHelper().getCurrencyDao();
        List<SupportedCurrenciesModel> retVal = currencyDao.queryForAll();
        for (SupportedCurrenciesModel model : retVal) {
            model.setRates(getSupportedRates(model.getId()));
        }
        return retVal;
    }

    public SupportedCurrenciesModel getSupportedCurrency(int currencyId) throws SQLException {
        Dao<SupportedCurrenciesModel, Integer> currencyDao = getHelper().getCurrencyDao();
        return currencyDao.queryForId(currencyId);
    }

    public List<RatesModel> getSupportedRates(int currencyId) {
        RuntimeExceptionDao<RatesModel, Integer> ratesDao = getHelper().getRuntimeExceptionDao(RatesModel.class);

        HashMap<String, Object> searchParams = new HashMap<String, Object>();
        searchParams.put("currencyId", currencyId);

        return ratesDao.queryForFieldValues(searchParams);
    }
}
