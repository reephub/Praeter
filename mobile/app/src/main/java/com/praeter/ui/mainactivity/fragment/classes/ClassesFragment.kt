package com.praeter.ui.mainactivity.fragment.classes

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.praeter.core.utils.PraeterNetworkManager
import com.praeter.data.local.model.ClassModel
import com.praeter.databinding.FragmentClassesBinding
import com.praeter.ui.base.BaseFragment
import timber.log.Timber
import java.util.*


class ClassesFragment : BaseFragment(), ClassClickListener {

    private var _viewBinding: FragmentClassesBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _viewBinding!!

    /**
     * passing data between fragments
     */
    private var listener: OnClassItemSelectedListener? = null

    ///////////////////////////////////////////
    //
    //  Override methods
    //
    ///////////////////////////////////////////
    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = if (context is OnClassItemSelectedListener) {
            context
        } else {
            throw ClassCastException(
                context.toString()
                        + " must implement MainActivity.OnClassItemSelectedListener"
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _viewBinding = FragmentClassesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // TODO : check internet connection
        if (!PraeterNetworkManager.isConnected(requireContext())) {
            val errorMessage = "Not connected to the internet. Check your internet connection."
            Timber.e(errorMessage)
            Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
        } else {
            val classModelList: List<ClassModel> = prepareClassData()
            val adapter = ClassAdapter(
                requireContext(), classModelList, this
            )
            val linearLayoutManager = LinearLayoutManager(context)
            binding.rvClasses.layoutManager = linearLayoutManager
            binding.rvClasses.itemAnimator = DefaultItemAnimator()
            binding.rvClasses.adapter = adapter
        }
    }

    private fun prepareClassData(): List<ClassModel> {
        val list: MutableList<ClassModel> = ArrayList<ClassModel>()
        list.add(ClassModel("Cours 1 ", "Dance Class"))
        list.add(ClassModel("Cours 2 ", "Art Class"))
        list.add(ClassModel("Cours 3 ", "Gastronomic Class"))
        list.add(ClassModel("Cours 4 ", "Gastronomic Class"))
        list.add(ClassModel("Cours 5 ", "Dance Class"))
        list.add(ClassModel("Cours 6 ", "Art Class"))
        list.add(ClassModel("Cours 7 ", "Art Class"))
        return list
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _viewBinding = null
    }

    override fun onClassItemCLickListener(view: View?, item: ClassModel, position: Int) {

        // send data to activity
        listener!!.onClassClick(item.name, item.type)
    }

    interface OnClassItemSelectedListener {
        fun onClassClick(className: String?, classType: String?)
    }

    companion object {
        fun newInstance(): ClassesFragment {
            val args = Bundle()
            val fragment = ClassesFragment()
            fragment.arguments = args
            return fragment
        }
    }
}