package com.example.account_test_app

import android.Manifest
import android.accounts.AccountManager
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.account_test_app.ui.theme.AccountTestAppTheme
import com.example.account_test_app.ui.theme.Purple80
import com.google.gson.GsonBuilder

private val gson = GsonBuilder()
    .setPrettyPrinting()
    .create()
const val TAG = "ljhtest"

class MainActivity : ComponentActivity() {

    private val permissionList = arrayOf(
        Manifest.permission.GET_ACCOUNTS,
        Manifest.permission.READ_CONTACTS
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val textState = mutableStateOf<String?>(null)
        val permissionFlag = mutableStateOf<Boolean?>(null)

        setContent {
            val context = LocalContext.current
            val launch = rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestMultiplePermissions()) { map ->
                val areGranted = map.values.reduce { acc, next -> acc && next }
                permissionFlag.value = areGranted

                if (areGranted) {
                    val am = AccountManager.get(context)
                    val sb = StringBuilder()
                        .appendLine("<< Accounts >>")
                        .appendLine(am.accounts.joinToString { gson.toJson(it) })
                    Log.e(TAG, "accounts: ${gson.toJson(am.accounts)}")
                    textState.value = sb.toString()
                }
            }

            fun onClickEvent() {
                if (permissionFlag.value != null && permissionFlag.value == true) {
                    val am = AccountManager.get(context)
                    val sb = StringBuilder()
                        .appendLine("<< Accounts >>")
                        .appendLine(am.accounts.joinToString { gson.toJson(it) })
                    Log.e(TAG, "accounts: ${gson.toJson(am.accounts)}")
                    textState.value = sb.toString()
                } else {
                    val flag = permissionList.all { ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED }

                    permissionFlag.value = flag

                    if (!flag) {
                        launch.launch(permissionList)
                    } else {
                        onClickEvent()
                    }
                }
            }

            AccountTestAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    Column(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Button(
                            modifier = Modifier.wrapContentWidth(),
                            onClick = ::onClickEvent
                        ) {
                            Text(text = "Get Account Info")
                        }

                        Text(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(8.dp)
                                .border(1.dp, color = Purple80)
                                .verticalScroll(state = rememberScrollState())
                                .padding(vertical = 4.dp, horizontal = 8.dp),
                            text = textState.value ?: "null",
                            style = TextStyle(
                                color = Color.Black,
                                fontSize = 14.sp,
                                fontFamily = FontFamily.SansSerif
                            )
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(
    showBackground = true
)
@Composable
fun GreetingPreview() {
    AccountTestAppTheme {
        Greeting("Android")
    }
}