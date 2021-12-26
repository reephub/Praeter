package com.praeter.ui.mainactivity.fragment.metttheancient

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.praeter.core.utils.PraeterNetworkManager
import com.praeter.data.local.model.Ancient
import com.praeter.databinding.FragmentMeetTheAncientBinding
import com.praeter.ui.base.BaseFragment
import timber.log.Timber
import java.util.*

class MeetTheAncientFragment : BaseFragment(), AncientClickListener {

    private var _viewBinding: FragmentMeetTheAncientBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _viewBinding!!

    /**
     * passing data between fragments
     */
    private var listener: OnMeetTheAncientItemSelectedListener? = null

    ///////////////////////////////////////////
    //
    //  Override methods
    //
    ///////////////////////////////////////////
    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = if (context is OnMeetTheAncientItemSelectedListener) {
            context
        } else {
            throw ClassCastException(
                context.toString()
                        + " must implement MainActivity.OnMeetTheAncientItemSelectedListener"
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _viewBinding = FragmentMeetTheAncientBinding.inflate(inflater, container, false)
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
            val ancientList: List<Ancient> = prepareAncientData()
            val adapter = AncientAdapter(requireContext(), ancientList, this)
            val linearLayoutManager = LinearLayoutManager(context)
            binding.rvAncients.layoutManager = linearLayoutManager
            binding.rvAncients.itemAnimator = DefaultItemAnimator()
            binding.rvAncients.adapter = adapter
        }
    }

    private fun prepareAncientData(): List<Ancient> {
        val list: MutableList<Ancient> = ArrayList<Ancient>()
        list.add(Ancient("Ancient 1 "))
        list.add(Ancient("Ancient 2 "))
        list.add(Ancient("Ancient 3 "))
        return list
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _viewBinding = null
    }

    override fun onAncientItemCLickListener(view: View?, item: Ancient, position: Int) {

        // send data to activity
        listener!!.onAncientClick(item.name, item.name)
    }

    interface OnMeetTheAncientItemSelectedListener {
        fun onAncientClick(ancientName: String?, ancientDescription: String?)
    }

    companion object {
        fun newInstance(): MeetTheAncientFragment {
            val args = Bundle()
            val fragment = MeetTheAncientFragment()
            fragment.arguments = args
            return fragment
        }
    }
}