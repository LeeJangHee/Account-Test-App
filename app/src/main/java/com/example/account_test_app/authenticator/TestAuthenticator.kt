package com.example.account_test_app.authenticator

import android.accounts.AbstractAccountAuthenticator
import android.accounts.Account
import android.accounts.AccountAuthenticatorResponse
import android.accounts.AccountManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.core.os.bundleOf
import com.example.account_test_app.LoginActivity
import java.lang.UnsupportedOperationException

class TestAuthenticator(private val context: Context) : AbstractAccountAuthenticator(context) {
    private val TAG = this.javaClass.simpleName

    override fun editProperties(response: AccountAuthenticatorResponse?, accountType: String?): Bundle {
        Log.e(TAG, "editProperties() called with: response = $response, accountType = $accountType")
        throw UnsupportedOperationException()
    }

    override fun addAccount(
        response: AccountAuthenticatorResponse?,
        accountType: String?,
        authTokenType: String?,
        requiredFeatures: Array<out String>?,
        options: Bundle?
    ): Bundle? {
        Log.e(
            TAG,
            "addAccount() called with: response = $response, accountType = $accountType, authTokenType = $authTokenType, requiredFeatures = $requiredFeatures, options = $options"
        )
        // need extra business logic for `intent`
        val intent = Intent(context, LoginActivity::class.java).apply {
            putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response)
        }

        return Bundle().apply { putParcelable(AccountManager.KEY_INTENT, intent) }
    }

    override fun confirmCredentials(
        response: AccountAuthenticatorResponse?,
        account: Account?,
        options: Bundle?
    ): Bundle {
        Log.e(TAG, "confirmCredentials() called with: response = $response, account = $account, options = $options")

        return bundleOf()
    }

    override fun getAuthToken(
        response: AccountAuthenticatorResponse?,
        account: Account?,
        authTokenType: String?,
        options: Bundle?
    ): Bundle {
        Log.e(TAG, "getAuthToken() called with: response = $response, account = $account, authTokenType = $authTokenType, options = $options")

        return bundleOf()
    }

    override fun getAuthTokenLabel(authTokenType: String?): String {
        return authTokenType ?: "Null Type"
    }

    override fun updateCredentials(
        response: AccountAuthenticatorResponse?,
        account: Account?,
        authTokenType: String?,
        options: Bundle?
    ): Bundle? {
        Log.e(TAG, "updateCredentials() called with: response = $response, account = $account, authTokenType = $authTokenType, options = $options")

        return null
    }

    override fun hasFeatures(response: AccountAuthenticatorResponse?, account: Account?, features: Array<out String>?): Bundle {
        Log.e(TAG, "hasFeatures() called with: response = $response, account = $account, features = $features")

        return bundleOf(AccountManager.KEY_BOOLEAN_RESULT to false)
    }
}