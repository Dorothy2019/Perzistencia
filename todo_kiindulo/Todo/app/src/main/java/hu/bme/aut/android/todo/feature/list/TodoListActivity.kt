package hu.bme.aut.android.todo.feature.list

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import com.google.android.material.snackbar.Snackbar
import hu.bme.aut.android.todo.R
import hu.bme.aut.android.todo.adapter.SimpleItemRecyclerViewAdapter
import hu.bme.aut.android.todo.feature.details.TodoDetailActivity
import hu.bme.aut.android.todo.feature.details.TodoDetailFragment
import hu.bme.aut.android.todo.model.Todo
import hu.bme.aut.android.todo.viewmodel.TodoViewModel
import kotlinx.android.synthetic.main.activity_todo_list.*
import kotlinx.android.synthetic.main.todo_list.*

/**
 * An activity representing a list of Pings. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a [TodoDetailActivity] representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
class TodoListActivity :
    AppCompatActivity(),
    TodoCreateFragment.TodoCreatedListener,
    SimpleItemRecyclerViewAdapter.TodoItemClickListener {

    private lateinit var simpleItemRecyclerViewAdapter: SimpleItemRecyclerViewAdapter

    private lateinit var todoViewModel: TodoViewModel

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private var twoPane: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_todo_list)

        setSupportActionBar(toolbar)
        toolbar.title = title

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

        if (todo_detail_container != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            twoPane = true
        }

        setupRecyclerView()

        todoViewModel = ViewModelProvider(this).get(TodoViewModel::class.java)
        todoViewModel.allTodos.observe(this) { todos ->
            simpleItemRecyclerViewAdapter.addAll(todos)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_list, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.itemCreateTodo) {
            val todoCreateFragment = TodoCreateFragment()
            todoCreateFragment.show(supportFragmentManager, "TAG")
        }
        if (item.itemId == R.id.itemDeleteAllTodo){
            todoViewModel.deleteAll()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupRecyclerView() {

        simpleItemRecyclerViewAdapter = SimpleItemRecyclerViewAdapter()
        simpleItemRecyclerViewAdapter.itemClickListener = this
        rvTodoList.adapter = simpleItemRecyclerViewAdapter
    }


    override fun onItemClick(todo: Todo) {
        if (twoPane) {
            val fragment = TodoDetailFragment.newInstance(todo.description)
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.todo_detail_container, fragment)
                .commit()
        } else {
            val intent = Intent(this, TodoDetailActivity::class.java)
            intent.putExtra(TodoDetailActivity.KEY_DESC, todo.description)
            startActivity(intent)
        }
    }

    override fun onItemLongClick(position: Int, view: View, todo: Todo): Boolean {
        val popup = PopupMenu(this, view)
        popup.inflate(R.menu.menu_todo)
        popup.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.delete ->{
                    todoViewModel.delete(todo)
                    return@setOnMenuItemClickListener  true
                }

            }
            false
        }
        popup.show()
        return false
    }

    override fun onTodoCreated(todo: Todo) {
        todoViewModel.insert(todo)
    }

}
