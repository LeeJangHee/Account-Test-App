package com.example.account_test_app

import android.accounts.Account
import android.accounts.AccountAuthenticatorResponse
import android.accounts.AccountManager
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.ContactsContract
import android.text.Editable
import android.util.Log
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivityResultRegistryOwner
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.account_test_app.authenticator.AccountAuthenticatorAppCompatActivity
import com.example.account_test_app.ui.theme.AccountTestAppTheme

class LoginActivity : AccountAuthenticatorAppCompatActivity() {
    private val TAG = this.javaClass.simpleName
    private var response: AccountAuthenticatorResponse? = null

    private val viewModel by viewModels<LoginViewModel>()
    override fun onCreate(icicle: Bundle?) {
        super.onCreate(icicle)

        response = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, AccountAuthenticatorResponse::class.java)
        } else {
            intent.getParcelableExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE)
        }

        setContent {
            val context = LocalContext.current

            val idRemember by remember { viewModel.idState }
            val pwRemember by remember { viewModel.pwState }
            AccountTestAppTheme {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    EmailEditText(vm = viewModel)
                    PasswordEditText(vm = viewModel)
                    AccountButton(email = idRemember, password = pwRemember) {
                        if (idRemember.isEmpty() || pwRemember.isEmpty()) {
                            Toast.makeText(context, "fill id and password", Toast.LENGTH_SHORT).show()
                            return@AccountButton
                        }

                        val accountManager = AccountManager.get(context)
                        val account = Account(idRemember, Constants.ACCOUNT_TYPE)
                        Log.i(TAG, "accountManager.addAccountExplicitly!!")
                        accountManager.addAccountExplicitly(account, pwRemember, null)

                        Log.i(TAG, "accountManager.setSyncAutomatically!!")
                        ContentResolver.setSyncAutomatically(account, ContactsContract.AUTHORITY, true)

                        val intent = Intent().apply {
                            putExtra(AccountManager.KEY_ACCOUNT_NAME, idRemember)
                            putExtra(AccountManager.KEY_ACCOUNT_TYPE, Constants.ACCOUNT_TYPE)
                        }

                        intent.extras?.let { extras -> setAccountAuthenticatorResult(extras) }
                        setResult(RESULT_OK, intent)
                        finish()
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmailEditText(
    vm: LoginViewModel
) {
    TextField(
        value = vm.idState.value,
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        onValueChange = {
            vm.setIdState(it)
        },
        placeholder = {
            Text(
                text = "Id",
                style = TextStyle(
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            )
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordEditText(
    vm: LoginViewModel
) {
    TextField(
        value = vm.pwState.value,
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        onValueChange = {
            vm.setPwState(it)
        },
        placeholder = {
            Text(
                text = "password",
                style = TextStyle(
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            )
        }
    )
}

@Composable
fun AccountButton(
    email: String,
    password: String,
    onClick: () -> Unit
) {
    Button(onClick = onClick) {
        Text(
            text = "Login",
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            style = TextStyle(
                fontSize = 16.sp,
                fontFamily = FontFamily.Serif,
                color = Color.Magenta
            )
        )
    }
}