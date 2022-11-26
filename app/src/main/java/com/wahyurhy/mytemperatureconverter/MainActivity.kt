package com.wahyurhy.mytemperatureconverter

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.wahyurhy.mytemperatureconverter.ui.theme.MyTemperatureConverterTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyTemperatureConverterTheme {
                MyTemperatureConverterApp()
            }
        }
    }
}

@Composable
fun MyTemperatureConverterApp() {
    // A surface container using the 'background' color from the theme
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colors.background
    ) {
        Column {
            StatefulTemperatureInput()
            ConverterApp()
            TwoWayConverterApp()
        }
    }
}

@Composable
fun StatefulTemperatureInput(modifier: Modifier = Modifier) {
    var input by rememberSaveable { mutableStateOf("") }
    var output by rememberSaveable { mutableStateOf("") }
    Column(modifier.padding(16.dp)) {
        Text(
            text = stringResource(id = R.string.stateful_converter),
            style = MaterialTheme.typography.h5
        )
        OutlinedTextField(
            value = input,
            label = { Text(text = stringResource(id = R.string.enter_celsius)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            onValueChange = { newInput ->
                input = newInput
                output = convertToFahrenheit(newInput)
            }
        )
        Text(text = stringResource(id = R.string.temperature_fahrenheit, output))
    }

}

@Composable
fun StatelessTemperatureInput(
    input: String,
    output: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier.padding(16.dp)) {
        Text(
            text = stringResource(id = R.string.stateless_converter),
            style = MaterialTheme.typography.h5
        )
        OutlinedTextField(
            value = input,
            label = { Text(text = stringResource(id = R.string.enter_celsius)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            onValueChange = onValueChange,
        )
        Text(text = stringResource(id = R.string.temperature_fahrenheit, output))
    }

}

@Composable
fun ConverterApp(modifier: Modifier = Modifier) {
    var input by rememberSaveable { mutableStateOf("") }
    var output by rememberSaveable { mutableStateOf("") }
    Column(modifier) {
        StatelessTemperatureInput(input = input, output = output, onValueChange = { newInput ->
            input = newInput
            output = convertToFahrenheit(newInput)
        })
    }
}

@Composable
fun GeneralTemperatureInput(
    scale: Scale,
    input: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier) {
        OutlinedTextField(
            value = input,
            label = {
                Text(
                    text = stringResource(
                        id = R.string.enter_temperature,
                        scale.scaleName
                    )
                )
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            onValueChange = onValueChange
        )
    }
}

@Composable
fun TwoWayConverterApp(modifier: Modifier = Modifier) {
    var celcius by rememberSaveable { mutableStateOf("") }
    var fahrenheit by rememberSaveable { mutableStateOf("") }
    Column(modifier.padding(16.dp)) {
        Text(
            text = stringResource(id = R.string.two_way_converter),
            style = MaterialTheme.typography.h5
        )
    }
    GeneralTemperatureInput(scale = Scale.CELCILUS, input = celcius, onValueChange = { newInput ->
        celcius = newInput
        fahrenheit = convertToFahrenheit(newInput)
    })
    GeneralTemperatureInput(scale = Scale.FAHRENHEIT, input = fahrenheit, onValueChange = { newInput ->
        fahrenheit = newInput
        celcius = convertToCelcius(newInput)
    })

}

fun convertToCelcius(fahrenheit: String) =
    fahrenheit.toDoubleOrNull()?.let {
        (it - 32) * 5 / 9
    }.toString()

fun convertToFahrenheit(celcius: String) =
    celcius.toDoubleOrNull()?.let {
        (it * 9 / 5) + 32
    }.toString()

enum class Scale(val scaleName: String) {
    CELCILUS("Celcilus"),
    FAHRENHEIT("Fahrenheit")
}
