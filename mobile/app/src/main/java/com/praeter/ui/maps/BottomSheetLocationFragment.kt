package com.praeter.ui.maps

import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.praeter.databinding.FragmentBottomLocationBinding
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class BottomSheetLocationFragment : BottomSheetDialogFragment() {

    companion object {
        const val TAG = "BottomSheetLocationFragment"

        private const val LOCATION_DATA = "location_data"

        fun newInstance(location: Location): BottomSheetLocationFragment {
            val args = Bundle()
            args.putParcelable(LOCATION_DATA, location)

            val fragment = BottomSheetLocationFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun getTheme(): Int {
        return android.R.style.Theme_Translucent
    }

    private var _viewBinding: FragmentBottomLocationBinding? = null
    private val binding get() = _viewBinding!!

    private val mViewModel: LocationViewModel by viewModels()

    private var location: Location? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val extras = this.arguments

        if (null != extras) {
            location = extras.getParcelable(LOCATION_DATA)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _viewBinding = FragmentBottomLocationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnCloseBottomSheet.setOnClickListener {
            this.dismissAllowingStateLoss()
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initViewModelsObservers()

        if (null != location) {
            binding.currentLocation = location

            location?.let {
                mViewModel.getAddressFromLocation(requireContext(), it)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _viewBinding
    }

    private fun initViewModelsObservers() {
        mViewModel.getAddress().observe(requireActivity(), {
            Timber.e("getAddress().observe - ${it.toString()}")
            binding.currentAddress = it
        })
    }
}