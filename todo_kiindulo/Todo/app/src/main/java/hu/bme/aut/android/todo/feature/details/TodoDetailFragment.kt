package hu.bme.aut.android.todo.feature.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import hu.bme.aut.android.todo.R
import hu.bme.aut.android.todo.model.Todo
import kotlinx.android.synthetic.main.todo_detail.*

/**
 * A fragment representing a single Todo detail screen.
 * This fragment is either contained in a [TodoListActivity]
 * in two-pane mode (on tablets) or a [TodoDetailActivity]
 * on handsets.
 */
class TodoDetailFragment : Fragment() {

    private var selectedTodo: Todo? = null

    companion object {

        private const val KEY_TODO_DESCRIPTION = "KEY_TODO_DESCRIPTION"

        fun newInstance(todoDesc: String): TodoDetailFragment {
            val args = Bundle()
            args.putString(KEY_TODO_DESCRIPTION, todoDesc)

            val result = TodoDetailFragment()
            result.arguments = args
            return result
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let { args ->
            selectedTodo = Todo(
                title = "cim",
                priority = Todo.Priority.LOW,
                dueDate = "1987.23.12",
                description = args.getString(KEY_TODO_DESCRIPTION) ?: ""
            )
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.todo_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tvTodoDetail.text = selectedTodo?.description
    }

}