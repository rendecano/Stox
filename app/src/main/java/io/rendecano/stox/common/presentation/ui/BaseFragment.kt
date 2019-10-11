package io.rendecano.stox.common.presentation.ui

import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import io.rendecano.stox.common.di.Injectable
import javax.inject.Inject

open class BaseFragment : Fragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

//    fun showToastMessage(message: String, type: Int) {
//        FancyToast.makeText(
//            activity,
//            message,
//            FancyToast.LENGTH_SHORT,
//            type,
//            false
//        ).show()
//    }
//
//    protected fun getCurrentDate(isAdvanced :  Boolean = false, hideDay: Boolean = false): String {
//        val now = Calendar.getInstance()
//        if (isAdvanced) {
//            now.add(Calendar.MONTH, 3)
//        }
//
//        val year = now.get(Calendar.YEAR)
//        val monthOfYear = now.get(Calendar.MONTH)
//        val dayOfMonth = now.get(Calendar.DAY_OF_MONTH)
//
//        val month = if (monthOfYear + 1 > 9) "" + (monthOfYear + 1) else "0" + (monthOfYear + 1)
//        val day = if (dayOfMonth > 9) "" + dayOfMonth else "0$dayOfMonth"
//
//        return if (hideDay) {
//            "$month-$year"
//        } else {
//            "$day-$month-$year"
//        }
//    }
//
//    protected fun setDatePickerListener(txtView: TextView, isPastDateAllowed: Boolean = false, hideDay: Boolean = false) {
//        txtView.setOnClickListener {
//            val currentDate = Calendar.getInstance()
//            val year = currentDate.get(Calendar.YEAR)
//            val month = currentDate.get(Calendar.MONTH)
//            val dayOfMonth = currentDate.get(Calendar.DAY_OF_MONTH)
//
//            val dialog = SupportedDatePickerDialog(
//                requireActivity(), R.style.SpinnerDatePickerDialogTheme,
//                object : SupportedDatePickerDialog.OnDateSetListener {
//                    override fun onDateSet(
//                        view: DatePicker,
//                        year: Int,
//                        monthOfYear: Int,
//                        dayOfMonth: Int
//                    ) {
//                        val txtMonth =
//                            if (monthOfYear + 1 > 9) "" + (monthOfYear + 1) else "0" + (monthOfYear + 1)
//                        val txtDay = if (dayOfMonth > 9) "" + dayOfMonth else "0$dayOfMonth"
//
//                        txtView.text = "$txtDay-$txtMonth-$year"
//                        if (hideDay) {
//                            txtView.text = "$txtMonth-$year"
//                        }
//                    }
//
//                }, year, month, dayOfMonth
//            )
//
//            if (txtView.text.isNotEmpty()) {
//                // day - month - year
//                val dateArr = txtView.text.split("-")
//
//                // year - month - day
//                if (hideDay) {
//                    dialog.updateDate(dateArr[1].toInt(), dateArr[0].toInt() - 1, 1)
//                } else {
//                    dialog.updateDate(dateArr[2].toInt(), dateArr[1].toInt() - 1, dateArr[0].toInt())
//                }
//            }
//
//            if (!isPastDateAllowed) {
//                dialog.datePicker.minDate = currentDate.timeInMillis
//            } else {
//                dialog.datePicker.maxDate = currentDate.timeInMillis
//            }
//
//            if (hideDay) {
//                dialog.datePicker.findViewById<View>(
//                    resources.getIdentifier(
//                        "day",
//                        "id",
//                        "android"
//                    )
//                ).visibility =
//                    View.GONE
//            }
//
//
//            dialog.show()
//        }
//    }
//
//    fun showStopDialog() {
//        val pDialog = PrettyDialog(activity)
//        pDialog.setCancelable(false)
//        pDialog.setTitle(R.string.stop.stringify())
//            .setMessage(R.string.stop_message.stringify())
//            .setIcon(R.drawable.pdlg_icon_close)
//            .setIconTint(R.color.pdlg_color_red)
//            .addButton(
//                R.string.exit.stringify(),
//                R.color.pdlg_color_white,
//                R.color.pdlg_color_red
//            ) {
//                findNavController().popBackStack(R.id.homeFragment, false)
//                pDialog.dismiss()
//            }
//            .setAnimationEnabled(true)
//            .setTypeface(ResourcesCompat.getFont(activity!!, R.font.renner_medium))
//
//        pDialog.show()
//    }
//
//    fun addTextChangedListener(e: EditText, t: TextInputLayout) {
//        e.addTextChangedListener(object : TextWatcher {
//            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
//
//            }
//
//            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
//                if (s.isNotEmpty()) {
//                    if (!TextUtils.isEmpty(t.error)) {
//                        t.error = null
//                        t.isErrorEnabled = false
//                    }
//                }
//            }
//
//            override fun afterTextChanged(s: Editable) {
//
//            }
//        })
//    }
//
//    fun File.writeBitmap(bitmap: Bitmap, format: Bitmap.CompressFormat, quality: Int) {
//        outputStream().use { out ->
//            bitmap.compress(format, quality, out)
//            out.flush()
//        }
//    }

    fun Int.stringify(): String = getString(this)

    fun <T> LiveData<T>.observe(observe: (T?) -> Unit) =
            observe(activity!!, Observer { observe(it) })
}