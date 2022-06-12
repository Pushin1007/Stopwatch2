package com.pd.stopwatch.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.pd.stopwatch.MAIN_VIEW_MODEL
import com.pd.stopwatch.STOPWATCH_LIST_ORCHESTRATOR

import com.pd.stopwatch.databinding.FragmentMainBinding
import com.pd.stopwatch.model.StopwatchListOrchestrator
import com.pd.stopwatch.viewmodel.MainViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.qualifier.named


class MainFragment : Fragment() {
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    val viewModel: MainViewModel by viewModel(named(MAIN_VIEW_MODEL))

    private var stopwatchListOrchestrator: StopwatchListOrchestrator? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentMainBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.liveData.observe(viewLifecycleOwner) {
            stopwatchListOrchestrator = it
        }

        CoroutineScope(
            Dispatchers.Main
                    + SupervisorJob()
        ).launch {
            stopwatchListOrchestrator?.ticker?.collect {
                binding.textTime.text = it
            }
        }

        with(binding) {
            buttonStart.setOnClickListener {
                stopwatchListOrchestrator?.start()
            }
            buttonPause.setOnClickListener {
                stopwatchListOrchestrator?.pause()
            }
            buttonStop.setOnClickListener {
                stopwatchListOrchestrator?.stop()
            }
        }
    }

    companion object {
        fun newInstance() = MainFragment()
    }
}