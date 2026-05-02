package com.inheritx.customedittext

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.switchmaterial.SwitchMaterial
import com.inheritx.standardedittext.CustomFrameLayout
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val viewModel: ShowcaseViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupStaticExamples()
        setupPlayground()
        observeViewModel()
    }

    private fun setupStaticExamples() {
        val errorField: CustomFrameLayout = findViewById(R.id.error_example_layout)
        errorField.setError("This email is already taken.", false)
    }

    private fun setupPlayground() {
        findViewById<SwitchMaterial>(R.id.switch_error).setOnCheckedChangeListener { _, isChecked ->
            viewModel.toggleError(isChecked)
        }

        findViewById<SwitchMaterial>(R.id.switch_spacing).setOnCheckedChangeListener { _, isChecked ->
            viewModel.toggleSpacing(isChecked)
        }

        findViewById<SwitchMaterial>(R.id.switch_enabled).setOnCheckedChangeListener { _, isChecked ->
            viewModel.toggleEnabled(isChecked)
        }
    }

    private fun observeViewModel() {
        val playgroundField: CustomFrameLayout = findViewById(R.id.playground_field)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.playgroundState.collect { state ->
                    // Update field properties
                    playgroundField.setLabelText(state.labelText)
                    playgroundField.setHelperText(state.helperText)
                    playgroundField.setUseSpacing(state.useSpacing)
                    playgroundField.setEnabled(state.isEnabled)
                    
                    if (state.hasError) {
                        playgroundField.setError("Demonstration error active", false)
                    } else {
                        playgroundField.removeError()
                    }
                }
            }
        }
    }
}