package com.telcobright.SmsReport.Admin.controller;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.telcobright.SmsReport.Admin.repositories.AccBalanceRepository;
import com.telcobright.SmsReport.Admin.repositories.TransactionHistoryRepository;
import com.telcobright.SmsReport.Models.AccountBalance;
import com.telcobright.SmsReport.Models.BalanceUpdateError;
import com.telcobright.SmsReport.Models.BalanceUpdateResult;
import com.telcobright.SmsReport.Models.TransactionHistory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin/Account")
public class AccBalanceController {
    final AccBalanceRepository accBalanceRepository;
    final TransactionHistoryRepository transactionHistoryRepository;

    public AccBalanceController (AccBalanceRepository accBalanceRepository, TransactionHistoryRepository transactionHistoryRepository){
        this.accBalanceRepository = accBalanceRepository;
        this.transactionHistoryRepository = transactionHistoryRepository;
    }

    public static AtomicInteger transactionDeleteSegmentCounter = new AtomicInteger(0);
    public static final int segmentSizeForTransactionDelete = 1000;
    private Double getBalanceFromOfbiz(String accountId){
        try{
            TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
                public java.security.cert.X509Certificate[] getAcceptedIssuers() { return null; }
                public void checkClientTrusted(X509Certificate[] certs, String authType) { }
                public void checkServerTrusted(X509Certificate[] certs, String authType) { }
            } };

            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

            HostnameVerifier allHostsValid = (hostname, session) -> true;
            HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);

            URL url = new URL("https://localhost:8443/ofbiz-spring/api/Accounting/getAccountBalance");

            HttpsURLConnection httpsURLConnection = (HttpsURLConnection) url.openConnection();
            httpsURLConnection.setRequestMethod("POST");
            httpsURLConnection.setDoOutput(true);
            httpsURLConnection.setRequestProperty("Content-Type", "application/json");
            httpsURLConnection.setRequestProperty("Accept", "application/json");
            OutputStream outputStream = httpsURLConnection.getOutputStream();

            Map<String,Object> payload = new HashMap<>();
            payload.put("accountId",accountId);
            outputStream.write(new ObjectMapper().convertValue(payload, JsonNode.class)
                    .toString().getBytes(StandardCharsets.UTF_8));
            outputStream.close();
            InputStream responseStream = httpsURLConnection.getInputStream();
            BufferedReader responseReader = new BufferedReader(new InputStreamReader(responseStream));

            String responseMessage = responseReader.lines().collect(Collectors.joining());
            Map<String, Object> result = new ObjectMapper().readValue(responseMessage, new TypeReference<Map<String, Object>>() {});
            return Double.parseDouble(String.valueOf(result.get("balance")));
        }catch (Exception e){
            return 0d;
        }
    }

    @CrossOrigin(origins = "*")
    @RequestMapping(
            value = "/getBalance",
            method = RequestMethod.POST,
            consumes = {"application/json"},
            produces = {"application/json"}
    )
    public Object getAccountBalance(@RequestBody Map<String, Object> payload) {
        try {
            return getAccountBalanceImpl(payload);
        } catch (Exception e) {
            return e;
        }
    }

    private AccountBalance getAccountBalanceImpl(Map<String, Object> payload) throws Exception {
        String accountId = (String) payload.get("billingAccountId");

        AccountBalance accountBalance = accBalanceRepository.getAccountBalanceByAccountId(accountId);

        if (accountBalance == null){
            accountBalance = new AccountBalance();
            accountBalance.accountId = accountId;
            accountBalance.newBalance = getBalanceFromOfbiz(accountId);
            accountBalance = accBalanceRepository.save(accountBalance);
        }

        return accountBalance;
    }

    @CrossOrigin(origins = "*")
    @RequestMapping(
            value = "/updateAccountBalance",
            method = RequestMethod.POST,
            consumes = {"application/json"},
            produces = {"application/json"}
    )
    public Object updateAccountBalance(@RequestBody Map<String, Object> payload) throws Exception {
        String accountId = (String) payload.get("accountId");
        double amount = Double.parseDouble(String.valueOf(payload.get("amount")));
        String txIdentifier = (String) payload.get("txIdentifier");
        String remark = (String) payload.get("remark");

        Map<String,Object> accountBalanceQueryData = new HashMap<>();
        accountBalanceQueryData.put("billingAccountId", accountId);
        return getBalanceUpdateResult(amount, txIdentifier, remark, accountBalanceQueryData);
    }

    private synchronized BalanceUpdateResult getBalanceUpdateResult(double amount, String txIdentifier, String remark, Map<String, Object> accountBalanceQueryData) throws Exception {
        AccountBalance accountBalance = getAccountBalanceImpl(accountBalanceQueryData);

        double maxNegativeBalance = 0.0; // retrieve from settings
        double prevBalance = accountBalance.newBalance;
        double newBalance = prevBalance + amount; ///if amount type decrease(-5) it won't be less than 0

        if (newBalance <= maxNegativeBalance) {
            String accountId = (String) accountBalanceQueryData.get("billingAccountId");
            AccountBalance accountBalanceNew = new AccountBalance();
            accountBalanceNew.accountId = accountId;
            accountBalanceNew.newBalance = getBalanceFromOfbiz(accountId);
            accountBalanceNew = accBalanceRepository.save(accountBalanceNew);
            newBalance = accountBalanceNew.newBalance;
        }

        if (newBalance <= maxNegativeBalance){
            BalanceUpdateError balanceUpdateError = new BalanceUpdateError("Insufficient Balance", newBalance);
            return BalanceUpdateResult.fromBalanceUpdateError(balanceUpdateError);
        } else {
            accountBalance.prevBalance = prevBalance;
            accountBalance.newBalance = newBalance;
            accountBalance.newAmount = amount;
            accountBalance.txIdentifier = txIdentifier;
            accountBalance.remark = remark;
            accBalanceRepository.save(accountBalance);
            BalanceUpdateResult balanceUpdateResult = BalanceUpdateResult.fromAccountBalance(accountBalance);
            CompletableFuture.runAsync(() -> {
                transactionHistoryRepository.save(TransactionHistory.fromAccountBalance(accountBalance));
                int transactionCount = transactionDeleteSegmentCounter.incrementAndGet();
                if (transactionCount >= segmentSizeForTransactionDelete){
                    transactionHistoryRepository.deleteTransactionHistory();
                    transactionDeleteSegmentCounter.set(0);
                }
            });
            return balanceUpdateResult;
        }
    }

}
