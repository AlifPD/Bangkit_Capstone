package capstoneproject.mediscan.view

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import capstoneproject.mediscan.data.MainViewModel
import capstoneproject.mediscan.data.ViewModelFactory
import capstoneproject.mediscan.data.local.UserPreferences
import capstoneproject.mediscan.databinding.ActivityLoginBinding

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "session")

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        val viewModel = ViewModelProvider(this,
            ViewModelFactory(UserPreferences.getInstance(dataStore)))[MainViewModel::class.java]

        viewModel.isLoading.observe(this) { showLoading(it) }

        binding.loginButton.setOnClickListener {
            val username = binding.usernameLoginEdittext.text.toString()
            val password = binding.passwordLoginEdittext.text.toString()

            viewModel.loginUser(username, password)
            viewModel.isLoggedIn.observe(this) {
                if (it) {
                    val intent = Intent(this, HomeActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    intent.putExtra(HomeActivity.LOGIN_FLAG, true)

                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(
                        this,
                        "Harap Coba Lagi",
                        Toast.LENGTH_SHORT
                    ).show()
                    binding.usernameLoginEdittext.text?.clear()
                    binding.passwordLoginEdittext.text?.clear()
                }
            }
        }
        binding.redirectSignup.setOnClickListener {
            startActivity(Intent(this, SignupActivity::class.java))
        }
    }

    private fun showLoading(b: Boolean) {
        if (b) {
            binding.loadingLogin.visibility = View.VISIBLE
        } else {
            binding.loadingLogin.visibility = View.GONE
        }
    }
}