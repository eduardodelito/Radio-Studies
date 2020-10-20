package com.radiostudies.main.ui.fragment

import android.content.Context
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import com.radiostudies.main.common.fragment.BaseFragment
import com.radiostudies.main.ui.fragment.databinding.DiaryDetailsFragmentBinding
import com.radiostudies.main.ui.model.diary.DiaryModel
import com.radiostudies.main.ui.viewmodel.DiaryDetailsViewModel
import javax.inject.Inject


class DiaryDetailsFragment : BaseFragment<DiaryDetailsFragmentBinding, DiaryDetailsViewModel>() {

    private var listener: DiaryDetailsFragmentListener? = null

    @Inject
    override lateinit var viewModel: DiaryDetailsViewModel

    override fun createLayout() = R.layout.diary_details_fragment

    override fun getBindingVariable() = BR.diaryDetailsViewModel

    override fun initViews() {
        listener?.showAppBar(true)
        setHasOptionsMenu(true)
        val diaryModel = arguments?.getSerializable(DiaryFragment.DIARY_ITEM) as DiaryModel?
        updateDetails(diaryModel)
    }

    override fun subscribeUi() {

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_diary, menu);
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.add_menu -> {
                listener?.navigateToAddDiaryScreen(requireView())
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

//    private fun addDetailsItem() {
//        val view = LayoutInflater.from(context).inflate(R.layout.details_item_diary, null)
//        val params = LinearLayout.LayoutParams(
//            LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT
//        )
//        params.topMargin = 20
//        view.layoutParams = params
//        val delete = view.findViewById<ImageView>(R.id.btn_delete)
//        delete.setOnClickListener {
//            deleteDialog(view)
//        }
//
//        val layout1 = view.findViewById<LinearLayout>(R.id.details_time_of_listening_layout)
//        layout1.visibility = View.VISIBLE
//        val tv1 = AppCompatTextView(context)
//        TextViewCompat.setTextAppearance(tv1, R.style.AvenirHeavy_Black);
//        tv1.layoutParams = params
//        tv1.text = "Testing"
//
//        val tv1a = AppCompatTextView(context)
//        TextViewCompat.setTextAppearance(tv1a, R.style.AvenirHeavy_Black);
//        tv1a.layoutParams = params
//        tv1a.text = "Testing"
//        layout1.addView(tv1)
//        layout1.addView(tv1a)
//
//        val layout2 = view.findViewById<LinearLayout>(R.id.details_radio_stations_layout)
//        layout2.visibility = View.VISIBLE
//        val tv2 = AppCompatTextView(context)
//        TextViewCompat.setTextAppearance(tv2, R.style.AvenirHeavy_Black);
//        tv2.layoutParams = params
//        tv2.text = "Testing"
//        layout2.addView(tv2)
//
//        val layout3 = view.findViewById<LinearLayout>(R.id.details_place_of_listening_layout)
//        layout3.visibility = View.VISIBLE
//        val tv3 = AppCompatTextView(context)
//        TextViewCompat.setTextAppearance(tv3, R.style.AvenirHeavy_Black);
//        tv3.layoutParams = params
//        tv3.text = "Testing"
//        layout3.addView(tv3)
//
//        val layout4 = view.findViewById<LinearLayout>(R.id.details_device_layout)
//        layout4.visibility = View.VISIBLE
//        val tv4 = AppCompatTextView(context)
//        TextViewCompat.setTextAppearance(tv4, R.style.AvenirHeavy_Black);
//        tv4.layoutParams = params
//        tv4.text = "Testing"
//        layout4.addView(tv4)
//
//        details_items_layout.addView(view)
//    }

    private fun updateDetails(mDiaryModel: DiaryModel?) {
        with(viewModel) {
            diaryModel = mDiaryModel
        }
        getBinding().executePendingBindings()
        getBinding().invalidateAll()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is DiaryDetailsFragmentListener) {
            listener = context
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    interface DiaryDetailsFragmentListener {
        fun showAppBar(show: Boolean)

        fun navigateToAddDiaryScreen(view: View)
    }

//    private fun deleteDialog(view: View) {
//        val builder = AlertDialog.Builder(requireContext())
//        builder.setTitle(R.string.delete_msg)
//        builder.setPositiveButton(getString(R.string.yes_label)) { dialog, _ ->
//            details_items_layout.removeView(view)
//            details_items_layout.invalidate()
//            dialog.dismiss()
//        }
//        builder.setNegativeButton(getString(R.string.no_label)) { dialog, _ ->
//            dialog.dismiss()
//        }
//        val dialog = builder.create()
//        dialog.show()
//    }

    companion object {
        fun newInstance() = DiaryDetailsFragment()
    }
}