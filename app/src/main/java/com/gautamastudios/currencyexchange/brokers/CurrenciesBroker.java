package com.gautamastudios.currencyexchange.brokers;

import com.google.gson.internal.LinkedTreeMap;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import retrofit2.Response;
import com.gautamastudios.currencyexchange.models.RatesModel;
import com.gautamastudios.currencyexchange.models.SupportedCurrenciesModel;
import com.gautamastudios.currencyexchange.repositories.SupportedCurrenciesRepository;
import com.gautamastudios.currencyexchange.utils.CalculationUtil;

public class CurrenciesBroker {

    private SupportedCurrenciesRepository mCurrenciesRepo;

    public CurrenciesBroker(SupportedCurrenciesRepository currenciesRepo) {
        this.mCurrenciesRepo = currenciesRepo;
    }

    //This method is not efficient, Best practice would be to maintain the supported list
    //on our own internal API, that way API control what currencies are supported and the
    //app doesnt need to be released to support a new currency
    public void saveSupportedCurrencies(Response<Object> response) throws Exception, NullPointerException {
        List<SupportedCurrenciesModel> supportedCurrenciesList = new ArrayList<>();
        SupportedCurrenciesModel model;
        if (response != null && response.body() != null) {
            ArrayList<Object> currencies = (ArrayList<Object>) ((LinkedTreeMap) response.body()).get("currencies");

            for (int i = 0; i < currencies.size(); i++) {
                LinkedTreeMap map = (LinkedTreeMap) currencies.get(i);
                if (map.get("currencyCode").equals("GBP") || map.get("currencyCode").equals("EUR")) {
                    model = new SupportedCurrenciesModel(map.get("currencyCode").toString(), new Date().getTime(), null);
                    supportedCurrenciesList.add(model);

                    if (supportedCurrenciesList.size() == 3) {
                        break;
                    }
                } else if (map.get("currencyCode").equals("USS")) {
                    //This else statement is to handle the API's multiple USD records
                    model = new SupportedCurrenciesModel("USD", new Date().getTime(), null);
                    supportedCurrenciesList.add(model);

                    if (supportedCurrenciesList.size() == 3) {
                        break;
                    }
                }
            }
        } else {
            //Added this code here to avoid Freemium API call restrictions should throw null pointer exception so Job can retry call
            //throw new NullPointerException();
            model = new SupportedCurrenciesModel("GBP", new Date().getTime(), null);
            supportedCurrenciesList.add(model);
            model = new SupportedCurrenciesModel("USD", new Date().getTime(), null);
            supportedCurrenciesList.add(model);
            model = new SupportedCurrenciesModel("EUR", new Date().getTime(), null);
            supportedCurrenciesList.add(model);
        }

        mCurrenciesRepo.saveSupportedCurrencies(supportedCurrenciesList);
    }

    public List<SupportedCurrenciesModel> getAllSupportedCurrencies() throws SQLException {
        return mCurrenciesRepo.getAllSupportedCurrencies();
    }

    public SupportedCurrenciesModel getSupportedCurrency(int currencyId) throws SQLException {
        return mCurrenciesRepo.getSupportedCurrency(currencyId);
    }

    public void saveSupportedRate(Response<Object> response, boolean fallbackAPIUsed, int currencyId) throws
            SQLException {
        List<RatesModel> ratesModelList = new ArrayList<>();
        RatesModel model;

        if (response != null && response.body() != null) {
            LinkedTreeMap map = (LinkedTreeMap) ((LinkedTreeMap) response.body()).get("rates");
            Set<Map.Entry> entries = map.entrySet();
            for (Map.Entry<String, Double> entry : entries) {
                if (entry.getKey().equals("GBP") || entry.getKey().equals("EUR") || entry.getKey().equals("USD")) {
                    model = new RatesModel(currencyId, entry.getKey(), CalculationUtil.convertDoubleValueToCents(entry.getValue()));
                    ratesModelList.add(model);
                }
            }
            mCurrenciesRepo.saveSupportedRates(ratesModelList);
        }
    }
}
