package hu.bme.aut.android.todo.feature.list

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.DialogFragment
import hu.bme.aut.android.todo.R
import hu.bme.aut.android.todo.model.Todo
import kotlinx.android.synthetic.main.fragment_create.*

class TodoCreateFragment : DialogFragment(),
    DatePickerDialogFragment.DateListener {

    private lateinit var listener: TodoCreatedListener

    override fun onAttach(context: Context) {
        super.onAttach(context)

        try {
            listener = if (targetFragment != null) {
                targetFragment as TodoCreatedListener
            } else {
                activity as TodoCreatedListener
            }
        } catch (e: ClassCastException) {
            throw RuntimeException(e)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_create, container, false)
        dialog?.setTitle(R.string.itemCreateTodo)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        spnrTodoPriority.adapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_item,
                listOf("Low", "Medium", "High")
        )
        tvTodoDueDate.text = "  -  "
        tvTodoDueDate.setOnClickListener { showDatePickerDialog() }

        btnCreateTodo.setOnClickListener {
            val selectedPriority = when (spnrTodoPriority.selectedItemPosition) {
                0 -> Todo.Priority.LOW
                1 -> Todo.Priority.MEDIUM
                2 -> Todo.Priority.HIGH
                else -> Todo.Priority.LOW
            }

            listener.onTodoCreated(Todo(
                    title = etTodoTitle.text.toString(),
                    priority = selectedPriority,
                    dueDate = tvTodoDueDate.text.toString(),
                    description = etTodoDescription.text.toString()
            ))
            dismiss()
        }

        btnCancelCreateTodo.setOnClickListener {
            dismiss()
        }

    }

    private fun showDatePickerDialog() {
        val datePicker = DatePickerDialogFragment()
        datePicker.setTargetFragment(this, 0)
        datePicker.show(requireFragmentManager(), DatePickerDialogFragment.TAG)
    }

    override fun onDateSelected(date: String) {
        tvTodoDueDate.text = date
    }

    interface TodoCreatedListener {
        fun onTodoCreated(todo: Todo)
    }

}