package com.praeter.ui.mainactivity.fragment.bottomfragment

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.praeter.R
import com.praeter.core.utils.PraeterNetworkManager
import com.praeter.data.local.model.Ancient
import com.praeter.data.local.model.ClassModel
import com.praeter.databinding.FragmentBottomSheetDialogBinding
import jp.wasabeef.glide.transformations.BlurTransformation
import timber.log.Timber

class BottomSheetFragment : BottomSheetDialogFragment() {

    private var _viewBinding: FragmentBottomSheetDialogBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _viewBinding!!

    private var storedClass: Any? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _viewBinding = FragmentBottomSheetDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Timber.i("onViewCreated()")

        //Retrieve data
        classData
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _viewBinding = null
    }

    fun book(view: View) {
        Timber.i("onReserveButtonClicked()")
        if (!PraeterNetworkManager.isConnected(requireContext())) {
            val errorMessage = "Not connected to the internet. Check your internet connection."
            Timber.e(errorMessage)
            Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
            return
        }
        val message = "You've successfully reserved the class " +
                if (storedClass is ClassModel) (storedClass as ClassModel?)?.name else (storedClass as Ancient?)?.name
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    fun close(view: View) {
        Timber.i("onCloseButtonClicked()")
        dismiss()
    }

    private val classData: Unit
        get() {
            Timber.i("getClassData()")
            assert(arguments != null)

            if (null == arguments?.getParcelable(CLASS_ITEM_BUNDLE)) {
                Timber.e("Bundle with key : CLASS_ITEM_BUNDLE, is null ")
                return
            }

            storedClass = arguments?.getParcelable(CLASS_ITEM_BUNDLE)

            if (storedClass is ClassModel) setViews((storedClass as ClassModel?)!!) else if (storedClass is Ancient) setViews(
                (storedClass as Ancient?)!!
            )
        }

    @SuppressLint("SetTextI18n")
    private fun setViews(classModelItem: ClassModel) {
        Glide.with(this)
            .load(R.drawable.visa_icon)
            .transform(
                BlurTransformation(75),
                RoundedCorners(48)
            )
            .into(binding.ivClassThumbBlurred)

        binding.tvName.text = classModelItem.name
        binding.tvType.text = classModelItem.type
    }

    @SuppressLint("SetTextI18n")
    private fun setViews(ancientItem: Ancient) {
        Glide.with(this)
            .load(R.drawable.visa_icon)
            .transform(
                BlurTransformation(75),
                RoundedCorners(48)
            )
            .into(binding.ivClassThumbBlurred)

        binding.tvName.text = ancientItem.name
        binding.tvType.text = ancientItem.name
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        Timber.e("onDismiss()")
    }

    companion object {
        const val CLASS_ITEM_BUNDLE = "CLASS_ITEM_BUNDLE"
        fun newInstance(): BottomSheetFragment {
            val args = Bundle()
            val fragment = BottomSheetFragment()
            fragment.arguments = args
            return fragment
        }

        fun newInstance(classModelItem: ClassModel?): BottomSheetFragment {
            val args = Bundle()
            args.putParcelable(CLASS_ITEM_BUNDLE, classModelItem)
            val fragment = BottomSheetFragment()
            fragment.arguments = args
            return fragment
        }

        fun newInstance(ancientItem: Ancient?): BottomSheetFragment {
            val args = Bundle()
            args.putParcelable(CLASS_ITEM_BUNDLE, ancientItem)
            val fragment = BottomSheetFragment()
            fragment.arguments = args
            return fragment
        }
    }
}