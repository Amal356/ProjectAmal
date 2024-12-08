package com.example.myamal

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myamal.tp5.AmalDBViewModel
import com.example.myamal.tp5.DatabaseHelper
import com.example.myamal.tp5.Product
import java.io.File


// Trendy colors with gradient effects
val TrendPurple = Color(0xFF8E44AD)
val TrendPink = Color(0xFFE91E63)
val TrendWhite = Color(0xFFF7F7F7)

class MainActivity : ComponentActivity() {

    private val viewModel: AmalDBViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContent {
            MainScreen(viewModel)
        }
    }
}

fun saveUserData(context: Context, name: String, email: String, password: String) {
    val userData = "Name: $name\nEmail: $email\nPassword: $password"
    val file = File(context.filesDir, "userData.txt")
    file.writeText(userData)
}

@Composable
fun MainScreen(viewModel: AmalDBViewModel) {
    var currentScreen by remember { mutableStateOf("Login") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        TrendPurple,
                        TrendPink,
                        TrendWhite
                    ) // Trendy background gradient
                )
            )
    ) {
        when (currentScreen) {
            "Login" -> LoginScreen(
                onLoginSuccess = { currentScreen = "Home" },
                onForgotPasswordClick = { currentScreen = "ResetPassword" },
                onSignUpClick = { currentScreen = "SignUp" }
            )

            "Home" -> HomeScreen(viewModel, onLogoutClick = { currentScreen = "Login" })
            "ResetPassword" -> ResetPasswordScreen(
                onBackClick = { currentScreen = "Login" }
            )

            "SignUp" -> SignUpScreen(
                onBackClick = { currentScreen = "Login" }
            )
        }
    }
}

// Login Screen with trendy gradient background
@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onForgotPasswordClick: () -> Unit,
    onSignUpClick: () -> Unit,
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var emailError by remember { mutableStateOf(false) }
    var passwordError by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        BasicTextField(
            value = email,
            onValueChange = {
                email = it
                emailError = false
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(TrendPurple, TrendPink, TrendWhite)
                    )
                )
                .padding(16.dp),
            singleLine = true,
            decorationBox = { innerTextField ->
                Box(modifier = Modifier.fillMaxWidth()) {
                    if (email.isEmpty()) {
                        Text("Email", fontSize = 20.sp, color = Color.White)
                    }
                    innerTextField()
                }
            }
        )
        if (emailError) {
            Text("Invalid email format", color = Color.White, fontSize = 16.sp)
        }

        BasicTextField(
            value = password,
            onValueChange = {
                password = it
                passwordError = false
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(TrendPurple, TrendPink, TrendWhite)
                    )
                )
                .padding(16.dp),
            singleLine = true,
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            decorationBox = { innerTextField ->
                Box(modifier = Modifier.fillMaxWidth()) {
                    if (password.isEmpty()) {
                        Text("Password", fontSize = 20.sp, color = Color.White)
                    }
                    innerTextField()
                }
            }
        )
        if (passwordError) {
            Text("Password cannot be empty", color = Color.White, fontSize = 16.sp)
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(checked = passwordVisible, onCheckedChange = { passwordVisible = it })
            Text("Show Password", color = Color.White)
        }

        Button(
            onClick = {
                /*emailError = !isValidEmail(email)
                passwordError = password.isEmpty()
                if (!emailError && !passwordError) {
                    onLoginSuccess()
                }*/
                onLoginSuccess()
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = TrendPink)
        ) {
            Text("Log In", fontSize = 20.sp, color = Color.Black)
        }

        TextButton(onClick = onSignUpClick) {
            Text("Sign Up", color = Color.White, fontSize = 18.sp)
        }

        TextButton(onClick = onForgotPasswordClick) {
            Text("Forgot Password?", color = Color.White, fontSize = 18.sp)
        }
    }
}

@Composable
fun HomeScreen(viewModel: AmalDBViewModel, onLogoutClick: () -> Unit) {
    val context = LocalContext.current
    val dbHelper = DatabaseHelper(context)

    // Add products to the database
    val productsToAdd = listOf(
        Product(
            name = "Jean",
            description = "Jean en denim délavé",
            image = painterResource(id = R.drawable.product_image_1).toString()
        ),
        Product(
            name = "Sweat-shirt",
            description = "Sweat-shirt femme décontracté à col ras",
            image = painterResource(id = R.drawable.product_image_2).toString()
        ),
        Product(
            name = "Baskets décontractées",
            description = "WARRIOR Baskets décontractées à semelle épaisse",
            image = painterResource(id = R.drawable.product_image_3).toString()
        ),
    )

    // Insert products into the database
    /*productsToAdd.forEach { product ->
        dbHelper.addProduct(product)
    }*/

    // Retrieve products from the database
    viewModel.getProductsList(dbHelper)
    val productList by viewModel.productsList.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Welcome to Shop", fontSize = 24.sp, color = Color.White)

        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(productList) { product ->
                ProductCard(viewModel, dbHelper = dbHelper, product = product)
            }
        }

        Button(
            onClick = onLogoutClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = TrendPurple)
        ) {
            Text("Logout", fontSize = 20.sp, color = Color.Black)
        }
    }
}


// Product Card with trendy gradient background
@Composable
fun ProductCard(viewModel: AmalDBViewModel, dbHelper: DatabaseHelper, product: Product) {
    var showDescription by remember { mutableStateOf(false) }
    var edit by remember { mutableStateOf(false) }
    var editedText by remember { mutableStateOf(product.name) }

    val context = LocalContext.current

    val imageList = mutableListOf(
        R.drawable.product_image_1, R.drawable.product_image_2,
        R.drawable.product_image_3, R.drawable.product_image_4,
        R.drawable.product_image_5, R.drawable.product_image_6
    )

    val randomImage = imageList.random()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.elevatedCardElevation(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                modifier = Modifier.size(60.dp),
                onClick = {
                    product.id?.let {
                        val isDeleted = dbHelper.deleteDB(it)
                        if (isDeleted){
                            Toast.makeText(context, "Product deleted successfully!", Toast.LENGTH_SHORT).show()
                            viewModel.getProductsList(dbHelper)
                        }

                    }
                }
            ) {
                Icon(imageVector = Icons.Default.Delete, contentDescription = null)
            }
            IconButton(
                modifier = Modifier.size(60.dp),
                onClick = {
                    edit = !edit
                }
            ) {
                Icon(imageVector = Icons.Default.Edit, contentDescription = null)
            }
        }
        Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = randomImage), // Replace with actual image resource or URL
                contentDescription = product.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(product.name, fontSize = 20.sp, color = Color.Black)

            Spacer(modifier = Modifier.height(8.dp))

            if (edit){
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    OutlinedTextField(
                        modifier = Modifier
                            .weight(.7f),
                        value = editedText,
                        onValueChange = {
                            editedText = it
                        }
                    )
                    Button(
                        modifier = Modifier.weight(.3f),
                        onClick = {
                            product.id?.let {
                                val isEdited = dbHelper.updateDB(editedText, it)
                                if (isEdited){
                                    edit = false
                                    Toast.makeText(context, "Product updated successfully!", Toast.LENGTH_SHORT).show()
                                    viewModel.getProductsList(dbHelper)
                                }
                            }
                        }
                    ) {
                        Text("Edit")
                    }
                }

            }


            Button(onClick = { showDescription = !showDescription }) {
                Text(if (showDescription) "Hide Description" else "Show Description")
            }


            if (showDescription) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(product.description, fontSize = 16.sp, color = Color.Black)
            }
        }
    }
}


// Reset Password Screen with trendy gradient background
@Composable
fun ResetPasswordScreen(onBackClick: () -> Unit) {
    var email by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf(false) }
    var emailSent by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Reset Password", fontSize = 30.sp, color = Color.White)

        if (!emailSent) {
            BasicTextField(
                value = email,
                onValueChange = {
                    email = it
                    emailError = false
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(TrendPurple, TrendPink, TrendWhite)
                        )
                    )
                    .padding(16.dp),
                singleLine = true,
                decorationBox = { innerTextField ->
                    Box(modifier = Modifier.fillMaxWidth()) {
                        if (email.isEmpty()) {
                            Text("Email", fontSize = 20.sp, color = Color.White)
                        }
                        innerTextField()
                    }
                }
            )
            if (emailError) {
                Text("Invalid email format", color = Color.White, fontSize = 16.sp)
            }

            Button(
                onClick = {
                    emailError = !isValidEmail(email)
                    if (!emailError) {
                        emailSent = true
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = TrendPink)
            ) {
                Text("Send Reset Email", fontSize = 20.sp, color = Color.Black)
            }
        } else {
            Text("Email sent! Please check your inbox.", color = Color.White, fontSize = 20.sp)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onBackClick,
            colors = ButtonDefaults.buttonColors(containerColor = TrendPurple)
        ) {
            Text("Back to Login", fontSize = 20.sp, color = Color.Black)
        }
    }
}

@Composable
fun SignUpScreen(onBackClick: () -> Unit) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var emailError by remember { mutableStateOf(false) }
    var passwordError by remember { mutableStateOf(false) }
    var confirmPasswordError by remember { mutableStateOf(false) }
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Sign Up", fontSize = 30.sp, color = Color.White)

        BasicTextField(
            value = name,
            onValueChange = { name = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(TrendPurple, TrendPink, TrendWhite)
                    )
                )
                .padding(16.dp),
            singleLine = true,
            decorationBox = { innerTextField ->
                Box(modifier = Modifier.fillMaxWidth()) {
                    if (name.isEmpty()) {
                        Text("Full Name", fontSize = 20.sp, color = Color.White)
                    }
                    innerTextField()
                }
            }
        )

        BasicTextField(
            value = email,
            onValueChange = {
                email = it
                emailError = false
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(TrendPurple, TrendPink, TrendWhite)
                    )
                )
                .padding(16.dp),
            singleLine = true,
            decorationBox = { innerTextField ->
                Box(modifier = Modifier.fillMaxWidth()) {
                    if (email.isEmpty()) {
                        Text("Email", fontSize = 20.sp, color = Color.White)
                    }
                    innerTextField()
                }
            }
        )
        if (emailError) {
            Text("Invalid email format", color = Color.White, fontSize = 16.sp)
        }

        BasicTextField(
            value = password,
            onValueChange = {
                password = it
                passwordError = false
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(TrendPurple, TrendPink, TrendWhite)
                    )
                )
                .padding(16.dp),
            singleLine = true,
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            decorationBox = { innerTextField ->
                Box(modifier = Modifier.fillMaxWidth()) {
                    if (password.isEmpty()) {
                        Text("Password", fontSize = 20.sp, color = Color.White)
                    }
                    innerTextField()
                }
            }
        )
        if (passwordError) {
            Text("Password cannot be empty", color = Color.White, fontSize = 16.sp)
        }

        BasicTextField(
            value = confirmPassword,
            onValueChange = {
                confirmPassword = it
                confirmPasswordError = false
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(TrendPurple, TrendPink, TrendWhite)
                    )
                )
                .padding(16.dp),
            singleLine = true,
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            decorationBox = { innerTextField ->
                Box(modifier = Modifier.fillMaxWidth()) {
                    if (confirmPassword.isEmpty()) {
                        Text("Confirm Password", fontSize = 20.sp, color = Color.White)
                    }
                    innerTextField()
                }
            }
        )
        if (confirmPasswordError) {
            Text("Passwords do not match", color = Color.White, fontSize = 16.sp)
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(checked = passwordVisible, onCheckedChange = { passwordVisible = it })
            Text("Show Password", color = Color.White)
        }

        Button(
            onClick = {
                emailError = !isValidEmail(email)
                passwordError = password.isEmpty()
                confirmPasswordError = confirmPassword != password
                if (!emailError && !passwordError && !confirmPasswordError) {
                    saveUserData(context, name, email, password)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = TrendPink)
        ) {
            Text("Sign Up", fontSize = 20.sp, color = Color.Black)
        }

        TextButton(onClick = onBackClick) {
            Text("Back to Login", color = Color.White, fontSize = 18.sp)
        }
    }
}

// Helper function to validate email format
fun isValidEmail(email: String): Boolean {
    return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {

}
