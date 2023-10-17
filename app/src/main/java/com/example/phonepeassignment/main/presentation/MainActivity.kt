package com.example.phonepeassignment.main.presentation

import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View.OnClickListener
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.core.view.children
import androidx.core.view.get
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import com.example.phonepeassignment.R
import com.example.phonepeassignment.databinding.ActivityMainBinding
import com.example.phonepeassignment.databinding.ItemInputBoxBinding
import com.example.phonepeassignment.main.domain.MainViewModel
import com.example.phonepeassignment.util.ImageLoader
import com.example.phonepeassignment.util.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.lang.StringBuilder

@AndroidEntryPoint
class MainActivity : FragmentActivity() {

    private val viewModel: MainViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding
    private var correctAnswer : String? = null
    private var userAnswer : StringBuilder = StringBuilder()
    private var currentPosition: Int = 0

    private val buttonClickListener = OnClickListener { v ->
        (v as? TextView)?.text?.let {
            if (currentPosition >= (correctAnswer?.length ?: 0))
                return@OnClickListener
            userAnswer.append(it)
            (binding.flowLayout[currentPosition++] as? TextView)?.text = it
            if (userAnswer.length == correctAnswer?.length) {
                viewModel.validateAnswer(userAnswer.toString())
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }

    private fun init() {
        // observe flow
        registerObservers()
    }

    private fun createButtonBoxes(answer: String) {
        binding.buttonsContainer.removeAllViews()
        val charArray = CharArray(20)
        var count = 0
        for (element in answer) {
            charArray[count] = element
            count++
        }
        while (count < 20)
            charArray[count++] = CharRange('A', 'Z').random()
        charArray.shuffle()

        for (i in charArray.indices)
            createSingleButtonBox(charArray[i].toString())
    }

    private fun createSingleButtonBox(value: String) {
        ItemInputBoxBinding.inflate(LayoutInflater.from(this@MainActivity), binding.root, false).root.apply {
            backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this@MainActivity, R.color.blackish))
            text = value
            binding.buttonsContainer.addView(this)
            setOnClickListener(buttonClickListener)
        }
    }

    private fun createInputBoxes(answer: String) {
        binding.flowLayout.removeAllViews()
        for (i in answer.indices) {
            createSingleInputBox()
        }
    }

    private fun createSingleInputBox() {
        ItemInputBoxBinding.inflate(LayoutInflater.from(this@MainActivity), binding.root, false).root.apply {
            binding.flowLayout.addView(this)
        }
    }

    private fun clearAllInputBoxes() {
        userAnswer.clear()
        currentPosition = 0
        binding.flowLayout.children.forEach {
            (it as? TextView)?.text = ""
        }
    }

    private fun registerObservers() {
        lifecycleScope.launch(Dispatchers.Main) {
            viewModel.logoModelFlow.collectLatest { resource ->
                when (resource) {
                    is Resource.Error -> {
                        Toast.makeText(this@MainActivity, resource.error, Toast.LENGTH_LONG).show()
                    }
                    is Resource.Success -> {
                        Log.e("PARAS", resource.data.toString())
                        ImageLoader.setImage(
                            imageView = binding.imageView,
                            url = resource.data.imgUrl
                        )
                        correctAnswer = resource.data.name
                        createInputBoxes(resource.data.name)
                        createButtonBoxes(resource.data.name)
                    }
                    else -> {

                    }
                }
            }
        }

        lifecycleScope.launch(Dispatchers.Main) {
            viewModel.validationFlow.collectLatest { validationMessage ->
                clearAllInputBoxes()
                currentPosition = 0
                validationMessage?.let {
                    Toast.makeText(this@MainActivity, it, Toast.LENGTH_LONG).show()
                }
            }
        }
    }


}