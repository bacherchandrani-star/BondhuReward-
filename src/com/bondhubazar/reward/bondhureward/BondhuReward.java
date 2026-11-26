
package com.bondhubazar.reward.bondhureward;

// App Inventor প্রয়োজনীয় ক্লাস
import com.google.appinventor.components.annotations.Extension;
import com.google.appinventor.components.annotations.SimpleFunction;
import com.google.appinventor.components.annotations.SimpleEvent;

import com.google.appinventor.components.runtime.AndroidNonvisibleComponent;
import com.google.appinventor.components.runtime.ComponentContainer;
import com.google.appinventor.components.runtime.EventDispatcher;

// Extension পরিচয়
@Extension(
    description = "Bondhu Bazar Reward and Wallet Manager"
)

public class BondhuReward extends AndroidNonvisibleComponent {

    // মোট Reward Points
    private int rewardPoints = 0;

    // Wallet Balance পয়সায় রাখা হবে
    // যেমন 10000 = 100 টাকা
    private long walletBalance = 0;

    // Constructor
    public BondhuReward(ComponentContainer container) {
        super(container.$form());
    }

    // ==================================
    // ১. Reward Points যোগ করা
    // ==================================

    @SimpleFunction(
        description = "Add reward points"
    )
    public boolean AddPoints(int points) {

        if (points <= 0) {
            ErrorOccurred("Points must be greater than zero");
            return false;
        }

        // Integer overflow এড়ানো
        if (rewardPoints > Integer.MAX_VALUE - points) {
            ErrorOccurred("Points limit exceeded");
            return false;
        }

        rewardPoints += points;

        PointsUpdated(rewardPoints);

        return true;
    }

    // ==================================
    // ২. Reward Points ব্যবহার করা
    // ==================================

    @SimpleFunction(
        description = "Redeem reward points"
    )
    public boolean RedeemPoints(int points) {

        if (points <= 0) {
            ErrorOccurred("Invalid points");
            return false;
        }

        if (points > rewardPoints) {
            ErrorOccurred("Not enough reward points");
            return false;
        }

        rewardPoints -= points;

        PointsUpdated(rewardPoints);

        return true;
    }

    // ==================================
    // ৩. মোট Points দেখা
    // ==================================

    @SimpleFunction(
        description = "Get total reward points"
    )
    public int GetPoints() {

        return rewardPoints;
    }

    // ==================================
    // ৪. Wallet-এ টাকা যোগ করা
    // ==================================

    @SimpleFunction(
        description = "Add money to wallet in rupees"
    )
    public boolean AddWalletMoney(double amount) {

        if (Double.isNaN(amount) ||
            Double.isInfinite(amount) ||
            amount <= 0 ||
            amount > 1000000000000.0) {

            ErrorOccurred("Invalid wallet amount");
            return false;
        }

        // টাকা থেকে পয়সায় রূপান্তর
        long paise = Math.round(amount * 100.0);

        if (paise <= 0) {
            ErrorOccurred("Amount is too small");
            return false;
        }

        if (walletBalance > Long.MAX_VALUE - paise) {
            ErrorOccurred("Wallet limit exceeded");
            return false;
        }

        walletBalance += paise;

        WalletUpdated(GetWalletBalance());

        return true;
    }

    // ==================================
    // ৫. Wallet থেকে টাকা ব্যবহার
    // ==================================

    @SimpleFunction(
        description = "Spend wallet money in rupees"
    )
    public boolean SpendWalletMoney(double amount) {

        if (Double.isNaN(amount) ||
            Double.isInfinite(amount) ||
            amount <= 0 ||
            amount > 1000000000000.0) {

            ErrorOccurred("Invalid amount");
            return false;
        }

        long paise = Math.round(amount * 100.0);

        if (paise <= 0) {
            ErrorOccurred("Amount is too small");
            return false;
        }

        if (paise > walletBalance) {
            ErrorOccurred("Insufficient wallet balance");
            return false;
        }

        walletBalance -= paise;

        WalletUpdated(GetWalletBalance());

        return true;
    }

    // ==================================
    // ৬. Wallet Balance দেখা
    // ==================================

    @SimpleFunction(
        description = "Get wallet balance in rupees"
    )
    public double GetWalletBalance() {

        return walletBalance / 100.0;
    }

    // ==================================
    // ৭. Wallet Balance পয়সায় দেখা
    // ==================================

    @SimpleFunction(
        description = "Get wallet balance in paise"
    )
    public long GetWalletBalancePaise() {

        return walletBalance;
    }

    // ==================================
    // ৮. Points Reset
    // ==================================

    @SimpleFunction(
        description = "Reset reward points"
    )
    public void ResetPoints() {

        rewardPoints = 0;

        PointsUpdated(rewardPoints);
    }

    // ==================================
    // ৯. Wallet Reset
    // ==================================

    @SimpleFunction(
        description = "Reset wallet balance"
    )
    public void ResetWallet() {

        walletBalance = 0;

        WalletUpdated(0);
    }

    // ==================================
    // ১০. Points Updated Event
    // ==================================

    @SimpleEvent(
        description = "Triggered when reward points change"
    )
    public void PointsUpdated(int points) {

        EventDispatcher.dispatchEvent(
            this,
            "PointsUpdated",
            points
        );
    }

    // ==================================
    // ১১. Wallet Updated Event
    // ==================================

    @SimpleEvent(
        description = "Triggered when wallet changes"
    )
    public void WalletUpdated(double balance) {

        EventDispatcher.dispatchEvent(
            this,
            "WalletUpdated",
            balance
        );
    }

    // ==================================
    // ১২. Error Event
    // ==================================

    @SimpleEvent(
        description = "Triggered when an operation fails"
    )
    public void ErrorOccurred(String message) {

        EventDispatcher.dispatchEvent(
            this,
            "ErrorOccurred",
            message
        );
    }
}
